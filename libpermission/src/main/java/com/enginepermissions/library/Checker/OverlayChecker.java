package com.enginepermissions.library.Checker;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

public class OverlayChecker {
    private final Activity activity;

    public OverlayChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean check() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        try {
            return Settings.canDrawOverlays(activity);
        } catch (Exception e) {
            return false;
        }
    }
}

