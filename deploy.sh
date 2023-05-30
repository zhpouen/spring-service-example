#!/bin/zsh

current_path=$PWD
mvn clean package -DskipTests
cd $current_path/hello-provider-boot
sh ./deploy.sh
cd $current_path/hello-consumer-boot
sh ./deploy.sh
cd $current_path/hello-gateway-boot
sh ./deploy.sh
cd $current_path
