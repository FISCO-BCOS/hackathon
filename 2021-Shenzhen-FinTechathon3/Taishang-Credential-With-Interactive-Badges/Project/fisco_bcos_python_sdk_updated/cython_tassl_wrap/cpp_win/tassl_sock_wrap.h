/*
  This lib is a tls client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  This lib is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
  @author: kentzhang
  @date: 2021-03
*/
#ifndef TASSL_SOCK_WRAP
#define TASSL_SOCK_WRAP
#include <stdlib.h>
#include <openssl/ssl.h>


namespace fisco_tassl_sock_wrap{
	#define ECHO_NONE     0x0000
	#define ECHO_PRINTF   0x0001
	#define ECHO_LOG      0x0010
	
	class TasslSockWrap{
		char host[256];
		int port;
		char ca_crt_file[256];
		char sign_crt_file[256];
		char sign_key_file[256];
		char en_crt_file[256];
		char en_key_file[256];
		SSL *ssl ; 
		SSL_CTX *ctx ;
		int  load_ca_files();
		bool is_connected;
		int sock;
		int echo_mode ;
	protected:
		void print_ssl_error(int err,const char *memo);
		int  create_socket();
		void close_socket(); 
		void set_host_port(const char *host_, int port_);
		int connect_socket_async();
		int handshake();
		//type : 0: read,1:write,2:both
		int select_socket(int sock_fd,int type, int timeout_msec);//超时时间:毫秒
	public :
		TasslSockWrap();
		~TasslSockWrap(); 

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
	};
	
	
}

#endif