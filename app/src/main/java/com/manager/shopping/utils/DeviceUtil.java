package com.manager.shopping.utils;

import android.content.Context;
import android.view.WindowManager;

import com.manager.shopping.base.BaseActivity;


/**
 * Created by Administrator on 2016/12/27.
 * 屏幕适配工具类
 * @author 文捷
 */

public class DeviceUtil {

    /**
     * 得到屏幕的宽
     * @param context
     * @return 屏幕宽
     */
    public static int getScreenWidth(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getWidth();
    }
    /**
     * 得到屏幕的高
     * @param context
     * @return 屏幕高
     */
    public static int getScreenHeight(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getHeight();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
	public static int dip2px(float dpValue) {
		final float scale = BaseActivity.currentActivity().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
	public static int px2dip(float pxValue) {
		final float scale = BaseActivity.currentActivity().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
