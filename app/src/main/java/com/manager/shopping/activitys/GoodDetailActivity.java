package com.manager.shopping.activitys;

import android.app.Activity;
import android.os.Bundle;

import com.manager.shopping.R;
import com.manager.shopping.bean.GoodInfo;

public class GoodDetailActivity extends Activity {
    private GoodInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        info = (GoodInfo) getIntent().getSerializableExtra("goodInfo");

    }
}
