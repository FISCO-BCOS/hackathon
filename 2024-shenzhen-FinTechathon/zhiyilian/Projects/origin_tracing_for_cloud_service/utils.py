from diffusers import DPMSolverMultistepScheduler,DPMSolverMultistepInverseScheduler
import torch
import copy
import numpy as np 
import random 
import matplotlib.pyplot as plt
from datasets import load_dataset
import json
from torchvision import transforms
from torch.utils.data import DataLoader, Dataset
from PIL import Image, ImageFilter,ImageEnhance
import PIL 
import cv2
from io import BytesIO

def set_random_seed(seed=0):
    torch.manual_seed(seed + 0)
    torch.cuda.manual_seed(seed + 1)
    torch.cuda.manual_seed_all(seed + 2)
    np.random.seed(seed + 3)
    torch.cuda.manual_seed_all(seed + 4)
    random.seed(seed + 5)

def get_dataset(args):
    if 'laion' in args.dataset:
        dataset = load_dataset(args.dataset)['train']
        prompt_key = 'TEXT'
    elif 'coco' in args.dataset:
        with open('../coco/meta_data.json') as f:
            dataset = json.load(f)
            dataset = dataset['annotations']
            prompt_key = 'caption'
    elif 'Gustavosta/Stable-Diffusion-Prompts' in args.dataset:
        dataset = load_dataset('../Stable-Diffusion-Prompts/data')['test']
        prompt_key = 'Prompt'
    elif 'stablediffusionDB':
        import pandas as pd

        df = pd.read_parquet('../metadata.parquet')
        json_data = df.to_json(orient='records')
        dataset=json.loads(json_data)[0:5000]
        prompt_key = 'prompt'
    else:
        dataset = load_dataset(args.dataset)['test']
        prompt_key = 'Prompt'
    return dataset, prompt_key


def transform_img(image, target_size=512):
    tform = transforms.Compose(
        [
            transforms.Resize(target_size),
            transforms.CenterCrop(target_size),
            transforms.ToTensor(),
        ]
    )
    image = tform(image)
    return 2.0 * image - 1.0

@torch.inference_mode()
def get_image_latents(pipe, image, sample=True, rng_generator=None):
        encoding_dist = pipe.vae.encode(image).latent_dist
        if sample:
            encoding = encoding_dist.sample(generator=rng_generator)
        else:
            encoding = encoding_dist.mode()
        latents = encoding * 0.13025
        return latents

def get_random_latents(pipe,args,latents=None,generator=None,batch_size=1):
        height = args.image_length
        width = args.image_length
        device = pipe._execution_device
        num_channels_latents = pipe.unet.config.in_channels
        latents = pipe.prepare_latents(
            batch_size,
            num_channels_latents,
            height,
            width,
            pipe.text_encoder.dtype,
            device,
            generator,
            latents,
        )
        return latents

class promptdataset(Dataset):
    def __init__(self, data_list , prompt_key):
        self.data = data_list
        self.prompt_key= prompt_key
    def __len__(self):
        return len(self.data)
    def __getitem__(self, index):
        return self.data[index][self.prompt_key]
    
def reverse(image,pipe,args):
    curr_scheduler = pipe.scheduler
    pipe.scheduler =DPMSolverMultistepInverseScheduler.from_pretrained(args.model_path, subfolder='scheduler')
    pipe.vae.to(torch.float32)
    img = transform_img(image,args.image_length).unsqueeze(0).to(pipe.vae.dtype).to(pipe.device)
    image_latents=get_image_latents(pipe, img, sample=False)
    image_latents=image_latents.to(pipe.unet.dtype)
    inverted_latents = pipe(prompt="", latents=image_latents, num_inference_steps=args.reverse_inference_steps, output_type="latent")
    inverted_latents = inverted_latents.images
    pipe.scheduler = curr_scheduler
    pipe.vae.to(pipe.unet.dtype)
    return inverted_latents

def compress_jpeg_to_pil(img, quality):
    output_buffer = BytesIO()
    img.save(output_buffer, format='JPEG', quality=quality)
    output_buffer.seek(0)
    return Image.open(output_buffer)


def adversarial_samples(img,batch,device,X,args):
        img_tmp=img.copy()
        for noise in [0.1,0.3,0.4,0.5]:
                for t,image in enumerate(img_tmp):
                    img1 = np.array(image, dtype=np.uint16)
                    g_noise = np.random.randn(*img1.shape).astype(np.uint8)*noise 
                    noisy_array = np.clip(img1.astype(np.uint16) + g_noise, 0, 255).astype(np.uint8)
                    img1 = Image.fromarray(noisy_array)
                    img.append(img1)
                    batch=torch.cat((batch,X[t]),dim=0).to(device)
        for compress_scale in [50,70,90]:
            for t,image in enumerate(img_tmp):
                img.append(compress_jpeg_to_pil(image, compress_scale))
                batch=torch.cat((batch,X[t]),dim=0).to(device)
        for resizescale in [0.3,0.6,0.9]:
            for t,image in enumerate(img_tmp):
                img.append(image.resize((int(args.image_length * resizescale), int(args.image_length * resizescale)), PIL.Image.BICUBIC))
                batch=torch.cat((batch,X[t]),dim=0).to(device)
        for kernelsize in [3,5,7]:
            for t,image in enumerate(img_tmp):
                blurred_array=cv2.GaussianBlur(np.array(image), (kernelsize, kernelsize), 0)
                img.append(Image.fromarray(blurred_array))
                batch=torch.cat((batch,X[t]),dim=0).to(device)
        for brightness in [1,2]:
            for t,image in enumerate(img_tmp):
                img.append(transforms.ColorJitter(brightness=brightness)(image))
                batch=torch.cat((batch,X[t]),dim=0).to(device)
        for contrast in [1,2]:
            for t,image in enumerate(img_tmp):
                enhancer = ImageEnhance.Contrast(image)
                image = enhancer.enhance(contrast)
                img.append(image)
                batch=torch.cat((batch,X[t]),dim=0).to(device)
         
        for(crop_scale,crop_ratio) in [(0.95,0.95)]:
                image = transforms.RandomResizedCrop(img1.size, scale=(crop_scale,crop_scale), ratio=(crop_ratio,crop_ratio))(image)
                img.append(image)
                batch=torch.cat((batch,X[t]),dim=0).to(device)
            
        return img,batch

def DPMSolverScheduler(path):
    scheduler=DPMSolverMultistepScheduler.from_pretrained(path, subfolder='scheduler')
    def set_step_index(self, step_index):
        self._step_index = step_index
    scheduler.set_step_index = set_step_index
    return scheduler