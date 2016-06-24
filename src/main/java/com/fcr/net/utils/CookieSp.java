package com.fcr.net.utils;

import java.util.ArrayList;
import java.util.List;

import com.fcr.net.http.GiveCookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * 该类保证cookie的本地存储和读取,sharedPreference方式
 * 
 * @author chaoranf@jumei.com
 * @CreateTime 2015-08-12
 */
public class CookieSp {
	public static final String TAG = "MeitianPreference";
	private Context mContext;
	private static CookieSp mInstance = null;
	private SharedPreferences mSharedPreferences;
	private String LOCAL_COOKIE = "local_cookie";
	private String USER_AGENT = "user_agent";
	private String HTTPHEAD = "fcr_http_head";

	private CookieSp(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(HTTPHEAD,
				Context.MODE_PRIVATE);
	}

	public static synchronized CookieSp getInstance(Context context) {
		if (mInstance == null)
			mInstance = new CookieSp(context);
		return mInstance;
	}

	/**
	 * 把api返回的set-cookie存储到本地
	 * @param cookies
	 * @return
	 */
	public boolean putSettedCookieToSp(List<GiveCookie> cookies) {
		if (cookies == null || cookies.isEmpty()) {
			return false;
		}
		Editor editor = mSharedPreferences.edit();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookies.size(); i++) {
			sb.append(cookies.get(i).toString()).append(";");
			editor.putString(cookies.get(i).getName(), cookies.get(i)
					.getValue());
		}
		String havedCookies = mSharedPreferences.getString(LOCAL_COOKIE, "");
		if (TextUtils.isEmpty(havedCookies)) {
			editor.putString(LOCAL_COOKIE, sb.toString());
		} else {
			editor.putString(LOCAL_COOKIE, havedCookies + ";" + sb.toString());
		}
		editor.commit();
		return true;
	}

	public boolean putCookieToSp(GiveCookie cookie) {
		if (cookie == null || TextUtils.isEmpty(cookie.getName())) {
			return false;
		}
		Editor editor = mSharedPreferences.edit();
		editor.putString(cookie.getName(), cookie.getValue());
		editor.commit();
		return true;
	}

	public boolean putValueToSp(String name, String value) {
		if (TextUtils.isEmpty(name)) {
			return false;
		}
		Editor editor = mSharedPreferences.edit();
		editor.putString(name, value);
		editor.commit();
		return true;
	}

	public String getCookieValue(String name) {
		if (TextUtils.isEmpty(name)) {
			return "";
		}
		return mSharedPreferences.getString(name, "");
	}

	/**
	 * 获取设置到本地的cookie列表
	 * @return
	 */
	public List<GiveCookie> getCookieListFromSp() {
		List<GiveCookie> list = new ArrayList<GiveCookie>();
		String cookieString = getSettedCookie();
		if (TextUtils.isEmpty(cookieString)) {
			return list;
		}
		String[] cookies = cookieString.split(";");
		for (String items : cookies) {
			String[] cookie = items.split("=");
			if (cookie.length > 1) {
				GiveCookie _cookie = new GiveCookie(cookie[0], cookie[1]);
				list.add(_cookie);
			}
		}
		return list;
	}

	/**
	 * 获取api设置到本地的cookie
	 * @return
	 */
	private String getSettedCookie() {
		return mSharedPreferences.getString(LOCAL_COOKIE, "");
	}

	public void clearLocalCookie() {
		Editor editor = mSharedPreferences.edit();
		editor.putString(LOCAL_COOKIE, "");
		editor.commit();
	}

}
