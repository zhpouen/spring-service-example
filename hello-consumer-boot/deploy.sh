#!/bin/zsh

kubectl delete -f hello-consumer-boot.yaml
docker rmi -f hello-consumer-boot:v1
docker build -t hello-consumer-boot:v1 .
kubectl apply -f hello-consumer-boot.yaml