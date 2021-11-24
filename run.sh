#!/usr/bin/env bash

set -e

export DD_TRACER_ENABLED=true
export DD_APPSEC_ENABLED=true
export DD_ENV=test
ME="$(whoami)"
ME_ARRAY=(${ME//./ })
export DD_SERVICE="${ME_ARRAY[0]}-test"
export DD_TRACE_SAMPLE_RATE=1.0

./gradlew build

pushd build/libs
java -javaagent:dd-java-agent.jar -jar webapp.jar
popd
