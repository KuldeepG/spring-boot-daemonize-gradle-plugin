#!/bin/bash
set -ev
if [ -n "${TRAVIS_TAG}" ]; then
  git reset --hard HEAD
  ./gradlew publishPlugins -Pversion='${TRAVIS_TAG}' -Dgradle.publish.key='${GRADLE_KEY}' -Dgradle.publish.secret='${GRADLE_SECRET}'
fi
