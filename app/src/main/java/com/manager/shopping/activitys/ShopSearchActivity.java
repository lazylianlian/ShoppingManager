package com.manager.shopping.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.manager.shopping.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopSearchActivity extends Activity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    private EditText editText;
    LinearLayout hotBtn, countBtn, reviewBtn;
    ListView listView;
    private List<JSONObject> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_search);

        editText = (EditText) findViewById(R.id.edit_text);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);

        hotBtn = (LinearLayout) findViewById(R.id.shop_list_hot);
        countBtn = (LinearLayout) findViewById(R.id.shop_list_count);
        reviewBtn = (LinearLayout) findViewById(R.id.shop_list_review);
        hotBtn.setOnClickListener(this);
        countBtn.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        textChange(s.toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String searchStr = v.getText().toString().trim();
            textChange(searchStr);

            return true;
        }
        return false;
    }

    private void textChange(String searchStr) {
//        if (recyclerView == null)
//            return;
//        list.clear();
//        delegateAdapter.notifyDataSetChanged();
//
//        if (searchStr.length() > 0) {
//            viewLine.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//            getMonthlySearchData(searchStr);
//        } else {
//            viewLine.setVisibility(View.INVISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//            cancelThread(searchThread);
//        }
    }
    public void backClick(View view) {
        finish();
    }
}
