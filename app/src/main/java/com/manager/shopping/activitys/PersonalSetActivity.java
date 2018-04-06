package com.manager.shopping.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.shopping.R;
import com.manager.shopping.bean.UserInfo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalSetActivity extends AppCompatActivity {
    EditText set_name,set_word;
    TextView set_email;
    TextView clearUserBtn;
    ImageView setBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_set);
        initView();
    }

    private void initView() {
        set_name = (EditText) findViewById(R.id.set_name);
        set_email = (TextView) findViewById(R.id.set_email);
        set_word = (EditText) findViewById(R.id.set_word);
        setBtn = (ImageView) findViewById(R.id.setBtn);
        clearUserBtn = (TextView) findViewById(R.id.clearCurrentBtn);
        UserInfo userInfo = UserInfo.getCurrentUser();
        set_name.setText(userInfo.getUsername());
        set_word.setText(userInfo.getUserWord());
        set_email.setText(userInfo.getEmail());

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = set_name.getText().toString();
                String word = set_word.getText().toString();
                doUpdateUserInfo(name,word);
            }
        });
        clearUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();   //清除缓存用户对象
                Intent intent = new Intent(PersonalSetActivity.this,LoginAndRegistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 修改用户信息
     * @param username
     * @param word
     */
    private void doUpdateUserInfo(String username,String word) {
        UserInfo newUser = new UserInfo();

        UserInfo bmobUser = UserInfo.getCurrentUser();

        if (bmobUser.getUsername().equals(username)&&bmobUser.getUserWord().equals(word)){
            Toast.makeText(PersonalSetActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bmobUser.getUsername().equals(username)&&!bmobUser.getUserWord().equals(word)){
            newUser.setUsername(username);
            newUser.setUserWord(word);
        }
        if (bmobUser.getUsername().equals(username)){
            newUser.setUserWord(word);
        }
        if (bmobUser.getUserWord().equals(word)){
            newUser.setUsername(username);
        }
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(PersonalSetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(PersonalSetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void backClick(View view) {
        finish();
    }

}
