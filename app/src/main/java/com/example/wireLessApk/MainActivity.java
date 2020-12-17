package com.example.wireLessApk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Context context;
    public static MyHandler myHandler ;

    public class MyHandler extends Handler {//防止内存泄漏
        //持有弱引用MainActivity,GC回收时会被回收掉.
        private final WeakReference<MainActivity> mAct;

        private MyHandler(MainActivity mainActivity) {
            mAct = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("Wireless ", "handleMessage code : " + msg.what);
            if (msg.what == 1001) {
                String path = Environment.getExternalStorageDirectory().getPath()
                        + "/com.example.robot" +"/update.apk";
                boolean install = ApkController.install(path, context);
                Log.d("Wireless" , "安装结果" + install);
                if (install) {
                    Log.d("Wireless" , "启动server");
                    Intent intent = new Intent("com.android.robot.server.start");
                    intent.setPackage("com.example.robot");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.sendBroadcast(intent);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        myHandler = new MyHandler(this);
        Utilities.exec("setprop service.adb.tcp.port 5555");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utilities.exec("stop adbd");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utilities.exec("start adbd");
    }
}