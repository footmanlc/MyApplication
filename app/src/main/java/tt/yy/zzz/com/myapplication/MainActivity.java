package tt.yy.zzz.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
                test();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("ricardo", Utils.isSystemApp(this) + "-----");

        startService(new Intent(this, WatchDogService.class));

    }

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
        Monitor.init(this);//.test();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        } else test();
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
        //startActivity("com.ss.android.article.news", "com.ss.android.article.news.activity.MainActivity");
        AdbugView.init(this).show();
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
