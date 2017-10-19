package com.huxq17.example.floatball;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huxq17.example.floatball.floatball.FloatBallManager;
import com.huxq17.example.floatball.floatball.floatball.FloatBallCfg;
import com.huxq17.example.floatball.floatball.menu.FloatMenuCfg;
import com.huxq17.example.floatball.floatball.menu.MenuItem;
import com.huxq17.example.floatball.permission.FloatPermissionManager;
import com.huxq17.example.floatball.utils.BackGroudSeletor;
import com.huxq17.example.floatball.utils.DensityUtil;

public class MainActivity extends Activity {
    private FloatBallManager mFloatBallManager;
    private FloatPermissionManager mFloatPermissionManager;
    private int resumed;

    public void showFloatBall(View v) {
        mFloatBallManager.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //如果没有添加菜单，可以设置悬浮球点击事件
        if (mFloatBallManager.getMenuItemSize() == 0) {
            mFloatBallManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    toast("点击了悬浮球");
                }
            });
        }
        //如果想做成应用内悬浮球，可以添加以下代码。
        addActivityLifeCycleListener(getApplication());
    }

    private void init() {
        boolean showMenu = true;//换成false试试
        if (showMenu) {
            initWithMenu();
        } else {
            initWithoutMenu();
        }
    }

    private void initWithMenu() {
        int menuSize = DensityUtil.dip2px(this, 180);
        int menuItemSize = DensityUtil.dip2px(this, 40);
        FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);

        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);

        mFloatBallManager = new FloatBallManager(getApplicationContext(), ballCfg, menuCfg);
        mFloatPermissionManager = new FloatPermissionManager();
        //如果不设置permission，则不会弹出悬浮球
        setFloatPermission();
        addFloatMenuItem();
    }

    private void initWithoutMenu() {
        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);

        mFloatBallManager = new FloatBallManager(getApplicationContext(), ballCfg);
        mFloatPermissionManager = new FloatPermissionManager();
        //如果不设置permission，则不会弹出悬浮球
        setFloatPermission();
    }

    private void setFloatPermission() {
        mFloatBallManager.setPermission(new FloatBallManager.IFloatBallPermission() {
            @Override
            public boolean onRequestFloatBallPermission() {
                requestFloatBallPermission(MainActivity.this);
                return true;
            }

            @Override
            public boolean hasFloatBallPermission(Context context) {
                return mFloatPermissionManager.checkPermission(context);
            }

            @Override
            public void requestFloatBallPermission(Activity activity) {
                mFloatPermissionManager.applyPermission(activity);
            }

        });
    }

    private void addActivityLifeCycleListener(Application app) {

        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ++resumed;
                setFloatballVisible(true);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                --resumed;
                if (!isApplicationInForeground()) {
                    setFloatballVisible(false);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void addFloatMenuItem() {
        MenuItem personItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weixin", this)) {
            @Override
            public void action() {
                toast("打开微信");
                mFloatBallManager.closeMenu();
            }
        };
        MenuItem walletItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weibo", this)) {
            @Override
            public void action() {
                toast("打开微博");
            }
        };
        MenuItem settingItem = new MenuItem(BackGroudSeletor.getdrawble("ic_email", this)) {
            @Override
            public void action() {
                toast("打开邮箱");
                mFloatBallManager.closeMenu();
            }
        };
        mFloatBallManager.addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)
                .buildMenu();
    }

    private void setFloatballVisible(boolean visible) {
        if (visible) {
            mFloatBallManager.show();
        } else {
            mFloatBallManager.remove();
        }
    }

    public boolean isApplicationInForeground() {
        return resumed > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
