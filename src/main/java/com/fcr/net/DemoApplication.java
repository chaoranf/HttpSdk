package com.fcr.net;

import com.fcr.net.http.GiveX509TrustManager;
import com.fcr.net.netutil.GiveHttpSdk;

import android.app.Application;

/**
 * 初始化工作
 * 
 * @author chaoranf
 * @CreateTime 2015-10-08
 * 
 */
public class DemoApplication extends Application {

	private static DemoApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		GiveHttpSdk.getInstance().initHttp(this);
		GiveX509TrustManager.allowAllSSL();
	}

	public static DemoApplication getInstance() {
		return instance;
	}
}
