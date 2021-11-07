#ifndef __CLIB_TEST__
#define __CLIB_TEST__
namespace clib_space
{
	extern "C" {
			__declspec(dllexport)  int __cdecl    testprint(const char *info);
			__declspec(dllexport)  void* __cdecl     create(int param,int * retval);
			__declspec(dllexport)  int __cdecl     create_pp(int param,void ** pp_obj);
			__declspec(dllexport)  int  __cdecl    call(const void * obj,const char *buff);
			__declspec(dllexport)  void  __cdecl    release(void *obj);
	}
}
#endif	
