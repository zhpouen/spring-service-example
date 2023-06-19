#!/bin/zsh

docker build -t hello-provider-boot:v1 .
kubectl apply -f hello-provider-boot.yaml