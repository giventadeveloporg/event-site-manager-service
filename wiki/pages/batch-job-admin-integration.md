# Batch Job Admin Integration

This backend exposes admin proxy APIs to monitor batch job execution history from the `event-site-manager-batch-jobs` service.

## New Admin APIs

Base path: `/api/admin/batch-jobs`

- `GET /executions`
- `GET /executions/{id}`
- `GET /executions/failed?limit=50`
- `GET /executions/running?limit=50`
- `GET /summary`
- `GET /configured-jobs`

`/executions` supports filters:

- `status`
- `jobName`
- `tenantId`
- `startedAfter` (ISO date-time)
- `startedBefore` (ISO date-time)
- `page`, `size`, `sort`

## Security and Token Flow

- Frontend calls this backend with Bearer JWT (existing auth model).
- This backend forwards the same `Authorization` header to `event-site-manager-batch-jobs`.
- Batch-jobs validates JWT with `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET`.

## Rollout Checklist

1. Ensure `event-site-manager-batch-jobs` has:
   - monitoring endpoints available;
   - inbound JWT security enabled for `/api/**`.
2. Ensure both services use the same JWT verification secret.
3. Deploy batch-jobs first, then this backend.
4. Verify:
   - `/api/admin/batch-jobs/summary` returns counts,
   - `/api/admin/batch-jobs/executions` returns paginated results,
   - `/api/admin/batch-jobs/executions/failed` returns failed runs.
