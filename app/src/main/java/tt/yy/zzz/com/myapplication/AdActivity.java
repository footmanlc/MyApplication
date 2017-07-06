package tt.yy.zzz.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.adbug.sdk.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liucheng on 2017/7/6.
 */

public class AdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Utils.isRoot()){
                    statrtActivity("com.ss.android.article.news/.activity.MainActivity");
                }else{
                    startActivity("com.ss.android.article.news", "com.ss.android.article.news.activity.MainActivity");
                }
                finish();
            }
        },7000);
    }
    private void statrtActivity(String s) {
        try {
            String cmd = "am start -n  " + s;
            Process process = Runtime.getRuntime().exec("su");
            Log.e("ricardo", cmd);
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startActivity(String packageName, String activityName) {
        try {
            Context mPluginContext = createPackageContext(packageName,
                    Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            Class<?> clazz = mPluginContext.getClassLoader().loadClass(
                    activityName);
            startActivity(new Intent(mPluginContext, clazz));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
