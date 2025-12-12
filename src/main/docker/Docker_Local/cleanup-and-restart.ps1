# PowerShell script to cleanup old containers and restart with new project name

Write-Host "Stopping and removing old containers..." -ForegroundColor Yellow

# Stop and remove the current container
docker-compose -f docker-compose.local.yml down

# Try to find and remove the old recursing_poitras container if it exists
$oldContainers = docker ps -a --format "{{.Names}}" | Select-String -Pattern "recursing|poitras"
if ($oldContainers) {
    Write-Host "Found old containers to remove:" -ForegroundColor Yellow
    $oldContainers | ForEach-Object {
        Write-Host "  Removing: $_" -ForegroundColor Yellow
        docker rm -f $_
    }
} else {
    Write-Host "No old containers found with 'recursing' or 'poitras' in the name." -ForegroundColor Green
}

# Clean up any stopped containers
Write-Host "`nCleaning up stopped containers..." -ForegroundColor Yellow
docker container prune -f

# Remove old images if needed (optional - uncomment if you want to clean up old images)
# Write-Host "`nCleaning up unused images..." -ForegroundColor Yellow
# docker image prune -f

Write-Host "`nRebuilding and starting with new project name 'event-mgmnt-site'..." -ForegroundColor Green
docker-compose -f docker-compose.local.yml up -d --build

Write-Host "`nDone! The container should now appear as 'event-mgmnt-site' in Docker Desktop." -ForegroundColor Green
Write-Host "Check status with: docker ps" -ForegroundColor Cyan

