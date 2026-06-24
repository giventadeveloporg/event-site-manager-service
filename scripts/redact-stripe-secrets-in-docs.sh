#!/bin/bash
# Replace Stripe-like token patterns in documentation with placeholders.
# Safe to commit: uses generic regex only (no real keys embedded).

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

FILES=(
  "scripts/README-STRIPE-CONFIG.md"
  "scripts/README-STRIPE-CONFIG.html"
)

for file in "${FILES[@]}"; do
  if [[ ! -f "$file" ]]; then
    continue
  fi
  if sed --version 2>/dev/null | grep -q GNU; then
    sed -i -E \
      -e 's/sk_(test|live)_[A-Za-z0-9]+/sk_test_YOUR_SECRET_KEY_HERE/g' \
      -e 's/whsec_[A-Za-z0-9]+/whsec_YOUR_WEBHOOK_SECRET_HERE/g' \
      -e 's/pk_(test|live)_[A-Za-z0-9]+/pk_test_YOUR_PUBLISHABLE_KEY_HERE/g' \
      "$file"
  else
    sed -i '' -E \
      -e 's/sk_(test|live)_[A-Za-z0-9]+/sk_test_YOUR_SECRET_KEY_HERE/g' \
      -e 's/whsec_[A-Za-z0-9]+/whsec_YOUR_WEBHOOK_SECRET_HERE/g' \
      -e 's/pk_(test|live)_[A-Za-z0-9]+/pk_test_YOUR_PUBLISHABLE_KEY_HERE/g' \
      "$file"
  fi
  echo "Redacted Stripe-like tokens in $file"
done
