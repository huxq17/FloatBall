package com.huxq17.example.floatball.interfaces;

import android.widget.RelativeLayout;

/**
 * Created by huxq17 on 2016/6/30.
 */
public interface IMenu {
    /**
     * 添加悬浮球左侧菜单的内容
     *
     * @param parent
     * @param childLayoutParams
     */
    void addLeftMenu(RelativeLayout parent, RelativeLayout.LayoutParams childLayoutParams);

    /**
     * 添加悬浮球右侧菜单的内容
     *
     * @param parent
     * @param childLayoutParams
     */
    void addRightMenu(RelativeLayout parent, RelativeLayout.LayoutParams childLayoutParams);

    /**
     * 悬浮球右侧的菜单是否可用
     *
     * @return
     */
    boolean isRightMenuEnable();

    /**
     * 悬浮球左侧的菜单是否可用
     *
     * @return
     */
    boolean isLeftMenuEnable();
}
