package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanBase;
import com.svv.dms.web.util.HIUtil;

public class ComBeanAPPModule extends ComBeanBase {    
    protected static HashMap<String, String> map = null;
    protected static HashMap<String, String> map2 = null;
    static{
    	load();
    }

    public static String getHtml(String moduleID) {
    	System.out.println("[ComBeanAPPModule] 模块："+moduleID);
        if(map==null) load();
        String[] ss = moduleID.split(",");
        StringBuilder rtn = new StringBuilder();
        String html;
        for(String s: ss){
        	System.out.println("[ComBeanAPPModule] sss："+s +"   ss.size="+ss.length);
        	html = map.get(s);
            if(HIUtil.isEmpty(html)){
            	System.out.println("[ComBeanAPPModule] ---------------------- ERROR 没有定义模块："+s);
            	return "";
            }
        	rtn.append(html);
        }
        return rtn.toString();
    }
    public static String getNextModule(String moduleID) {
        if(map2==null) load();
        String[] ss = moduleID.split(",");
        StringBuilder rtn = new StringBuilder();
        String html;
        for(String s: ss){
        	html = map2.get(s);
            if(HIUtil.isEmpty(html)){
            	System.out.println("[ComBeanAPPModule] ---------------------- ERROR 没有定义模块2："+moduleID);
            	return "";
            }
        	rtn.append(html);
        }
    	System.out.println("[ComBeanAPPModule] 模块2："+moduleID);
        return rtn.toString();
    }

    @SuppressWarnings("rawtypes")
	public static void load() {
        map = new HashMap<String, String>();
        map2 = new HashMap<String, String>();
        List result = HIUtil.dbQuery("select a.c1||decode(b.c2,null,'','-'||b.c2) moduleID, b.c5 html, b.c6 html2, b.c8 nextModule from IS_AppModule a, IS_AppModuleDetail b where a.dataid=b.parentDataid and a.c6=130001 and b.c9=130001 order by moduleID");
        if (result != null && result.size() > 0) {
            for (Object o : result) {
//            	System.out.println(DBUtil.getDBString(o, "moduleID")+ "     " + (DBUtil.getDBString(o, "html").length()>0?"1111":"---")+"   "+(DBUtil.getDBString(o, "html2").length()>0?"1111":"---"));
                map.put(DBUtil.getDBString(o, "moduleID"), (DBUtil.getDBString(o, "html")+DBUtil.getDBString(o, "html2")).replaceAll("  ", "").replaceAll("\\\\n", ""));
                map2.put(DBUtil.getDBString(o, "moduleID"), DBUtil.getDBString(o, "nextModule").replaceAll("  ", "").replaceAll("\\\n", ""));
            }
        }
    }
}
