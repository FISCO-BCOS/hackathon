# cython: language_level=3
# distutils: language = c++
# import traceback
# traceback.print_stack()

from cpython.bytes cimport PyBytes_FromStringAndSize
from cython_tassl_sock_wrap cimport TasslSockWrap

import time


cdef class PyTasslSockWrap:
	cdef TasslSockWrap* ssock  # Hold a C++ instance which we're wrapping
 
	def __cinit__(self):
		#print("in pyx ->cinit")
		self.ssock = new TasslSockWrap()

		
	def __dealloc__(self):
		del self.ssock
		

		
	def finish(self):
		self.ssock.finish()
		  

	def init(self,
					ca_crt_file_,
					sign_crt_file_,
					sign_key_file_,
					en_crt_file_,
					en_key_file_
						):
		return self.ssock.init(
					ca_crt_file_.encode("UTF-8"),
					sign_crt_file_.encode("UTF-8"),
					sign_key_file_.encode("UTF-8"),
					en_crt_file_.encode("UTF-8"),
					en_key_file_.encode("UTF-8")
					)	
		
	def try_connect(self,host,port):
		self.ssock.try_connect(host.encode("UTF-8"),port)

	ECHO_MODE_NONE = 0
	ECHO_MODE_PRINTF  = 0x01
	ECHO_MODE_LOG = 0x10

	def set_echo_mode(self,mode):
		self.ssock.set_echo_mode(mode)

	def send(self,buffer: bytes,bufferlen=None):
		# print("{ in pyx -->} ssock send :",buffer)
		if bufferlen is  None:
			bufferlen = len(buffer)
		for i in range(0,3):
			r = self.ssock.send(buffer,bufferlen)
			if r>0:
				break
			time.sleep(0.1)
		return r;

	
	def recv(self,buffersize=None) -> bytes:
		
		buffersize = 102400 # must = char buffer[SIZE]
		cdef char buffer[102400];  
		r = self.ssock.recv(buffer,buffersize) 
		# print("{ in pyx -->} after recv ,len ", r )
		bytebuffer:bytes = b''
		if(r>0):
			bytebuffer = PyBytes_FromStringAndSize(buffer,r)
			# print(bytebuffer)
		return bytebuffer 
		
