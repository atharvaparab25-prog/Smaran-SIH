#!/usr/bin/env sh

# Helper script for running Gradle commands on systems without global installations
DIRNAME=$(dirname "$0")
if [ -z "$DIRNAME" ]; then
    DIRNAME="."
fi
exec "$DIRNAME/gradle/wrapper/gradle-wrapper.jar" "$@"
