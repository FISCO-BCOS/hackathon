import torch
import torch.nn as nn


class Gaussian(nn.Module):
    def __init__(self, std, device=None):
        super(Gaussian, self).__init__()
        device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
        self.noise_level = std

    def forward(self, watermarked_image):
        self.min_value = torch.min(watermarked_image)
        self.max_value = torch.max(watermarked_image)
        ### Add gaussian noise
        gaussian = torch.randn_like(watermarked_image)
        noised_image = watermarked_image + self.noise_level * gaussian
        noised_image = noised_image.clamp(self.min_value.item(), self.max_value.item())
        return noised_image