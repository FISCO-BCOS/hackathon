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
#include <netdb.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <openssl/rand.h>
#include <openssl/ssl.h>
#include <openssl/x509v3.h>
#include "tassl_sock_wrap.h"
#include <pthread.h> 

using namespace fisco_tassl_sock_wrap;

void printhex(char *str, int len)
{
	char buff[4096]="\0";
	int i;
	for(i=0;i<len;i++)
	{
		char s[10]="\0";
		sprintf(s,"%02x",(char)str[i]);
		strcat(buff,s);
	}
	printf("%s\n",buff);
}

/*
length	uint32_t	4	数据包长度，含包头和数据，大端
type	uint16_t	2	数据包类型，大端
seq	string	32	数据包序列号，32字节uuid
result	int32_t	4	错误码，大端
data	bytes	length-42	数据包体，字节流
*/
int make_channel_pack(char *data,int len,char *output)
{
		short type = 0x12;
		int headerlen = (4+2+32+4);
		int totallen = headerlen + len;
		int net_totallen = htonl(totallen);
		int pos = 0;
		memcpy(output+pos,&net_totallen,4);
		pos+=4;
		type = htons(type);
		memcpy(output+pos,&type,2);
		pos+=2;
		char seqid[256]="12345678901234567890123456789012";
		memcpy(output+pos,&seqid,32);
		pos+=32;
		int code =0;
		memcpy(output+pos,&code,4);
		pos+=4;
		memcpy(output+pos,data,len);
		pos+=len;
		printf("encode done pos = %d , datalen = %d\n",pos,len);
		printhex(output,totallen);
		int lll;
		memcpy(&lll,output,4);
		printf("check length in header : %d\n",ntohl(lll));
 
		return totallen;
}
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
		 sleep(1);
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
		 sleep(1);
	 }
} 
  

int main(int argc, char **argv)
{   
	printf("wrap test start->\n");

	int retval = 0;   
	printf("1>");
	wrap.set_echo_mode(ECHO_PRINTF);
	wrap.init("sdk/gmca.crt","sdk/gmsdk.crt","sdk/gmsdk.key","sdk/gmensdk.crt","sdk/gmensdk.key");
	printf("2>\n");

	printf("init ret %d\n",retval);
	retval = wrap.try_connect("127.0.0.1",20800);
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
	
	sleep(3);
	
	wrap.finish();
	return 0;	
} 

