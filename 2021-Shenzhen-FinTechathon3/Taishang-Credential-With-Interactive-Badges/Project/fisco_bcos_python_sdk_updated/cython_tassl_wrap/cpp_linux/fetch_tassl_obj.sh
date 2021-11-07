if [ ! -d tassl ]; then
	mkdir tassl
fi
objcount=670
count=`ls tassl/*.o|wc -l`
if [ $count -ge $objcount ] 
then
	echo "--- TASSL objects already in ./tassl ---"
	echo ---  tassl objects count = $count
else
	echo TASSL in $TASSL
	if [ -z $TASSL ]; then
		echo TASSL env not set , pls confirm the path of TASSL project 
		echo and add to ~/.bash_rc or ~/.bash_profile 
		echo then,make sure DONE this '"config shared;make;make install"' in TASSL project path
		exit -1
	fi 
	echo fetching...
	find  $TASSL/ssl -name "*.o"|xargs -I % cp % tassl 
	find  $TASSL/crypto -name "*.o"|xargs  -I % cp % tassl 
	count=`ls tassl/*.o|wc -l`
	echo Finish. total objects : $count
	if [ $count -lt $objcount ]
	then
		echo " --- TASSL maybe NOT build or build failed --- "
		echo " --- TOO few tassl objects for build so: $count ,should >= $objcount "
		echo !!! MAKE SURE DONE this : '"config shared;make;make install"' in $TASSL !!!
		exit -2
	fi
fi


