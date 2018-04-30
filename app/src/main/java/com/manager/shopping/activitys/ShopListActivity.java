package com.manager.shopping.activitys;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.manager.shopping.R;
import com.manager.shopping.bean.CateCollectionInfo;
import com.manager.shopping.bean.GoodInfo;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.constants.ConstantUtils;
import com.manager.shopping.service.GoodInfoService;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.GsonUtils;
import com.manager.shopping.utils.JsonUtil;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ShopListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    LinearLayout hotBtn, countBtn, reviewBtn;
    ListView listView;
    TextView titleTv;
    List<GoodInfo> list = new ArrayList<>();
    CommonAdapter<GoodInfo> adapter;
    RequestQueue queue;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "844b411fb7129f92886dad13103fde9f");

        setContentView(R.layout.activity_shop_list);

        queue = Volley.newRequestQueue(this);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        hotBtn = (LinearLayout) findViewById(R.id.shop_list_hot);
        countBtn = (LinearLayout) findViewById(R.id.shop_list_count);
        reviewBtn = (LinearLayout) findViewById(R.id.shop_list_review);
        hotBtn.setOnClickListener(this);
        countBtn.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.listView);
        initAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        cid = intent.getIntExtra("cid", 1);
        titleTv.setText(title + "");
        getSortData(cid, 0);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<GoodInfo>(this, list, R.layout.activity_shop_list_item) {

            @Override
            public void convert(ViewHolder helper, final GoodInfo item) {
                helper.setText(R.id.item_name, item.getGoodname());
                helper.setText(R.id.item_intro, item.getDescripe());
                if ("1".equals(item.getSource())) {
                    helper.setText(R.id.item_from, "天猫商城");
                } else if ("2".equals(item.getSource())) {
                    helper.setText(R.id.item_from, "京东商城");
                } else {
                    helper.setText(R.id.item_from, "苏宁易购");
                }
                helper.setText(R.id.item_price, "￥" + item.getGoodprice());
                helper.setText(R.id.item_hot, item.getViews() + "");
                helper.setText(R.id.item_count, item.getSells() + "");
                helper.setText(R.id.item_review, item.getComments() + "");
                TextView addBtn = helper.getView(R.id.item_add);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //加入购物车
                        AddBarketData(item);
                    }
                });
                helper.getView(R.id.item_detele).setVisibility(View.GONE);
                helper.setImageByUrl(R.id.item_img, item.getGoodImage());

            }
        };
    }

    //加入购物车，不可重复加入
    private void AddBarketData(final GoodInfo item) {

        BmobQuery<GoodInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("userInfo", UserInfo.getCurrentUser());
        query.addWhereEqualTo("id", item.getGoodid());
        query.findObjects(new FindListener<GoodInfo>() {
            @Override
            public void done(List<GoodInfo> list, BmobException e) {
                if (list == null || list.size() == 0) {
                    addInfo(item);
                } else {
                    Toast.makeText(ShopListActivity.this, "购物车已有该宝贝", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    /**
     * 加入购物车
     */
    private void addInfo(GoodInfo item) {

        item.setUserInfo(UserInfo.getCurrentUser());
        item.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(ShopListActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSortData(final int cid, final int order) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.GOOD_BASE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("-----", s);
                        JSONObject obj = JsonUtil.getJSONObject(s);
                        JSONArray data = JsonUtil.getJSONArray(obj, "data");
                        if (data != null && data.length() != 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject good = JsonUtil.getJSONObject(data, i);
                                list.add(GoodInfoService.getGootInfoFromJson(good));
                            }
                        }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("goodType", cid + "");
                if (order != 0) {
                    maps.put("orderFlag", order + "");
                }
//                maps.put("rn","25");
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }

    public void backClick(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_list_hot:
                list.clear();
                adapter.notifyDataSetChanged();
                getSortData(cid, 1);
                break;
            case R.id.shop_list_count:
                list.clear();
                adapter.notifyDataSetChanged();
                getSortData(cid, 2);
                break;
            case R.id.shop_list_review:
                list.clear();
                adapter.notifyDataSetChanged();
                getSortData(cid, 3);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ShopListActivity.this, GoodDetailActivity.class);
        intent.putExtra("goodInfo", list.get(i));
        startActivity(intent);
    }
}
