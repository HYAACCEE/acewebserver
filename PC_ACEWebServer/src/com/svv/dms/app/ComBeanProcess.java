package com.svv.dms.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanBase;
import com.svv.dms.web.util.HIUtil;

public class ComBeanProcess extends ComBeanBase {
    private final static String CODE = "c1";
    private final static String NAME = "c2";
    private final static String TYPE = "c3";
    private final static String ACTION = "c4";
    private final static String CONDITION_ST_STATUS = "c7";
    private final static String CONDITION_STCHECK_STATUS = "c8";
    private final static String CONDITION_C_STATUS = "c9";
    private final static String CONDITION_P_STATUS = "c10";
    private final static String CONDITION_P_CLOSE_STATUS = "c11";
    private final static String CONDITION_BILL_STATUS = "c12";
    private final static String CONDITION_COLLECT_STATUS = "c13";
    private final static String CONDITION_ACTIVE_STATUS = "c14";
    private final static String CONDITION_P_INNER_STATUS = "c15";
    private final static String CONDITION_CV_STATUS = "c16";
    private final static String NEXT_ST_STATUS = "c17";
    private final static String NEXT_STCHECK_STATUS = "c18";
    private final static String NEXT_C_STATUS = "c19";
    private final static String NEXT_P_STATUS = "c20";
    private final static String NEXT_P_CLOSE_STATUS = "c21";
    private final static String NEXT_BILL_STATUS = "c22";
    private final static String NEXT_COLLECT_STATUS = "c23";
    private final static String NEXT_ACTIVE_STATUS = "c24";
    private final static String NEXT_P_INNER_STATUS = "c25";
    private final static String NEXT_CV_STATUS = "c26";
    private final static String FORMAT_TITLE = "c27";
    private final static String FORMAT_DESC = "c28";
    private final static String FORMAT_CONTENT = "c29";
    private final static String DESC = "c30";
    private final static String ADD_IV_FLAG = "c31";
    private final static String ISIV = "c32";
    private final static String ADD_HIS_FLAG = "c33";
    
    private Object o;
    
    public ComBeanProcess(String code){
        if(map==null || map.get(code)==null) load();
    	o = map.get(code);
    }
    public String code(){
    	return DBUtil.getDBString(o, CODE);
    }
    public String name(){
    	return DBUtil.getDBString(o, NAME);
    }
    public String type(){
    	return DBUtil.getDBString(o, TYPE);
    }
    public ComBeanAction action(){
    	String action = DBUtil.getDBString(o, ACTION);
    	if(HIUtil.isEmpty(action)) return null;
    	return new ComBeanAction(action);
    }
    public String desc(){
    	return DBUtil.getDBString(o, DESC);
    }
    public String FormatTitle(){
    	return DBUtil.getDBString(o, FORMAT_TITLE);
    }
    public String FormatDesc(){
    	return DBUtil.getDBString(o, FORMAT_DESC);
    }
    public String FormatContent(){
    	return DBUtil.getDBString(o, FORMAT_CONTENT);
    }
    public String condition_stStatus(){
    	return DBUtil.getDBString(o, CONDITION_ST_STATUS);
    }
    public String condition_stcheckStatus(){
    	return DBUtil.getDBString(o, CONDITION_STCHECK_STATUS);
    }
    public String condition_cStatus(){
    	return DBUtil.getDBString(o, CONDITION_C_STATUS);
    }
    public String condition_pStatus(){
    	return DBUtil.getDBString(o, CONDITION_P_STATUS);
    }
    public String condition_pcloseStatus(){
    	return DBUtil.getDBString(o, CONDITION_P_CLOSE_STATUS);
    }
    public String condition_billStatus(){
    	return DBUtil.getDBString(o, CONDITION_BILL_STATUS);
    }
    public String condition_collectStatus(){
    	return DBUtil.getDBString(o, CONDITION_COLLECT_STATUS);
    }
    public String condition_processInnerStatus(){
    	return DBUtil.getDBString(o, CONDITION_P_INNER_STATUS);
    }
    public String condition_cvStatus(){
    	return DBUtil.getDBString(o, CONDITION_CV_STATUS);
    }
    public String condition_activeStatus(){
    	return DBUtil.getDBString(o, CONDITION_ACTIVE_STATUS);
    }
    public String next_stStatus(){
    	return DBUtil.getDBString(o, NEXT_ST_STATUS);
    }
    public String next_stcheckStatus(){
    	return DBUtil.getDBString(o, NEXT_STCHECK_STATUS);
    }
    public String next_cStatus(){
    	return DBUtil.getDBString(o, NEXT_C_STATUS);
    }
    public String next_pStatus(){
    	return DBUtil.getDBString(o, NEXT_P_STATUS);
    }
    public String next_pcloseStatus(){
    	return DBUtil.getDBString(o, NEXT_P_CLOSE_STATUS);
    }
    public String next_billStatus(){
    	return DBUtil.getDBString(o, NEXT_BILL_STATUS);
    }
    public String next_collectStatus(){
    	return DBUtil.getDBString(o, NEXT_COLLECT_STATUS);
    }
    public String next_activeStatus(){
    	return DBUtil.getDBString(o, NEXT_ACTIVE_STATUS);
    }
    public String next_processInnerStatus(){
    	return DBUtil.getDBString(o, NEXT_P_INNER_STATUS);
    }
    public String next_cvStatus(){
    	return DBUtil.getDBString(o, NEXT_CV_STATUS);
    }
    public boolean addIVTimesFlag(){
    	return DBUtil.getDBString(o, ADD_IV_FLAG).equals("1");
    }
    public boolean addHisFlag(){
    	return DBUtil.getDBString(o, ADD_HIS_FLAG).equals("1");
    }
    public boolean updateIVFlag(){
    	return DBUtil.getDBString(o, ISIV).equals("2");
    }
    public boolean ivOverFlag(){
    	return DBUtil.getDBString(o, ISIV).equals("9");
    }
    public boolean addIVFlag(){
    	return DBUtil.getDBString(o, ISIV).equals("1");
    }

    protected static HashMap<String, Object> map = null;
    protected static HashMap<String, List<Object>> mapChildren = null;

    public static String getValue(String code, String col) {
        if(map==null || map.get(code)==null) load();
        return map.get(code)==null? "":DBUtil.getDBString(map.get(code), col);
    }
    
    public static List<Object> getNextProcessList(String curProcessStatus){
        if(mapChildren==null) load();
        return mapChildren.get(curProcessStatus);
    }

    @SuppressWarnings("rawtypes")
	public static void load() {
        map = new HashMap<String, Object>();
        mapChildren = new HashMap<String, List<Object>>();
        List result = HIUtil.dbQuery("select * from A_Process where c5=130001");
        if (result != null && result.size() > 0) {
        	String conditionProcessStatus;
        	String[] ss;
        	List<Object> tmp;
            for (Object o : result) {
                map.put(DBUtil.getDBString(o, "c1"), o);
                
                conditionProcessStatus = DBUtil.getDBString(o, "c9");
                if(conditionProcessStatus.length()>0){
                    ss = conditionProcessStatus.split(",");
                    for(String s: ss){
                    	tmp = mapChildren.get(s);
                    	if(tmp==null) tmp = new ArrayList<Object>();
                    	tmp.add(o);
                    	mapChildren.put(s, tmp);
                    }
                }
            }
        }
    }
}
