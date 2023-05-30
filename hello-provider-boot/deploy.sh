#!/bin/zsh

kubectl delete -f hello-provider-boot.yaml
docker rmi -f hello-provider-boot:v1
docker build -t hello-provider-boot:v1 .
kubectl apply -f hello-provider-boot.yaml