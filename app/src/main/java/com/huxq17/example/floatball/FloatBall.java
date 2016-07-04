package com.huxq17.example.floatball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.huxq17.example.floatball.interfaces.IMenu;

public class FloatBall extends RelativeLayout {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private float mLastX;
    private float mLastY;
    private boolean isHiddenWhenExit = false;
    private int mLayoutGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
    private Scroller mScroller, mClipScroller;
    private int mScreenWidth, mScreenHeight;
    private int mRightEdge;
    private boolean isAdded;
    private int mTop;
    private int mLeft;
    private Rect windowRect = new Rect();
    private ImageView ivFloatBall;
    private int leftMenuWidth;
    private int mTouchSlop;
    private boolean isIntercepted = false;
    private ExpanableLayout showingmenuView;
    private boolean layoutfromTouch = false;
    private boolean isMenuShowing = false;

    private IMenu menuOperator;

//    public void setIsHiddenWhenExit(boolean isHiddenWhenExit) {
//        this.isHiddenWhenExit = isHiddenWhenExit;
//    }

    public FloatBall(Context context) {
        super(context);
        init(context, null);
    }

    public FloatBall(Context context, IMenu menu) {
        super(context);
        init(context, menu);
    }

    private int menuWidth;
    private int menuHeight;
    private ExpanableLayout menu;
    private int floatBallWidth, floatBallHeight;

    private void init(Context context, IMenu menu) {

        floatBallWidth = DensityUtil.dip2px(getContext(), 40);
        floatBallHeight = DensityUtil.dip2px(getContext(), 40);
        mScroller = new Scroller(getContext());
        mClipScroller = new Scroller(getContext(), new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        menuOperator = menu;
        addMenu(context);
        ivFloatBall = new ImageView(context);
        ivFloatBall.setId(getId());
        setFloatImage(R.drawable.floatball2, 1);
        ivFloatBall.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(floatBallWidth, floatBallHeight);
        layoutParams.addRule(CENTER_IN_PARENT);
//        layoutParams.addRule(RIGHT_OF, leftMenu.getId());
//        layoutParams.addRule(LEFT_OF, rightMenu.getId());
//        layoutParams.setMargins(-floatBallHeight / 2, 0, -floatBallHeight / 2, 0);
        ivFloatBall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(!isMenuShowing);
            }
        });
        this.addView(ivFloatBall, layoutParams);
        leftMenuWidth = menuWidth - floatBallHeight / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(menuWidth * 2, ivFloatBall.getMeasuredHeight());
    }

    private int menuBackground = Color.parseColor("#fafafa");

    private void addMenu(Context context) {
        menuWidth = DensityUtil.dip2px(getContext(), 135);
        menuHeight = DensityUtil.dip2px(getContext(), 30);
        menu = new ExpanableLayout(context);
        menu.setId(getId());
        menu.setBackgroundColor(menuBackground);
        addLeftMenu(menu);
        LayoutParams layoutParams = new LayoutParams(menuWidth, menuHeight);
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(ALIGN_PARENT_LEFT);
        if (menuOperator != null && menuOperator.isLeftMenuEnable()) {
            addView(menu, layoutParams);
        }

//        rightMenu = new ExpanableLayout(context);
//        rightMenu.setId(getId());
//        rightMenu.setOritation(ExpanableLayout.RIGHT);
//        leftMenu = new ExpanableLayout(context);
//        leftMenu.setId(getId());
//        leftMenu.setOritation(ExpanableLayout.LEFT);

//        leftMenu.setBackgroundColor(Color.parseColor("#fafafa"));
//        addLeftMenu(leftMenu);
//        rightMenu.setBackgroundColor(Color.parseColor("#fafafa"));
//        addRightMenu(rightMenu);
//
//        LayoutParams layoutParams = new LayoutParams(DensityUtil.dip2px(getContext(), 135), DensityUtil.dip2px(getContext(), 30));
//        layoutParams.addRule(CENTER_VERTICAL);
//        if (menuOperator != null && menuOperator.isLeftMenuEnable()) {
//            addView(leftMenu, 0, layoutParams);
//        }
//
//        layoutParams = new LayoutParams(DensityUtil.dip2px(getContext(), 135), DensityUtil.dip2px(getContext(), 30));
//        layoutParams.addRule(RIGHT_OF, leftMenu.getId());
//        layoutParams.addRule(CENTER_VERTICAL);
//        if (menuOperator != null && menuOperator.isRightMenuEnable()) {
//            addView(rightMenu, layoutParams);
//        }
    }

    public int getId() {
        return IDFactory.getId();
    }

    private void setFloatImage(int imageId, float alpha) {
        ivFloatBall.setImageResource(imageId);
        ivFloatBall.setAlpha(alpha);
    }

    private void addRightMenu(RelativeLayout parent) {
        LayoutParams layoutParams = new LayoutParams(DensityUtil.dip2px(getContext(), 52), DensityUtil.dip2px(getContext(), 30));
        if (menuOperator != null) {
            menuOperator.addRightMenu(parent, layoutParams);
        }
    }

    private void addLeftMenu(RelativeLayout parent) {
        LayoutParams layoutParams = new LayoutParams(DensityUtil.dip2px(getContext(), 52), DensityUtil.dip2px(getContext(), 30));
        if (menuOperator != null) {
            menuOperator.addLeftMenu(parent, layoutParams);
        }
    }

    public void setOnCenterClickListener(final OnClickListener listener) {
        if (listener == null) {
            return;
        }
//        tvLeftCenter.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickButton();
//                listener.onClick(v);
//            }
//        });
//        tvRightCenter.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickButton();
//                listener.onClick(v);
//            }
//        });
    }

    public void setOnGiftClickListener(final OnClickListener listener) {
        if (listener == null) {
            return;
        }
//        tvLeftGift.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickButton();
//                listener.onClick(v);
//            }
//        });
//        tvRightGift.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickButton();
//                listener.onClick(v);
//            }
//        });
    }

    public void clear() {
//        tvLeftGift.setOnClickListener(null);
//        tvLeftCenter.setOnClickListener(null);
//        tvRightCenter.setOnClickListener(null);
//        tvRightGift.setOnClickListener(null);
    }

    public void clickButton() {
        showMenu(false);
        setVisibility(GONE);
    }

    private static final int Left = 0;
    private static final int Right = 1;

    private void showMenuSide(int side) {
        showingmenuView = menu;
        if (menu == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) menu.getLayoutParams();
        switch (side) {
            case Left:
                menu.setOritation(ExpanableLayout.LEFT);
                layoutParams.addRule(ALIGN_PARENT_LEFT);
                break;
            case Right:
                menu.setOritation(ExpanableLayout.RIGHT);
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
                break;
        }
        menu.setLayoutParams(layoutParams);
    }

    private void showMenu(boolean show) {
        int width = DensityUtil.dip2px(getContext(), 135);
        int[] floatLocation = new int[2];
        ivFloatBall.getLocationOnScreen(floatLocation);
        int floatLeft = floatLocation[0];
        if (show) {
            setFloatImage(R.drawable.floatball2, 1);
            if (floatLeft + ivFloatBall.getHeight() / 2 <= mScreenWidth / 2) {
                if (menuOperator != null && menuOperator.isRightMenuEnable()) {
                    isMenuShowing = true;
//                    showingmenuView = rightMenu;
                    showMenuSide(Right);
                } else {
                    isMenuShowing = false;
                }
            } else {
                if (menuOperator != null && menuOperator.isLeftMenuEnable()) {
                    isMenuShowing = true;
//                    showingmenuView = leftMenu;
                    showMenuSide(Left);
                } else {
                    isMenuShowing = false;
                }
            }
            if (isMenuShowing) {
                mClipScroller.startScroll(0, 0, width, 0, 300);
                post(mclipRunnable);
            }
        } else {
            if (isMenuShowing) {
                isMenuShowing = false;
                mClipScroller.startScroll(width, 0, -width, 0, 300);
                post(mclipRunnable);
            }
        }
    }

    public void show() {
        if (mWindowManager == null) {
            createWindowManager();
        }
        if (mWindowManager == null) {
            return;
        }
        ViewParent parent = this.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this);
        }
        setVisibility(VISIBLE);
        if (!isAdded) {
            mWindowManager.addView(this, mLayoutParams);
            fadeOutFloatBall();
            isAdded = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (layoutfromTouch || mScroller.computeScrollOffset() || mClipScroller.computeScrollOffset()) {
            return;
        }
        initScreenParams();
        int[] finalLocation = correctLocation();
        doMove(finalLocation[0], finalLocation[1]);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        Log.i("tag", "dispatchTouchEvent width=" + getWidth()+";height="+getHeight());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                removeCallbacks(mFadeOutRunnable);
                layoutfromTouch = true;
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                float y = event.getRawY();
                int deltaX = (int) (x - mLastX);
                int deltaY = (int) (y - mLastY);
                if (!isIntercepted) {
                    if (Math.abs(deltaX) > mTouchSlop || Math.abs(deltaY) > mTouchSlop) {
                        sendCancelEvent(event);
                        isIntercepted = true;
                        if (isMenuShowing && showingmenuView != null) {
                            isMenuShowing = false;
                            showingmenuView.setOffset(0);
                        }
                    } else {
                        return super.dispatchTouchEvent(event);
                    }
                }
                move(deltaX, deltaY);
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                layoutfromTouch = false;
                isIntercepted = false;
                onFingerReleased();
                ivFloatBall.setClickable(true);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    private void sendCancelEvent(MotionEvent lastEvent) {
        MotionEvent last = lastEvent;
        MotionEvent e = MotionEvent.obtain(
                last.getDownTime(),
                last.getEventTime()
                        + ViewConfiguration.getLongPressTimeout(),
                MotionEvent.ACTION_CANCEL, last.getX(), last.getY(),
                last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private int[] correctLocation() {
        int[] correctLocation = new int[2];
        if (mLayoutParams == null || mWindowManager == null) {
            return correctLocation;
        }
        int finalX = mLayoutParams.x;
        int finalY = mLayoutParams.y;
        finalX = finalX + leftMenuWidth;
        mRightEdge = mScreenWidth - ivFloatBall.getWidth();
        if (finalX + ivFloatBall.getWidth() / 2 > mScreenWidth / 2) {
            finalX = mRightEdge - leftMenuWidth;
        } else {
            finalX = 0 - leftMenuWidth;
        }
        int[] floatLocation = new int[2];
        ivFloatBall.getLocationOnScreen(floatLocation);
        int offsetY = 0;
        if (floatLocation[1] < mTop) {
            offsetY = mTop - floatLocation[1];
        } else if (floatLocation[1] + ivFloatBall.getHeight() > mScreenHeight) {
            offsetY = -(floatLocation[1] + ivFloatBall.getHeight() - mScreenHeight);
        }
        int gravity = mLayoutParams.gravity;
        if ((Gravity.BOTTOM & gravity) == Gravity.BOTTOM) {
            offsetY = -offsetY;
        }
        finalY = finalY + offsetY;
        correctLocation[0] = finalX;
        correctLocation[1] = finalY;
        return correctLocation;
    }

    private void initScreenParams() {
        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(windowRect);

            mLeft = windowRect.left;
            if (mTop == 0) {
                //不知道为什么横竖屏切换以后，这里获取到的值总是0，
                // 考虑到statebarHeight是固定的，在第一次获取到以后就不再获取
                mTop = windowRect.top;
            }
            mScreenWidth = windowRect.right;
            mScreenHeight = windowRect.bottom;
            int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        }
    }

    public void startScroll(int finalLeft, int finalTop, int duration) {
        final int startLeft = mLayoutParams.x;
        final int startTop = mLayoutParams.y;
        final int dx = finalLeft - startLeft;
        final int dy = finalTop - startTop;
        mScroller.startScroll(startLeft, startTop, dx, dy, duration);
        post(mScrollRunnable);
    }

    private void onFingerReleased() {
        int[] finalLocation = correctLocation();
        startScroll(finalLocation[0], finalLocation[1], 300);
    }

    private void doMove(int x, int y) {
        if (mWindowManager == null || mLayoutParams == null) {
            return;
        }
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    private void swapHeightAndWidth() {
        mScreenWidth = mScreenHeight + mScreenWidth;
        mScreenHeight = mScreenWidth - mScreenHeight;
        mScreenWidth = mScreenWidth - mScreenHeight;
    }

    private void move(int deltaX, int deltaY) {
        int gravity = mLayoutParams.gravity;
        if ((Gravity.RIGHT & gravity) == Gravity.RIGHT) {
            deltaX = -deltaX;
        } else if ((Gravity.LEFT & gravity) == Gravity.LEFT) {
        }
        if ((Gravity.BOTTOM & gravity) == Gravity.BOTTOM) {
            deltaY = -deltaY;
        } else if ((Gravity.TOP & gravity) == Gravity.TOP) {
        }
        mLayoutParams.x += deltaX;
        mLayoutParams.y += deltaY;
        doMove(mLayoutParams.x, mLayoutParams.y);
    }

    private ClipRunnable mclipRunnable = new ClipRunnable();

    private class ClipRunnable implements Runnable {
        @Override
        public void run() {
            if (mClipScroller.computeScrollOffset() && showingmenuView != null) {
                ivFloatBall.setClickable(false);
                final int currentX = mClipScroller.getCurrX();
                showingmenuView.setOffset(currentX);
                if (currentX == mClipScroller.getFinalX()) {
                    ivFloatBall.setClickable(true);
                }
                post(this);
            }
        }

    }

    private void fadeOutFloatBall() {
        removeCallbacks(mFadeOutRunnable);
        postDelayed(mFadeOutRunnable, 5000);
    }

    private FadeOutRunnable mFadeOutRunnable = new FadeOutRunnable();

    private class FadeOutRunnable implements Runnable {

        @Override
        public void run() {
            if (!isMenuShowing) {
                setFloatImage(R.drawable.floatball2, 0.3f);
            }
        }
    }

    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                final int currentX = mScroller.getCurrX();
                final int currentY = mScroller.getCurrY();
                if (currentX != mScroller.getFinalX()
                        || currentY != mScroller.getFinalY()) {
                    doMove(currentX, currentY);
                    post(this);
                } else {
                    removeCallbacks(this);
                    mScroller.forceFinished(true);
                    fadeOutFloatBall();
                }
            } else {
                removeCallbacks(this);
            }
        }
    };

    public void dismiss() {
        if (isAdded && mWindowManager != null) {
            mWindowManager.removeView(this);
        }
        removeCallbacks(mclipRunnable);
        removeCallbacks(mFadeOutRunnable);
        removeCallbacks(mScrollRunnable);
        isAdded = false;
    }

    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        Context context = isHiddenWhenExit ? getContext() : getContext().getApplicationContext();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = mLayoutGravity;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 设置悬浮球的位置
     */
    public void setLayoutGravity(int layoutgravity) {
        mLayoutGravity = layoutgravity;
    }

    /**
     * 判断悬浮球是否在屏幕以内
     *
     * @return
     */
    private boolean hasBallInside() {
        Rect bounds = new Rect();
        ivFloatBall.getGlobalVisibleRect(bounds);
        int[] location = new int[2];
        ivFloatBall.getLocationOnScreen(location);
        bounds.set(location[0], location[1], location[0] + bounds.right, location[1] + bounds.bottom);
        if (windowRect.contains(bounds)) {
            return true;
        }
        return false;
    }

    /**
     * 将悬浮球的中心点移动到触摸点
     *
     * @param event
     */
    private void moveToCenter(MotionEvent event) {
        int motionX = (int) event.getX();
        int motionY = (int) event.getY();
        int startX = mLayoutParams.x;
        int startY = mLayoutParams.y;
        int width = ivFloatBall.getWidth();
        int height = ivFloatBall.getHeight();

        int offsetX = motionX - width / 2;
        int offsetY = motionY - height / 2;
        int gravity = mLayoutParams.gravity;
        if ((Gravity.RIGHT & gravity) == Gravity.RIGHT) {
            offsetX = -offsetX;
        } else if ((Gravity.LEFT & gravity) == Gravity.LEFT) {
        }
        if ((Gravity.BOTTOM & gravity) == Gravity.BOTTOM) {
            offsetY = -offsetY;
        } else if ((Gravity.TOP & gravity) == Gravity.TOP) {
        }
        int finalX = startX + offsetX;
        int finalY = startY + offsetY;
        doMove(finalX, finalY);
    }
}
