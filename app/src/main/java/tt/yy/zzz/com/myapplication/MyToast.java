package tt.yy.zzz.com.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yuri on 2017/7/6.
 */

public class MyToast {

    private Toast mToast;
    private Context mContext;

    private Timer mTimer;

    private static MyToast mInstance;

    public static MyToast getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MyToast.class) {
                if (mInstance == null) {
                    mInstance = new MyToast(context);
                }
            }
        }

        return mInstance;
    }

    private MyToast(Context context) {
        mContext = context;
    }

    public MyToast makeToast() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_toast, null);
        mToast = new Toast(mContext);
        mToast.setView(v);
        return this;
    }

    private MyToast(Context context, CharSequence text, int durataion) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        mToast = new Toast(context);
        mToast.setDuration(durataion);
        mToast.setView(v);

    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            setGravity(Gravity.FILL, 0, 0);
            mToast.show();

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mToast.show();
                }
            }, 100);

            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                   mToast.cancel();
                }
            }, 6000);
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

    public void dismiss() {
        mToast.cancel();
    }
}
