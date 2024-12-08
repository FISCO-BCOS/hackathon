import torch
import torch.nn as nn

#
class Brightness(nn.Module):
    def __init__(self, contrast):
        super(Brightness, self).__init__()
        self.contrast = contrast
        self.brightness = 0.2


    def forward(self, watermarked_img):
        self.min_value = torch.min(watermarked_img)
        self.max_value = torch.max(watermarked_img)
        noised_img = torch.clamp((self.contrast * watermarked_img + self.brightness), self.min_value.item(), self.max_value.item())

        return noised_img