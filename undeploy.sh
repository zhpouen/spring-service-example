#!/bin/zsh

current_path=$PWD
cd $current_path/hello-gateway-boot
sh ./undeploy.sh
cd $current_path/hello-consumer-boot
sh ./undeploy.sh
cd $current_path/hello-provider-boot
sh ./undeploy.sh
cd $current_path/hello-flowserver-boot
sh ./undeploy.sh
cd $current_path
