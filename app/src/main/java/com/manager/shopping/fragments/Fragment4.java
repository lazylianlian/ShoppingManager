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

public class Fragment4 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag4, null);
		Button btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),LoginAndRegistActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		return view;
	}
}
