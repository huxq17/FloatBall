package com.huxq17.example.floatball.interfaces;

import android.widget.RelativeLayout;

/**
 * Created by huxq17 on 2016/6/30.
 */
public interface IMenu {
    /**
     * 添加左侧的菜单
     *
     * @param parent
     * @param layoutParams
     */
    void addLeftMenu(RelativeLayout parent, RelativeLayout.LayoutParams layoutParams);

    /**
     * 添加右侧的菜单
     *
     * @param parent
     * @param layoutParams
     */
    void addRightMenu(RelativeLayout parent, RelativeLayout.LayoutParams layoutParams);
}
