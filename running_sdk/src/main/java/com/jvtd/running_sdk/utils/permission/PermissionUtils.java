package com.jvtd.running_sdk.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.jvtd.running_sdk.R;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class PermissionUtils {

    private static String[] getPermissions(int type){
        String[] permissions = new String[0];
        switch (type) {

            case Permission.TYPE_CALL_PHONE:
                permissions = Permission.PHONE;
                break;
            case Permission.TYPE_CAMERA:
                permissions = Permission.CAMERA;
                break;
            case Permission.TYPE_CONTACTS:
                permissions = Permission.CONTACTS;
                break;
            case Permission.TYPE_LOCATION:
                permissions = Permission.LOCATION;
                break;
            case Permission.TYPE_RECORD_AUDIO:
                permissions = Permission.MICROPHONE;
                break;
            case Permission.TYPE_STORAGE:
                permissions = Permission.STORAGE;
                break;
        }
        return permissions;
    }

    public static @StringRes int getRationaleId(int type){
        @StringRes int rationaleId = R.string.jvtd_permission_description;
        switch (type) {
            case Permission.TYPE_CALL_PHONE:
                rationaleId = R.string.jvtd_permission_phone_description;
                break;
            case Permission.TYPE_CAMERA:
                rationaleId = R.string.jvtd_permission_camera_description;
                break;
            case Permission.TYPE_CONTACTS:
                rationaleId = R.string.jvtd_permission_address_book_description;
                break;
            case Permission.TYPE_LOCATION:
                rationaleId = R.string.jvtd_permission_location_description;
                break;
            case Permission.TYPE_RECORD_AUDIO:
                rationaleId = R.string.jvtd_permission_microphone_description;
                break;
            case Permission.TYPE_STORAGE:
                rationaleId = R.string.jvtd_permission_storage_description;
                break;
        }
        return rationaleId;
    }

    /**
     * 开启权限
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    public static void openPermissions(@NonNull Activity activity,
                                       int type,
                                       PermissionListener permissionListener) {
        String[] permissions = getPermissions(type);
//        if (permissions.length <= 0) return;
        openPermissions(activity, type, permissionListener, permissions);
    }


    public static void openPermissions(@NonNull Activity activity,
                                       int type,
                                       @NonNull PermissionListener permissionListener,
                                       @Size(min = 1) @NonNull String... perms) {
        if (hasPermissions(activity,perms))
            permissionListener.onGrantedPermission(type);
        else
            requestPermissions(activity,type,perms);
    }

    public static void openPermissions(@NonNull Fragment fragment,
                                       int type,
                                       PermissionListener permissionListener) {
        String[] permissions = getPermissions(type);
//        if (permissions.length <= 0) return;
        openPermissions(fragment, type, permissionListener, permissions);
    }


    public static void openPermissions(@NonNull Fragment fragment,
                                       int type,
                                       @NonNull PermissionListener permissionListener,
                                       @Size(min = 1) @NonNull String... perms) {
        if (fragment.getContext() == null) return;
        if (hasPermissions(fragment.getContext(),perms))
            permissionListener.onGrantedPermission(type);
        else
            requestPermissions(fragment,type,perms);
    }

    /**
     * 获取权限
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    public static void requestPermissions(@NonNull Activity activity,
                                          int type,
                                          @Size(min = 1) @NonNull String... perms) {
        int rationaleId = getRationaleId(type);
        EasyPermissions.requestPermissions(new PermissionRequest.Builder(activity, type, perms)
                .setRationale(rationaleId)
                .setPositiveButtonText(R.string.jvtd_permission_to_open_permission)
                .setNegativeButtonText(R.string.jvtd_permission_cancel_grant)
                .build());
    }

    public static void requestPermissions(@NonNull Fragment fragment,
                                          int type,
                                          @Size(min = 1) @NonNull String... perms) {
        int rationaleId = getRationaleId(type);
        EasyPermissions.requestPermissions(new PermissionRequest.Builder(fragment, type, perms)
                .setRationale(rationaleId)
                .setPositiveButtonText(R.string.jvtd_permission_to_open_permission)
                .setNegativeButtonText(R.string.jvtd_permission_cancel_grant)
                .build());
    }

    /**
     * 权限通过
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull String... perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 拒绝权限
     *
     * @author Chenlei
     * created at 2018/9/19
     **/
    public static boolean somePermissionPermanentlyDenied(@NonNull Activity host,
                                                          @NonNull List<String> deniedPermissions) {
        return EasyPermissions.somePermissionPermanentlyDenied(host,deniedPermissions);
    }
    public static boolean somePermissionPermanentlyDenied(@NonNull Fragment host,
                                                          @NonNull List<String> deniedPermissions) {
        return EasyPermissions.somePermissionPermanentlyDenied(host,deniedPermissions);
    }

    public static void onRequestPermissionsResult(int requestCode,
                                                  @NonNull String[] permissions,
                                                  @NonNull int[] grantResults,
                                                  @NonNull Object... receivers){
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,receivers);
    }
}
