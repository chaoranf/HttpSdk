package com.fcr.net.iterfaze;

/**
 * Http请求的回调接口
 * @author chaoranf 2015-8-3
 */
public interface IRequestListener {
	/**
	 * @param header 返回json数据的headr
	 * @param response 返回json数据的业务数据
	 * 为什么要在这里返回业务数据，而不同过请求的时候传递引用赋值数据呢？
	 * 因为，这样做可以减少不必要的业务类对数据的持有，只有当有需要的时候才将业务数据进行业务类的持有操作
	 * 注意，response有可能为空，因为有的api解析不需要response
	 * response返回如果是null，应该在meitianHttp做拦截，直接不会进入onSuccess，这样这里便不用进行判空处理，统一在入口处理
	 */
	public void onSuccess(Object response);
	/**
	 * @param header 返回json数据的headr
	 */
	public void onFail(Object header);
	/**
	 * @param header 返回json数据的headr
	 */
	public void onError(Object header);

}
