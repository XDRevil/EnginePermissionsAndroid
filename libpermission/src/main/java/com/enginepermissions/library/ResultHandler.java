package com.enginepermissions.library;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.Flow.FlowManager;

public class ResultHandler {
    private final Checker checker;
    private final StateManager state;
    private final FlowManager flowManager;
    private final SmsHandler smsHandler;
    private final Handler handler;
    
    public ResultHandler(Checker checker, 
                        StateManager state,
                        FlowManager flowManager,
                        SmsHandler smsHandler) {
        this.checker = checker;
        this.state = state;
        this.flowManager = flowManager;
        this.smsHandler = smsHandler;
        this.handler = new Handler(Looper.getMainLooper());
    }
    
    private void continueFlow() {
        flowManager.schedule(0);
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestConstants.REQ_SMS) {
            handleSmsResult();
        } else if (requestCode == RequestConstants.REQ_ACC) {
            handleAccessibilityResult();
        } else if (requestCode == RequestConstants.REQ_OVR) {
            handleOverlayResult();
        } else if (requestCode == RequestConstants.REQ_STG) {
            handleStorageResult();
        } else if (requestCode == RequestConstants.REQ_BATTERY) {
            handleBatteryResult();
        }
    }

    private void handleSmsResult() {
        boolean wasSmsManager = state.isInFlight(5);
        state.setInFlight(0, false);
        state.setInFlight(5, false);
        boolean smsGranted = checker.checkSMSManagerChange();
        if (smsGranted) {
            state.resetSmsRetry();
            continueFlow();
        } else if (state.canRetrySMS()) {
            state.incrementSmsRetry();
            if (wasSmsManager) {
                smsHandler.requestSMSManager();
            } else {
                smsHandler.requestSMS();
            }
        } else {
            continueFlow();
        }
    }

    private void handleAccessibilityResult() {
        state.setInFlight(1, false);
        continueFlow();
    }

    private void handleOverlayResult() {
        state.setInFlight(2, false);
        boolean granted = checker.checkOverlay();
        if (granted) {
            continueFlow();
        } else {
            handler.post(() -> {
                checker.checkOverlay();
                continueFlow();
            });
        }
    }

    private void handleStorageResult() {
        state.setInFlight(3, false);
        continueFlow();
    }

    private void handleBatteryResult() {
        state.setInFlight(4, false);
        continueFlow();
    }
}

