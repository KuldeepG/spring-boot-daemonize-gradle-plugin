#!/bin/bash
set -ev
if [ -n "${TRAVIS_TAG}" ]; then
  ./gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion="${TRAVIS_TAG}" -Prelease.newVersion="${TRAVIS_TAG}"-SNAPSHOT --full-stacktrace
  ./gradlew publishPlugins -Pversion="${TRAVIS_TAG}" -Pgradle.publish.key="${GRADLE_KEY}" -Pgradle.publish.secret="GRADLE_SECRET" --full-stacktrace
fi