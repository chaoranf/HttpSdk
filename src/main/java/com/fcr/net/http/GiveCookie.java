/**
 * 
 */
package com.fcr.net.http;

import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * 实例化api返回cookie
 * 
 * @author chaoranf@jumei.com
 * @date 2015-8-13
 */
public class GiveCookie extends BasicClientCookie {
	/**
	 * @param name
	 * @param value
	 */
	public GiveCookie(String name, String value) {
		super(name, value);
	}

	@Override
	public String toString() {
		return getName() + "=" + getValue();
	}
}
