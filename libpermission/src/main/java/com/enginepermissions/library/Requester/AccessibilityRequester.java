package com.enginepermissions.library.Requester;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class AccessibilityRequester extends BaseRequester {
    public AccessibilityRequester(Activity activity, StateManager state, Checker checker, int requestCode) {
        super(activity, state, checker, requestCode, 1);
    }

    @Override
    protected boolean isGranted() {
        return checker.checkAccessibility();
    }

    @Override
    protected boolean performRequest() throws Exception {
        Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivityForResult(i, requestCode);
        return true;
    }
}

