package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanBase;
import com.svv.dms.web.util.HIUtil;

public class ComBeanMMOrgan extends ComBeanBase {
    public final static String FULLNAME = "c2"; //全称
    public final static String SIMPNAME = "simpName"; //简称
    public final static String OTHERNAME = "otherName"; //别名
    public final static String MID = "mid";
    public final static String AMID = "amid";

    protected static HashMap<String, Object> map = null;
    protected static HashMap<String, Object> map2 = null;
    static boolean initFlag = false;

    public static String getFullName(String key) {
    	if(key==null) return "";
        if(!initFlag) load();
        return map.get(key)==null? "":DBUtil.getDBString(map.get(key), FULLNAME);
    }
    public static String getSimpName(String key) {
    	if(key==null) return "";
        if(!initFlag) load();
        return map.get(key)==null? "":DBUtil.getDBString(map.get(key), SIMPNAME);
    }
    public static String getOtherName(String key) {
    	if(key==null) return "";
        if(!initFlag) load();
        return map.get(key)==null? "":DBUtil.getDBString(map.get(key), OTHERNAME);
    }
    public static String getMid(String key) {
    	if(key==null) return "";
        if(!initFlag) load();
        return map.get(key)==null? "":DBUtil.getDBString(map.get(key), MID);
    }
    public static String getAmid(String key) {
    	if(key==null) return "";
        if(!initFlag) load();
        return map.get(key)==null? "":DBUtil.getDBString(map.get(key), AMID);
    }

    public static String getFullNameByAmid(String amid) {
    	if(amid==null) return "";
        if(!initFlag) load();
        return map2.get(amid)==null? "":DBUtil.getDBString(map2.get(amid), FULLNAME);
    }
    public static String getSimpNameByAmid(String amid) {
    	if(amid==null) return "";
        if(!initFlag) load();
        return map2.get(amid)==null? "":DBUtil.getDBString(map2.get(amid), SIMPNAME);
    }
    public static String getOtherNameByAmid(String amid) {
    	if(amid==null) return "";
        if(!initFlag) load();
        return map2.get(amid)==null? "":DBUtil.getDBString(map2.get(amid), OTHERNAME);
    }
    public static String getMidByAmid(String amid) {
    	if(amid==null) return "";
        if(!initFlag) load();
        return map2.get(amid)==null? "":DBUtil.getDBString(map2.get(amid), MID);
    }

	public synchronized static void reload() {
		initFlag = false;
		load();
	}
    @SuppressWarnings("rawtypes")
	private synchronized static void load() {
    	if(!initFlag){
	        map = new HashMap<String, Object>();
	        map2 = new HashMap<String, Object>();
	        List result = HIUtil.dbQuery("select b.c2 key, b.c2, b.dataid mid, b.c1 amid, NVL(a.c2,b.c28) simpName, a.c25 otherName from B_MMOrgan b, A_Organ a where b.c2=a.c5(+)||'['||a.c1(+)||']'"); // and b.c8=130001
	        if (result != null && result.size() > 0) {
	        	String amid;
	            for (Object o : result) {
	                map.put(DBUtil.getDBString(o, "key"), o);
	                
	                amid = DBUtil.getDBString(o, "amid");
	                if(amid.length()>0) map2.put(amid, o);
	            }
	            initFlag = true;
	        }
    	}
    }
}
