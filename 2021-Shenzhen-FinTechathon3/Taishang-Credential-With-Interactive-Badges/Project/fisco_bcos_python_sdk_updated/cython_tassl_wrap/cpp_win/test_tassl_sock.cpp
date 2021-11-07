/*
  This lib is a tls client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  This lib is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  @author: kentzhang
  @date: 2021-03
*/

#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <errno.h>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <openssl/rand.h>
#include <openssl/ssl.h>
#include <openssl/x509v3.h>
#include "tassl_sock_wrap.h"
#include <pthread.h> 
#include "client_tools.h"
/*采用多线程模式测试tassl_sock封装库*/

using namespace fisco_tassl_sock_wrap;

TasslSockWrap wrap;
char buff[4096];


void* send_thread( void* args )
{
	 printf("send_thread start\n");
	 char reqtext[1024] = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}";
	 char channelpack[10000]="\0";
	 int packlen = make_channel_pack(reqtext,strlen(reqtext),channelpack);
	 
	 int i=1;
	 int r=0;
	 while(1){

		 printf(">>>>SEND DATA %d\n",i++);
		 r  = wrap.send(channelpack,packlen);
		 printf(">>>>SEND DONE %d\n",r);
		 Sleep(1000);
	 }
} 
 
 
void* recv_thread( void* args )
{
    printf("recv_thread start\n");
	int i=1000;
	int r=0;
	while(1){
		 
		 char buff[4096]="\0";
		 printf("----RECV START %d\n",i++);
		 r  = wrap.recv(buff,sizeof(buff));
		 printf("!after read ret len: %d\n",r);
		 if (r > 42)
		 {
			char data[4096]="\0";
			strncpy(data,buff+42,r-42);
			printf("server return :%s\n",data);
			printf("----RECV DONE %d\n",r);
		 }
		 Sleep(1000);
	 }
} 
  

int main(int argc, char **argv)
{   
	printf("wrap test start->\n");

	int retval = 0;   
	printf("1>");
	wrap.set_echo_mode(ECHO_PRINTF);
	wrap.init("sdk/ca.crt","sdk/sdk.crt","sdk/sdk.key","","");
//	wrap.init("sdk/gmca.crt","sdk/gmsdk.crt","sdk/gmsdk.key","sdk/gmensdk.crt","sdk/gmensdk.key");
	printf("2>\n");

	printf("init ret %d\n",retval);
	char ip[1024]="127.0.0.1";
	int port=20200;
	load_host_port(ip,&port);
	retval = wrap.try_connect(ip,port);
	pthread_t  tid=0;
	pthread_create(&tid,NULL,recv_thread,NULL);
	pthread_create(&tid,NULL,send_thread,NULL);
	
	
/*
	retval = wrap.write(channelpack,packlen);
	retval = wrap.read(buff,sizeof(buff));
	printf("!after read ret len: %d\n",retval);
	char data[4096]="\0";
	strncpy(data,buff+42,retval-42);
	printf("server return :%s\n",data);
	*/
	
	Sleep(1000*10);
	
	wrap.finish();
	return 0;	
} 

