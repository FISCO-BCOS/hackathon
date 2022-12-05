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
#include <stdarg.h>
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
#include <fcntl.h>
#include <arpa/inet.h>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <openssl/rand.h>
#include <openssl/ssl.h>
#include <openssl/x509v3.h>
#include <pthread.h>
#include "tassl_sock_wrap.h"

using namespace fisco_tassl_sock_wrap;


void altprint(const int echo_mode,const char *fmt,...)
{
	
	va_list arg;
	va_start(arg,fmt);
	if(echo_mode & ECHO_PRINTF ){
		//printf("PRINTF: %s\n",fmt);
		vprintf(fmt,arg);
	}
	if(echo_mode & ECHO_LOG ){ 
		//printf("LOGGING:\n");
		vprintf(fmt,arg); 
	}
	va_end(arg);
}


char * get_ssl_err_type(int err,char *err_type)
{
	if (err_type== NULL)
	{
		return NULL;
	}
	switch(err)
	{

		case SSL_ERROR_NONE  :   
			strcpy(err_type,"SSL_ERROR_NONE");
			break;
		case SSL_ERROR_SSL   :    
			strcpy(err_type,"SSL_ERROR_SSL"); 
			break;
		case SSL_ERROR_WANT_READ   : 
			strcpy(err_type,"SSL_ERROR_WANT_READ"); 
			break;		
		case SSL_ERROR_WANT_WRITE  : 
			strcpy(err_type,"SSL_ERROR_WANT_WRITE"); 
			break;		
		case SSL_ERROR_WANT_X509_LOOKUP :
			strcpy(err_type,"SSL_ERROR_WANT_X509_LOOKUP"); 
			break;		
		case SSL_ERROR_SYSCALL       : 
			strcpy(err_type,"SSL_ERROR_SYSCALL"); 
			break;				                            
		case SSL_ERROR_ZERO_RETURN   : 
			strcpy(err_type,"SSL_ERROR_ZERO_RETURN"); 
			break;		
		case SSL_ERROR_WANT_CONNECT :  
			strcpy(err_type,"SSL_ERROR_WANT_CONNECT"); 
			break;		
		case SSL_ERROR_WANT_ACCEPT  :     
			strcpy(err_type,"SSL_ERROR_WANT_ACCEPT"); 
			break;
		default:
			strcpy(err_type,"unknow");
	}
	return err_type;
}

void TasslSockWrap::print_ssl_error(int err,const char *memo)
{
	char err_type[1024]="\0";
	get_ssl_err_type(err,err_type);
	altprint(echo_mode,"[in cpp wrap -->] %s: SSL ERROR : %d -> %s\n",memo,err,err_type);
}


//-------------------------------------------------
 
TasslSockWrap::TasslSockWrap()
{
	 //printf("construct TasslSockWrap %d\n",this);

	 sock = 0;
	 is_connected = false;
	 echo_mode = ECHO_NONE;
}

TasslSockWrap::~TasslSockWrap()
{
    finish();
	
}
static int g_echo_mode;
void TasslSockWrap::set_echo_mode(int mode_)
{
	echo_mode = mode_;
	g_echo_mode = mode_;
}

bool g_is_ssl_init = false;
pthread_mutex_t lock;//创建锁

int  TasslSockWrap::init_openssl()
{
	 if( g_is_ssl_init ){
        altprint(g_echo_mode,"[in cpp wrap -->] openssl has been init ,return\n");
        return 0;
     }
	pthread_mutex_lock(&lock);
	 if( g_is_ssl_init ){
        altprint(g_echo_mode,"[in cpp wrap -->] openssl has been init ,release lock and return\n");
        pthread_mutex_unlock(&lock);
        return 0;
     }

	//printf("init 0000 this->en_crt_file %s,keyfile %s\n", en_crt_file,en_key_file);
    int retval = SSL_library_init();
    altprint(g_echo_mode ,"[in cpp wrap -->] init openssl (NOT THREAD SAFE) ret %d\n",retval);
    if (retval == 1)
	{
    	//载入所有SSL算法
	    OpenSSL_add_ssl_algorithms ();
	    //载入所有错误信息
        SSL_load_error_strings();
	    ERR_load_crypto_strings();
	    g_is_ssl_init = true;
	}
	pthread_mutex_unlock(&lock);
	return retval;
}


int TasslSockWrap::load_ca_files()
{
	
	int retval;
	//printf("load_ca_files this->en_crt_file [%s]\n", en_crt_file);
	
	//加载ca证书
	retval = SSL_CTX_load_verify_locations(ctx,ca_crt_file,NULL); 
	altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_load_verify_locations %d , %s\n",retval,ca_crt_file);
	if(retval <= 0 )
	{
		altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_load_verify_locations ERROR ");
		return -101;
	}
	
	
	//加载sdk签名证书
	
	retval = SSL_CTX_use_certificate_chain_file(ctx, sign_crt_file);
	altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_use_certificate_chain_file res =  %d,file: %s\n",retval,sign_crt_file);
	if ( retval<= 0)
	{
		ERR_print_errors_fp(stderr);
		return -102;
	}
	
    //加载sdk key
	retval = SSL_CTX_use_PrivateKey_file(ctx, sign_key_file, SSL_FILETYPE_PEM);
	altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_use_PrivateKey_file res =  %d,file: %s\n",retval,sign_key_file);
	if ( retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		return -103;
	}
	
	
	//加载sdk en加密证书 
	 
	retval = SSL_CTX_use_certificate_file(ctx, en_crt_file, SSL_FILETYPE_PEM);
	altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_use_certificate_file res =  %d,file: %s\n",retval,en_crt_file);
	if (retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		return -104;
	}
	
	//加载sdk en加密key
	retval = SSL_CTX_use_enc_PrivateKey_file(ctx, en_key_file, SSL_FILETYPE_PEM);
	altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_use_enc_PrivateKey_file res =  %d,file: %s\n",retval,en_key_file);
	if (retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		return -105;
	}
		
	//如果客户端开启SSL_CTX_set_verify，则代表客户端需要严格验证服务端的身份，所以客户端需要加载ca来验证对端。服务器端是默认开启。
	SSL_CTX_set_verify(ctx,SSL_VERIFY_PEER|SSL_VERIFY_FAIL_IF_NO_PEER_CERT,NULL);
	SSL_CTX_set_verify_depth(ctx,10); 
		
	//检查证书和key
	if (!SSL_CTX_check_private_key(ctx))
	{
		altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_check_private_key:Private key does not match the certificate public key\n");
		return -106;
	}

	
	//检查en证书和key
	if (!SSL_CTX_check_enc_private_key(ctx))
	{
		altprint(echo_mode,"[in cpp wrap -->] SSL_CTX_check_enc_private_key:Private key does not match the certificate public key\n");
		return -107;
	}
	
	return 0;
	
}

void TasslSockWrap::set_host_port(const char *host_, int port_)
{
	strcpy(host,host_);
	port = port_;
}
int TasslSockWrap::init(
				const char *ca_crt_file_,
				const char * sign_crt_file_,
				const char * sign_key_file_,
				const char * en_crt_file_,
				const char * en_key_file_
				)
{
    altprint(echo_mode,"[in cpp wrap -->] %s\n",OPENSSL_VERSION_TEXT);
    altprint(echo_mode,"[in cpp wrap -->] TasslSockWrap:init %s,%s,%s,%s,%s\n",ca_crt_file_,sign_crt_file_,sign_key_file_,en_crt_file_,en_key_file_);
	strcpy(ca_crt_file,ca_crt_file_);
	strcpy(sign_crt_file,sign_crt_file_);
	strcpy(sign_key_file,sign_key_file_);
	strcpy(en_crt_file,en_crt_file_);
	strcpy(en_key_file,en_key_file_);	
	//printf("init this->en_crt_file %s,input %s,keyfile %s\n", en_crt_file,en_crt_file_,en_key_file);	
	int retval = 0; 
	char cmdbuff[1024]="\0";


	//getcwd(cmdbuff,sizeof(cmdbuff));
	//printf("cwd %s\n",cmdbuff);
	altprint(echo_mode,"[in cpp wrap -->] TasslSockWrap init\n");
	ctx = NULL;
	ssl = NULL;
	retval = init_openssl()	;
	if (retval < 0 )
	{
		return retval;
	}
	ctx = SSL_CTX_new(TLSv1_2_client_method());
	if(ctx == NULL)
	{
		altprint(echo_mode,"[in cpp wrap -->] Error of Create SSL CTX!\n");
		ERR_print_errors_fp(stderr);
		return -2;
	}
	
	SSL_CTX_set_timeout (ctx, 5); //in seconds 
	SSL_CTX_set_mode (ctx, SSL_MODE_AUTO_RETRY);
	retval = load_ca_files();
	ssl = SSL_new (ctx);
	//altprint(echo_mode,"[in cpp wrap -->] after init ctx %d, ssl %d\n",ctx,ssl);
	return retval;
	
}


//after finish ,is need to run again,must init first
int TasslSockWrap::finish()
{

    if (ssl == NULL && ctx == NULL && sock == 0)
    {
        return 0;
	}
	altprint(echo_mode,"[in cpp wrap -->] TasslSockWrap::finish.\n");

	close_socket();
	if (ssl) {
		SSL_free(ssl);
		ssl = NULL;
	}

	fflush(stdout);
	if (ctx) {
		SSL_CTX_free(ctx);
		ctx = NULL;
	}
	return 0;
}


int TasslSockWrap::create_socket()
{
	int retval;
	/* 创建socket描述符 */
	sock = socket (AF_INET, SOCK_STREAM, 0);
	if (sock == -1)
	{
		altprint (echo_mode,"SOCKET error. \n");
		return -201;
	}
	
	int flags = fcntl(sock, F_GETFL, 0);   
	if (flags <0)
	{
		altprint(echo_mode,"create socket error : fcntl F_GETFL");
		close(sock);
		sock = 0;
		return -201;
	}
	retval = fcntl(sock, F_SETFL, flags | O_NONBLOCK);   //设置成非阻塞模式；	
	if(retval <0 )
	{
		altprint(echo_mode,"create_socket error : fcntl F_SETFL");
		close_socket();
		return -201;
	}
	return 0;
}


void TasslSockWrap::close_socket()
{
	if(sock==0)
		return ;
	shutdown(sock,SHUT_RDWR);
	close(sock);
	sock = 0;
	is_connected = false;
}

//type : 0: read,1:write,2:both
int TasslSockWrap::select_socket(int sock_fd,int type, int timeout_msec)//超时时间:毫秒
{
	char trace_msg[1024]="OK";
	int retval = 0;
	int select_result = 0;

	fd_set fdset;
	FD_ZERO(&fdset);
	FD_SET(sock_fd,&fdset);  


	struct timeval timeo;
	timeo.tv_sec = timeout_msec / 1000; 
	timeo.tv_usec = (timeout_msec % 1000) * 1000;

	fd_set *p_write_set = NULL;
	fd_set *p_read_set = NULL;
	if (type == 0 || type == 2)
	{
		p_read_set = &fdset;
	}
	if (type ==1 || type == 2 )
	{
		p_write_set = &fdset;
	}
	retval = select(sock_fd + 1, p_read_set, p_write_set, NULL, &timeo);           //事件监听
	altprint(echo_mode,"[in cpp wrap -->]select ret: %d\n",retval);
	if(retval < 0)   
	{
		//错误
		select_result = -1001;
		snprintf(trace_msg,sizeof(trace_msg),"select error %d, errono %d: %s\n",
			retval,errno,strerror(errno));
	}
	if(retval == 0) // 超时
	{
		//超时
		select_result = -1002; 
		snprintf(trace_msg,sizeof(trace_msg),"select timeout %d, errono %d: %s\n",
			retval,errno,strerror(errno));
	}
	if(retval > 0)
	{
		//将检测读事件或写时间，并不能说明成功
		if(FD_ISSET(sock_fd,&fdset))
		{
		   int sockerr = 0;
		   socklen_t len = sizeof(sockerr);
		   retval = getsockopt(sock_fd, SOL_SOCKET, SO_ERROR, &sockerr, &len) ;
		   if(retval< 0)
		   {
				  //获取信息失败
				 select_result = -1003;
				 snprintf(trace_msg,sizeof(trace_msg),"select: getsockopt %d, errono %d: %s\n",
				 retval,errno,strerror(errno));
		   }
		   if(sockerr != 0) 
		   {
				  //有错误的失败
				 select_result  = -1004;
				 snprintf(trace_msg,sizeof(trace_msg),"select: after FD_ISSET,error %d, errono %d: %s\n",
				 sockerr,errno,strerror(errno));
		   }
		}
	}	
	altprint(echo_mode,"select result %d, %s\n",select_result,trace_msg);
	return select_result;
}


int TasslSockWrap::connect_socket_async(){
	struct sockaddr_in sin;
	memset (&sin, '\0', sizeof (sin));
	int retval;
	int err;
	int connect_result = 0;
	char trace_msg[1024]="OK";
		 
	if(sock == 0)
	{
		retval = create_socket();
		if(retval < 0)
		{
			return -200;
		}
	}
	altprint(echo_mode,"sock = %d,ready to connect [%s:%d]\n",sock,host,port);
	/* 准备通信地址和端口号 */
	sin.sin_family = AF_INET;
	sin.sin_addr.s_addr = inet_addr (host); /* Server IP */
	sin.sin_port = htons (port); /* Server Port number */
	retval = connect (sock, (struct sockaddr *) &sin, sizeof (sin));
	altprint(echo_mode,"[in cpp wrap -->] socket connect %d\n",sock);
	
	if(retval < 0 )          //errno == EINPROGRESS表示正在建立链接
	{
		if (errno == EINPROGRESS)
		{
			altprint(echo_mode,"[in cpp wrap -->]connect EINPROGRESS %d\n",sock);
			connect_result = select_socket(sock,1,1000);
		}else{
			snprintf(trace_msg,sizeof(trace_msg),"connect error: %d,%d:%s\n",retval,errno,strerror(errno));
			connect_result = -201;
		}
	}//if(ret < 0 && errno == EINPROGRESS)
	altprint(echo_mode,"[in cpp wrap -->] try_connect %d, %s\n",connect_result,trace_msg);
	if (connect_result < 0)
	{
		close(sock);
		return connect_result;
	}
	return 0;	
}

int TasslSockWrap::handshake()
{
	int retval = 0;
	int sslerr = 0;
	altprint(echo_mode,"[in cpp wrap -->]handshake SSL_set_fd = %d , ssl= %d\n",sock,ssl);
	/* 为ssl设置socket  */
	if (0 >= SSL_set_fd (ssl, sock))
	{
		altprint (echo_mode,"Attach to Line fail!\n");
		close(sock);
		return -301;
	}
	altprint(echo_mode,"[in cpp wrap -->] SSL_connect = %d\n",sock); 
	retval = SSL_connect(ssl) ;
	sslerr = SSL_get_error(ssl, retval);
	char buffer[4096];
	altprint(echo_mode,"[in cpp wrap -->] SSL_connect retval = %d \n",retval);
	print_ssl_error(sslerr,"SSL_connect");
	if (sslerr == SSL_ERROR_WANT_WRITE) { //SSL需要在非阻塞socket可写时写入数据
		retval = select_socket(sock,1,1000);
		altprint(echo_mode,"[in cpp wrap -->] SSL_connect select WRITE retval = %d,SSL_ERROR_WANT_WRITE\n",retval);
	} 
	else if (sslerr == SSL_ERROR_WANT_READ) { //SSL需要在非阻塞socket可读时读入数据
		retval = select_socket(sock,0,1000);
		altprint(echo_mode,"[in cpp wrap -->] SSL_connect select READ retval = %d\n",retval);
	}
	else{ 
		//错误　
		altprint(echo_mode,"[in cpp wrap -->] SSL_connect SSL_connect retval = %d,SSL_ERROR_WANT_READ\n",retval);
		retval = -302;
	}
	
	if (retval <0)
	{
		return retval;
	}
	long verify_flag = SSL_get_verify_result(ssl);
	//altprint(echo_mode,"SSL_get_verify_result %d\n",(int)verify_flag);
	if (verify_flag != X509_V_OK)
	{	
		altprint(echo_mode,"verify_flag != X509_V_O\n");
		return -303;
	}


	retval = SSL_do_handshake(ssl); //发起握手
	sslerr = SSL_get_error(ssl, retval);
	altprint(echo_mode,"[in cpp wrap -->] SSL_do_handshake retval = %d \n");
	print_ssl_error(sslerr,"SSL_do_handshake");
	if (sslerr == SSL_ERROR_WANT_WRITE) { //SSL需要在非阻塞socket可写时写入数据
		retval = select_socket(sock,1,1000);
		altprint(echo_mode,"[in cpp wrap -->] SSL_do_handshake select WRITE retval = %d,SSL_ERROR_WANT_WRITE\n",retval);
	} 
	else if (sslerr == SSL_ERROR_WANT_READ) { //SSL需要在非阻塞socket可读时读入数据
		retval = select_socket(sock,0,1000);
		altprint(echo_mode,"[in cpp wrap -->] SSL_do_handshake select READ retval = %d,SSL_ERROR_WANT_READ\n",retval);
	}
	else{ 
		//错误　
		altprint(echo_mode,"[in cpp wrap -->] SSL_connect SSL_connect retval = %d\n",retval);
		retval = -302;
	}
	return 0;
}
 

int TasslSockWrap::try_connect(const char * host_,const int port_)
{
	altprint(echo_mode,"[in cpp wrap -->] try_connect ,ssl= %d ,target: %s:%d\n",ssl,host_,port_);
	int retval = 0;
	set_host_port(host_,port_);
	retval = connect_socket_async();
	if (retval  < 0 )
	{
		return retval;
	}
	
	//socket连接成功,开始SSL握手
	retval = handshake();
	if(retval < 0)
	{
		//握手失败
		close_socket();
		return retval;
	}
	is_connected = true;
	return 0;
}

int TasslSockWrap::send(const char * buffer,const int len)
{
	altprint(echo_mode,"[in cpp wrap -->]:send len %d \n",len);
	int retval = 0;
	int err;
	if (ssl==NULL || sock == 0)
	{
		return -1;
	}	
	retval = SSL_write(ssl, buffer, len);
	int sslerr = SSL_get_error(ssl, (int)retval);
	altprint(echo_mode,"[in cpp wrap -->]:after send  retval =  %d \n",retval);
	if(retval <= 0){
		if (sslerr == SSL_ERROR_WANT_WRITE) { //SSL需要在非阻塞socket可写时写入数据
			altprint(echo_mode,"[in cpp wrap -->] send select WRITE retval = %d,SSL_ERROR_WANT_WRITE\n",retval);
			retval = 0;
		} 
		else if (sslerr == SSL_ERROR_WANT_READ) { //SSL需要在非阻塞socket可读时读入数据
			altprint(echo_mode,"[in cpp wrap -->] send select READ retval = %d,SSL_ERROR_WANT_READ\n",retval);
			retval = 0;
		}
		else{ 
			//错误　
			altprint(echo_mode,"[in cpp wrap -->] send SSL_connect retval = %d, sslerror=%d\n",retval,sslerr);
		}
	}
	return retval;	
}

int TasslSockWrap::recv(char *buffer, const int buffersize)
{ 
	altprint(echo_mode,"[in cpp wrap -->] on recv ,buffersize %d \n",buffersize);
	int retval = 0;
	int err;
	if (ssl==NULL || sock == 0)
	{
		return -1;
	}
	retval = SSL_read(ssl,buffer,buffersize);
	int sslerr = SSL_get_error(ssl, (int)retval);
	altprint(echo_mode,"[in cpp wrap -->]:after recv  retval =  %d \n",retval);
	if(retval <= 0){
		if (sslerr == SSL_ERROR_WANT_WRITE) { //SSL需要在非阻塞socket可写时写入数据
			altprint(echo_mode,"[in cpp wrap -->] recv select WRITE retval = %d,SSL_ERROR_WANT_WRITE\n",retval);
			retval = 0;
		} 
		else if (sslerr == SSL_ERROR_WANT_READ) { //SSL需要在非阻塞socket可读时读入数据
			altprint(echo_mode,"[in cpp wrap -->] recv select READ retval = %d, SSL_ERROR_WANT_READ\n",retval);
			retval = 0;
		}
		else{ 
			//错误　
			
			altprint(echo_mode,"[in cpp wrap -->] recv retval = %d ,sslerr = %d\n",retval,sslerr);
		}
	}
	return retval;
}
