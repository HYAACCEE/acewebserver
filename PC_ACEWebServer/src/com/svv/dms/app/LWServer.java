package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.UGID;
import com.svv.dms.web.util.HIUtil;

public class LWServer {
    protected static HashMap<String, String> map = null;
    protected static HashMap<String, String> map2 = null;
    protected static HashMap<String, String> map3 = null;

    public static String getUrl(String key) {
        if(map==null) load();
        return map.get(key)==null? "":map.get(key);
    }
    public static String getRealPath(String key) {
        if(map2==null) load();
        return map2.get(key)==null? "":map2.get(key);
    }
    public static String getRealPathByUrl(String key) {
        if(map3==null) load();
        return map3.get(key)==null? "":map3.get(key);
    }
    
    public static String getUrlByAUID(String auid) {
        return getUrl(UGID.getServerIndex(auid));
    }
    
    @SuppressWarnings("rawtypes")
	private static void load() {
    	map = new HashMap<String, String>();
    	map2 = new HashMap<String, String>();
    	map3 = new HashMap<String, String>();
        List list = HIUtil.dbQuery(YSSQL.LWServer_Query);
        if (list != null && list.size() > 0) {
            for (Object o : list) {
                map.put(DBUtil.getDBString(o, "serverID"), DBUtil.getDBString(o, "url"));
                map2.put(DBUtil.getDBString(o, "serverID"), DBUtil.getDBString(o, "realPath"));
                map3.put(DBUtil.getDBString(o, "url"), DBUtil.getDBString(o, "realPath"));
            }
        }
    }
   
}
