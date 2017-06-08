package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import com.gs.db.dao.DaoUtil;
import com.gs.db.util.DBUtil;
import com.svv.dms.app.ACE;
import com.svv.dms.app.BMMOrgan;
import com.svv.dms.app.BUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.EmailUtil;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWServer;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanI_DataTable;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.DES;
import com.svv.dms.web.util.HIUtil;

public class HIService extends LWBaseBean {

	public String HIService(){
		this.systemuser.setUserName("[LWUSER]");
		return this.exeByCmd("");
	}
	public String HIBService(){
		this.systemuser.setUserName("[LWUSER]");
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String HISService(){
		this.systemuser.setUserName("[LWUSER]");
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String HICService(){
		this.systemuser.setUserName("[LWUSER]");
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String HIMService(){
		this.systemuser.setUserName("[LWUSER]");
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String desD(){
		if(!ACCESS(false, false)) return "error";
		LWResult result = new LWResult(LWResult.SUCCESS, dES.decrypt(this.getParameter("a")));
		return APPRETURN(result);
	}
	
	//获取导出数据地址
	public String getdownloadUrl(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = new LWResult(LWResult.SUCCESS, com.svv.dms.web.Constants.WEB_URL);
		return APPRETURN(result);
	}
	
	
	//联系我们
	public String contact(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_contact_ToUs();
		return APPRETURN(result);
	}
	
	private LWResult ServiceDO_contact_ToUs() {
		LWResult result = null;
		String memo = "问题反馈";
		try {
			String name = this.getParameter("name");
			String email = this.getParameter("email");
			String phone = this.getParameter("phone");
			String job = this.getParameter("job");
			String company = this.getParameter("company");
			String question = this.getParameter("problem");
			
			String auid = UGID.createUGID();
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWUserQuestion_Add, new Object[]{auid,name,phone,email,company,job,question}));
			if(dbExe(sqls)){
			 	result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	//onlog
	public String onlog(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		String log = this.getParameter("log");
		String title = this.getParameter("title");
		if(auid.length()>20) dbExe(String.format("update B_UserLogin set c38='%s', c39=sysdate where c1='%s'", new Object[]{title, auid}));
        loggerM(ComBeanLogType.TYPE_LOGIN, log);
		return BConstants.MESSAGE;
	}
	public String autoComplete(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
			String hidata = this.getParameter("id");
			String obj = this.getParameter("obj");
			String data = this.getParameter("data");
			String keyword = dES.decrypt(this.getParameter("keyword"));
			int maxcount = this.getParameter("i9", 50);
			pageFlag = 1; //使用分页
			pageRow = maxcount;

			int n = 0;
			StringBuilder s = new StringBuilder("");
			if("COMPANY".equals(obj)){
				String[] cols = {"a.c1", "a.c2", "a.c3", "a.c4"};
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWAOrgan_QueryByC("", c, keyword, "", ""));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "dataid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append("{")
							 .append("\"id\":\"").append(id).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "c2")+"["+this.getDBString(o, "c1").replaceAll("\\\n", "")+"]").append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "c2")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "c8")+this.getDBString(o, "c9")).append("\"")
							 .append("},");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("HC".equals(obj)){
				String[] cols = {"a.c4", "a.c6", "a.c7", "b.c2","b.c28"};
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = null;
		            if(keyword.trim().length()>0) list = dbQuery(YSSQL.LWHC_QueryByc(c, keyword));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							String companyName = this.getDBString(o, "CompanyName", "FreeLancer").replaceAll("\\[(.+?)\\]", "");
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "HCAuid")).append("\"")
							 .append(",\"title\":\"").append(companyName.isEmpty()?"FreeLancer":companyName)
							 .append("|").append(this.getDBString(o, "HCName"))
							 .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(this.getDBString(o, "email")).append("/").append(this.getDBString(o, "phone")).append("\"")
							 .append(",\"data\":\"").append(id).append("\"")
							 .append(",\"data1\":\"").append(id).append("\"")
							 .append("},");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("HCCOMPANY_M".equals(obj)){
				String[] cols = {"a.c1", "a.c2", "a.c3", "a.c4"};
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWCompany_QueryByC("", c, keyword, "", ""));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append("{")
							 .append("\"id\":\"").append(id).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "c2")).append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "c2")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "c2")+this.getDBString(o, "c2")).append("\"")
							 .append("},");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			

			if("AORGAN".equals(obj)){
				String name = this.getParameter("name");
				String[] cols = {"c1", "c2", "c3", "c4"};
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWAOrgan_QueryByC("", c, keyword, "", ""));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "dataid");
							if(ids.contains(id)) continue;
							ids.add(id);
							/*s.append("{")
							 .append("id:\"").append(this.getDBString(o, "c1")).append("\"")
							 .append(",text:\"").append(this.getDBString(o, "c1")).append("\"")
							 .append("},");*/
							s.append("{")
							 .append("\"id\":\"").append(id).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "c2")+"["+this.getDBString(o, "c1").replaceAll("\\\n", "")+"]").append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "c2")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "c8")+this.getDBString(o, "c9")).append("\"")
							 .append("},");
							/*s.append("\"").append(this.getDBString(o, "c2")).append("[").append(this.getDBString(o, "c1")).append("]").append("\",");*/
						}
						n++;
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称搜索完成");
			}
			if("ORGAN".equals(obj)){
				String[] cols = {"c2"};
				List<Long> ids = new ArrayList<Long>();
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWHROrgan_QueryByC("", "", "", "", keyword, "", "",""));
					if(list!=null && list.size()>0){
						s.append("[");
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "amid")).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "companyName")).append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "addr1")).append(",").append(this.getDBString(o, "addr2")).append(",").append(this.getDBString(o, "addr3")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "amid")).append("\"")
							 .append("},");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
				}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("CHECKEDHR".equals(obj)){
				String[] cols = {"c2"};
				List<Long> ids = new ArrayList<Long>();
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWHROrgan_QueryByC1(keyword,ACE.MM_BizStatus_CHECKING+","+ACE.MM_BizStatus_CHECKED+""));
					if(list!=null && list.size()>0){
						s.append("[");
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "amid")).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "companyName")).append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "addr1")).append(",").append(this.getDBString(o, "addr2")).append(",").append(this.getDBString(o, "addr3")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "amid")).append("\"")
							 .append("},");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
				}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("ADDHR".equals(obj)){
				String[] cols = {"b.c2","b.c28","a.c4","a.c7","a.c6"};
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWCandidate_Command_queryHR(c, keyword));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							String logo = this.getDBString(o, "logo").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "logo");
							String CompanyName = this.getDBString(o, "CompanyName").replaceAll("\\[.*?]", "");
							String button = "";
							if(this.getDBString(o, "status").equals("0") || this.getDBString(o, "status").equals("2")){
								button = "<button class='float-r btn-disabled pd5 h20 f12'>已添加</button>";
							}else{
								button = "<button class='float-r btn-focus pd5 h20 f12 clickAdd_'>添加</button>";
							}
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "hrAuid")).append("\"")
							 .append(",\"title\":\"").append(CompanyName).append("|").append(this.getDBString(o, "HRName")).append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<a class='grey'>"+this.getDBString(o, "email")+"</a>")
							 .append("&nbsp;&nbsp;").append(button)
							 .append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "hrAuid")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "hrAuid")).append("\"")
							 .append("},");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "HR查询条件匹配("+keyword+")");
			}
			if("Candidate".equals(obj)){
				List<String> cols = new ArrayList<String>();
				cols.add("b.c8");
				cols.add("b.c41");
				cols.add("b.c42");
				if(keyword.matches("[0-9]+")){
					cols.add("b.dataid");
				}
				List<Long> ids = new ArrayList<Long>();
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWCandidate_SpecialCommand_queryCandidate(c, keyword,ACE.CANDI_STStatus_ST,ACE.CANDI_ActiveStatus_ACTIVE));
					if(list!=null && list.size()>0){
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							String photo = this.getDBString(o, "photo").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "photo");
							//String CompanyName = this.getDBString(o, "CompanyName").replaceAll("\\[.*?]", "");
							String button = "";
							List<Object> extisCandi = dbQuery(String.format(YSSQL.LWCandidate_SpecialCommand_checkCandidate, new Object[]{this.getDBString(o, "auid"),data}));
							if(extisCandi!=null&&extisCandi.size()>0){
								button = "<button class='float-r btn-disabled pd5 h20 f12'>已添加</button>";
							}else{
								button = "<button class='float-r btn-focus pd5 h20 f12 clickAdd_'>添加</button>";
							}
							String ace = "";
							if(this.getDBString(o, "ace").contains("2")){
								ace = "<span class='light-grey'>"+"精选名单下周预备中"+"</span>";
							}else if(this.getDBString(o, "ace").contains("1")){
								ace = "<span class='light-grey'>"+"精选于"+this.getDBString(o, "aceTime")+"</span>";
							}
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "auid")).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "engName").isEmpty()?this.getDBString(o, "chiName"):this.getDBString(o, "chiName")+"("+this.getDBString(o, "engName")+")").append("&nbsp;&nbsp;&nbsp;").append(this.getDBString(o, "recentCompany")).append("|").append(this.getDBString(o, "recentJob"))
							 .append("&nbsp;&nbsp;&nbsp;&nbsp;").append(ace)
							 .append("&nbsp;&nbsp;").append(button)
							 .append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "hrAuid")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "hrAuid")).append("\"")
							 .append("},");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "HR查询条件匹配("+keyword+")");
			}
			if("JOBTYPE".equals(obj)){
				String[] cols = {"c36"};
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWCandidateJobType_QueryByC(myamid, ACE.CANDI_STStatus_ST, ACE.CANDI_ActiveStatus_ACTIVE, "1", ""));
					if(list!=null && list.size()>0){
						s.append("[");
						long id;
						s.append("{")
						 .append("\"id\":\"").append("").append("\"")
						 .append(",\"title\":\"").append("所有职位类型").append("\"")
						 .append(",\"data\":\"").append("").append("\"")
						 .append(",\"data1\":\"").append("").append("\"")
						 .append("},");
						for(Object o: list){
							if(n == maxcount) break;
//							s.append("\"").append(this.getDBDataParam(o, "cname","","","")).append("\",");
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "cname")).append("\"")
							 .append(",\"title\":\"").append(this.getDBDataParam(o, "cname","","","")).append("\"")
							 .append(",\"data\":\"").append("").append("\"")
							 .append(",\"data1\":\"").append("").append("\"")
							 .append("},");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
				}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("JOB".equals(obj)){

				System.out.println("[HISERVICE.JOB]======================================="+auid+"auid的值");
				String[] cols = {"c1"};
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWCandidateJob_QueryByC(auid));
					if(list!=null && list.size()>0){
						s.append("[");
						long id;
						for(Object o: list){
							if(n == maxcount) break;
							s.append("{")
							 .append("\"id\":\"").append("").append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "cname","","","")).append("\"")
							 .append(",\"data\":\"").append("").append("\"")
							 .append(",\"data1\":\"").append("").append("\"")
							 .append("},");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
				}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("CITY".equals(obj)){
				String[] cols = {"c15"};
				List<Long> ids = new ArrayList<Long>();
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWHRCity_QueryByC(myamid, ACE.CANDI_STStatus_ST,ACE.CANDI_ActiveStatus_ACTIVE, "1", ""));
					if(list!=null && list.size()>0){
						s.append("[");
						s.append("\"").append("所有城市").append("\",");
						for(Object o: list){
							if(n == maxcount) break;
							s.append("\"").append(this.getDBString(o, "cname")).append("\",");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
				}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "公司名称匹配("+keyword+")");
			}
			if("SCHOOL".equals(obj)){
				System.out.println("[HISERVICE.SCHOOL]=======================================");
				String[] cols = {"c1", "c2", "c3", "c4"};
				List<Long> ids = new ArrayList<Long>();
				long id;
				s.append("[");
				for(String c: cols){
		            List<Object> list = dbQuery(YSSQL.LWSchool_QueryByC(c, keyword, ""));
					if(list!=null && list.size()>0){
						for(Object o: list){
							if(n == maxcount) break;
							id = this.getDBLong(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append("\"").append(this.getDBString(o, "c1")).append("\",");
							n++;
						}
					}
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "学校名称匹配("+keyword+")");
			}
			if("HCCOMPANY".equals(obj)){
				System.out.println("[HISERVICE.HCCOMPANY]=======================================");
				List<Long> ids = new ArrayList<Long>();
		            List<Object> list = dbQuery(YSSQL.LWHCCompany_QueryByC("", "", keyword));
					if(list!=null && list.size()>0){
						s.append("[");
						for(Object o: list){
							if(n == maxcount) break;
							s.append("\"").append(this.getDBString(o, "companyName")).append("\",");
							n++;
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
					}
				if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
				else result = new LWResult(LWResult.FAILURE, "[]");
		        loggerM(ComBeanLogType.TYPE_QUERY, "猎头公司名称匹配("+keyword+")");
			}
			if("HR".equals(obj)){
				String[] cols = {"c4"};
				List<Long> ids = new ArrayList<Long>();
				
				for (String c : cols) {
					List<Object> hrList = dbQuery(YSSQL.LWHR_QueryByC(keyword, data,ACE.USER_CHECK_STATUS_CHECKED));
					if(hrList!=null && hrList.size()>0){
						s.append("[");
						for (Object o : hrList) {
							if(n == maxcount) break;
							s.append("{")
							 .append("\"id\":\"").append(this.getDBString(o, "auid")).append("\"")
							 .append(",\"title\":\"").append(this.getDBString(o, "hrName").replaceAll("\\\n", "")).append("\"")
							 .append(",\"data\":\"").append(this.getDBString(o, "amid")).append("\"")
							 .append(",\"data1\":\"").append(this.getDBString(o, "auid")).append("\"")
							 .append("},");
						}
						s.deleteCharAt(s.length()-1);
						s.append("]");
						n++;
					}
					if(n>0) result = new LWResult(LWResult.SUCCESS, s.toString());
					else result = new LWResult(LWResult.FAILURE, "[]");
					loggerM(ComBeanLogType.TYPE_QUERY, "HR名称匹配("+keyword+")");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.ERRLOGIN, "autoComplete异常");
		}
		
		
		return APPRETURN(result);
	}
	
	//version
	public String version(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String  s = getVersion();
		if(s.length()>0){
			result = new LWResult(LWResult.SUCCESS, "{"+s.toString()+"}");
		}else{
			result = new LWResult(LWResult.FAILURE, "无记录");
		}
		return APPRETURN(result);
	}
	
	public String onStart(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		String appid = this.getParameter("appid"); //ACE appid=2016120101
		String secret = this.getParameter("secret"); //ACE secret=666ef4q3f8fhiu89sd33
		LWResult result = null;
		StringBuilder s = new StringBuilder("");
		s.append("{\"appid\":\"").append(appid).append("\"")
		 .append(",\"version\":{").append(getVersion()).append("}")
		 .append(",\"LOG_DEBUG_FLAG\":").append(Constants.LOG_DEBUG_FLAG)
		 .append(",\"LOG_UPLOAD_FLAG\":").append(Constants.LOG_UPLOAD_FLAG)
		 .append(",\"QQ_APPID\":\"").append(Constants.QQ_APPID).append("\"")
		 .append(",\"QQ_APPKEY\":\"").append(Constants.QQ_APPKEY).append("\"")
		 .append(",\"WEIXIN_APPKEY\":\"").append(Constants.WEIXIN_APPKEY).append("\"")
		 .append(",\"WEIXIN_SECRET\":\"").append(Constants.WEIXIN_SECRET).append("\"")
		.append("}");		
		result = new LWResult(LWResult.SUCCESS, s.toString());
		return APPRETURN(result);
	}

	//验证登录
	public String applyLogon(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
			String a = auid.length()==0? "" : "验证";
			String by = "PHONE";
			String loginName = this.getParameter("a2");
			String password = this.getParameter("a1");
			String openid = this.getParameter("usid");
			Object user = checkUserExist(mmGroup, loginName, password, null, null, openid, null);
			if(user==null || getDBInt(user, "mmGroup")!=mmGroup){ //验证mmGroup
				result = new LWResult(LWResult.ERRLOGIN, "用户或密码错误");
				return APPRETURN(result);
			}
			auid = this.getDBString(user, "auid");
			mmGroup = this.getDBInt(user, "mmGroup");
			
			List<String> sqls = new ArrayList<String>();
			if(a.length()==0) sqls.add(String.format(YSSQL.LWUserLogin_LOGON_SET, new Object[]{auid}));
			if(M0.equals("MMB") ||M0.equals("IMMB")){
				sqls.add(String.format(YSSQL.PUSHSMS_RESET, new Object[]{auid}));
			}
			dbExe(sqls);
			
			result = new LWResult(LWResult.SUCCESS, F_toUserJson(user));
			this.systemuser.setUserName("["+toMMGroupDesc(mmGroup)+"-"+this.getParameter("M1")+"]"+auid);
	        loggerM(ComBeanLogType.TYPE_LOGIN, loginName+a+"登录 by "+by);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.ERRLOGIN, "异常");
		}
		return APPRETURN(result);
	}
	/*
	public String loginByOthers(){
		mmGroup = Constants.USER1_MMGROUP;
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		// 手机
		String openid = this.getParameter("usid");
		String nickName = this.getParameter("nickName");
		String face = this.getParameter("face");
		String area = this.getParameter("area");
		String sex = this.getParameter("sex");
		String by = this.getParameter("by");
		String memo = by + nickName;
		if(openid.length()==0){
			result = new LWResult(LWResult.FAILURE, "参数异常");
		}else{
			Object user = checkUserExist(null, null, null, openid);
			if(user == null){
			    BMMOrgan mm = null;
			    if(Constants.USER1_MMGROUP.equals(mmGroup)) mm = new BMMOrgan(mmGroup, -1, "", "");
			    else{
			    	//mm = new BMMOrgan(mmGroup, 11001, "160201002323999010130714348232", "管理中心");
			    }
			    
				BUser auser = addAUser(mmGroup, by, "", "", "", openid, face, area, sex, "", "", memo, mm);
				if(auser!=null){
					result = new LWResult(LWResult.SUCCESS, toRegistJson(auser));
					this.systemuser.setUserName("[LW-"+this.getParameter("M1")+"]"+nickName+"."+auser.getAuid());
					loggerM(ComBeanLogType.TYPE_ADD, nickName+"注册["+auser.getAuid()+"] "+openid);
				}
			}else{
				result = new LWResult(LWResult.SUCCESS, DBUtil.getDBString(user, "auid"));
				dbExe(String.format(YSSQL.LWUserLogin_LOGON_SET, new Object[]{getDBString(user, "auid")}));
				this.systemuser.setUserName("["+toMMGroupDesc(mmGroup)+"-"+this.getParameter("M1")+"]"+getDBString(user, "auid"));
		        loggerM(ComBeanLogType.TYPE_LOGIN, openid+"登录 by "+by);
			}
		}
		return APPRETURN(result);
	}*/
	
	//验证邮箱
	public String checkEmailPass(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "邮箱验证";
		try{
			String[] ss = dES.decrypt(this.getParameter("pass")).split("`");
			long time = Long.parseLong(ss[0]);
			String userAuid = ss[1];
			String email = ss[2];
//			if((new Date()).getTime() - time > 60000*5){
//				result = new LWResult(LWResult.FAILURE, memo+"失败，链接已失效！");
//			}else{
				List<Object> list = dbQuery(String.format("select c9, c40 addMode from B_UserLogin where c1='%s' and c32='%s'", new Object[]{userAuid, email}));
				if(list!=null && list.size()>0){
					List<String> sqls = new ArrayList<String>();
					int addMode = this.getDBInt(list.get(0), "addMode");
					int userBizStatus = addMode==1? ACE.USER_CHECK_STATUS_CHECKING : ACE.USER_CHECK_STATUS_CONFIRMING;
				    sqls.add(String.format("update B_UserLogin set c9=%s, c54=sysdate, c55=%s where c1='%s'", new Object[]{ACE.USER_STATUS_VALID, userBizStatus, userAuid}));
				    sqls.add(String.format("update B_HR set c17=%s, c18=%s where c1='%s'", new Object[]{userBizStatus, ACE.USER_STATUS_VALID, userAuid}));
					if(dbExe(sqls)){
						result = new LWResult(LWResult.SUCCESS, memo+"成功.");
					}else{
						result = new LWResult(LWResult.SUCCESS, "系统繁忙，请稍后再试~~~");
					}
				}else{
					result = new LWResult(LWResult.FAILURE, memo+"失败！");
				}
//			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"失败！");
		}
		return APPRETURN(result);
	}
	
	//验证邀请码
	public String checkInviteCode(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "验证邀请码";

		try{
			String[] vv = F_formParam();
			int i = 0;
			String code = this.getParameter("code", vv[i++]).toUpperCase();
			List<Object> list = dbQuery(String.format("select a.c3 auid, NVL(b.c4,-1) mmGroup, b.c2 phone, b.c11 nickName, b.c32 email from B_InviteCode a, B_UserLogin b where a.c3=b.c1(+) and a.c1='%s' and a.c4>0 and a.c5=130001 and a.c6>sysdate", new Object[]{code}));
	
			String userAuid = "";
			List<String> sqls = new ArrayList<String>();
			if(list!=null && list.size()>0){
				sqls.add(this.sqlToZ("B_InviteCode", memo, "c1='"+code+"'"));
				sqls.add(String.format("update B_InviteCode set c4=c4-1, uptdate=sysdate where c1='%s'", new Object[]{code}));
	
				Object o = list.get(0);
				int userGroup = this.getDBInt(o, "mmGroup");
				if(userGroup > 0){
					if(isHC() && userGroup==3 || isHR() && userGroup==2){
						userAuid = this.getDBString(o, "auid");
						//HR验证邀请码：需要通过发送验证邮件
					    String password = RandomCode(2, 6).toLowerCase();
					    String nickName = getDBString(o, "nickName");
					    String email = getDBString(o, "email");
					    sqls.add(String.format(YSSQL.LWUserLogin_UpdatePSWD, new Object[]{DES.md5(password), userAuid}));
						String sendEmailResult = F_sendEmail(true, this.getParameter("url"), userAuid, getDBString(o, "phone"), password, email);
						if(sendEmailResult.equals("SUCCESS")){
							if(dbExe(sqls)){
								sqls.clear();
							    result = new LWResult(LWResult.SUCCESS, memo+"成功");
						        loggerM(ComBeanLogType.TYPE_ADD, memo);
							}else{
								result = new LWResult(LWResult.FAILURE, memo+"失败");
							}
						}else if(sendEmailResult.equals(ACE.TXT_EMAIL_NOTCOM)){ //非公司邮箱
					    	//通知短信
					    	if(ACE.ONLINE) sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ACE.ADMIN_BIZ_PHONE, String.format(ACE.TXT_SMS_ADMIN_HR, new Object[]{nickName, email, "激活ACE邀请码失败"}), ACE.SMS_TYPE_ADMIN}));
						}
						
						//HR验证邀请码：需要通过发送验证邮件才能激活用户，所以此处注释掉
	//					sqls.add(this.sqlToZ("B_UserLogin", memo, "c1="+userAuid));
	//					sqls.add(String.format("update B_UserLogin set c9=130001 where c1='%s'", new Object[]{userAuid}));
	//					sqls.add(String.format(YSSQL.LWUserLogin_LOGON_SET, new Object[]{userAuid}));
	
						Object user = dbQuery(String.format(YSSQL.LWUserLogin_QueryByAUID, new Object[]{userAuid})).get(0);
						result = new LWResult(LWResult.SUCCESS, F_toUserJson(user));
					}else{
						result = new LWResult(LWResult.FAILURE, "该邀请码已被使用");
					}
				}else{
					result = new LWResult(LWResult.SUCCESS, "");
				}
			}else{
				result = new LWResult(LWResult.FAILURE, "该邀请码不存在");
			}
		}catch(Exception e){
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
			e.printStackTrace();
		}
		return APPRETURN(result);
	}

	/** 登录 by 手机号  **/
	public String login(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
			// 手机
			String by = "anywhere";
			String loginName = this.getParameter("a2");
			String password = this.getParameter("a1");
			Object user = checkUserExist(mmGroup, loginName, password, null, null, null, null);
			if(user==null || password.length()!=32 || getDBInt(user, "mmGroup")!=mmGroup ){///////////验证mmGroup
				result = new LWResult(LWResult.ERRLOGIN, "用户名或密码错误");
				return APPRETURN(result);
			}
			mmGroup = this.getDBInt(user, "mmGroup");
			if(isHR() && getDBInt(user, "power")==ACE.USER_CHECK_STATUS_NEW ){///////////验证power
				result = new LWResult(LWResult.ERRLOGIN, "您所在的用户组没有权限登录");
				return APPRETURN(result);
			}
			result = new LWResult(LWResult.SUCCESS, F_toLoginJson(user));
			dbExe(String.format(YSSQL.LWUserLogin_LOGON_SET, new Object[]{getDBString(user, "auid")}));
			this.systemuser.setUserName("["+toMMGroupDesc(mmGroup)+"-"+this.getParameter("M1")+"]"+getDBString(user, "auid"));
	        loggerM(ComBeanLogType.TYPE_LOGIN, loginName+"登录 by "+by);
		}catch(Exception e){
			result = new LWResult(LWResult.EXCEPTION, "登录异常");
			e.printStackTrace();
		}
		return APPRETURN(result);
	}
	
	//注销
	public String logout(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		List<String> sqls = new ArrayList<String>();
		sqls.add(String.format(YSSQL.LWUserLogin_LOGOUT, new Object[]{auid}));
		if(dbExe(sqls)){}
		LWResult result = new LWResult(LWResult.SUCCESS, "注销成功");
		return APPRETURN(result);
	}
	
	//忘记密码:重设密码
	public String resetPassword(){

		System.out.println("[resetPassword0]=======================================");
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String phone = this.getParameter("phone");
		String pswd = this.getParameter("pswd");
		List<Object> list = dbQuery(String.format(YSSQL.LWUserLogin_EXIST_PHONE, new Object[]{phone,mmGroup}));
		if(list==null || list.size()!=1){
			result = new LWResult(LWResult.FAILURE, "手机号不存在或无效");
	        loggerM(ComBeanLogType.TYPE_EDIT, "设置登录密码失败，手机号不存在或无效");
		}else{
			if(pswd.length()==32){
				if(dbExe(String.format(YSSQL.LWUserLogin_UpdatePSWD, new Object[]{pswd, DBUtil.getDBString(list.get(0), "auid")}))){
					result = new LWResult(LWResult.SUCCESS, "设置登录密码成功");
			        loggerM(ComBeanLogType.TYPE_EDIT, "设置登录密码成功 ");
				}else{
					result = new LWResult(LWResult.FAILURE, "设置登录密码失败(e0)");
			        loggerM(ComBeanLogType.TYPE_EDIT, "设置登录密码失败(e0) ");
				}
			}else{
				result = new LWResult(LWResult.FAILURE, "设置登录密码失败");
		        loggerM(ComBeanLogType.TYPE_EDIT, "设置登录密码失败");
			}
		}
		return APPRETURN(result);
	}
	
	//发送手机验证码
	public String sendCodeToPhone(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try {
			int seq = this.getParameter("seq", 138003);
			String phone = String.valueOf(this.getParameter("phone", -1L));
			if(phone.length()>0){
				List<Object> list;
				String password = "";
				if(seq==138011 || seq==138012){//重设密码, 登录验证
					list = dbQuery(String.format(YSSQL.LWUserLogin_EXIST_PHONE, new Object[]{phone,mmGroup}));
					if(list==null || list.size()==0 || DBUtil.getDBInt(list.get(0), "state")!=130001){
						result = new LWResult(LWResult.FAILURE, "手机号不存在或无效");
				        loggerM(ComBeanLogType.TYPE_EDIT, "发送手机验证码失败，手机号不存在或无效");
				        return APPRETURN(result);
					}
					if(seq==138012) password = DBUtil.getDBString(list.get(0), "password");
				}
				
				list = dbQuery("select istdate from T_Sms_Ing where c6="+seq+" and c1="+phone);
				if(list!=null && list.size()>0){
					//检查发送频次
					//////////////////////////////////////////////
				}
				
				//生成四位验证码 //////////////////////////////////////////测试平台118 统一发送1111
				String sendCode = String.valueOf(new Random().nextInt(8999)+1000);	
				if(com.svv.dms.web.Constants.WEB_URL.indexOf("118.")>=0 || com.svv.dms.web.Constants.WEB_URL.indexOf("192.168.")>=0 || com.svv.dms.web.Constants.WEB_URL.indexOf("127.0.")>=0) sendCode = "1111";
				
				//发送验证码
				try {
					dbExe(String.format(YSSQL.SMS_INSERT, new Object[]{phone, "您的验证码是："+sendCode+"，如非本人操作，可不用理会。", seq}));
					result = new LWResult(LWResult.SUCCESS, sendCode.concat(password));
			        loggerM(ComBeanLogType.TYPE_EDIT, "发送手机["+phone+"]验证码["+sendCode+"]");
				} catch (Exception e) {
					result = new LWResult(LWResult.FAILURE, "发送手机验证码失败(e1)");
					e.printStackTrace();
				}
					
			}else{
				result = new LWResult(LWResult.FAILURE, "发送手机验证码失败(e0)");
			}
		} catch (Exception e) {
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}

	/** 注册 step1 检验手机号 Email是否已注册  **/
	public String registCheckPhoneEmail(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = new LWResult(LWResult.SUCCESS, "");
		String phone = this.getParameter("phone");
		String email = this.getParameter("email");
		int mmGroup = this.getParameter("userGroup", -1);
		Object user = null;
		if(result.isSuccess() && !HIUtil.isEmpty(phone)){
			user = checkUserExist(mmGroup, phone, null, null, null, null, null);
			if(user!=null){
				result = new LWResult(LWResult.FAILURE, "该手机号已注册");
			}else{
				result = new LWResult(LWResult.SUCCESS, "该手机号未注册");
			}
		}
		if(result.isSuccess() && !HIUtil.isEmpty(email)){
			user = checkUserExist(mmGroup, null, null, null, email, null, null);
			if(user!=null){
				result = new LWResult(LWResult.FAILURE, "该邮箱已注册");
			}else{
				result = new LWResult(LWResult.SUCCESS, "该邮箱未注册");
			}
		}
		return APPRETURN(result);
	}

	/** 注册 step1 检验手机号是否已注册  **/
	public String registCheckPhone(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		String phone = this.getParameter("phone");
		Object user = checkUserExist(mmGroup, phone, null, null, null, null, null);
		LWResult result = null;
		if(user!=null){
			result = new LWResult(LWResult.FAILURE, "该手机号已注册");
		}else{
			result = new LWResult(LWResult.SUCCESS, "该手机号未注册");
		}
		return APPRETURN(result);
	}

	/** 注册 step1 检验邮箱是否已注册  **/
	public String registCheckEmail(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;		
		String email = this.getParameter("email");
		Object user = checkUserExist(mmGroup, null, null, null, email, null, null);
		LWResult result = null;
		if(user!=null){
			result = new LWResult(LWResult.FAILURE, "该邮箱已注册");
		}else{
			result = new LWResult(LWResult.SUCCESS, "该邮箱未注册");
		}
		return APPRETURN(result);
	}

	/** 注册 step1 检验身份证是否已注册  **/
	public String registCheckCardno(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;		
		String cardno = this.getParameter("cardno");
		Object user = checkUserExist(mmGroup, null, null, null, null, null, cardno);
		LWResult result = null;
		if(user!=null){
			result = new LWResult(LWResult.FAILURE, "该身份证已注册");
		}else{
			result = new LWResult(LWResult.SUCCESS, "该身份证未注册");
		}
		return APPRETURN(result);
	}

	/** 注册 step1 检验昵称是否已注册  **/
	public String registCheckNickname(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		String nickname = this.getParameter("nickname");		
		LWResult result = checkNickname(nickname);
		return APPRETURN(result);
	}
	
	private LWResult checkNickname(String nickname){
		LWResult result = null;
		if(!nickname.matches("^(?!-)(?!.*?-$)[a-zA-Z0-9_\u4e00-\u9fa5]+$")){
		//if(!nickname.matches("[\u4e00-\u9fa5\\w]+")){
			result = new LWResult(LWResult.FAILURE, "非法字符");
		}else{
			Object user = checkUserExist(mmGroup, null, null, nickname, null, null, null);
			if(user!=null){
				result = new LWResult(LWResult.FAILURE, "已被注册");
			}else{
				result = new LWResult(LWResult.SUCCESS, "可注册");
			}
		}
		return result;
	}
	/** 注册用户check */
	public String registCheckFirst(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		////////////////////////////////////////////检验手机号是否存在
		String phone = this.getParameter("phone");
		Object user = checkUserExist(mmGroup, phone, null, null, null, null, null);
		if(user!=null){
			result = new LWResult(LWResult.FAILURE, "该手机号已注册");
		}else{
			result = new LWResult(LWResult.SUCCESS, "该手机号未注册");
		}
		
		return APPRETURN(result);
	}
	/** 注册用户  **/
	public String registNewUser(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_registNewUser();
		if(isHC()) result = ServiceDO_registNewUser();
		return APPRETURN(result);
	}
	
	// 设置公司邮箱
	public String verifyEmail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		String email = this.getParameter("email");
		String url = this.getParameter("url");
		LWResult result = F_sendVerifyEmail(url, email); //发送验证email 并修改用户邮箱
		return APPRETURN(result);
	}
	
	/** 用户是否存在 **/
	@SuppressWarnings("rawtypes")
	public static Object checkUserExist(int mmGroup, String loginName, String password, String nickname, String email, String openid, String cardNum){
		String sql = null;
		if(openid!=null && openid.length()>0){ //第三方登录
			sql = String.format(YSSQL.LWUserLogin_LOGIN_ByOPENID, new Object[]{openid});
		} else if(nickname!=null){ //检查昵称
			sql = String.format(YSSQL.LWUserLogin_EXIST_NICKNAME, new Object[]{nickname});
		} else if(email!=null){ //检查email
			sql = String.format(YSSQL.LWUserLogin_EXIST_EMAIL, new Object[]{email,mmGroup});
		} else if(cardNum!=null){ //检查身份证
			sql = String.format(YSSQL.LWUserLogin_EXIST_SIDNUM , new Object[]{cardNum,mmGroup});
		} else if(loginName!=null && password!=null){ //登录验证
			if(loginName.contains("@")){
				sql = String.format(YSSQL.LWUserLogin_LOGIN_ByEmail, new Object[]{loginName, password, mmGroup});
			}else{
				sql = String.format(YSSQL.LWUserLogin_LOGIN_ByPHONE, new Object[]{loginName, password, mmGroup});
			}
		} else if(loginName!=null && password==null){ //注册验证
			sql = String.format(YSSQL.LWUserLogin_EXIST_PHONE, new Object[]{loginName, mmGroup});
		}
		List list = HIUtil.dbQuery(sql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/** 用户是否存在 **/
	public static LWResult F_checkUser(int mmGroup, String userAuid, String phone, String email, String identityCardNO){
		LWResult result = null;
		String errMemo = "手机号已存在";
		Object user = checkUserExist(mmGroup, phone, null, null, null, null, null);
		if(user != null && DBUtil.getDBString(user, "auid").equals(userAuid)) user = null;
		if(user == null){
			errMemo = "Email已存在";
			user = checkUserExist(mmGroup, null, null, null, email, null, null);
			if(user != null && DBUtil.getDBString(user, "auid").equals(userAuid)) user = null;
		}
		if(user == null){
			errMemo = "身份证号已存在";
			user = checkUserExist(mmGroup, null, null, null, null, null, identityCardNO);
			if(user != null && DBUtil.getDBString(user, "auid").equals(userAuid)) user = null;
		}
		if(user == null){
			result = new LWResult(LWResult.SUCCESS, "");
		}else{
			result = new LWResult(LWResult.FAILURE, errMemo);
		}
		return result;
	}	
	//addMode : 注册方式（0-用户注册 1-后台添加）
	public static BUser addAUser(String newAuid, String mmGroup, int addMode, String loginName, String password, String _nickName, String fullName, String openid, String face, String area, String sex, String comPhone, String job, String mmFlag, String companyName, String email, String identityCardNO, String sidPhoto, String sidPhoto2, String namenameCardPhoto1, String namenameCardPhoto2, String memo, BMMOrgan mm){
		try{
			String nickName = HIUtil.isEmpty(_nickName, "t".concat(newAuid.substring(20)));
			BUser user = new BUser(mmGroup, newAuid, loginName, mm.getMid(), mm.getAmid(), nickName, face, sex);
			
			List<String> sqls = new ArrayList<String>();
			System.out.println(String.format(YSSQL.LWUserLogin_ADD, new Object[]{newAuid, loginName, password, mm.getMmGroup(), mm.getMid(), mm.getAmid(), nickName, memo, fullName, face, sex, area, openid, comPhone, email, job, addMode, identityCardNO}));
			sqls.add(String.format(YSSQL.LWUserLogin_ADD, new Object[]{newAuid, loginName, password, mm.getMmGroup(), mm.getMid(), mm.getAmid(), nickName, memo, fullName, face, sex, area, openid, comPhone, email, job, addMode,identityCardNO}));
			if(mmGroup.equals(Constants.USER2_MMGROUP)){ // HR c1auid,c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
				System.out.println(String.format(YSSQL.LWHR_ADD, new Object[]{newAuid, mm.getAmid(), mm.getMid(), nickName, sex, loginName, email, comPhone, companyName, job, fullName, identityCardNO, sidPhoto,sidPhoto2,namenameCardPhoto1,namenameCardPhoto2}));
			    sqls.add(String.format(YSSQL.LWHR_ADD, new Object[]{newAuid, mm.getAmid(), mm.getMid(), nickName, sex, loginName, email, comPhone, companyName, job, fullName, identityCardNO, sidPhoto,sidPhoto2,namenameCardPhoto1,namenameCardPhoto2}));
				
			    //后台管理平台添加的用户，会生成邀请码
			    if(addMode==1){
				    //生成6位邀请码
				    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			    	String inviteCode  = RandomCode(1, 6);
				    sqls.add(String.format(YSSQL.LWHR_InviteCode_ADD, new Object[]{inviteCode, mm.getAmid(), newAuid, 1, 0, ""}));
			    }
	 
			    sqls.add(String.format("update B_UserLogin set c9=%s, c55=%s where c1='%s'", new Object[]{ACE.USER_STATUS_NEW, ACE.USER_CHECK_STATUS_NEW, newAuid}));
			    sqls.add(String.format("update B_HR set c17=%s, c18=%s where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_NEW, ACE.USER_STATUS_NEW, newAuid}));
	
			    if(addMode==0){
			    	//通知短信
			    	if(ACE.ONLINE) sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ACE.ADMIN_PHONE, String.format(ACE.TXT_SMS_ADMIN_HR, new Object[]{companyName, nickName, "注册ACE"}), ACE.SMS_TYPE_ADMIN}));
			    }
		    
			}else if(mmGroup.equals(Constants.USER3_MMGROUP)){ // 猎头c1,c2,c3,c4昵称,c5,c6,c7,c8,c9,c14,c16,c18,c33真实姓名,c34,c35,c36,c37,c38
				System.out.println(String.format(YSSQL.LWHunter_ADD, new Object[]{newAuid, mm.getAmid(), mm.getMid(), nickName, sex, loginName, email, comPhone, companyName, area, mmFlag, fullName,identityCardNO,sidPhoto,sidPhoto2,namenameCardPhoto1,namenameCardPhoto2}));
			    sqls.add(String.format(YSSQL.LWHunter_ADD, new Object[]{newAuid, mm.getAmid(), mm.getMid(), nickName, sex, loginName, email, comPhone, companyName, area, mmFlag, fullName,identityCardNO,sidPhoto,sidPhoto2,namenameCardPhoto1,namenameCardPhoto2}));
	
			    sqls.add(String.format("update B_UserLogin set c9=%s, c55=%s where c1='%s'", new Object[]{ACE.USER_STATUS_NEW, ACE.USER_CHECK_STATUS_NEW, newAuid}));
			    sqls.add(String.format("update B_Hunter set c17=%s, c18=%s where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_NEW, ACE.USER_STATUS_NEW, newAuid}));
			    
			    sqls.add(CandiBase.F_getActionSQL("A000002", newAuid)); //引导
			    sqls.add(CandiBase.F_getActionSQL("A000001", newAuid)); //欢迎来到ACE
			    
			    if(addMode==0){
			    	//通知短信
			    	if(ACE.ONLINE) sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ACE.ADMIN_PHONE, String.format(ACE.TXT_SMS_ADMIN_HC, new Object[]{companyName, nickName, "注册ACE"}), ACE.SMS_TYPE_ADMIN}));
			    }
	
			}
			
			if(DaoUtil.dbExe(sqls).getResult()){
				return user;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static BUser editAUser(String auid, String mmGroup, String loginName, String _nickName, String fullName, String openid, String face, String area, String sex, String comPhone, String job, String mmFlag, String companyName, String email, String cardnum, String sidPhoto1, String sidPhoto2, String nameCardPhoto1, String nameCardPhoto2, String memo, BMMOrgan mm){
		try{
			String nickName = HIUtil.isEmpty(_nickName, "t".concat(auid.substring(20)));
			BUser user = new BUser(mmGroup, auid, loginName, mm.getMid(), mm.getAmid(), nickName, face, sex);
			
			List<String> sqls = new ArrayList<String>();
			sqls.add(ComBeanI_DataTable.sqlToZ("B_UserLogin", "编辑用户信息", "c1="+auid)); // ....c31座机 c32邮箱 c33职位
			sqls.add(String.format(YSSQL.LWUserLogin_EDIT, new Object[]{loginName, nickName, memo, fullName, face, sex, area, openid, comPhone, email, job, cardnum, auid}));
			if(mmGroup.equals(Constants.USER2_MMGROUP)){ // HR c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
				sqls.add(ComBeanI_DataTable.sqlToZ("B_HR", "编辑用户信息", "c1="+auid));
			    sqls.add(String.format(YSSQL.LWHR_EDIT, new Object[]{nickName, sex, loginName, email, comPhone, companyName, job, fullName,cardnum,sidPhoto1,sidPhoto2,nameCardPhoto1,nameCardPhoto2, auid}));
			}else if(mmGroup.equals(Constants.USER3_MMGROUP)){ // 猎头c2,c3,c4昵称,c5,c6,c7,c8,c9,c14,c16,c18,c33真实姓名,c34,c35,c36,c37,c38
				sqls.add(ComBeanI_DataTable.sqlToZ("B_Hunter", "编辑用户信息", "c1="+auid));
			    sqls.add(String.format(YSSQL.LWHunter_EDIT, new Object[]{nickName, sex, loginName, email, comPhone, companyName, area, mmFlag, fullName,cardnum,sidPhoto1,sidPhoto2,nameCardPhoto1,nameCardPhoto2, auid}));
			}		
			if(DaoUtil.dbExe(sqls).getResult()){
				return user;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8917658913528347968L;

	/** 注册新用户  **/
	private LWResult ServiceDO_registNewUser(){
		LWResult result = null;
		try{
			result = new LWResult(LWResult.FAILURE, "registNewUser is not defined");
			
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, "异常");
		}
		return result;
	}

	private LWResult F_sendVerifyEmail(String url, String email){
		LWResult result = null;
		String memo = "设置公司邮箱";
		try{
			List<String> sqls = new ArrayList<String>();
			if(isHR()){
				sqls.add(String.format(YSSQL.LWHR_EditEmail, new Object[]{ email, auid, }));
			}else if(isHC()){
				sqls.add(String.format(YSSQL.LWHunter_EditEmail, new Object[]{ email, auid, }));
			}
			sqls.add(String.format(YSSQL.LWUserLogin_EditEmail, new Object[]{ email, auid, }));
			
			//后台新增的用户（addMode=1）需要生成随机密码
			List<Object> list = dbQuery("select c2 phone, NVL(c40,0) addMode from B_UserLogin where c1="+auid);
			Object user = list.get(0);
			String phone = this.getDBString(user, "phone");
			String password = null;
			int addMode = this.getDBInt(user, "addMode");
			if(addMode==1){
			    password = RandomCode(2, 6).toLowerCase();
			    loggerM(ComBeanLogType.TYPE_ADD, "重发验证邮箱 loginPhone="+phone+"   email="+email+"    password:"+password);
			    sqls.add(String.format(YSSQL.LWUserLogin_UpdatePSWD, new Object[]{DES.md5(password), auid}));
			}
			
			String sendEmailResult = F_sendEmail(true, url, auid, phone, password, email);
			if(sendEmailResult.equals("SUCCESS")){
				if(dbExe(sqls)){
					sqls.clear();
				    result = new LWResult(LWResult.SUCCESS, memo+"成功");
			        loggerM(ComBeanLogType.TYPE_ADD, memo);
				}else{
					result = new LWResult(LWResult.FAILURE, memo+"失败");
				}
			}else{
				sqls.clear();
			    result = new LWResult(LWResult.FAILURE, sendEmailResult);
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	
	public String YY_sendEmail() {
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		//List<Object> list = HIUtil.dbQuery("select c1,c2,c3,c32 from B_UserLogin a where c1 in (select c3 from B_InviteCode where c4=0) and instr('gmail.com,qq.com,sina.com,sina.cn,sohu.com,126.com,188.com,163.com,163.net,263.net,3721.com,tom.com,yeah.net,mail.com,googlemail.com,hotmail.com,msn.com,live.com,yahoo.com,aol.com,ask.com,0355.com',substr(a.c32, instr(a.c32,'@')+1))>0");
	    List<Object> list = HIUtil.dbQuery("select c1,c2,c3,c32 from B_UserLogin a where c32='"+this.getParameter("email")+"'");
		if(list!=null && list.size()>0){
	    	for(Object o: list){
	    		String phone = DBUtil.getDBString(o, "c2");
	    		String email = DBUtil.getDBString(o, "c32");
				String userAuid = DBUtil.getDBString(o, "c1");
				//HR验证邀请码：需要通过发送验证邮件
				System.out.println(phone + "    " + email);

			    String password = RandomCode(2, 6).toLowerCase();
			    loggerM(ComBeanLogType.TYPE_ADD, "ADMIN重发验证邮箱 loginPhone="+phone+"   email="+email+"    password:"+password);
				try {
					List<String> sqls = new ArrayList<String>();
					sqls.add(String.format(YSSQL.LWUserLogin_UpdatePSWD, new Object[]{DES.md5(password), userAuid}));
					sqls.add(String.format("update B_InviteCode set c4=c4-1 where c3='%s'", new Object[]{userAuid}));
				    sqls.add(String.format("update B_UserLogin set c9=%s, c54=sysdate, c55=%s where c1='%s'", new Object[]{ACE.USER_STATUS_VALID, ACE.USER_CHECK_STATUS_CHECKING, userAuid}));
				    sqls.add(String.format("update B_HR set c17=%s, c18=%s where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_CHECKING, ACE.USER_STATUS_VALID, userAuid}));
					String sendEmailResult = F_sendEmail(false, "http://www.ace-elite.com/acehr", userAuid, phone, password, email);
					if(sendEmailResult.equals("SUCCESS")){
						if(dbExe(sqls)){
							System.out.println(phone + "    " + email + "  成功");
							result = new LWResult(LWResult.SUCCESS, "成功"+password);
						}else{
							System.out.println(phone + "    " + email + "  失败");
							result = new LWResult(LWResult.SUCCESS, "失败");
						}
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		}else{
			result = new LWResult(LWResult.SUCCESS, "无记录");
		}
		return APPRETURN(result);
	}

	// 发送验证邮件
	public static String F_sendEmail(boolean comLimitFlag, String url, String userAuid, String phone, String password, String email) throws MessagingException{

	    System.out.println("[HIService.F_sendEmail] 重发验证邮箱 loginPhone="+phone+"   email="+email+"    password:"+password==null?"NULL":password);
		
	    if(comLimitFlag && "gmail.com,qq.com,sina.com,sina.cn,sohu.com,126.com,188.com,163.com,163.net,263.net,3721.com,tom.com,yeah.net,mail.com,googlemail.com,hotmail.com,msn.com,live.com,yahoo.com,aol.com,ask.com,0355.com,".indexOf(email.split("@")[1])>0){
	    	System.out.println("[HIService.F_sendEmail] " + phone + " 请使用公司邮箱注册 ++++++++++++++++++++++" + email);
	    	return ACE.TXT_EMAIL_NOTCOM;
		}
		
		String domain = url.substring(0, url.indexOf("/aceh"));
		domain = domain.substring(0, url.lastIndexOf("/"));
		long time = (new Date()).getTime();
    	StringBuilder emailContent = new StringBuilder()  
        .append("<html><body style=\"margin:0px; color:#565656; font-family:微软雅黑;\">")
//        .append("<div style=\"background-image:url(http://www.ace-elite.com/doc/images/email_top.png) norepeat; background-size:100%; vertical-align:top;\">")  
		.append("<div>")
        .append("<a href='www.ace-elite.com' target='blank'><div style=\"height:80px\"><br><br></div></a>")
		.append("</div>")
		.append("<div><span style=\"color: #565656;font-size:18px;font-weight: bold\">").append(ACE.TXT_EMAIL_SLOGAN).append("</span></div>")
		.append("<div><a style=\"color: #565656;font-size:14px;font-weight: bold\" href=\"www.ace-elite.com\">www.ace-elite.com</a></div>")
        .append("<div style=\"height:40px\"></div><div>")    
        .append("您的登录账户为: <b>"+HIUtil.isEmpty(phone, email)+"</b>")
        .append(HIUtil.isEmpty(password) ? "" : "<span style=\"margin-left:20px\">密码为：</b>"+password+"</b>")
        .append("</div><div style=\"height:20px\"></div>")   
        .append("请点击以下链接验证您的邮箱地址<br/>")  
        .append("<a style=\"color: #FEA008;font-weight: bold;font-size: larger;font-family: cursive;\" href=")  
        .append(url+"/user_verifyemail_result.html?"+dES.encrypt(time+"`"+userAuid+"`"+email))  
        .append(">")   
        .append(url+"/user_verifyemail_result.html?"+dES.encrypt(time+"`"+userAuid+"`"+email))  
        .append("</a>")   
        .append("</span><div style=\"height:20px\"></div>")
        .append("<span style=\"color: #A0A0A0;\">如果以上链接无法访问,请将该网址复制并粘贴至新的浏览器窗口中<span><br/>")
        .append("<div style=\"height:100px\"></div>")
        .append("<div style=\"color: #A0A0A0;font-size:12px;\">如果您错误地收到了此电子邮件，请忽略此封邮件</div>")
        //.append("<div style=\"height:50px;background-image:url(http://www.ace-elite.com/doc/images/email_bottom.png); background-size:90%; vertical-align:top;\"></div>")  
        .append("</div></body></html>");
		return EmailUtil.sendEmailFile(email,"欢迎使用ACE招聘平台!", emailContent.toString(),"");
	}

	private String getVersion(){
		String code = this.getParameter("code");
		List<Object> list = dbQuery(String.format("select c1 code, c2 version, c3 url from IS_AppVersion where c1='%s'", new Object[]{code}));
		StringBuilder s = new StringBuilder("");
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			s.append("\"code\":").append(getDBString(o, "code"))
			.append(",\"version\":\"").append(getDBString(o, "version")).append("\"")
			.append(",\"url\":\"").append(getDBString(o, "url")).append("\"");
		}
		return s.toString();
	}

	/** 生成user json **/
	private String F_toLoginJson(Object user){
		StringBuilder s = new StringBuilder();
		s.append("{\"auid\":\"").append(DBUtil.getDBString(user, "auid")).append("\"")
		 .append(",\"amid\":\"").append(DBUtil.getDBString(user, "amid")).append("\"")
		 .append(",\"mid\":\"").append(DBUtil.getDBString(user, "mid")).append("\"")
		 .append(",\"mmGroup\":\"").append(DBUtil.getDBString(user, "mmGroup")).append("\"")
         .append("}");
		return s.toString();
	}
	/** 生成user json **/
	private String F_toRegistJson(BUser user){
		StringBuilder s = new StringBuilder();
		s.append("{\"auid\":\"").append(user.getAuid()).append("\"")
		 .append(",\"amid\":\"").append(user.getAmid()).append("\"")
		 .append(",\"mid\":\"").append(user.getMid()).append("\"")
		 .append(",\"mmGroup\":\"").append(user.getMmGroup()).append("\"")
         .append("}");
		return s.toString();
	}
	/** 生成user json **/
	private static String F_toUserJson(Object o){
		StringBuilder s = new StringBuilder();
		if(o!=null){
			int mmGroup = DBUtil.getDBInt(o, "mmGroup");
			int power = DBUtil.getDBInt(o, "power")==ACE.USER_CHECK_STATUS_CHECKED ? 1:0;
			s.append("{\"user\":{")
			 .append("\"logintime\":\"").append((new Date()).getTime()).append("\"") //登录时间
			 .append(",\"auid\":\"").append(DBUtil.getDBString(o, "auid")).append((new Date()).getTime()).append("\"")
			 .append(",\"phone\":\"").append(DBUtil.getDBString(o, "phone")).append("\"")
			 .append(",\"face\":\"").append(DBUtil.getDBString(o, "face")).append("\"")
			 .append(",\"nickName\":\"").append(DBUtil.getDBString(o, "nickName")).append("\"")
			 .append(",\"fullName\":\"").append(DBUtil.getDBString(o, "fullName")).append("\"")
			 .append(",\"signing\":\"").append(DBUtil.getDBString(o, "signing")).append("\"")
			 .append(",\"sex\":\"").append(DBUtil.getDBString(o, "sex")).append("\"")
			 .append(",\"email\":\"").append(DBUtil.getDBString(o, "email")).append("\"")
			 .append(",\"province\":\"").append(DBUtil.getDBString(o, "province")).append("\"")
			 .append(",\"mmGroup\":\"").append(mmGroup).append("\"")
			 .append(",\"mmGroupDesc\":\"").append(toMMGroupDesc(mmGroup)).append("\"")
			 .append(",\"mid\":").append(DBUtil.getDBString(o, "mid")).append("")
			 .append(",\"amid\":\"").append(DBUtil.getDBString(o, "amid")).append("\"")
			 .append(",\"mmName\":\"").append(DBUtil.getDBString(o, "mmName")).append("\"")
			 .append(",\"mmType\":").append(DBUtil.getDBInt(o, "mmType")).append("")
			 .append(",\"power\":").append(power).append("")
			 .append("},");
			s.append("\"server\":{")
			 .append("\"LW_SERVER\":\"").append(LWServer.getUrlByAUID(DBUtil.getDBString(o, "auid"))).append("\"") //LW服务器url
			 .append(F_serverParams(mmGroup))
			 .append("},");
			s.append("\"appModules\":[")
			.append(F_appModuleParams(mmGroup))
			 .append("],");
			s.append("\"appModuleDetails\":[")
			.append(F_appModuleDetailParams(mmGroup))
			 .append("]");
			s.append("}");
		}
		return s.toString();
	}
	
	private static String F_serverParams(int mmGroup){
		StringBuilder s = new StringBuilder();
		s.append(",\"FACE_PIC_URL\":\"").append(com.svv.dms.web.Constants.FILESERVER_URL+"FACE/").append("\"")
		 .append(",\"PIC_MAX_NUM\":\"").append(PIC_MAX_NUM(mmGroup)).append("\"")
		 .append(",\"AVI_MAX_NUM\":\"").append(AVI_MAX_NUM(mmGroup)).append("\"")
		 .append(",\"PIC_MAX_SIZE\":\"").append(PIC_MAX_SIZE(mmGroup)).append("\"")
		 .append(",\"AVI_MAX_SIZE\":\"").append(AVI_MAX_SIZE(mmGroup)).append("\"")
		 .append(",\"PIC_MAX_SIZE_X\":\"").append(PIC_MAX_SIZE_X(mmGroup)).append("\"")
		 .append(",\"PIC_MAX_SIZE_Y\":\"").append(PIC_MAX_SIZE_Y(mmGroup)).append("\"")
		 .append(",\"JPUSH_APPKEY\":\"").append(Constants.JPUSH_APPKEY).append("\"")
		 .append(",\"JPUSH_SECRET\":\"").append(Constants.JPUSH_SECRET).append("\"")
		 .append(",\"YOUKU_APPKEY\":\"").append(Constants.YOUKU_APPKEY).append("\"")
		 .append(",\"YOUKU_SECRET\":\"").append(Constants.YOUKU_SECRET).append("\"")
		 .append(",\"YOUKU_ACCESS_TOKEN\":\"").append(Constants.YOUKU_ACCESS_TOKEN).append("\"")
		 .append(",\"YOUKU_CALLBACK_URL\":\"").append(Constants.YOUKU_CALLBACK_URL).append("\"")
		 .append(",\"YOUKU_LOGINNAME\":\"").append(Constants.YOUKU_LOGINNAME).append("\"")
		 .append(",\"YOUKU_PASSWORD\":\"").append(Constants.YOUKU_PASSWORD).append("\"")
		 .append(",\"HUANXIN_APPKEY\":\"").append(Constants.HUANXIN_APPKEY).append("\"")
		 .append(",\"HUANXIN_CLIENTID\":\"").append(Constants.HUANXIN_CLIENTID).append("\"")
		 .append(",\"HUANXIN_SECRET\":\"").append(Constants.HUANXIN_SECRET).append("\"")
		 ;
		return s.toString();
		
	}

	private static String F_appModuleParams(int mmGroup){
		StringBuilder s = new StringBuilder();
		List<Object> list = HIUtil.dbQuery(String.format(YSSQL.LWAppModule_QueryByRole, new Object[]{mmGroup}));
		if(list!=null && list.size()>0){
			for(Object o: list){
				s.append("{")
				 .append("\"moduleID\":\"").append(DBUtil.getDBString(o, "moduleID")).append("\"")
				 .append("\"moduleType\":").append(DBUtil.getDBString(o, "moduleType"))
				 .append("\"moduleSeq\":").append(DBUtil.getDBString(o, "moduleSeq"))
				 .append("\"title\":\"").append(DBUtil.getDBString(o, "title")).append("\"")
				 .append("},");
			}
			s.deleteCharAt(s.length()-1);
		}
		return s.toString();
	}

	private static String F_appModuleDetailParams(int mmGroup){
		StringBuilder s = new StringBuilder();
		List<Object> list = HIUtil.dbQuery(String.format(YSSQL.LWAppModuleDetail_QueryByRole, new Object[]{mmGroup}));
		if(list!=null && list.size()>0){
			for(Object o: list){
				s.append("{")
				 .append("\"id\":").append(DBUtil.getDBLong(o, "dataid"))
				 .append("\"moduleID\":\"").append(DBUtil.getDBString(o, "moduleID")).append("\"")
				 .append("\"mdTitle\":\"").append(DBUtil.getDBString(o, "mdTitle")).append("\"")
				 .append("\"moduleType\":").append(DBUtil.getDBString(o, "moduleType"))
				 .append("\"mdImg\":\"").append(DBUtil.getDBString(o, "mdImg")).append("\"")
				 .append("\"mdParam\":\"").append(DBUtil.getDBString(o, "mdParam")).append("\"")
				 .append("\"mdParam2\":\"").append(DBUtil.getDBString(o, "mdParam2")).append("\"")
				 .append("\"mdParam3\":\"").append(DBUtil.getDBString(o, "mdParam3")).append("\"")
				 .append("\"mdAction\":\"").append(DBUtil.getDBString(o, "mdAction")).append("\"")
				 .append("},");
			}
			s.deleteCharAt(s.length()-1);
		}
		return s.toString();
	}

}
