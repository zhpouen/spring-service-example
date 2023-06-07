#!/bin/zsh

kubectl delete -f hello-flowserver-boot.yaml
docker rmi -f hello-flowserver-boot:v1
docker build -t hello-flowserver-boot:v1 .
kubectl apply -f hello-flowserver-boot.yaml