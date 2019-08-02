package com.jvtd.running_sdk.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gyf.barlibrary.ImmersionBar;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.eventBus.EventCenter;
import com.jvtd.running_sdk.receiver.JvtdNetBroadcastReceiver;
import com.jvtd.running_sdk.utils.JvtdKeyboardUtils;
import com.jvtd.running_sdk.utils.JvtdNetworkUtils;
import com.jvtd.running_sdk.utils.permission.Permission;
import com.jvtd.running_sdk.utils.permission.PermissionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/10/16.
 * 无 MVP 的 Activity 基类
 */

public abstract class JvtdActivity extends SupportActivity implements Toolbar.OnMenuItemClickListener, EasyPermissions.PermissionCallbacks, JvtdNetBroadcastReceiver.NetEvevt
{
  /**
   * 网络类型
   */
  private int netMobile;
  private JvtdNetBroadcastReceiver mNetBroadcastReceiver;

  private ImmersionBar mImmersionBar;
  private CompositeDisposable mCompositeDisposable;
  protected Context mContext;

  /**
   * 创建方法  必须实现
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
//  public static Intent getIntent(Context context){
//    return new Intent(context,JvtdActivity.class);
//  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mContext = this;
    getLayout();
    mImmersionBar = ImmersionBar.with(this);
    bindView();
    EventBus.getDefault().register(this);
    onViewCreated(savedInstanceState);
    initViewAndData();
    updateBar();
  }

  /**
   * 绑定界面  用于第三方绑定方法
   *
   * @author Chenlei
   * created at 2018/9/25
   **/
  protected abstract void bindView();

  private void updateBar()
  {
    mImmersionBar.navigationBarColor(R.color.jvtd_navigation_bar_color)
            .flymeOSStatusBarFontColor(R.color.jvtd_status_bar_color);
    if (ImmersionBar.isSupportStatusBarDarkFont())
      mImmersionBar.statusBarColor(setStatusBarColor())
              .statusBarDarkFont(setStatusBarTextBlack());
    else
    {
      if (isImmersionBar())
        mImmersionBar.statusBarColor(setStatusBarColor());
      else
        mImmersionBar.statusBarColor(R.color.jvtd_status_bar_color);
      mImmersionBar.statusBarDarkFont(false);
    }
    mImmersionBar.init();
  }

  public int setStatusBarColor()
  {
    if (ImmersionBar.isSupportStatusBarDarkFont())
      return R.color.jvtd_status_bar_dark_font_color;
    return R.color.jvtd_status_bar_color;
  }

  public boolean isImmersionBar()
  {
    return false;
  }

  public boolean setStatusBarTextBlack()
  {
    return true;
  }

  protected ImmersionBar getBar()
  {
    return mImmersionBar;
  }

  /**
   * 是否启用网络监听
   * @return 默认 否
   */
  protected boolean isNetworkEnable(){
    return false;
  }

  /**
   * 设置 Toolbar
   *
   * @param toolbar         Toolbar
   * @param homeAdUpEnabled true - 显示返回箭头
   */
  protected void setToolbar(Toolbar toolbar, boolean homeAdUpEnabled)
  {
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    if (getSupportActionBar() == null) return;
    getSupportActionBar().setDisplayHomeAsUpEnabled(homeAdUpEnabled);
    getSupportActionBar().setDisplayShowHomeEnabled(homeAdUpEnabled);
    toolbar.setNavigationOnClickListener(view ->
    {
      JvtdKeyboardUtils.hideSoftInput(this);
      onBackPressedSupport();
    });
  }

  /**
   * 初始 Toolbar ，含有菜单
   *
   * @param toolbar    Toolbar
   * @param menuResId  MenuResId
   */
  protected void setToolbarAndMenu(Toolbar toolbar, int menuResId)
  {
    toolbar.inflateMenu(menuResId);
    toolbar.setOnMenuItemClickListener(this);
//    toolbar.setNavigationIcon(ContextCompat.getDrawable(mContext, R.drawable.icon_back_black));
    toolbar.setNavigationOnClickListener(v -> {
      JvtdKeyboardUtils.hideSoftInput(this);
      onBackPressedSupport();
    });
  }

  @Override
  public FragmentAnimator onCreateFragmentAnimator()
  {
    // 默认竖向(和安卓5.0以上的动画相同)
    // return super.onCreateFragmentAnimator();
    // 设置横向(和安卓4.x动画相同)
    return new DefaultHorizontalAnimator();
    // 设置自定义动画
    // return new FragmentAnimator(enter,exit,popEnter,popExit);
  }

  protected void onViewCreated(Bundle savedInstanceState)
  {
  }

  protected abstract void getLayout();

  protected abstract void initViewAndData();

  @Override
  public boolean onMenuItemClick(MenuItem item)
  {
    return true;
  }

  /**
   * 事件处理
   *
   * @param eventCenter 事件实体类
   */
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void activityOnEvent(EventCenter eventCenter)
  {
  }

  protected void addDisposable(Disposable d)
  {
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

  @Override
  protected void onResume()
  {
    super.onResume();
    if (isNetworkEnable())
      registerNetReceiver();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    if (isNetworkEnable())
      unregisterNetReceiver();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
      onGranted();
  }

  private void registerNetReceiver()
  {
    if (mNetBroadcastReceiver == null)
      mNetBroadcastReceiver = new JvtdNetBroadcastReceiver();
    mNetBroadcastReceiver.setNetWorkChangerListener(this);
    IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(mNetBroadcastReceiver, intentFilter);
  }

  private void unregisterNetReceiver()
  {
    if (mNetBroadcastReceiver != null)
      unregisterReceiver(mNetBroadcastReceiver);
  }

  /**
   * 网络变化之后的类型
   */
  @Override
  public void onNetChange(int netMobile)
  {
    this.netMobile = netMobile;
  }

  /**
   * 判断有无网络
   *
   * @return true 有网, false 没有网络.
   */
  protected boolean isNetConnect()
  {
    return netMobile == JvtdNetworkUtils.NETWORK_MOBILE || netMobile == JvtdNetworkUtils.NETWORK_WIFI;
  }

  @Override
  protected void onDestroy()
  {
    EventBus.getDefault().unregister(this);
    if (mImmersionBar != null) mImmersionBar.destroy();
    if (mCompositeDisposable != null) mCompositeDisposable.dispose();
    super.onDestroy();
  }
}
