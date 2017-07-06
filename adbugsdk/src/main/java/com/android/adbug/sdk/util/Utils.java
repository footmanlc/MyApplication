package com.android.adbug.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

public class Utils {
    /*
    * 判断给定的context是不是系统应用
    * */
    public static boolean isSystemApp(Context context) {
        Log.e("rrr", context.getCacheDir().getAbsolutePath() + "+++++++++++" + context.getApplicationInfo().uid);
        ApplicationInfo info = context.getApplicationInfo();
        return ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) !=
                0 || (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /*获取所有安装的程序*/
    public static List<PackageInfo> getAllInstallApk(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }
    public static boolean isRoot(){
        try
        {
            Process process  = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            int i = process.waitFor();
            if(0 == i){
                process = Runtime.getRuntime().exec("su");
                return true;
            }
        } catch (Exception e)
        {
            return false;
        }

        return false;
    }
}
