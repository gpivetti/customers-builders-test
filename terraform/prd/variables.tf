variable "APP_VERSION" {
 default = "latest"
}

variable "APP_PROJECT_NAME" {
  default = "prd-api-cep"
}

variable "APP_NAME" {
  default = "customer-builders-test"
}

variable "TGN_NAME" {
  default = "prd-api-cep-customer-builders-test"
}

variable "VPC_TAG_NAME" {
  default = "VPC-Arquitetura"
}

variable "ECR_NAME"{
  default = "prd-api-cep/customer-builders-test"
}

variable "aws_region" {
  default = "us-east-1"
}

variable "APP_PORT" {
  default = "8295"
}

variable "APP_COUNT" {
  default = 1
}