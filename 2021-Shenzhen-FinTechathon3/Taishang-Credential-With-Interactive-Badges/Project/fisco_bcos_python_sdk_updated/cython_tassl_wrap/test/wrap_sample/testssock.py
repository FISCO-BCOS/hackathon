from py_ssock_wrap import PySSockWrap

wrap = PySSockWrap();
wrap.init()
wrap.write(b"this is a test ",10)
result  = wrap.read(4096)
print("read from lib : ",result)