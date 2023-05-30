#!/bin/zsh

kubectl delete -f hello-gateway-boot.yaml
docker rmi -f hello-gateway-boot:v1
docker build -t hello-gateway-boot:v1 .
kubectl apply -f hello-gateway-boot.yaml