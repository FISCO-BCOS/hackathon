#include <stdlib.h>
#include <stdio.h>
#include "ssock_wrap.h"
#include <string.h>
#include "clib.h"

//using namespace clibtest;
using namespace py_ssock_wrap;
namespace clib_space
{
		SSockWrap sw;
	int testprint(const char *info)
	{
		printf(info);

		char buff[256]="dll testing\n";
		int retval = 0;   
		retval = sw.init(23);
		printf("[in testprint] init ret %d\n",retval);
		retval = sw.write(buff,strlen(buff));	
		fflush(stdout);
		return 99;
	}

	void *   create(int param,int * retval)
	{
		SSockWrap *psw  =new SSockWrap();
		*retval = psw->init(param);
		printf("create pointer %ld\n",psw);
			fflush(stdout);
		return psw;
		
	}
	
	
	int  create_pp(int param,void **pp)
	{
		SSockWrap *psw  =new SSockWrap();
		int retval = psw->init(param);
		printf("create pointer %ld\n",psw);
		*pp = psw;
		fflush(stdout);
		return retval;
		
	}
	int   call(const void * obj,const char *buff)
	{
		SSockWrap *psw = (SSockWrap *)obj;
		printf("call pointer %ld\n",psw);
		fflush(stdout);
		int retval = 0;
		retval = psw->write(buff,strlen(buff));
		
		return retval;
		
	}

	void  release(void *obj)
	{
		SSockWrap *psw = (SSockWrap *)obj;
		delete psw;
	}
}