# cython: language_level=3
# Declare the class with cdef
cdef extern from "ssock_wrap.h" namespace "py_ssock_wrap":
	cdef cppclass SSockWrap:
		SSockWrap() except +
		int init();
		int shutdown();
		int close();
		int write(const char * buffer,const int len);
		int read(char *buffer,const int buffersize);