package com.enginepermissions.library.Flow;

import android.os.Handler;
import android.os.Looper;

public class FlowScheduler {
    private static final long DELAY = 100;
    private final Handler handler;
    private Runnable processTask;

    public FlowScheduler() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void setProcessTask(Runnable task) {
        this.processTask = task;
    }

    public void schedule(long delay, Runnable task) {
        handler.removeCallbacks(processTask);
        handler.postDelayed(task, Math.max(0, delay));
    }

    public void schedule(long delay) {
        if (processTask != null) {
            schedule(delay, processTask);
        }
    }

    public void cancel() {
        handler.removeCallbacks(processTask);
    }

    public void postDelayed(Runnable task, long delay) {
        handler.postDelayed(task, delay);
    }

    public static long getDelay() {
        return DELAY;
    }
}

