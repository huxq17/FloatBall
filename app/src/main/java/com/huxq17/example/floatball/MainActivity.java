package com.huxq17.example.floatball;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    private FloatBall mFloatBall;

    public void changeOrientation(View v) {
        if (Utils.isScreenOriatationPortrait(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDesktopLayout();
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                show();
            }
        });
        mFloatBall.setLayoutGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    }

    private void createDesktopLayout() {
        FloatBallMenu menu = new FloatBallMenu();
        mFloatBall = new FloatBall(this, menu);
    }

    private void show() {
        mFloatBall.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismiss();
    }

    private void dismiss() {
        mFloatBall.dismiss();
    }

}
