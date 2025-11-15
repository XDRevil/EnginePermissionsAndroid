package com.enginepermissions.library;

public class StateManager {
    private static final int MAX_RETRY = 3;
    private int smsRetry = 0;
    private boolean[] inFlight = new boolean[6];
    private long[] requestStartTime = new long[6];
    private long lastRequestTime = 0;
    private static final long MIN_REQUEST_INTERVAL = 100;
    private static final long REQUEST_TIMEOUT = 30000;

    public boolean isInFlight(int index) {
        if (index < 0 || index >= inFlight.length) {
            return false;
        }
        if (!inFlight[index]) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - requestStartTime[index];
        if (elapsed > REQUEST_TIMEOUT) {
            inFlight[index] = false;
            return false;
        }
        return true;
    }

    public void setInFlight(int index, boolean value) {
        if (index >= 0 && index < inFlight.length) {
            inFlight[index] = value;
            if (value) {
                requestStartTime[index] = System.currentTimeMillis();
                lastRequestTime = System.currentTimeMillis();
            } else {
                requestStartTime[index] = 0;
            }
        }
    }

    public void resetAllInFlight() {
        for (int i = 0; i < inFlight.length; i++) {
            inFlight[i] = false;
            requestStartTime[i] = 0;
        }
    }
    
    public boolean isRequestTimedOut(int index) {
        if (index < 0 || index >= inFlight.length || !inFlight[index]) {
            return false;
        }
        return System.currentTimeMillis() - requestStartTime[index] > REQUEST_TIMEOUT;
    }

    public boolean canRetrySMS() {
        return smsRetry < MAX_RETRY;
    }

    public void incrementSmsRetry() {
        if (smsRetry < MAX_RETRY) smsRetry++;
    }

    public void resetSmsRetry() {
        smsRetry = 0;
    }

    public int getSmsRetry() {
        return smsRetry;
    }

    public boolean canMakeRequest() {
        return System.currentTimeMillis() - lastRequestTime >= MIN_REQUEST_INTERVAL;
    }

    public boolean hasAnyInFlight() {
        for (boolean flight : inFlight) {
            if (flight) return true;
        }
        return false;
    }
}

