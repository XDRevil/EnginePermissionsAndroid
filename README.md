# Permissions Engine Toolkit - Usage Guide
dododod Vibe :D
You need install OpenJDK 17 + SdkManager 

sudo apt install -y unzip wget openjdk-17-jdk
echo $HOME 


mkdir -p $HOME/AndroidManagerCMD/cmdline-tools
cd $HOME/AndroidManagerCMD/cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdline-tools.zip
unzip cmdline-tools.zip
rm cmdline-tools.zip
mv cmdline-tools latest

export ANDROID_SDK_ROOT=$HOME/AndroidManagerCMD
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_SDK_ROOT/platform-tools

source ~/.bashrc
sdkmanager --list
sdkmanager --licenses

sdkmanager "platforms;android-27" "platforms;android-30" "platforms;android-36"

sdkmanager "build-tools;27.0.3" "build-tools;30.0.3" "build-tools;36.0.0"



---

## Introduction

Permissions Engine Toolkit is a modular system for managing Android permissions. It automatically handles permission requests, checks status, and manages the permission flow.

### Supported Permissions:
- **ACCESSIBILITY** - Accessibility service
- **OVERLAY** - Display over other apps
- **MANAGE_STORAGE** - Manage all files
- **BATTERY_OPTIMIZATIONS** - Ignore battery optimizations
- **CHANGE_SMS_MANAGER** - Change default SMS manager

---

## Building and Installation

### Build Library

To compile the library, run one of the following commands:

**Debug build:**
```bash
./gradlew :libpermission:assembleDebug
```

**Release build:**
```bash
./gradlew :libpermission:assembleRelease
```

**Build all variants:**
```bash
./gradlew :libpermission:build
```

After building, the AAR files will be located in:
```
libpermission/build/outputs/aar/
  - libpermission-debug.aar
  - libpermission-release.aar
```

### Add to Project

Add the AAR file to your project's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("path/to/libpermission-release.aar"))
}
```

**Example with relative path:**
```kotlin
dependencies {
    implementation(files("../libpermission/build/outputs/aar/libpermission-release.aar"))
}
```

**Example with absolute path:**
```kotlin
dependencies {
    implementation(files("/home/user/libs/libpermission-release.aar"))
}
```

---

## Quick Start

### 1. Add imports:
```java
import com.enginepermissions.toolkit.Engine;
import com.enginepermissions.toolkit.Stage;
```

### 2. Create Engine instance:
```java
Engine permissionEngine = new Engine(this);
```

### 3. Integrate with Activity:
```java
@Override
protected void onResume() {
    super.onResume();
    if (permissionEngine != null) {
        permissionEngine.onResume();
    }
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (permissionEngine != null) {
        permissionEngine.onActivityResult(requestCode, resultCode, data);
    }
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (permissionEngine != null) {
        permissionEngine.onDestroy();
    }
}
```

---

## Basic Usage

### Initialization
```java
public class MainActivity extends Activity {
    private Engine permissionEngine;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionEngine = new Engine(this);
    }
}
```

### Set Permission Order
```java
Stage[] order = new Stage[] {
    Stage.ACCESSIBILITY,
    Stage.OVERLAY,
    Stage.MANAGE_STORAGE
};
permissionEngine.setPermissionOrder(order);
```

### Check Status
```java
Status status = permissionEngine.getStatus();
boolean allGranted = status.allGranted;
boolean hasOverlay = status.overlay;
```

### Start Permission Flow
```java
permissionEngine.start();
```

### Set Callbacks
```java
permissionEngine.setStageCallback(Stage.OVERLAY, () -> {
    Log.d("Permissions", "Overlay requested");
});
```

### Set Listener
```java
permissionEngine.setListener(new Listener() {
    @Override
    public void onPermissionGranted(Stage stage) {
        Log.d("Permissions", "Granted: " + stage);
    }
    
    @Override
    public void onPermissionDenied(Stage stage) {
        Log.d("Permissions", "Denied: " + stage);
    }
    
    @Override
    public void onAllPermissionsGranted() {
        Log.d("Permissions", "All permissions granted!");
    }
    
    @Override
    public void onStageChanged(Stage currentStage) {
        Log.d("Permissions", "Stage changed: " + currentStage);
    }
});
```

---

## API Reference

### Engine Methods

**Initialization:**
- `Engine(Activity activity)` - Create Engine instance
- `setPermissionOrder(Stage[] order)` - Set permission request order
- `start()` - Start permission request flow
- `stop()` - Stop the flow

**Status:**
- `getStatus()` - Get status of all permissions (returns `Status`)
- `areAllGranted()` - Check if all permissions are granted
- `getCurrentStage()` - Get current stage
- `isComplete()` - Check if process is complete

**Callbacks:**
- `setStageCallback(Stage stage, Runnable callback)` - Set callback for specific stage
- `setStageCallbacks(Map<Stage, Runnable> callbacks)` - Set multiple callbacks

**Listeners:**
- `setListener(Listener listener)` - Set event listener

**Activity Integration:**
- `onResume()` - Call in `Activity.onResume()`
- `onPause()` - Call in `Activity.onPause()`
- `onActivityResult(int requestCode, int resultCode, Intent data)` - Call in `Activity.onActivityResult()`
- `onDestroy()` - Call in `Activity.onDestroy()`

### Stage Enum
```java
public enum Stage {
    ACCESSIBILITY,
    OVERLAY,
    MANAGE_STORAGE,
    BATTERY_OPTIMIZATIONS,
    CHANGE_SMS_MANAGER,
    COMPLETE
}
```

### Status Class
```java
public class Status {
    public final boolean accessibility;
    public final boolean overlay;
    public final boolean needManageStorage;
    public final boolean batteryOptimizations;
    public final boolean sms;
    public final boolean allGranted;
    
    public int getGrantedCount();
    public int getTotalCount();
}
```

### Listener Interface
```java
public interface Listener {
    void onPermissionGranted(Stage stage);
    void onPermissionDenied(Stage stage);
    void onAllPermissionsGranted();
    void onStageChanged(Stage currentStage);
}
```

---

## Examples

### Example 1: Basic Setup
```java
public class MainActivity extends Activity {
    private Engine permissionEngine;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionEngine = new Engine(this);
        
        Stage[] order = new Stage[] {
            Stage.ACCESSIBILITY,
            Stage.OVERLAY
        };
        permissionEngine.setPermissionOrder(order);
        permissionEngine.start();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (permissionEngine != null) {
            permissionEngine.onResume();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (permissionEngine != null) {
            permissionEngine.onActivityResult(requestCode, resultCode, data);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (permissionEngine != null) {
            permissionEngine.onDestroy();
        }
    }
}
```

### Example 2: Check Before Request
```java
public void requestOverlayIfNeeded() {
    if (!permissionEngine.getStatus().overlay) {
        permissionEngine.setStageCallback(Stage.OVERLAY, () -> {
            Log.d("Permissions", "Overlay permission requested");
        });
        permissionEngine.start();
    }
}
```

### Example 3: Using Listener
```java
permissionEngine.setListener(new Listener() {
    @Override
    public void onAllPermissionsGranted() {
        startMainFunctionality();
    }
    
    @Override
    public void onPermissionGranted(Stage stage) {
        switch (stage) {
            case OVERLAY:
                showOverlayWindow();
                break;
        }
    }
    
    @Override
    public void onPermissionDenied(Stage stage) {
        showPermissionDeniedDialog(stage);
    }
    
    @Override
    public void onStageChanged(Stage currentStage) {
        updateUI(currentStage);
    }
});
```

---

## Important Notes

1. **Always call lifecycle methods:**
   - `onResume()` in `Activity.onResume()`
   - `onActivityResult()` in `Activity.onActivityResult()`
   - `onDestroy()` in `Activity.onDestroy()`

2. **Permission order:**
   - Set order via `setPermissionOrder()` before `start()`
   - Default order is used if not set

3. **Callbacks:**
   - Callbacks are invoked after permission request
   - Use `getStatus()` to check already granted permissions
