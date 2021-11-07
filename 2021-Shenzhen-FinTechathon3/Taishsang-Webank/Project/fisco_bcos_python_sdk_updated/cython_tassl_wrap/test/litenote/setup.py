from distutils.core import setup, Extension
from Cython.Build import cythonize


# setup(name='cython litenote app',
#      ext_modules=cythonize("litenote.pyx"))  
 
 
extension = Extension(
    "litenote",
    ["litenote.pyx"],
    libraries=["litenote"]
)

setup(
    ext_modules=cythonize([extension])
)
