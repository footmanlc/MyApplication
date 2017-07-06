package tt.yy.zzz.com.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.adbug.real.AdbugView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuri on 2017/7/6.
 */

public class WatchDogService extends Service {

    private static final String TAG = "WatchDogService";
    private static boolean flag = true;// 线程退出的标记
    private ActivityManager am;

    private MyHandler myHandler;

    private boolean hasStart = false;


    @Override
    public void onCreate() {
        super.onCreate();

        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        myHandler = new MyHandler();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while (flag) {
                    synchronized (WatchDogService.class) {
                        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = am.getRunningTasks(1);
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfoList.get(0);
                        ComponentName topActivity = runningTaskInfo.topActivity;
                        String packageName = topActivity.getPackageName();
                        Log.d(TAG, "package:" + packageName + "," + topActivity.getClassName());
//                         package:com.ss.android.article.news,com.ss.android.article.news.activity.SplashActivity
                        if (packageName.equals("com.ss.android.article.news") && topActivity.getClassName().equals("com.ss.android.article.news.activity.SplashActivity")) {
                            Log.d(TAG, "111111111111111");
                            if (!hasStart) {
                                hasStart = true;
                                Log.d(TAG, "222222222222222");
                                myHandler.sendEmptyMessage(0);
                                myHandler.sendEmptyMessageDelayed(1, 500);
                                myHandler.sendEmptyMessageDelayed(2, 6000);
                                myHandler.sendEmptyMessageDelayed(3, 7000);
                            }
                        }
                        SystemClock.sleep(500);
                    }
                    Log.i(TAG, "服务在循环");
                }
            }
        }.start();

    }

    private AdTestView mAdTestView;
    private void addAdView() {
        Log.d(TAG, "addAdView(): ");
//        Toast.makeText(getApplicationContext(), "今日头条启动了", Toast.LENGTH_LONG).show();
//        MyToast.makeText(getApplicationContext(), "", 5000).show();
        View mAdLayoutView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ad_test, null);
        mAdTestView = AdTestView.init(getApplicationContext());
        mAdTestView.show(mAdLayoutView);
        Log.d(TAG, "addAdView(): Over");
    }

    private void closeTouTiao() {
        Log.d(TAG, "closeTouTiao() : ");
        try {
            String cmd = "am force-stop com.ss.android.article.news";
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "closeTouTiao:" + e.getMessage());
        }
    }

    private void startTouTiao() {
        Log.d(TAG, "startTouTiao()");
        try {
            String cmd = "am start -n  com.ss.android.article.news/.activity.MainActivity";
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeAdView() {
        AdTestView.init().remove();
        hasStart = false;
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "msg.what=" + msg.what);
            switch (msg.what) {
                case 0:
                    addAdView();
                    break;
                case 1:
                    closeTouTiao();
                    break;
                case 2:
                    startTouTiao();
                    break;
                case 3:
                    removeAdView();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
