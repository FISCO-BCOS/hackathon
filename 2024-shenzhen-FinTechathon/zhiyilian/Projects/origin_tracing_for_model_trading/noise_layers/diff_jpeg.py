# From https://github.com/ChaoningZhang/Pseudo-Differentiable-JPEG/blob/master/noise_layers/diff_jpeg.py
import torch
import torch.nn as nn
# Local
from noise_layers.modules.compression import compress_jpeg
from noise_layers.modules.decompression import decompress_jpeg

def diff_round(x):
    """ Differentiable rounding function
    Input:
        x(tensor)
    Output:
        x(tensor)
    """
    return torch.round(x) + (x - torch.round(x))**3

def quality_to_factor(quality):
    """ Calculate factor corresponding to quality
    Input:
        quality(float): Quality for jpeg compression
    Output:
        factor(float): Compression factor
    """
    if quality < 50:
        quality = 5000. / quality
    else:
        quality = 200. - quality*2
    return quality / 100.

class DiffJPEG(nn.Module):
    def __init__(self, quality, differentiable=True):
        ''' Initialize the DiffJPEG layer
        Inputs:
            height(int): Original image height
            width(int): Original image width
            differentiable(bool): If true uses custom differentiable
                rounding function, if false uses standrard torch.round
            quality(float): Quality factor for jpeg compression scheme.
        '''
        super(DiffJPEG, self).__init__()
        if differentiable:
            self.rounding = diff_round
        else:
            self.rounding = torch.round
        self.factor = quality_to_factor(quality)


    def forward(self, x):
        '''
        '''
        mini, maxi = torch.min(x), torch.max(x)
        x = (x - mini) / (maxi - mini)

        height = width = x.shape[3]
        self.compress = compress_jpeg(rounding=self.rounding, factor=self.factor)
        self.decompress = decompress_jpeg(height, width, rounding=self.rounding,
                                          factor=self.factor)
        y, cb, cr = self.compress(x)
        recovered = self.decompress(y, cb, cr)

        recovered = recovered * (maxi - mini) + mini

        return recovered