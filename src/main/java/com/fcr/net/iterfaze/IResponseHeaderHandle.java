package com.fcr.net.iterfaze;

import java.util.Map;

/**
 * http请求的返回header的处理
 * @author chaoranf@jumei.com
 * @date 2015-8-13
 */
public interface IResponseHeaderHandle {
	public void handleCookie(Map<String, String> headers);
}
