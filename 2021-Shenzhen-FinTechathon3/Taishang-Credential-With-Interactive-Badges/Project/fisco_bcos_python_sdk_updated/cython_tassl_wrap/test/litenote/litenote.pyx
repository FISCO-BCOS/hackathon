def say_hello_to(name):
	print("Hello %s!" % name)

cdef extern from "litenote.h":
	int write_note(char * text)
	
def py_write_note(text):
	return write_note(text)