package com.svv.dms.app;

import com.jeesoon.util.logger.Logger;
import com.svv.dms.web.util.DES;

public class LWResult {
    public static Logger logger = Logger.getLogger(LWResult.class.getSimpleName());

	public final static String RESULT_SPLITER = "`";
	public final static int SUCCESS = 1;
	public final static int FAILURE = 2;
	public final static int SYSALARM = 3;
	public final static int EXCEPTION = 4;
	public final static int ERRLOGIN = 5; //错误登录

	private int result = -1;
	private long totalCount = 0;
	private String firstDataid = "";
	private String lastDataid = "";
	private String pageHtml = "";
	private String info = "";
	private String user = "";
	
	public LWResult(int result, String info){
		this.result = result;
		this.info = info;
//		logger.info(toString());
	}
	
	public LWResult(int result, long totalCount, String firstDataid, String lastDataid, String pageHtml, String html){
		this.result = result;
		this.totalCount = totalCount;
		this.firstDataid = firstDataid;
		this.lastDataid = lastDataid;
		this.pageHtml = pageHtml;
		this.info = html;
//		logger.info(toString());
	}
	public boolean isSuccess(){
		return this.result==SUCCESS;
	}
	public int getResult(){
		return this.result;
	}
	public String getInfo(){
		return this.info;
	}
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append(result);
		if(lastDataid.length()>0){
			s.append(RESULT_SPLITER).append(totalCount);
			s.append(RESULT_SPLITER).append(firstDataid);
			s.append(RESULT_SPLITER).append(lastDataid);
			s.append(RESULT_SPLITER).append(pageHtml);
		}
		s.append(RESULT_SPLITER).append(info);
		if(user.length()>0) s.append(RESULT_SPLITER).append("USER="+user);
		return s.toString();
	}
	public String toDESString(){
		StringBuilder s = new StringBuilder();
		s.append(result);
		if(lastDataid.length()>0){
			s.append(RESULT_SPLITER).append(totalCount);
			s.append(RESULT_SPLITER).append(firstDataid);
			s.append(RESULT_SPLITER).append(lastDataid);
			s.append(RESULT_SPLITER).append(pageHtml);
		}
		s.append(RESULT_SPLITER).append(info);
		if(user.length()>0) s.append(RESULT_SPLITER).append("USER="+user);
		return new DES().encrypt(s.toString());
	}
	
	public void setUser(String user){
		this.user = user;
	}

}
