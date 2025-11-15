package com.enginepermissions.library;

public interface Listener {
    void onPermissionGranted(Stage stage);
    void onPermissionDenied(Stage stage);
    void onAllPermissionsGranted();
    void onStageChanged(Stage currentStage);
}

