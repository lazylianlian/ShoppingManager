package com.manager.shopping.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.manager.shopping.R;

public class BaseActivity extends AppCompatActivity {
    //获取手机屏幕分辨率的类
    private static DisplayMetrics dm;
    private static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        dm = new DisplayMetrics();
        activity = BaseActivity.this;
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
    public static Activity currentActivity(){
        return activity;
    }

}
