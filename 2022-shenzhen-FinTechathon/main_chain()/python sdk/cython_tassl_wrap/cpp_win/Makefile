SYS_CFLAGS=-Wall -pedantic -g -O2
LINUX_LIBS=

all:  native_lib  test_native commit

native_lib: native_tassl_sock_wrap.dll

#common flags
CC=gcc
CPP_COMMON=../cpp_common
SYSLIBS=
LDFLAGS=$(SYSLIBS)
INCLUDES=-I$(CPP_COMMON) -I./ -I../cpp_win
CFLAGS=$(SYSCFLAGS) $(INCLUDES)
SYS_CFLAGS=-Wall -pedantic -g -O2  -D__WINDOWS__
 


tasslobj =  $(wildcard ./tassl/*.o) 
#tasslobj =
#cryptobj =  $(wildcard ./crypto/*.o) 
cryptobj = 
#for openssl & tassl
OPENSSL_DIR=$(TASSL)
OPENSSL_INCLUDES=-I$(OPENSSL_DIR)/include
OPENSSL_LIBS=-L$(OPENSSL_DIR) -L./ -lssleay32  -leay32 
#OPENSSL_LIBS=-ldl
OPENSSL_LDFLAGS=$(SYSLIBS) $(OPENSSL_LIBS)


LIB_TASSL_SOCK_WRAP_SOURCES=tassl_sock_wrap.cpp 
LIB_TASSL_SOCK_WRAP_OBJS=tassl_sock_wrap.o
LIB_TASSL_SOCK_WRAP_CFLAGS=$(CFLAGS) $(OPENSSL_INCLUDES) $(SYS_CFLAGS)  -c -fPIC
LIB_TASSL_SOCK_WRAP_LDFLAGS=-shared  -g $(OPENSSL_LDFLAGS) -lwsock32 -lstdc++ 
LIB_TASSL_SOCK_WRAP_STATIC_LDFLAGS=  -g $(OPENSSL_LDFLAGS) -lwsock32 -lstdc++ 	
DEF_LIB_FLAG=-Wl,--output-def,$(LIB_NAME).def,--out-implib,$(LIB_NAME).lib 
DEF_LIB_FLAG_NATIVE=-Wl,--output-def,native_tassl_sock_wrap.def,--out-implib,native_tassl_sock_wrap.lib \


native_tassl_sock_wrap.dll: $(LIB_TASSL_SOCK_WRAP_SOURCES) $(CPP_COMMON)/native_tassl_sock_wrap.cpp $(CPP_COMMON)/native_tassl_sock_wrap.h
	@echo "-->Build native tassl sock wrap lib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) tassl_sock_wrap.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) $(CPP_COMMON)/native_tassl_sock_wrap.cpp
	$(CC) $(DEF_LIB_FLAG_NATIVE) -Wl,-Bsymbolic $(LIB_TASSL_SOCK_WRAP_OBJS) native_tassl_sock_wrap.o -L./runtime_libs_mingw64 -Wl,-Bstatic -lssl  -lcrypto -Wl,-Bdynamic -pthread  -shared  -lgdi32 -lwsock32 -lstdc++ -o native_tassl_sock_wrap.dll
	

test_native: test_native.cpp  $(CPP_COMMON)/client_tools.h $(CPP_COMMON)/client_tools.cpp
	@echo "-->Build test_native"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c $(CPP_COMMON)/client_tools.cpp 
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c test_native.cpp 
	$(CC) test_native.o client_tools.o  -L./ -lpthread  -lwsock32 -lstdc++  -o test_native 

commit:native_tassl_sock_wrap.dll 
	@echo 
	@echo --- checking libs in runtime_libs: ---
	@cp native_tassl_sock_wrap.dll runtime_libs_mingw64
	@cd runtime_libs_mingw64;ls -lh *.dll

	
clean:
	-@rm -f tassl_sock_wrap.o
	-@rm -f libtassl_sock_wrap.dll
	-@rm -f libnative_tassl_sock_wrap.o
	-@rm -f native_tassl_sock_wrap.dll
	-@rm -f test_tassl_sock
	-@rm -f test_tassl_sock.o
	-@rm -f test_native
	-@rm -f *.o
	-@rm -f py_tassl_sock_wrap.cpython*.dll
	-@rm -rf __pycache__ 
