
//仅供测试使用的小方法
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#ifdef __WINDOWS__
#include <winsock2.h>
#endif
#ifdef __LINUX__
#include <netinet/in.h>
#endif
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

int  load_host_port(char *ip,int *port)
{
	FILE * fp = fopen("server.ini","r");
	if(fp==NULL)
		return -1;
	char sLine[1024]="\0";	
	if(fgets(sLine, 1024, fp)!=NULL)
	{
		printf("read from file: %s\n",sLine);
		fflush(stdout);
		char * wTmp = NULL;
		wTmp = strchr(sLine, ':');
		fflush(stdout);	
		if (wTmp!=NULL)
		{
			strncpy(ip,sLine,wTmp-sLine);
			//printf("strncpy %s, %d\n",ip,wTmp-sLine);	
			fflush(stdout);	
			char portstr[1024]="\0";
			strcpy(portstr,wTmp+1);
			*port = atoi(portstr);
		}
	}
	fclose(fp);	
	return 0;
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
		printf("check length in header : %d\n",(int)ntohl(lll));
 
		return totallen;
}