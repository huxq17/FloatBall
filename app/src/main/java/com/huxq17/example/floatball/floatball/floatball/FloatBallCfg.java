package com.huxq17.example.floatball.floatball.floatball;

import android.graphics.drawable.Drawable;

public class FloatBallCfg {
    public Drawable mIcon;
    public int mSize;
    //第一次是否显示在左边
    public boolean mLeft;
    //第一次显示的y坐标偏移量，左上角是原点。
    public int mOffsetY = -1;

    /**
     * 第一次是否显示在左边
     *
     * @param isLeft true显示在左边，false则在右边
     */
    public void setLeft(boolean isLeft) {
        mLeft = isLeft;
    }

    /**
     * 第一次显示的y坐标偏移量，屏幕左上角是原点。
     *
     * @param offsetY
     */
    public void setOffsetY(int offsetY) {
        mOffsetY = offsetY;
    }

    public FloatBallCfg(int size, Drawable icon) {
        this(size, icon, true, -1);
    }

    public FloatBallCfg(int size, Drawable icon, boolean isLeft, int offsetY) {
        mSize = size;
        mIcon = icon;
        mLeft = isLeft;
        mOffsetY = offsetY;
    }
}
