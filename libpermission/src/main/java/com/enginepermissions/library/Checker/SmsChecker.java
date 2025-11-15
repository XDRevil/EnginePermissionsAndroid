package com.enginepermissions.library.Checker;

import android.app.Activity;
import android.app.role.RoleManager;
import android.os.Build;
import android.provider.Telephony;

public class SmsChecker {
    private final Activity activity;

    public SmsChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean checkSMSManagerChange() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RoleManager rm = (RoleManager) activity.getSystemService(Activity.ROLE_SERVICE);
                return rm != null && rm.isRoleHeld(RoleManager.ROLE_SMS);
            }
            String def = Telephony.Sms.getDefaultSmsPackage(activity);
            return def != null && def.equals(activity.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }
}

