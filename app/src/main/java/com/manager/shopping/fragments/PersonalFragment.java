package com.manager.shopping.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manager.shopping.R;
import com.manager.shopping.activitys.GoodDetailActivity;
import com.manager.shopping.activitys.PersonalSetActivity;
import com.manager.shopping.activitys.ShopListActivity;
import com.manager.shopping.adapter.RecyclerAdapter;
import com.manager.shopping.bean.CateCollectionInfo;
import com.manager.shopping.bean.GoodInfo;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.constants.ConstantUtils;
import com.manager.shopping.service.GoodInfoService;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ImagePathUtils;
import com.manager.shopping.utils.JsonUtil;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends Fragment {
	RecyclerView recyclerView;
	List<GoodInfo> list = new ArrayList<>();
	RecyclerAdapter adapter;
	Button collectBtn;
	ImageView settingBtn,personal_img;
	TextView person_name,person_word,toast_collec;
    TextView photoChoose,cancle;//popWindow中控件
    PopupWindow popupWindow;
    View popView;
    ImageLoader imgLoader;
    DisplayImageOptions options;
    RequestQueue queue;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Bmob.initialize(getActivity(),"844b411fb7129f92886dad13103fde9f");
        queue = Volley.newRequestQueue(getActivity());
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .build();
        View view = inflater.inflate(R.layout.fragment_personal, null);
        popView = inflater.inflate(R.layout.fragment_personal_pop, null);
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initPopWindow();

        recyclerView = (RecyclerView) view.findViewById(R.id.personListView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        person_name = (TextView) view.findViewById(R.id.person_name);
		person_word = (TextView) view.findViewById(R.id.person_word);
        toast_collec = (TextView) view.findViewById(R.id.toast_collec);
        personal_img = (ImageView) view.findViewById(R.id.personal_img) ;
        personal_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow();
            }
        });
        initUserInfo();
        settingBtn = (ImageView) view.findViewById(R.id.personal_setting);
		settingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),PersonalSetActivity.class);
				startActivity(intent);
			}
		});
        initData();
		return view;
	}

    private void initPopWindow() {
        photoChoose = (TextView) popView.findViewById(R.id.photoChoose);
        cancle = (TextView) popView.findViewById(R.id.cancle);

        photoChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //android6.0 动态申请权限
                if (Build.VERSION.SDK_INT >= 23) {
                    if(ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                2);
                        return;
                    }else{
                        //进入系统相册更换用户图片
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra("crop", true);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 2);
                    }
                } else {
                    //进入系统相册更换用户图片
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra("crop", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 2);
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                backgroundAlpha(1);
            }
        });
    }


    /**
     * 获取手机相册图片
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getActivity().getContentResolver();
            try {
                Bitmap bmp = BitmapFactory
                        .decodeStream(cr.openInputStream(uri));
                personal_img.setImageBitmap(bmp);
                popupWindow.dismiss();
                backgroundAlpha(1);
                String path = ImagePathUtils.getRealPathFromUri(getActivity(),uri);
                doPostImageFile(path);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    private void doPostImageFile(String path) {
        final BmobFile imageFile = new BmobFile(new File(path));
        imageFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    //String url = imageFile.getFileUrl();
                    //上传图片成功，更换用户头像
                    popupWindow.dismiss();
                    backgroundAlpha(1);
                    doSetUserImg(imageFile);

                }
            }

        });
    }
    private void doSetUserImg(BmobFile imageFile) {
        UserInfo newUser = new UserInfo();
        UserInfo bmobUser = UserInfo.getCurrentUser();
        newUser.setUserHeadImg(imageFile);
        newUser.setUserWord(bmobUser.getUserWord());
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        initData();
    }


    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        initData();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        UserInfo userInfo = UserInfo.getCurrentUser();
        person_name.setText(userInfo.getUsername());
        if (userInfo.getUserHeadImg()==null||userInfo.getUserHeadImg().getFileUrl().length()==0){
            personal_img.setBackgroundResource(R.mipmap.person_default_img);
        }else{
            imgLoader.loadImage(userInfo.getUserHeadImg().getFileUrl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    personal_img.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }

        if (userInfo.getUserWord()==null||userInfo.getUserWord().equals("")){
            person_word.setText("我的美食宣言");

        }else{
            person_word.setText(userInfo.getUserWord());
        }
    }


    private void initData() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstantUtils.GOOD_COLLECT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("-----", s);
                JSONObject obj = JsonUtil.getJSONObject(s);
                JSONArray data = JsonUtil.getJSONArray(obj, "data");
                if (data != null && data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject good = JsonUtil.getJSONObject(data, i);
                        list.add(GoodInfoService.getGootInfoFromJson(good));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userId", UserInfo.getCurrentUser() + "");
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }
    /*
     *   代码计算ListView的高度
     *   切换为recyclerView,注释掉了
     */
//    public void setListViewHeightBasedOnChildren(RecyclerView listView) {
//        // 获取ListView对应的Adapter
//        CommonAdapter<CateCollectionInfo> listAdapter = (CommonAdapter<CateCollectionInfo>) listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//        recyclerView.get
//        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
//            // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            // 计算子项View 的宽高
//            listItem.measure(0, 0);
//            // 统计所有子项的总高度
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + 400 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        // recyclerView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
//        listView.setLayoutParams(params);
//    }

    /*
     *   popWindow显示隐藏
     */
    private void showPopWindow() {
        //第三方分享
        if (!popupWindow.isShowing()) {
            //在底部显示
            popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
            popupWindow.setAnimationStyle(R.style.myPopWindowAnim);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            backgroundAlpha(0.8f);
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
