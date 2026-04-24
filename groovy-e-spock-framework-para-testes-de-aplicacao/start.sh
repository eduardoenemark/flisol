#!/usr/bin/env bash

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR="$PROJECT_DIR/target/groovy-spock-teste-app-1.0.0.jar"

echo "================================================"
echo "  Groovy + Spock Demo Application"
echo "================================================"

cd "$PROJECT_DIR"

# Build if jar does not exist
if [ ! -f "$JAR" ]; then
  echo "[INFO] JAR not found. Building project..."
  mvn clean package -DskipTests
fi

echo "[INFO] Starting application..."
#java -jar "$JAR"
mvn spring-boot:run
