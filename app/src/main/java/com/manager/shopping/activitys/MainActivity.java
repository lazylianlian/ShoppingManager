package com.manager.shopping.activitys;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manager.shopping.R;
import com.manager.shopping.fragments.CateFragment;
import com.manager.shopping.fragments.FindFragment;
import com.manager.shopping.fragments.PersonalFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout tv_cate, tv_find, tv_person;
	FragmentManager manager;
	FragmentTransaction transaction;
	CateFragment cateFragment;
	FindFragment findFragment;
	PersonalFragment personalFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		transaction.replace(R.id.mainLayout, cateFragment, "cate").commit();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_cate = (LinearLayout) findViewById(R.id.tv_cate);
		tv_find = (LinearLayout) findViewById(R.id.tv_find);
		tv_person = (LinearLayout) findViewById(R.id.tv_person);
		tv_cate.setOnClickListener(this);
		tv_find.setOnClickListener(this);
		tv_person.setOnClickListener(this);
		cateFragment = new CateFragment();
		findFragment = new FindFragment();
		personalFragment = new PersonalFragment();
	}

	@Override
	public void onClick(View arg0) {
		transaction = manager.beginTransaction();

		switch (arg0.getId()) {
		case R.id.tv_cate:
			transaction.replace(R.id.mainLayout, cateFragment, "cate");
			break;
		case R.id.tv_find:
			transaction.replace(R.id.mainLayout, findFragment, "find");

			break;
		case R.id.tv_person:
			transaction.replace(R.id.mainLayout, personalFragment, "person");

			break;
		default:
			break;
		}
		transaction.commit();
	}
}
