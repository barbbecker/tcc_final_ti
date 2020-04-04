#!/bin/bash

echo update packages and install ansible
sudo apt-get update
sudo apt-get install software-properties-common -y
sudo apt-add-repository --yes ppa:ansible/ansible -y
sudo apt-get update
sudo apt-get install ansible -y

echo install git
sudo apt-get update -y
sudo apt-get install git -y