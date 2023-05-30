#!/bin/zsh

docker build -t hello-provider-boot:latest .
kubectl apply -f hello-provider-boot.yaml