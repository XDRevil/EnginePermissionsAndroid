# Permissions Engine Toolkit - Usage Guide
 

---
 

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
 
```kotlin Kts Gradle
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
 
```java
Stage[] order = new Stage[] {
    Stage.ACCESSIBILITY,
    Stage.OVERLAY,
    Stage.MANAGE_STORAGE
};
permissionEngine.setPermissionOrder(order);
```
 
```java
Status status = permissionEngine.getStatus();
boolean allGranted = status.allGranted;
boolean hasOverlay = status.overlay;
```
 
```java
permissionEngine.start();
```

 
```java
permissionEngine.setStageCallback(Stage.OVERLAY, () -> {
    Log.d("Permissions", "Overlay requested");
});
```

 
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

