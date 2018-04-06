package com.manager.shopping.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.manager.shopping.R;
import com.manager.shopping.activitys.CateListActivity;
import com.manager.shopping.activitys.ShopListActivity;
import com.manager.shopping.activitys.ShopSearchActivity;
import com.manager.shopping.adapter.CateGridAdapter;


public class ShopFragment extends Fragment {
    RelativeLayout titleLayout;
    LinearLayout searchLayout;
    private GridView gridView;
    private int[] pics = {R.mipmap.shop_grid_clothes, R.mipmap.shop_grid_food, R.mipmap.shop_grid_phone,  R.mipmap.shop_grid_good};
    private String[] tags = {"服饰美衣","美食甜品","电子产品","家具日用"};
    private CateGridAdapter adapter;
    RequestQueue queue;
//    PopupWindow popupWindow;
    View popView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        queue = Volley.newRequestQueue(getActivity());
        View view = inflater.inflate(R.layout.fragment_shop, null);
        titleLayout = (RelativeLayout) view.findViewById(R.id.titleLayout);
        searchLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cate_search_pop,null);
//        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopWindow();
                getActivity().startActivity(new Intent(getActivity(), ShopSearchActivity.class));
            }
        });
        gridView = (GridView) view.findViewById(R.id.cateGridView);
        adapter = new CateGridAdapter(getActivity(), pics, tags);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selected = tags[position];
                Intent intent = new Intent(getActivity(), ShopListActivity.class);
                intent.putExtra("title", selected);
                intent.putExtra("cid",position+1);
                startActivity(intent);
            }

        });
        return view;
    }

    private void searchCateData(final String searchText) {
//        StringRequest request = new StringRequest(Request.Method.POST,
//                ConstantUtils.JUHE_CATE_URL, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String s) {
//                Gson gson = new Gson();
//                Log.i("-----",s);
//                CateInfo cateInfo = gson.fromJson(s, CateInfo.class);
//                Intent intent = new Intent(getActivity(),CateListActivity.class);
//                intent.putExtra("searchT",searchText);
//
//                intent.putExtra("searchCateInfo",cateInfo);
//                showPopWindow();
//                startActivity(intent);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////				String err = volleyError.getCause().toString();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> maps = new HashMap<String,String>();
//                maps.put("key",ConstantUtils.JUHE_CATE_KEY);
//                maps.put("menu",searchText);
//                maps.put("rn","10");
//                return maps;
//            }
//        };
//        queue.add(request);
//        queue.start();
    }
    private void showPopWindow() {
//
//
//        TextView doSearch = (TextView) popView.findViewById(R.id.doSearch);
//        final EditText et_search = (EditText) popView.findViewById(R.id.et_search);
//
//        if (!popupWindow.isShowing()) {
//
//            //在底部显示
//            popupWindow.showAsDropDown(titleLayout);
//            //popupWindow.showAtLocation(titleLayout, Gravity.BOTTOM, 0, 0);
//            //popupWindow.setAnimationStyle(R.style.mySesrchPopAnim);
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.setTouchable(true);
//            popupWindow.setFocusable(true);
//            backgroundAlpha(0.5f);
//            popView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if (popupWindow.isShowing()) {
//                        backgroundAlpha(1);
//                        popupWindow.dismiss();
//                    }
//                    return false;
//                }
//            });
//
//            doSearch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (et_search.getText().toString().length()!=0){
//                        searchCateData(et_search.getText().toString().trim());
//
//                    }
//                }
//            });
//        } else {
//            backgroundAlpha(1);
//            popupWindow.dismiss();
//        }
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
