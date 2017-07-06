package tt.yy.zzz.com.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yuri on 2017/7/6.
 */

public class MyToast {

    private Toast mToast;

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
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
