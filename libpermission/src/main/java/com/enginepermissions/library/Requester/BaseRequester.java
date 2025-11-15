package com.enginepermissions.library.Requester;

import android.app.Activity;
import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.StateManager;

public abstract class BaseRequester implements PermissionRequester {
    protected final Activity activity;
    protected final StateManager state;
    protected final Checker checker;
    protected final int requestCode;
    protected final int stateIndex;

    protected BaseRequester(Activity activity, StateManager state, Checker checker, 
                           int requestCode, int stateIndex) {
        this.activity = activity;
        this.state = state;
        this.checker = checker;
        this.requestCode = requestCode;
        this.stateIndex = stateIndex;
    }

    protected boolean checkInFlight() {
        if (state.isInFlight(stateIndex)) {
            if (state.isRequestTimedOut(stateIndex)) {
                state.setInFlight(stateIndex, false);
            } else {
                return false;
            }
        }
        return true;
    }

    protected void setInFlight(boolean value) {
        state.setInFlight(stateIndex, value);
    }

    protected abstract boolean isGranted();
    protected abstract boolean performRequest() throws Exception;

    public boolean request() {
        if (!checkInFlight()) {
            return false;
        }
        if (isGranted()) {
            setInFlight(false);
            return false;
        }
        setInFlight(true);
        try {
            return performRequest();
        } catch (Exception e) {
            setInFlight(false);
            return false;
        }
    }
}

