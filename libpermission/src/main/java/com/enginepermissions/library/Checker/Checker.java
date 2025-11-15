package com.enginepermissions.library.Checker;

import android.app.Activity;
import com.enginepermissions.library.Status;

public class Checker {
    private final AccessibilityChecker accessibilityChecker;
    private final OverlayChecker overlayChecker;
    private final StorageChecker storageChecker;
    private final BatteryChecker batteryChecker;
    private final SmsChecker smsChecker;

    public Checker(Activity activity) {
        this.accessibilityChecker = new AccessibilityChecker(activity);
        this.overlayChecker = new OverlayChecker(activity);
        this.storageChecker = new StorageChecker(activity);
        this.batteryChecker = new BatteryChecker(activity);
        this.smsChecker = new SmsChecker(activity);
    }

    public boolean checkAccessibility() {
        return accessibilityChecker.check();
    }

    public boolean checkOverlay() {
        return overlayChecker.check();
    }

    public boolean needManageStorage() {
        return storageChecker.needManageStorage();
    }

    public boolean checkSMSManagerChange() {
        return smsChecker.checkSMSManagerChange();
    }

    public boolean checkBatteryOptimizations() {
        return batteryChecker.check();
    }

    public Status getAllStatus() {
        return new Status(
            checkAccessibility(),
            checkOverlay(),
            needManageStorage(),
            checkBatteryOptimizations(),
            checkSMSManagerChange()
        );
    }

    public boolean checkAll() {
        Status status = getAllStatus();
        return status.allGranted;
    }
}

