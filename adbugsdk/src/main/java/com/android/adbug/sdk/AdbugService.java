package com.android.adbug.sdk;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


/**
 * Created by liucheng on 2017/7/5.
 */

public class AdbugService extends IntentService {
    public AdbugService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
