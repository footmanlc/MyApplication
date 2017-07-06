package com.android.adbug.real.thread;

import android.util.Log;

import com.android.adbug.real.LogUtil;
import com.android.adbug.real.Monitor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liucheng on 2017/7/4.
 */

public class LogThread extends Thread {
    private InputStream inputStream = null;
    private String tag = null;
    public boolean flag = true;

    public LogThread(InputStream inputStream, String str) {
        super();
        this.inputStream = inputStream;
        tag = str;
        Log.e("ricardo",tag);
    }

    @Override
    public void run() {
        super.run();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (flag) {
            try {
                String s = reader.readLine();
                if (tag != null) {
                    if ("InputStream".equals(tag)) {
                        Log.e("ricardo", "log is " + s);
                        if (s.contains("android.intent.action.MAIN")) {//启动,检查activity
                            Monitor.startCmd(s);
                        }
                    }
                    if ("ErrorStream".equals(tag)) {
                    }
                    if ("PSErrorStream".equals(tag)) {
                    }
                    if ("PSInputStream".equals(tag)) {
                    }// ps命令log，检测自己还在不在吧
                } else
                    flag = false;
               // Thread.sleep(1000);
            } catch (IOException e) {
                Log.e("ricardo",tag+"-----------------------"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
