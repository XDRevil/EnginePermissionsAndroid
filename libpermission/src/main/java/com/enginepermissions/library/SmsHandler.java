package com.enginepermissions.library;

import com.enginepermissions.library.Checker.Checker;
import com.enginepermissions.library.Flow.FlowManager;
import com.enginepermissions.library.Requester.Requester;

public class SmsHandler {
    private final Checker checker;
    private final StateManager state;
    private final Requester requester;
    private final FlowManager flowManager;
    
    public SmsHandler(Checker checker,
                     StateManager state,
                     Requester requester,
                     FlowManager flowManager) {
        this.checker = checker;
        this.state = state;
        this.requester = requester;
        this.flowManager = flowManager;
    }

    public void requestSMS() {
        if (checker.checkSMSManagerChange()) {
            if (state.isInFlight(5)) state.setInFlight(5, false);
            if (state.isInFlight(0)) state.setInFlight(0, false);
            return;
        }
        if (!state.canMakeRequest()) {
            flowManager.schedule(0);
            return;
        }
        requester.requestSMS();
        if (state.isInFlight(5)) state.setInFlight(5, false);
    }

    public void requestSMSManager() {
        if (state.isInFlight(5)) {
            if (state.isRequestTimedOut(5)) {
                state.setInFlight(5, false);
            } else {
                flowManager.schedule(0);
                return;
            }
        }
        state.setInFlight(5, true);
        requestSMS();
    }
}

