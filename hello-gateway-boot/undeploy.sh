#!/bin/zsh

kubectl delete -f hello-gateway-boot.yaml
docker rmi -f hello-gateway-boot:v1