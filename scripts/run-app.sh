#!/bin/bash
# Run the Spring Boot application locally with the dev profile.
# Set CLERK_* and database env vars in your shell or via ENV_FILE (never commit secrets).

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

ENV_FILE="${ENV_FILE:-src/main/docker/Docker_Local/.env}"
if [[ -f "$ENV_FILE" ]]; then
  echo "Loading environment from $ENV_FILE"
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
fi

missing=()
for var in CLERK_PUBLISHABLE_KEY CLERK_SECRET_KEY CLERK_WEBHOOK_SECRET; do
  if [[ -z "${!var:-}" ]]; then
    missing+=("$var")
  fi
done

if ((${#missing[@]} > 0)); then
  echo "Missing required environment variables: ${missing[*]}"
  echo "Export them in your shell or set ENV_FILE to a local .env file (gitignored)."
  exit 1
fi

echo "Starting Spring Boot (dev profile) from $ROOT_DIR"
echo "Java: $(java -version 2>&1 | head -1)"
echo ""

./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
