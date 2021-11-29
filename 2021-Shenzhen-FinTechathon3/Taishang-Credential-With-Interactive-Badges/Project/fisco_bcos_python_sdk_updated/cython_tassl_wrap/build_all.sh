sysname=`uname -s` 
ext='so'
cpp_path=""
echo -e current dev environment is: "\033[33m $sysname\033[0m" 
echo -----------------------
case $sysname in 
Linux*) 
	echo -e Using "\033[33m [Linux] \033[0m" configuration '---------->'
	cpp_path="cpp_linux"
	echo "goto" $cpp_path
	cd cpp_linux	
	source ./fetch_tassl_obj.sh
	make
	cp *.so ..
	cd .. 
	;;
MINGW64*) 
	echo -e Using "\033[33m [Windows] $sysname \033[0m" configuration '---------->'
	cpp_path="cpp_win"
	echo "goto" $cpp_path
	cd cpp_win 
	make 
	ext=dll
	cp *.dll ../
	cd ..
	;;
*) echo "unknown system"; exit 1;; 
esac
echo -------------------------
echo return to : `pwd` 
  
CFLAGS="" LDFLAGS="-L`pwd` -L$TASSL -L./$cpp_path" python setup_tassl_sock_wrap.py build_ext --inplace 
echo 
echo "---- after build: --->"
echo 
ls -l  *.$ext
#ldd *.$ext
#cp *$ext ..
