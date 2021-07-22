package com.retron.wireLessApk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("UpdateReceiver : " , intent.getAction());
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("UpdateReceiver : " , "install apk : "+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("UpdateReceiver : " , "uninstall apk : "+packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("UpdateReceiver : " , "replace apk : "+packageName);
            if (!"com.retron.robotmqtt".equals(packageName)) {
                MainActivity.myHandler.sendEmptyMessage(1003);
            } else {
                MainActivity.myHandler.sendEmptyMessage(1006);
            }
        }
    }
}
