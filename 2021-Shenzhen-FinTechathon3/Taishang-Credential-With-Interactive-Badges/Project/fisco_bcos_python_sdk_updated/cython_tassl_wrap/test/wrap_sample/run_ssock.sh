make
CFLAGS="-I`pwd`" LDFLAGS="-L`pwd`" python setup_pyssockwrap.py build_ext --inplace
python testssock.py