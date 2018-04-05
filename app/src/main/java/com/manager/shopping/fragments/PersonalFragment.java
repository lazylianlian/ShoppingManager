package com.manager.shopping.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.shopping.R;
import com.manager.shopping.activitys.CateDetailActivity;
import com.manager.shopping.activitys.PersonalSetActivity;
import com.manager.shopping.bean.CateCollectionInfo;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ImagePathUtils;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends Fragment {
	ListView listView;
	List<CateCollectionInfo> cList = new ArrayList<>();
	CommonAdapter<CateCollectionInfo> adapter;
	Button collectBtn;
	ImageView settingBtn,personal_img;
	TextView person_name,person_word,toast_collec;
    TextView photoChoose,cancle;//popWindow中控件
    PopupWindow popupWindow;
    View popView;
    ImageLoader imgLoader;
    DisplayImageOptions options;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Bmob.initialize(getActivity(),"844b411fb7129f92886dad13103fde9f");
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .build();
        View view = inflater.inflate(R.layout.fragment_personal, null);
        popView = inflater.inflate(R.layout.fragment_personal_pop, null);
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initPopWindow();

        listView = (ListView) view.findViewById(R.id.personListView);
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
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
				collectBtn = (Button) view.findViewById(R.id.collectBtn);
				collectBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
                        deleteCollectInfo(cList.get(i));
//						boolean result = manager.addCateCollecInfo(cList.get(i));
//						if (!result){
//							collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
//							initData();
//							Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
//						}
					}
				});

				Intent intent = new Intent(getActivity(),CateDetailActivity.class);
                intent.putExtra("id",cList.get(i).getId());
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
//				intent.putExtra("bundle",bundle);
				startActivity(intent);

			}
		});
		return view;
	}

    private void initPopWindow() {
        photoChoose = (TextView) popView.findViewById(R.id.photoChoose);
        cancle = (TextView) popView.findViewById(R.id.cancle);
        photoChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入系统相册更换用户图片
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 2);
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

    /**s
     * 取消收藏
     * @param cateCollectionInfo 所选的美食
     */
    private void deleteCollectInfo(CateCollectionInfo cateCollectionInfo) {
        cateCollectionInfo.delete(cateCollectionInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                    initData();
                }
            }
        });
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
        cList = new ArrayList<>();
        BmobQuery<CateCollectionInfo> bmobQuery = new BmobQuery<>("CateCollectionInfo");
        bmobQuery.addWhereEqualTo("userInfo", UserInfo.getCurrentUser());
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

        bmobQuery.findObjects(new FindListener<CateCollectionInfo>() {
            @Override
            public void done(List<CateCollectionInfo> list, BmobException e) {
                if (e==null){
                    if (list.size()==0){
                        toast_collec.setVisibility(View.VISIBLE);
                    }else{
                        cList = list;
                        adapter.setmDatas(cList);
                        toast_collec.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<CateCollectionInfo>(getActivity(), cList, R.layout.activity_cate_list_item) {
            @Override
            public void convert(ViewHolder helper, CateCollectionInfo item) {
                helper.setText(R.id.cateNameTv, item.getTitle());
                helper.setImageByUrl(R.id.cate_img, item.getAlbums());
                helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_click);
            }
        };
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }
    /*
     *   代码计算ListView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        CommonAdapter<CateCollectionInfo> listAdapter = (CommonAdapter<CateCollectionInfo>) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 400 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

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
