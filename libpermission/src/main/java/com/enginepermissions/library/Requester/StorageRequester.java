package com.enginepermissions.library.Requester;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class StorageRequester extends BaseRequester {
    public StorageRequester(Activity activity, StateManager state, Checker checker, int requestCode) {
        super(activity, state, checker, requestCode, 3);
    }

    @Override
    protected boolean isGranted() {
        return !checker.needManageStorage();
    }

    @Override
    protected boolean performRequest() throws Exception {
        try {
            Intent i = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            i.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(i, requestCode);
            return true;
        } catch (Exception e) {
            activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), requestCode);
            return true;
        }
    }
}

