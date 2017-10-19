package com.huxq17.example.floatball.floatball;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.huxq17.example.floatball.utils.Util;


public class FloatBallUtil {
    public static WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !Util.isOnePlus()) {
            //一加5用TYPE_TOAST时，在下拉状态栏后，过个几秒悬浮球会消失，所以加个是不是一加手机的判断。
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return mLayoutParams;
    }
}
