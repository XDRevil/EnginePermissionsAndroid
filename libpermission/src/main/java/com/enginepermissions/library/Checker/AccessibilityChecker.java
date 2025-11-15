package com.enginepermissions.library.Checker;

import android.app.Activity;
import android.content.ComponentName;
import android.provider.Settings;
import android.text.TextUtils;

public class AccessibilityChecker {
    private final Activity activity;

    public AccessibilityChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean check() {
        try {
            String packageName = activity.getPackageName();
            String enabledServices = Settings.Secure.getString(
                activity.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );
            
            if (enabledServices == null || enabledServices.isEmpty()) {
                return false;
            }

            TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
            splitter.setString(enabledServices);

            while (splitter.hasNext()) {
                String componentNameString = splitter.next();
                ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);
                
                if (enabledService != null && packageName.equals(enabledService.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}

