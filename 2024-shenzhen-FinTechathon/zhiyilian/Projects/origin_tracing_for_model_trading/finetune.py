import argparse
from tqdm.auto import tqdm
import torch
import matplotlib.pyplot as plt
from utils import *
from utils import adversarial_samples
import random
import numpy as np
import math
import os 
import scipy
import torch.nn as nn
import torch.optim as optim
from modified_stable_diffusion import ModifiedStableDiffusionPipeline
import torch.nn.functional as F
from PIL import Image,ImageFilter,ImageEnhance
import PIL 
from encoder_decoder_pretrain.watermark_model import *


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='diffusion watermark')
    parser.add_argument('--w_seed', default=0, type=int)
    parser.add_argument('--dataset', default='Gustavosta/Stable-Diffusion-Prompts')
    # parser.add_argument('--model_path', default='../stable-diffusion-2-1-base')
    parser.add_argument('--model_path', default='../stable-diffusion-v1-4')
    parser.add_argument('--image_length', default=512, type=int)
    parser.add_argument('--secret_length', default=48, type=int)
    parser.add_argument('--num_inference_steps', default=25, type=int)
    parser.add_argument('--guidancescale', default=5, type=float)
    parser.add_argument('--reverse_inference_steps', default=25, type=int)
    parser.add_argument('--batchsize', default=4, type=int)
    parser.add_argument('--lr', default=0.0004, type=float)
    parser.add_argument('--steps', default=200, type=int)
    # parser.add_argument('--checkpoint', default='./encoder_decoder_pretrain/model48bit.pth', type=str)
    # parser.add_argument('--checkpoint', default='./model48bit_finetuned.pth', type=str)
    parser.add_argument('--checkpoint', default='./model48bit_finetuned.pth', type=str)
    parser.add_argument('--save_path', default='./model48bit_finetuned.pth', type=str)
    args =parser.parse_known_args()[0]
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    torch.set_printoptions(sci_mode=False,profile='full')
    os.environ["CUDA_VISIBLE_DEVICES"] = "0"
    maxlength=150
    
    #Prompt Dataset
    dataset, prompt_key = get_dataset(args)
    dataset=promptdataset(dataset,prompt_key)

    #Load Diffusion Model
    scheduler = DPMSolverMultistepScheduler.from_pretrained(args.model_path, subfolder='scheduler')
    pipe = ModifiedStableDiffusionPipeline.from_pretrained(
            args.model_path,
            scheduler=scheduler,
            torch_dtype=torch.float16,
            revision='fp16',
            )
    pipe = pipe.to(device)

    #Load DiffuseTrace
    wm = Watermark(secret_length=args.secret_length).to(device)
    if args.checkpoint is not None: 
        wm.load_state_dict(torch.load(args.checkpoint))
        
    #freeze the batchnorm layer
    wm.eval()
    
    #generator
    # generator = torch.Generator(device=pipe.text_encoder.device)
    # generator.manual_seed(0)

    #optimizer
    optimizer = torch.optim.Adam([
        {'params': wm.decoder_projection.parameters()},
        {'params': wm.decoder.parameters()},

    ], lr=args.lr)
    progress_bar1 = tqdm(total=args.steps, desc=f'steps')

    for i in range(args.steps):
            optimizer.zero_grad()
            #secret batch
            X=[]
            for j in range(args.batchsize):
                    binary=torch.Tensor(np.random.choice([0, 1], size=(args.secret_length))).to(device)
                    binary = binary.unsqueeze(-1).unsqueeze(-1).unsqueeze(0)
                    binary = binary.expand(-1,-1,64,64)
                    X.append(binary)
            batch=torch.cat(X,dim=0).to(device)
            #watermark distribution
            _,Mean,Logvar=wm(batch)
            mean=Mean.reshape(-1,4,64,64)
            logvar=Logvar.reshape(-1,4,64,64)
            eps = torch.randn_like(logvar)
            std = torch.exp(logvar / 2)
            init_latents = eps * std + mean
            init_latents=init_latents.half()
            reverse_latents=None
            
            #inference
            with torch.no_grad():
                prompt=dataset[random.randint(0,len(dataset)-1)][0:maxlength]
                print(prompt)
                img= pipe(prompt=prompt,num_inference_steps=args.num_inference_steps,\
                latents=init_latents,guidance_scale=args.guidancescale,num_images_per_prompt=len(batch)).images

            # adversarial samples
            img,batch=adversarial_samples(img,batch,device,X,args)
                        
            #reverse：
            reverse_latents_list=[]
            for r in range(len(img)):
                latents=reverse(img[r],pipe,args).unsqueeze(0)
                reverse_latents_list.append(latents)
            reverse_latents = torch.cat(tuple(reverse_latents_list), dim=0).float()
            reverse_latents = reverse_latents.view(len(img), -1)  
            
            #decode                                                                                                                                    
            x = wm.decoder_projection(reverse_latents)
            x = torch.reshape(x, (-1, *wm.decoder_input_chw))
            
            #calculate loss
            recloss=F.mse_loss(batch,wm.decoder(x),reduction='sum')
            original_secret = torch.mean(batch, dim=(-2, -1))
            pred_secret = torch.round(torch.mean(wm.decoder(x), dim=(-2, -1)))
            pred_secret_tensor = torch.mean(wm.decoder(x)[0], dim=(-2, -1))
            loss=recloss
            loss.backward()
            optimizer.step()
            
            #loss
            print(f'max loss bits={(torch.sum(abs(original_secret-pred_secret),dim=1))}')
            print(f'max loss bits={torch.max(torch.sum(abs(original_secret-pred_secret),dim=1))}')
            
            #process bar update
            progress_bar1.set_postfix(steps=f'{i}', recloss=f'{loss:.4f}')
            progress_bar1.update(1)
            
            if i%10==0 and i>1:
                torch.save(wm.state_dict(),'model48bit_finetuned.pth')
    #save model
    torch.save(wm.state_dict(),'model48bit_finetuned.pth')
