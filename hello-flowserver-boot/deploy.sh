#!/bin/zsh

docker build -t hello-flowserver-boot:v1 .
kubectl apply -f hello-flowserver-boot.yaml