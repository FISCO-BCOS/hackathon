echo 
echo "build python Extension"
source build_all.sh 
echo 
#echo "run cpp test --->"
#cpp_linux/test_tassl_sock
echo 
echo "start python test -->"
echo 
python test_tassl_sock_wrap.py	
echo 
echo "<--python test done"
