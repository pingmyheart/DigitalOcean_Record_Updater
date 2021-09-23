#!/bin/bash
if ! docker-compose -v COMMAND &>/dev/null; then
  echo "docker-compose command could not be found"
  exit
fi
docker-compose down --remove-orphans
