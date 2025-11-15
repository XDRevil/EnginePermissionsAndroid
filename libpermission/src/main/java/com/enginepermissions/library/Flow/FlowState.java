package com.enginepermissions.library.Flow;

import com.enginepermissions.library.Stage;

public class FlowState {
    private Stage[] order;
    private Stage stage = Stage.COMPLETE;
    private boolean paused = false;

    public Stage[] getOrder() {
        return order;
    }

    public void setOrder(Stage[] order) {
        this.order = order;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Stage[] getDefaultOrder() {
        return new Stage[] { 
            Stage.ACCESSIBILITY, 
            Stage.OVERLAY, 
            Stage.MANAGE_STORAGE, 
            Stage.BATTERY_OPTIMIZATIONS, 
            Stage.CHANGE_SMS_MANAGER 
        };
    }

    public Stage getNext() {
        if (order == null || order.length == 0) return Stage.COMPLETE;
        for (int i = 0; i < order.length; i++) {
            if (order[i] == stage && i + 1 < order.length) {
                return order[i + 1];
            }
        }
        return Stage.COMPLETE;
    }

    public boolean isComplete() {
        return stage == Stage.COMPLETE;
    }
}

