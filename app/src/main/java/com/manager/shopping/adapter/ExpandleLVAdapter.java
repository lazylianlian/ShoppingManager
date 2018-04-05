package com.manager.shopping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manager.shopping.R;
import com.manager.shopping.bean.CateInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 美食详情页面ExpandleListView适配器
 * Created by 文捷 on 2017/1/6.
 */

public class ExpandleLVAdapter extends BaseExpandableListAdapter{
    private List<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo> stepList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ExpandleLVAdapter(Context context, List<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo> stepList) {
        this.context = context;
        this.stepList = stepList;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cate_default)
                .showImageOnFail(R.mipmap.cate_default)
                .build();
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return stepList.size();
    }

    @Override
    public Object getGroup(int i) {
        return "制作步骤";
    }

    @Override
    public Object getChild(int i, int i1) {
        return stepList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView groupTv = new TextView(context);
        groupTv.setText("制作步骤");
        groupTv.setPadding(20,10,0,0);
        groupTv.setTextColor(Color.BLACK);
        return groupTv;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null){
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_cate_detail_expand_item,null);
            vh.step_img = (ImageView) view.findViewById(R.id.step_img);
            vh.step_text = (TextView) view.findViewById(R.id.step_text);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
            vh.step_text.setText(stepList.get(i1).getStep());
            imageLoader.displayImage(stepList.get(i1).getImg(),vh.step_img,options);

        return view;
    }
    class ViewHolder{
        ImageView step_img;
        TextView step_text;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
