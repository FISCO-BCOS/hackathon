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
#include <stdlib.h>
#include <string.h>
#include <windows.h>
#include "client_tools.h"
#include "native_tassl_sock_wrap_api_def.h"

	#define ECHO_NONE     0x0000
	#define ECHO_PRINTF   0x0001
	#define ECHO_LOG      0x0010
	
//调用native c dll库，进行测试

int main(int argc, char **argv)
{   
	
	printf("try load module\n");
	char dllname[]="./native_tassl_sock_wrap.dll";
	HMODULE module  = LoadLibraryEx(dllname,NULL, LOAD_WITH_ALTERED_SEARCH_PATH);
	if (module == NULL)
	{
		printf("load module fail\n");
		printf("last error %ld\n",GetLastError());
		//printf("last error %s\n",GetLastErrorMessage());
		
		return -1;
	}

	FN_ssock_create 		pfn_create=(FN_ssock_create)GetProcAddress(module, "ssock_create");
	FN_ssock_release 		pfn_release = (FN_ssock_release)GetProcAddress(module, "ssock_release");
	FN_ssock_init 			pfn_init = (FN_ssock_init)GetProcAddress(module, "ssock_init");
	FN_ssock_finish 		pfn_finish = (FN_ssock_finish)GetProcAddress(module, "ssock_finish");
	FN_ssock_try_connect 	pfn_try_connect= (FN_ssock_try_connect)GetProcAddress(module, "ssock_try_connect");
	FN_ssock_set_echo_mode 	pfn_set_echo_mode = (FN_ssock_set_echo_mode)GetProcAddress(module, "ssock_set_echo_mode");
	FN_ssock_recv 			pfn_recv = (FN_ssock_recv)GetProcAddress(module, "ssock_recv");
	FN_ssock_send 			pfn_send = (FN_ssock_send)GetProcAddress(module, "ssock_send");
	
	bool b_fn_ok = true;

	if(pfn_create==NULL){printf("pf_create is NULL\n");b_fn_ok=false;}
	if(pfn_release==NULL){printf("pfn_release is NULL\n");b_fn_ok=false;}
	if(pfn_init==NULL){printf("FN_ssock_init is NULL\n");b_fn_ok=false;}
	if(pfn_finish==NULL){printf("pfn_finish is NULL\n");b_fn_ok=false;}
	if(pfn_try_connect==NULL){printf("pfn_try_connect is NULL\n");b_fn_ok=false;}
	if(pfn_set_echo_mode==NULL){printf("pfn_set_echo_mode is NULL\n");b_fn_ok=false;}
	if(pfn_recv==NULL){printf("pfn_recv is NULL\n");b_fn_ok=false;}
	if(pfn_send==NULL){printf("pfn_send is NULL\n");b_fn_ok=false;}
	if(!b_fn_ok){
		printf("load function error\n");
		FreeLibrary (module);
		exit(-1);
	}
	
	void * p_ssock=pfn_create();
	printf("dll create return %lld\n",(long long int)p_ssock);
	int retval = 0;
	pfn_set_echo_mode(p_ssock, ECHO_PRINTF);
	pfn_init(p_ssock,"sdk/gmca.crt","sdk/gmsdk.crt","sdk/gmsdk.key","sdk/gmensdk.crt","sdk/gmensdk.key");
	//pfn_init(p_ssock,"sdk/ca.crt","sdk/sdk.crt","sdk/sdk.key","","");
	printf("ssock init ret %d\n",retval);
	char ip[1024]="127.0.0.1";
	int port = 20200;
	
	load_host_port(ip,&port);
	printf("after load : %s: %d\n",ip,port);
	
	retval = pfn_try_connect(p_ssock,ip,port);
	printf("connect %s,%d,ret %d\n",ip,port,retval);
	
	
	char reqtext[1024] = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}";
	char channelpack[10000]="\0";
	int packlen = make_channel_pack(reqtext,strlen(reqtext),channelpack);
	 	
	retval = pfn_send(p_ssock,channelpack,packlen);
	printf("write socket %d\n",retval);
	char buff[1024*100]="\0";
	for (int i=0;i<10;i++)
	{
		retval = pfn_recv(p_ssock,buff,sizeof(buff));
		printf("!after recv ret len: %d\n",retval);
		if (retval >42)
		{
			char data[4096]="\0";
			strncpy(data,buff+42,retval-42);
			printf("server return :%s\n",data);
			break;

		}
		Sleep(200);
	}
	
	pfn_finish(p_ssock);
	FreeLibrary (module);
}
