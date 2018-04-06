package com.manager.shopping.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.manager.shopping.R;

public class ShopListActivity extends Activity implements View.OnClickListener {
    LinearLayout hotBtn, countBtn, reviewBtn;
    ListView listView;
    TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        hotBtn = (LinearLayout) findViewById(R.id.shop_list_hot);
        countBtn = (LinearLayout) findViewById(R.id.shop_list_count);
        reviewBtn = (LinearLayout) findViewById(R.id.shop_list_review);
        hotBtn.setOnClickListener(this);
        countBtn.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int cid = intent.getIntExtra("cid", 1);
        titleTv.setText(title + "");
        getSortData(cid);
    }

    private void getSortData(int cid) {

    }

    public void backClick(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_list_hot:
                break;
            case R.id.shop_list_count:
                break;
            case R.id.shop_list_review:
                break;
        }
    }
}
