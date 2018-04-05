package com.manager.shopping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.manager.shopping.R;
import com.manager.shopping.activitys.FindAddActivity;
import com.manager.shopping.activitys.FindDetailActivity;
import com.manager.shopping.bean.Post;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindFragment extends Fragment {
	List<Post> postList = new ArrayList<>();
	CommonAdapter<Post> adapter;
	ListView listView;
	Button find_addBtn;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Bmob.initialize(getActivity(),"844b411fb7129f92886dad13103fde9f");
		View view = inflater.inflate(R.layout.fragment_find, null);
		listView = (ListView) view.findViewById(R.id.findListView);
		find_addBtn = (Button) view.findViewById(R.id.find_addBtn);
        find_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindAddActivity.class);
                startActivity(intent);
            }
        });

        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = postList.get(i);
                Intent intent = new Intent(getActivity(),FindDetailActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
            }
        });
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BmobQuery<Post> query = new BmobQuery<>("Post");
        query.order("-updatedAt");
        query.include("author");
        query.include("image");

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
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e==null){
                    postList = list;
                    initAdapter();
                    adapter.notifyDataSetChanged();
                }
            }
        });

	}

    private void initAdapter() {
        adapter = new CommonAdapter<Post>(getActivity(),postList,R.layout.fragment_find_list_item) {
            @Override
            public void convert(ViewHolder helper, Post item) {
                helper.setText(R.id.find_userName,item.getAuthor().getUsername());
                helper.setText(R.id.fd_title,item.getTitle());
                helper.setText(R.id.fd_content,item.getContent());
                if (item.getImage()!=null){
                    helper.setImageByUrl(R.id.fd_img,item.getImage().getFileUrl());
                }else{
                    helper.setImageResource(R.id.fd_img,R.mipmap.find_img);
                }
            }
        };
        listView.setAdapter(adapter);
    }
}
