package com.manager.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manager.shopping.R;
import com.manager.shopping.activitys.GoodDetailActivity;
import com.manager.shopping.bean.GoodInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 文捷 on 2018/4/30.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder1> {
    private Context context;
    private List<GoodInfo> list = new ArrayList<>();
    private LayoutInflater inflater;
    ImageLoader imgLoader;
    DisplayImageOptions options;
    public RecyclerAdapter(Context context, List<GoodInfo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading_64px)
                .showImageOnFail(R.mipmap.icon_error_64px)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public viewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_shop_collect_item, parent);
        return new viewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder1 holder, int position) {
        final GoodInfo info = list.get(position);
        holder.goodName.setText(info.getGoodname());
        holder.goodPrice.setText("￥"+info.getGoodprice());
        imgLoader.loadImage(info.getGoodImage(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                holder.goodImg.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodDetailActivity.class);
                intent.putExtra("goodInfo", info);
                intent.putExtra("fromCollect", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder1 extends RecyclerView.ViewHolder {
        private TextView goodName, goodPrice;
        private ImageView goodImg;

        public viewHolder1(View itemView) {
            super(itemView);
            goodImg = (ImageView) itemView.findViewById(R.id.item_img);
            goodName = (TextView) itemView.findViewById(R.id.item_name);
            goodPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
    }
}
