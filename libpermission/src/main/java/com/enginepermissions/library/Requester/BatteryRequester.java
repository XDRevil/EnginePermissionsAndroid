package com.enginepermissions.library.Requester;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class BatteryRequester extends BaseRequester {
    public BatteryRequester(Activity activity, StateManager state, Checker checker, int requestCode) {
        super(activity, state, checker, requestCode, 4);
    }

    @Override
    protected boolean isGranted() {
        return checker.checkBatteryOptimizations();
    }

    @Override
    protected boolean performRequest() throws Exception {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        PowerManager pm = (PowerManager) activity.getSystemService(Activity.POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(activity.getPackageName())) {
            Intent i = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            i.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(i, requestCode);
            return true;
        }
        return false;
    }
}

