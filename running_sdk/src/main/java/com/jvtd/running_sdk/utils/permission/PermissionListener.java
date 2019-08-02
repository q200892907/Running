package com.jvtd.running_sdk.utils.permission;

public interface PermissionListener {
    void onGrantedPermission(@Permission.PermissionType int permissionType);
}
