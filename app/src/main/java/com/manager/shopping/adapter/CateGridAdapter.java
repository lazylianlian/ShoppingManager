package com.manager.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manager.shopping.R;


public class CateGridAdapter extends BaseAdapter {
	private Context context;
	private int[] pics;
	private String[] tags;
	
	
	public CateGridAdapter(Context context, int[] pics, String[] tags) {
		super();
		this.context = context;
		this.pics = pics;
		this.tags = tags;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pics.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return tags[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (arg1==null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.fragment_cate_grid_item, null);
			vh.tagTv = (TextView) arg1.findViewById(R.id.grid_item_text);
			vh.imgView = (ImageView) arg1.findViewById(R.id.grid_item_img);
			arg1.setTag(vh);
		}else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tagTv.setText(tags[arg0]);
		vh.imgView.setBackgroundResource(pics[arg0]);
		return arg1;
	}
	class ViewHolder{
		private TextView tagTv;
		private ImageView imgView;
		
	}

}
