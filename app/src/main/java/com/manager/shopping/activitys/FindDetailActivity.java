package com.manager.shopping.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.shopping.R;
import com.manager.shopping.bean.Comment;
import com.manager.shopping.bean.Post;
import com.manager.shopping.bean.UserInfo;
import com.manager.shopping.utils.CommonAdapter;
import com.manager.shopping.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class FindDetailActivity extends AppCompatActivity {
    TextView fd_title,fd_author,fd_content, doComment;
    ImageView fd_img;
    EditText et_coment;
    Post post;
    ListView listView;
    List<Comment> commentList;
    CommonAdapter<Comment> adapter;
    ImageLoader imgLoader;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_detail);
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.find_img)
                .showImageOnFail(R.mipmap.find_img)
                .build();
        initView();
        getCommentList();
    }

    private void initView() {
        fd_title = (TextView) findViewById(R.id.fd_title);
        fd_author = (TextView) findViewById(R.id.fd_userName);
        fd_content = (TextView) findViewById(R.id.fd_content);
        fd_img = (ImageView) findViewById(R.id.fd_img);
        doComment = (TextView) findViewById(R.id.doComent);
        et_coment = (EditText) findViewById(R.id.fd_coment);
        listView = (ListView) findViewById(R.id.comentList);
        commentList = new ArrayList<>();
        fd_title.setText(post.getTitle());
        fd_content.setText(post.getContent());
        fd_author.setText(post.getAuthor().getUsername());
        if (post.getImage()==null||post.getImage().getFileUrl().length()==0){
            fd_img.setBackgroundResource(R.mipmap.grid_hongbei);
        }else{
            imgLoader.loadImage(post.getImage().getFileUrl(), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    fd_img.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
        doComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (et_coment.getText().toString().length()==0){
                    return;
                }
                Comment comment = new Comment();
                comment.setPost(post);
                comment.setUser(UserInfo.getCurrentUser());
                comment.setContent(et_coment.getText().toString());
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            Toast.makeText(FindDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            getCommentList();
                            adapter.setmDatas(commentList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            et_coment.setText("");
                        }
                    }
                });
            }
        });
    }

    private void getCommentList() {
        BmobQuery<Comment> query = new BmobQuery<>("Comment");
        query.addWhereEqualTo("post",new BmobPointer(post));
        query.include("user,post.author");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e==null&&list!=null){
                    commentList = list;
                    initAdapter();
                }else{
                    Toast.makeText(FindDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<Comment>(this,commentList,R.layout.activity_find_detail_comm) {
            @Override
            public void convert(ViewHolder helper, Comment item) {
                helper.setText(R.id.com_content,item.getContent());
                helper.setText(R.id.com_author,item.getUser().getUsername());
            }
        };
        listView.setAdapter(adapter);
    }
    public void backClick(View view) {
        finish();
    }
}
