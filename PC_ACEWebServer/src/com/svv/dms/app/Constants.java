package com.svv.dms.app;

import com.svv.dms.web.common.LWParam;

public class Constants {
	
	public final static String SPLITER = "`";
	public final static String SPLITER_SP = "^";

	public static String WEBSERVER_URL = LWParam.getValue("WEBSERVER_URL");
	public static String FILESERVER_URL = LWParam.getValue("FILESERVER_URL");
	public static String FILESERVER_PATH = LWParam.getValue("FILESERVER_PATH");
	
	public static String USER1_MMGROUP = LWParam.getValue("USER1_MMGROUP");
	public static String USER1_MMGROUP_DESC = LWParam.getValue("USER1_MMGROUP_DESC");
	public static String USER2_MMGROUP = LWParam.getValue("USER2_MMGROUP");
	public static String USER2_MMGROUP_DESC = LWParam.getValue("USER2_MMGROUP_DESC");
	public static String USER3_MMGROUP = LWParam.getValue("USER3_MMGROUP");
	public static String USER3_MMGROUP_DESC = LWParam.getValue("USER3_MMGROUP_DESC");
	public static String USER9_MMGROUP = LWParam.getValue("USER9_MMGROUP");
	public static String USER9_MMGROUP_DESC = LWParam.getValue("USER9_MMGROUP_DESC");
	public static String LOG_DEBUG_FLAG = LWParam.getValue("LOG_DEBUG_FLAG");
	public static String LOG_UPLOAD_FLAG = LWParam.getValue("LOG_UPLOAD_FLAG");
	public static String QQ_APPID = LWParam.getValue("QQ_APPID");
	public static String QQ_APPKEY = LWParam.getValue("QQ_APPKEY");
	public static String WEIXIN_APPKEY = LWParam.getValue("WEIXIN_APPKEY");
	public static String WEIXIN_SECRET = LWParam.getValue("WEIXIN_SECRET");
	public static String JPUSH_APPKEY = LWParam.getValue("JPUSH_APPKEY");
	public static String JPUSH_SECRET = LWParam.getValue("JPUSH_SECRET");
	public static String YOUKU_APPKEY = LWParam.getValue("YOUKU_APPKEY");
	public static String YOUKU_SECRET = LWParam.getValue("YOUKU_SECRET");
	public static String YOUKU_ACCESS_TOKEN = LWParam.getValue("YOUKU_ACCESS_TOKEN");
	public static String YOUKU_CALLBACK_URL = LWParam.getValue("YOUKU_CALLBACK_URL");
	public static String YOUKU_LOGINNAME = LWParam.getValue("YOUKU_LOGINNAME");
	public static String YOUKU_PASSWORD = LWParam.getValue("YOUKU_PASSWORD");
	public static String HUANXIN_APPKEY = LWParam.getValue("HUANXIN_APPKEY");
	public static String HUANXIN_CLIENTID = LWParam.getValue("HUANXIN_CLIENTID");
	public static String HUANXIN_SECRET = LWParam.getValue("HUANXIN_SECRET");
	
}
