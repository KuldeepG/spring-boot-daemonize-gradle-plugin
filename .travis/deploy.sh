#!/bin/bash
set -ev
if [ -n "${TRAVIS_TAG}" ]; then
  ./gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion="${TRAVIS_TAG}" -Prelease.newVersion="${TRAVIS_TAG}"-SNAPSHOT
  ./gradlew publishPlugins -Pversion="${TRAVIS_TAG}"
fi