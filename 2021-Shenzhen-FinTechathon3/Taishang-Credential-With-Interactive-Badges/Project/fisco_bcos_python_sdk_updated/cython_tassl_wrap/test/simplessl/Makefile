LINUX_CFLAGS=-Wall -pedantic -g -O2
LINUX_LIBS=

all: test_tassl


#common flags
CC=gcc
SYSCFLAGS=
SYSLIBS=
LDFLAGS=$(SYSLIBS)
INCLUDES=
CFLAGS=$(SYSCFLAGS) $(INCLUDES)
LINUX_CFLAGS=-Wall -pedantic -g -O2

#for openssl & tassl
OPENSSL_DIR=../../../TASSL
OPENSSL_INCLUDES=-I$(OPENSSL_DIR)/include
OPENSSL_LIBS=-L$(OPENSSL_DIR) -lssl -lcrypto -ldl
OPENSSL_LDFLAGS=$(SYSLIBS) $(OPENSSL_LIBS)
LIB_TASSL_SOCK_WRAP_CFLAGS=$(CFLAGS) $(OPENSSL_INCLUDES) $(LINUX_CFLAGS) -fPIC


TEST_SOURCES=test_tassl.c
TEST_OBJS=test_tassl.o
TEST_CFLAGS= $(LIB_TASSL_SOCK_WRAP_CFLAGS)
TEST_LDFLAGS=-lstdc++ -L./  
test_tassl: $(TEST_SOURCES) 
	@echo "-->Build test"
	$(CC) $(TEST_CFLAGS) -c test_tassl.c 
	$(CC) $(TEST_CFLAGS) $(TEST_LDFLAGS) $(OPENSSL_LDFLAGS) test_tassl.o -o test_tassl



clean:
	@rm -f *.o
	@rm -f test_tassl