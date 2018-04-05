package com.manager.shopping.activitys;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.manager.shopping.R;
import com.manager.shopping.bean.CateCollectionInfo;
import com.manager.shopping.bean.CateInfo;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.constants.ConstantUtils;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CateListActivity extends Activity {
    ListView cateListView;
    TextView titleTv;
    List<CateInfo.ResultBean.CateDetailInfo> cateDetailInfos = new ArrayList<>();
    CommonAdapter<CateInfo.ResultBean.CateDetailInfo> adapter;
    RequestQueue queue;
    Button collectBtn;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catelist);
        Bmob.initialize(this,"844b411fb7129f92886dad13103fde9f");

        queue = Volley.newRequestQueue(this);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        titleTv = (TextView) findViewById(R.id.tv_title);
        cateListView = (ListView) findViewById(R.id.cateList_lv);

        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null){
            String title = intent.getStringExtra("title");
            int cid = intent.getIntExtra("cid", 1);
            titleTv.setText(title + "");
            GetInternetData(cid);
        }else{
            String title = intent.getStringExtra("searchT");
            titleTv.setText(title + "");

            CateInfo cateInfo = (CateInfo)intent.getSerializableExtra("searchCateInfo");
            cateDetailInfos = cateInfo.getResult().getData();
            initAdapter();
        }
        cateListView.setOnScrollListener(new PauseOnScrollListener(imageLoader,false,true));

        cateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                Intent intent = new Intent(CateListActivity.this,CateDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
                intent.putExtra("bundle",bundle);
                startActivity(intent);

            }
        });
    }
    /*
     *  获取网络数据
     */
    private void GetInternetData(final int cid) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConstantUtils.JUHE_TAG_SEARCH_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Log.i("-----",s);
                CateInfo cateInfo = gson.fromJson(s, CateInfo.class);
                Log.i("hahhahahhaa",cateInfo.toString());
                cateDetailInfos = cateInfo.getResult().getData();
                initAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String err = volleyError.getCause().toString();
                Toast.makeText(CateListActivity.this, err, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> maps = new HashMap<String,String>();
                maps.put("key",ConstantUtils.JUHE_CATE_KEY);
                maps.put("cid",cid+"");
                maps.put("pn","1");
                maps.put("rn","25");
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<CateInfo.ResultBean.CateDetailInfo>(this,cateDetailInfos,R.layout.activity_cate_list_item) {
            @Override
            public void convert(ViewHolder helper, final CateInfo.ResultBean.CateDetailInfo item) {
                helper.setText(R.id.cateNameTv, item.getTitle());
                //helper.setImageByUrl(R.id.cate_img, item.getAlbums().get(0));
                //解决ImageLoader在快速滑动时加载图片的问题
                final ImageView imgView = helper.getView(R.id.cate_img);
                imageLoader.loadImage(item.getAlbums().get(0),options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        imgView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
                collectBtn = helper.getView(R.id.collectBtn);
                CateInfo.ResultBean.CateDetailInfo cInfo = item;
                final CateCollectionInfo cateCollectionInfo = new CateCollectionInfo(cInfo.getId(), cInfo.getTitle(), cInfo.getAlbums().get(0));

                collectExitMethod(cateCollectionInfo);
//                if(manager.isCollected(item.getId())){
//                    helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_normal);
//                }else{
//                    helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_click);
//                }
                collectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //addInfo(cateCollectionInfo);
                        addCollectMethod(cateCollectionInfo);
//                        boolean result = manager.addCateCollecInfo(cateCollectionInfo);
//                        if (result){
//                            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
//                        }else{
//                            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
//                        }
                        //Toast.makeText(CateListActivity.this, cInfo.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        cateListView.setAdapter(adapter);

        cateListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
    }

    /**
     * 收藏或取消收藏方法
     * @param cateCollectionInfo
     */
    private void addCollectMethod(final CateCollectionInfo cateCollectionInfo) {

        BmobQuery<CateCollectionInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("userInfo", UserInfo.getCurrentUser());
        query.addWhereEqualTo("id",cateCollectionInfo.getId());
        query.findObjects(new FindListener<CateCollectionInfo>() {
            @Override
            public void done(List<CateCollectionInfo> list, BmobException e) {
                if (list.size()==0){
                    addInfo(cateCollectionInfo);
                }else{
                    deleteCollectInfo(cateCollectionInfo);
                }
            }
        });
    }

    /**
     * 收藏美食
     * @param cInfo
     */
    private void addInfo(CateCollectionInfo cInfo) {
        CateCollectionInfo info = new CateCollectionInfo();
        info.setTitle(cInfo.getTitle());
        info.setAlbums(cInfo.getAlbums());
        info.setId(cInfo.getId());
        info.setUserInfo(UserInfo.getCurrentUser());
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
                    initAdapter();
                }
            }
        });
    }

    private void collectExitMethod(CateCollectionInfo cateCollectionInfo) {
        BmobQuery<CateCollectionInfo> query = new BmobQuery<>("CateCollectionInfo");
        query.addWhereEqualTo("userInfo", UserInfo.getCurrentUser());
        query.addWhereEqualTo("id",cateCollectionInfo.getId());
        query.findObjects(new FindListener<CateCollectionInfo>() {
            @Override
            public void done(List<CateCollectionInfo> list, BmobException e) {
                if (list.size()==0){
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                }else{
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
                }

            }
        });
    }
    /**
     * 取消收藏
     * @param cateCollectionInfo 所选的美食
     */
    private void deleteCollectInfo(CateCollectionInfo cateCollectionInfo) {

        cateCollectionInfo.delete(cateCollectionInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                    initAdapter();
                }
            }
        });
    }
    public void backClick(View view) {
        finish();
    }
}
