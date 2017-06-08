package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;

import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;

public class MessageService extends LWBaseBean {

	public String MessageService(){
		return this.exeByCmd("");
	}

	public String list(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_MessageList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览消息列表");
		return APPRETURN(result);
	}

	public String listDetail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_MessageDetailList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览消息明细列表");
		return APPRETURN(result);
	}

	public String del(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
			String msgType = this.getParameter("msgType");
			String dataid = this.getParameter("dataid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(YSSQL.LWMessage_DelByC(auid, msgType, dataid));
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, "删除消息成功");
			}else{
				result = new LWResult(LWResult.FAILURE, "删除消息失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, "删除消息");
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, "异常");
		}
		return APPRETURN(result);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;
	
	
	private LWResult ServiceDO_MessageList(){
		try{
			StringBuilder s = new StringBuilder("");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			List<Object> list = dbQuery(YSSQL.LWMessage_QueryByC(auid, "", "", FIRSTID, LASTID));
			int n = 0;
			if(list!=null && list.size()>0){
				s = new StringBuilder("[");
				for(Object o : list){
					if(n == maxcount) break;
					s.append("{\"dataid\":").append(getDBString(o, "dataid"))
					.append(",\"content\":\"").append(getDBString(o, "content")).append("\"")
					.append(",\"msgType\":\"").append(getDBString(o, "msgType")).append("\"")
					.append(",\"msgSubType\":\"").append(getDBString(o, "msgSubType")).append("\"")
					.append(",\"objectID\":\"").append(getDBString(o, "objectID")).append("\"")
					.append(",\"face\":\"").append("").append("\"")
					.append(",\"msgImg\":\"").append(getDBString(o, "msgImg")).append("\"")
					.append(",\"istdate\":\"").append(LWUtil.toChatTime(getDBString(o, "istdate"))).append("\"")
					.append("},");
					n++;
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
			}
			
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}

	private LWResult ServiceDO_MessageDetailList(){
		StringBuilder s = new StringBuilder("");
		String msgType = this.getParameter("msgType");
		String FIRSTID = this.getParameter("i0");
		String LASTID = this.getParameter("i1");
		int maxcount = this.getParameter("i9", 20);
		pageFlag = 1; //使用分页
		pageRow = maxcount;
		List<Object> list = dbQuery(YSSQL.LWMessage_QueryByC(auid, msgType, "", FIRSTID, LASTID));
		int n = 0;
		if(list!=null && list.size()>0){
			s = new StringBuilder("[");
			for(Object o : list){
				if(n == maxcount) break;
				s.append("{\"dataid\":").append(getDBString(o, "dataid"))
				.append(",\"content\":\"").append(getDBString(o, "content")).append("\"")
				.append(",\"msgType\":\"").append(getDBString(o, "msgType")).append("\"")
				.append(",\"msgSubType\":\"").append(getDBString(o, "msgSubType")).append("\"")
				.append(",\"objectID\":\"").append(getDBString(o, "objectID")).append("\"")
				.append(",\"face\":\"").append(getDBString(o, "face")).append("\"")
				.append(",\"msgImg\":\"").append(getDBString(o, "msgImg")).append("\"")
				.append(",\"istdate\":\"").append(LWUtil.toChatTime(getDBString(o, "istdate"))).append("\"")
				.append("},");
				n++;
			}
			s.deleteCharAt(s.length()-1);
			s.append("]");
		}
		
		if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
		return new LWResult(LWResult.FAILURE, "无记录");
	}

}
