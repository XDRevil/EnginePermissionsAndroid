package com.enginepermissions.library.Flow;

import android.app.Activity;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.Listener;
import com.enginepermissions.library.Requester.Requester;
import com.enginepermissions.library.Stage;
import java.util.function.Consumer;

public class FlowManager {
    private static final long DELAY = FlowScheduler.getDelay();

    private final FlowState state;
    private final FlowScheduler scheduler;
    private final FlowNotifier notifier;
    private final StageProcessor processor;

    public FlowManager(Activity activity, Checker checker, Requester requester) {
        this.state = new FlowState();
        this.scheduler = new FlowScheduler();
        this.notifier = new FlowNotifier();
        this.processor = new StageProcessor(activity, checker, requester);
        
        initialize();
    }

    private void initialize() {
        state.setOrder(state.getDefaultOrder());
        state.setStage(state.getOrder()[0]);
        scheduler.setProcessTask(this::process);
        processor.setScheduleCallback(delay -> scheduler.schedule(delay));
    }

    public void setListener(Listener listener) {
        notifier.setListener(listener);
    }

    public void setStageCallbackHandler(Consumer<Stage> callbackHandler) {
        processor.setStageCallbackHandler(callbackHandler);
    }

    public void setPermissionOrder(Stage[] order) {
        Stage[] validOrder = (order != null && order.length > 0) ? order.clone() : state.getDefaultOrder();
        state.setOrder(validOrder);
        state.setStage(validOrder.length > 0 ? validOrder[0] : Stage.ACCESSIBILITY);
        state.setPaused(false);
        scheduler.schedule(0);
    }
    
    public void processImmediate() {
        if (!state.isPaused() && !state.isComplete()) {
            process();
        }
    }

    public void onResume() {
        state.setPaused(false);
        if (!state.isComplete()) {
            processImmediate();
        }
    }

    public void pause() {
        state.setPaused(true);
        scheduler.cancel();
    }

    public void resume() {
        state.setPaused(false);
        if (!state.isComplete()) {
            processImmediate();
        }
    }

    public void schedule(long delay) {
        if (state.isPaused()) return;
        scheduler.schedule(delay);
    }

    public void onDestroy() {
        scheduler.cancel();
        notifier.clear();
    }

    public Stage getCurrentStage() {
        return state.getStage();
    }

    public boolean isComplete() {
        return state.isComplete();
    }

    private void process() {
        if (state.isPaused()) {
            return;
        }
        while (!state.isComplete()) {
            Stage currentStage = state.getStage();
            boolean completed = processor.processStage(currentStage);
            if (!completed) {
                return;
            }
            notifier.notifyPermissionGranted(currentStage);
            Stage next = state.getNext();
            if (next == null || next == Stage.COMPLETE) {
                state.setStage(Stage.COMPLETE);
                notifier.notifyAllGranted();
                return;
            }
            state.setStage(next);
            notifier.notifyStageChanged(next);
        }
    }
}

