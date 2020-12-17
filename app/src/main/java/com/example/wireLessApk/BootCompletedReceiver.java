package com.example.wireLessApk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("BootCompletedReceiver", "开机启动");
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        }

    }
}
