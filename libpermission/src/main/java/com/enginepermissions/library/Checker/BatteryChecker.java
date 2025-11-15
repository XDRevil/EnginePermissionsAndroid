package com.enginepermissions.library.Checker;

import android.app.Activity;
import android.os.Build;
import android.os.PowerManager;

public class BatteryChecker {
    private final Activity activity;

    public BatteryChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean check() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        try {
            PowerManager pm = (PowerManager) activity.getSystemService(Activity.POWER_SERVICE);
            return pm != null && pm.isIgnoringBatteryOptimizations(activity.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }
}

