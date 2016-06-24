package com.fcr.net.utils;

/**
 * Json的工具类，用来处理返回的json数据
 * @author chaoranf@jumei.com
 * @date 2015-8-19
 */
public class JsonUtil {

	/**
	 * 处理不规范的返回body
	 * @param sResponse 接收到的响应body
	 * @return 返回json形式的字符串
	 */
	public static String getCorrectJson(String sResponse){
		int indexo=sResponse.indexOf("{");
		
		if( indexo>=0){
			sResponse = sResponse.substring(indexo);
		}else{
			sResponse = "{}";
		}
		
		return sResponse;
	}
	
}
