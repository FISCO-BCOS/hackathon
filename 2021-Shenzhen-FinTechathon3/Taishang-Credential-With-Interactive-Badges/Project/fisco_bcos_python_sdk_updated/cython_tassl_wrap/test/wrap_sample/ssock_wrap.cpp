#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include "ssock_wrap.h"

using namespace py_ssock_wrap;

 
SSockWrap::SSockWrap(void)
{
	 
}

SSockWrap::~SSockWrap(void)
{
	printf("ssockobj released %d\n",param);
	fflush(stdout);
}


int SSockWrap::init(int param_)
{
	int retval = param_+1000; 
	param = param_;
	return retval;
}


int SSockWrap::shutdown()
{
	printf("SSockWrap::shutdown!!\n");
	return 0;
}

int SSockWrap::close()
{
	printf("SSockWrap::close.\n");
	return 0;
}

int SSockWrap::write(const char * buffer,const int len)
{
	printf("[ in cpp ]param = %d,write len %d : %s\n",param,len,buffer);
	return len+100;	
}

int SSockWrap::read(char *buffer, const int buffersize)
{
	snprintf(buffer,buffersize,"[ in cpp ] >>on read ,buffersize %d ",buffersize);
	return buffersize;
}
