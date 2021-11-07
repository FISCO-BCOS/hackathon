
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <windows.h>
typedef int (* PrintFunc)(const char * );
typedef	void *	(* FN_create)(int param,int * retval);
typedef	int	(* FN_create_pp)(int param,void ** pp);
typedef	int 	(* FN_call)(const void * obj,const char *input);
typedef	void 	(* FN_release)(void *obj);	

int main(int argc, char **argv)
{   
	
	printf("try load module\n");
	//HMODULE module = LoadLibrary("native_lib.dll");
	HMODULE module  = LoadLibraryEx("./native_lib.dll",NULL, LOAD_WITH_ALTERED_SEARCH_PATH);
	if (module == NULL)
	{
		printf("load module fail\n");
		printf("last error %d\n",GetLastError());
		
		return -1;
	}
	printf("after load module =%ld\n",module);
	fflush(stdout);

	PrintFunc pf=(PrintFunc)GetProcAddress(module, "testprint");
	printf("load func %ld\n",pf);
	fflush(stdout);
	if (pf!=NULL)
	{
		int r=pf("this is a test\n");
		printf("dll return %d\n",r);
	}
	FN_create fncreate = (FN_create)GetProcAddress(module, "create");
	FN_call fncall = (FN_call)GetProcAddress(module, "call");
	FN_release fnrelease = (FN_release)GetProcAddress(module, "release");	
	printf("create %ld,call %ld,release %ld\n",fncreate,fncall,fnrelease);
	int ret=0;
	void * p = fncreate(998,&ret);
	printf("create ret %d, pointer %ld\n",ret,p);
	ret = fncall(p,"write from pointer");
	printf("call ret %d\n",ret);
	fnrelease(p);
	p=NULL;
	
	printf("test [2]\n");
	FN_create_pp fn_create_pp = (FN_create_pp)GetProcAddress(module, "create_pp");
	void * pp = NULL;
	printf("pppp %ld\n",&pp);
	ret = fn_create_pp(1999,&pp);
	printf("create pp ret=%d, pp %ld\n",ret,pp);
	ret = fncall(pp,"pp write from pointer");
	printf("pp call ret %d\n",ret);
	fnrelease(pp);
	
	return 0;	
} 


