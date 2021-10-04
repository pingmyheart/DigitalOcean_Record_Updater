#!/bin/bash
if ! mvn -v COMMAND &>/dev/null; then
  echo "mvn command could not be found"
  exit
fi
if [ -d target/ ]; then
  echo -e "Removing target folder"
  rm -rf target
fi
mvn -U clean package install -DskipTests
if ! docker-compose -v COMMAND &>/dev/null; then
  echo "docker-compose command could not be found"
  exit
fi
docker-compose stop
docker-compose up -d --build
