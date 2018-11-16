package com.huxq17.example.floatball;

import android.app.Application;

import com.buyi.huxq17.serviceagency.ServiceAgency;
import com.huxq17.floatball.libarary.LocationService;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //需要记录并在再次打开app的时候恢复位置
        //在LocationService的实现类里用的是SharedPreferences来记录位置，需要Context
        //如果你的实现方式不需要Context，则可以不需要这个步骤，可以去掉这行代码
        ServiceAgency.getService(LocationService.class).start(this);
    }
}
