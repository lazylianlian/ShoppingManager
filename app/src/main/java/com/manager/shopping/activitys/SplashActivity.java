package com.manager.shopping.activitys;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.manager.shopping.R;
import com.manager.shopping.bean.UserInfo;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends Activity {
	Timer timer;
	private SharedPreferences sp;
	String string;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Bmob.initialize(this,"844b411fb7129f92886dad13103fde9f");
		ShareSDK.initSDK(this);
		//�洢
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		string = sp.getString("edit", "first");
		
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (string.equals("first")) {
					Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
					startActivity(intent);
				}else {
					UserInfo currentUser = BmobUser.getCurrentUser(UserInfo.class);
					Intent intent;
					if (currentUser==null){
						intent = new Intent(SplashActivity.this,LoginAndRegistActivity.class);

					}else {
						intent = new Intent(SplashActivity.this, MainActivity.class);

					}
					startActivity(intent);
				}
				finish();
				
			}
		}, 2000);
	}
}
