#!/bin/bash
set -ev
if [ -n "${TRAVIS_TAG}" ]; then
  $HOME/gradlew -b $HOME/build.gradle release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion="${TRAVIS_TAG}" -Prelease.newVersion="${TRAVIS_TAG}"-SNAPSHOT
  $HOME/gradlew -b $HOME/build.gradle publishPlugins -Pversion="${TRAVIS_TAG}" -Pgradle.publish.key="${GRADLE_KEY}" -Pgradle.publish.secret="${GRADLE_SECRET}"
fi
