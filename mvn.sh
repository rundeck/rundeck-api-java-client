#!/bin/bash

MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=192m" exec mvn "$@"
