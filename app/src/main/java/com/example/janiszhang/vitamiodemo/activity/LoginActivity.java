package com.example.janiszhang.vitamiodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.janiszhang.vitamiodemo.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class LoginActivity extends BaseActivity {

    private EditText mUserName;
    private EditText mPassWord;
    private Button mSignUp;
    private Button mLongin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FindViewById();

        SetOnClickListener();

        //如果用户已经登录过,且没有主动退出,则跳过登录注册过程,直接进入视频列表
        BmobUser currentUser = BmobUser.getCurrentUser(this);
        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, VideoListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void SetOnClickListener() {
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(mUserName.getText().toString());
                bmobUser.setPassword(mPassWord.getText().toString());
                bmobUser.signUp(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("注册成功");
                        Intent intent = new Intent(LoginActivity.this, VideoListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("登录失败:" + s);
                    }
                });
            }
        });

        mLongin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(mUserName.getText().toString());
                bmobUser.setPassword(mPassWord.getText().toString());
                bmobUser.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("登录成功");
                        Intent intent = new Intent(LoginActivity.this, VideoListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("登录失败:" + s);
                    }
                });
            }
        });
    }

    private void FindViewById() {
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassWord = (EditText) findViewById(R.id.pass_word);
        mSignUp = (Button) findViewById(R.id.sign_up);
        mLongin = (Button) findViewById(R.id.login);
    }
}
