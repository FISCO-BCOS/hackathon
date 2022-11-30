import os
import platform
from distutils.core import Extension
from distutils.core import setup

from Cython.Build import cythonize

if "TASSL" in os.environ:
    TASSL_ENV = os.environ["TASSL"]
else:
    TASSL_ENV = "./"
target_platform = "linux"
include_dir = "./cpp_linux"
platsys = platform.system()
if platsys.lower().startswith("win"):
    target_platform = "win64"
    include_dir = "./cpp_win"

print("set target platform = {} ,on system :{}\n".format(target_platform, platsys))

extension = Extension(
    "cython_tassl_sock_wrap",
    ["cython_tassl_sock_wrap.pyx"],
    include_dirs=["./", include_dir, TASSL_ENV + "/include/openssl"],
    extra_link_args=["-L./ -L" + TASSL_ENV],
    libraries=["tassl_sock_wrap"],
)

setup(
    name='cython_tassl_sock_wrap',
    version='0.2.0',
    description='use TASSL to connect fisco bcos , wrap cpp code by @cython',
    url='https://github.com/fisco-bcos/python-sdk',
    license='MIT',
    platforms=[target_platform],
    ext_modules=cythonize([extension])
)
