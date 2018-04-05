package com.manager.shopping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.manager.shopping.R;
import com.manager.shopping.activitys.LoginAndRegistActivity;


public class Fragment3 extends Fragment {
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag3, null);
		btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), LoginAndRegistActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
}
