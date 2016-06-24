package com.fcr.net.base;

import com.fcr.net.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 所有Activity的基类
 * 
 * @author chaoranf@jumei.com
 * @CreateTime 2015-8-8
 */
public abstract class DemoBaseActivity extends Activity implements
		OnClickListener {

	protected LayoutInflater mInflater;
	protected View mProgressDialog;

	/** title_bar的布局 */
	protected RelativeLayout mTitleLayout;
	protected Button mTitleLeft;
	protected Button mTitleRight;
	protected TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayoutId());

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mInflater = getLayoutInflater();
		/** 获得activity的根视图 */
		ViewGroup rootView = (ViewGroup) getWindow().getDecorView()
				.findViewById(android.R.id.content);
		/** 加载进度条视图 */
		mProgressDialog = mInflater.inflate(R.layout.progress_dialog, null);
		mProgressDialog.setVisibility(View.GONE);
		rootView.addView(mProgressDialog);

		mTitleLayout = (RelativeLayout) findViewById(R.id.base_title_layout);
		mTitleLeft = (Button) mTitleLayout.findViewById(R.id.btn_left);
		mTitleRight = (Button) mTitleLayout.findViewById(R.id.btn_right);
		mTitle = (TextView) mTitleLayout.findViewById(R.id.title_text);
		mTitleLeft.setOnClickListener(this);

		initPage();
		initTitle();
		onCreateFinished();

	}

	public void changeTitle(String titleText) {
		mTitle.setText(titleText);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onActivityDestory();
	}

	@Override
	public void onClick(View v) {
		int key = v.getId();
		if (key == R.id.btn_left) {
			this.finish();
		} else {

		}
		onClickListener(key);
	}

	/**
	 * 菊花消失
	 */
	public void cancelProgressDialog() {
		if (mProgressDialog == null) {
			return;
		}
		mProgressDialog.setVisibility(View.GONE);
	}

	/**
	 * 菊花旋转
	 */
	public void showProgressDialog() {
		if (mProgressDialog == null) {
			return;
		} else if (!mProgressDialog.isShown()) {
			mProgressDialog.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 暂时用ToastUtil的toast，如果需要自定义样式，再开发
	 * 
	 * @param message
	 */
	public void toast(String message) {
	}

	/** 1.00版本增加内容--------begin------ */
	/** 初始化页面，该方法在onCreate的生命周期中 */
	public abstract void initPage();

	/** 设置setContentView的Id */
	public abstract int setLayoutId();

	/** 在Activity创建完成时，做的一些初始化操作 */
	public abstract void onCreateFinished();

	/** 要求子类在销毁时，实现该方法，完成内存释放、广播注销等操作 */
	public abstract void onActivityDestory();

	/** 点击事件 */
	public abstract void onClickListener(int viewId);

	/**
	 * 初始化title_bar,可以用来重写点击事件和title标题等，默认父类做后退为关闭操作,
	 * mTitle操作标题，mTitleLayout操作整个布局
	 */
	public abstract void initTitle();

	/** 1.00版本增加内容--------end------ */

}
