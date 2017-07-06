package com.android.adbug.real;

import android.util.Log;

import com.android.adbug.real.thread.LogThread;

import java.io.InputStream;

/**
 * Created by liucheng on 2017/7/5.
 */

public class LogUtil {

    private static LogUtil mInstance = new LogUtil();
    private Process process, psProcess;
    private boolean isRunning;
    private LogThread psError, psInput, inputLog, errorLog;

    private LogUtil() {

    }

    public static LogUtil getInstance() {
        return mInstance;
    }

    public void start() {
        if (isRunning)
            return;
        isRunning = true;
        startLog();
    }

    private void startLog() {
        Log.e("ricardo ","start log");
        try {
            /*psProcess = Runtime.getRuntime().exec("ps");
            psError = new LogThread(psProcess.getErrorStream(), "PSErrorStream");
            psError.start();
            psInput = new LogThread(psProcess.getInputStream(), "PSInputStream");
            psInput.start();
            Runtime.getRuntime().exec("logcat -c -b events");
            Thread.sleep(1000);*/
            Log.e("ricardo","logcat -b events -s am_create_activity");
            process = Runtime.getRuntime().exec("logcat -b events ");
            errorLog = new LogThread(process.getErrorStream(), "ErrorStream");
            errorLog.start();
            inputLog = new LogThread(process.getInputStream(), "InputStream");
            inputLog.start();
            Log.e("ricarod",process.getInputStream().available()+"");
            // new Thread(new p(this)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.process.destroy();
        psProcess.destroy();
        errorLog.flag = false;
        inputLog.flag = false;
        psInput.flag = false;
        psError.flag = false;
        isRunning = false;

    }
}
