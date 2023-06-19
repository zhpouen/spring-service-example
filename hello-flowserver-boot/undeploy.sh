#!/bin/zsh

kubectl delete -f hello-flowserver-boot.yaml
docker rmi -f hello-flowserver-boot:v1