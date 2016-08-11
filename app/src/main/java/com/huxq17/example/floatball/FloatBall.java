package com.huxq17.example.floatball;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.huxq17.example.floatball.interfaces.IMenu;

public class FloatBall extends ViewGroup {
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
    private boolean layoutfromTouch = false;
    private boolean isMenuShowing = false;
    private int menuWidth;
    private int menuHeight;
    private ExpanableLayout menu;
    private int floatBallWidth, floatBallHeight;
    private int SCROLL_DURATION = 300;
    private SingleIcon singleIcon;
    private DoubleIcon doubleIcon;

    private IMenu menuOperator;

    //    public void setIsHiddenWhenExit(boolean isHiddenWhenExit) {
//        this.isHiddenWhenExit = isHiddenWhenExit;
//    }
    public static class Builder {
        private Context context;
        private int width, height;
        private IMenu iMenu;
        private SingleIcon singleIcon;
        private DoubleIcon doubleIcon;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置悬浮球的菜单
         *
         * @param menu
         * @return
         */
        public Builder menu(IMenu menu) {
            this.iMenu = menu;
            return this;
        }

        /**
         * 设置悬浮球的宽度
         *
         * @param width
         * @return
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置悬浮球的高度
         *
         * @param height
         * @return
         */
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * 设置悬浮球显示的图片和在点击以后和正常两种状态下图片显示的透明度
         *
         * @param singleIcon
         * @return
         */
        public Builder icon(SingleIcon singleIcon) {
            this.singleIcon = singleIcon;
            this.doubleIcon = null;
            return this;
        }

        /**
         * 设置悬浮球在点击以后和正常两种状态下显示的两张图片
         *
         * @param doubleIcon
         * @return
         */
        public Builder doubleIcon(DoubleIcon doubleIcon) {
            this.doubleIcon = doubleIcon;
            this.singleIcon = null;
            return this;
        }

        public FloatBall build() {
            FloatBall floatBall = new FloatBall(context, iMenu, singleIcon, doubleIcon, width, height);
            return floatBall;
        }
    }

    public FloatBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, null, null, 0, 0);
    }

    private FloatBall(Context context, IMenu menu, SingleIcon singleIcon, DoubleIcon doubleIcon, int floatBallWidth, int floatBallHeight) {
        super(context);
        init(context, menu, singleIcon, doubleIcon, floatBallWidth, floatBallHeight);
    }

    private void init(Context context, IMenu menu, SingleIcon singleIcon, DoubleIcon doubleIcon, int fbWidth, int fbHeight) {
        if (menu != null) {
            menu.onAttach(this, context.getApplicationContext());
        }
        this.singleIcon = singleIcon;
        this.doubleIcon = doubleIcon;
        menuOperator = menu;
        floatBallWidth = DensityUtil.dip2px(getContext(), 40);
        floatBallHeight = DensityUtil.dip2px(getContext(), 40);
        if (fbWidth != 0) {
            floatBallWidth = fbWidth;
        }
        if (fbHeight != 0) {
            floatBallHeight = fbHeight;
        }
        mScroller = new Scroller(getContext());
        mClipScroller = new Scroller(getContext(), new LinearInterpolator());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        addMenu(context);
        ivFloatBall = new ImageView(context);
        ivFloatBall.setId(getId());
        setFloatImage(true);
        ivFloatBall.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(floatBallWidth, floatBallHeight);
        ivFloatBall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(!isMenuShowing);
            }
        });
        this.addView(ivFloatBall, layoutParams);
        leftMenuWidth = menuWidth - floatBallWidth / 2;
    }

    private void addMenu(Context context) {
        if (menuOperator != null) {
            menuWidth = menuOperator.getMenuWidth();
            menuHeight = menuOperator.getMenuHeight();
        } else {
            menuWidth = DensityUtil.dip2px(getContext(), 135);
            menuHeight = DensityUtil.dip2px(getContext(), 30);
        }
        menu = new ExpanableLayout(context);
        menu.setId(getId());
        addMenuContent(menu);
        LayoutParams layoutParams = new LayoutParams(menuWidth, menuHeight);
        if (menuOperator != null) {
            addView(menu, layoutParams);
//            menu.setVisibility(GONE);
        }
        //如果没有背景，则设置透明的背景，不然不会出现动画展开和缩放的动画
        if (menu.getBackground() == null) {
            menu.setBackgroundColor(Color.TRANSPARENT);
        }
        setMenuOffset(0);
    }

    public int getId() {
        return IDFactory.getId();
    }

    private void setFloatImage(boolean enable) {
        if (singleIcon != null) {
            ivFloatBall.setImageResource(singleIcon.bitmap);
            ivFloatBall.setAlpha(enable ? singleIcon.enable : singleIcon.normal);
        } else if (doubleIcon != null) {
            ivFloatBall.setImageResource(enable ? doubleIcon.enable : doubleIcon.normal);
        }
    }

    private void addMenuContent(RelativeLayout parent) {
        if (menuOperator != null) {
            menuOperator.addMenu(parent);
        }
    }

    public void hideFloatBall() {
        isMenuShowing = false;
//        stopClipRunner();
        menu.setOffset(0);
        setVisibility(GONE);
        removeCallbacks(mclipRunnable);
        mClipScroller.setFinalX(0);
    }

    public void hideMenu() {
        showMenu(false);
    }

    public void hideMenuImmediately() {
        isMenuShowing = false;
        setMenuOffset(0);
    }

    public void showMenu() {
        showMenu(true);
    }

    private static final int Left = 0;
    private static final int Right = 1;

    private void showMenuSide(int side) {
        if (menu == null) {
            return;
        }
        switch (side) {
            case Left:
                menu.setOritation(ExpanableLayout.LEFT);
                if (menuOperator != null) {
                    menuOperator.showingLeftMenu();
                }
                break;
            case Right:
                menu.setOritation(ExpanableLayout.RIGHT);
                if (menuOperator != null) {
                    menuOperator.showingRightMenu();
                }
                break;
        }
    }

    private void showMenu(boolean show) {
        stopClipRunner();
        if (show) {
            if (isOnLeft()) {
                if (menuOperator != null && menuOperator.isRightMenuEnable()) {
                    isMenuShowing = true;
                    showMenuSide(Right);
                } else {
                    isMenuShowing = false;
                }
            } else {
                if (menuOperator != null && menuOperator.isLeftMenuEnable()) {
                    isMenuShowing = true;
                    showMenuSide(Left);
                } else {
                    isMenuShowing = false;
                }
            }
            if (isMenuShowing) {
                menu.setVisibility(VISIBLE);
                mClipScroller.startScroll(0, 0, menuWidth, 0, SCROLL_DURATION);
                post(mclipRunnable);
            }
        } else {
            if (isMenuShowing) {
                isMenuShowing = false;
                mClipScroller.startScroll(menuWidth, 0, -menuWidth, 0, SCROLL_DURATION);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(menuWidth * 2, ivFloatBall.getMeasuredHeight());
    }

    private boolean forceLayout = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!forceLayout && (layoutfromTouch || mScroller.computeScrollOffset() || mClipScroller.computeScrollOffset())) {
            return;
        }
        initScreenParams();
        int[] finalLocation = correctLocation();
        if (!forceLayout)
            doMove(finalLocation[0], finalLocation[1]);
        int[] floatLocation = new int[2];
        getLocationOnScreen(floatLocation);
        int left = floatLocation[0];
        int menuTop = (getMeasuredHeight() - menu.getMeasuredHeight()) / 2;
        int menuLeft = 0;
        if (isOnLeft()) {
            menuLeft = menu.getMeasuredWidth();
        } else {
            menuLeft = 0;
        }
        menu.layout(menuLeft, menuTop, menuLeft + menu.getMeasuredWidth(), menuTop + menu.getMeasuredHeight());
        int floatLeft = l + leftMenuWidth;
        if (!forceLayout)
            ivFloatBall.layout(floatLeft, t, floatLeft + ivFloatBall.getMeasuredWidth(), t + ivFloatBall.getMeasuredHeight());
        forceLayout = false;
    }


    private void Log(String msg) {
        Log.i("Tag", msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                layoutfromTouch = true;
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                if (isTouchFloatBall(event)) {
                    removeCallbacks(mFadeOutRunnable);
                    removeCallbacks(mScrollRunnable);
                    mScroller.forceFinished(true);
                    setFloatImage(true);
                }
                if (isTouchFloatBall(event)) {
                    hasTouchFloatBall = true;
                }
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
                        if (isMenuShowing && menu != null) {
                            isMenuShowing = false;
                            setMenuOffset(0);
                        }
                    } else {
                        return super.dispatchTouchEvent(event);
                    }
                }
                if (hasTouchFloatBall || isTouchFloatBall(event)) {
                    hasTouchFloatBall = true;
                }
                if (hasTouchFloatBall && !isMenuShowing || isMenuShowing) {
                    move(deltaX, deltaY);
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                hasTouchFloatBall = false;
                layoutfromTouch = false;
                isIntercepted = false;
                onFingerReleased();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void setMenuOffset(int offset) {
        menu.setOffset(offset);
        menu.setVisibility(offset == 0 ? GONE : VISIBLE);
        removeCallbacks(mclipRunnable);
        mClipScroller.setFinalX(0);
    }

    private boolean hasTouchFloatBall = false;

    /**
     * 是否摸到了某个view
     *
     * @param ev
     * @return
     */
    private boolean isTouchFloatBall(MotionEvent ev) {
        if (ivFloatBall != null && ivFloatBall.getVisibility() == VISIBLE) {
            Rect bounds = new Rect();
            ivFloatBall.getGlobalVisibleRect(bounds);
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (bounds.contains(x, y)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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

    private boolean isOnLeft() {
        int[] floatLocation = new int[2];
        ivFloatBall.getLocationOnScreen(floatLocation);
        if (floatLocation[0] + ivFloatBall.getWidth() / 2 > mScreenWidth / 2) {
            return false;
        } else {
            return true;
        }
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
        mTop = Utils.getStatusHeight(getContext());
        mScreenWidth = Utils.getScreenWidth(getContext());
        mScreenHeight = Utils.getScreenHeight(getContext());
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
        forceLayout = true;
        requestLayout();

        int[] finalLocation = correctLocation();
        startScroll(finalLocation[0], finalLocation[1], SCROLL_DURATION);
    }


    private void doMove(int x, int y) {
        if (mWindowManager == null || mLayoutParams == null) {
            return;
        }
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        mWindowManager.updateViewLayout(this, mLayoutParams);
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
            final int currentX = mClipScroller.getCurrX();
            if (menu != null && menu.getVisibility() == VISIBLE) {
                if (mClipScroller.computeScrollOffset()) {
                    menu.setOffset(currentX);
                    if (currentX == mClipScroller.getFinalX() && currentX == 0) {
                        menu.setVisibility(GONE);
                    }
                    post(this);
                } else {
                    menu.setOffset(currentX);
                    if (currentX == mClipScroller.getFinalX() && currentX == 0) {
                        menu.setVisibility(GONE);
                    }
                    removeCallbacks(this);
                }
            }
        }
    }

    private void fadeOutFloatBall() {
        removeCallbacks(mFadeOutRunnable);
        postDelayed(mFadeOutRunnable, 2000);
    }

    private FadeOutRunnable mFadeOutRunnable = new FadeOutRunnable();

    private class FadeOutRunnable implements Runnable {

        @Override
        public void run() {
            if (!isMenuShowing && !hasTouchFloatBall) {
                setFloatImage(false);
            }
        }
    }

    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                final int currentX = mScroller.getCurrX();
                final int currentY = mScroller.getCurrY();
                doMove(currentX, currentY);
                post(this);
            } else {
                removeCallbacks(this);
                fadeOutFloatBall();
            }
        }
    };

    public void dismiss() {
        if (isAdded && mWindowManager != null) {
            mWindowManager.removeView(this);
        }
        removeCallbacks(mFadeOutRunnable);
        stopClipRunner();
        stopScrollRunner();
        isAdded = false;
    }

    private void stopScrollRunner() {
        mScroller.abortAnimation();
    }

    private void stopClipRunner() {
        mClipScroller.abortAnimation();
    }

    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        Context context = isHiddenWhenExit ? getContext() : getContext().getApplicationContext();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
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

    public static class DoubleIcon {
        public int normal, enable;

        /**
         * @param enable 点击悬浮球以后显示的图片
         * @param normal 普通状态下悬浮球上显示的图片
         */
        public DoubleIcon(int enable, int normal) {
            this.normal = normal;
            this.enable = enable;
        }
    }

    public static class SingleIcon {
        public float normal, enable;
        public int bitmap;

        /**
         * @param icon
         * @param enable 图标点击以后的透明度,范围是0~1,正常是1
         * @param normal 图标普通状态的下的透明度,范围是0~1
         */
        public SingleIcon(int icon, float enable, float normal) {
            this.bitmap = icon;
            this.normal = normal;
            this.enable = enable;
        }
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
}
