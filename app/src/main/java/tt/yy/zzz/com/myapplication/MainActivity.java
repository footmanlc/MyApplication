package tt.yy.zzz.com.myapplication;

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
        Log.e("ricardo", Utils.isSystemApp(this) + "-----");

        startService(new Intent(this, WatchDogService.class));

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("ricardo","-------------------");
            closePackage("com.ss.android.article.news");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AdActivity.class);
            startActivity(intent);
            finish();
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
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.ss.android.article.news");
        startActivity(intent);
        /*if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        } else  Monitor.init(this).test();*/

        handler.sendEmptyMessageDelayed(0, 2000);
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


}
