package com.enginepermissions.library;

public class Status {
    public final boolean accessibility;
    public final boolean overlay;
    public final boolean needManageStorage;
    public final boolean batteryOptimizations;
    public final boolean sms;
    public final boolean allGranted;

    public Status(boolean accessibility, boolean overlay, 
                 boolean needManageStorage, boolean batteryOptimizations, boolean sms) {
        this.accessibility = accessibility;
        this.overlay = overlay;
        this.needManageStorage = needManageStorage;
        this.batteryOptimizations = batteryOptimizations;
        this.sms = sms;
        this.allGranted = accessibility && overlay && !needManageStorage && batteryOptimizations && sms;
    }

    public int getGrantedCount() {
        int count = 0;
        if (accessibility) count++;
        if (overlay) count++;
        if (!needManageStorage) count++;
        if (batteryOptimizations) count++;
        if (sms) count++;
        return count;
    }

    public int getTotalCount() {
        return 5;
    }
}

