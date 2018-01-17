package com.huxq17.example.floatball.floatball.floatball;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.huxq17.example.floatball.floatball.FloatBallManager;
import com.huxq17.example.floatball.floatball.FloatBallUtil;

public class StatusBarView extends View {

    private Context mContext;
    private FloatBallManager mFloatBallManager;
    private WindowManager.LayoutParams mLayoutParams;
    private OnLayoutChangeListener layoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            mFloatBallManager.onStatusBarHeightChange();
        }
    };

    public StatusBarView(Context context, FloatBallManager floatBallManager) {
        super(context);
        mContext = context;
        mFloatBallManager = floatBallManager;
        Activity activity = null;
        if (mContext instanceof Activity) {
            activity = (Activity) mContext;
        }
        mLayoutParams = FloatBallUtil.getLayoutParams(activity);
    }

    public void attachToWindow(WindowManager wm) {
        addOnLayoutChangeListener(layoutChangeListener);
        wm.addView(this, mLayoutParams);
    }

    public void detachFromWindow(WindowManager windowManager) {
        removeOnLayoutChangeListener(layoutChangeListener);
        if (getContext() instanceof Activity) {
            windowManager.removeViewImmediate(this);
        } else {
            windowManager.removeView(this);
        }
    }

    public int getStatusBarHeight() {
        int[] windowParams = new int[2];
        int[] screenParams = new int[2];
        getLocationInWindow(windowParams);
        getLocationOnScreen(screenParams);
        return screenParams[1] - windowParams[1];
    }
}