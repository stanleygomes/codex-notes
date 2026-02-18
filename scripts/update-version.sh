#!/bin/bash

set -e

CURRENT_VERSION=$(grep 'pluginVersion' gradle.properties | cut -d'=' -f2 | tr -d ' ')

# Try to get latest published release tag
LAST_TAG=$(gh api repos/stanleygomes/codex-notes/releases/latest --jq '.tag_name' 2>/dev/null || echo "")

# If no published release, try git tags
if [ -z "$LAST_TAG" ]; then
  LAST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "")
fi

echo "Last tag: '$LAST_TAG'"
echo "Current version: $CURRENT_VERSION"

# Get commits to analyze
if [ -n "$LAST_TAG" ]; then
  COMMITS=$(git log --oneline $LAST_TAG..HEAD 2>/dev/null || echo "")
else
  # No tags, check recent commits (last 20)
  COMMITS=$(git log --oneline -20 2>/dev/null || echo "")
fi

echo "Commits to analyze:"
echo "$COMMITS"

INCREMENT="patch"
if echo "$COMMITS" | grep -qE "BREAKING CHANGE|![:]"; then
  INCREMENT="major"
  echo "Detected: BREAKING CHANGE - incrementing MAJOR"
elif echo "$COMMITS" | grep -qE " feat[:(]"; then
  INCREMENT="minor"
  echo "Detected: feat - incrementing MINOR"
else
  echo "Detected: patch changes - incrementing PATCH"
fi

IFS='.' read -r major minor patch <<< "$CURRENT_VERSION"
case $INCREMENT in
  major)
    VERSION="$((major + 1)).0.0"
    ;;
  minor)
    VERSION="$major.$((minor + 1)).0"
    ;;
  patch)
    VERSION="$major.$minor.$((patch + 1))"
    ;;
esac

echo "New version: $VERSION"

# Update gradle.properties
sed -i "s/pluginVersion.*/pluginVersion = $VERSION/" gradle.properties

echo ""
echo "Updated gradle.properties:"
cat gradle.properties

