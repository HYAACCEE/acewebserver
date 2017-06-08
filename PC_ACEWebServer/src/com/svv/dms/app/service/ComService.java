package com.svv.dms.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.db.util.DBUtil;
import com.svv.dms.app.ComBeanAOrgan;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanAction;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.ComBeanProcess;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.Constants;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanI_DataTable;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.entity.I_DataParamType;
import com.svv.dms.web.entity.I_DataTable;
import com.svv.dms.web.entity.I_DataTableColumn;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.DES;
import com.svv.dms.web.util.HIUtil;
import com.svv.dms.web.util.Pinyin4jUtil;

public class ComService extends LWBaseBean {

	public String ComService(){
		return this.exeByCmd("");
	}
	
	public String reloadSysData(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String password = this.getParameter("password");
		if(password.equals(DES.md5("wwwwwwww"))){
			ComBeanProcess.load();
			ComBeanAction.load();
			ComBeanAOrgan.load();
			ComBeanMMOrgan.reload();
			ComBeanAPPModule.load();
			ComBeanLoginUser.load();
			ComBeanI_DataParamType.load();
			ComBeanI_DataTable.load();
			result = new LWResult(LWResult.SUCCESS, "重载系统数据成功");
		}else{
			result = new LWResult(LWResult.FAILURE, "错误");
		}		
		return APPRETURN(result);
	}
	
	public String html(){
		logger.debug("[html]====================================");
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;

		try{
			String objectMode = this.getParameter("objectMode");
			int enid = this.getParameter("enid", -1);
			String html = ComBeanAPPModule.getHtml(objectMode);
			String id, type, limit, subFORMAT, subHtml;
			
			Pattern p;
			Matcher m;
			p = Pattern.compile("<DATAPARAM id=\"(.+?)\" type=\"(.+?)\">(.+?)</DATAPARAM>");
			m = p.matcher(html);
			while(m.find()){
				id = m.group(1);
				type = m.group(2);
				subFORMAT = m.group(3);
				
				List<I_DataParamType> list = ComBeanI_DataParamType.getList(Integer.parseInt(id));
				String[][] tmp = null;
				if("RADIO".equals(type) || "CHECKBOX".equals(type)){
					tmp = new String[list.size()][4];
					int i = 0;
					String s;
					for(I_DataParamType o: list){
						s = o.getParamClassID()+"";
						tmp[i++] = new String[]{s, s, s, o.getClassName()};
					}
					
				}else if("ID_MEMO_NAME".equals(type)){
					tmp = new String[list.size()][3];
					int i = 0;
					String s;
					for(I_DataParamType o: list){
						s = o.getParamClassID()+"";
						tmp[i++] = new String[]{s, o.getMemo(), o.getClassName()};
					}
					
				}else{
					if("SELECT".equals(type)){
					}else if("selectPage".equals(type)){
					}
					tmp = new String[list.size()][2];
					int i = 0;
					String s;
					for(I_DataParamType o: list){
						s = o.getParamClassID()+"";
						tmp[i++] = new String[]{s, o.getClassName()};
					}
				}
				subHtml = LWUtil.HTML_toDatalist(subFORMAT, tmp, "", "");
				html = html.replaceAll("<DATAPARAM id=\""+id+"\" type=\""+type+"\">(.+?)</DATAPARAM>", subHtml);
			}
			
			p = Pattern.compile("<DATATABLE id=\"(.+?)\" type=\"(.+?)\" limit=\"(.+?)\">(.+?)</DATATABLE>");
			m = p.matcher(html);
			while(m.find()){
				id = m.group(1);
				type = m.group(2);
				limit = m.group(3);
				subFORMAT = m.group(4);

				int tableID = Integer.parseInt(id);
                I_DataTable tmptable = ComBeanI_DataTable.get(tableID);
				String tableName = tmptable.getTableName();
				List<I_DataTableColumn> columns = tmptable.queryColumnList();
				String keyColumn = "c1";
				for(I_DataTableColumn c: columns){
					if(c.getExtKeyNameFlag()==1){
						keyColumn = c.getColName();
						break;
					}
				}
				String where = limit.replaceAll("AUID", auid);
				TreeMap<Long, String> map = new TreeMap<Long, String>();
				List<Object> list = dbQuery(String.format("select dataid key, %s name from %s where %s order by dataid", new Object[]{keyColumn, tableName, where}));
				if(list!=null && list.size()>0){
					for(Object o: list){
						map.put(this.getDBLong(o, "key"), this.getDBString(o, "name"));
					}
				}
				String[][] tmp = null;
				if("SELECT".equals(type)){
					tmp = new String[map.size()][2];
					int i = 0;
					String s;
					for(Long key: map.keySet()){
						tmp[i++] = new String[]{String.valueOf(key), map.get(key)};
					}
				}
				if(tmp!=null){
					subHtml = LWUtil.HTML_toDatalist(subFORMAT, tmp, "", "");
				}else{
					subHtml = "";
				}
				html = html.replaceAll("<DATATABLE id=\""+id+"\" type=\""+type+"\" limit=\""+limit+"\">(.+?)</DATATABLE>", subHtml);
					
			}
			
			result = new LWResult(LWResult.SUCCESS, html);
			
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}

	//新增对象
	public String objectAdd(){
		logger.debug("[objectAdd]====================================");
		if(!ACCESS(true, true, true)) return BConstants.MESSAGE;
		LWResult result = null;

		try{
			String objectMode = this.getParameter("objectMode");
			String relationQueryObjectMode = this.getParameter("relationQueryObjectMode");
			String relationQueryResult = this.getParameter("relationQueryResult");
			long dataid = this.getParameter("dataid", -1L);
			HashMap<String, String> map = new HashMap<String, String>();
			List<Object> list = dbQuery(YSSQL.LWModuleObjectProperties_QueryByC(objectMode, "180014"));
			I_DataTable table = ComBeanI_DataTable.get(DBUtil.getDBInt(list.get(0), "tableID"));
			Object queryO = null;
			if(relationQueryResult.length()>0){
				queryO = dbQuery(String.format(YSSQL.LWObject_QueryByDataid, new Object[]{ComBeanI_DataTable.get(Integer.parseInt(relationQueryObjectMode)).getTableName(), relationQueryResult})).get(0);
			}
			
			if(list!=null && list.size()>0){
				String cname, defvalue, param2, action; int ctype; boolean replationNew = false;
				String ext_tableID = null;
				String recheck_condition_myamid = "", recheck_condition_relation = "";
				HashMap<String, String> ext_map = new HashMap<String, String>();
				for(Object o: list){
					cname = DBUtil.getDBString(o, "cname");
					ctype = DBUtil.getDBInt(o, "ctype");
					defvalue = DBUtil.getDBString(o, "defvalue");
					param2 = DBUtil.getDBString(o, "param2");
					action = DBUtil.getDBString(o, "action");
					
					if(defvalue.equals("=AMID")){
						recheck_condition_myamid = " and " + cname + "=" + myamid;
					}
					
					if(queryO!=null && param2.length()>0 && param2.indexOf(".")>0){
						if(param2.equals(relationQueryObjectMode+".dataid")){
							recheck_condition_relation = " and " + cname + "=" + relationQueryResult;
						}
						
						String[] ss = param2.split("\\.");
						map.put(cname, getDBString(queryO, ss[1]));
						
					}else{
						if(ctype==2){ //图片
							String picpath = param2;
							if(this.getParameter("N", 0)==1){
								map.put(cname, this.uploadPic(cname, picpath, UGID.createUGID(), 2000*1024, false, false, true, false, -1, -1));
							}else if(this.getParameter("N", 0)==0){
								map.put(cname, dES.decrypt(this.getParameter(cname)));
							}
							
						}else if(ctype==4){ //UGID   // && this.getParameter(cname).length()==0
							if(dataid==-1){
								if(defvalue.equals("=UGID")) map.put(cname, UGID.createUGID());
								if(defvalue.startsWith("seq")) map.put(cname, this.getSqlValue("select "+defvalue+" cc from dual", "cc"));
							}else{
							    map.put(cname, dES.decrypt(this.getParameter(cname)));
							}
						}else{
							if(cname.length()>0)
							    map.put(cname, dES.decrypt(this.getParameter(cname)));
						}
					}

					if(param2.length()>0 && param2.indexOf(".")>0){
						String[] ss = param2.split("\\.");
						ext_tableID = ss[0];
						ext_map.put(ss[1], HIUtil.isEmpty(map.get(cname), defvalue));
						if(action.equals("RELATIONNEW")) replationNew = true;
					}
					
				}
				if(recheck_condition_relation.length()>0){
					List templist = dbQuery(String.format("select dataid from %s where 1=1 and dataStatus<>-2 %s %s", new Object[]{table.getTableName(), recheck_condition_myamid, recheck_condition_relation}));
					if(templist!=null && templist.size()>0){
						result = new LWResult(LWResult.FAILURE, "请不要重复添加");
						return APPRETURN(result);
					}
				}
				
				String ext_sql = null;
				if(ext_map.size()>0){					
					if(dataid==-1 && replationNew){ //新增
						StringBuilder a = new StringBuilder();
						StringBuilder b = new StringBuilder();
						String ext_tableName = ComBeanI_DataTable.get(Integer.parseInt(ext_tableID)).getTableName();
						Set<String> keys = ext_map.keySet();
						for(String key: keys){
							a.append(key).append(",");
							b.append("'").append(ext_map.get(key)).append("',");
						}
						a.deleteCharAt(a.length()-1);
						b.deleteCharAt(b.length()-1);
						ext_sql = String.format("insert into %s(%s) values(%s)", new Object[]{ext_tableName, a.toString(), b.toString()});
					    
					}else{ //更新
						
					}
				}
				ext_map.clear();

				List<I_DataTableColumn> collist = table.getColumns();
		        int fixColNum = (!map.containsKey("parentDataid") ? 2 : 3);
				Object[] tmpParams = new Object[collist.size() + fixColNum];
		    	int k = 0;
		    	tmpParams[k++] = -1;
		    	tmpParams[k++] = -1;
		    	StringBuilder keyword = new StringBuilder();
		    	String colvalue;
		    	for(I_DataTableColumn o: collist){
		        	if(map.containsKey(o.getColName())){
			    		colvalue = map.get(o.getColName());
		        		tmpParams[k++] = colvalue;
			        	if(o.getExtKeywordFlag()==1) keyword.append(colvalue).append(" ").append(Pinyin4jUtil.getPinYin(colvalue)).append(" ")
			        	                            .append(Pinyin4jUtil.getPinYinHeadChar(colvalue)).append(" ");
		        	}else{
		        		tmpParams[k++] = "";
		        	}
		        }
		    	if(map.containsKey("parentDataid")) tmpParams[k++] = map.get("parentDataid");
				
				Object[] params = new Object[]{
			        		"add",
			        		dataid,
			        		table.getTableID(),
			        		HIUtil.toDBStr(keyword.toString()),
			        		HIUtil.toSPParamSplitString(tmpParams),
			        		Constants.SPLITER,
			        		tmpParams.length,
			        		myUserName,
			        		};
			    if(dbExe("SP_B_DataManager", params)){
			    	if(ext_sql!=null) dbExe(ext_sql);
					result = new LWResult(LWResult.SUCCESS, "操作成功");
			        loggerM(ComBeanLogType.TYPE_ADD, "添加/编辑对象");
		        }else{
					result = new LWResult(LWResult.FAILURE, "操作失败");
		        }
			}
		}catch(Exception e){
			result = new LWResult(LWResult.FAILURE, "操作异常");
			e.printStackTrace();
		}
		return APPRETURN(result);
	}

	//删除对象
	public String objectDel(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result;
		try{
			String objectMode = this.getParameter("objectMode");
			String dataid = this.getParameter("dataid");
			List<Object> list = dbQuery(YSSQL.LWModuleObjectProperties_QueryByC(objectMode, ""));
			I_DataTable table = ComBeanI_DataTable.get(DBUtil.getDBInt(list.get(0), "tableID"));
			Object[] params = new Object[]{
	        		"delStatus",
	        		dataid,
	        		table.getTableID(),
	        		"",
	        		"",
	        		"",
	        		0,
	        		myUserName,
	        		};
	    if(dbExe("SP_B_DataManager", params)){
			result = new LWResult(LWResult.SUCCESS, "删除成功");
	        loggerM(ComBeanLogType.TYPE_ADD, "删除对象");
        }else{
			result = new LWResult(LWResult.FAILURE, "删除失败");
        }

		}catch(Exception e){
			result = new LWResult(LWResult.FAILURE, "删除异常");
			e.printStackTrace();
		}
		return APPRETURN(result);
	}

	//对象编辑属性列表
	public String objectProperties(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result;
		try{
			int n = 0;
			String objectMode = this.getParameter("objectMode");
			String dataid = this.getParameter("dataid");
			StringBuilder s = new StringBuilder();
			List<Object> list = dbQuery(YSSQL.LWModuleObjectProperties_QueryByC(objectMode, "180014"));
			if(list==null || list.size()==0){
				list = dbQuery(YSSQL.LWModuleObjectProperties_QueryByC(objectMode, "180014"));
			}
			Object d = null;
			if(dataid.length()>0){
				I_DataTable table = ComBeanI_DataTable.get(DBUtil.getDBInt(list.get(0), "tableID"));
				d = dbQuery(String.format(YSSQL.LWObject_QueryByDataid, new Object[]{table.getTableName(), dataid})).get(0);
			}
			if(list!=null && list.size()>0){
				s = new StringBuilder("[");
				for(Object o: list){
					s.append("{\"ptitle\":\"").append(DBUtil.getDBString(o, "ptitle")).append("\"")
					.append(",\"title\":\"").append(DBUtil.getDBString(o, "title")).append("\"")
					.append(",\"ctype\":").append(DBUtil.getDBString(o, "ctype"))
					.append(",\"cname\":\"").append(DBUtil.getDBString(o, "cname")).append("\"")
					.append(",\"defvalue\":\"").append(DBUtil.getDBString(o, "defvalue")).append("\"")
					.append(",\"action\":\"").append(DBUtil.getDBString(o, "action")).append("\"")
					.append(",\"relatid\":\"").append(DBUtil.getDBString(o, "relatid")).append("\"")
					.append(",\"power\":").append(DBUtil.getDBString(o, "power"))
					.append(",\"mvalue\":\"").append(d==null?"":DBUtil.getDBString(d, DBUtil.getDBString(o, "cname"))).append("\"")
					.append("},");
					n++;
				}
			}
			if(n>0){
				s.deleteCharAt(s.length()-1);
				s.append("]");
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, "无记录");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}

	//对象列表
	public String objectList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result;
		try{
			int n = 0;
			String objectMode = this.getParameter("objectMode"); //模块ID 或tableID
			String condition = this.getParameter("condition");
			String relationQuery = this.getParameter("query");
			String orderDesc = this.getParameter("orderDesc",0)==1? " desc":""; //是否倒序
			String keyword = this.getParameter("keyword");
			String title = this.getParameter("title");
			String groupby = this.getParameter("groupby");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			StringBuilder s = new StringBuilder();
			s = new StringBuilder("[");
			List<Object> list = dbQuery(YSSQL.LWObjectList_QueryByModule(myamid, objectMode, "", relationQuery, keyword, condition, orderDesc, title, groupby, FIRSTID, LASTID));

			String ptitle = "";
			boolean hasChild = false;
			if(list!=null && list.size()>0){
				for(Object o: list){
					if(!ptitle.equals(DBUtil.getDBString(o, "ptitle"))){
						hasChild = true;
						ptitle = DBUtil.getDBString(o, "ptitle");
						if(n>0){
							s.deleteCharAt(s.length()-1);
							s.append("]},");
						}
						s.append("{\"dataid\":\"99999999").append(DBUtil.getDBString(o, "dataid")).append("\"")
						 .append(",\"pk\":\"").append(DBUtil.getDBString(o, "ppk")).append("\"")
						 .append(",\"data1\":\"").append(DBUtil.getDBString(o, "pdata1")).append("\"")
						 .append(",\"data2\":\"").append(DBUtil.getDBString(o, "pdata2")).append("\"")
						 .append(",\"data3\":\"").append(DBUtil.getDBString(o, "pdata3")).append("\"")
						 .append(",\"data4\":\"").append(DBUtil.getDBString(o, "pdata4")).append("\"")
					     .append(",\"title\":\"").append(ptitle).append("\"")
						 .append(",\"typeMode\":\"").append(DBUtil.getDBString(o, "typeMode")).append("\"")
						 .append(",\"pic\":\"").append(DBUtil.getDBString(o, "ppic")).append("\"")
						 .append(",\"pro1\":\"").append(DBUtil.getDBString(o, "ppro1")).append("\"")
						 .append(",\"pro2\":\"").append(DBUtil.getDBString(o, "ppro2")).append("\"")
						 .append(",\"pro3\":\"").append(DBUtil.getDBString(o, "ppro3")).append("\"")
						 .append(",\"pro4\":\"").append(DBUtil.getDBString(o, "ppro4")).append("\"")
						 .append(",\"content\":\"").append(DBUtil.getDBString(o, "pcontent")).append("\"")
						 .append(",\"btnValue\":\"").append(DBUtil.getDBString(o, "pbtnValue")).append("\"")
						 .append(",\"btnAction\":\"").append(DBUtil.getDBString(o, "pbtnAction")).append("\"")
						 .append(",\"btnActionURL\":\"").append(DBUtil.getDBString(o, "pbtnActionURL")).append("\"")
						 .append(",\"childs\":[");
					}
					if(DBUtil.getDBString(o, "title").length()>0 || DBUtil.getDBString(o, "pic").length()>0){
						s.append("{\"dataid\":\"").append(DBUtil.getDBString(o, "dataid")).append("\"")
						 .append(",\"pk\":\"").append(DBUtil.getDBString(o, "pk")).append("\"")
						 .append(",\"data1\":\"").append(DBUtil.getDBString(o, "data1")).append("\"")
						 .append(",\"data2\":\"").append(DBUtil.getDBString(o, "data2")).append("\"")
						 .append(",\"data3\":\"").append(DBUtil.getDBString(o, "data3")).append("\"")
						 .append(",\"data4\":\"").append(DBUtil.getDBString(o, "data4")).append("\"")
						 .append(",\"title\":\"").append(DBUtil.getDBString(o, "title")).append("\"")
						 .append(",\"typeMode\":\"").append(DBUtil.getDBString(o, "typeMode")).append("\"")
						 .append(",\"pic\":\"").append(DBUtil.getDBString(o, "pic")).append("\"")
						 .append(",\"pro1\":\"").append(DBUtil.getDBString(o, "pro1")).append("\"")
						 .append(",\"pro2\":\"").append(DBUtil.getDBString(o, "pro2")).append("\"")
						 .append(",\"pro3\":\"").append(DBUtil.getDBString(o, "pro3")).append("\"")
						 .append(",\"pro4\":\"").append(DBUtil.getDBString(o, "pro4")).append("\"")
						 .append(",\"content\":\"").append(DBUtil.getDBString(o, "content")).append("\"")
						 .append(",\"btnValue\":\"").append(DBUtil.getDBString(o, "btnValue")).append("\"")
						 .append(",\"btnAction\":\"").append(DBUtil.getDBString(o, "btnAction")).append("\"")
						 .append(",\"btnActionURL\":\"").append(DBUtil.getDBString(o, "btnActionURL")).append("\"")
						 .append(",\"childs\":[]")
						 .append("},");
						n++;
					}
				}
			    if(n>0){
				    s.deleteCharAt(s.length()-1);
			    }
				s.append("]");
				if(hasChild) s.append("}]"); 
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, "无记录");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
