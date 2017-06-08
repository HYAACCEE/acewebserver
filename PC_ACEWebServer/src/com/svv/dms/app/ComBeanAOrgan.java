package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanBase;
import com.svv.dms.web.util.HIUtil;

public class ComBeanAOrgan extends ComBeanBase {
    public final static String FULLNAME = "c1"; //全称
    public final static String SIMPNAME = "c2"; //简称
    public final static String ENNAME = "c3"; //英文名称
    public final static String BRAND = "c5"; //品牌
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
    public static String getMidByAmid(String amid) {
    	if(amid==null) return "";
        if(!initFlag) load();
        return map2.get(amid)==null? "":DBUtil.getDBString(map2.get(amid), MID);
    }

    @SuppressWarnings("rawtypes")
	public synchronized static void load() {
    	if(!initFlag){
	        map = new HashMap<String, Object>();
	        map2 = new HashMap<String, Object>();
	        List result = HIUtil.dbQuery("select distinct a.c5||'['||a.c1||']' key, a.c1,a.c2,a.c3, b.dataid mid, b.c1 amid from A_Organ a, B_MMOrgan b where a.c5||'['||a.c1||']'=b.c2(+) and a.c26=130001");
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
