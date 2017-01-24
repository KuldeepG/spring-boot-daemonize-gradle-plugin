#!/bin/bash
set -ev
if [ -n "${TRAVIS_TAG}" ]; then
  git reset --hard HEAD
  ./gradlew publishPlugins -Pversion="${TRAVIS_TAG}" -Pgradle.publish.key="${GRADLE_KEY}" -Pgradle.publish.secret="${GRADLE_SECRET}"
fi
