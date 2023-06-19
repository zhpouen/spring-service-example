#!/bin/zsh

kubectl delete -f hello-consumer-boot.yaml
docker rmi -f hello-consumer-boot:v1