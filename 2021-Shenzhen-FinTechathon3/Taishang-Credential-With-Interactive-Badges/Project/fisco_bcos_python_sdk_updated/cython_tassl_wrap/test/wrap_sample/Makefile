LINUX_CFLAGS=-Wall -pedantic -g -O2
LINUX_LIBS=

all: lib native libtest.exe

test: wrap_test 
lib: libssock_wrap.dll
native: native_lib.dll

#common flags
CC=gcc
SYSCFLAGS=
SYSLIBS=
LDFLAGS=$(SYSLIBS)
INCLUDES=
CFLAGS=$(SYSCFLAGS) $(INCLUDES)
LINUX_CFLAGS=-Wall -pedantic -g -O2 




TEST_SOURCES=wrap_test.cpp
TEST_OBJS=wrap_test.o
TEST_CFLAGS=$(CFLAGS)
TEST_LDFLAGS=-L./ -lssock_wrap  -lstdc++
wrap_test: $(TEST_SOURCES) libssock_wrap.so
	@echo "-->Build test"
	$(CC) $(TEST_CFLAGS) $(LINUX_CFLAGS) -c wrap_test.cpp -o wrap_test.o
	$(CC) $(TEST_OBJS) $(TEST_CFLAGS) $(TEST_LDFLAGS)  -o wrap_test


libtest.exe	:libtest.cpp
	@echo "-->Build lib test"
	$(CC) $(TEST_CFLAGS) $(LINUX_CFLAGS) -c libtest.cpp -o libtest.o
	$(CC) libtest.o $(TEST_CFLAGS)  -o  libtest



LIB_SOURCES=ssock_wrap.cpp clib.cpp
LIB_OBJS=ssock_wrap.o
LIB_CFLAGS=$(CFLAGS) 
LIB_LDFLAGS=-shared  -Wl,--output-def,ssock_wrap.def,--out-implib,ssock_wrap.lib 
libssock_wrap.dll: $(LIB_SOURCES) 
	@echo "-->Build ssock lib"
	$(CC) $(LIB_CFLAGS) $(LINUX_CFLAGS) -fPIC -c ssock_wrap.cpp
	$(CC) ssock_wrap.o  $(LIB_LDFLAGS) -lstdc++  -o ssock_wrap.dll
	
NATIVE_LIB_LDFLAGS=-shared  -Wl,--output-def,native_lib.def,--out-implib,native_lib.lib 	
native_lib.dll:clib.cpp
	@echo "-->Build native lib"
	$(CC) $(LIB_CFLAGS) $(LINUX_CFLAGS) -fPIC -c clib.cpp
	$(CC) clib.o ssock_wrap.o   $(NATIVE_LIB_LDFLAGS) -lstdc++ -o native_lib.dll
	

clean:
	@rm -f wrap_test
	@rm -f libssock_wrap.so
	@rm -f ssock_wrap.o
	@rm -f *.o
	@rm -f *.so
	@rm -f ssock_wrap.dll
	@rm -f *.def
	@rm -f *.lib
	@rm -r *.exe
	@rm -r native_lib.dll
