package com.manager.shopping.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.manager.shopping.R;
import com.manager.shopping.activitys.CateListActivity;
import com.manager.shopping.adapter.CateGridAdapter;
import com.manager.shopping.bean.CateInfo;
import com.manager.shopping.constants.ConstantUtils;

import java.util.HashMap;
import java.util.Map;

public class CateFragment extends Fragment{
	RelativeLayout titleLayout;
	Button searchBtn;
	private GridView gridView;
	private int[] pics = {R.mipmap.grid_jiachangcai, R.mipmap.grid_kuaishou, R.mipmap.grid_chuangyi, R.mipmap.grid_sucai, R.mipmap.grid_liangcai, R.mipmap.grid_hongbei, R.mipmap.grid_mianshi, R.mipmap.grid_tang};
	private String[] tags = {"家常菜","快手菜","创意菜","凉菜","素菜","烘焙","面食","汤"};
	private CateGridAdapter adapter;
	RequestQueue queue;
	PopupWindow popupWindow;
	View popView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		queue = Volley.newRequestQueue(getActivity());
		View view = inflater.inflate(R.layout.fragment_cate, null);
		titleLayout = (RelativeLayout) view.findViewById(R.id.titleLayout);
		searchBtn = (Button) view.findViewById(R.id.searchBtn);
		popView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cate_search_pop,null);
		popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopWindow();
			}
		});
		gridView = (GridView) view.findViewById(R.id.cateGridView);
		adapter = new CateGridAdapter(getActivity(), pics, tags);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selected = tags[position];
				Intent intent = new Intent(getActivity(), CateListActivity.class);
				intent.putExtra("title", selected);
				intent.putExtra("cid",position+1);
				startActivity(intent);
			}

		});
		return view;
	}

	private void searchCateData(final String searchText) {
		StringRequest request = new StringRequest(Request.Method.POST,
				ConstantUtils.JUHE_CATE_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String s) {
				Gson gson = new Gson();
				Log.i("-----",s);
				CateInfo cateInfo = gson.fromJson(s, CateInfo.class);
				Intent intent = new Intent(getActivity(),CateListActivity.class);
				intent.putExtra("searchT",searchText);

				intent.putExtra("searchCateInfo",cateInfo);
				showPopWindow();
				startActivity(intent);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
//				String err = volleyError.getCause().toString();

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> maps = new HashMap<String,String>();
				maps.put("key",ConstantUtils.JUHE_CATE_KEY);
				maps.put("menu",searchText);
				maps.put("rn","10");
				return maps;
			}
		};
		queue.add(request);
		queue.start();
	}
	private void showPopWindow() {


		TextView doSearch = (TextView) popView.findViewById(R.id.doSearch);
		final EditText et_search = (EditText) popView.findViewById(R.id.et_search);

		if (!popupWindow.isShowing()) {

			//在底部显示
			popupWindow.showAsDropDown(titleLayout);
			//popupWindow.showAtLocation(titleLayout, Gravity.BOTTOM, 0, 0);
			//popupWindow.setAnimationStyle(R.style.mySesrchPopAnim);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);
			backgroundAlpha(0.5f);
			popView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					if (popupWindow.isShowing()) {
						backgroundAlpha(1);
						popupWindow.dismiss();
					}
					return false;
				}
			});

			doSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (et_search.getText().toString().length()!=0){
						searchCateData(et_search.getText().toString().trim());

					}
				}
			});
		} else {
			backgroundAlpha(1);
			popupWindow.dismiss();
		}
	}
	/*
     *   设置窗体背景颜色变化
     */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
		layoutParams.alpha = bgAlpha;
		getActivity().getWindow().setAttributes(layoutParams);
	}
}
