#!/bin/bash

set -e

command="$1"

if [ "$command" == "java" ];
then 
	echo "Launching Docker HazelcastRunner"; 
else 
	echo "ERROR: command must start with: java -jar /dockertest/hazelcast-docker-test.jar"; 
	exit 1; 
fi

exec "$@"
