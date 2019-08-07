.PHONY: init deps

CONTRACT = HelloWorld
GOPKG = "hello"
ROOT = $(PWD)

init:
	rm -rf benchreports && mkdir benchreports
	rm -rf ./node_modules && npm install && npm run bootstrap
	rm -rf ./node_modules/ethereumjs-tx && tar -xvf ethereumjs-tx.tar.gz && mv -f ethereumjs-tx ./node_modules/
	rm -rf ./node_modules/web3-core-method/ && tar -xvf web3-core-method.tar.gz && mv -f web3-core-method ./node_modules/
	rm -rf ./node_modules/web3-eth-abi/ && tar -xvf web3-eth-abi.tar.gz && mv -f web3-eth-abi ./node_modules/
	rm -rf ./node_modules/web3-eth-contract/ && tar -xvf web3-eth-contract.tar.gz && mv -f web3-eth-contract ./node_modules/

deps:
	rm -rf build && mkdir -p ./build/$(GOPKG)
	docker run --rm -v $(ROOT)/contracts:/sources -v $(ROOT)/build/:/output ethereum/solc:0.4.25 --overwrite --abi --bin -o /output /sources/$(CONTRACT).sol

bench:up
	node $(ROOT)/packages/caliper-cli/caliper.js benchmark run -w  $(ROOT)/benchmark -c $(ROOT)/benchmark/$(CONTRACT)/config.yaml  -n $(ROOT)/benchmark/4nodes1group/fisco-bcos.json

up:down
	docker-compose -f benchmark/4nodes1group/docker-compose.yaml up -d; sleep 3s

down:
	docker-compose -f benchmark/4nodes1group/docker-compose.yaml down

go:deps
	 go get github.com/chislab/go-fiscobcos && cd $(GOPATH)/src/github.com/chislab/go-fiscobcos && make all
	 $(GOPATH)/src/github.com/chislab/go-fiscobcos/build/bin/abigen --bin=./build/$(CONTRACT).bin --abi=./build/$(CONTRACT).abi --pkg=$(GOPKG) --out=./build/$(GOPKG)/$(CONTRACT).go
