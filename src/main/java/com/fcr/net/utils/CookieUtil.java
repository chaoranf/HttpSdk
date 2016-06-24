package com.fcr.net.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fcr.net.http.GiveCookie;

import android.content.Context;


/**
 * http请求的时候用到的cookie工具类,用来添加请求头的cookie
 * @author chaoranf@jumei.com
 * @date 2015-8-13
 */
public class CookieUtil {
	
	public static String getRequestCookies(Context context) {
		Map<String, String> cookieMap = getCookiesMap(context);
		
		Iterator<Entry<String, String>> it = cookieMap.entrySet().iterator(); 
		int count = 0;
		String contentStr = "";
		while(it.hasNext()){ 
			@SuppressWarnings("rawtypes")
			java.util.Map.Entry entry = it.next(); 
			count ++ ;
			if(count == 1){
				contentStr = entry.getKey()+"="+entry.getValue();
			}else{
				contentStr += ";"+entry.getKey()+"="+entry.getValue();
			}			
		} 
		
		return contentStr;
	}
	
	/**获取cookie
	 * @param context
	 * @return 返回要添加到header中的cookie
	 */
	public static Map<String, String> getCookiesMap(Context context) {
	    
		Map<String, String> cookieMap = new HashMap<String, String>();
		
		//取手机的相关信息
		if(Constants.mCookieMap !=null && !Constants.mCookieMap.isEmpty()){
			for (Entry<String, String> entry : Constants.mCookieMap.entrySet()) {  
				cookieMap.put(entry.getKey(), entry.getValue());
			}  
		}
		
		//从本地取，从本地取，保证不丢cookie;
		//本地存储的都是最新的一些cookie
		List<GiveCookie> cookieList =CookieSp.getInstance(context).getCookieListFromSp();
		for (GiveCookie item: cookieList) {
			cookieMap.put(item.getName(), item.getValue());
		}
		
		return cookieMap;
	
	}
	/**
	 * 获取本地保存的请求cookie
	 * @param context
	 * @return
	 */
	public static List<GiveCookie> getLocalHttpCookie(Context context){
		return CookieSp.getInstance(context).getCookieListFromSp();
	}
	
	/**
	 * 清除本地保存的请求cookie
	 * @param context
	 */
	public static void clearRequestCookie(Context context){
		CookieSp.getInstance(context).clearLocalCookie();
	}
}
