package com.svv.dms.app;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.gs.db.util.DBUtil;
import com.gs.db.util.DateUtil;
import com.jeesoon.struts.beanaction.ActionContext;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.common.ComBeanIS_Organ;
import com.svv.dms.web.common.LWParam;
import com.svv.dms.web.entity.I_DataParamType;
import com.svv.dms.web.service.base.AbstractBean;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;
import com.svv.dms.web.util.JsonUtil;

public class LWBaseBean extends AbstractBean {
	protected boolean IFACE_DBBUG_FLAG = false;
	protected boolean IFACE_DBLOG_FLAG = true;
	final public static String SP0 = "_SP0;"; //用户JSON替换引号"
	final public static String SP1 = "_SP1;"; //由___改造
	final public static String SP2 = "_SP2;"; //由@@@改造

	protected String DATE_FORMAT = "yyyy-MM-dd";
	protected String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	protected String SYS_AMID = "SYS_AMID";
	protected String SYS_ORGANID;
	protected String auid;
	protected String myUserName;
	protected String mymid;
	protected String myamid;
	protected String M0;
	protected String M3;
	protected int CUR_HOUR;
	protected String dotGroup;
	protected int mmGroup = 0;
	protected int bizStatus = -1;
	protected String userRole = "";
	protected String APPLE_TEST_USER1 = "24"; ///////////////////苹果测试身份1
	protected String APPLE_TEST_USER2 = "43"; ///////////////////苹果测试身份2
	private String user = ""; //FOR LWResult
	
	protected boolean isWeb(){
		return M0.equals("H5WEB");
	}
	protected boolean isAndroid(){
		return M0.equals("MMB") || M0.equals("MMS");
	}
	protected boolean isIos(){
		return M0.equals("IMMB") || M0.equals("IMMS");
	}
	protected boolean isWW(){
		return M0.equals("WW");
	}
	protected boolean isMMGroupUser1(){
		return M0.indexOf("MMB")>=0;
	}
	protected boolean isMMGroupUser2(){
		return M0.indexOf("MMS")>=0;
	}
	protected boolean isMMGroupUser3(){
		return M0.indexOf("MMM")>=0;
	}
	
	protected boolean TIME_ACCESS(int accessMarkid){
		//=============1:00-6:00 禁止买家下单============
//		if(accessMarkid==ACCESS_MARKID_ORDER_COMMIT && CUR_HOUR >=1 && CUR_HOUR <3){
//			logger.debug("系统维护时间1:00-6:00禁止下单.");
//			this.setMessage(true, new LWResult(LWResult.SYSALARM, "系统维护时间1:00-6:00禁止下单.").toDESString());
//			return false;
//		}

//		//=============11点之后禁止退单============
//		if(accessMarkid==ACCESS_MARKID_ORDER_REBACK && CUR_HOUR >= 18){
//			logger.debug("每日18点之后禁止退单.");
//			this.setMessage(true, new LWResult(LWResult.SYSALARM, "每日18点之后禁止退单.").toDESString());
//			return false;
//		}
		
		//=============20点之后禁止改价============
//		if(accessMarkid==ACCESS_MARKID_GOODSPRICE_UPDATE && CUR_HOUR >= 20){
//			logger.debug("每日20点之后禁止改价.");
//			this.setMessage(true, new LWResult(LWResult.SYSALARM, "每日20点之后禁止改价.").toDESString());
//			return false;
//		}

        return true;
	}

	protected boolean ACCESS(){
		return ACCESS(true, false, false);
	}
	
	protected boolean ACCESS(boolean checkFlag){
		return ACCESS(checkFlag, false, false);
	}
	
	protected boolean ACCESS(boolean checkFlag, boolean renzhengFlag){
		return ACCESS(checkFlag, renzhengFlag, false);
	}
	
	protected boolean ACCESS(boolean checkFlag, boolean renzhengFlag, boolean uploadFlag){
		try{
			if("SYS_AMID".equals(SYS_AMID)){
				com.svv.dms.web.Constants.WEB_DOMAIN = this.getRequest().getServerName().substring(0, this.getRequest().getServerName().indexOf("."));
				SYS_AMID = String.valueOf(ComBeanIS_Organ.getAMIDByDomain(com.svv.dms.web.Constants.WEB_DOMAIN));
				SYS_ORGANID = String.valueOf(ComBeanIS_Organ.getIDByDomain(com.svv.dms.web.Constants.WEB_DOMAIN));
			}
			
	  	    if (com.svv.dms.web.Constants.WEB_URL.length()==0) {
	            String url = getRequest().getRequestURI();
	    		logger.debug("[ACCESS] 000 getRequestURI="+url);
	            url = "http://"+getRequest().getLocalAddr()+":"+getRequest().getLocalPort()+url;
	        	int slash1 = url.lastIndexOf("/");
	            logger.debug("[ACCESS] 000 url="+url);
	            if(com.svv.dms.web.Constants.WEB_URL.length()==0){
	            	com.svv.dms.web.Constants.WEB_URL = url.substring(0, slash1+1);
	                logger.debug("[ACCESS] 111 Constants.WEB_URL="+com.svv.dms.web.Constants.WEB_URL);
	            }
            }
	  	    if(com.svv.dms.web.Constants.WEB_URL.indexOf("120.27.249.104:8099") >= 0){
	  	    	ACE.ONLINE = true;
	  	    }
	  	    if(com.svv.dms.web.Constants.WEB_URL.indexOf("118.178.126.204:8088") >= 0){
	  	    	ACE.ONLINE = true;
	  	    	ACE.ADMIN_PHONE = "13693301331";
	  	    }
	  	    
			if(!uploadFlag){
				this.readStreamParam(IFACE_DBLOG_FLAG);
			}
			logger.debug("[ACCESS] 222");

			auid = this.getParameter("a0");
			mymid = this.getParameter("mid", "");
			myamid = this.getParameter("amid", "");
			M0 = this.getParameter("M0", "");  //用户身份
			M3 = this.getParameter("M3", "");  //位置
			String M1 = this.getParameter("M1", ""); //设备
			long M2 = this.getParameter("M2", -1L); //最后登录时间
			long M9 = this.getParameter("M9", -1L); //当前请求时间

			if(uploadFlag){
	            String M = dES.decrypt(this.getParameter("M"));
				logger.debug("M: "+M);
				if(M!=null && M.length()>3){
					try{
						Map<String, String> map = JsonUtil.getMap4Json(M);
						M0 = map.get("M0");  //用户身份
						M3 = map.get("M3");  //位置
						M1 = map.get("M1"); //手机设备
						M2 = Long.parseLong(map.get("M2")); //最后登录时间
						M9 = Long.parseLong(map.get("M9")); //当前请求时间
					}catch(Exception e){
						logger.debug("JsonUtil Error: " + e.getMessage());
					}
				}
			}
			
			logger.debug("[ACCESS] 444 auid="+auid);
			Object loginUser = null;
			if(auid.length()>32){
				auid = auid.substring(0, auid.length()-13);
				M2 = Long.parseLong(auid.substring(auid.length()-13));
				logger.debug("[ACESS] 444 M2=" + M2);
	
				this.systemuser.setUserName(ComBeanLoginUser.getUserName(auid));
				this.systemuser.setUserDescName(String.format("[%s-%s-%s-%s]", new Object[]{M0, mymid, auid, ComBeanLoginUser.getUserName(auid)}));


				List<Object> userStatusList = this.dbQuery(String.format(YSSQL.LWMMUser_QueryByAUID_AMID, new Object[]{auid, myamid}));
				if(userStatusList!=null && userStatusList.size()>0){
					loginUser = userStatusList.get(0);
					String noticeFlag = getDBString(loginUser, "noticeFlag");
					user = String.format("{\"NICKNAME\":\"%s\",\"POWER\":\"%s\",\"FACE\":\"%s\",\"NOTICE_TOP\":\"%s\",\"NOTICE_COLLECT\":\"%s\"}", new Object[]{getDBString(loginUser, "nickName"), getDBInt(loginUser, "bizStatus")==203008?1:0, getDBString(loginUser, "face"), noticeFlag.substring(0, 1), noticeFlag.substring(1, 2)});
				}
			}
			/*************************************** 以下开始校验  *************************************/
			if( ((new Date()).getTime() - M9 > 1200000)){ //120秒
				logger.debug("非法请求！This is not correct request. M9:"+M9+"("+DateUtil.parseDate(M9)+") TIME:"+(new Date()).getTime());
				APPRETURN(new LWResult(2, "非法请求！"));
				return false; 
			}
			if(!checkFlag){
				logger.debug("ACCESS SUCCESS(0)");
				return true;
			}
			///////////if(!auid.equals("160321093240000010165026971238")){APPRETURN(new LWResult(2, "请更新版本！"+auid)); return false;}
			return this.ACCESS_CHECK(renzhengFlag, loginUser, auid, mymid, myamid, M0);

		}catch(Exception e){
			APPRETURN(new LWResult(LWResult.EXCEPTION, "非法请求！"));
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean ACCESS_CHECK(boolean renzhengFlag, Object loginUser, String auid, String mid, String amid, String M0){
		this.systemuser.setUserName(ComBeanLoginUser.getUserName(auid));
		this.systemuser.setUserDescName(String.format("[%s-%s-%s-%s]", new Object[]{M0, mymid, auid, ComBeanLoginUser.getUserName(auid)}));
		this.systemuserloc = M3;
		CUR_HOUR = Integer.parseInt(HIUtil.getCurrentDate("HH"));
		
		int flag = -1;
		if(isAndroid() || isIos() || isWW() || isWeb()){ //用户
			if(auid.length()<20){
				logger.debug("非法用户！This is not system request.auid:"+auid+" amid:"+amid);
				APPRETURN(new LWResult(LWResult.ERRLOGIN, "请重新登录"));
				return false;
			}
			
			if(loginUser==null){
				List userStatusList = this.dbQuery(String.format(YSSQL.LWMMUser_QueryByAUID_AMID, new Object[]{auid, amid}));
				if(userStatusList!=null && userStatusList.size()>0){
					loginUser = userStatusList.get(0);
				}
			}
			if(loginUser!=null){
				myUserName = getDBString(loginUser, "userName");
				int mmGroup_ = getDBInt(loginUser, "mmGroup");
				///////////////////////////////////////////mmGroup = Integer.parseInt(mmGroup_);

				bizStatus = getDBInt(loginUser, "bizStatus");
				userRole = getDBString(loginUser, "userRole");
				if(mmGroup==mmGroup_ && DBUtil.getDBInt(loginUser, "userStatus")>0){
					
				    if(!renzhengFlag){ //不检查认证状态
				    	if(bizStatus>=203001 || bizStatus>=0){
					    	flag = 1;
					    }else{
					    	flag = 4;
					    }
				    }else if(renzhengFlag){ //检查是否认证
				    	if(bizStatus==203008){
				    		flag = 1;
					    }else if(bizStatus>=203000){
					    	flag = 2;
					    }else{
					    	flag = 4;
					    }
				    }
				}else{
					flag = 4;
					logger.debug("用户状态("+mmGroup_+")("+DBUtil.getDBInt(loginUser, "userStatus")+")！This is not system request.auid:"+auid+" amid:"+amid+" bizStatus:"+bizStatus);
				}
			}
			
		}

		if(flag == -1){
			logger.debug("非法登录！This is not system command.Please try to contact service.");
			APPRETURN(new LWResult(LWResult.ERRLOGIN, "非法登录"));
			return false;

		}else if(flag == 2) {
			logger.debug("您的账户还未通过审核");
			APPRETURN(new LWResult(2, "您的账户还未通过审核！"));
			return false;
		}else if(flag == 4) {
			logger.debug("用户状态不能执行此操作！This is not system command.Please try to contact service.");
			APPRETURN(new LWResult(2, "用户状态不能执行此操作！"));
			return false;
		}else{
			logger.debug("ACCESS SUCCESS");
		}
		return true;
	}
	//是否已认证用户
	protected boolean isCheckedUser(){
		return bizStatus==ACE.USER_CHECK_STATUS_CHECKED;
	}
	
	protected String getDataParamTypeJson(Integer parentID, String memoFlag, boolean nullFlag, String nullStr, String picPath){
		StringBuilder s = new StringBuilder("");
		TreeMap<Integer, I_DataParamType> typeMap = ComBeanI_DataParamType.getMap(parentID);
		s = new StringBuilder("[");
		if(typeMap!=null && typeMap.size()>0){
			if(nullFlag){
				s.append("{\"typeID\":\"\"")
				.append(",\"typeName\":\"").append(nullStr).append("\"")
				.append(",\"typePic\":\"\"")
				.append("},");
			}
			Set<Integer> keys = typeMap.keySet();
			for(Integer key: keys){
				I_DataParamType o = typeMap.get(key);
				if(memoFlag.length()>0 && !o.getMemo().equals(memoFlag)) continue;
				s.append("{\"typeID\":\"").append(o.getParamClassID()).append("\"")
				.append(",\"typeName\":\"").append(o.getClassName()).append("\"")
				.append(",\"typePic\":\"").append(picPath.length()==0?"":(com.svv.dms.web.Constants.WEB_URL+"upfile/"+picPath+"/"+o.getParamClassID()+".png")).append("\"")
				.append("},");
			}
			s.deleteCharAt(s.length()-1);
		}
		s.append("]");
		logger.debug(s.toString());
		return s.toString();
	}
	
	protected String APPRETURN(LWResult result){
		result.setUser(user);
		
        if(getParameter("callback_func").length()>0){
        	this.setMessage(true, "JSONP:"+getParameter("callback_func")+"({\"result\":\""+result.toString()+"\"})");
        	return BConstants.MESSAGE;
        }else if(getParameter("json", 0)==1){
        	this.setMessage(true, "{\"result\":\""+result.toString()+"\"}");
        	return BConstants.MESSAGE;
        }else{		
			this.setMessage(true, result.toDESString());
			if(IFACE_DBLOG_FLAG){
				HttpServletRequest req = ActionContext.getActionContext().getRequest();
	            String url = req.getRequestURI();
            	int slash1 = url.lastIndexOf("/") + 1;
                int slash2 = url.lastIndexOf(".");
                String path = url.substring(slash1, slash2)+".y?cmd="+cmd+"返回结果===";
				logger(ComBeanLogType.TYPE_EDIT, path, new Object[]{result.toString()});
			}
			logger.debug(this.log_cmdstr + "=====================================" + result.getInfo());//////////////////////////日志
			return BConstants.MESSAGE;
        }
	}
	
	protected String[] F_formParam(){
		String formParam = this.getParameter("formParam").replaceAll("\\r", "").replaceAll("\\n", "\\\\n").replaceAll("'", "''");////.replaceAll("'", "&#39;");
		return HIUtil.split(formParam, Constants.SPLITER);
	}
	protected String[] F_formContent(){
		String formContent = this.getParameter("formContent").replaceAll("\\r", "").replaceAll("\\n", "\\\\n").replaceAll("'", "''");////.replaceAll("'", "&#39;");
		return HIUtil.split(formContent, Constants.SPLITER);
	}

	//返回到MESSAGE，上传图片url
	protected String F_UploadFace(String subpath, int width, int height){
		logger.debug("[LWBaseBean.APPUploadPic]=======================================");
		try{
			logger.debug("[LWBaseBean.APPUploadPic] [上传1个FACE图片]");
			LWResult tmp = F_UploadPic("file1", subpath+"/"+HIUtil.getCurrentDate("yyyyMM"), UGID.createUGID(), 2048*1024, false, false, false, true, width, height);
			this.setMessage(true, tmp.getInfo());
		}catch(Exception e){
			e.printStackTrace();
		}
		return BConstants.MESSAGE;
	}
	
	//返回到MESSAGE，多个上传图片url
	protected String F_UploadPic_Multi(String subpath){
		logger.debug("[LWBaseBean.APPUploadPic]=======================================");
		try{
			int N = this.getParameter("N", 1);
			StringBuilder s = new StringBuilder();
			for(int i=0; i<N; i++){
				logger.debug("[LWBaseBean.F_UploadPic_Multi] [上传共"+N+"个图片] i="+i);
				LWResult tmp = F_UploadPic("file"+(i+1), subpath+"/"+HIUtil.getCurrentDate("yyyyMM"), UGID.createUGID(), 2048*1024, false, false, false, false, -1, -1);
			    s.append(tmp.getInfo()).append(Constants.SPLITER);
			}
			s.deleteCharAt(s.length()-1);
			this.setMessage(true, s.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return BConstants.MESSAGE;
	}
	
	//返回到MESSAGE，多个上传文件url
	protected String F_UploadFile_Multi(String subpath){
		logger.debug("[LWBaseBean.APPUploadFile]=======================================");
		try{
			int N = this.getParameter("N", 1);
			StringBuilder s = new StringBuilder();
			for(int i=0; i<N; i++){
				logger.debug("[LWBaseBean.F_UploadFile_Multi] [上传共"+N+"个文件] i="+i);
				LWResult tmp = F_UploadFile("file"+(i+1), subpath+"/"+HIUtil.getCurrentDate("yyyyMM"), UGID.createUGID(), 2048*1024, false, false);
			    s.append(tmp.isSuccess()?"":"222").append(tmp.getInfo()).append(Constants.SPLITER);
			}
			s.deleteCharAt(s.length()-1);
			this.setMessage(true, s.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return BConstants.MESSAGE;
	}
	
	protected LWResult F_UploadPic(String fileFormName, String path, String fileID, int size, boolean desFile, boolean desFileName, boolean smallPic, boolean compressPic, int compressWidth, int compressHeight){
		LWResult result = null;
		try{
			logger.debug("[LWBaseBean.F_UploadPic] file="+this.getParameter(fileFormName));
	    	String fileName = this.uploadPic(fileFormName, this.getParameter("savePath", path), fileID, size, desFile, desFileName, smallPic, compressPic, compressWidth, compressHeight);
	    	//logger.debug(fileName);
	    	if(fileName!=null){
				result = new LWResult(LWResult.SUCCESS, fileName);
			}else{
				result = new LWResult(LWResult.FAILURE, "图片无法读取");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return result;
	}
	protected LWResult F_UploadFile(String name, String path, String fileid, int size, boolean desFile, boolean desFileName){
		LWResult result = null;
		try{
			logger.debug("[LWBaseBean.F_UploadFile] file="+this.getParameter(name));
	    	String fileName = this.uploadFile(name, this.getParameter("savePath", path), fileid, size, false, desFile, desFileName);
	    	if(fileName!=null){
				result = new LWResult(LWResult.SUCCESS, fileName);
			}else{
				result = new LWResult(LWResult.FAILURE, this.getMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return result;
	}
	//mode=1第二位为字母，mode=2第二位为数字
	protected static String RandomCode(int mode, int len){
	    //生成6位邀请码
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String code = "";
	    for(int i=0; i<len; i++){
	    	if(i==1){
	    		if(mode==1) code += chars.charAt((int)(Math.random() * 26));
	    		if(mode==2) code += new Random().nextInt(9);
	    	}else{
		    	if(new Random().nextInt(2)==1){
		    		code += chars.charAt((int)(Math.random() * 26));
		    	}else{
			    	code += new Random().nextInt(9);
		    	}
	    	}
	    }
	    return code;
	}
	
	protected boolean isC(){
		return mmGroup==Integer.parseInt(Constants.USER1_MMGROUP);
	}
	protected boolean isHR(){
		return mmGroup==Integer.parseInt(Constants.USER2_MMGROUP);
	}
	protected boolean isHC(){
		return mmGroup==Integer.parseInt(Constants.USER3_MMGROUP);
	}
	protected boolean isM(){
		return mmGroup==Integer.parseInt(Constants.USER9_MMGROUP);
	}
	protected static String toMMGroupDesc(int mmGroup){
		if(mmGroup==Integer.parseInt(Constants.USER1_MMGROUP)) return Constants.USER1_MMGROUP_DESC;
		if(mmGroup==Integer.parseInt(Constants.USER2_MMGROUP)) return Constants.USER2_MMGROUP_DESC;
		if(mmGroup==Integer.parseInt(Constants.USER3_MMGROUP)) return Constants.USER3_MMGROUP_DESC;
		if(mmGroup==Integer.parseInt(Constants.USER9_MMGROUP)) return Constants.USER9_MMGROUP_DESC;
		return "未知角色";
	}
	protected static String PIC_MAX_SIZE_X(int mmGroup){
		return LWParam.getValue(String.format("USER%s_PIC_MAX_SIZE_X", new Object[]{mmGroup}));
	}
	protected static String PIC_MAX_SIZE_Y(int mmGroup){
		return LWParam.getValue(String.format("USER%s_PIC_MAX_SIZE_Y", new Object[]{mmGroup}));
	}
	protected static String PIC_MAX_NUM(int mmGroup){
		return LWParam.getValue(String.format("USER%s_PIC_MAX_NUM", new Object[]{mmGroup}));
	}
	protected static String AVI_MAX_NUM(int mmGroup){
		return LWParam.getValue(String.format("USER%s_AVI_MAX_NUM", new Object[]{mmGroup}));
	}

	protected static String PIC_MAX_SIZE(int mmGroup){
		return LWParam.getValue(String.format("USER%s_PIC_MAX_SIZE", new Object[]{mmGroup}));
	}
	protected static String AVI_MAX_SIZE(int mmGroup){
		return LWParam.getValue(String.format("USER%s_AVI_MAX_SIZE", new Object[]{mmGroup}));
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3561271519094504585L;

	protected int getDBInt(Object o, String colName){
		return DBUtil.getDBInt(o, colName);
	}
	protected String getDBInt(Object o, String colName, String zeroDef, String pre, String end){
		String tmp = getDBString(o, colName);
		if(zeroDef!=null && (tmp.length()==0 || tmp.equals("0")))
			return zeroDef;
		return pre.concat(tmp).concat(end);
	}
	protected double getDBDouble(Object o, String colName){
		return DBUtil.getDBDouble(o, colName);
	}
	protected long getDBLong(Object o, String colName){
		return DBUtil.getDBLong(o, colName);
	}
	protected String getDBDate(Object o, String colName, String format){
		if(format.indexOf("/")>0){
			return DBUtil.getDBDateStr(o, colName, format.replaceAll("/", "-")).replaceAll("-", "/");
		}
		return DBUtil.getDBDateStr(o, colName, format);
	}
	protected String getDBString(Object o, String colName){
		return getDBString(o, colName, "");
	}
	protected String getDBString_JSON(Object o, String colName){
		return getDBString(o, colName, "", false).replaceAll("\"", SP0); //JSON字符串分隔符"必须转换
	}
	protected String getDBString_JSON(Object o, String colName, boolean isClob){
		return getDBString(o, colName, "", isClob).replaceAll("\"", SP0); //JSON字符串分隔符"必须转换
	}
	protected boolean isEmpty(String value){
		return isEmpty(value, false);		
	}
	protected boolean isEmpty(String value, boolean zeroIgnore){
		String rtn = value;
		if(zeroIgnore && rtn.equals("0")) rtn = null;
		return HIUtil.isEmpty(rtn);		
	}
	protected boolean isEmpty(Object o, String colName){
		return isEmpty(o, colName, false);		
	}
	protected boolean isEmpty(Object o, String colName, boolean zeroIgnore){
		String rtn = null;
		if(zeroIgnore) rtn = getDBInt(o, colName, "", "", "");
		else rtn = getDBString(o, colName);
		return HIUtil.isEmpty(rtn);		
	}
	protected boolean isEmpty_BO(Object o, String colName, Object o2){
		return isEmpty_BO(o, colName, false, o2);		
	}
	protected boolean isEmpty_BO(Object o, String colName, boolean zeroIgnore, Object o2){
		String rtn = null;
		if(zeroIgnore) rtn = getDBInt(o, colName, "", "", "")+(o2==null?"":getDBInt(o2, colName, "", "", ""));
		else rtn = getDBString(o, colName)+(o2==null?"":getDBString(o2, colName));
		return HIUtil.isEmpty(rtn);		
	}
	protected String getDBString_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput){
		return this.getDBString_BO(o, colName, def, pre, exd, o2, spanOrInput, false);
	}
	protected String getDBString_JSON_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput, boolean isClob){
		return this.getDBString_BO(o, colName, def, pre, exd, o2, spanOrInput, false, isClob).replaceAll("\"", SP0); //JSON字符串分隔符"必须转换
	}
	protected String getDBString_JSON_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput){
		return this.getDBString_BO(o, colName, def, pre, exd, o2, spanOrInput, false).replaceAll("\"", SP0); //JSON字符串分隔符"必须转换
	}
	protected String getDBString_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput, boolean htmlEncodeFlag){
		return this.getDBString_BO(o, colName, def, pre, exd, o2, spanOrInput, htmlEncodeFlag, false);
	}
	
	protected String getDBString_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput, boolean htmlEncodeFlag, boolean isClob){
		String value = getDBString(o, colName, def, pre, exd, isClob);
		String rtn = value;
		if(htmlEncodeFlag) rtn = HIUtil.toHtmlStr(rtn);
		if(o2!=null){
			if(o2 instanceof Boolean){
				if((Boolean)o2){
					if(spanOrInput==1) rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{"", rtn});
					if(spanOrInput==2) rtn = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{rtn});
				}
			}else{
				String checkValue = getDBString(o2, colName, def, pre, exd, isClob);
				if(!checkValue.equals(value)){
					if(spanOrInput==1){
						if((isHC() || isM()) && value.length()==0){
						    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN_DEL, new Object[]{checkValue});
						}else{
						    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{checkValue, rtn});
						}
					}
					if(spanOrInput==2) rtn = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{rtn});
				}
			}
		}
		return rtn;
	}
	protected String toBO(String value, String value2, int spanOrInput, boolean htmlEncodeFlag){
		String rtn = value;
		if(htmlEncodeFlag) rtn = HIUtil.toHtmlStr(rtn);
		if(value2!=null){
			String checkValue = value2;
			if(!checkValue.equals(value)){
				if(spanOrInput==1){
					if((isHC() || isM()) && value.length()==0){
					    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN_DEL, new Object[]{checkValue});
					}else{
					    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{checkValue, rtn});
					}
				}
				if(spanOrInput==2) rtn = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{rtn});
			}
		}
		return rtn;
	}
	protected String getDBString(Object o, String colName, String def){
		return this.getDBString(o, colName, def, false);
	}
	protected String getDBString(Object o, String colName, String def, boolean isClob){
		if(isClob) return HIUtil.isEmpty(DBUtil.getDBClob(o, colName), def);
		return HIUtil.isEmpty(DBUtil.getDBString(o, colName), def);
	}
	protected String getDBString(Object o, String colName, String def, String pre, String exd){
		return this.getDBString(o, colName, def, pre, exd, false);
	}
	protected String getDBString(Object o, String colName, String def, String pre, String exd, boolean isClob){
		String rtn = getDBString(o, colName, def, isClob);
		if(rtn.length()>0){
			rtn = pre.concat(rtn).concat(exd);
		}
		return rtn;
	}
	protected String getDBString_JSON(Object o, String colName, String def, String pre, String exd, boolean isClob){
		return this.getDBString(o, colName, def, pre, exd, isClob).replaceAll("\"", SP0); //JSON字符串分隔符"必须转换
	}
	protected String getDBPrice(Object o, String colName){
		return HIUtil.NumFormat(DBUtil.getDBString(o, colName), 2);
	}
	protected String getDBNumString(Object o, String colName, int dot){
		return HIUtil.NumFormat(DBUtil.getDBString(o, colName), dot);
	}
	protected String getDBDataParam(Object o, String colName, String def, String pre, String exd){
		String id = getDBString(o, colName);
		String rtn = id.length()==0 ? def : ComBeanI_DataParamType.getText(Integer.parseInt(id));
		if(rtn.length()>0){
			rtn = pre.concat(rtn).concat(exd);
		}
		return rtn;
	}
	protected String getDBDataParam_BO(Object o, String colName, String def, String pre, String exd, Object o2, int spanOrInput){
		String value = getDBString(o, colName);
		String rtn = value.length()==0 ? def : ComBeanI_DataParamType.getText(Integer.parseInt(value));
		if(rtn.length()>0){
			rtn = pre.concat(rtn).concat(exd);
		}
		if(o2!=null){
			if(o2 instanceof Boolean){
				if((Boolean)o2){
					if(spanOrInput==1) rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{"", rtn});
					if(spanOrInput==2) rtn = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{rtn});
				}
			}else{
				String checkValue = getDBString(o2, colName);
				if(!checkValue.equals(value)){
					try{
						Integer.parseInt(checkValue);
						checkValue = ComBeanI_DataParamType.getText(Integer.parseInt(checkValue));
					}catch(Exception e){
						checkValue = "";
					}
					checkValue = checkValue.length()==0 ? def : checkValue;
					
					if(spanOrInput==1){
						if((isHC() || isM()) && rtn.length()==0){
						    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN_DEL, new Object[]{checkValue});
						}else{
						    rtn = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{checkValue, rtn});
						}
					}
					if(spanOrInput==2) rtn = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{rtn});
				}
			}
		}
		return rtn;
	}
	final static public String ACE_CHECK_CV_COLUMN_SPAN_DEL = "<span class=\"red delete\">%s</span>";
	final static public String ACE_CHECK_CV_COLUMN_SPAN = "<span class=\"red\" title=\"%s\">%s</span>";
	final static public String ACE_CHECK_CV_COLUMN_INPUT = "UUU===%s";
	
	protected void getPageQuery(String sql, int lastDataid, int page, int pageRow){
		
	}
}
