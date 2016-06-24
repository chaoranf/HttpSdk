package com.fcr.net.http;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.fcr.net.iterfaze.IResponseHeaderHandle;
import com.fcr.net.utils.CookieUtil;
import com.fcr.net.utils.JsonUtil;

/**
 * 封装http请求，加上对返回header的处理
 * 
 * @author chaoranf@jumei.com
 * @date 2015-8-13
 */
public class GiveRequest extends StringRequest implements
        IResponseHeaderHandle {

    private static final int REQUEST_TIME_OUT = 2000;

    private Context mContext;

    public GiveRequest(Context context, int method, String url,
                       Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mContext = context;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // TODO Auto-generated method stub
        return new DefaultRetryPolicy(REQUEST_TIME_OUT, 0, 1.0f);
    }

    @Override
    protected void deliverResponse(String response) {
        response = JsonUtil.getCorrectJson(response);
        super.deliverResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        handleCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    @Override
    public void handleCookie(Map<String, String> headers) {
        GiveHeaderParser.storeCookie(mContext, headers);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> temp = new HashMap<String, String>();
        temp.put("Cookie", CookieUtil.getRequestCookies(mContext));
        //apikey,先无耻的写死在这里
        temp.put("apikey", "315ec2b823c85f76d98449369427068f");
        // temp.put("User-Agent",
        // CookieSp.getInstance(mContext).getUserAgent());
        return temp;
    }

}
