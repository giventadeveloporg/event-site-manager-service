# IntelliJ Maven Repository Configuration Fix

## Problem

IntelliJ is using a different Maven local repository (`E:\maven_repo\.m2\repository`) than Maven command line (`C:\Users\gain\.m2\repository`), causing Stripe dependency to be missing in IntelliJ.

## Solution Options

### Option 1: Configure IntelliJ to Use Default Maven Repository (Recommended)

1. Open **File** → **Settings** (or **IntelliJ IDEA** → **Preferences** on Mac)
2. Navigate to **Build, Execution, Deployment** → **Build Tools** → **Maven**
3. Under **User settings file**, check if a custom `settings.xml` is configured
4. Under **Local repository**, change it to: `C:\Users\gain\.m2\repository`
5. Click **Apply** and **OK**
6. Go to **File** → **Invalidate Caches / Restart** → **Invalidate and Restart**
7. After restart, go to **File** → **Reload Project** or right-click `pom.xml` → **Maven** → **Reload Project**

### Option 2: Copy Stripe Dependency to IntelliJ Repository

The script above should have copied the Stripe dependency. If not, manually copy:

```powershell
# Copy Stripe dependency from default repo to IntelliJ repo
Copy-Item -Path "C:\Users\gain\.m2\repository\com\stripe" -Destination "E:\maven_repo\.m2\repository\com\stripe" -Recurse -Force
```

Then in IntelliJ:

1. **File** → **Invalidate Caches / Restart** → **Invalidate and Restart**
2. After restart, right-click `pom.xml` → **Maven** → **Reload Project**

### Option 3: Force Maven to Download to IntelliJ Repository

1. Open **File** → **Settings** → **Build Tools** → **Maven**
2. Note the **Local repository** path (should be `E:\maven_repo\.m2\repository`)
3. Open terminal in IntelliJ or command prompt
4. Run:
   ```powershell
   cd E:\project_workspace\malayalees-us-site-boot
   mvn dependency:resolve -Dmaven.repo.local=E:\maven_repo\.m2\repository
   ```
5. In IntelliJ, right-click `pom.xml` → **Maven** → **Reload Project**

### Option 4: Use Maven Wrapper with Custom Repository

If you need to keep the custom repository, ensure IntelliJ uses it consistently:

1. **File** → **Settings** → **Build Tools** → **Maven**
2. Set **Local repository** to: `E:\maven_repo\.m2\repository`
3. Set **Maven home directory** to your Maven installation
4. Click **Apply**
5. Right-click `pom.xml` → **Maven** → **Download Sources and Documentation**
6. **File** → **Invalidate Caches / Restart**

## Verification

After applying the fix:

1. Check that `E:\maven_repo\.m2\repository\com\stripe\stripe-java\24.16.0\stripe-java-24.16.0.jar` exists
2. IntelliJ should no longer show red errors on `import com.stripe.*`
3. Code completion should work for Stripe classes

## Why This Happens

IntelliJ can be configured to use a different Maven local repository than the command line Maven. This is useful for:

- Isolating project dependencies
- Using a shared repository across team
- Custom build configurations

However, it requires ensuring all dependencies are available in the configured repository.
