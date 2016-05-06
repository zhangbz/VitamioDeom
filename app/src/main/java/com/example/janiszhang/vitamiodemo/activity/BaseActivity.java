package com.example.janiszhang.vitamiodemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.Bmob;

/**
 *基类
 * @ClassName: BaseActivity
 */
public class BaseActivity extends AppCompatActivity{

	//Bmob Application ID
	private String Bmob_AppId = "8ccd22955340ad082283bf59fa40aa0c";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, Bmob_AppId);
	}

	//封装Toast,写在BaseActivity中,所有继承BaseActivity的Activity都可以调用ShowToast()方法
	Toast mToast;
	public void ShowToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}
}
