package com.huxq17.example.floatball;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huxq17.example.floatball.interfaces.IMenu;

/**
 * Created by huxq17 on 2016/7/1.
 */
public class FloatBallMenu implements IMenu {
    private TextView tvLeftCenter, tvRightCenter, tvLeftGift, tvRightGift;

    @Override
    public void addLeftMenu(RelativeLayout parent, RelativeLayout.LayoutParams childLayoutParams) {
        childLayoutParams.setMargins(0, 0, DensityUtil.dip2px(parent.getContext(), 20), 0);
        childLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvLeftGift = new TextView(parent.getContext());
        tvLeftGift.setId(getId());
        tvLeftGift.setText("福利");
        tvLeftGift.setTextSize(14);
        tvLeftGift.setGravity(Gravity.CENTER);
        tvLeftGift.setTextColor(Color.parseColor("#333333"));
        parent.addView(tvLeftGift, childLayoutParams);
        childLayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(parent.getContext(), 1), DensityUtil.dip2px(parent.getContext(), 30));
        childLayoutParams.setMargins(0, DensityUtil.dip2px(parent.getContext(), 8), 0, DensityUtil.dip2px(parent.getContext(), 8));
        childLayoutParams.addRule(RelativeLayout.LEFT_OF, tvLeftGift.getId());
        View verticalLine = new View(parent.getContext());
        verticalLine.setId(getId());
        verticalLine.setBackgroundColor(Color.parseColor("#e5e5e5"));
        parent.addView(verticalLine, childLayoutParams);

        childLayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(parent.getContext(), 52), DensityUtil.dip2px(parent.getContext(), 30));
        childLayoutParams.addRule(RelativeLayout.LEFT_OF, verticalLine.getId());
        tvRightCenter = new TextView(parent.getContext());
        tvRightCenter.setId(getId());
        tvRightCenter.setText("我的");
        tvRightCenter.setTextSize(14);
        tvRightCenter.setGravity(Gravity.CENTER);
        tvRightCenter.setTextColor(Color.parseColor("#333333"));
        parent.addView(tvRightCenter, childLayoutParams);
    }

    @Override
    public void addRightMenu(RelativeLayout parent, RelativeLayout.LayoutParams childLayoutParams) {
        childLayoutParams.setMargins(DensityUtil.dip2px(parent.getContext(), 20), 0, 0, 0);
        tvLeftCenter = new TextView(parent.getContext());
        tvLeftCenter.setId(getId());
        tvLeftCenter.setText("我的");
        tvLeftCenter.setTextSize(14);
        tvLeftCenter.setGravity(Gravity.CENTER);
        tvLeftCenter.setTextColor(Color.parseColor("#333333"));
        parent.addView(tvLeftCenter, childLayoutParams);

        childLayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(parent.getContext(), 1), DensityUtil.dip2px(parent.getContext(), 30));
        childLayoutParams.setMargins(0, DensityUtil.dip2px(parent.getContext(), 8), 0, DensityUtil.dip2px(parent.getContext(), 8));
        childLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvLeftCenter.getId());
        View verticalLine = new View(parent.getContext());
        verticalLine.setId(getId());
        verticalLine.setBackgroundColor(Color.parseColor("#e5e5e5"));
        parent.addView(verticalLine, childLayoutParams);

        childLayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(parent.getContext(), 52), DensityUtil.dip2px(parent.getContext(), 30));
        childLayoutParams.addRule(RelativeLayout.RIGHT_OF, verticalLine.getId());
        tvRightGift = new TextView(parent.getContext());
        tvRightGift.setId(getId());
        tvRightGift.setText("福利");
        tvRightGift.setTextSize(14);
        tvRightGift.setGravity(Gravity.CENTER);
        tvRightGift.setTextColor(Color.parseColor("#333333"));
        parent.addView(tvRightGift, childLayoutParams);
    }

    @Override
    public boolean isRightMenuEnable() {
        return true;
    }

    @Override
    public boolean isLeftMenuEnable() {
        return true;
    }

    private int getId() {
        return IDFactory.getId();
    }
}
