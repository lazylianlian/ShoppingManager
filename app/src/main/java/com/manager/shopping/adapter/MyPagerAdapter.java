package com.manager.shopping.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> list;
	
	public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return super.getPageTitle(position);
	}
	
}
