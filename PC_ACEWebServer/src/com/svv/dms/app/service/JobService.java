package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;

import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class JobService extends LWBaseBean {

	public String JobBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String JobMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String JobSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	//职位信息
	public String jobInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = SerivceDO_jobInfo();
		if(isM()) result = SerivceDO_jobInfo();
		return APPRETURN(result);
	}
	//职位列表
	public String list(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_JobList_HR();
		if(isM()) result = ServiceDO_JobList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览职位列表");
		return APPRETURN(result);
	}
	//JD列表
	public String jdlist(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_JDList_HR();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览JD列表");
		return APPRETURN(result);
	}
	// 职位预览
	public String jobdetail(){
		System.out.println("[JobService.jobdetail]=======================================");
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "职位预览";
		try{
			String jobid = this.getParameter("jobid");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(YSSQL.LWJob_QueryByC(jobid, "", "", "", "", "", ""));
			if(list!=null && list.size()>0){
				for(Object o: list){
					int isNeed = this.getDBString(o, "c16").equals("1") ? 1 :0;
					s.append(String.format(HTML, new Object[]{// c18JD上传日期
							this.getDBString(o, "c1"),
							this.getDBDataParam(o, "c7", "", "", ""),
							this.getDBString(o, "c15").length() >0 ? "" : "gone",
							this.getDBString(o, "c15"),
							this.getDBString(o, "c11"),
							this.getDBDataParam(o, "c10", "", "", "").equals("不限") ? "学历" + this.getDBDataParam(o, "c10", "", "", "") : this.getDBDataParam(o, "c10", "", "", ""),
									this.getDBDataParam(o, "c9", "", "", "").equals("不限") ? "工作经验" + this.getDBDataParam(o, "c9", "", "", "") : this.getDBDataParam(o, "c9", "", "", ""),
							isNeed ==1 ? "" : "gone",
							this.getDBString(o, "c12").length()>0 ? "" :"gone",
							"\""+this.getDBString(o, "c12")+"\"",
							this.getDBString(o, "c8").length()>0 ? "" :"gone",
							this.getDBString(o, "c8"),
							this.getDBString(o, "c13").length()>0 ? "" :"gone",
							HIUtil.toHtmlStr(getDBString(o, "c13")),//公司介绍
							this.getDBString(o, "c14").length()>0 ? "" :"gone",	
						    HIUtil.toHtmlStr(getDBString(o, "c14")),//公司介绍						
					}));
				}
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"无数据");
			}
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	public String editForm(){
		logger.debug("[editForm]====================================");
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result;
		String objectMode = this.getParameter("objectMode");
		String jobid = this.getParameter("jobid");		
		String HTML = ComBeanAPPModule.getHtml(objectMode);
		Object[] data = {"", "", "", "", "",""};///////////////////改的
		if(jobid.length() >0){
			List<Object> list = dbQuery(YSSQL.LWJob_QueryByC(jobid, myamid, auid, "", "", "", ""));
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				int i = 0;
				String[] xinzi = getDBString(o, "c11").split("-");
				
				data[i++] = getDBString(o, "c1");////////////参数
				data[i++] = getDBString(o, "c7");
				data[i++] = getDBString(o, "c8");
				data[i++] = getDBString(o, "c9");
				data[i++] = getDBString(o, "c10");
				data[i++] = xinzi[0];
				data[i++] = xinzi.length>1?xinzi[1] :"";
				data[i++] = getDBString(o, "c12");
				data[i++] = getDBString(o, "c13");
				data[i++] = getDBString(o, "c14");
				data[i++] = getDBString(o, "c16");
				data[i++] = getDBInt(o, "c16")==1?"checked":"";
			}
		}
	
		String html = String.format(HTML, data);      
        result = new LWResult(LWResult.SUCCESS, html);
		return APPRETURN(result);
	}
	//添加职位
	public String add(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddJob();
		if(isHR()) result = ServiceDO_AddJob(); 
		return APPRETURN(result);
	}
	
	public String addJD(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "新增JD";
		try{
			String jdFileName = this.getParameter("jdFileName");
			String jdFile = this.getParameter("jdFile");
			
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWJob_JD, new Object[]{
					mymid,
					jdFileName,
					auid,
					130001,
					1,
					myamid,
					mymid,
					jdFile,
					"",//处理状态
					"您的职位将在1个工作日内发布",//状态说明
					1,//是否是JD
			}));
			
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	//更新职位
	public String edit(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_editJob();
		return APPRETURN(result);
	}
	
	public String online(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "职位上线";
		try{
			String jobid = this.getParameter("jobid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(this.sqlToZ("B_Job", memo, "dataid="+jobid));
			sqls.add(String.format(YSSQL.LWJob_UpdateStatus, new Object[]{1, jobid}));
			
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	public String underline(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "职位下线";
		try{
			String jobid = this.getParameter("jobid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(this.sqlToZ("B_Job", memo, "dataid="+jobid));
			sqls.add(String.format(YSSQL.LWJob_UpdateStatus, new Object[]{0, jobid}));
			
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	public String uploadFile(){
		System.out.println("[JobServiceBean.uploadFile]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("JOB");
	}
	public String uploadPic(){
		System.out.println("[JobServiceBean.uploadPic]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("JOB");
	}
	public String listCount(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_Job_List_Count();
        loggerM(ComBeanLogType.TYPE_QUERY, "我的职位列表统计数量");
		return APPRETURN(result);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;
	private LWResult ServiceDO_JobList_HR(){
		System.out.println("[CandiServiceBean.ServiceDO_JobList_HR]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String status = this.getParameter("status");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = dbQuery(YSSQL.LWJob_QueryByC("", myamid, auid, "0", status, FIRSTID, LASTID));
			int n = 0;
			if(list!=null && list.size()>0){// online_ edit_ underline_
				for(Object o : list){
					if(objectMode.equals("H00103-4")){
						if(n == maxcount) break;
						s.append(String.format(HTML, new Object[]{
								this.getDBString(o, "dataid"),
								this.getDBString(o, "dataid"),
								this.getDBString(o, "dataid"),
								this.getDBString(o, "c1"),
								this.getDBString(o, "c11", "", "", "K"),
								this.getDBDate(o, "istdate", "yyyy-MM-dd"),						
						}));
						
					}else{
						int jobStatus = this.getDBInt(o, "c4");
						String[] btn11Class = {"gone","gone","gone"}, btn11 = {"","",""};
						if(jobStatus == 0){
							int m = 0;
							btn11Class[m] = "online_"; btn11[m] = "上线";
							m++;
							btn11Class[m] = "edit_"; btn11[m] = "编辑";
							m++;
						}else{
							int m = 0;
							m++;
							btn11Class[m] = "edit_"; btn11[m] = "编辑";
							m++;
							btn11Class[m] = "underline_"; btn11[m] = "下线";
						}
						if(n == maxcount) break;
						s.append(String.format(HTML, new Object[]{
								this.getDBString(o, "dataid"),
								this.getDBString(o, "c1"),
								this.getDBString(o, "c11")+"K",
								this.getDBDataParam(o, "c10", "", "", "").equals("不限") ? "学历" + this.getDBDataParam(o, "c10", "", "", "") : this.getDBDataParam(o, "c10", "", "", ""),						
								this.getDBDataParam(o, "c9", "", "", "").equals("不限") ? "工作经验" + this.getDBDataParam(o, "c9", "", "", "") :this.getDBDataParam(o, "c9", "", "", ""),						
								this.getDBString(o, "c15"),
								this.getDBDate(o, "istdate", "yyyy-MM-dd"),
								btn11Class[0], // 样式
								btn11[0], // 文本
								btn11Class[1], // 样式
								btn11[1], // 文本
								btn11Class[2], // 样式
								btn11[2], // 文本							
						}));
					}
					n++;
					
				}
			}
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}
	// JD列表
	private LWResult ServiceDO_JDList_HR(){
		System.out.println("[CandiServiceBean.ServiceDO_JDList_HR]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String status = this.getParameter("status");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = dbQuery(YSSQL.LWJob_QueryByC("", myamid, auid, "1", status, FIRSTID, LASTID));
			int n = 0;
			if(list!=null && list.size()>0){
				for(Object o : list){
					if(n == maxcount) break;
					s.append(String.format(HTML, new Object[]{// c18JD上传日期
							this.getDBString(o, "dataid"),
							this.getDBString(o, "c1"),
							this.getDBDate(o, "c18", "yyyy-MM-dd"),	
							this.getDBString(o, "c20"),							
					}));
					n++;
				}
			}
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}
	private LWResult ServiceDO_JobList_M(){
		System.out.println("[CandiServiceBean.ServiceDO_HRList_M]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //job1，JD2
			String keyword = this.getParameter("keyword"); //关键字
			/*
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			this.page = 1;//有lastid, page永远为1//////////////////////////////////this.getParameter("i8", 1);*/
			String FIRSTID = "";
			String LASTID = "";
			this.page = this.getParameter("i8", 1);
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			List<Object> list = null;
			if(queryMK==1) //job
                list = dbQuery(YSSQL.LWJob_QueryByC("", "", "", "0", "", FIRSTID, LASTID));
			else if(queryMK==2) //JD
                list = dbQuery(YSSQL.LWJob_QueryByC("", "", "", "1", "", FIRSTID, LASTID));
			int n = 0;

			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String html = null;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				int index = 100;
            	firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					index++;
					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
					
					//jd
					if(queryMK==2){
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "dataid"), //DATA
								"", //DATA1
								this.getDBString(o, "c1"), // 李大志
								this.getDBInt(o, "c4")==1?"在线":"下线",
								this.getDBDate(o, "uptdate", DATETIME_FORMAT), // 拉黑于2016/12/23 23:01
						}));
					}else{
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "dataid"), //DATA
								"", //DATA1
								this.getDBString(o, "c1"), // 李大志
								this.getDBInt(o, "c4")==1?"在线":"下线",
								this.getDBDate(o, "uptdate", DATETIME_FORMAT), // 申请于2016/12/23 23:01
						}));
						
					}
					
					n++;
				}
			}
			if(n>0) return new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "", s.toString());
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}
	// 我的职位列表统计
	private LWResult ServiceDO_Job_List_Count(){
		System.out.println("[jobserviceBean.ServiceDO_Job_List_Count]=======================================");
		String memo = "我的职位统计数量查询";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			list = dbQuery(YSSQL.LWJob_QueryByC(auid, myamid));
			
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(String.format(HTML, new Object[]{
                            "招聘中",						
							getDBInt(o, "sum1") > 0 ? this.getDBString(o, "sum1") : "",									
							"已下线",							
							getDBInt(o, "sum2") > 0 ? this.getDBString(o, "sum2") : "",
					}));
					n++;
				}
			}

			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}	
	private LWResult SerivceDO_jobInfo() {
		LWResult result;
		String memo = "职位信息";
		try{
			List<Object> list = new ArrayList<Object>();
			String jobid = this.getParameter("jobid");
			StringBuilder s = new StringBuilder("");
			if(isM()){
				list = dbQuery(YSSQL.LWJob_QueryByC(jobid, "", "", "", "", "", ""));
			}
			if(isHR()){
				list = dbQuery(YSSQL.LWJob_QueryByC(jobid, myamid, auid, "", "", "", ""));
			}
			if(list!=null && list.size()>0){
				for(Object o: list){
					String[] xinzi = getDBString(o, "c11").split("-");
					if(isM()){
						String hrAuid = this.getDBString(o, "c2");
						List<Object> names = dbQuery(String.format("select h.c4 as hrName,o.c2 as companyName from B_HR h,B_MMORGAN o where h.c2=o.c1 and h.c1='%s'", new Object[]{hrAuid}));
						if(names!=null&&names.size()>0){
							for (Object name : names) {
								s.append(this.getDBString(name, "companyName"))
								.append(Constants.SPLITER).append(this.getDBString(name, "hrName"))
								.append(Constants.SPLITER).append(hrAuid);
							}
						}
						s.append(Constants.SPLITER);
					}
					s.append(this.getDBString(o, "c1"))
					  .append(Constants.SPLITER).append(this.getDBString(o, "c7"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c8"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c9"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c10"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c11"))
					 .append(Constants.SPLITER).append(xinzi[0])
					 .append(Constants.SPLITER).append(xinzi.length>1?xinzi[1] :"")
					 .append(Constants.SPLITER).append(this.getDBString(o, "c12"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c13"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c14"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c15"))
					 .append(Constants.SPLITER).append("")
					 .append(Constants.SPLITER).append(this.getDBString(o, "c16"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c17"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c21"));
					
				}
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"无数据");
			}
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	private LWResult ServiceDO_AddJob() {
		LWResult result;
		String memo = "新增职位";
		
		try{
			String[] vv = F_formParam();
			
			int i = 0;
			String jobName = vv[i++];
			String hangye= vv[i++];
			String department = vv[i++];
			String gzjy = vv[i++];
			String xlyq = vv[i++];i++;
			String xinzifw = vv[i++]+"-"+vv[i++];
			String zhiyeyh = vv[i++];
			String zhiyems = vv[i++];
			String rzyq = vv[i++];
			String jobAdress = vv[i++];
			if(isM()){
				if(jobAdress.equals("0")){
					jobAdress = vv[i++];
				}else{
					i++;
				}
			}
			String isSelected = vv[i++];
			String hrmid = "";
			String hramid = "";
			String hrAuid = this.getParameter("hrAuid");
			
			if(isHR()){
				hrmid = mymid;
				hramid = myamid;
				hrAuid = auid;
			}
			if(isM()){
				List<Object> list = dbQuery(String.format("select a.c2,a.c3 from B_HR a where a.c1='%s' and a.c17='%s'", new Object[]{hrAuid,ACE.USER_CHECK_STATUS_CHECKED}));
				if(list==null || list.size()<=0){
					return result = new LWResult(LWResult.FAILURE, memo+"该HR未通过审核，请审核完毕再添加职位！");
				}
				if(list!=null&&list.size()>0){
					for (Object o : list) {
						hrmid = getDBString(o, "c3");
						hramid = getDBString(o, "c2");
					}
				}
			}
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWJob_Add, new Object[]{
					hrmid,
					jobName,
					hrAuid,
					130001,
					1,
					hramid,//企业amid
					hrmid,//企业dataid
					hangye,
					department,
					gzjy,
					xlyq,
					xinzifw,
					zhiyeyh,
					zhiyems,
					rzyq,
					jobAdress,
					isSelected,
			}));
			sqls.add(String.format("update B_HR set c19=NVL(c19,0)+(%s) where c1='%s'", new Object[]{1, hrAuid}));
			
			if(isHR()){
				//ADMIN通知短信
				if(ACE.ONLINE) sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ACE.ADMIN_PHONE, String.format(ACE.TXT_SMS_ADMIN_HR, new Object[]{ComBeanMMOrgan.getSimpName(myamid), ComBeanLoginUser.getUserName(auid), "新增职位『"+jobName+"』"}), ACE.SMS_TYPE_ADMIN}));
			}
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	private LWResult ServiceDO_editJob() {
		LWResult result;
		String memo = "编辑职位";
		try{
			String jobid = this.getParameter("jobid");
			String[] vv = F_formParam();
			int i = 0;
			String jobName = vv[i++];
			String hangye= vv[i++];
			String department = vv[i++];
			String gzjy = vv[i++];
			String xlyq = vv[i++];
			i++;
			String xinzifw = vv[i++]+"-"+vv[i++];
			String zhiyeyh = vv[i++];
			String zhiyems = vv[i++];
			String rzyq = vv[i++];
			String jobAdress = vv[i++];
			if(jobAdress.equals("0")){
				jobAdress = vv[i++];
			}else{
				i++;
			}
			String isSelected= vv[i++];		
			String hrmid = "";
			String hramid = "";
			String hrAuid = this.getParameter("auid");
			if(isHR()){
				hrmid = mymid;
				hramid = myamid;
				hrAuid = auid;
			}
			if(isM()){
				List<Object> list = dbQuery(String.format("select a.c2,a.c3 from B_HR a where a.c1='%s'", new Object[]{hrAuid}));
				if(list!=null&&list.size()>0){
					for (Object o : list) {
						hrmid = getDBString(o, "c3");
						hramid = getDBString(o, "c2");
					}
				}
			}
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWJob_Edit, new Object[]{
					jobName,
					hrAuid,
					130001,
					1,
					hramid,
					hrmid,
					hangye,
					department,
					gzjy,
					xlyq,
					xinzifw,
					zhiyeyh,
					zhiyems,
					rzyq,
					jobAdress,
					isSelected,
					jobid,
			}));

			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
}
