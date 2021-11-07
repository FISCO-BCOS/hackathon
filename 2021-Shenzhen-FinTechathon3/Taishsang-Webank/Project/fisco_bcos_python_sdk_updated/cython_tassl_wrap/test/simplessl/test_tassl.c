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
#include <unistd.h>
#define CLIENT_CA_CERT  "./sdk/gmca.crt"
#define CLIENT_S_CERT   "./sdk/gmsdk.crt"
#define CLIENT_S_KEY   "./sdk/gmsdk.key"
#define CLIENT_E_CERT   "./sdk/gmensdk.crt"
#define CLIENT_E_KEY   "./sdk/gmensdk.key"
static BIO *bio_s_out = NULL;



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
		char seqid[32]="12345678901234567890123456789012";
		memcpy(output+pos,&seqid,32);
		pos+=32;
		int code =0;
		memcpy(output+pos,&code,4);
		pos+=4;
		memcpy(output+pos,data,len);
		pos+=len;
		printf("encode done pos = %d , datalen = %d\n",pos,len);
		char hexbuff[4096]="\0";
		printhex(output,totallen);
		int lll;
		memcpy(&lll,output,4);
		printf("check length in header : %d\n",ntohl(lll));
 
		return totallen;
}


void Init_OpenSSL()
{
    if (!SSL_library_init())
        exit(0);
	//载入所有SSL算法
	OpenSSL_add_ssl_algorithms ();

	//载入所有错误信息
    SSL_load_error_strings();
	ERR_load_crypto_strings();
}

int seed_prng(int bytes)
{
    if (!RAND_load_file("/dev/random", bytes))
        return 0;
    return 1; 
}


void print_ssl_error(int err,char *memo)
{
	char err_type[1024]="\0";
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
	printf("%s: SSL ERROR : %d -> %s\n",memo,err,err_type);
}


int main(int argc, char **argv)
{
	BIO *bio_conn = NULL;
	SSL *ssl = NULL;
	SSL_CTX *ctx = NULL;
	int usecert = 1;
	int retval;

	
	Init_OpenSSL();
	
	//ctx = SSL_CTX_new(CNTLS_client_method());
	ctx = SSL_CTX_new(TLSv1_2_client_method());
	if (ctx == NULL)
	{
		printf("Error of Create SSL CTX!\n");
		goto err;
	}

	

	//加载ca证书
	retval = SSL_CTX_load_verify_locations(ctx,CLIENT_CA_CERT,NULL); 
	
	printf("SSL_CTX_load_verify_locations %d , %s\n",retval,CLIENT_CA_CERT);
	if(retval <= 0 )
	{
		printf("SSL_CTX_load_verify_locations ERROR ");
		goto err;
	}
	
	/*
	//加载ca证书的另一种方式
	BIO *in = NULL, *out = NULL, *tbio = NULL, *cont = NULL;
    X509_STORE *st = NULL;
    X509 *cacert = NULL;
    // Read in CA certificate 
    tbio = BIO_new_file(CLIENT_CA_CERT, "r");
    if (!tbio)
        goto err;
    cacert = PEM_read_bio_X509(tbio, NULL, 0, NULL);
    if (!cacert)
        goto err;
	st = SSL_CTX_get_cert_store(ctx);
	retval = X509_STORE_add_cert(st, cacert);
	printf("X509_STORE_add_cert retval : %d\n",retval);
    if (!retval)
	{
		int err = SSL_get_error(ssl, (int)retval);
		printf("ERR_print_errors_fp ->");
		ERR_print_errors_fp(stderr);
		printf("<--ERR_print_errors_fp \n");
	    printf("Error SSL_connect ,ret  %d, error: %d\n",retval,err);
		printf("errno %d,ERR_get_error %d\n",errno,ERR_get_error());
        goto err;
	}
	*/
	
	//加载sdk证书
	
	retval = SSL_CTX_use_certificate_chain_file(ctx, CLIENT_S_CERT);
	//retval = SSL_CTX_use_certificate_file(ctx, CLIENT_S_CERT,SSL_FILETYPE_PEM);
	printf("SSL_CTX_use_certificate_chain_file res =  %d,file: %s\n",retval,CLIENT_S_CERT);
	if ( retval<= 0)
	{
		ERR_print_errors_fp(stderr);
		goto err;
	}
	
    //加载sdk key
	retval = SSL_CTX_use_PrivateKey_file(ctx, CLIENT_S_KEY, SSL_FILETYPE_PEM);
	printf("SSL_CTX_use_PrivateKey_file res =  %d,file: %s\n",retval,CLIENT_S_KEY);
	if ( retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		goto err;
	}
	
	
	//加载sdk en加密证书 -- 貌似无需加载也是可以的???
	 
	retval = SSL_CTX_use_certificate_file(ctx, CLIENT_E_CERT, SSL_FILETYPE_PEM);
	printf("SSL_CTX_use_certificate_file res =  %d,file: %s\n",retval,CLIENT_E_CERT);
	if (retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		goto err;
	}
	
	//加载sdk en加密key
	retval = SSL_CTX_use_enc_PrivateKey_file(ctx, CLIENT_E_KEY, SSL_FILETYPE_PEM);
	printf("SSL_CTX_use_enc_PrivateKey_file res =  %d,file: %s\n",retval,CLIENT_E_KEY);
	if (retval <= 0)
	{
		ERR_print_errors_fp(stderr);
		goto err;
	}
		
	//如果客户端开启SSL_CTX_set_verify，又没加载ca证书，节点端日志就会报SSL handshake error,message=tlsv1 alert unknown ca
	//但如果客户端注释这一句，服务器则不会校验客户端的ca证书，就让客户端连上来了
	SSL_CTX_set_verify(ctx,SSL_VERIFY_PEER|SSL_VERIFY_FAIL_IF_NO_PEER_CERT,NULL);
	SSL_CTX_set_verify_depth(ctx,10); 
		
	//检查证书和key
	if (!SSL_CTX_check_private_key(ctx))
	{
		printf("Private key does not match the certificate public key/n");
		goto err;
	}

	/*
	//检查en证书和key
	if (!SSL_CTX_check_enc_private_key(ctx))
	{
		printf("Private key does not match the certificate public key/n");
		goto err;
	}
	*/
	SSL_CTX_set_timeout (ctx, 300); 
	SSL_CTX_set_mode (ctx, SSL_MODE_AUTO_RETRY);
	
	
	ssl = SSL_new (ctx);
	int is_biomode = 0; 	
	/*BIO模式的Now Connect host:port*/
	if (is_biomode){
		
		printf("\n>>>> use BIO mode to connect server <<<<\n\n");
		
		char ipport[]="127.0.0.1:20800";
		printf("ip port :%s\n",ipport);
		bio_conn = BIO_new_connect(ipport);
		if (!bio_conn)
		{
			printf("Error Of Create Connection BIO\n");
			goto err;
		}
		BIO_set_nbio(bio_conn,0);
		if (BIO_do_connect(bio_conn) <= 0)
		{
			printf("Error Of Connect to %s\n", ipport);
			goto err;
		}
		printf("after BIO_do_connect bio_conn : %d\n",bio_conn);
		SSL_set_mode(ssl, SSL_MODE_AUTO_RETRY);
		SSL_set_bio(ssl, bio_conn, bio_conn);
		//当使用bio模式时，需要SSL_set_connect_state这一句，而使用socket模式时，加这一句就跪了
		SSL_set_connect_state(ssl);
		

	}else{
		
		int sock;
		printf("\n----- use SOCKET mode to connect server -----\n\n");
		/* 创建socket描述符 */
		sock = socket (AF_INET, SOCK_STREAM, 0);
		if (sock == -1)
		{
			printf ("SOCKET error. \n");
		}
		struct sockaddr_in sin;
		memset (&sin, '\0', sizeof (sin));
		 
		/* 准备通信地址和端口号 */
		sin.sin_family = AF_INET;
		sin.sin_addr.s_addr = inet_addr ("127.0.0.1"); /* Server IP */
		sin.sin_port = htons (20800); /* Server Port number */
		int icnn = connect (sock, (struct sockaddr *) &sin, sizeof (sin));
		if (icnn == -1)
		{
			printf ("can not connect to server,%s\n", strerror (errno));
			exit (1);
		}else{
			printf("socket connect %d\n",sock);
		}
		/* 为ssl设置socket  */
		if (0 >= SSL_set_fd (ssl, sock))
		{
			printf ("Attach to Line fail!\n");
			goto err;
		}

		retval = SSL_connect(ssl) ;
		printf("SSL_connect retval = %d\n",retval);
		if (retval<= 0)
		{
			int err = SSL_get_error(ssl, (int)retval);
			printf("ERR_print_errors_fp ->");
			ERR_print_errors_fp(stderr);
			printf("<--ERR_print_errors_fp \n");
			print_ssl_error(err,"Error SSL_connect");
			printf("errno %d,ERR_get_error %d\n",errno,ERR_get_error());
			goto err;
		}
	}
	
	long verify_flag = SSL_get_verify_result(ssl);
	printf("SSL_get_verify_result %d\n",verify_flag);
	if (verify_flag != X509_V_OK){
		printf("verify_flag != X509_V_O\n");
	} 	

	
	while (1)
	{
		//这部分居然是多余的，无需SSL_do_handshake或者BIO_do_handshake,SSL_Write也会发起握手的
		retval = SSL_do_handshake(ssl); //实测当有这一句时会发起握手
		//retval = BIO_do_handshake(bio_conn);//实测这一句绝不会发起握手
		int err = SSL_get_error(ssl, (int)retval);
		printf("BIO_do_handshake retval = %d,err=%d\n",retval,err);
		if (retval > 0)
		{
			break;
		}
		else
		{
			print_ssl_error(err,"SSL do handshake"); 
			break; 
			//goto err;
		}
	} 
	int i=0;
	/*
	SSL_METHOD *ssl_method = SSL_get_ssl_method(ssl);
	printf("ssl version : 0x%x\n",ssl_method->version);
	printf("ssl num_ciphers %d\n",ssl_method->num_ciphers());
	
	
	int num_ciphers = ssl_method->num_ciphers();
	for(i=0;i<  num_ciphers ;i++)
	{
		//SSL_CIPHER *(*get_cipher)
		SSL_CIPHER *cipher = ssl_method->get_cipher((unsigned)i);
		if(cipher)
		{
			printf("%d)  cipher valid %d ,name %s\n",i+1,cipher->valid,cipher->name);
		}
		
	}*/
	
	char reqtext[1024] = "{\"jsonrpc\":\"2.0\",\"method\":\"getClientVersion\",\"params\":[],\"id\":1}";
	//strcpy(reqtext,"{\"jsonrpc\":\"2.0\",\"method\":\"getBlockByNumber\",\"params\":[1,\"0x0\",true],\"id\":1}");
	char channelpack[4096] =  "\0";
	int packlen = make_channel_pack(reqtext,strlen(reqtext),channelpack);
	
	char readbuff[10240]="\0";
	
	for(i=0;i<1;i++)
	{
		printf(">>loop %d, ssl = %d\n",i,ssl);
		//printhex(channelpack,packlen);
		int r = 0;
		//用ssl_write/ read都ok,但用BIO模式就不行
		//r = BIO_write(bio_conn, channelpack, packlen) ;
		printf("reqtext(in channel data) : %s\n",reqtext);
		r = SSL_write(ssl, channelpack, packlen);
		printf("ssl write result %d\n",r);
		if (r>0) 
		{
			int should_retry  = 0;
			time_t t = time(NULL);
			do{
				retval = SSL_read(ssl,readbuff,sizeof(readbuff));
				//retval = BIO_read(bio_conn,readbuff,sizeof(readbuff));
				if(retval> 0)
				{
					printf("BIO_read result: %d\n",retval);
					printf("%s\n",readbuff);
					char data[2046]="";
					strncpy(data,readbuff+42,retval-42);
					printf("response data =\n %s\n",data);
					goto finish;
					break;
				}
				usleep(5000);
			}
			while(time(NULL)-t < 3);	
		}
		else
		{
			printf("error happened when sslwrite %d ,errno %d\n",r,stderr);
			ERR_print_errors_fp(stderr);
			
			int err = SSL_get_error(ssl, (int)retval);
			print_ssl_error(err,"SSL_Write");
			SSL_shutdown(ssl);
		}
		sleep(1);
	}
	
		
	printf("finish\n");
	
err:
	printf("on error\n");
finish:
	if (ssl) SSL_free(ssl);
	if (ctx) SSL_CTX_free(ctx);
	printf("app done \n");
	return 0;

}


/*

typedef struct ssl_method_st
 {
 int version; // 版本号
 int (*ssl_new)(SSL *s); // 建立新SSL
 void (*ssl_clear)(SSL *s); // 清除SSL
 void (*ssl_free)(SSL *s);  // 释放SSL
 int (*ssl_accept)(SSL *s); // 服务器接受SSL连接
 int (*ssl_connect)(SSL *s); // 客户端的SSL连接
 int (*ssl_read)(SSL *s,void *buf,int len); // SSL读
 int (*ssl_peek)(SSL *s,void *buf,int len); // SSL查看数据
 int (*ssl_write)(SSL *s,const void *buf,int len); // SSL写
 int (*ssl_shutdown)(SSL *s); // SSL半关闭
 int (*ssl_renegotiate)(SSL *s); // SSL重协商
 int (*ssl_renegotiate_check)(SSL *s); // SSL重协商检查
 long (*ssl_ctrl)(SSL *s,int cmd,long larg,void *parg); // SSL控制
 long (*ssl_ctx_ctrl)(SSL_CTX *ctx,int cmd,long larg,void *parg); //SSL上下文控制
 SSL_CIPHER *(*get_cipher_by_char)(const unsigned char *ptr); // 通过名称获取SSL的算法
 int (*put_cipher_by_char)(const SSL_CIPHER *cipher,unsigned char *ptr);
 int (*ssl_pending)(SSL *s);
 int (*num_ciphers)(void); // 算法数
 SSL_CIPHER *(*get_cipher)(unsigned ncipher); // 获取算法
 struct ssl_method_st *(*get_ssl_method)(int version);
 long (*get_timeout)(void); // 超时
 struct ssl3_enc_method *ssl3_enc; /* Extra SSLv3/TLS stuff // SSL3加密
 int (*ssl_version)(); // SSL版本
 long (*ssl_callback_ctrl)(SSL *s, int cb_id, void (*fp)()); // SSL控制回调函数
 long (*ssl_ctx_callback_ctrl)(SSL_CTX *s, int cb_id, void (*fp)()); //SSL上下文控制回调函数
 } SSL_METHOD;

*/