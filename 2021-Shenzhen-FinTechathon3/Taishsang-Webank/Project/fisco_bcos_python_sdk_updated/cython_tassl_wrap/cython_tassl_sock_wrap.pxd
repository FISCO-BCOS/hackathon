# cython: language_level=3
# Declare the class with cdef
cdef extern from "tassl_sock_wrap.h" namespace "fisco_tassl_sock_wrap":
	cdef cppclass TasslSockWrap:
		TasslSockWrap() except +

		int init(const char *ca_crt_file_,
						const char * sign_crt_file_,
						const char * sign_key_file_,
						const char * en_crt_file_,
						const char * en_key_file_
							);

		int try_connect(const char *host_,const int port_);
		int finish();
	
		void set_echo_mode(int mode);

		int send(const char * buffer,const int len);
		int recv(char *buffer,const int buffersize);