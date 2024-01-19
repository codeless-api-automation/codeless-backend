#!/bin/bash

docker-compose down

# Script to stop a Docker container and remove an image
# Define the container name and image name
IMAGE_NAME="apisentinel/codeless-backend:latest"
CONTAINER_ID=$(docker ps -aqf "name=$IMAGE_NAME")

# Stop the container
docker stop "$CONTAINER_ID"

# Remove the container
docker rm "$CONTAINER_ID"

# Remove the image
docker rmi $IMAGE_NAME

echo "Container and image removed successfully."

docker-compose up