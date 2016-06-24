package com.fcr.net.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    // 请求cookie 开始**********************************

    public static final Map<String, String> mCookieMap = new HashMap<String, String>();

    /** 客户端软件版本号 */
    public static final String CLIENT_V = "client_v";
    public static final String CLIENT_V_VALUE = "1.00";
    /** 手机型号 */
    public static final String MODEL = "model";
    public static final String MODEL_VALUE = android.os.Build.MODEL;
    /** 安装渠道 */
    public static final String SETUP_SOURCE = "setup_source";

    // 请求cookie 结束**********************************
    
    public static final String HOST_RD = "http://hilo.qa.jumei.com/";
    public static final String HOST_ONLINE = "http://hilo.jumei.com/";
    public static String HOST = HOST_ONLINE;
    /**
     * 是否为线上版本
     */
    public static boolean isOnLine = true;

}
