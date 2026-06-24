#!/bin/bash
# Check local PostgreSQL, Spring Boot, and health endpoints.

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "=============================================="
echo "   APPLICATION STATUS CHECK"
echo "=============================================="
echo ""

echo "PostgreSQL Database:"
if docker ps 2>/dev/null | grep -q postgres; then
  echo "   Running on port 5432"
  docker ps | grep postgres | awk '{print "   Container:", $NF, "- Status:", $7, $8, $9}'
else
  echo "   Not running"
fi
echo ""

echo "Spring Boot Application:"
if command -v lsof >/dev/null 2>&1 && lsof -i :8080 >/dev/null 2>&1; then
  echo "   Running on port 8080"
  echo "   Process ID: $(lsof -ti :8080)"
  echo ""
  echo "Health Check:"
  if HEALTH=$(curl -s http://localhost:8080/management/health 2>/dev/null); then
    echo "   $HEALTH"
  else
    echo "   Application starting up..."
  fi
else
  echo "   Not running on port 8080"
  echo ""
  echo "   To start the application, run:"
  echo "   ./scripts/run-app.sh"
fi
echo ""

echo "Java Processes:"
if JAVA_COUNT=$(ps aux 2>/dev/null | grep java | grep -v grep | wc -l) && [[ "$JAVA_COUNT" -gt 0 ]]; then
  echo "   Found $JAVA_COUNT Java process(es)"
  ps aux | grep java | grep -v grep | awk '{print "   -", $11, $12, $13}' | head -5
else
  echo "   No Java processes running"
fi
echo ""

echo "=============================================="
echo "   QUICK ACCESS URLs"
echo "=============================================="
echo "Health Check:   http://localhost:8080/management/health"
echo "Swagger UI:     http://localhost:8080/swagger-ui/index.html"
echo "Actuator Info:  http://localhost:8080/management/info"
echo "API Base:       http://localhost:8080/api/"
echo "=============================================="
