#!/bin/bash
PROJECT_NAME="dev-api-cep"
APP_NAME="rest-test"

mvn clean && mvn package

docker build -t $PROJECT_NAME/$APP_NAME .

alreadyExists=$(docker ps --filter "name=$APP_NAME" -a -q)
if [[ ! -z "$alreadyExists" ]]
then
    echo " - Container already exists: " $alreadyExists
    echo " - Removing..."

    docker rm -f $(docker ps --filter "name=$APP_NAME" -a -q)
fi

# Pushing Docker Image
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 584991916600.dkr.ecr.us-east-1.amazonaws.com

docker build -t $PROJECT_NAME/$APP_NAME .

docker tag $PROJECT_NAME/$APP_NAME:latest 584991916600.dkr.ecr.us-east-1.amazonaws.com/$PROJECT_NAME/$APP_NAME:latest

docker push 584991916600.dkr.ecr.us-east-1.amazonaws.com/$PROJECT_NAME/$APP_NAME:latest