data "terraform_remote_state" "ecs_cluster" {
    backend = "s3"
    config = {
       bucket = "prd-marisa-tfstate"
       key = "global/${var.APP_PROJECT_NAME}/terraform.state"
       region = "us-east-1"
    }
}


data "template_file" "containers_definitions_json" {
  template = file("./templates/containers_definitions.json")

  vars = {
    APP_VERSION = var.APP_VERSION
    APP_PROJECT_NAME = var.APP_PROJECT_NAME
    APP_NAME = var.APP_NAME
    LOG_GROUP_NAME = "/ecs/${var.APP_PROJECT_NAME}_${var.APP_NAME}"
    AWS_REGION  = var.aws_region
  }
}