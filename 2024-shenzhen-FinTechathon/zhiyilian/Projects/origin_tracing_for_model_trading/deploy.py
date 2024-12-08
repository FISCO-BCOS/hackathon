import argparse
from tqdm.auto import tqdm
import torch
import matplotlib.pyplot as plt
from utils import *
import random
import numpy as np
import math
import os 
import scipy
import torch.nn as nn
from modified_stable_diffusion import ModifiedStableDiffusionPipeline
import PIL
from PIL import Image, ImageFilter,ImageEnhance
import commpy.utilities as util
import cv2
from diffusers_ori.models import AutoencoderKL
import argparse
from tqdm.auto import tqdm
import torch
import matplotlib.pyplot as plt
from utils import *
import random
import numpy as np
import math
import os 
import scipy
import torch.nn as nn
from modified_stable_diffusion import ModifiedStableDiffusionPipeline
import PIL
from PIL import Image, ImageFilter,ImageEnhance
import commpy.utilities as util
import cv2
if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='diffusion watermark')
    parser.add_argument('--w_seed', default=0, type=int)
    # parser.add_argument('--dataset', default='Gustavosta/Stable-Diffusion-Prompts')
    parser.add_argument('--dataset', default='coco')
    # parser.add_argument('--dataset', default='stablediffusionDB')
    # parser.add_argument('--model_path', default='../stable-diffusion-2-1-base')
    parser.add_argument('--model_path', default='../stable-diffusion-v1-4')
    parser.add_argument('--image_length', default=512, type=int)
    parser.add_argument('--secret_length', default=48, type=int)
    parser.add_argument('--num_inference_steps', default=25, type=int)
    parser.add_argument('--guidancescale', default=5, type=float)
    parser.add_argument('--reverse_inference_steps', default=25, type=int)
    # parser.add_argument('--model', default='./encoder_decoder_pretrain/model48bit.pth', type=str)
    # parser.add_argument('--model', default='./model48bit_finetuned.pth', type=str)
    parser.add_argument('--model', default='./model48bit_finetuned_backup.pth', type=str)
    args =parser.parse_known_args()[0]
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    torch.set_printoptions(sci_mode=False,profile='full')
    os.environ["CUDA_VISIBLE_DEVICES"] = "0"
    maxlength=150
    
# dataset
dataset, prompt_key = get_dataset(args)
dataset=promptdataset(dataset,prompt_key)
vae = AutoencoderKL.from_pretrained('../stable-diffusion-v1-4/vae', torch_dtype=torch.float16).to(device)
#model
scheduler = DPMSolverMultistepScheduler.from_pretrained(args.model_path, subfolder='scheduler')
pipe = ModifiedStableDiffusionPipeline.from_pretrained(
        args.model_path,
        scheduler=scheduler,
        torch_dtype=torch.float16,
        revision='fp16',
        )
pipe = pipe.to(device)

#diffusetrace
from encoder_decoder_pretrain.watermark_model import *
encoder=Watermark(secret_length=args.secret_length).to(device)
if args.model !=None:
    encoder.load_state_dict(torch.load(args.model))
encoder.eval()

def showlatent(latents,channel):
      matrix = np.array(latents[0, channel].detach().cpu())
      fig, axs = plt.subplots(1, 1, figsize=(5, 5))
      im1=axs.imshow(matrix, cmap='viridis', interpolation='nearest')
      axs.axis('off')
      fig.colorbar(im1).remove()
      fig.tight_layout(pad=0, h_pad=0,rect=(0.1, 0.1, 0.9, 0.9))
      canvas = fig.canvas
      canvas.draw()
      w, h = fig.canvas.get_width_height()
      buf = np.frombuffer(fig.canvas.tostring_argb(), dtype=np.uint8)
      buf.shape = (w, h, 4)
      buf = np.roll(buf, 3, axis=2)
      pil_image = Image.frombytes("RGBA", (w, h), buf.tobytes())
      plt.close()
      return pil_image

def Watermark_Generation():
    bina = torch.Tensor(args.secret).unsqueeze(-1).unsqueeze(-1).unsqueeze(0).to(device)
    bina = bina.expand(-1,-1,64,64)
    matrix1,mean,logvar=encoder(bina)
    mean=mean.reshape(-1,4,64,64)
    logvar=logvar.reshape(-1,4,64,64)
    eps = torch.randn_like(logvar)
    std = torch.exp(logvar / 2)
    matrix = eps * std + mean
    init_latents=matrix.half()
    args.init_latents=init_latents
    rad=[]
    for i in range(4):
        rad.append(showlatent(random_latents,i))
    return rad

def Image_Generation(prompt):
    with torch.no_grad():
                        height,height = 512,512
                        do_classifier_free_guidance = args.guidancescale > 1.0
                        text_embeddings,negative_prompt_embeds = pipe.encode_prompt(
                            prompt, device, 1, do_classifier_free_guidance
                        )
                        if do_classifier_free_guidance:
                            text_embeddings = torch.cat([negative_prompt_embeds, text_embeddings])
                        pipe.scheduler.set_timesteps(args.num_inference_steps, device=device)
                        timesteps = pipe.scheduler.timesteps
                        latents=args.init_latents
                        lat=[]
                        for i, t in enumerate(timesteps):
                                latent_model_input = torch.cat([latents] * 2) if do_classifier_free_guidance else latents
                                latent_model_input = pipe.scheduler.scale_model_input(latent_model_input, t)
                                noise_pred = pipe.unet(latent_model_input, t, encoder_hidden_states=text_embeddings)[0]
                                if do_classifier_free_guidance:
                                    noise_pred_uncond, noise_pred_text = noise_pred.chunk(2)
                                    noise_pred = noise_pred_uncond + args.guidancescale * (noise_pred_text - noise_pred_uncond)
                                latents = pipe.scheduler.step(noise_pred, t, latents).prev_sample
                                tmp=(latents / 0.18215).to(next(iter(pipe.vae.post_quant_conv.parameters())).dtype)
                                tmp=vae.decode(tmp, return_dict=False)[0].to(device)
                                lat.append(pipe.image_processor.postprocess(tmp.detach(), output_type='pil')[0] )
                        latents = latents / 0.18215     
                        latents = latents.to(next(iter(pipe.vae.post_quant_conv.parameters())).dtype)
                        
                        lat_fine = vae.decode(latents, return_dict=False)[0].to(device)
                        img_fine = pipe.image_processor.postprocess(lat_fine.detach(), output_type='pil')[0] 
                        args.image=img_fine
    return img_fine,lat
    
def prompt_gen(input):
    return input

def Watermark_Generation():
    bina = torch.Tensor(args.secret).unsqueeze(-1).unsqueeze(-1).unsqueeze(0).to(device)
    bina = bina.expand(-1,-1,64,64)
    matrix1,mean,logvar=encoder(bina)
    mean=mean.reshape(-1,4,64,64)
    logvar=logvar.reshape(-1,4,64,64)
    eps = torch.randn_like(logvar)
    std = torch.exp(logvar / 2)
    matrix = eps * std + mean
    init_latents=matrix.half()
    args.init_latents=init_latents
    ret=[]
    rad=[]
    for i in range(4):
        ret.append(showlatent(init_latents,i))
    return ret



def ID_gen():
    args.secret=torch.Tensor(np.random.choice([0, 1], size=(args.secret_length))).to(device)
    return args.secret.int().tolist()

def detect():
    reverse_latents=reverse(args.image,pipe,args).float()
    reverse_latents = reverse_latents.view(1, -1)
    x = encoder.decoder_projection(reverse_latents)
    x = torch.reshape(x, (-1, *encoder.decoder_input_chw))
    secret_tmp = torch.Tensor(args.secret).unsqueeze(-1).unsqueeze(-1).unsqueeze(0).to(device)
    secret_tmp = secret_tmp.expand(-1,-1,64,64)
    average_tensor1 = torch.mean(secret_tmp, dim=(-2, -1))
    average_tensor2 = torch.round(torch.mean(encoder.decoder(x), dim=(-2, -1)))
    biterror=torch.sum(abs(average_tensor1-average_tensor2))
    bitacc=1-biterror/48
    return average_tensor2.int().tolist(),bitacc

def Image_Attack(
                  jpeg_ratio,
                  scale_ratio,
                  gauss_blur,
                  gauss_noise,
                  brightness,
                  contrast,
                  saturation_factor,
                  hue_factor,
                  
):
    img1=args.image


    if jpeg_ratio!=100:
        os.makedirs('tmp', exist_ok=True)
        img1.save(f"./tmp/tmp_{jpeg_ratio}_img1.jpg", quality=jpeg_ratio)
        img1 = Image.open(f"./tmp/tmp_{jpeg_ratio}_img1.jpg")

   
    if scale_ratio!=1:
        set_random_seed(attack_seed)        
        #获取原始图像的宽度和高度
        width, height = img1.size
        new_width = width * scale_ratio
        new_height = height 
        img1 = img1.resize((int(new_width), int(new_height)), PIL.Image.BICUBIC)
        img2 = img2.resize((int(new_width), int(new_height)), PIL.Image.BICUBIC)

    if gauss_blur!=0:
        img1 = img1.filter(ImageFilter.GaussianBlur(radius=gauss_blur))
        img2 = img2.filter(ImageFilter.GaussianBlur(radius=gauss_blur))
        
    if gauss_noise!=0:
        g_noise = (np.random.normal(0, gauss_noise, np.array(img1).shape) * 255).astype(np.uint8)
        img1 = Image.fromarray(np.clip(np.array(img1, dtype=np.int16) + g_noise, 0, 255).astype(np.uint8))
        
    if brightness!=0:
        img1 = transforms.ColorJitter(brightness=brightness)(img1)
        
    if contrast!=1:
       img1 = transforms.ColorJitter(contrast=[contrast,contrast])(img1)
       
    if saturation_factor!=1:
       img1 = transforms.ColorJitter(saturation=[saturation_factor,saturation_factor])(img1)
       
    if hue_factor!=0:
       img1 = transforms.ColorJitter(hue=[hue_factor,hue_factor])(img1)
       
    args.attacked_wm_image=img1
    return args.image, args.attacked_wm_image

import gradio as gr
block = gr.Blocks().queue()
with block:
    
        with gr.Row():
           gr.Markdown("云服务场景下的扩散模型版权保护与用户溯源")
        with gr.Column():
                user=gr.Textbox(value='', label="用户ID(分发式场景下为特定用户微调模型组件)")  
        with gr.Row():       
                ID_button = gr.Button(value="随机生成一个48bit的用户ID")        
        with gr.Row():
           with gr.Column():
               Watermark_latent = gr.Gallery(label="带水印的初始隐变量", show_label=True,
                    columns=[2], rows=[2], object_fit="contain")
               
        with gr.Row():       
                watermark_button = gr.Button(value="水印生成")
                
        with gr.Row():
                with gr.Column():
                    prompt_Input = gr.Textbox(label="提示词输入") 
                with gr.Column():
                    prompt = gr.Textbox(label="已经被确认的提示词")
        with gr.Row():
           prompt_generation_button = gr.Button(value="生成提示词")
        
        
        
        with gr.Row():
               Generated_Watermark_image = gr.Image(label="生成的图像",show_label=True,
                   interactive="True")
        with gr.Row():
               Generated_images = gr.Gallery(label="图像生成的过程", show_label=True,
                    object_fit="contain")
        with gr.Row():
                  image_generation_button = gr.Button(value="图像生成")
        
        with gr.Column():
               with gr.Accordion("Attack Settings", open=True): 
                 jpeg_ratio = gr.Slider(
                    label="JPEG Compression", minimum=0, maximum=100, value=100, step=1)
                 scale_ratio = gr.Slider(
                    label="Scale", minimum=0.05, maximum=20, value=1, step=0.05)
                 brightness = gr.Slider(
                    label="Brightness", minimum=0.1, maximum=3, value=0, step=0.1)
                 contrast = gr.Slider(
                    label="Contrast", minimum=0.1, maximum=2, value=1, step=0.1)
                 saturation_factor = gr.Slider(
                    label="Saturation_factor", minimum=0.1, maximum=2, value=1, step=0.1)
                 hue_factor = gr.Slider(
                    label="Hue_factor", minimum=-0.5, maximum=0.5, value=0, step=0.001)
                 gauss_blur = gr.Slider(
                    label="Gauss Blur", minimum=0, maximum=10, value=0, step=0.1)
                 gauss_noise = gr.Slider(
                    label="Gauss Noise Std", minimum=0, maximum=0.05, value=0, step=0.0001)
        with gr.Row():
               ori = gr.Image(label="生成的图像",show_label=True,
                   interactive="True")
               atta = gr.Image(label="攻击后的",show_label=True,
                   interactive="True")
        with gr.Row():
                 attack_button = gr.Button(value="图像处理攻击") 
        with gr.Row():
                with gr.Column():
                        msg=gr.Textbox(value='', label="解码水印信息") 
        with gr.Row():
                with gr.Column():
                        acc=gr.Textbox(value='', label="比特准确率")
        with gr.Row(): 
                  detect_button=gr.Button(value="水印解码")
        
               
        ID_button.click(fn=ID_gen,inputs=[],outputs=[user])
        watermark_button.click(fn=Watermark_Generation, inputs= [],
                               outputs=[Watermark_latent])
        prompt_generation_button.click(fn=prompt_gen,inputs=[prompt_Input],outputs=[prompt])
        image_generation_button.click(fn=Image_Generation, inputs= [prompt],\
                        outputs=[Generated_Watermark_image,Generated_images])
        attack_button.click(fn=Image_Attack, inputs= [jpeg_ratio,
                        scale_ratio,gauss_blur,gauss_noise,brightness,contrast,
                        saturation_factor,hue_factor],outputs=[ori,atta])
        
        detect_button.click(fn=detect,inputs=[],outputs=[msg,acc])
        
        
block.launch(share=True)   
        