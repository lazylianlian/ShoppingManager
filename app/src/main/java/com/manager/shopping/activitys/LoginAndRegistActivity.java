package com.manager.shopping.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.manager.shopping.R;
import com.manager.shopping.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录注册
 * @author 文捷
 */
public class LoginAndRegistActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout loginLayout, registLayout;
    Button login_login, login_regist, regist_login, regist_regist;
    EditText login_userName, login_pass, regist_email, regist_userName,
            regist_pass, regist_secondPass;
    RequestQueue queue;
    SharedPreferences sp;
    TextView toast_pass1,toast_pass2,toast_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_regist);
        initView();
        queue = Volley.newRequestQueue(LoginAndRegistActivity.this);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String uemail = sp.getString("uemail", "null");
        if (!"null".equals(uemail)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginAndRegistActivity.this);
            builder.setMessage("退出当前账户？");
            builder.setCancelable(false);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("uemail", "null");
                            editor.putString("uid", "null");
                            editor.putString("uphoto", "null");
                            editor.putString("utype", "null");
                            editor.commit();
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.show();
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        registLayout = (LinearLayout) findViewById(R.id.registLayout);
        registLayout.setVisibility(View.GONE);
        toast_email = (TextView) findViewById(R.id.toast_email);
        toast_pass1 = (TextView) findViewById(R.id.Toast_pass1);
        toast_pass2 = (TextView) findViewById(R.id.Toast_pass2);
        login_login = (Button) findViewById(R.id.login_login);
        login_regist = (Button) findViewById(R.id.login_regist);
        regist_login = (Button) findViewById(R.id.regist_login);
        regist_regist = (Button) findViewById(R.id.regist_regist);
        login_userName = (EditText) findViewById(R.id.login_username);
        login_pass = (EditText) findViewById(R.id.login_pass);
        regist_email = (EditText) findViewById(R.id.regist_email);
        regist_userName = (EditText) findViewById(R.id.regist_userName);
        regist_pass = (EditText) findViewById(R.id.regist_pass);
        regist_secondPass = (EditText) findViewById(R.id.regist_secondPass);

        login_login.setOnClickListener(this);
        login_regist.setOnClickListener(this);
        regist_login.setOnClickListener(this);
        regist_regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                String email = login_userName.getText().toString().trim();
                String pass = login_pass.getText().toString().trim();
                Log.i("~~~~email~~~~", email);
                Log.i("~~~~pass~~~~", pass);

                if (!(email==null&&"".equals(email))&&!(pass==null&&"".equals(pass))) {
                    //执行登录操作
                    DoLoginData(email, pass);
                }
                break;
            case R.id.login_regist://切换为注册页面
                regist_email.setText("");
                regist_pass.setText("");
                regist_secondPass.setText("");
                regist_userName.setText("");
                loginLayout.setVisibility(View.GONE);
                registLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.regist_login://切换为登录页面
                login_userName.setText("");
                login_pass.setText("");
                loginLayout.setVisibility(View.VISIBLE);
                registLayout.setVisibility(View.GONE);
                break;
            case R.id.regist_regist:
                String rPass = regist_pass.getText().toString().trim();
                String secondPass = regist_secondPass.getText().toString().trim();
                if (!rPass.equals(secondPass)) {
                    toast_pass2.setVisibility(View.VISIBLE);
                }else {
                    toast_pass2.setVisibility(View.GONE);

                    //执行注册操作
                    String remail = regist_email.getText().toString().trim();
                    String rUserName = regist_userName.getText().toString().trim();
                    DoRegistData(rUserName, remail, rPass);
                }
                break;

            default:
                break;
        }
    }
    /*
     *  用户登录
     */
    public void DoLoginData(String email,String pass) {
        UserInfo.loginByAccount(email, pass, new LogInListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e==null){
                    toast_pass1.setVisibility(View.GONE);

                    UserInfo currentUser = BmobUser.getCurrentUser(UserInfo.class);
                    Intent intent = new Intent(LoginAndRegistActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast_pass1.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /*
     *  新用户注册
     */
    public void DoRegistData(String userName,String email,String pass) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userName);
        userInfo.setEmail(email);
        userInfo.setPassword(pass);
        userInfo.signUp(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e==null){
                    toast_email.setVisibility(View.VISIBLE);
                    toast_email.setText("注册成功，请直接登录");
                }else{
                    toast_email.setVisibility(View.VISIBLE);
                    toast_email.setText(e.getMessage());
                }
            }
        });
    }
    public void backClick(View view) {
        finish();
    }
}

