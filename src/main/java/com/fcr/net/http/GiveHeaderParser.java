package com.fcr.net.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fcr.net.utils.CookieSp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * 处理Api返回的header
 * 
 * @author chaoranf@jumei.com
 * @date 2015-8-13
 */
public class GiveHeaderParser {

	/**
	 * 存储set-cookie到本地
	 * @param headers
	 */
	public static void storeCookie(Context context,Map<String, String> headers) {
		if(headers == null){
			return;
		}
		CookieSp.getInstance(context).putSettedCookieToSp(getCookieMap(headers));
	}

	private static List<GiveCookie> getCookieMap(Map<String, String> headers) {
		List<GiveCookie> list = new ArrayList<GiveCookie>();
		String value = headers.get("Set-Cookie");
		if(TextUtils.isEmpty(value)){
			return list;
		}
		String [] setCookies= value.split("____");
		for (String item:setCookies) {
			try {
				list.add(parseRawCookie(item));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private static GiveCookie parseRawCookie(String rawCookie)
			throws Exception {
		if (TextUtils.isEmpty(rawCookie)) {
			return null;
		}

		String[] rawCookieParams = rawCookie.split(";");

		String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
		if (rawCookieNameAndValue.length != 2) {
			throw new Exception("cookie的键值对不合法"+rawCookieParams[0]);
		}

		String cookieName = rawCookieNameAndValue[0].trim();
		String cookieValue = rawCookieNameAndValue[1].trim();
		GiveCookie cookie = new GiveCookie(cookieName, cookieValue);

		return cookie;
	}
}