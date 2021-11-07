

ext='so'
sysname=$(shell uname -s); 



all: share_lib native_lib  test_tassl_sock test_sharelib test_static test_native
	@echo $(sysname)


share_lib: libtassl_sock_wrap.dll
native_lib: native_tassl_sock_wrap.dll

#common flags
CC=gcc
SYSLIBS=
LDFLAGS=$(SYSLIBS) 

#-Wl,-Bstatic -lstdc++ -lgcc_eh -lpthread -Wl,-Bdynamic
#-static-libstdc++ -static-libgcc -Wl,-Bstatic,--whole-archive -lwinpthread -Wl,--no-whole-archive -Wl,-Bdynamic
CPP_COMMON=../cpp_common
INCLUDES=-I$(CPP_COMMON) -I./ -I../cpp_win
CFLAGS=$(SYSCFLAGS) $(INCLUDES)
SYS_CFLAGS=-Wall -pedantic -g -O2 -D__WINDOWS__





#for openssl & tassl
OPENSSL_DIR=$(TASSL)
OPENSSL_INCLUDES=-I$(OPENSSL_DIR)/include
OPENSSL_LIBS=-L$(OPENSSL_DIR) -L./ -lssleay32  -leay32 
#-lssleay32  -leay32 
OPENSSL_LDFLAGS= $(OPENSSL_LIBS)

LIB_TASSL_SOCK_WRAP_SOURCES=tassl_sock_wrap_win.cpp 
LIB_TASSL_SOCK_WRAP_OBJS=tassl_sock_wrap_win.o
LIB_NAME=libtassl_sock_wrap
LIB_TASSL_SOCK_WRAP_CFLAGS=$(CFLAGS) $(OPENSSL_INCLUDES) $(SYS_CFLAGS) -fPIC 
LIB_TASSL_SOCK_WRAP_LDFLAGS=-shared  -g $(OPENSSL_LDFLAGS) -lwsock32 -lstdc++ 
LIB_TASSL_SOCK_WRAP_STATIC_LDFLAGS=  -g $(OPENSSL_LDFLAGS) -lwsock32 -lstdc++ 
	
DEF_LIB_FLAG=-Wl,--output-def,$(LIB_NAME).def,--out-implib,$(LIB_NAME).lib 
DEF_LIB_FLAG_NATIVE=-Wl,--output-def,native_tassl_sock_wrap.def,--out-implib,native_tassl_sock_wrap.lib \

	
	
$(LIB_NAME).dll: $(LIB_TASSL_SOCK_WRAP_SOURCES)
	@echo "-->Build tassl sock wrap lib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c tassl_sock_wrap_win.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_OBJS)  $(LIB_TASSL_SOCK_WRAP_LDFLAGS) $(DEF_LIB_FLAG)  -o $(LIB_NAME).dll

native_tassl_sock_wrap.dll: tassl_sock_wrap_win.cpp $(CPP_COMMON)/native_tassl_sock_wrap.cpp $(CPP_COMMON)/native_tassl_sock_wrap.h
	@echo "-->Build native tassl sock wrap lib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c tassl_sock_wrap_win.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c $(CPP_COMMON)/native_tassl_sock_wrap.cpp 
	$(CC) native_tassl_sock_wrap.o  $(DEF_LIB_FLAG_NATIVE)  $(LIB_TASSL_SOCK_WRAP_OBJS) $(LIB_TASSL_SOCK_WRAP_LDFLAGS)  -o native_tassl_sock_wrap.dll
		
	
TEST_TASSL_SOCK_WRAP_LDFLAGS =$(LDFLAGS) -L./ -ltassl_sock_wrap  -lwsock32 
test_tassl_sock: test_tassl_sock.cpp $(CPP_COMMON)/client_tools.cpp $(LIB_NAME).dll
	@echo "-->Build test_tassl_sock"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c $(CPP_COMMON)/client_tools.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c test_tassl_sock.cpp -o test_tassl_sock.o
	$(CC)  test_tassl_sock.o client_tools.o $(TEST_TASSL_SOCK_WRAP_LDFLAGS) -lwinpthread  -o test_tassl_sock 



test_sharelib: test_simple.cpp $(LIB_NAME).dll  $(CPP_COMMON)/client_tools.cpp
	@echo "-->Build test_sharelib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c $(CPP_COMMON)/client_tools.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) -c test_simple.cpp -o test_simple.o
	$(CC)  test_simple.o client_tools.o $(TEST_TASSL_SOCK_WRAP_LDFLAGS) -o test_sharelib
	
test_static: test_simple.cpp $(LIB_NAME).dll  $(CPP_COMMON)/client_tools.cpp
	@echo "-->Build test_static"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c $(CPP_COMMON)/client_tools.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c test_simple.cpp -o test_simple.o
	$(CC)  test_simple.o client_tools.o tassl_sock_wrap_win.o -L$(OPENSSL_DIR) $(OPENSSL_LDFLAGS) -lwsock32 -lstdc++ -o test_static

test_native: test_native.cpp $(CPP_COMMON)/client_tools.cpp
	@echo "-->Build test_native"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c test_native.cpp 
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c $(CPP_COMMON)/client_tools.cpp
	$(CC)  test_native.o  client_tools.o -lwsock32 -lstdc++ -o test_native
		

clean:
	-@rm -f $(LIB_TASSL_SOCK_WRAP_OBJS)
	-@rm -f $(LIB_NAME).dll
	-@rm -f native_tassl_sock_wrap.dll
	-@rm -f test_tassl_sock.exe
	-@rm -f test_simple.exe
	-@rm -f test_shared.exe
	-@rm -f test_static.exe
	-@rm -f *.def
	-@rm -f *.lib
	-@rm -f *.o
