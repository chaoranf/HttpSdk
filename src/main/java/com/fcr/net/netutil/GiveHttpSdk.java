package com.fcr.net.netutil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fcr.net.http.GiveHurlStack;
import com.fcr.net.utils.Constants;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by chaoranf on 16/6/22.
 */
public final class GiveHttpSdk {
    private final String TAG = "default_tag";
    private static RequestQueue mRequestQueue;
    private Context mContext;

    private GiveHttpSdk() {
    }

    public static GiveHttpSdk getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        public static final GiveHttpSdk instance = new GiveHttpSdk();
    }

    /**
     * 必须在Application中初始化此方法
     *
     * @param context
     */
    public void initHttp(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley
                    .newRequestQueue(mContext, new GiveHurlStack());
        }

        return mRequestQueue;
    }

    protected <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    protected <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    protected void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void initCookie() {
        Constants.mCookieMap.put(Constants.CLIENT_V, Constants.CLIENT_V_VALUE);
        Constants.mCookieMap.put(Constants.MODEL, Constants.MODEL_VALUE);
        Constants.mCookieMap.put(Constants.SETUP_SOURCE, "");
    }
}
