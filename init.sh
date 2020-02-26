#!/usr/bin/env bash

set -e

ENVIRONMENT_NAME="${SPRING_PROFILES_ACTIVE:-"development"}"
JVM_OPS="${JVM_OPS:-""}"
echo "ENVIRONMENT: ${ENVIRONMENT_NAME}"

java ${JVM_OPS} \
      -Duser.Timezone=America/Sao_Paulo \
      -Dspring.profiles.active=${ENVIRONMENT_NAME} \
      -Dspring.datasource.url=${DATABASE_URL} \
      -jar /app/spring-kotlin-template-*.jar \
      $COMMAND
