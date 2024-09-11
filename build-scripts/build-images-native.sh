#!/bin/bash

function build_basic() {
  JAR_FILE=$1
  APP_NAME=$2
  JAR_PORT=$3

  docker build -f ./build-scripts/docker/basic/Dockerfile \
    --build-arg JAR_FILE=${JAR_FILE} \
    --build-arg JAR_PORT=${JAR_PORT} \
    -t ${APP_NAME}:latest \
    -t ${APP_NAME}:naive .
}

APP_VERSION=0.0.1-SNAPSHOT

# Building the app
cd ..

echo "Building JAR files"
mvn clean package -DskipTest

docker rmi application/java-examinator:naive


echo "Building Docker images"
build_basic ./target/java-examinator-${APP_VERSION}.jar application/java-examinator 8080
