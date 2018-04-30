package com.manager.shopping.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manager.shopping.R;
import com.manager.shopping.bean.GoodInfo;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.constants.ConstantUtils;
import com.manager.shopping.service.GoodInfoService;
import com.manager.shopping.utils.JsonUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class GoodDetailActivity extends Activity implements View.OnClickListener {
    private GoodInfo info;
    private Boolean fromCollect;
    private TextView goodName, goodIntro, goodPrice, goodFrom, goodCount, goodHot, goodReview, goodBuy, goodAdd;
    private ImageView goodImg, goodLike;
    RequestQueue queue;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "844b411fb7129f92886dad13103fde9f");

        setContentView(R.layout.activity_good_detail);
        info = (GoodInfo) getIntent().getSerializableExtra("goodInfo");
        fromCollect = getIntent().getBooleanExtra("fromCollect",false);
        queue = Volley.newRequestQueue(this);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        initData();
    }

    private void initData() {
        goodName = (TextView) findViewById(R.id.good_name);
        goodIntro = (TextView) findViewById(R.id.good_intro);
        goodPrice = (TextView) findViewById(R.id.good_price);
        goodCount = (TextView) findViewById(R.id.good_count);
        goodHot = (TextView) findViewById(R.id.good_hot);
        goodReview = (TextView) findViewById(R.id.good_review);
        goodFrom = (TextView) findViewById(R.id.good_from);
        goodAdd = (TextView) findViewById(R.id.good_add);
        goodBuy = (TextView) findViewById(R.id.good_buy);
        goodLike = (ImageView) findViewById(R.id.good_like);
        goodImg = (ImageView) findViewById(R.id.good_img);
        goodName.setText(info.getGoodname());
        goodIntro.setText(info.getDescripe());
        goodPrice.setText("￥"+info.getGoodprice());
        goodCount.setText(info.getSells()+"");
        goodHot.setText(info.getViews()+"");
        goodReview.setText(info.getComments()+"");
        if ("1".equals(info.getSource())) {
            goodFrom.setText("天猫商城");
        } else if ("2".equals(info.getSource())) {
            goodFrom.setText("京东商城");
        } else {
            goodFrom.setText("苏宁易购");
        }
        goodLike.setOnClickListener(this);
        goodBuy.setOnClickListener(this);
        goodAdd.setOnClickListener(this);
        if (fromCollect){
            goodLike.setImageResource(R.mipmap.cate_list_like_click);
        }else {
            goodLike.setImageResource(R.mipmap.cate_list_like_normal);
        }
        imageLoader.loadImage(info.getGoodImage(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                goodImg.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void backClick(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.good_like:
                sendLikeData();
                break;
            case R.id.good_add:
                AddBarketData(info);
                break;
            case R.id.good_buy:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("loadUrl", info.getLink());
                startActivity(intent);
                break;

        }
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
                    Toast.makeText(GoodDetailActivity.this, "购物车已有该宝贝", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GoodDetailActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 加入收藏夹
     */
    private void sendLikeData() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.GOOD_ADD_COLLECT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("-----", s);
                JSONObject obj = JsonUtil.getJSONObject(s);
                String msg = JsonUtil.getString(obj, "result");
                if ("success".equals(msg)){
                    goodLike.setImageResource(R.mipmap.cate_list_like_click);
                }
                Toast.makeText(GoodDetailActivity.this, msg+"", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("goodId", info.getGoodid() + "");
                maps.put("userId", UserInfo.getCurrentUser() + "");
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }

    /**
     * 删除收藏夹中的商品
     */
    private void delLikeData() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.GOOD_DEL_COLLECT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("-----", s);
                JSONObject obj = JsonUtil.getJSONObject(s);
                String msg = JsonUtil.getString(obj, "result");
                if ("success".equals(msg)){
                    goodLike.setImageResource(R.mipmap.cate_list_like_normal);
                }
                Toast.makeText(GoodDetailActivity.this, msg+"", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("goodId", info.getGoodid() + "");
                maps.put("userId", UserInfo.getCurrentUser() + "");
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }
}
