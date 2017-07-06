package tt.yy.zzz.com.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuri on 2017/7/6.
 */

public class WatchDogService extends Service {

    private static final String TAG = "WatchDogService";
    private static boolean flag = true;// 线程退出的标记
    private ActivityManager am;

    @Override
    public void onCreate() {
        super.onCreate();

        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

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
                        SystemClock.sleep(500);
                    }
                    Log.i(TAG, "服务在循环");
                }
            }
        }.start();

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
