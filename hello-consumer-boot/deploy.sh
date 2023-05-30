#!/bin/zsh

docker build -t hello-consumer-boot:latest .
kubectl apply -f hello-consumer-boot.yaml