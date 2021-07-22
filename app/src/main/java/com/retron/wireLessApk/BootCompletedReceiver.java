package com.retron.wireLessApk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("BootCompletedReceiver", "BootCompletedReceiver");
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        } else if (intent.getAction().equals("com.android.robot.update")) {
            Log.d("UpdateReceiver", "start install");
            MainActivity.myHandler.sendEmptyMessage(1001);
        } else if (intent.getAction().equals("com.android.mqtt.update")) {
            Log.d("UpdateReceiver", "start install");
            MainActivity.myHandler.sendEmptyMessage(1005);
        }

    }
}
