package com.example.wireLessApk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("UpdateReceiver : " , intent.getAction());
        if (intent.getAction().equals("com.android.robot.update")) {
            Log.d("UpdateReceiver", "开始安装");
            MainActivity.myHandler.sendEmptyMessage(1001);
        }
    }
}
