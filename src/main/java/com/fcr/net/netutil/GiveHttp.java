package com.fcr.net.netutil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fcr.net.http.GiveHttpHeader;
import com.fcr.net.http.GiveRequest;
import com.fcr.net.http.GiveUploadFile;
import com.fcr.net.iterfaze.IRequestListener;

/**
 * Http请求的方法类，采用Volley+fastJson的数据通信和解析方式
 *
 * @author chaoranf@jumei.com
 * @CreateTime 2015-8-8
 */
public class GiveHttp {

    private static final String TAG = "GiveHttp";

    private static final String CHAR_SET = "UTF-8";

    private static final String BOUNDARY = "GiveHttp___BOUNDARY";

    //TODO暂时写死，以后优化成可配置或者加入到参数
    private static final boolean IS_USE_HEAD = false;

    /**
     * 网络请求错误时的处理
     *
     * @param error
     * @param callback
     */
    private static void handleResponse(VolleyError error,
                                       final IRequestListener callback) {
        GiveHttpHeader tempHeader = new GiveHttpHeader();

        if (TextUtils.isEmpty(error.getMessage())) {
            tempHeader.reason = "当前网络不好哟，请稍候重试哦~";
        } else {
            tempHeader.reason = "" + error.getMessage();
        }
        if (callback != null) {
            callback.onError(tempHeader);
        }
    }

    /**
     * 处理没有业务头部的数据
     */
    private static void handleWithOutHeaderResponse(final String response,
                                                    final Class clazz, final IRequestListener callback) {
        Object mResponse = null;
        try {
            if (response.startsWith("{}")) {
                // 没数据
            } else if (response.startsWith("{")) {
                mResponse = JSON.parseObject(response, clazz);
            } else if (response.startsWith("[")) {
                mResponse = JSON.parseArray(response, clazz);
            } else {
            }
            callback.onSuccess(mResponse);
        } catch (Exception e) {
            e.printStackTrace();
            handleResponse(new VolleyError("数据解析异常~"), callback);
        }
    }

    /**
     * 处理有业务头部的数据
     */
    private static void handleWithHeaderResponse(final String response,
                                                 final Class clazz, final IRequestListener callback) {
        GiveHttpHeader mHeader = new GiveHttpHeader();
        JSONObject mResult = null;
        try {
            mHeader = JSON.parseObject(response, GiveHttpHeader.class);
        } catch (Exception e) {
            mHeader.reason = "服务器返回异常";
            callback.onError(mHeader);
            return;
        }
        if (TextUtils.isEmpty(mHeader.error_code)
                || mHeader.error_code.equals("null")) {
            // 暂时不作处理，等后期讨论一下，如何处理
            mHeader.error_code = "没有返回error_code，未知错误";
            // mHeader.reason，数据解析出来是什么就保持为什么
            callback.onError(mHeader);
            return;
        }
        if (TextUtils.isEmpty(mHeader.reason) || mHeader.reason.equals("null")) {
            mHeader.reason = "Api，reason无返回";
        }

        int error_code = Integer.parseInt(mHeader.error_code);
        if (0 == error_code) {
            Object mResponse = null;
            // 这里不需要再做parseObject的异常捕捉和处理，因为在处理header时已经处理，这里不会出现parse失败,只会出现一堆null值
            if (clazz != null) {
                mResult = JSON.parseObject(response);
                String tempResult = getEntityString(mResult
                        .getString("response"));
                if (tempResult.startsWith("{}")) {
                    // 没数据
                } else if (tempResult.startsWith("{")) {
                    mResponse = JSON.parseObject(tempResult, clazz);
                } else if (tempResult.startsWith("[")) {
                    mResponse = JSON.parseArray(tempResult, clazz);
                } else {
                }
            } else {
            }
            callback.onSuccess(mResponse);
        } else if (error_code > 0) {
            callback.onFail(mHeader);
        } else if (error_code < 0) {
            callback.onError(mHeader);
        }
    }

    /**
     * 处理数据
     *
     * @param response http请求返回的json数据
     * @param clazz    需要实例化的业务数据模型，可能为null（不许要解析具体业务模型的时候）
     * @param callback 具体业务的回调方法
     */
    private static void handleResponse(final String response,
                                       final Class clazz, final IRequestListener callback) {

        if (callback == null || clazz == null) {
            // 回调方法或clazz为空，说明该请求不许要做后续处理
            return;
        }

        if (IS_USE_HEAD) {
            handleWithHeaderResponse(response, clazz, callback);
        } else {
            handleWithOutHeaderResponse(response, clazz, callback);
        }

    }

    /**
     * get请求
     *
     * @param context  上下文环境
     * @param url      连接url
     * @param path     连接path
     * @param params   参数
     * @param clazz    需要处理的业务数据模型,如果不需要解析response模型，则传null
     * @param callback 回调方法
     */
    public static void httpGet(String url, String path,
                               Map<String, String> param, final Class clazz, final IRequestListener callback) {
        httpGetAction(url, path, param, clazz, callback);
    }

    private static void httpGetAction(String url, String path, Map<String, String> param, final Class clazz,
                                      final IRequestListener callback) {
        // 拼接链接
        String requestUrl = andGetParams(url + path, param);
        StringRequest strReq = new GiveRequest(GiveHttpSdk.getInstance().getContext(), Method.GET,
                requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                handleResponse(response, clazz, callback);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleResponse(error, callback);
            }
        });
        GiveHttpSdk.getInstance().addToRequestQueue(strReq);
    }

    /**
     * post请求
     *
     * @param context  上下文环境
     * @param url      连接url
     * @param path     连接path
     * @param params   参数
     * @param clazz    需要处理的业务数据模型
     * @param callback 回调方法
     */
    public static void httpPost(final String url,
                                final String path, final Map<String, String> params,
                                final Class clazz, final IRequestListener callback) {
        httpPostAction(url, path, params, clazz, callback);
    }

    private static void httpPostAction(final String url,
                                       final String path, final Map<String, String> params,
                                       final Class clazz, final IRequestListener callback) {
        // 拼接链接
        String requestUrl = url + path;
        StringRequest strReq = new GiveRequest(GiveHttpSdk.getInstance().getContext(), Method.POST,
                requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                handleResponse(response, clazz, callback);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleResponse(error, callback);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };
        GiveHttpSdk.getInstance().addToRequestQueue(strReq);

    }

    /**
     * 上传文件
     *
     * @param context
     * @param url
     * @param path
     * @param params
     * @param clazz
     * @param callback
     * @param file
     */
    public static void httpPostFile(final String url,
                                    final String path, final Map<String, String> params,
                                    final Class clazz, final IRequestListener callback,
                                    final GiveUploadFile file) {
        httpPostFileAction(url, path, params, clazz, callback, file);
    }

    private static void httpPostFileAction(
            final String url, final String path,
            final Map<String, String> params, final Class clazz,
            final IRequestListener callback, final GiveUploadFile uploadFile) {
        // 拼接链接
        String requestUrl = url + path;
        StringRequest strReq = new GiveRequest(GiveHttpSdk.getInstance().getContext(), Method.POST,
                requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                handleResponse(response, clazz, callback);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleResponse(error, callback);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
                // 当复写body时，这个方法就被覆盖掉了,其实参数也就是body的一部分了
            }

            @Override
            public String getBodyContentType() {
                // TODO Auto-generated method stub
                return "multipart/form-data; boundary=" + BOUNDARY;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                // TODO Auto-generated method stub

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                // 参数
                if (params != null) {
                    StringBuffer strBuf = new StringBuffer();
                    Iterator<Map.Entry<String, String>> iter = params
                            .entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, String> entry = iter.next();
                        String inputName = (String) entry.getKey();
                        String inputValue = (String) entry.getValue();
                        if (inputValue == null) {
                            continue;
                        }
                        strBuf.append("\r\n").append("--").append(BOUNDARY)
                                .append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    }
                    try {
                        bos.write(strBuf.toString().getBytes("utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 文件
                StringBuffer sb = new StringBuffer();
                /* 第一行 */
                // `"--" + BOUNDARY + "\r\n"`
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                /* 第二行 */
                // Content-Disposition: form-data; name="参数的名称";
                // filename="上传的文件名" + "\r\n"
                sb.append("Content-Disposition: form-data;");
                sb.append(" name=\"");
                sb.append(uploadFile.getName());
                sb.append("\"");
                sb.append("; filename=\"");
                sb.append(uploadFile.getFileName());
                sb.append("\"");
                sb.append("\r\n");
                /* 第三行 */
                // Content-Type: 文件的 mime 类型 + "\r\n"
                sb.append("Content-Type: ");
                sb.append(uploadFile.getMimeType());
                sb.append("\r\n");
                /* 第四行 */
                // "\r\n"
                sb.append("\r\n");
                try {
                    bos.write(sb.toString().getBytes("utf-8"));
                    /* 第五行 */
                    // 文件的二进制数据 + "\r\n"
                    // bos.write(uploadFile.getByteArray());
                    // bos.write("\r\n".getBytes("utf-8"));

                    DataInputStream in = new DataInputStream(
                            new FileInputStream(uploadFile));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        bos.write(bufferOut, 0, bytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

				/* 结尾行 */
                String endLine = "\r\n" + "--" + BOUNDARY + "--" + "\r\n";
                try {
                    bos.write(endLine.toString().getBytes("utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bos.toByteArray();
            }

        };
        GiveHttpSdk.getInstance().addToRequestQueue(strReq);

    }

    /**
     * @param url    连接
     * @param params 参数
     *
     * @return 返回get的请求连接
     */
    private static String andGetParams(String url, Map<String, String> params) {
        String result = "";
        try {
            result = url + "?" + getParamsString(params);
        } catch (UnsupportedEncodingException e) {
            Log.w("HttpUtil", "error in urlEncoder");
        }
        return result;
    }

    private static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = URLEncoder.encode(entry.getValue(), CHAR_SET);
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }

        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        sb = null;
        return result;
    }

    private static Map<String, String> getEncodedParams(
            Map<String, String> params) throws UnsupportedEncodingException {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = URLEncoder.encode(entry.getValue(), CHAR_SET);
            params.put(entry.getKey(), value);
        }
        return params;
    }

    /**
     * @param data
     *
     * @return 返回json传中的业务数据字符串
     */
    private static String getEntityString(String data) {
        if (TextUtils.isEmpty(data) || data.startsWith("[]")) {
            return "{}";
        } else {
            return data;
        }
    }

}
