package tt.yy.zzz.com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.adbug.real.Monitor;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liucheng on 2017/7/5.
 */

public class AdTestView {
    private TranslateAnimation C = null;
    private TranslateAnimation D = null;
    private RelativeLayout relativeLayout;
    private int c = 0;
    private int d = 0;
    private boolean flag = false;
    private Context context = null;
    private int count = -1;
    private DisplayMetrics displayMetrics = null;
    private WindowManager wm = null;
    private ImageView imageView = null;
    private TextView textView = null;
    private int o;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private Bitmap bitmap = null;
    private AlphaAnimation v = null;
    private BitmapFactory.Options options = null;
    private RelativeLayout relativeLayout1 = null;
    private ImageView imageView1 = null;
    private static AdTestView adbugView;
    private View btn_floatView;

    private View mAdLayoutView;
    private TextView mTimeView;
    private int duration = 5;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            remove();

            if (duration > 0) {
//                mTimeView.setText(duration + "");
            }
            duration--;
            if (duration == 0) {
                mHandler.removeMessages(0);
            }
        }
    };

    protected void remove() {
        wm.removeView(btn_floatView);
//        wm.removeView(mAdLayoutView);
    }

    private AdTestView(Context context) {
        this.context = context;
    }

    public static AdTestView init() {
        return adbugView;
    }

    public static AdTestView init(Context context) {
        if (adbugView == null) {
            adbugView = new AdTestView(context);
        }
        return adbugView;
    }

    private void initView(Context context) {
        mAdLayoutView = LayoutInflater.from(context).inflate(R.layout.ad_test, null);
        mTimeView = mAdLayoutView.findViewById(R.id.textview);
    }

    public void show(View v) {
        btn_floatView = v;
        wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 设置window type
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {*/
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //}
        params.format = PixelFormat.RGBA_8888;

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.TYPE_STATUS_BAR;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长得宽
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        wm.addView(v, params);
//        if (mAdLayoutView != null) {
//            wm.addView(mAdLayoutView, params);
//        }
//        mHandler.sendEmptyMessageDelayed(0, 5000);
    }

    public void show() {
//        btn_floatView = new Button(context);
//        ((Button) btn_floatView).setText("悬浮窗");
//        btn_floatView.setBackgroundColor(Color.argb(0xff, 0xff, 0, 0));

        wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 设置window type
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {*/
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //}
        params.format = PixelFormat.RGBA_8888;

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长得宽
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        wm.addView(btn_floatView, params);

        mAdLayoutView = LayoutInflater.from(context).inflate(R.layout.ad_test, null);
        mTimeView = mAdLayoutView.findViewById(R.id.textview);
        if (mAdLayoutView != null) {
            wm.addView(mAdLayoutView, params);
        }
        mHandler.sendEmptyMessageDelayed(0, 5000);
    }
}
