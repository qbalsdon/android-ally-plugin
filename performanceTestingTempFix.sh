#!/bin/sh

# https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1738

FOLDER=/Users/$USER/.gradle/caches/transforms-3/0efa82f00626821fb6f42f42cc9d940c/transformed/android-studio-2024.2.1.10-mac_arm/plugins
mkdir -p $FOLDER/performanceTesting
mkdir -p $FOLDER/performanceTesting/lib
FOLDER=$FOLDER/performanceTesting/lib/

open ${FOLDER}
open https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/performanceTesting/performance-testing/242.23339.19/performance-testing-242.23339.19.jar