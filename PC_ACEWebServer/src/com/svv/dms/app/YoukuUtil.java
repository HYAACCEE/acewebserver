package com.svv.dms.app;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gs.db.dao.DaoUtil;
import com.gs.db.util.DBUtil;
import com.svv.dms.web.service.base.AbstractBean;
import com.svv.dms.web.util.DES;
import com.svv.dms.web.util.JsonUtil;

public class YoukuUtil {
    public static Logger logger = Logger.getLogger(YoukuUtil.class.getSimpleName());
    final public static String STATE_NORNAL = "normal";
    final public static String STATE_CHECKING = "in_review";
	
	public static void main(String[] args){
//		YoukuUtil youku = new YoukuUtil("XMTUxNDU1NzE4MA");
//		System.out.println("IMG:  "+youku.getVideoImg());
//		System.out.println("AVI:  "+youku.getVideoUrl());
//		System.out.println("TITLE:  "+youku.getTitle());
		
		YoukuUtil YoukuUtil = new YoukuUtil();
		YoukuUtil.getAcessToken();
	}
	//https://openapi.youku.com/v2/videos/show_basic.json?client_id=f68332c6b3e560c8&video_id=XMTU0OTE4ODc1Ng==
	private static AccessToken AT;
	final static String URL = "https://openapi.youku.com/v2/videos/show_basic.json?client_id=%s&video_id=%s";
	String title = null;
	String videoImg = null;
	String videoUrl = null;
	String state = null;
	
	public void query(String videoID){
		try{
			String rtn = LWUtil.url(String.format(URL, new Object[]{Constants.YOUKU_APPKEY, videoID}));
			Map map = JsonUtil.getMap4Json(rtn);
			state = (String)map.get("state");
			System.out.println("[YoukuUtil]state="+state);
			title = (String)map.get("title");
			if(state.equals(STATE_NORNAL) || state.equals(STATE_CHECKING))
			    videoImg = (String)map.get("thumbnail");
			else
				videoImg = "http://101.201.80.73:3033/padm/upfile/images/youkunull.jpg";
			videoUrl = "http://dmt.huayinfo.com/videoapp.html?"+videoID; //(String)JsonUtil.getMap4Json(rtn).get("link");
		}catch(Exception e){
			logger.debug("优酷获取视频信息异常：" + e.getMessage());
		}
	}

	public String getVideoImg(){
		return videoImg;
	}
	public String getVideoUrl(){
		return videoUrl;
	}
	public String getTitle(){
		return title;
	}

	private String getAcessToken(){
		if(AT==null) AT = new AccessToken(Constants.YOUKU_APPKEY, Constants.YOUKU_SECRET);
		AT.updateAT();
		return AT.at;
	}
	
	
	class AccessToken {
		public final static String T_MARK_QueryByMark = "select v1, v2 from T_Mark where mark = 'AT%s'";
		public final static String T_MARK_ADD = "insert into T_Mark(mark,v1,v2,istdate,uptdate) values('AT%s','%s','%s',sysdate,sysdate)";
		public final static String T_MARK_EDIT = "update T_Mark set v1='%s',v2='%s',uptdate=sysdate where mark = 'AT%s'";

		//private final static String ACCESS_TOKEN_ASK_URL = "https://openapi.youku.com/v2/oauth2/token?opensysparams={\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"refresh_token\",\"refresh_token\":\"11111112222222\"}";
		private final static String ACCESS_TOKEN_ASK_URL = "https://openapi.youku.com/v2/oauth2/token"; //?&grant_type=refresh_token&refresh_token=
		private final static String ACCESS_TOKEN_ASK_PARAM = "client_id=%s&client_secret=%s";
		private String APPID = "";
		private String AccessToken_AskParam = "";

		private String at = "";
		private long tm = 0;
		private long expires_in = 0;

		public AccessToken(String APPID, String APPSECRET) {
			this.APPID = APPID;
			AccessToken_AskParam = String.format(ACCESS_TOKEN_ASK_PARAM, new Object[] { APPID, APPSECRET });
		}

		@SuppressWarnings("rawtypes")
		public String AT() {
			if (tm == 0) {
				List list = DaoUtil.dbQuery("sql", String.format(T_MARK_QueryByMark, new Object[] { APPID })).getRecordset();
				if (list != null && list.size() > 0) {
					at = DBUtil.getDBString(list.get(0), "v1");
					tm = DBUtil.getDBLong(list.get(0), "v2");
				}
				if (list == null || list.size() == 0) {
					updateAT();
					DaoUtil.dbExe(
							"sql",
							String.format(T_MARK_ADD, new Object[] { APPID, at,
									tm }));
				}
			}
			if (new Date().getTime() - tm > expires_in - 5) {
				updateAT();
				DaoUtil.dbExe("sql", String.format(T_MARK_EDIT, new Object[] {
						at, tm, APPID }));
			}
			return at;
		}

		public void updateAT() {
			String rtn = LWUtil.urlPost(ACCESS_TOKEN_ASK_URL, AccessToken_AskParam);
			at = (String) JsonUtil.getMap4Json(rtn).get("access_token");
			tm = new Date().getTime();
			expires_in = Long.parseLong((String) JsonUtil.getMap4Json(rtn).get("expires_in"));
		}
	}
	 
	//确保params中的参数值进行了UTF-8的URLEncode。参数值为空的参数，也要加入到签名字符串中。
     public static String signApiRequest(Map params, String secret, String signMethod) throws IOException {
	    // 第一步：检查参数是否已经排序
	    String[] keys = (String[]) params.keySet().toArray(new String[0]);
	    Arrays.sort(keys);

	    // 第二步：把所有参数名和参数值串在一起
	    StringBuilder query = new StringBuilder();
	    for (String key : keys) {
	        String value = (String) params.get(key);
	        if (StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(value)) {
	            query.append(key).append(value);
	        }
	    }

	    // 第三步：使用MD5/HMAC加密
	    byte[] bytes;
	    if ("HmacSHA256".equals(signMethod)) {
	        bytes = encryptHMAC(query.toString(), secret);
	        // 第四步：把二进制转化为大写的十六进制
		    return byte2hex(bytes);
	    } else {
	        query.append(secret);
	        //32位小写md5加密
	        return DES.md5(query.toString());
	    }
	}

	public static byte[] encryptHMAC(String data, String secret) throws IOException {
	    byte[] bytes = null;
	    try {
	        SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	        mac.init(secretKey);
	        bytes = mac.doFinal(data.getBytes("UTF-8"));
	    } catch (GeneralSecurityException gse) {
	        throw new IOException(gse.toString());
	    }
	    return bytes;
	}

	public static String byte2hex(byte[] bytes) {
	    StringBuilder sign = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(bytes[i] & 0xFF);
	        if (hex.length() == 1) {
	            sign.append("0");
	        }
	        sign.append(hex.toUpperCase());
	    }
	    return sign.toString();
	}
}
