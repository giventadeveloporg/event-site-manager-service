# Maintenance scripts

One-off utilities for repository hygiene. Do not commit real API keys or secrets in these scripts.

## Redact Stripe tokens in documentation

If Stripe test keys were accidentally pasted into docs:

```bash
./scripts/redact-stripe-secrets-in-docs.sh
git add scripts/README-STRIPE-CONFIG.md scripts/README-STRIPE-CONFIG.html
git commit -m "Redact Stripe secrets from documentation"
```

## Remove secrets from git history

If secrets were committed in the past, use [git-filter-repo](https://github.com/newren/git-filter-repo) or BFG Repo-Cleaner with a local replacements file (never commit that file). Example pattern file (keep local only):

```
sk_test_YOUR_OLD_KEY==>sk_test_YOUR_SECRET_KEY_HERE
whsec_YOUR_OLD_SECRET==>whsec_YOUR_WEBHOOK_SECRET_HERE
```

After rewriting history, force-push only with team coordination and rotate the exposed credentials in Stripe/Clerk dashboards.
