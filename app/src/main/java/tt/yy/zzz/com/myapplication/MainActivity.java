package tt.yy.zzz.com.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.adbug.real.AdbugView;
import com.android.adbug.real.Monitor;
import com.android.adbug.sdk.util.Utils;

import java.io.DataOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Log.i("", "onActivityResult granted");
                Monitor.init(this).test();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("ricardo", Utils.isSystemApp(this) + "-----" + Build.VERSION.SDK_INT);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.ad, null);
                AdbugView.init(MainActivity.this).show(view1);*/
                /*Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, AdActivity.class);
                startActivity(intent1);*/
                //handler.sendEmptyMessageDelayed(0, 500);
                startService(new Intent(MainActivity.this, AdService.class));
                handler.sendEmptyMessage(0);
            }
        });

        startService(new Intent(this, WatchDogService.class));

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("ricardo", "-------------------" + Utils.isRoot());
            if (msg.what == 0) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.ss.android.article.news");
                startActivity(intent);
                // closePackage("com.ss.android.article.news");
                ActivityManager activityMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                try {
                    activityMgr.killBackgroundProcesses("com.ss.android.article.news");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Utils.isRoot()) {
                    statrtActivity("com.ss.android.article.news/.activity.MainActivity");
                } else {
                    startActivity("com.ss.android.article.news", "com.ss.android.article.news.activity.MainActivity");
                }
               // handler.sendEmptyMessageDelayed(1, 6000);
                finish();
            } else
         /* Intent intent = new Intent();
            intent.setClass(MainActivity.this, AdActivity.class);
            startActivity(intent);*/
                finish();
            //  closePackage("com.ss.android.article.news");
        }
    };

    private void start() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.ss.android.article.news");
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        }
        Monitor.init(this);
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
            Log.e("ricardo", e.toString());
        }

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
            Intent intent = new Intent(mPluginContext, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
