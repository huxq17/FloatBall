package com.huxq17.example.floatball;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by 2144 on 2016/4/28.
 */
public class Utils {
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
