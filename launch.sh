#!/bin/bash
find . -name "docker-compose.yml" -exec docker-compose -f {} pull \;
find . -name "docker-compose.yml" -exec docker-compose -f {} build \;
docker-compose up &>/dev/null &
echo $! > .last_pid
docker-compose ps
