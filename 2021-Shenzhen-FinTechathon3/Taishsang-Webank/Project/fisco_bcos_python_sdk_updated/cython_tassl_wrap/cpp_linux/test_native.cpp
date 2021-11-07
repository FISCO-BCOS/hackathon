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

#include <dlfcn.h>  
#include "client_tools.h"
#include "string.h"
#include "unistd.h"
#include "native_tassl_sock_wrap_api_def.h"
  
int main(int argc, char **argv) {  
	void *module;    
	char libname[]="libnative_tassl_sock_wrap.so";
	module = dlopen (libname, RTLD_LAZY);  
	if (!module) {  
		fprintf (stderr, "%s ", dlerror());  
		exit(1);  
	}  
		
	
	FN_ssock_create 		pfn_create=(FN_ssock_create)dlsym(module, "ssock_create");
	FN_ssock_release 		pfn_release = (FN_ssock_release)dlsym(module, "ssock_release");
	FN_ssock_init 			pfn_init = (FN_ssock_init)dlsym(module, "ssock_init");
	FN_ssock_finish 		pfn_finish = (FN_ssock_finish)dlsym(module, "ssock_finish");
	FN_ssock_try_connect 	pfn_try_connect= (FN_ssock_try_connect)dlsym(module, "ssock_try_connect");
	FN_ssock_set_echo_mode 	pfn_set_echo_mode = (FN_ssock_set_echo_mode)dlsym(module, "ssock_set_echo_mode");
	FN_ssock_recv 			pfn_recv = (FN_ssock_recv)dlsym(module, "ssock_recv");
	FN_ssock_send 			pfn_send = (FN_ssock_send)dlsym(module, "ssock_send");
	
	

	
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
		dlclose (module);
		exit(-1);
	}
	
	void * p_ssock=pfn_create();
	printf("dll create return %d\n",p_ssock);
	int retval = 0;
	pfn_set_echo_mode(p_ssock, ECHO_PRINTF);
	pfn_init(p_ssock,"sdk/gmca.crt","sdk/gmsdk.crt","sdk/gmsdk.key","sdk/gmensdk.crt","sdk/gmensdk.key");
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
		sleep(1);
	}
	
	pfn_finish(p_ssock);
	dlclose (module);
}