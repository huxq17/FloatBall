package com.huxq17.floatball.libarary.floatball;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.FloatBallUtil;

public class StatusBarView extends View {

    private Context mContext;
    private FloatBallManager mFloatBallManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean isAdded;
    private OnLayoutChangeListener layoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            mFloatBallManager.onStatusBarHeightChange();
        }
    };

    public StatusBarView(Context context, FloatBallManager floatBallManager) {
        super(context);
//        setBackgroundColor(Color.BLACK);
        mContext = context;
        mFloatBallManager = floatBallManager;
        mLayoutParams = FloatBallUtil.getStatusBarLayoutParams(context);
    }

    public void attachToWindow(WindowManager wm) {
        if (!isAdded) {
            addOnLayoutChangeListener(layoutChangeListener);
            wm.addView(this, mLayoutParams);
            isAdded = true;
        }
    }

    public void detachFromWindow(WindowManager windowManager) {
        if (!isAdded) return;
        isAdded = false;
        removeOnLayoutChangeListener(layoutChangeListener);
        if (getContext() instanceof Activity) {
            windowManager.removeViewImmediate(this);
        } else {
            windowManager.removeView(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getStatusBarHeight() {
        int[] windowParams = new int[2];
        int[] screenParams = new int[2];
        getLocationInWindow(windowParams);
        getLocationOnScreen(screenParams);
        return screenParams[1] - windowParams[1];
    }
}