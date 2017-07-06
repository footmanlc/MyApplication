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
}
