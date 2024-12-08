from typing import Callable, List, Optional, Union, Any, Dict
import copy
import numpy as np
import PIL

import torch
from diffusers import StableDiffusionPipeline
from diffusers.utils import logging, BaseOutput

logger = logging.get_logger(__name__)  # pylint: disable=invalid-name


class ModifiedStableDiffusionPipelineOutput(BaseOutput):
    images: Union[List[PIL.Image.Image], np.ndarray]
class ModifiedStableDiffusionPipeline(StableDiffusionPipeline):
    def __init__(self,
        vae,
        text_encoder,
        tokenizer,
        unet,
        scheduler,
        safety_checker,
        feature_extractor,
        image_encoder,
        requires_safety_checker: bool = True,
    ):
        super(ModifiedStableDiffusionPipeline, self).__init__(
                vae,
                text_encoder,
                tokenizer,
                unet,
                scheduler,
                safety_checker,
                feature_extractor,
                image_encoder,
                requires_safety_checker,
                )
        
    def __call__(
        self,
        prompt: Union[str, List[str]],
        height: Optional[int] = None,
        width: Optional[int] = None,
        num_inference_steps: int = 50,
        guidance_scale: float = 7.5,
        negative_prompt: Optional[Union[str, List[str]]] = None,
        num_images_per_prompt: Optional[int] = 1,
        latents: Optional[torch.FloatTensor] = None,
        output_type: Optional[str] = "pil",
    ):
        with torch.no_grad():
            # 0. Default height and width to unet
            height = height or self.unet.config.sample_size * self.vae_scale_factor
            width = width or self.unet.config.sample_size * self.vae_scale_factor
            # 2. Define call parameters
            device = self._execution_device
            # here `guidance_scale` is defined analog to the guidance weight `w` of equation (2)
            # of the Imagen paper: https://arxiv.org/pdf/2205.11487.pdf . `guidance_scale = 1`
            # corresponds to doing no classifier free guidance.
            do_classifier_free_guidance = guidance_scale > 1.0
            # 3. Encode input prompt
            text_embeddings,negative_prompt_embeds = self.encode_prompt(
                prompt, device, num_images_per_prompt, do_classifier_free_guidance
            )
            if do_classifier_free_guidance:
                text_embeddings = torch.cat([negative_prompt_embeds, text_embeddings])
                
            # 4. Prepare timesteps
            self.scheduler.set_timesteps(num_inference_steps, device=device)
            timesteps = self.scheduler.timesteps
            
            # 7. Denoising loop
            num_warmup_steps = len(timesteps) - num_inference_steps * self.scheduler.order
            # with self.progress_bar(total=num_inference_steps) as progress_bar:
            for i, t in enumerate(timesteps):
                    # expand the latents if we are doing classifier free guidance
                    latent_model_input = torch.cat([latents] * 2) if do_classifier_free_guidance else latents
                    latent_model_input = self.scheduler.scale_model_input(latent_model_input, t)

                    # predict the noise residual
                    # noise_pred = self.unet(latent_model_input, t, encoder_hidden_states=text_embeddings).sample
                    noise_pred = self.unet(latent_model_input, t, encoder_hidden_states=text_embeddings)[0]

                    # perform guidance
                    if do_classifier_free_guidance:
                        noise_pred_uncond, noise_pred_text = noise_pred.chunk(2)
                        noise_pred = noise_pred_uncond + guidance_scale * (noise_pred_text - noise_pred_uncond)

                    # compute the previous noisy sample x_t -> x_t-1
                    latents = self.scheduler.step(noise_pred, t, latents).prev_sample

        if output_type=="latent":
            image=latents
        else:
            with torch.no_grad():
            
                image=self.latent_to_img(latents)
        return ModifiedStableDiffusionPipelineOutput(images=image)
  
    def latent_to_img(self,latents):
                needs_upcasting = self.vae.dtype == torch.float16 and self.vae.config.force_upcast
                if needs_upcasting:
                    self.vae.to(torch.float32)
                latents = latents.to(next(iter(self.vae.post_quant_conv.parameters())).dtype)
                has_latents_mean = hasattr(self.vae.config, "latents_mean") and self.vae.config.latents_mean is not None
                has_latents_std = hasattr(self.vae.config, "latents_std") and self.vae.config.latents_std is not None
                if has_latents_mean and has_latents_std:
                    latents_mean = (
                        torch.tensor(self.vae.config.latents_mean).view(1, 4, 1, 1).to(latents.device, latents.dtype)
                    )
                    latents_std = (
                        torch.tensor(self.vae.config.latents_std).view(1, 4, 1, 1).to(latents.device, latents.dtype)
                    )
                    latents = latents * latents_std / self.vae.config.scaling_factor + latents_mean
                else:
                    latents = latents / self.vae.config.scaling_factor
                image = self.vae.decode(latents, return_dict=False)[0]
                if needs_upcasting:
                    self.vae.to(dtype=torch.float16)
                image = self.image_processor.postprocess(image, output_type='pil')
                return image
        
    def decode_image(self, latents: torch.FloatTensor, **kwargs):
        scaled_latents = 1 / 0.18215 * latents
        image = [
            self.vae.decode(scaled_latents[i : i + 1]).sample for i in range(len(latents))
        ]
        image = torch.cat(image, dim=0)
        return image

    def get_image_latents(self, image, sample=True, rng_generator=None):
        encoding_dist = self.vae.encode(image).latent_dist
        if sample:
            encoding = encoding_dist.sample(generator=rng_generator)
        else:
            encoding = encoding_dist.mode()
        latents = encoding * 0.18215
        return latents