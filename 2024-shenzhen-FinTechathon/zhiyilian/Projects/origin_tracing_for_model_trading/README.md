Implementation of DistriMark
===
<img src="./method.png" type="application/pdf" width="600" height="400" />
Step1: Pre-training Message Encoder-Decoder:

The training objective of the message encoder is to construct a unified representation of messages and latent variables under a standard Gaussian distribution. 

```
python train_pretrain.py --secret_length 48 --steps 10000000 --kl_weight 1 --rec_weight 1 --lr 0.0005 --batchsize 64 --load_path '' --save_path './model48bit.pth'
```
 
Step2: Finetuning VAE-Decoder:

The aim is to minimize the disparity of output images between fine-tuned VAE-Decoder and original model when input contains watermarked latent variables, and maximize it for non-watermarked inputs.

```
python vae.py --secret_length 48 --lr 0.0006 --batchsize 4 --model_path '../stable-diffusion-v1-4' --steps 40000 --guidancescale 5 --checkpoint './model48bit_finetuned_backup.pth'
```

Step3: Finetuning Message Decoder:

This step aims to perform adversarial training against image processing and to mitigate the effects of diffusion inversion.
```
python finetune.py --w_seed 0 --dataset Gustavosta/Stable-Diffusion-Prompts --model_path ../stable-diffusion-2-1-base --secret_length 48 --num_inference_steps 25 --guidancescale 5 --reverse_inference_steps 25 --batchsize 8 --lr 0.0005 --steps 500
```
