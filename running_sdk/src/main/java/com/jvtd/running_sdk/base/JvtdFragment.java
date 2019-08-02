package com.jvtd.running_sdk.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.eventBus.EventCenter;
import com.jvtd.running_sdk.utils.permission.Permission;
import com.jvtd.running_sdk.utils.permission.PermissionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/10/16.
 * 无 MVP 的 Fragment 基类
 */

public abstract class JvtdFragment extends SupportFragment implements Toolbar.OnMenuItemClickListener, View.OnTouchListener, EasyPermissions.PermissionCallbacks {
    protected final String TAG = getClass().getSimpleName();
    private CompositeDisposable mCompositeDisposable;
    protected Context mContext;
    protected View mView;
    private int mType;

    /**
     * 创建方法  必须实现
     *
     * @author Chenlei
     * created at 2018/9/25
     **/
//    public static JvtdFragment newInstance(){
//        return null;
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCompositeDisposable = new CompositeDisposable();
        mView = getLayout(inflater, container);
        onCreateMap(savedInstanceState);
        return mView;
    }

    protected void onCreateMap(Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        bindView(view);
    }

    /**
     * 绑定界面  用于第三方界面绑定
     *
     * @author Chenlei
     * created at 2018/9/25
     **/
    protected abstract void bindView(@NonNull View view);

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initViewAndData();
    }

    protected ImmersionBar getBar() {
        if (getActivity() == null) return null;
        return ((JvtdActivity) getActivity()).getBar();
    }

    /**
     * 设置 Toolbar
     *
     * @param toolbar  Toolbar
     * @param backIcon true - 显示返回箭头
     */
    protected void setToolbar(Toolbar toolbar, boolean backIcon) {
        if (getActivity() != null)
            ((JvtdActivity) getActivity()).setToolbar(toolbar, backIcon);
    }

    /**
     * 初始 Toolbar ，含有菜单
     *
     * @param toolbar   Toolbar
     * @param menuResId MenuResId
     */
    protected void setToolbarAndMenu(Toolbar toolbar, int menuResId) {
        if (menuResId != 0) {
            Menu menu = toolbar.getMenu();
            if (menu != null)
                menu.clear();
            toolbar.inflateMenu(menuResId);
        }
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(mContext, R.drawable.icon_back));
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null)
                ((JvtdActivity) getActivity()).onBackPressedSupport();
        });
    }

    /**
     * 根 View 获取焦点，重写 onTouch()
     *
     * @param views 根View
     */
    protected void setViewTouch(View... views) {
        for (View view : views)
            view.setOnTouchListener(this);
    }

    /**
     * Toolbar 菜单点击事件
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.requestFocus();
        return false;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        if (mCompositeDisposable != null) mCompositeDisposable.dispose();
        super.onDestroyView();
    }

    protected abstract View getLayout(LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initViewAndData();

    /**
     * 事件处理
     *
     * @param eventCenter 事件实体类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fragmentOnEvent(EventCenter eventCenter) {

    }

    protected void addDisposable(Disposable d) {
        if (mCompositeDisposable == null) mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(d);
    }

    @AfterPermissionGranted(Permission.TYPE_LOCATION)
    protected void checkLocationPermission()
    {
        PermissionUtils.openPermissions(this, Permission.TYPE_LOCATION, permissionType -> onGranted());
    }

    @AfterPermissionGranted(Permission.TYPE_STORAGE)
    protected void checkStoragePermission()
    {
        PermissionUtils.openPermissions(this, Permission.TYPE_STORAGE, permissionType -> onGranted());
    }

    @AfterPermissionGranted(Permission.TYPE_CALL_PHONE)
    protected void checkPhonePermission()
    {
        PermissionUtils.openPermissions(this, Permission.TYPE_CALL_PHONE, permissionType -> onGranted());
    }

    @AfterPermissionGranted(Permission.TYPE_RECORD_AUDIO)
    protected void checkRecordAudioPremission(){
        PermissionUtils.openPermissions(this, Permission.TYPE_RECORD_AUDIO, permissionType -> onGranted());
    }

    @AfterPermissionGranted(Permission.TYPE_CAMERA)
    protected void checkCameraPermission()
    {
        PermissionUtils.openPermissions(this, Permission.TYPE_CAMERA, permissionType -> onGranted());
    }

    @AfterPermissionGranted(Permission.TYPE_CONTACTS)
    protected void checkContactsPermission()
    {
        PermissionUtils.openPermissions(this, Permission.TYPE_CONTACTS, permissionType -> onGranted());
    }

    /**
     * 权限通过
     */
    protected void onGranted()
    {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> list)
    {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms)
    {
        // 被拒绝且禁止再次询问
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            int rationale = PermissionUtils.getRationaleId(requestCode);

            new AppSettingsDialog.Builder(this)
                    .setTitle(R.string.jvtd_permission_title)
                    .setRationale(rationale)
                    .setPositiveButton(R.string.jvtd_permission_to_grant_authorization)
                    .build()
                    .show();
        } else if (perms.size() != 0)
            if (requestCode == Permission.TYPE_LOCATION)
                checkLocationPermission();
            else if (requestCode == Permission.TYPE_STORAGE)
                checkStoragePermission();
            else if (requestCode == Permission.TYPE_CALL_PHONE)
                checkPhonePermission();
            else if (requestCode == Permission.TYPE_CAMERA)
                checkCameraPermission();
            else if (requestCode == Permission.TYPE_RECORD_AUDIO)
                checkRecordAudioPremission();
            else if (requestCode == Permission.TYPE_CONTACTS)
                checkContactsPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
