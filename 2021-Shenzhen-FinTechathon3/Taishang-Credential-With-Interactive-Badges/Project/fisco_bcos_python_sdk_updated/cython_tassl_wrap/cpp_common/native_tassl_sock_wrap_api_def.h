/*
  This lib is a tls client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  This lib is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  @author: kentzhang
  @date: 2021-03
*/
/*
pure c api的函数指针类型定义，在加载dll/so时需要用到这些函数指针类型
*/
#ifndef ECHO_NONE
	#define ECHO_NONE     0x0000
	#define ECHO_PRINTF   0x0001
	#define ECHO_LOG      0x0010
#endif

#ifndef NATIVE_TASSL_SOCK_WRAP_API_DEF
#define NATIVE_TASSL_SOCK_WRAP_API_DEF 
#ifdef __GNUC__ 
__extension__
#endif
 
typedef void * (* FN_ssock_create)( );
typedef  void   (* FN_ssock_release)(void * p_void_ssock);


typedef  void   (* FN_ssock_init)(
			void * p_void_ssock,
			const char *ca_crt_file_,
			const char * sign_crt_file_,
			const char * sign_key_file_,
			const char * en_crt_file_,
			const char * en_key_file_
			);
typedef  	int  (* FN_ssock_try_connect)(
			void * p_void_ssock,
			const char *host_,const int port_);
typedef  	int   (* FN_ssock_finish)(void * p_void_ssock);
typedef  	void   (* FN_ssock_set_echo_mode)(void * p_void_ssock,int mode);
typedef  	int   (* FN_ssock_send)(void * p_void_ssock,
			const char * buffer,const int len);
typedef  	int   (* FN_ssock_recv)(void * p_void_ssock,
				char *buffer,const int buffersize);
#endif
