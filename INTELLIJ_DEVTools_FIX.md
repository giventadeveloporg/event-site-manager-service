# IntelliJ DevTools Restart Fix

## Problem

IntelliJ is still using DevTools restart classloader even though `restart.enabled: false` is set in `application-dev.yml`, causing `NoClassDefFoundError` for Stripe classes.

## Solution Options

### Option 1: Disable DevTools Restart in IntelliJ Run Configuration (Recommended)

1. Open **Run** → **Edit Configurations...**
2. Select your Spring Boot run configuration
3. In the **VM options** field, add:
   ```
   -Dspring.devtools.restart.enabled=false
   ```
4. Click **OK** and restart the application

### Option 2: Add System Property to IntelliJ Run Configuration

1. Open **Run** → **Edit Configurations...**
2. Select your Spring Boot run configuration
3. Go to **Environment variables** section
4. Add:
   ```
   SPRING_DEVTOOLS_RESTART_ENABLED=false
   ```
5. Click **OK** and restart the application

### Option 3: Exclude Stripe Classes from Restart (Alternative)

If you want to keep restart enabled but exclude Stripe classes:

1. Open **Run** → **Edit Configurations...**
2. Select your Spring Boot run configuration
3. In the **VM options** field, add:
   ```
   -Dspring.devtools.restart.exclude=com/nextjstemplate/service/payment/adapter/**,com/stripe/**
   ```
4. Click **OK** and restart the application

### Option 4: Use Maven Run Configuration Instead

1. Create a new **Maven** run configuration
2. Set **Working directory** to: `$PROJECT_DIR$`
3. Set **Command line** to: `spring-boot:run -Pdev`
4. Add environment variables in **Environment variables** section
5. Run this configuration instead of the Spring Boot one

## Why This Happens

IntelliJ's Spring Boot run configuration may override YAML settings or use a different classloader initialization order. The command line (`mvn spring-boot:run`) respects the YAML configuration, but IntelliJ's internal Spring Boot runner may not.

## Verification

After applying the fix, you should see in the logs:

- No `RestartClassLoader` in stack traces
- Application starts successfully
- Stripe classes are accessible
