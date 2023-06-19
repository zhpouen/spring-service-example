#!/bin/zsh

kubectl delete -f hello-provider-boot.yaml
docker rmi -f hello-provider-boot:v1