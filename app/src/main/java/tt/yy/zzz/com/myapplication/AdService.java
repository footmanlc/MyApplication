package tt.yy.zzz.com.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.android.adbug.real.AdbugView;

/**
 * Created by liucheng on 2017/7/6.
 */

public class AdService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        View view1 = LayoutInflater.from(this).inflate(R.layout.ad, null);
        AdbugView.init(this).show(view1);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
