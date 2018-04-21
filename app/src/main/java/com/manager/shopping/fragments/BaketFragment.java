package com.manager.shopping.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.manager.shopping.R;
import com.manager.shopping.activitys.GoodDetailActivity;
import com.manager.shopping.activitys.WebActivity;
import com.manager.shopping.bean.CateCollectionInfo;
import com.manager.shopping.bean.GoodInfo;
import com.manager.shopping.bean.Post;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaketFragment extends Fragment {
    LinearLayout hotBtn, countBtn, reviewBtn;
    ListView listView;
    TextView titleTv;
    List<GoodInfo> goodInfoList = new ArrayList<>();
    CommonAdapter<GoodInfo> adapter;
    RequestQueue queue;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bmob.initialize(getActivity(), "844b411fb7129f92886dad13103fde9f");
        View view = inflater.inflate(R.layout.fragment_baket, null);
        listView = (ListView) view.findViewById(R.id.listView);
        initAdapter();
        listView.setAdapter(adapter);
        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
                intent.putExtra("goodInfo", goodInfoList.get(i));
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {
        BmobQuery<GoodInfo> query = new BmobQuery<>("GoodInfo");
        query.order("-updatedAt");
        query.include("author");
//        query.include("image");

        /*
        缓存策略
        Bmob SDK提供了几种不同的缓存策略，以适应不同应用场景的需求：

            IGNORE_CACHE :只从网络获取数据，且不会将数据缓存在本地，这是默认的缓存策略。
            CACHE_ONLY :只从缓存读取数据，如果缓存没有数据会导致一个BmobException,可以忽略不处理这个BmobException.
            NETWORK_ONLY :只从网络获取数据，同时会在本地缓存数据。
            NETWORK_ELSE_CACHE:先从网络读取数据，如果没有，再从缓存中获取。
            CACHE_ELSE_NETWORK:先从缓存读取数据，如果没有，再从网络获取。
            CACHE_THEN_NETWORK:先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
         */
        //先从网络读取数据，没有再获取缓存数据
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<GoodInfo>() {
            @Override
            public void done(List<GoodInfo> list, BmobException e) {
                if (e == null) {
                    goodInfoList = list;
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initAdapter() {
        adapter = new CommonAdapter<GoodInfo>(getActivity(), goodInfoList, R.layout.activity_shop_list_item) {

            @Override
            public void convert(ViewHolder helper, final GoodInfo item) {
                helper.setText(R.id.item_name, item.getGoodname());
                helper.setText(R.id.item_intro, item.getDescripe());
                if ("1".equals(item.getSource())) {
                    helper.setText(R.id.item_from, "天猫商城");
                } else if ("1".equals(item.getSource())) {
                    helper.setText(R.id.item_from, "京东商城");
                } else {
                    helper.setText(R.id.item_from, "苏宁易购");
                }
                helper.setText(R.id.item_price, "￥" + item.getGoodprice());
                helper.setText(R.id.item_hot, item.getViews() + "");
                helper.setText(R.id.item_count, item.getSells() + "");
                helper.setText(R.id.item_review, item.getComments() + "");
                TextView deleteBtn = helper.getView(R.id.item_detele);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //购物车删除商品
                        deleteGoodInfo(item);
                    }
                });
                helper.getView(R.id.item_add).setVisibility(View.GONE);
                TextView buyBtn = helper.getView(R.id.item_buy);
                buyBtn.setVisibility(View.VISIBLE);
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("loadUrl", item.getLink());
                        startActivity(intent);
                    }
                });

                helper.setImageByUrl(R.id.item_img, item.getGoodImage());

            }
        };
    }

    private void deleteGoodInfo(final GoodInfo item) {
        item.delete(item.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    goodInfoList.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
