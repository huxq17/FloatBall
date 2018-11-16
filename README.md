# FloatBall

### 效果图

 <img src="gifs/gif1.gif" width="280" height="475" />|<img src="gifs/gif2.gif" width="280" height="475" />


### Gradle
项目根目录的build.gradle：
```
buildscript {
   allprojects {
       repositories {
           jcenter()
       }
   }
    dependencies {
        classpath 'com.buyi.huxq17:agencyplugin:1.1.2'
    }
}
```
App模块的build.gradle：

```
apply plugin: 'service_agency'
```

### 初始化悬浮球配置

```
 int ballSize = DensityUtil.dip2px(this, 45);
 Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
 FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);
 //设置悬浮球的位置
 //FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon,FloatBallCfg.Gravity.LEFT_CENTER);
 //设置悬浮球不半隐藏
 //ballCfg.setHideHalfLater(false);
```
### 生成悬浮球
FloatBallManager是提供操控悬浮球的类，可以通过它来添加菜单，添加点击事件，显示和隐藏悬浮球
#### 显示菜单
##### 初始化悬浮菜单配置

```
 int menuSize = DensityUtil.dip2px(this, 180);
 int menuItemSize = DensityUtil.dip2px(this, 40);
 FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
```
##### 生成floatballManger

```
mFloatballManager = new FloatBallManager(getApplicationContext(), ballCfg, menuCfg);
```
##### 添加悬浮菜单

```
addFloatMenuItem();
private void addFloatMenuItem() {
    MenuItem personItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weixin", this)) {
        @Override
        public void action() {
            toast("打开微信");
            mFloatballManager.closeMenu();
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
            mFloatballManager.closeMenu();
        }
    };
    mFloatballManager.addMenuItem(personItem)
            .addMenuItem(walletItem)
            .addMenuItem(settingItem)
            .buildMenu();
}
```

#### 不显示菜单
##### 生成floatballManger
```
mFloatballManager = new FloatBallManager(getApplicationContext(), ballCfg);
```
##### 设置悬浮球点击事件

```
// 如果没有添加菜单，可以设置悬浮球点击事件
if (mFloatballManager.getMenuItemSize() == 0) {
    mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
    @Override
        public void onFloatBallClick() {
            toast("点击了悬浮球");
        }
});
}
```
### 设置悬浮球所需权限

悬浮球需要判断有没有权限和申请权限这两个功能，这里用的是别人写好的库，可以自己实现。

```
private void setFloatPermission() {
    //设置悬浮球权限，用于申请悬浮球权限的，这里用的是别人写好的库，可以自己选择
    //如果不设置permission，则不会弹出悬浮球
    mFloatPermissionManager = new FloatPermissionManager();
    mFloatballManager.setPermission(new FloatBallManager.IFloatBallPermission() {
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
```
### 记录悬浮球位置并恢复
如果想记录悬浮球位置并在下次打开的时候在上次的位置显示，可参照以下的步骤：
1. 实现LocationService，这里用的SharedPreferences记录位置,你也可以用其他的方式，在类上添加ServiceAgent注解，floatball在需要的时候会找到这个实现类并进行位置记录和恢复的操作。

```
@ServiceAgent
public class SimpleLocationService implements LocationService {
    private SharedPreferences sharedPreferences;

    @Override
    public void onLocationChanged(int x, int y) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("floatball_x", x);
        editor.putInt("floatball_y", y);
        editor.apply();
    }

    @Override
    public int[] onRestoreLocation() {
        //如果坐标点传的是-1，那么就不会恢复位置。
        int[] location = {sharedPreferences.getInt("floatball_x", -1),
                sharedPreferences.getInt("floatball_y", -1)};
        return location;
    }

    @Override
    public void start(Context context) {
        sharedPreferences = context.getSharedPreferences("floatball_location", Context.MODE_PRIVATE);
    }
}
```

2. 启动LocationService，如果你的实现需要Context的话，可选的。
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在LocationService的实现类里用的是SharedPreferences来记录位置，需要context
        //如果你的实现方式不需要context，则可以不需要这个步骤，可以去掉这行代码
         try {
             ServiceAgency.getService(LocationService.class).start(this);
         }catch (AgencyException e){
            //ignore
         }
    }
}
```
这里用到了ServiceAgency，如果感兴趣可以[点击查看更多](https://github.com/huxq17/ServiceAgency)。



### 实现应用内悬浮球
如果想让悬浮球只在应用内打开，可以参考以下代码实现：

```
private ActivityLifeCycleListener mActivityLifeCycleListener = new ActivityLifeCycleListener();

 //注册Activity生命周期的监听回调
 getApplication().registerActivityLifecycleCallbacks(mActivityLifeCycleListener);

 //注册ActivityLifeCyclelistener以后要记得注销，以防内存泄漏。
 getApplication().unregisterActivityLifecycleCallbacks(mActivityLifeCycleListener);

 public class ActivityLifeCycleListener implements Application.ActivityLifecycleCallbacks {

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
    }

```
也可以在Application里实现此功能

### 悬浮球显示与隐藏

```
  //显示悬浮球
  mFloatballManager.show();
  //隐藏悬浮球
  mFloatballManager.hide();
```
### 更新日志：<br/>
    2018-11-16：
    1.添加悬浮球位置记录和恢复功能。详见 https://github.com/huxq17/FloatBall/issues/16

    2018-5-23：
    1.解决在小米miui9上悬浮球不显示的问题。

    2018-1-18：
    1.添加悬浮球仅在单Activity里显示时不需要权限,详见appnopermission模块。

    2018-1-16：
    1.添加对是否半隐藏悬浮球的设置。

    2018-1-13：
    1.解决快速点击悬浮球不断的打开和关闭菜单，会高几率的点中菜单的问题，详见 https://github.com/huxq17/FloatBall/issues/10。

    2017-12-21：
    1.添加设置悬浮球初始显示位置的方法，详见 https://github.com/huxq17/FloatBall/issues/8。

    2017-12-16：
    1.解决菜单item个数大于3时，菜单item位置会错乱的问题，详见 https://github.com/huxq17/FloatBall/pull/7。

    2017-11-16：
    1.添加按下返回键关闭悬浮菜单的功能。

    2017-11-9：
    1.解决android 7.1.1以后悬浮球显示几秒后自动隐藏的问题。

### 感谢

1. [悬浮球权限适配相关的库](https://github.com/zhaozepeng/FloatWindowPermission)

### LICENSE

[Apache License 2.0](LICENSE)