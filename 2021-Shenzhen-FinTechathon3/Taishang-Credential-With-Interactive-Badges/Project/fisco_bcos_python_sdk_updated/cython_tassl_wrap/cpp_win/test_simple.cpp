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
#include "client_tools.h"
using namespace fisco_tassl_sock_wrap;
TasslSockWrap wrap;
char buff[4096];
/*
采用单线程简单模式测试tassl_sock_wrap.cpp里的TasslSockWrap对象，可以编译成动态库连接方式，
以及直接内链.o的模式，以确认动态库代码可用性
*/
int main(int argc, char **argv)
{   
	printf("wrap test start->\n");
	
	int retval = 0;   
	printf("1>");
	wrap.set_echo_mode(ECHO_PRINTF);
	wrap.init("sdk/gmca.crt","sdk/gmsdk.crt","sdk/gmsdk.key","sdk/gmensdk.crt","sdk/gmensdk.key");
	printf("2>\n");

	
	printf("init ret %d\n",retval);
	char ip[1024]="127.0.0.1";
	int port=20200;
	load_host_port(ip,&port);
	retval = wrap.try_connect(ip,port);

	char reqtext[1024] = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}";
	char channelpack[10000]="\0";
	int packlen = make_channel_pack(reqtext,strlen(reqtext),channelpack);
	 	
	retval = wrap.send(channelpack,packlen);
	printf("write socket %d\n",retval);
	for (int i=0;i<10;i++)
	{
		retval = wrap.recv(buff,sizeof(buff));
		printf("!after read ret len: %d\n",retval);
		if (retval >42)
		{
			char data[4096]="\0";
			strncpy(data,buff+42,retval-42);
			printf("server return :%s\n",data);
			break;

		}
		Sleep(100);
	}
	
	
	wrap.finish();
	return 0;	
} 

