from typing import Tuple
import torch
import torch.nn as nn
from torch.nn.functional import conv2d


def gaussian(window_size, sigma):
    def gauss_fcn(x):
        return -(x - window_size // 2) ** 2 / float(2 * sigma ** 2)

    gauss = torch.stack([torch.exp(torch.tensor(gauss_fcn(x))) for x in range(window_size)])
    return gauss / gauss.sum()


def get_gaussian_kernel(kernel_size: int, sigma: float) -> torch.Tensor:
    r"""Function that returns Gaussian filter coefficients.
        Args:
            kernel_size (int): filter size. It should be odd and positive.
            sigma (float): gaussian standard deviation.
        Returns:
            Tensor: 1D tensor with gaussian filter coefficients.
        Shape:
            - Output: :math:`(\text{kernel_size})`
        Examples::
            >>> kornia.image.get_gaussian_kernel(3, 2.5)
            tensor([0.3243, 0.3513, 0.3243])
            >>> kornia.image.get_gaussian_kernel(5, 1.5)
            tensor([0.1201, 0.2339, 0.2921, 0.2339, 0.1201])
        """
    if not isinstance(kernel_size, int) or kernel_size % 2 == 0 or \
            kernel_size <= 0:
        raise TypeError("kernel_size must be an odd positive integer. " "Got {}".format(kernel_size))
    window_1d: torch.Tensor = gaussian(kernel_size, sigma)
    return window_1d


def get_gaussian_kernel2d(kernel_size: Tuple[int, int], sigma: Tuple[float, float]) -> torch.Tensor:
    r"""Function that returns Gaussian filter matrix coefficients.
        Args:
            kernel_size (Tuple[int, int]): filter sizes in the x and y direction.
            Sizes should be odd and positive.
            sigma (Tuple[int, int]): gaussian standard deviation in the x and y
            direction.
        Returns:
            Tensor: 2D tensor with gaussian filter matrix coefficients.
        Shape:
            - Output: :math:`(\text{kernel_size}_x, \text{kernel_size}_y)`
        Examples::
            >>> kornia.image.get_gaussian_kernel2d((3, 3), (1.5, 1.5))
            tensor([[0.0947, 0.1183, 0.0947],
                    [0.1183, 0.1478, 0.1183],
                    [0.0947, 0.1183, 0.0947]])
            >>> kornia.image.get_gaussian_kernel2d((3, 5), (1.5, 1.5))
            tensor([[0.0370, 0.0720, 0.0899, 0.0720, 0.0370],
                    [0.0462, 0.0899, 0.1123, 0.0899, 0.0462],
                    [0.0370, 0.0720, 0.0899, 0.0720, 0.0370]])
        """
    if not isinstance(kernel_size, tuple) or len(kernel_size) != 2:
        raise TypeError("kernel_size must be a tuple of length two. Got {}".format(kernel_size))
    if not isinstance(sigma, tuple) or len(sigma) != 2:
        raise TypeError("sigma must be a tuple of length two. Got {}".format(sigma))
    ksize_x, ksize_y = kernel_size
    sigma_x, sigma_y = sigma
    kernel_x: torch.Tensor = get_gaussian_kernel(ksize_x, sigma_x)
    kernel_y: torch.Tensor = get_gaussian_kernel(ksize_y, sigma_y)
    kernel_2d: torch.Tensor = torch.matmul(kernel_x.unsqueeze(-1), kernel_y.unsqueeze(-1).t())
    return kernel_2d


class GaussianBlur(nn.Module):
    r"""Creates an operator that blurs a tensor using a Gaussian filter.
        The operator smooths the given tensor with a gaussian kernel by convolving
        it to each channel. It suports batched operation.
        Arguments:
            kernel_size (Tuple[int, int]): the size of the kernel.
            sigma (Tuple[float, float]): the standard deviation of the kernel.
        Returns:
            Tensor: the blurred tensor.
        Shape:
            - Input: :math:`(B, C, H, W)`
            - Output: :math:`(B, C, H, W)`
        Examples::
            >>> input = torch.rand(2, 4, 5, 5)
            >>> gauss = kornia.filters.GaussianBlur((3, 3), (1.5, 1.5))
            >>> output = gauss(input)  # 2x4x5x5
        """

    def __init__(self, std):
        super(GaussianBlur, self).__init__()

        kernel_size = (129, 129)
        sigma = (std, std)

        self.kernel_size: Tuple[int, int] = kernel_size
        self.sigma: Tuple[float, float] = sigma
        self._padding: Tuple[int, int] = self.compute_zero_padding(kernel_size)
        self.kernel: torch.Tensor = get_gaussian_kernel2d(kernel_size, sigma)

    @staticmethod
    def compute_zero_padding(kernel_size: Tuple[int, int]) -> Tuple[int, int]:
        """Computes zero padding tuple."""
        computed = [(k - 1) // 2 for k in kernel_size]
        return computed[0], computed[1]

    def forward(self, x):  # type: ignore
        if not torch.is_tensor(x):
            raise TypeError("Input x type is not a torch.Tensor. Got {}".format(type(x)))
        if not len(x.shape) == 4:
            raise ValueError("Invalid input shape, we expect BxCxHxW. Got: {}".format(x.shape))
        # prepare kernel
        b, c, h, w = x.shape
        tmp_kernel: torch.Tensor = self.kernel.to(x.device).to(x.dtype)
        kernel: torch.Tensor = tmp_kernel.repeat(c, 1, 1, 1)

        # TODO: explore solution when using jit.trace since it raises a warning
        # because the shape is converted to a tensor instead to a int.
        # convolve tensor with gaussian kernel
        out = conv2d(x, kernel, padding=self._padding, stride=1, groups=c)

        return out