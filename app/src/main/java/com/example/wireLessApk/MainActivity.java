package com.example.wireLessApk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    public static MyHandler myHandler ;
    private ActivityManager am;
    private UpdateReceiver updateReceiver;

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
                Log.d("zdzd ", "handle message1 ：" + install);
            } else if (msg.what == 1002) {
                Log.d("zdzd ", "handle message2");
                getRunningProgressCount(context);
            } else if (msg.what == 1003) {
                Log.d("zdzd ", "handle message3");
                Intent intent = new Intent("com.android.robot.server.start");
                intent.setPackage("com.example.robot");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.sendBroadcast(intent);
            }
        }
    };

    /*
     * 正在运行的进程数量
     * */
    public void getRunningProgressCount(Context context){
        PackageManager localPackageManager = getPackageManager();
        List localList = localPackageManager.getInstalledPackages(0);
        for (int i = 0; i < localList.size(); i++) {
            PackageInfo localPackageInfo1 = (PackageInfo) localList.get(i);
            String str1 = localPackageInfo1.packageName.split(":")[0];
            if (((ApplicationInfo.FLAG_SYSTEM & localPackageInfo1.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & localPackageInfo1.applicationInfo.flags) == 0)
                    && ((ApplicationInfo.FLAG_STOPPED & localPackageInfo1.applicationInfo.flags) == 0)) {
                Log.v("进程信息222",str1);
                if ("com.example.robot".equals(str1)) {
                    myHandler.sendEmptyMessageDelayed(1002, 5000);
                    return;
                }
            }
            if (i == localList.size() - 1) {
                Log.v("进程自启动 ", "" + localList.size());
                Intent intent = new Intent("com.android.robot.server.start");
                intent.setPackage("com.example.robot");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent. FLAG_INCLUDE_STOPPED_PACKAGES);
                context.sendBroadcast(intent);
            }
        }
        myHandler.sendEmptyMessageDelayed(1002, 5000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        myHandler = new MyHandler(this);
        am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        myHandler.sendEmptyMessageDelayed(1002, 5000);
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


        IntentFilter filter_app = new IntentFilter();
        filter_app.addDataScheme("package");
        filter_app.addAction(Intent.ACTION_PACKAGE_ADDED);//应用安装的广播
        filter_app.addAction(Intent.ACTION_PACKAGE_REPLACED);//应用替换的广播
        filter_app.addAction(Intent.ACTION_PACKAGE_REMOVED);//应用卸载的广播
        updateReceiver = new UpdateReceiver();
        registerReceiver(updateReceiver, filter_app);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateReceiver);
    }
}