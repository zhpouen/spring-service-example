#!/bin/zsh

docker build -t hello-gateway-boot:v1 .
kubectl apply -f hello-gateway-boot.yaml