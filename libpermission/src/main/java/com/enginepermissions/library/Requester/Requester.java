package com.enginepermissions.library.Requester;

import android.app.Activity;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public class Requester {
    private final AccessibilityRequester accessibilityRequester;
    private final OverlayRequester overlayRequester;
    private final StorageRequester storageRequester;
    private final BatteryRequester batteryRequester;
    private final SmsRequester smsRequester;

    public Requester(Activity activity, StateManager state, Checker checker,
                    int reqSms, int reqAcc, int reqOvr, int reqStg, int reqBattery) {
        this.accessibilityRequester = new AccessibilityRequester(activity, state, checker, reqAcc);
        this.overlayRequester = new OverlayRequester(activity, state, checker, reqOvr);
        this.storageRequester = new StorageRequester(activity, state, checker, reqStg);
        this.batteryRequester = new BatteryRequester(activity, state, checker, reqBattery);
        this.smsRequester = new SmsRequester(activity, state, checker, reqSms);
    }

    public boolean requestAccessibility() {
        return accessibilityRequester.request();
    }

    public boolean requestOverlay() {
        return overlayRequester.request();
    }

    public boolean requestManageStorage() {
        return storageRequester.request();
    }

    public boolean requestBatteryOptimizations() {
        return batteryRequester.request();
    }

    public void requestSMS() {
        smsRequester.request();
    }
}

