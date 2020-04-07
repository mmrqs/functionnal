#!/bin/bash
kill `cat .last_pid`
docker stop `docker ps | grep funct | cut -d' ' -f1`
docker-compose stop
