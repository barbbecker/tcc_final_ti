variable "aws_region" {
  description = "AWS region to launch servers"
  default = "us-east-1"
}

variable "aws_amis" {
  default = {
    us-east-1 = "ami-{number-ami}"
  }
}

variable "aws_access_key" {
  description = "AWS ACCESS KEY"
}

variable "aws_secret_key" {
  description = "AWS SECRET KEY"
}

variable "key_name" {
  description = "Name of SSH keypair to use in AWS"
}