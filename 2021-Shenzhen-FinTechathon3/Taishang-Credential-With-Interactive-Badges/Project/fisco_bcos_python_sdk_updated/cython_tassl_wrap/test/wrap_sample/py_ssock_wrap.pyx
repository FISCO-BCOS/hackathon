# cython: language_level=3
# distutils: language = c++

from py_ssock_wrap cimport SSockWrap

cdef class PySSockWrap:
	cdef SSockWrap ssock  # Hold a C++ instance which we're wrapping

	def __cinit__(self):
		self.ssock =  SSockWrap()
		
	def __dealloc__(self):
		#del self.ssock
		pass
		
	def init(self):
		self.ssock.init()
		
	def shutdown(self):
		self.ssock.shutdown()
		
		
	def close(self):
		self.ssock.close()

	def write(self,buffer,len):
		print("in pyx ssock write :",buffer)
		r = self.ssock.write(buffer,len)
		return r;

	def read(self,size=4096):
		print("in pyx ssock read :",size)
		cdef char buffer[4096];
		cdef int buffersize=4096;
		r = self.ssock.read(buffer,buffersize)
		return buffer 
		