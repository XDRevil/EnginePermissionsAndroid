package com.enginepermissions.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.Flow.FlowManager;
import com.enginepermissions.library.Requester.Requester;
import java.util.HashMap;
import java.util.Map;

public class Engine {
    private final Activity activity;
    private final Handler handler;
    private final Checker checker;
    private final Requester requester;
    private final FlowManager flowManager;
    private final StateManager state;
    private final ResultHandler resultHandler;
    private final SmsHandler smsHandler;
    private Listener listener;
    private final Map<Stage, Runnable> stageCallbacks = new HashMap<>();

    public Engine(Activity activity) {
        this.activity = activity;
        this.handler = new Handler(Looper.getMainLooper());
        this.state = new StateManager();
        this.checker = new Checker(activity);
        this.requester = new Requester(activity, state, checker,
            RequestConstants.REQ_SMS, 
            RequestConstants.REQ_ACC, 
            RequestConstants.REQ_OVR, 
            RequestConstants.REQ_STG,
            RequestConstants.REQ_BATTERY);
        this.flowManager = new FlowManager(activity, checker, requester);
        this.flowManager.setStageCallbackHandler(this::executeStageCallback);
        this.smsHandler = new SmsHandler(checker, state, requester, flowManager);
        this.resultHandler = new ResultHandler(checker, state, flowManager, smsHandler);
        setupListener();
    }

    private void executeStageCallback(Stage stage) {
        Runnable callback = stageCallbacks.get(stage);
        if (callback != null) {
            handler.post(callback);
        }
    }

    private void setupListener() {
        flowManager.setListener(new Listener() {
            @Override
            public void onPermissionGranted(Stage stage) {
                if (listener != null) listener.onPermissionGranted(stage);
            }

            @Override
            public void onPermissionDenied(Stage stage) {
                if (listener != null) listener.onPermissionDenied(stage);
            }

            @Override
            public void onAllPermissionsGranted() {
                state.resetSmsRetry();
                if (listener != null) listener.onAllPermissionsGranted();
            }

            @Override
            public void onStageChanged(Stage currentStage) {
                if (listener != null) listener.onStageChanged(currentStage);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPermissionOrder(Stage[] order) {
        state.resetAllInFlight();
        flowManager.setPermissionOrder(order);
    }

    public void setStageCallback(Stage stage, Runnable callback) {
        if (callback != null) {
            stageCallbacks.put(stage, callback);
        } else {
            stageCallbacks.remove(stage);
        }
    }

    public void setStageCallbacks(Map<Stage, Runnable> callbacks) {
        if (callbacks != null) {
            stageCallbacks.clear();
            stageCallbacks.putAll(callbacks);
        }
    }

    public void onResume() {
        if (!state.hasAnyInFlight()) {
            flowManager.onResume();
        } else {
            handler.post(() -> {
                if (!state.hasAnyInFlight()) {
                    flowManager.onResume();
                }
            });
        }
    }

    public void onPause() {
        flowManager.pause();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        resultHandler.handleResult(requestCode, resultCode, data);
    }

    public void requestSMS() {
        smsHandler.requestSMS();
    }

    public Status getStatus() {
        return checker.getAllStatus();
    }

    public boolean areAllGranted() {
        return checker.checkAll();
    }

    public Stage getCurrentStage() {
        return flowManager.getCurrentStage();
    }

    public boolean isComplete() {
        return flowManager.isComplete();
    }

    public void start() {
        flowManager.resume();
    }

    public void stop() {
        flowManager.pause();
    }

    public void onDestroy() {
        flowManager.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

