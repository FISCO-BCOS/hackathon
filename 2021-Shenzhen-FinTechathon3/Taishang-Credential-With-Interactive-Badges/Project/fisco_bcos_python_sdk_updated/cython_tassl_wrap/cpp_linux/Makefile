LINUX_CFLAGS=-Wall -pedantic -g -O2
LINUX_LIBS=

all:  share_lib native_lib test_tassl_sock test_native commit

share_lib: libtassl_sock_wrap.so
native_lib: libnative_tassl_sock_wrap.so

#common flags
CC=gcc
CPP_COMMON=../cpp_common
SYSLIBS=
LDFLAGS=$(SYSLIBS)
INCLUDES=-I$(CPP_COMMON) -I./ -I../cpp_linux
CFLAGS=$(SYSCFLAGS) $(INCLUDES)
LINUX_CFLAGS=-Wall -pedantic -g -O2  -D__LINUX__
 


tasslobj =  $(wildcard ./tassl/*.o) 
#tasslobj =
#cryptobj =  $(wildcard ./crypto/*.o) 
cryptobj = 
#for openssl & tassl
OPENSSL_DIR=$(TASSL)
OPENSSL_INCLUDES=-I$(OPENSSL_DIR)/include
#OPENSSL_LIBS=-L$(OPENSSL_DIR) -lssl -lcrypto -ldl
OPENSSL_LIBS=-ldl
OPENSSL_LDFLAGS=$(SYSLIBS) $(OPENSSL_LIBS)

LIB_TASSL_SOCK_WRAP_SOURCES=tassl_sock_wrap.cpp 
LIB_TASSL_SOCK_WRAP_OBJS=tassl_sock_wrap.o
LIB_TASSL_SOCK_WRAP_CFLAGS=$(CFLAGS) $(OPENSSL_INCLUDES) $(LINUX_CFLAGS)  -c -fPIC
LIB_TASSL_SOCK_WRAP_LDFLAGS=-pthread  -shared  -Wl,-Bsymbolic $(OPENSSL_LDFLAGS) $(tasslobj) $(cryptobj)
	
libtassl_sock_wrap.so: $(LIB_TASSL_SOCK_WRAP_SOURCES)
	@echo $(sslobj)
	@echo "-->Build tassl sock wrap lib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) tassl_sock_wrap.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_OBJS) $(LIB_TASSL_SOCK_WRAP_LDFLAGS)  -o libtassl_sock_wrap.so

libnative_tassl_sock_wrap.so: $(LIB_TASSL_SOCK_WRAP_SOURCES) $(CPP_COMMON)/native_tassl_sock_wrap.cpp $(CPP_COMMON)/native_tassl_sock_wrap.h
	@echo "-->Build native tassl sock wrap lib"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) tassl_sock_wrap.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS) $(CPP_COMMON)/native_tassl_sock_wrap.cpp
	$(CC) $(LIB_TASSL_SOCK_WRAP_OBJS) native_tassl_sock_wrap.o $(LIB_TASSL_SOCK_WRAP_LDFLAGS) -lstdc++ -o libnative_tassl_sock_wrap.so
	
	
TEST_TASSL_SOCK_WRAP_LDFLAGS =-L./ -ltassl_sock_wrap -lpthread $(OPENSSL_LDFLAGS) -lstdc++
test_tassl_sock: test_tassl_sock.cpp libtassl_sock_wrap.so
	@echo "-->Build test_tassl_sock"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c test_tassl_sock.cpp -o test_tassl_sock.o
	$(CC) test_tassl_sock.o  $(TEST_TASSL_SOCK_WRAP_LDFLAGS)  -o test_tassl_sock 

test_native: test_native.cpp  $(CPP_COMMON)/client_tools.h $(CPP_COMMON)/client_tools.cpp
	@echo "-->Build test_native"
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c $(CPP_COMMON)/client_tools.cpp 
	$(CC) $(LIB_TASSL_SOCK_WRAP_CFLAGS)  -c test_native.cpp 
	$(CC) test_native.o client_tools.o $(TEST_TASSL_SOCK_WRAP_LDFLAGS)  -o test_native 

commit:libnative_tassl_sock_wrap.so libtassl_sock_wrap.so
	@echo 
	@echo --- checking libs in runtime_libs_linux: ---
	@cp libnative_tassl_sock_wrap.so runtime_libs_linux
	@cp libtassl_sock_wrap.so runtime_libs_linux
	@cd runtime_libs_linux;ls -lh *.so

	
clean:
	-@rm -f tassl_sock_wrap.o
	-@rm -f libtassl_sock_wrap.so
	-@rm -f libnative_tassl_sock_wrap.o
	-@rm -f libnative_tassl_sock_wrap.so
	-@rm -f test_tassl_sock
	-@rm -f test_tassl_sock.o
	-@rm -f test_native
	-@rm -f *.o
	-@rm -f py_tassl_sock_wrap.cpython*.so
	-@rm -rf __pycache__ 
