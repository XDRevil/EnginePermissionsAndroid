package com.enginepermissions.library.Requester;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class OverlayRequester extends BaseRequester {
    private static final String MIUI_ACT = "miui.intent.action.APP_PERM_EDITOR";
    private static final String MIUI_PKG = "com.miui.securitycenter";
    private static final String MIUI_CLS1 = "com.miui.permcenter.permissions.PermissionsEditorActivity";
    private final boolean isXiaomi;

    public OverlayRequester(Activity activity, StateManager state, Checker checker, int requestCode) {
        super(activity, state, checker, requestCode, 2);
        this.isXiaomi = "xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    @Override
    protected boolean isGranted() {
        return checker.checkOverlay();
    }

    @Override
    protected boolean performRequest() throws Exception {
        Intent i;
        if (isXiaomi) {
            i = new Intent(MIUI_ACT);
            i.setClassName(MIUI_PKG, MIUI_CLS1);
            i.putExtra("extra_pkgname", activity.getPackageName());
        } else {
            i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, 
                          Uri.parse("package:" + activity.getPackageName()));
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivityForResult(i, requestCode);
        return true;
    }
}

