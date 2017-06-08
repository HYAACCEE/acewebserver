package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;

import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.EmailUtil;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class CandiProcessService extends CandiBase {
		
	public String CandiProcessCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiProcessMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiProcessBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiProcessSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	
	//特别推荐邮件发送
	public String recommendEmail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "特别推荐邮件发送";
		try{
			String hrAuid = this.getParameter("hrAuid");
			String url = com.svv.dms.web.Constants.WEB_URL;
			if(url.indexOf("118.")>0) url = "http://118.178.126.204/";
			if(url.indexOf("120.")>0) url = "http://www.acelite.com/";
		    List<Object> list = dbQuery(String.format("select b.c7 email, p.c8,p.c11,p.c13,p.c15,p.c21,p.c25,p.c39,p.c41,p.c42,p.c43/1000 yuexin_k from B_Candidate_Organ_Relation a, B_HR b, BO_Candidate p where a.c4=b.c1 and a.c1=p.c1 and a.c4='%s' and a.c5=1 and p.c5=%s", new Object[]{hrAuid, ACE.CANDI_ActiveStatus_ACTIVE}));
		    if(list!=null && list.size()>0){
		    	String email;
				email = getDBString(list.get(0), "email");
				if(!HIUtil.isEmpty(email)){
			    	StringBuilder emailContent = new StringBuilder()
			        .append("<html><body style=\"margin:0px; color:#565656; font-family:微软雅黑;\">")
                    .append("<div><a href='"+url+"'><img src=\"http://www.ace-elite.com/doc/images/email_top.png\" style=\"width:900px;border:0px;\"></a></div>")
			        .append("<div style=\"height:40px\"></div>")
					.append("<div><span style=\"color: #565656;font-size:18px;font-weight: bold\">本周特别推荐-ACE为您量身定制</span></div>")
			        .append("<div style=\"height:40px\"></div>");
				    for(Object o: list){
				    	emailContent.append("<div>")   
				        .append("<a href=\""+url+"/acehr/index.html\" style=\"text-decoration:none;\"><span style=\"margin-right: 30px; color:#4990B1; font-weight:bold; font-size:16px;\">").append(getDBString(o, "c41")).append(getDBString(o, "c42", "", " - ", "")).append("</span>") 
		                .append("<span style=\"display:inline-block; padding-left:10px; padding-right:10px; height:20px; border:1px solid #ef0000; background:#ef0000; color:#fefefe;\">推荐</span>")
				        .append("</a></div>")
				        .append("<div>")  
				        .append("<span style=\"color:#A0A0A0;font-size:12px;\">").append(getDBDataParam(o, "c13", "", "", "")).append(getDBString(o, "c11",""," | ","岁")).append(getDBDataParam(o, "c25", "", " | ", "")).append(getDBInt(o, "c39", "", " | ", "年工作经验")).append(getDBString(o, "yuexin_k", "", " | ", "K/月")).append(getDBString(o, "c15", "", " | ", "")).append("</span>")
				        .append("</div>")
				        .append("<div style=\"height:20px;\"></div>")
				        .append("<div style=\"width:660px; height:1px; background:#c0c0c0;\"></div>")
				        .append("<div style=\"height:20px;\"></div>");
				    	
					}
			    	emailContent.append("<div style=\"height:20px;\"></div>")
			        .append("<div><a href=\""+url+"/acehr/main.html\" style=\"text-decoration:none;\"><span style=\"display:inline-block; width:630px; height:20px; padding:15px; text-align:center; background:#FFA942; color:#ffffff; border:1px solid #FFA942;\">查看更多精选候选人</span></a></div>")
			        .append("<div style=\"height:20px;\"></div>")
			        .append("<div style=\"color: #909090;\">ACE平台每周都会精选看机会的优质候选人以满足雇主的要求<div>")
			        .append("<div style=\"height:20px;\"></div>")
					.append("<div style=\"height:100px\"></div>")
					.append("<div><a style=\"color: #565656;font-size:14px;font-weight: bold\" href=\"www.ace-elite.com\">www.ace-elite.com</a></div>")
			        .append("<div style=\"color: #A0A0A0;font-size:12px;\">如有任何问题请联系我们，我们将尽快为您解答。 电话：4006-2828-35</div>")
			        .append("<div style=\"height:20px;\"></div>")
                    .append("<div><img src=\"http://www.ace-elite.com/doc/images/email_bottom.png\" style=\"width:900px;border:0px;\"></div>")
			        .append("</body></html>");
					String sendEmailResult = EmailUtil.sendEmailFile(email, "【ACE】ACE为您特别推荐的候选人", emailContent.toString(),"");
					if(sendEmailResult.equals("SUCCESS")){
				        loggerM(ComBeanLogType.TYPE_QUERY, memo);
						result = new LWResult(LWResult.SUCCESS, memo+"成功");
					}else{
					    result = new LWResult(LWResult.FAILURE, sendEmailResult);
					}
				}	
		    }
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//精选候选人状态变更
	public String changeGoodStatus(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		int mk = this.getParameter("mk", -1);
		String memo = "精选候选人状态变更为"+mk;
		if(isM()){
			List<String> sqls = new ArrayList<String>();
			sqls.add(this.sqlToZ("B_Candidate", memo, "c1='"+cid+"'"));
			sqls.add(this.sqlToZ("BO_Candidate", memo, "c1='"+cid+"'"));
			if(mk==1){//加精
				sqls.add(String.format("update B_Candidate set c91=%s, c92=sysdate where c1='%s'", new Object[]{mk, cid}));
				sqls.add(String.format("update BO_Candidate set c91=%s, c92=sysdate where c1='%s'", new Object[]{mk, cid}));
			}
			if(mk==2){//备选
				sqls.add(String.format("update B_Candidate set c91=c91||%s where c1='%s'", new Object[]{mk, cid}));
				sqls.add(String.format("update BO_Candidate set c91=c91||%s where c1='%s'", new Object[]{mk, cid}));
			}
			if(mk==6){//移出精选
				sqls.add(String.format("update B_Candidate set c91=replace(c91,'1','%s') where c1='%s'", new Object[]{mk, cid}));
				sqls.add(String.format("update BO_Candidate set c91=replace(c91,'1','%s') where c1='%s'", new Object[]{mk, cid}));
			}
			if(mk==0){//移出备选
				sqls.add(String.format("update B_Candidate set c91=replace(c91,'2','') where c1='%s'", new Object[]{cid}));
				sqls.add(String.format("update BO_Candidate set c91=replace(c91,'2','') where c1='%s'", new Object[]{cid}));
			}
			sqls.add(String.format("update B_Candidate set c91=0 where c1='%s' and c91 is null", new Object[]{cid}));
			sqls.add(String.format("update BO_Candidate set c91=0 where c1='%s' and c91 is null", new Object[]{cid}));
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败！");
			}
		}
		return APPRETURN(result);
	}
	
	//添加候选人被邀请记录
	public String inviteLog(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_inviteLog_Add(); //======================================流程事件
		return APPRETURN(result);
	}
	
	//Inactive候选人
	public String inactive(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		String cid = this.getParameter("cid");
		String url = this.getParameter("url");
		String detail = this.getParameter("formContent", "(顾问未提交Inactive原因)");
		LWResult result = Process_inactive(cid, detail, url); //======================================流程事件
		return APPRETURN(result);
	}
	//申请入库
	public String stApply(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		String cid = this.getParameter("cid");
		LWResult result = Process_stApply(cid); //======================================流程事件
		return APPRETURN(result);
	}
	//申请Active
	public String activeApply(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		String cid = this.getParameter("cid");
		LWResult result = checkActiveNumLimit(auid);
		if(result==null){
		    result = P_doProcess(ACE.PROCESS_CODE_ACTIVE_APPLY, cid); //=========================================流程事件
		}
		return APPRETURN(result);
	}
	//HR签约状态
	public String hrPower(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "HR签约状态";
		try{
			String mmBizStatus = this.getSqlValue("select c6 mmBizStatus from B_MMOrgan where c1='"+myamid+"'", "mmBizStatus");
			result = new LWResult(LWResult.SUCCESS, mmBizStatus.equals(ACE.MM_BizStatus_CHECKED) ? "1":"0");
			loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//查看offer文件
	public String offerFile(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看offer文件";
		try{
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			List<Object> list = dbQuery(String.format("select a.c9 title, b.c8 detail from B_Candidate_Process a, B_CandiDate_Files b where a.c1=b.c1 and a.c4=b.c9 and a.c1='%s' and a.c5='%s' order by b.istdate desc", new Object[]{cid, pcid}));
			
			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "detail"));
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"无记录");
			}
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//查看详情（原因）
	public String detailInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看详情";
		try{
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			String tid = this.getParameter("tid");
			String processSeq = this.getParameter("pid");
			List<Object> list = null;
			if(tid.length()>0) list = dbQuery(String.format("select c12 title, c13 detail from B_Action where dataid=%s and c1='%s'", new Object[]{tid, cid}));
			else if(processSeq.length()>0) list = dbQuery(String.format("select c9 title, c11 detail from B_Candidate_Process_His where c1='%s' and c5='%s' and c38=%s", new Object[]{cid, pcid, processSeq}));			
			else if(pcid.length()>0) list = dbQuery(String.format("select c9 title, c11 detail from B_Candidate_Process where c1='%s' and c5='%s'", new Object[]{cid, pcid}));
			else list = dbQuery(String.format("select c12 title, c13 detail from B_Action where c1='%s' and c3=130001 order by dataid desc", new Object[]{cid}));
			
			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "title").replaceAll("\\[OPEN=(.+?)\\]", "")).append(Constants.SPLITER); //事件
				s.append(this.getDBString(o, "detail"));
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"无记录");
			}
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return APPRETURN(result);
	}
	// 候选人信息
	public String candidateInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看候选人信息";
		try{
			String candidateId = this.getParameter("candidateId");
			List<Object> list = dbQuery(String.format("select a.* from B_Candidate a  where a.c1='%s'", new Object[]{candidateId}));

			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "c61", "../doc/images/HC/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_m.png")).append(Constants.SPLITER);
				s.append(this.getDBString(o, "c8")).append(Constants.SPLITER); //名称
				s.append("("+this.getDBString(o, "c41")); //最近所在公
				s.append(this.getDBString(o, "c42", "", "/", "") +")").append(Constants.SPLITER); // LV
				s.append(this.getDBDataParam(o, "c13", "", "", "")).append(Constants.SPLITER); // 男
				s.append(this.getDBString(o, "c11").length() >0 ? this.getDBString(o, "c11") +"岁" : "").append(Constants.SPLITER); // 27岁
				s.append(this.getDBDataParam(o, "c25", "", "", "")).append(Constants.SPLITER);
				s.append(this.getDBString(o, "c39").length() >0 && !this.getDBString(o, "c39").equals(0) ? this.getDBString(o, "c39") +"年工作经验" :"").append(Constants.SPLITER); // 3年工作经验
				s.append(this.getDBString(o, "yuexin_k").length() >0 ? this.getDBString(o, "yuexin_k") +"K" : "").append(Constants.SPLITER); // 25K
				s.append("<img style=\"width:17px;height:17px\" src=\"../doc/images/HR/location.png\">" +this.getDBString(o, "c15")).append(Constants.SPLITER); // 上海
				s.append(this.getDBString(o, "c22")).append(Constants.SPLITER); //联系Email
				s.append(this.getDBString(o, "c21")).append(Constants.SPLITER); //联系电话（座机）
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}	
	//顾问信息
	public String hcInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看顾问信息";
		try{
			String hcid = this.getParameter("hcid");
			List<Object> list = dbQuery(String.format("select c2,c11,c31,c32,c12 from B_UserLogin a where a.c1='%s' and c9=130001", new Object[]{hcid}));

			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "c12", "../doc/images/HR/icon_touxiang.png")).append(Constants.SPLITER);
				s.append(this.getDBString(o, "c11")).append(Constants.SPLITER); //昵称
				s.append(this.getDBString(o, "c32")).append(Constants.SPLITER); //联系Email
				s.append(this.getDBString(o, "c2")).append(Constants.SPLITER); //手机
				s.append(this.getDBString(o, "c31")); //联系电话（座机）
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//HR信息
	public String hrInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看HR信息";
		try{
			String hrid = this.getParameter("hrid");
			List<Object> list = dbQuery(String.format("select a.*, b.c2 mmName from B_UserLogin a, B_MMOrgan b where a.c6=b.c1 and a.c1='%s'", new Object[]{hrid}));

			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "c12", "../doc/images/HR/icon_touxiang.png")).append(Constants.SPLITER);
				s.append(this.getDBString(o, "c11")).append(Constants.SPLITER); //昵称
				s.append(this.getDBString(o, "mmName")).append(Constants.SPLITER); //公司名称
				s.append(this.getDBString(o, "c32")).append(Constants.SPLITER); //联系Email
				s.append(this.getDBString(o, "c2")).append(Constants.SPLITER); //联系手机
				s.append(this.getDBString(o, "c31")); //联系电话（座机）
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//面试信息
	public String ivInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "查看面试信息";
		try{
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			String ivMK = this.getParameter("ivMK");
			List<Object> list = null;
			if(ivMK.equals("HR")){
				if(pcid.length()>0)
					list = dbQuery(String.format("select NVL(a.c14,-1) status, decode(a.c14,0,a.c6,b.c34) job, to_char(NVL(a.c7,sysdate),'yyyy-mm-dd hh24:mi') ivTime, to_char(a.c8,'yyyy-mm-dd hh24:mi') ivTime2, decode(a.c14,0,a.c9,c.c14) location, p.c8 cName from B_Candidate p, B_Candidate_Interview a, B_Candidate_Process b, B_MMOrgan c where p.c1=b.c1 and b.c31=c.c1 and p.c1=a.c1(+) and p.c1='%s' and b.c5='%s' and a.c18(+)=%s order by a.c14,a.dataid desc", new Object[]{cid, pcid, pcid}));
				else if(isHR())
					list = dbQuery(String.format("select NVL(a.c14,-1) status, decode(a.c14,0,a.c6,b.c34) job, to_char(NVL(a.c7,sysdate),'yyyy-mm-dd hh24:mi') ivTime, to_char(a.c8,'yyyy-mm-dd hh24:mi') ivTime2, decode(a.c14,0,a.c9,c.c14) location, p.c8 cName from B_Candidate p, B_Candidate_Interview a, B_Candidate_Process b, B_MMOrgan c where p.c1=b.c1 and b.c31=c.c1 and p.c1=a.c1(+) and p.c1='%s' and b.c4='%s' and a.c4(+)='%s' order by a.c14,a.dataid desc", new Object[]{cid, auid, auid}));
			}else if(ivMK.equals("HC")){
				if(isM()){
					list = dbQuery(String.format("select NVL(a.c14,-1) status, a.c6 job, to_char(NVL(a.c7,sysdate),'yyyy-mm-dd hh24:mi') ivTime, to_char(a.c8,'yyyy-mm-dd hh24:mi') ivTime2, a.c9 location, p.c8 cName from B_Candidate p, B_Candidate_Interview a where p.c1=a.c1 and p.c72=a.c7 and p.c1='%s' order by a.c14,a.dataid desc", new Object[]{cid}));
				}else{
					list = dbQuery(String.format("select NVL(a.c14,-1) status, a.c6 job, to_char(NVL(a.c7,sysdate),'yyyy-mm-dd hh24:mi') ivTime, to_char(a.c8,'yyyy-mm-dd hh24:mi') ivTime2, a.c9 location, p.c8 cName from B_Candidate p, B_Candidate_Interview a where p.c1=a.c1(+) and p.c72=a.c7(+) and p.c1='%s' order by a.c14,a.dataid desc", new Object[]{cid}));
				}
			}
			
			StringBuilder s = new StringBuilder();
			boolean currentFlag = false;
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				if(this.getDBString(o, "status").length()>0){
					currentFlag = this.getDBInt(o, "status")==0;
				}
				if(isM()){currentFlag = true;}
				if(ivMK.equals("HR")){
					s.append(this.getDBString(o, "cName"))
					 .append(Constants.SPLITER).append(currentFlag ? this.getDBString(o, "ivTime") : "")
					 .append(Constants.SPLITER).append(currentFlag ? this.getDBString(o, "ivTime2") : "")
					 .append(Constants.SPLITER).append(this.getDBString(o, "job"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "location"));
				}else{
					s.append(this.getDBString(o, "cName"))
					 .append(Constants.SPLITER).append(currentFlag ? this.getDBString(o, "ivTime") : "")
					 .append(Constants.SPLITER).append(this.getDBString(o, "location"));
				}
				result = new LWResult(LWResult.SUCCESS, s.toString());
		        loggerM(ComBeanLogType.TYPE_QUERY, memo);
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	//通用流程 [objectMode, cid, formParam(process,content)] ////////////////////////////////////////////////////////
	public String doProcess(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "";
		try{
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			String tid = this.getParameter("tid");
			String objectMode = this.getParameter("objectMode");
			String processCode = this.getParameter("process", ComBeanAPPModule.getNextModule(objectMode)); //流程编号////////在appModule中取					
			
			//关注、取消关注 不用认证用户
			if(!(processCode.equals(ACE.PROCESS_CODE_COLLECT) || processCode.equals(ACE.PROCESS_CODE_UNCOLLECT)
				  || processCode.equals(ACE.PROCESS_CODE_ADDBLACKLIST) || processCode.equals(ACE.PROCESS_CODE_DELBLACKLIST) )){
			    if(!ACCESS_CHECK(true, null, auid, mymid, myamid, M0)) return BConstants.MESSAGE;
			}
			
			//其他form参数
			String noticeTime = this.getParameter("noticeTime"); //面试时间
			String jobid = this.getParameter("jobid"); //面试职位ID
			String job = this.getParameter("job"); //面试职位
			String detail = this.getParameter("formContent");
			if(detail.equals("空")) detail = "";
			String[] vv = F_formParam();

            result = F_doProcess(processCode, cid, pcid, tid, jobid, job, noticeTime, detail, vv);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	
	public String uploadFile(){
		System.out.println("[CandiProcessServiceBean.uploadFile]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("CANDI");
	}

	public String uploadPic(){
		System.out.println("[CandiProcessServiceBean.uploadPic]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("CANDI");
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;
	
	private LWResult ServiceDO_inviteLog_Add() {
		LWResult result = null;
		String memo = "添加候选人被邀请记录";
		try {
			String cid = this.getParameter("cid");
			String jobid = this.getParameter("jobid");
			String hrAuid = auid;
			String candiName = "";//候选人姓名
			String hcAuid = "";//hcAuid
			String hcName = "";//hc姓名
			String hrName = "";//hr姓名
			String hrAmid = myamid;
			String organName = "";//hr公司名称
			String jobName = "";//职位名称
			List<String> sqls = new ArrayList<String>();

			if(jobid!=null && jobid.length()>0){
				List<Object> job = dbQuery(String.format(YSSQL.LWJob_QueryByC(jobid, "", "", "", "", "", "")));
				if(job!=null && job.size()>0){
					for (Object o : job) {
						jobName = this.getDBString(o, "c1");
					}
				}
			}
			if(cid!=null && cid.length()>0){
				List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_QueryByC, new Object[]{cid}));
				if(list!=null&&list.size()>0){
					for (Object o : list) {
						candiName = this.getDBString(o, "c8");
						hcAuid = this.getDBString(o, "c2");
						hcName = ComBeanLoginUser.getUserName(hcAuid);
					}
				}
			}
			if(hrAuid!=null && hrAuid.length()>0){
				hrName = ComBeanLoginUser.getUserName(hrAuid);
				List<Object> companys = dbQuery(String.format("select m.c2 companyName from B_HR h,B_MMORGAN m where h.c2=m.c1 and h.c1='%s' ", new Object[]{hrAuid}));
				if(companys!=null&&companys.size()>0){
					for (Object o : companys) {
						organName = this.getDBString(o, "companyName");
					}
				}
			}
			String sql = String.format(YSSQL.LWBT_Candidate_InviteLog_ADD, new Object[]{
					cid,
					candiName,
					hcAuid,
					hcName,
					hrAuid,
					hrName,
					hrAmid,
					organName,
					jobid,
					jobName	
			});
			System.out.println(sql);
			sqls.add(sql);
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功.");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败!");
			}
		} catch (Exception e) {
			result = new LWResult(LWResult.FAILURE, memo+"异常!");
		}
		return result;
	}

}
