# Permissions Engine Toolkit - Usage Guide
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

echo $ANDROID_SDK_ROOT

nano local.properties 

sdk.dir=/media/vaganov/44FE3B91FE3B79EE/AndroidSDK to "sdk.dir=your Android SDK($ANDROID_SDK_ROOT)"


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

