/*
  This lib is a tls client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  This lib is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
  @author: kentzhang
  @date: 2021-03
*/
#ifndef NATIVE_TASSL_SSOCK_WRAP
#define NATIVE_TASSL_SSOCK_WRAP
//将tassl_sock_wrap.cpp里的TasslSockWrap类，桥接为native c代码接口
//基本逻辑就是new一个TasslSockWrap对象，把指针返回给上层，后续拿这个指针调用接口
//在mingw编译时，如果有namespace，则输出的def符号带上了其他特殊符号，所以不用namespace

#ifdef __WINDOWS__
	#define EXPORT_API __declspec(dllexport)
	#define C_API __cdecl
#endif

#ifdef __LINUX__
	#define EXPORT_API 
	#define C_API 
#endif

	extern "C" {
		EXPORT_API void * C_API ssock_create();
		EXPORT_API void  C_API ssock_release(void * p_void_ssock);
		

		EXPORT_API int  C_API ssock_init(
					void * p_void_ssock,
					const char *ca_crt_file_,
					const char * sign_crt_file_,
					const char * sign_key_file_,
					const char * en_crt_file_,
					const char * en_key_file_
					);

		 EXPORT_API int C_API ssock_try_connect(
					void * p_void_ssock,
					const char *host_,const int port_);
					
		EXPORT_API int  C_API ssock_finish(void * p_void_ssock);
		
		EXPORT_API void  C_API ssock_set_echo_mode(void * p_void_ssock,int mode);


			
		EXPORT_API int  C_API ssock_send(void * p_void_ssock,
					const char * buffer,const int len);
		EXPORT_API int  C_API ssock_recv(void * p_void_ssock,
						char *buffer,const int buffersize);
	}
	


#endif 
