package com.enginepermissions.library.Flow;

import com.enginepermissions.library.Listener;
import com.enginepermissions.library.Stage;

public class FlowNotifier {
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyStageChanged(Stage stage) {
        if (listener != null) {
            listener.onStageChanged(stage);
        }
    }

    public void notifyPermissionGranted(Stage stage) {
        if (listener != null) {
            listener.onPermissionGranted(stage);
        }
    }

    public void notifyAllGranted() {
        if (listener != null) {
            listener.onAllPermissionsGranted();
        }
    }

    public void clear() {
        listener = null;
    }
}

