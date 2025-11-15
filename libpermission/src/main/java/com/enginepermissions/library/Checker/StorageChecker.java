package com.enginepermissions.library.Checker;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;

public class StorageChecker {
    private final Activity activity;

    public StorageChecker(Activity activity) {
        this.activity = activity;
    }

    public boolean needManageStorage() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager();
    }
}

