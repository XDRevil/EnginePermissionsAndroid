package com.enginepermissions.library.Requester;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class SmsRequester {
    private final Activity activity;
    private final StateManager state;
    private final Checker checker;
    private final int requestCode;

    public SmsRequester(Activity activity, StateManager state, Checker checker, int requestCode) {
        this.activity = activity;
        this.state = state;
        this.checker = checker;
        this.requestCode = requestCode;
    }

    public void request() {
        if (state.isInFlight(0)) {
            if (state.isRequestTimedOut(0)) {
                state.setInFlight(0, false);
            } else {
                return;
            }
        }
        if (checker.checkSMSManagerChange()) {
            state.setInFlight(0, false);
            return;
        }
        state.setInFlight(0, true);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RoleManager rm = (RoleManager) activity.getSystemService(Activity.ROLE_SERVICE);
                if (rm != null && rm.isRoleAvailable(RoleManager.ROLE_SMS) && !rm.isRoleHeld(RoleManager.ROLE_SMS)) {
                    activity.startActivityForResult(rm.createRequestRoleIntent(RoleManager.ROLE_SMS), requestCode);
                } else {
                    state.setInFlight(0, false);
                }
            } else {
                Intent i = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                i.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, activity.getPackageName());
                activity.startActivityForResult(i, requestCode);
            }
        } catch (Exception e) {
            state.setInFlight(0, false);
        }
    }
}

