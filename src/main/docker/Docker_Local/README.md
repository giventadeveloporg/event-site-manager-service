# Local Docker (Spring Boot app)

Run the backend from this directory. All compose settings are loaded from **`.env` in this folder** (`src/main/docker/Docker_Local/.env`). Do not put usernames, passwords, API keys, or tokens in this README—keep them only in `.env` (gitignored).

## Prerequisites

- Docker Desktop
- PostgreSQL stack running (`src/main/docker/postgres_docker`)
- External networks: `event_site_manager_db_default`, `event-site-manager-batch-jobs_batch-jobs-network`
- Built JAR at repo root: `target/nextjs-template-boot-0.0.1-SNAPSHOT.jar` (run `mvn package -DskipTests` from project root if missing)

## Configure

Edit `.env` in this directory (not `src/main/docker/.env`). Key settings:

| Variable | Purpose |
|----------|---------|
| `RDS_ENDPOINT` | Postgres hostname on Docker network (e.g. `event_site_manager_db-postgresql-1`) |
| `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` | Database connection (set values in `.env` only) |
| `JWT_API_AUTH_USERNAME`, `JWT_API_AUTH_PASSWORD` | Service JWT login (must match frontend `API_JWT_USER` / `API_JWT_PASS`; set in `.env` only) |
| `SPRING_PROFILES_ACTIVE` | Spring profile (default `local-postgres-cache`) |

## Start / restart

```powershell
cd src\main\docker\Docker_Local
docker compose --env-file .env -f docker-compose.local.yml up -d --build
```

Or use the helper script:

```powershell
.\cleanup-and-restart.ps1
```

## Stop

```powershell
docker compose --env-file .env -f docker-compose.local.yml down
```

## Health check

```powershell
curl.exe http://127.0.0.1:8080/management/health
```
