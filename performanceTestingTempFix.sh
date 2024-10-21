#!/bin/sh

# https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1738

FOLDER=/Users/${USER}/.gradle/caches/transforms-3/0efa82f00626821fb6f42f42cc9d940c/transformed/android-studio-2024.2.1.10-mac_arm/plugins
#FOLDER=/home/runner/.gradle/caches/transforms-3/e773a64bc76e36769f59d43df2c66fa6/transformed/android-studio-2024.2.1.10-linux/plugins/performanceTesting/lib/performanceTesting.jar
mkdir -p ${FOLDER}/performanceTesting
mkdir -p ${FOLDER}/performanceTesting/lib

FOLDER=${FOLDER}/performanceTesting/lib
FILE=performance-testing-242.23339.19.jar
URL=https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/performanceTesting/performance-testing/242.23339.19/${FILE}

echo ${URL}
curl -I ${URL} -o ${FOLDER}/${FILE}

#curl https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/performanceTesting/performance-testing/242.23339.19/performance-testing-242.23339.19.jar -o /Users/${USER}/.gradle/caches/transforms-3/0efa82f00626821fb6f42f42cc9d940c/transformed/android-studio-2024.2.1.10-mac_arm/plugins/performanceTesting/lib/performance-testing-242.23339.19.jar