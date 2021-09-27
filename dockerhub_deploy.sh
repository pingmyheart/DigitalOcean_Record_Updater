#!/bin/bash
function generate_and_publish {
  echo -e "Final image name --> $image_name"
  echo -e "Image does not exists\nBuilding maven project..."
  mvn -U clean package install -DskipTests
  echo -e "Creating image..."
  docker build -t "$image_name" .
  echo -e "Publishing image..."
  docker push "$image_name"
  echo -e "Image published"
  apt moo moo moo
}

function check_commands {
  if ! mvn -v COMMAND &>/dev/null; then
    echo -e "mvn command could not be found"
    exit
  fi

  if ! git --version COMMAND &>/dev/null; then
    echo -e "git command could not be found"
    exit
  fi

  if ! docker -v COMMAND &>/dev/null; then
    echo -e "docker command could not be found"
    exit
  fi
}

check_commands

if ! git branch --show-current | grep -q 'release/'; then
  echo -e "$(git branch --show-current) is not a release branch. Interrupt"
  exit
fi

branch=$(git branch --show-current)

IFS='/'                     #setting / as delimiter
read -a strarr <<<"$branch" #reading str as an array as tokens separated by IFS

echo "Branch : ${strarr[0]} "
echo "Release : ${strarr[1]} "

if [ -f Dockerfile ]; then
  image_name="pingmyheart/digitalocean_record_updater:${strarr[1]}"

  if docker manifest inspect "$image_name" > /dev/null ; then
    echo -e "Image already exists on Docker Hub. Skipping"
    exit
  fi

  if ! docker image ls | grep pingmyheart/digitalocean_record_updater | grep -q ${strarr[1]}; then
    generate_and_publish
  else
    read -p "Image already exists locally. Do you want to delete it and retry?(y/n) " -n 1 -r
    echo # (optional) move to a new line
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
      echo -e "Interrupting..."
      exit
    fi
    echo -e "Removing local image..."
    docker image rm "$image_name"
    echo -e "Local image removed..."
    generate_and_publish
  fi
else
  echo -e "Dockerfile not found. Interrupt"
fi
