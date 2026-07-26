#!/bin/sh
APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$CLASSPATH" ]; then
  echo "[gradlew] Downloading gradle-wrapper.jar..."
  curl -fsSL -o "$CLASSPATH" https://github.com/gradle/gradle/raw/v8.13.0/gradle/wrapper/gradle-wrapper.jar || {
    echo "[gradlew] Cannot download gradle-wrapper.jar, installing Gradle..."
    curl -fsSL https://services.gradle.org/distributions/gradle-8.13-bin.zip -o /tmp/gradle.zip
    unzip -qo /tmp/gradle.zip -d /tmp/
    /tmp/gradle-8.13/bin/gradle "$@"
    exit $?
  }
fi
exec java $DEFAULT_JVM_OPTS $JAVA_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
