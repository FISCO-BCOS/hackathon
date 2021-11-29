#!/bin/sh

set -xe
rm -f *.o
rm -f *.so
gcc -fPIC -shared -g *.cpp -o liblitenote.so


CFLAGS="-I`pwd`" LDFLAGS="-L`pwd`" python setup.py build_ext --inplace

LD_LIBRARY_PATH=`pwd` python testlitenote.py 
