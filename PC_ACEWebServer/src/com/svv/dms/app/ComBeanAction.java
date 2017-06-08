package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanBase;
import com.svv.dms.web.util.HIUtil;

public class ComBeanAction extends ComBeanBase {
    private final static String CODE = "c1";
    private final static String TITLE = "c2";
    private final static String MODE = "c4";
    private final static String TYPE = "c5";
    private final static String OPERATOR = "c6";
    private final static String MEMO_ADMIN = "c7";
    private final static String MEMO_BS = "c8";
    private final static String MEMO_HC = "c9";
    private final static String MEMO_HR = "c10";
    private final static String MEMO_C = "c11";
    private final static String VISIBLE_ADMIN = "c12";
    private final static String VISIBLE_BS = "c13";
    private final static String VISIBLE_HC = "c14";
    private final static String VISIBLE_HR = "c15";
    private final static String VISIBLE_C = "c16";
    private final static String URL = "c18";
    private final static String NOTICEFLAG = "c20";
    private final static String NOTICEFLAG_ADMIN = "c21";
    private final static String NOTICEFLAG_BS = "c23";
    private final static String NOTICEFLAG_HC = "c25";
    private final static String NOTICEFLAG_HR = "c27";
    private final static String NOTICEFLAG_C = "c29";
    private final static String NOTICE_ADMIN = "c22";
    private final static String NOTICE_BS = "c24";
    private final static String NOTICE_HC = "c26";
    private final static String NOTICE_HR = "c28";
    private final static String NOTICE_C = "c30";
    private final static String LIMIT_HOUR = "c31";
    private final static String OVERTIME_PROCESS = "c32";
    private final static String NOTIFY_TYPE = "c33";
    private final static String NOTIFY_PLANTIME_FLAG = "c34";
    private final static String NOTIFY_DELAYFLAG = "c35";
    private final static String NOTIFY_DETAIL = "c36";
    private final static String NOTIFY_DESC = "c37";
    private final static String NOTIFY_STATUS = "c38";
    private final static String NOTIFY_STATUS_DESC = "c39";
    private final static String QUERY_STATUS = "c40";
    private final static String IGNORE_HC_USER = "c41";
    private Object o = null;
    
    protected static HashMap<String, Object> map = null;
    
    public ComBeanAction(String code){
        System.out.println("[ComBeanAction] code=================="+code);
        if(map==null || map.get(code)==null) load();
    	o = map.get(code);
    }

    public String code(){
    	return DBUtil.getDBString(o, CODE);
    }
    public String title(){
    	return DBUtil.getDBString(o, TITLE);
    }
    public String type(){
    	return DBUtil.getDBString(o, TYPE);
    }
    public String mode(){
    	return DBUtil.getDBString(o, MODE);
    }
    public String limitHour(){
    	return DBUtil.getDBString(o, LIMIT_HOUR);
    }
    public String overTimeProcess(){
    	return DBUtil.getDBString(o, OVERTIME_PROCESS);
    }
    public String url(){
    	return DBUtil.getDBString(o, URL);
    }
    public String memoM(){
    	return DBUtil.getDBString(o, MEMO_ADMIN);
    }
    public String memoBS(){
    	return DBUtil.getDBString(o, MEMO_BS);
    }
    public String memoHC(){
    	return DBUtil.getDBString(o, MEMO_HC);
    }
    public String memoHR(){
    	return DBUtil.getDBString(o, MEMO_HR);
    }
    public String memoC(){
    	return DBUtil.getDBString(o, MEMO_C);
    }
    public int visibleM(){
    	return DBUtil.getDBInt(o, VISIBLE_ADMIN);
    }
    public int visibleBS(){
    	return DBUtil.getDBInt(o, VISIBLE_BS);
    }
    public int visibleHC(){
    	return DBUtil.getDBInt(o, VISIBLE_HC);
    }
    public int visibleHR(){
    	return DBUtil.getDBInt(o, VISIBLE_HR);
    }
    public int visibleC(){
    	return DBUtil.getDBInt(o, VISIBLE_C);
    }
    public String noticeM(){
    	return DBUtil.getDBString(o, NOTICE_ADMIN);
    }
    public String noticeBS(){
    	return DBUtil.getDBString(o, NOTICE_BS);
    }
    public String noticeHC(){
    	return DBUtil.getDBString(o, NOTICE_HC);
    }
    public String noticeHR(){
    	return DBUtil.getDBString(o, NOTICE_HR);
    }
    public String noticeC(){
    	return DBUtil.getDBString(o, NOTICE_C);
    }
    public boolean noticeFlag(){
    	return DBUtil.getDBInt(o, NOTICEFLAG)==1;
    }
    public boolean noticeFlagM(){
    	return DBUtil.getDBInt(o, NOTICEFLAG_ADMIN)==1;
    }
    public boolean noticeFlagBS(){
    	return DBUtil.getDBInt(o, NOTICEFLAG_BS)==1;
    }
    public boolean noticeFlagHC(){
    	return DBUtil.getDBInt(o, NOTICEFLAG_HC)==1;
    }
    public boolean noticeFlagHR(){
    	return DBUtil.getDBInt(o, NOTICEFLAG_HR)==1;
    }
    public boolean noticeFlagC(){
    	return DBUtil.getDBInt(o, NOTICEFLAG_C)==1;
    }
    public String notifyType(){
    	return DBUtil.getDBString(o, NOTIFY_TYPE);
    }
    public boolean notify_planTimeFlag(){
    	return DBUtil.getDBInt(o, NOTIFY_PLANTIME_FLAG)==1;
    }
    public String notifyDetail(){
    	return DBUtil.getDBString(o, NOTIFY_DETAIL);
    }
    public String notifyDesc(){
    	return DBUtil.getDBString(o, NOTIFY_DESC);
    }
    public String notifyStatus(){
    	return DBUtil.getDBString(o, NOTIFY_STATUS);
    }
    public String notifyStatusDesc(){
    	return DBUtil.getDBString(o, NOTIFY_STATUS_DESC);
    }
    public String delayFlag(){
    	return DBUtil.getDBString(o, NOTIFY_DELAYFLAG);
    }
    public String queryStatus(){
    	return DBUtil.getDBString(o, QUERY_STATUS);
    }
    public boolean ignoreHCUserFlag(){
    	return DBUtil.getDBString(o, IGNORE_HC_USER).equals("1");
    }

    public static String getValue(String code, String col) {
        if(map==null || map.get(code)==null) load();
        return map.get(code)==null? "":DBUtil.getDBString(map.get(code), col);
    }
    public static Object getAction(String code) {
        if(map==null || map.get(code)==null) load();
        return map.get(code);
    }

    @SuppressWarnings("rawtypes")
	public static void load() {
        map = new HashMap<String, Object>();
        List result = HIUtil.dbQuery("select * from A_Action where c3=130001");
        if (result != null && result.size() > 0) {
            for (Object o : result) {
                map.put(DBUtil.getDBString(o, "c1"), o);
            }
        }
    }
}
