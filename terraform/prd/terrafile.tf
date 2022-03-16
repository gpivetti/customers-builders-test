provider "aws" {
  region = "us-east-1"
}

terraform {
  backend "s3" {
    bucket = "prd-marisa-tfstate"
    key = "prd-api-cep/customer-builders-test/terraform.state"
    region = "us-east-1"
  }
}

resource "aws_ecr_repository" "terraform_ecr" {
  name                 = var.ECR_NAME
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = false
  }
}

module "app-deploy" {
  source                 = "git::http://user-WI7DDWmYR_6XMjLvaUKVkw:JChh6RNqMzh8nHBKSCdP@bitbucket.org/lojas-marisa/tf-module-ecs-rest.git?ref=v0.6.1"
  containers_definitions = data.template_file.containers_definitions_json.rendered
  ecs_cluster_id         = data.terraform_remote_state.ecs_cluster.outputs.ecs_cluster_id
  app_project_name       = var.APP_PROJECT_NAME
  app_name               = var.APP_NAME
  app_port               = var.APP_PORT
  cloudwatch_group_name  = "/ecs/${var.APP_PROJECT_NAME}_${var.APP_NAME}"
  target_group_name      = var.TGN_NAME
  vpc_tag_name           = var.VPC_TAG_NAME
  app_count              = var.APP_COUNT
}