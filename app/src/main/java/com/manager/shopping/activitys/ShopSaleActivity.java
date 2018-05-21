package com.manager.shopping.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.manager.shopping.R;

public class ShopSaleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_sale);
    }

    public void backClick(View view) {
        finish();
    }

}
