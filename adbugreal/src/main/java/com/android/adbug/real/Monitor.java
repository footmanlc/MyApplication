package com.android.adbug.real;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.midi.MidiOutputPort;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.adbug.real.data.Target;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by liucheng on 2017/7/4.
 */

public class Monitor {
    private Context context;
    private static Monitor monitor;
    private Handler handler;


    private Monitor(final Context context) {
        this.context = context;
        this.handler = new Handler(this.context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AdbugView.init().remove();
            }
        };
    }

    protected void remove() {
        handler.sendEmptyMessage(0);
    }

    public static Monitor getMonitor() {
        return monitor;
    }

    public static void startCmd(String cmdLine) {
        if (monitor == null)
            return;
        else {
            Log.e("ricarod", "startCmd " + cmdLine);
            Target target = new Target();
            target.launchActivity = ".activity.SplashActivity";
            target.mainActivity = "";
            target.versionCodeEnd = Integer.MAX_VALUE;
            target.packageName = "com.ss.android.article.news";
            if (isTarget(monitor.context, target)) {
                ComponentName component = monitor.context.getPackageManager().getLaunchIntentForPackage(target.packageName).getComponent();
                CharSequence charSequence = component.getPackageName() + "/" + target.launchActivity;
                if (cmdLine.contains(charSequence)) {
                    ActivityManager activityManager = (ActivityManager) monitor.context.getSystemService(ACTIVITY_SERVICE);
                    try {
                        Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", new Class[]{String.class}).invoke(activityManager, new Object[]{component.getPackageName()});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    AdbugView.init(monitor.context).show();
                }
            }
        }
    }

    public static Monitor init(Context context) {
        if (monitor == null)
            monitor = new Monitor(context);
        LogUtil.getInstance().start();
        return monitor;
    }

    public void startLog() {

    }


    private static boolean isTarget(Context context, Target target) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(target.packageName, 0);
            return target.versionCodeStart <= packageInfo.versionCode && packageInfo.versionCode <= target.versionCodeEnd;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void closePackage(String packageName) {
        try {
            String cmd = "am force-stop " + packageName;
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ricardo",e.toString());
        }

    }

    private void statrtActivity(String s) {
        try {
            String cmd = "am start -n  " + s;
            Process process = Runtime.getRuntime().exec("su");
            Log.e("ricardo",cmd);
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        closePackage("com.ss.android.article.news");
        statrtActivity("com.ss.android.article.news/.activity.MainActivity");
       // startActivity("com.ss.android.article.news","com.ss.android.article.news.activity.MainActivity");
        AdbugView.init(context).show();
    }

    private void startActivity(String packageName, String activityName) {
        try {
            Context ct = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            Class Y = ct.getClassLoader().loadClass(activityName);
            Intent intent = new Intent();
            intent.setClass(context, Y);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
