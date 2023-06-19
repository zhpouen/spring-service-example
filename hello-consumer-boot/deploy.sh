#!/bin/zsh

docker build -t hello-consumer-boot:v1 .
kubectl apply -f hello-consumer-boot.yaml