.PHONY: build cluster test

default: build

build:
	go build -o bin/swarm    github.com/chislab/go-fiscobcos/cmd/swarm
	go build -o bin/geth     github.com/chislab/go-fiscobcos/cmd/geth
	go build -o bin/bootnode github.com/chislab/go-fiscobcos/cmd/bootnode

cluster: build
	scripts/boot-cluster.sh

test:
	go test -v github.com/chislab/go-fiscobcos/swarm/...
