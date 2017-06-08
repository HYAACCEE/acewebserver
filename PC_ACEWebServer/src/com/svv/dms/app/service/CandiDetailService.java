package com.svv.dms.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.EmailUtil;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class CandiDetailService extends CandiBase {
	//queryMK //进行中 1 ，已收藏2，已结束3
	
	public String CandiDetailCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiDetailMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiDetailBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiDetailSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String uploadFile(){
		System.out.println("[CandiDetailService.uploadFile]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("CANDI");
	}
	public String uploadPic(){
		System.out.println("[CandiDetailService.uploadPic]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("CANDI");
	}
	public String uploadFace(){
		System.out.println("[CandiDetailService.uploadFace]=======================================");
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		return F_UploadFace("CANDI", 120, 150);
	}
	
	//候选人详情：转发
	public String shareByEmail(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;///////////////////////////////////////
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String[] vv = this.F_formParam();
			Object o = dbQuery("select c8,c11,c13,c21,c25,c39,c41,c42,p.c43/1000 yuexin_k, c95 from BO_Candidate p where c1='"+cid+"'").get(0);
			if(o!=null){
                String file = this.getDBString(o, "c95");
		    	StringBuilder emailContent = new StringBuilder()  
		        .append("<html><body style=\"margin:0px; color:#565656; font-family:微软雅黑;\">")
		        .append("<div style=\"background-image:url(http://www.ace-elite.com/doc/images/email_top.png); background-size:90%; vertical-align:top;\">")  
				.append("<a href='www.ace-elite.com'><div style=\"height:80px\"><br></div></a>")
				.append("</div>")
				.append("<div><span style=\"color: #565656;font-size:18px;font-weight: bold\">").append(ACE.TXT_EMAIL_SLOGAN).append("</span></div>")
		        .append("<div style=\"height:50px\"></div>")
		        .append("<div>").append(vv[2]).append("</div>")
		        .append("<div style=\"height:20px\"></div>")
		        .append("<div class=\"font-weight:bold\">")   
		        .append("<span style=\"font-size:16px\">").append(getDBString(o, "c8")).append("</span>") 
		        .append("<span style=\"font-size:12px\">(").append(getDBString(o, "c41")).append(getDBString(o, "c42", "", " / ", "")).append(")</span>")  
		        .append("<br>")   
		        .append("<span style=\"font-size:12px\">").append(getDBDataParam(o, "c13", "", "", "")).append(getDBString(o, "c11",""," | ","岁")).append(getDBDataParam(o, "c25", "", " | ", "")).append(getDBInt(o, "c39", "", " | ", "年工作经验")).append(getDBString(o, "yuexin_k", "", " | ", "K/月")).append("</span>")
		        .append("</div>")
		        .append("<div><a style=\"color: #FEA008;font-weight: bold;font-size: larger;font-family: cursive;\" href=")  
		        .append(file)
		        .append(">点击查看简历</a></div><div style=\"height:20px\"></div>")
		        .append("<div style=\"color: #505050;\">如果以上链接无法访问,请将该网址复制并粘贴至新的浏览器窗口中<br/>") 
		        .append("<span style=\"color: #FEA008;font-size:10px;font-family: cursive;\">").append(file).append("</span>")
		        .append("</div>")
				.append("<div style=\"height:100px\"></div>")
		        .append("<div style=\"color: #A0A0A0;font-size:12px;\">如果您错误地收到了此电子邮件，请忽略此封邮件</div>") 
		        .append("<div style=\"height:50px;background-image:url(http://www.ace-elite.com/doc/images/email_bottom.png); background-size:90%; vertical-align:top;\"></div>")  
		        .append("</div></body></html>");
				String sendEmailResult = EmailUtil.sendEmailFile(vv[0], "【ACE】"+vv[1], emailContent.toString(),"");

				if(sendEmailResult.equals("SUCCESS")){
			        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-转发简历");
					result = new LWResult(LWResult.SUCCESS, com.svv.dms.web.Constants.FILESERVER_URL+"/download.jsp?"+file);
				}else{
				    result = new LWResult(LWResult.FAILURE, sendEmailResult);
				}
				
			}else{
				result = new LWResult(LWResult.FAILURE, "文件不存在");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, "异常："+e.getMessage());
		}
		return APPRETURN(result);
	}
	
	//候选人详情：下载
	public String download(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;///////////////////////////////////////
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String file = this.getSqlValue("select c95 from BO_Candidate where c1='"+cid+"'", "c95");
			if(file.length()>0){
		        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-下载简历");
				result = new LWResult(LWResult.SUCCESS, com.svv.dms.web.Constants.FILESERVER_URL+"/download.jsp?"+file);
			}else{
				result = new LWResult(LWResult.FAILURE, "文件不存在");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, "异常："+e.getMessage());
		}
		return APPRETURN(result);
	}
	
	//候选人详情：简历
	public String detail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;///////////////////////////////////////
		int detailIndex = this.getParameter("index", 0);
		LWResult result = ServiceDO_CandiDetail(detailIndex);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-简历("+detailIndex+")");
		return APPRETURN(result);
	}
	//候选人详情：流程
	public String processDetail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);
		LWResult result = ServiceDO_Candi_ProcessDetail(detailIndex);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-流程");
		return APPRETURN(result);
	}
	//候选人详情：事件
	public String actionDetail(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);
		LWResult result = ServiceDO_Candi_ActionList(detailIndex);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-事件");
		return APPRETURN(result);
	}
	//候选人详情：附件分类
	public String attachFileType(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);
		LWResult result = ServiceDO_Candi_AttachFileType();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-附件分类");
		return APPRETURN(result);
	}
	
	//候选人详情：附件
	public String attachFiles(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);
		LWResult result = ServiceDO_Candi_AttachFiles(detailIndex);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人详情-附件");
		return APPRETURN(result);
	}
	//编辑候选人附件
	public String editAttachFile(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_editAttachFile();
		return APPRETURN(result);
	}
	//删除候选人附件
	public String delAttachFile(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_delAttachFile();
		return APPRETURN(result);
	}
	// 发送简历的时候,查询当前候选人信息
	public String selectCandidateInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		System.out.println("[CandiListService.selectCandidateInfo]=======================================");
		LWResult result = null;
		String memo = "查看候选人信息";
		try{
			String hcid = this.getParameter("hcid");
			List<Object> list = dbQuery(String.format("select c8,c42,c41 from B_Candidate a where a.c1='%s'", new Object[]{hcid}));

			StringBuilder s = new StringBuilder();
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "c8", "", "", "")+"  ("+this.getDBString(o, "c42", "", "", "") + this.getDBString(o, "c41", "", "", "")+")"); //联系电话（座机）
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
	        loggerM(ComBeanLogType.TYPE_QUERY, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return APPRETURN(result);
	}
	//候选人详情: 备忘录
	public String detailMemo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_Memo();
		return APPRETURN(result);
	}
	//候选人详情: 备忘录
	public String addCandidataMemo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_AddMemo();
		return APPRETURN(result);
	}
	
	
	//M端Active详情界面
	public String candiInfoForActive(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_candiInfoForActive_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "Active审核详情界面");
		return APPRETURN(result);
	}
	private LWResult ServiceDO_candiInfoForActive_M(){
		System.out.println("[CandiListService.ServiceDO_Active_M]=======================================");
		String memo = "Active审核状态详情";
		try{
			StringBuilder s = new StringBuilder("");
			String cid = this.getParameter("cid");
			int n = 0;
			List<Object> list = dbQuery(String.format("select a.* from B_Candidate a where c1='%s'", new Object[]{cid}));

			
			if(list!=null && list.size()>0){
				for(Object o : list){
					String hcName = ComBeanLoginUser.getUserName(this.getDBString(o, "c2"));
					s.append(this.getDBString(o, "c1"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) 
					 .append(Constants.SPLITER).append(this.getDBString(o, "c9"))
					 .append(Constants.SPLITER).append(this.getDBDataParam(o, "c13", "未填", "", "") + " |")
					 .append(Constants.SPLITER).append(this.getDBString(o, "c11", "", "", "岁 |"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c48") + " |")
					 .append(Constants.SPLITER).append(this.getDBString(o, "c41") + " |")//公司
					 .append(Constants.SPLITER).append(this.getDBString(o, "c42"))//品牌经理
					 .append(Constants.SPLITER).append(this.getDBString(o, "c2")) 
					 .append(Constants.SPLITER).append(hcName); 
				}
				n++;
			}

			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;

	// 新增候选人的备忘录
	private LWResult ServiceDO_Candi_AddMemo(){
		System.out.println("[CandiDetailServiceBean.ServiceDO_Candi_AddMemo]=======================================");
		LWResult result = null;
		String memo = "新增候选人备忘录";
		try{
			String[] vv = F_formParam();
			int i = 0;
			String memoText = vv[i++];//备忘录内容
			String cid = this.getParameter("cid");
			
			StringBuilder s = new StringBuilder();
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWCandidate_addMemo, new Object[]{auid,memoText,auid,ComBeanLoginUser.getUserName(auid),cid}));/////////////////////////////////////////////////////////////////////////////////////
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		
		return result;
	}

	// 查询候选人的备忘录
	private LWResult ServiceDO_Candi_Memo(){
		System.out.println("[CandiDetailServiceBean.ServiceDO_Candi_Memo]=======================================");
		LWResult result = null;
		String memo = "候选人备忘录";
		try{
			String cid = this.getParameter("cid");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder();
			List<Object> list = null;
			if(isHC()){ // HC顾问
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid));
			}
			int n = 0;
			list = dbQuery("select a.* from B_Candidate_Memo a where a.c1="+cid+" order by a.c11 desc");/////////////////////////////////////////////////////////////////////////////////////
			if(list!=null && list.size()>0){
				for(Object o: list){
					s.append(String.format(HTML, new Object[]{
							this.getDBDate(o, "c11", "yyyy-MM-dd HH:mm"),
							HIUtil.toHtmlStr(this.getDBString(o, "c6")),
							n==list.size()-1 ? "gone" : ""
					}));
				}
				n++;
			}

			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "候选人详情：附件: 异常");
		}
		
		return result;
	}
	// 新增/编辑候选人附件
	private LWResult ServiceDO_Candi_editAttachFile(){
		System.out.println("[CandiDetailServiceBean.ServiceDO_Candi_editAttachFile]=======================================");
		LWResult result = null;
		String memo = "候选人附件";
		try{
			List<String> sqls = new ArrayList<String>();
			String cid = this.getParameter("cid");
			long attachDataid = this.getParameter("attachDataid", -1);
			int attachType = this.getParameter("attachType", 0);
			String attachTitle = this.getParameter("attachTitle");
			String attachName = this.getParameter("attachName");
			String attachFileType = this.getParameter("attachFileType");
			String attachUrl = this.getParameter("attach");
			
			Object[] oo = new Object[12];
			int i = 0;
			oo[i++] = attachType;//c3
			oo[i++] = ComBeanI_DataParamType.getText(attachType);//c4
			oo[i++] = attachTitle;//c5
			oo[i++] = attachName;//c6
			oo[i++] = attachFileType;//c7
			oo[i++] = attachUrl;//c8
			oo[i++] = auid;//c9
			oo[i++] = ComBeanLoginUser.getUserName(auid);//c10
			oo[i++] = auid;//c12
			oo[i++] = ComBeanLoginUser.getUserName(auid);//c13
			oo[i++] = cid;//c1
			oo[i++] = attachDataid;//c3

			String actionCode = null;
			if(attachType==ACE.CVFILE_TYPE_FACE){ actionCode = "A111003"; memo = "候选人头像"; }
			if(attachType==ACE.CVFILE_TYPE_CV){ actionCode = "A111004"; memo = "候选人简历"; }
			if(attachType==ACE.CVFILE_TYPE_OTHERS){ actionCode = "A111005"; memo = "候选人其他附件"; }

			if(attachDataid==-1){
				memo += "添加";
			    sqls.add(String.format(YSSQL.LWCandidate_addAttachFile, oo));
			}else{
				memo += "编辑";
				sqls.add(this.sqlToZ("B_Candidate_Files", memo, "dataid="+attachDataid));
				sqls.add(String.format(YSSQL.LWCandidate_editAttachFile, oo));
			}
			
			sqls.add(this.sqlToZ("B_Candidate", memo, "c1='"+cid+"'"));
			if(attachType==ACE.CVFILE_TYPE_FACE){
				sqls.add(String.format("update B_Candidate set c61='%s' where c1='%s'", new Object[]{attachUrl, cid}));
				sqls.add(String.format("update BO_Candidate set c61='%s' where c1='%s'", new Object[]{attachUrl, cid}));
			}
			if(attachType==ACE.CVFILE_TYPE_CV){
				sqls.add(String.format("update B_Candidate set c62='%s' where c1='%s'", new Object[]{attachUrl, cid}));
				sqls.add(String.format("update BO_Candidate set c62='%s' where c1='%s'", new Object[]{attachUrl, cid}));
			}
			
			sqls.add(P_getActionSQL(actionCode, cid, attachName)); //========================================事件
			
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return result;
	}


	// 删除候选人附件
	private LWResult ServiceDO_Candi_delAttachFile(){
		System.out.println("[CandiDetailServiceBean.ServiceDO_Candi_delAttachFile]=======================================");
		LWResult result = null;
		String memo = "候选人附件删除";
		try{
			List<String> sqls = new ArrayList<String>();
			String cid = this.getParameter("cid");
			long attachDataid = this.getParameter("attachDataid", -1);
			int attachType = this.getParameter("attachType", -1);
			String attachName = this.getParameter("attachName");
			
			String actionCode = null;
			if(attachType == ACE.CVFILE_TYPE_FACE){ actionCode = "A111043"; memo = "候选人头像删除"; }
			if(attachType == ACE.CVFILE_TYPE_CV){ actionCode = "A111044"; memo = "候选人简历删除"; }
			if(attachType == ACE.CVFILE_TYPE_OTHERS){ actionCode = "A111045"; memo = "候选人其他附件删除"; }
			
			sqls.add(this.sqlToZ("B_Candidate_Files", memo, "dataid="+attachDataid));
			sqls.add(String.format(YSSQL.LWCandidate_delAttachFile, new Object[]{cid, attachDataid}));

			sqls.add(this.sqlToZ("B_Candidate", memo, "c1='"+cid+"'"));
			if(attachType == ACE.CVFILE_TYPE_FACE)
				sqls.add(String.format("update B_Candidate set c61='%s' where c1='%s'", new Object[]{"", cid}));
				sqls.add(String.format("update BO_Candidate set c61='%s' where c1='%s'", new Object[]{"", cid}));
			if(attachType == ACE.CVFILE_TYPE_CV)
				sqls.add(String.format("update B_Candidate set c62='%s' where c1='%s'", new Object[]{"", cid}));
				sqls.add(String.format("update BO_Candidate set c62='%s' where c1='%s'", new Object[]{"", cid}));
			
			sqls.add(P_getActionSQL(actionCode, cid, attachName)); //========================================事件
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return result;
	}
	
	//候选人详情：附件分类
	private LWResult ServiceDO_Candi_AttachFileType(){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_AttachFileType]=======================================");
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			StringBuilder s = new StringBuilder();
			List<Object> list = dbQuery(YSSQL.LWCandidate_Files_QueryByC(cid, "", ""));
			Object[] oo = new Object[]{"", "", "", "", ""};
			
			int n = 0;
			String rtnString = null;
			if(list!=null && list.size()>0){
				int fileType;
				String dataid, istdate;
				for(Object o : list){
					fileType = this.getDBInt(o, "c3");
					dataid = this.getDBString(o, "dataid");
					istdate = this.getDBDate(o, "istdate", "yyyy-MM-dd").replaceAll("-", ".");
					if(fileType==ACE.CVFILE_TYPE_FACE){
						oo[0] = dataid;
						oo[1] = istdate;
					}
					if(fileType==ACE.CVFILE_TYPE_CV){
						oo[2] = dataid;
						oo[3] = istdate;
					}
					if(fileType==ACE.CVFILE_TYPE_OTHERS){
						s.append(String.format(htmls.get(1), new Object[]{
							dataid,
							this.getDBString(o, "c5"),
							this.getDBString(o, "c7"),
							this.getDBString(o, "c8"), //dES.decrypt(this.getDBString(o, "c8")).replaceAll("###", "@@@"),
							istdate,
						}));
					}
					n++;
				}
				oo[4] = s.toString();
			}
			rtnString = String.format(htmls.get(0), oo);
			return new LWResult(LWResult.SUCCESS, rtnString);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "候选人详情：附件分类: 异常");
		}
		return result;
	}	
	//候选人详情：附件
	private LWResult ServiceDO_Candi_AttachFiles(int detailIndex){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_AttachFiles]=======================================");
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String fileID = this.getParameter("fileID");
			String objectMode = "H00404-"+detailIndex;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder();
			List<Object> list = null;
			if(isHR()){ // HR
				list = dbQuery(YSSQL.LWCandidateProcess_QueryByCID("O", cid, "", "130001", "", "", auid, myamid));
			}else if(isHC()){ // HC顾问
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid));
			}else if(isM()){ //管理员
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, "", ""));
			}else{
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10700");
				return result;
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10400");
				return result;
			}
			if(this.getDBInt(list.get(0), "c69")!=130001){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10200");
				return result;
			}
			list = dbQuery(YSSQL.LWCandidate_Files_QueryByC(cid, detailIndex==1?"":(detailIndex+""), fileID));/////////////////////////////////////////////////////////////////////////////////////
			if(list!=null && list.size()>0){
				for(Object o: list){
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "dataid"),
							this.getDBString(o, "c5"),
							this.getDBString(o, "c7"),
							this.getDBString(o, "c8"), //dES.decrypt(this.getDBString(o, "c8")).replaceAll("###", "@@@"),
							this.getDBDate(o, "uptdate", "yyyy-MM-dd HH:mm"),
					}));
				}
			}else if(detailIndex==1){
				s.append(String.format(HTML, new Object[]{
						"",
						"",
						"",
						"",
				}));
			}

			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "候选人详情：附件: 异常");
		}
		return result;
	}

	//候选人详情：事件
	private LWResult ServiceDO_Candi_ActionList(int detailIndex){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_ActionList]=======================================");
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String actionType = this.getParameter("actionType");
			String startDate = this.getParameter("startDate");
			if(startDate.length()>0) startDate = this.getParameterStartDate("startDate");
			String endDate = this.getParameter("endDate");
			if(endDate.length()>0) endDate = this.getParameterEndDate("endDate");

			String objectMode = "H00403-"+detailIndex;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder();
			List<Object> list = null;
			if(isHR()){ // HR
				list = dbQuery(YSSQL.LWCandidateProcess_QueryByCID("O", cid, "", "130001", "", "", auid, myamid));
			}else if(isHC()){ // HC顾问
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid));
			}else if(isM()){ //管理员
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, "", ""));
			}else{
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10700");
				return result;
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10400");
				return result;
			}
			if(this.getDBInt(list.get(0), "c69")!=130001){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10200");
				return result;
			}
			if(isM()){
				list = dbQuery(YSSQL.LWCandidate_Action_QueryByC(cid, "", "", "", actionType, startDate, endDate, "", "1", "", "", "", "", "", ""));
			}else if(isHC()){
				list = dbQuery(YSSQL.LWCandidate_Action_QueryByC(cid, auid, "", "", actionType, startDate, endDate, "", "", "", "1", "", "", "", ""));
			}else if(isHR()){
				list = dbQuery(YSSQL.LWCandidate_Action_QueryByC(cid, "", auid, myamid, actionType, startDate, endDate, "", "", "", "", "1", "", "", ""));
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.FAILURE, "无记录, err=10402");
				return result;
			}
			String html = HTML;
			int index = 100;
			int n = 0;
			String dircss = null;
			String title = null;
			String detail = null;
			for(Object o: list){
				if(HTML.indexOf("<INDEX>")>=0){
					index++;
					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
				}
				
				if(n==0) dircss = "dir-line-root";
				else if(n==list.size()-1) dircss = "dir-line-end";
				else dircss = "dir-line";

				if(isM()) title = this.getDBString(o, "c14");
				else if(isHC()) title = this.getDBString(o, "c16");
				else if(isHR()) title = this.getDBString(o, "c17");
				else if(isC()) title = this.getDBString(o, "c18");
				
				detail = this.getDBString(o, "c13");
				
				s.append(String.format(html, new Object[]{
						dircss,
						HIUtil.isEmpty(detail) ? (n==0 ? "dir-root":"dir-dot") : "close btn-slide",
					    DBUtil.getDBDateStr(o, "istdate", "yyyy-MM-dd HH:mm"),
					    title.replaceAll("\\[OPEN=(.+?)\\]", ""),
					    detail,
				}));
				n++;
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "候选人详情：事件: 异常");
		}
		return result;
	}

	//候选人详情：流程
	private LWResult ServiceDO_Candi_ProcessDetail(int detailIndex){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_ProcessDetail]=======================================");
		LWResult result = null;
		try{
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			String processStatus = this.getParameter("processStatus");
			
			String objectMode = "H00402-"+detailIndex;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder();
			List<Object> list = null;
			if(isHR()){ // HR
				list = dbQuery(YSSQL.LWCandidateProcess_QueryByCID("O", cid, "", "130001", "", "", auid, myamid));
			}else if(isHC()){ // HC顾问
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid));
			}else if(isM()){ //管理员
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, "", ""));
			}else{
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10700");
				return result;
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10400");
				return result;
			}
			if(this.getDBInt(list.get(0), "c69")!=130001){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10200");
				return result;
			}
			if(detailIndex==1){
				if(isHC()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailList_QueryByC("", cid, auid, myamid, "", "", "", processStatus));				
				}else if(isM()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailList_QueryByC("", cid, "", "", "", "", "", processStatus));				
				}else if(isHR()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailList_QueryByC("O", cid, "", "", auid, myamid, "", processStatus));				
				}
			}
			if(detailIndex==3){
				if(isHC()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailHis_QueryByC("", cid, auid, myamid, "", "", pcid, processStatus));				
				}else if(isM()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailHis_QueryByC("", cid, "", "", "", "", pcid, processStatus));				
				}else if(isHR()){
					list = dbQuery(YSSQL.LWCandidate_ProcessDetailHis_QueryByC("O", cid, "", "", auid, myamid, pcid, processStatus));				
				}
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.FAILURE, "无记录, err=10402");
				return result;
			}
			String html = HTML;
			int index = pcid.length()==0 ? 100 : Integer.parseInt(pcid.substring(26));
			int n = 0;
			for(Object o: list){
				if(HTML.indexOf("<INDEX>")>=0){
					index++;
					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
				}
				
				if(detailIndex==1){
					boolean isActiveProcess = this.getDBString(o, "candiStatus").equals(ACE.CANDI_STATUS_ING);

					String stStatus = this.getDBString(o, "stStatus");
					String stCheckStatus = this.getDBString(o, "stCheckStatus");
					String activeStatus = this.getDBString(o, "activeStatus");
					String candiStatus = this.getDBString(o, "candiStatus");
					String processCloseStatus = this.getDBString(o, "processCloseStatus");
					String processInnerStatus = this.getDBString(o, "processInnerStatus");
					String collectStatus = this.getDBString(o, "collectStatus");

					boolean powerForHC_addHRIV = powerForHC_addHRIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_editHRIV = powerForHC_editHRIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_confirmHRIV = powerForHC_confirmHRIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_candidateReplyIV = powerForHC_candidateReplyIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_candidateReplyOffer = powerForHC_candidateReplyOffer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_setIVResult = powerForHC_setIVResult(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_CandidateReplyEntry = powerForHC_CandidateReplyEntry(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_CandidateEntryConfirm = powerForHC_CandidateEntryConfirm(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHC_setDimissionReason = powerForHC_setDimissionReason(processInnerStatus);
					boolean powerForHC_setUnPassIVReason = powerForHC_setUnPassIVReason(candiStatus, processInnerStatus);
					boolean powerForHC_setUnEntryReason = powerForHC_setUnEntryReason(candiStatus, processInnerStatus);
					boolean powerForHR_billing = powerForHR_billing(candiStatus, processInnerStatus);
					boolean powerForHR_billPayingToM = powerForHR_billPayingToM(candiStatus, processInnerStatus);
					boolean powerForHR_billPayedToM = powerForHR_billPayedToM(candiStatus, processInnerStatus);
					boolean powerForHR_billPayedToHC = powerForHR_billPayedToHC(candiStatus, processInnerStatus);

					String[] btn11Class = {"gone", "gone", "gone", "gone", "gone", "gone", "gone", "gone","gone"}, btn11 = {"", "", "", "", "", "", "", "", ""};
					int m = 0;
					if(powerForHR_editOffer){ btn11Class[m] = "HC_editOffer_"; btn11[m] = "Offer变更"; }
					if(powerForHC_candidateReplyOffer){ btn11Class[m] = "HC_setCandidateReplyOffer_"; btn11[m] = "候选人反馈"; }
					if(powerForHC_addHRIV) { btn11Class[m] = "HC_addHRIV_"; btn11[m] = "安排面试"; }
					if(powerForHC_confirmHRIV) { btn11Class[m] = "HC_confirmHRIV_"; btn11[m] = "确定面试"; }
					if(powerForHC_editHRIV) { btn11Class[m] = "HC_editHRIV_"; btn11[m] = "变更面试"; }
					if(powerForHC_setIVResult) { btn11Class[m] = "HC_setHRIVResult_"; btn11[m] = "面试反馈"; }
					if(powerForHR_addOffer) { btn11Class[m] = "HC_uploadOffer_"; btn11[m] = "上传Offer"; }
					if(powerForHC_candidateReplyIV){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "候选人反馈"; }
					if(powerForHC_CandidateReplyEntry){ btn11Class[m] = "HC_setCandidateReplyEntry_"; btn11[m] = "候选人入职"; }
					if(powerForHC_CandidateEntryConfirm){ btn11Class[m] = "HC_setCandidateEntryConfirm_"; btn11[m] = "确定入职"; }
					if(powerForHR_billing){ btn11Class[m] = ""; btn11[m] = "待开票"; }
					if(powerForHR_billPayingToM){ btn11Class[m] = ""; btn11[m] = "待付款到平台"; }
					if(powerForHR_billPayedToM) { btn11Class[m] = ""; btn11[m] = "待付款到您的账户"; }
					if(powerForHR_billPayedToHC) { btn11Class[m] = ""; btn11[m] = "已付款到您的账户"; }
					if(this.getDBString(o, "processDetail").length()>0){
						if(powerForHC_setDimissionReason){ btn11Class[m] = "HC_setDimission_"; btn11[m] = "修改原因"; }//离职
						if(powerForHC_setUnPassIVReason) { btn11Class[m] = "HC_setUnPassIVReason_"; btn11[m] = "修改原因"; }//面试未通过
						if(powerForHC_setUnEntryReason) { btn11Class[m] = "HC_setUnEntryReason_"; btn11[m] = "修改原因"; }// 未入职
						
					}else{
						if(powerForHC_setDimissionReason){ btn11Class[m] = "HC_setDimission_"; btn11[m] = "备注原因"; }//离职
						if(powerForHC_setUnPassIVReason) { btn11Class[m] = "HC_setUnPassIVReason_"; btn11[m] = "备注原因"; }//面试未通过
						if(powerForHC_setUnEntryReason) { btn11Class[m] = "HC_setUnEntryReason_"; btn11[m] = "备注原因"; }// 未入职
					}
					
					s.append(String.format(html, new Object[]{
							isActiveProcess ? "" : "state_disable",
							this.getDBString(o, "cid"),
							this.getDBString(o, "pcid"),
							isActiveProcess ? "当前" : DBUtil.getDBDateStr(o, "operateTime", "yyyy-MM-dd HH:mm"),
							this.getDBString(o, "processTitle") + (isActiveProcess ? "" : "（已结束）"), //流程标题,
							this.getDBString(o, "mmName"), //公司
							this.getDBString(o, "jobName", "职位"), //job
							this.getDBString(o, "hrName"), //hr
							isHC() && isActiveProcess ? "" : "gone", //关闭流程
					}));
				}else if(detailIndex==3){
					s.append(String.format(html, new Object[]{
							n==list.size()-1 ? "dir-line-end" : "dir-line",
							HIUtil.isEmpty(this.getDBString(o, "processDetail")) ? "dir-dot" : "close btn-slide",
							DBUtil.getDBDateStr(o, "operateTime", "yyyy-MM-dd HH:mm"),
						    this.getDBString(o, "processTitle"), //流程标题
							this.getDBString(o, "processDetail"), //流程详情HR_showDetail_ HC_showDetail_ pointer
					}));
				}
				
				n++;
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "候选人详情：流程: 异常");
		}
		return result;
	}


	private LWResult ServiceDO_CandiDetail(int detailIndex){
		System.out.println("[CandiServiceBean.ServiceDO_CandiDetail]=======================================");
		LWResult result = null;
		String memo = null;
		try{
			String cid = this.getParameter("cid");
			String objectMode = "H00401-"+detailIndex;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			StringBuilder s = new StringBuilder();
			List<Object> list = null, list2 = null;

		    boolean hasO2 = isHR() ? false : this.getSqlIntValue("select count(dataid) counter from BO_Candidate where c1='"+cid+"' and c69=130001", "counter") > 0;
			HashMap<Long, Object> O2_Map = null;

			if(isHR()){ // HR
				list = dbQuery(YSSQL.LWCandidateProcess_QueryByCID("O", cid, "", "130001", "", "", auid, myamid));
			}else if(isHC()){ // HC顾问
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid)); //简历变更版本
				if(hasO2) list2 = dbQuery(YSSQL.LWCandidate_QueryByCID("O", cid, auid, myamid)); //简历在线版本
			}else if(isM()){ //管理员
				list = dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, "", "")); //简历变更版本
				if(hasO2) list2 = dbQuery(YSSQL.LWCandidate_QueryByCID("O", cid, "", "")); //简历在线版本
			}else{
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10700");
				return result;
			}
			if(hasO2){
				O2_Map = new HashMap<Long, Object>();
				if(list2!=null && list2.size()>0){
					for(Object o: list2){
						O2_Map.put(this.getDBLong(o, "dataid"), o);
					}
				}
			}
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10400");
				return result;
			}
			if(this.getDBInt(list.get(0), "c69")!=130001){
				result = new LWResult(LWResult.EXCEPTION, "您访问的链接不存在, err=10200");
				return result;
			}
		    boolean hasO2_hcc = isHR() ? false : this.getSqlIntValue("select count(dataid) counter from BO_Candidate_HCComment where c1='"+cid+"' and c2='"+this.getDBString(list.get(0), "hcid")+"' and c3=130001", "counter") > 0;
			
			memo = LWCandidate_CVOtherDetails_MEMO[detailIndex];
			if(detailIndex==1){
				//浏览量+1
				if(isHR()){
					List<String> sqls = new ArrayList<String>();
					sqls.add(String.format("update B_HR set c29=NVL(c29,0)+(%s) where c1='%s'", new Object[]{1, auid}));
					sqls.add(String.format("update BO_Candidate set c28=NVL(c28,0)+(%s) where c1='%s'", new Object[]{1, cid}));
					sqls.add(String.format("update B_Candidate set c28=NVL(c28,0)+(%s) where c1='%s'", new Object[]{1, cid}));
//					int counter = this.getSqlIntValue("select count(dataid) counter from B_T_Candidate_ViewLog where c1='"+cid+"' and c2='"+auid+"'", "counter");
//					if(counter==0) 
						sqls.add(String.format("insert into B_T_Candidate_ViewLog(dataid,c1,c2,c3) values(seqB_T_Candidate_ViewLog.nextval, '%s', '%s', 1)", new Object[]{cid, auid}));
//					if(counter>0) 
//						sqls.add(String.format("update B_T_Candidate_ViewLog set c3=c3+1,uptdate=sysdate where c1='%s' and c2='%s'", new Object[]{cid, auid}));
					dbExe(sqls);
				}

			}else{
				if(isHR()){
						list = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{detailIndex==12?"":"O", cid, auid}));
				}else if(isHC()){
					 //简历变更版本
					list = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{"", cid, auid}));
					 //简历在线版本
					if(detailIndex!=12) list2 = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{"O", cid, auid}));
				}else if(isM()){
					 //简历变更版本
					list = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{detailIndex==12?"":"O", cid, ""}));
					 //简历在线版本
					if(detailIndex!=12) list2 = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{"O", cid, ""}));
				}
			    if(list==null || list.size()==0){
					result = new LWResult(LWResult.FAILURE, memo+"无记录");
					return result;
			    }
				if(hasO2){
					O2_Map = new HashMap<Long, Object>();
					if(list2!=null && list2.size()>0){
						for(Object o: list2){
							O2_Map.put(this.getDBLong(o, "dataid"), o);
						}
					}
				}
			}

			int index = 100;
			String html = null;
			Object o2 = null;
			for(Object o: list){
				if(hasO2){
					o2 = O2_Map.get(this.getDBLong(o, "dataid"));
					if(o2==null) o2 = (Boolean)true;
				}
				
				index++;
				if(detailIndex == 1){
					String stStatus = this.getDBString(o, "stStatus");
					String stCheckStatus = this.getDBString(o, "stCheckStatus");
					String activeStatus = this.getDBString(o, "activeStatus");
					String candiStatus = this.getDBString(o, "candiStatus");
					String processCloseStatus = this.getDBString(o, "processCloseStatus");
					String processInnerStatus = this.getDBString(o, "processInnerStatus");
					String collectStatus = this.getDBString(o, "collectStatus");

					String[] btn11Class = {"gone", "gone", "gone", "gone", "gone", "gone", "gone", "gone", "gone"}, btn11 = {"", "", "", "", "", "", "", "", ""};
					String btnMoreClass = "gone";
					if(isHR()){
						boolean powerForHR_inviteIV = powerForHR_inviteIV(activeStatus, candiStatus, processCloseStatus);
						boolean powerForHR_invited = powerForHR_invited(activeStatus, processInnerStatus);
						boolean powerForHR_collect = powerForHR_collect(collectStatus);
						boolean powerForHR_unCollect = powerForHR_unCollect(collectStatus);
						boolean powerForHR_addBlackList = powerForHR_addBlackList(candiStatus, collectStatus);
						boolean powerForHR_delBlackList = powerForHR_delBlackList(collectStatus);
						boolean powerForHR_addIV = powerForHR_addIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_editIV = powerForHR_editIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_confirmingIV = powerForHR_confirmingIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_setIVResultPass = powerForHR_setIVResult_Pass(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_setEntryResult = powerForHR_setEntryResult(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_entrying = powerForHR_entrying(candiStatus, processInnerStatus);
						boolean powerForHR_Offer = powerForHR_Offer(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_setDimission = powerForHR_setDimission(candiStatus, processInnerStatus);
						boolean powerForHR_dimissioning = powerForHR_dimissioning(candiStatus, processInnerStatus);
    					boolean powerForHR_billing = powerForHR_billing(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayingToM = powerForHR_billPayingToM(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayedToM = powerForHR_billPayedToM(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayedToHC = powerForHR_billPayedToHC(candiStatus, processInnerStatus);

						int m = 0; //信息
						if(powerForHR_invited) { btn11Class[m] = ""; btn11[m] = "已邀请面试"; }
						if(powerForHR_entrying) { btn11Class[m] = ""; btn11[m] = "入职待确认..."; }
						if(powerForHR_dimissioning) { btn11Class[m] = ""; btn11[m] = "离职待确认..."; }
						if(powerForHR_editOffer) { btn11Class[m] = "HR_showOffer_"; btn11[m] =  "Offer查看"; }
						if(powerForHR_setDimission){ btn11Class[m] = "HR_setDimission_"; btn11[m] = "候选人已离职？";}
						if(powerForHR_editIV && !powerForHR_confirmingIV){
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String time = this.getDBDate(o, "ivTime", "yyyy-MM-dd HH:mm");
							String showTime = null;
							if(time.length()>0){
								Date date = format.parse(time);
						  		calendar.setTime(date);
								String newTime = LWUtil.getWeekOfDate(time);
								if(newTime.equals("月份")){
									 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
								}
								showTime = "面试：".concat(newTime).concat(time.substring(10));
							}else{
								showTime = "";
							}
						    if(powerForHR_editIV){ btn11Class[m] = "HR_showDetail_"; btn11[m] = showTime; }
						}
						if(powerForHR_confirmingIV){ btn11Class[m] = "HR_showDetail_"; btn11[m] = "面试信息确定中"; }
						m++; //无事件按钮
						if(powerForHR_billing){ btn11Class[m] = ""; btn11[m] = "待开票"; }
						if(powerForHR_billPayingToM){ btn11Class[m] = ""; btn11[m] = "待付款到平台"; }
						if(powerForHR_billPayedToHC) { btn11Class[m] = ""; btn11[m] = "已付款"; }
						m++; //副按钮
						if(powerForHR_setIVResultPass) { btn11Class[m] = "HR_setIVUnPass_"; btn11[m] = "面试未通过"; }
						if(powerForHR_setEntryResult) { btn11Class[m] = "HR_setUnEntry_"; btn11[m] = "未能入职";}
						if(powerForHR_editIV) { btn11Class[m] = "HR_editIV_"; btn11[m] = "面试变更";}
						if(powerForHR_editOffer) { btn11Class[m] = "HR_editOffer_"; btn11[m] = "Offer变更"; }
						if(powerForHR_Offer) { btn11Class[m] = "HR_addIV_"; btn11[m] = "安排下一轮面试";} //当可以offer的时候，也可以安排下一轮面试
						if(powerForHR_billPayedToM) { btn11Class[m] = "HR_billPayToHC_"; btn11[m] = "付款给顾问"; }
						m++; //主按钮
						if(powerForHR_inviteIV) { btn11Class[m] = "HR_inviteIV_"; btn11[m] = "邀请面试<br><span><img class=\"middle\" style=\"width:66px;height:11px\" src=\"../doc/images/HR/yaoqing_icon.png\"></span>"; }
						if(powerForHR_addIV) { btn11Class[m] = "HR_addIV_"; btn11[m] = "安排面试";}
						if(powerForHR_Offer) { btn11Class[m] = "HR_addOffer_"; btn11[m] = "上传Offer";}
						if(powerForHR_addOffer) { btn11Class[m] = "HR_addOffer_"; btn11[m] = "上传Offer";}
						if(powerForHR_setIVResultPass) { btn11Class[m] = "HR_setIVPass_"; btn11[m] = "面试通过";}
						if(powerForHR_setEntryResult) { btn11Class[m] = "HR_setEntry_"; btn11[m] = "确认入职";}
						m++; //副按钮
						if(powerForHR_collect) { btn11Class[m] = "HR_collect_"; btn11[m] = "收藏"; }
						m++; //信息
						if(powerForHR_unCollect) { btn11Class[m] = ""; btn11[m] = "已收藏"; }
						m++; //更多
						if(powerForHR_unCollect) { btnMoreClass = ""; btn11Class[m] = "HR_unCollect_"; btn11[m] = "取消收藏"; }
						m++;
						//BUG-234:HR将候选人加入黑名单功能暂时不开放/////if(powerForHR_addBlackList) { btnMoreClass = ""; btn11Class[m] = "HR_addBlacklist_"; btn11[m] = "加入黑名单"; }
						m++;
						//BUG-234:HR将候选人加入黑名单功能暂时不开放////if(powerForHR_delBlackList) { btnMoreClass = ""; btn11Class[m] = "HR_delBlacklist_"; btn11[m] = "移出黑名单"; }
						m++;
						
					}
					boolean powerForHC_checkingCV = false, powerForHC_editCV = false;
					int hccommentPower = -1; 
					boolean powerForHR_export = powerForHR_export(activeStatus, candiStatus, processInnerStatus);
					if(isHC()){
						powerForHC_checkingCV = powerForHC_checkingCV(getDBString(o, "cvStatus"));
						powerForHC_editCV = powerForHC_editCV(stStatus, stCheckStatus, getDBString(o, "cvStatus"));
						hccommentPower = stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_ONLINE) || stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_REFUSED) ? 1 : 0;
						if(stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_CHECKING)) hccommentPower = 2;
						
						boolean powerForHC_applyST = powerForHC_applyST(stStatus, stCheckStatus);
						boolean powerForHC_cancelST = powerForHC_cancelST(stStatus, stCheckStatus);
						boolean powerForHC_addHCIV = powerForHC_addHCIV(stCheckStatus);
						boolean powerForHC_editHCComment = powerForHC_editHCComment(stCheckStatus);
						boolean powerForHC_commitHCComment = powerForHC_commitHCComment(stCheckStatus);
						boolean powerForHC_inActive = powerForHC_inActive(activeStatus);
						boolean powerForHC_unBlock = powerForHC_unBlock(activeStatus);
						boolean powerForHC_activeApply = powerForHC_activeApply(activeStatus);
						boolean powerForHC_activeChecking = powerForHC_activeChecking(activeStatus);
						int m = 0; //信息
						if(powerForHC_activeChecking) { btn11Class[m] = ""; btn11[m] = "Active状态审核中..."; }
						if(powerForHC_cancelST) { btn11Class[m] = "HC_cancelST_"; btn11[m] = "入库审核中..."; }
						m++; //无事件按钮
						m++; //副按钮
						if(powerForHC_applyST){ btn11Class[m] = "HC_applyST_"; btn11[m] = "申请入库"; }
						else if(powerForHC_cancelST) { btn11Class[m] = "HC_cancelST_"; btn11[m] = "取消入库"; }
						else if(powerForHC_addHCIV) { btn11Class[m] = "HC_addHCIV_"; btn11[m] = "顾问面试"; }
						else if(powerForHC_editHCComment) { btn11Class[m] = "HC_editHCComment_"; btn11[m] = "编辑顾问面评"; }
						else if(powerForHC_commitHCComment) { btn11Class[m] = "HC_editHCComment_"; btn11[m] = "编辑顾问面评"; }
						m++; //主按钮
						if(powerForHC_commitHCComment){ btn11Class[m] = "HC_commitHCComment_"; btn11[m] = "提交审核"; }
						m++; //副按钮
						m++; //信息
						m++; //更多
						if(powerForHC_applyST){ btnMoreClass = ""; btn11Class[m] = "HC_delCandidate_"; btn11[m] = "删除候选人"; }
						if(powerForHC_inActive) { btnMoreClass = ""; btn11Class[m] = "HC_InActive_"; btn11[m] = "Inactive候选人"; }
						if(powerForHC_unBlock) { btnMoreClass = ""; btn11Class[m] = "HC_activeApply_"; btn11[m] = "Active候选人"; }
						if(powerForHC_activeApply) { btnMoreClass = ""; btn11Class[m] = "HC_activeApply_"; btn11[m] = "Active候选人"; }
						m++;
						m++;
					}

					String status = stStatus.substring(3,5);
					if(stStatus.equals(ACE.CANDI_STStatus_ST)){ status += this.getDBString(o, "c5").substring(4,6);}
					s.append(String.format(HTML, new Object[]{
							isHR() ? "" : "pointer HC_candidateInfo_",
							this.getDBString(o, "c61", "../doc/images/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_b.png"), //性别 face_c1_m.png
							status, //入库状态 status_001_txt.png
							(isHC()||isM() || isCheckedUser() && candiStatus.length() > 0) ? this.getDBString_BO(o, "c8", "", "", "", o2, 1) : this.getDBDataParam(o, "c36","","",""), // 李大志 未成功邀请面试（包括未签约邀请不成功）的候选人详情里面不显示候选人姓名（含英文名），替换为职位类型
							(isHC()||isM() || isCheckedUser() && candiStatus.length() > 0) ? this.getDBString_BO(o, "c9", "", "", "", o2, 1) : "",  // 英文名称
							this.getDBString_BO(o, "c41", "", "", "", o2, 1), // 公司名称
							this.getDBString_BO(o, "c42", "", "<span class=pd5>/</span>", "", o2, 1), // 职位名称
							this.getDBString_BO(o, "c38", "", "<span class=pd5>/</span>", "", o2, 1), // 奢侈品
							"<span title=\"" + (IFACE_DBBUG_FLAG ? " ["+stStatus+"-"+ComBeanI_DataParamType.getText(stStatus)+"]["+stCheckStatus+"-"+ComBeanI_DataParamType.getText(stCheckStatus)+"]["+activeStatus+"-"+ComBeanI_DataParamType.getText(activeStatus)+"]["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"<"+this.getDBString(o, "pid")+">"+"]["+collectStatus+"-"+ComBeanI_DataParamType.getText(collectStatus)+"]" : "") + "\">T"+this.getDBString(o, "dataid")+"</span>", // #74554 //this.getDBString(o, "c7")
							this.getDBDataParam_BO(o, "c13", "", "", "", o2, 1), // 男
							this.getDBString_BO(o, "c11", "", "", "岁", o2, 1), // 27岁
							this.getDBDataParam_BO(o, "c25", "", "", "", o2, 1), // 本科
							this.getDBString(o, "c39").equals("0") ? "" :this.getDBString_BO(o, "c39", "", "", "年工作经验", o2, 1), // 3年工作经验
							this.getDBString_BO(o, "c43", "", "", "元/月", hasO2_hcc?o2:null, 1), // 25K
							this.getDBString_BO(o, "c23", "", "", "国籍", o2, 1), // 中国
							this.getDBString_BO(o, "c17", "", "　", "户口", o2, 1), // 上海户口
							this.getDBDataParam_BO(o, "c24", "", "　", "", hasO2_hcc?o2:null, 1), // 已婚
							this.getDBDataParam_BO(o, "c12", "", "　", "", o2, 1), // 摩羯座
							isHC()?"" :"gone",
							this.getDBString(o, "c28"),
							this.getDBString(o, "c27"),
							this.getDBString(o, "c29"),		
							isHC()?"gone" :"",// hc
							this.getDBString(o, "c27"), // 66收藏数量
							isEmpty_BO(o, "c15", o2) && isEmpty_BO(o, "c16", o2) ? "gone" : "",
							this.getDBString_BO(o, "c15", "", "　", "", o2, 1), // 上海
							this.getDBString_BO(o, "c16", "", "　", "", o2, 1), // 浦东新区
							btn11Class[0], btn11[0],
							btn11Class[1], btn11[1],
							btn11Class[2], btn11[2],
							btn11Class[3], btn11[3],
							btn11Class[4], btn11[4],
							btn11Class[5], btn11[5],
							btnMoreClass,
							btn11Class[6], btn11[6],
							btn11Class[7], btn11[7],
							btn11Class[8], btn11[8],
							powerForHC_checkingCV ? "2" : (powerForHC_editCV ? "1" : "0"),
							this.getDBDate(o, "uptdate", "yyyy-MM-dd HH:mm"),
							this.getDBString(o, "hcid"),
							this.getDBString(o, "pcid"),
							hccommentPower,
							powerForHR_export ? 1 : 0,
							this.getDBString(o, "c8", "", "", "")+"  ("+this.getDBString(o, "c42", "", "", "") + this.getDBString(o, "c41", "", "", "")+")",
					}));
				}else{
					if(list==null || list.size()==0){
						result = new LWResult(LWResult.FAILURE, "没有"+memo+"记录");
						return result;
					}
					
					switch(detailIndex){
					case 3: // 简历-教育
						s.append(String.format(HTML, new Object[]{
								this.getDBString_BO(o, "c3", "", "", "", o2, 1)+this.getDBString_BO(o, "c4", "", "-", "", o2, 1),
								this.getDBString_BO(o, "c5", "", "", "", o2, 1),
								this.getDBString_BO(o, "c6", "", "", "", o2, 1) + this.getDBDataParam_BO(o, "c7", "", " | ", "", o2, 1),
						}));
						break;
					case 4: // 简历-培训
						s.append(String.format(HTML, new Object[]{
								this.getDBString_BO(o, "c3", "", "", "", o2, 1),
								this.getDBString_BO(o, "c6", "", "", "", o2, 1),
						}));
						break;
					case 5: // 简历-证书
						s.append(String.format(HTML, new Object[]{
								this.getDBString_BO(o, "c3", "", "", "", o2, 1),
								this.getDBString_BO(o, "c4", "", "", "", o2, 1),
						}));
						break;
					case 6: // 简历-任职经历
						s.append(String.format(HTML, new Object[]{
								this.getDBString_BO(o, "c3", "", "", "", o2, 1),
								this.getDBString_BO(o, "c4", "", "", "", o2, 1),
								this.getDBString_BO(o, "c5", "", "", "", o2, 1).replaceAll("(.+?)\\[", "").replaceAll("\\]", ""),
								this.getDBString_BO(o, "c9", "", "", "", o2, 1), //部门
								this.getDBString_BO(o, "c10", "", "(", ")", o2, 1), //职位
								this.getDBString_BO(o, "c6", "", "", "", o2, 1),
						}));
						break;
					case 7: // 简历-工作经验
						html = HTML;
						//工作权重
						List<String> html_formats = LWUtil.HTML_getFormatHTMLs(html, new String[]{"WORKWEIGHT"});
						html = html_formats.get(0);
						String html_WorkWeight = html_formats.get(1);
						
						String workWeight_value = getDBString(o, "c17");
						String workWeight_str = "";
						if(workWeight_value.length()>3){
							workWeight_str = workWeight_value;
							String[] ss = workWeight_str.split(SP1);
							String[][] ss1 = new String[ss.length][3];
							for(int j=0; j<ss.length; j++){
								String[] tmp = ss[j].split(SP2);
								String title = tmp.length>0? tmp[0]:"";
								String value = tmp.length>1? tmp[1]:"0";
								ss1[j] = new String[]{String.valueOf(Integer.parseInt(value)*1.6), value, title};
							}
							workWeight_str = LWUtil.HTML_toDatalist(html_WorkWeight, ss1, "", "");
						}
						if(o2!=null){
							if(o2 instanceof Boolean){
								if((Boolean)o2){
									workWeight_str = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{"", workWeight_str});
								}
							}else{
								String checkValue = getDBString(o2, "c17");
								if(!checkValue.equals(workWeight_value)){
									workWeight_str = String.format(ACE_CHECK_CV_COLUMN_SPAN, new Object[]{checkValue, workWeight_str});
								}
							}
						}

						//工作职责
						String c16 = getDBString(o, "c16", "", true);
						String c16_o2 = hasO2_hcc ? getDBString(o2, "c16", "", true) : null;
						//工作业绩
			            String c29 = getDBString(o, "c29", "", true);
			            String c29_o2 = hasO2_hcc ? getDBString(o2, "c29", "", true) : null;
						
						s.append(String.format(html, new Object[]{
								getDBString_BO(o, "c3", "", "", "", o2, 1),//开始时间
								getDBString_BO(o, "c4", "", "", "", o2, 1),//结束时间
								getDBString_BO(o, "c5", "", "", "", o2, 1).replaceAll("(.+?)\\[", "").replaceAll("\\]", "") + this.getDBString_BO(o, "c8", "", " | ", "", o2, 1),//公司名称 跟部门
								isEmpty_BO(o, "c9", o2) && isEmpty_BO(o, "c10", o2) ? "gone" : "",
								getDBString_BO(o, "c9", "", "", "", o2, 1),//职位
								this.getDBString(o, "c10").startsWith("227") ? this.getDBDataParam_BO(o, "c10", "", "(", ")", o2, 1) : this.getDBString_BO(o, "c10", "", "(", ")", o2, 1), //职级
								isEmpty_BO(o, "c7", hasO2_hcc?o2:null) && isEmpty_BO(o, "c20", hasO2_hcc?o2:null) ? "gone":"",//行业 职位类型
								isEmpty_BO(o, "c7", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c7", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c20", hasO2_hcc?o2:null) ? "gone":"",
								getDBDataParam_BO(o, "c20", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c11", hasO2_hcc?o2:null) && isEmpty_BO(o, "c12", hasO2_hcc?o2:null) && isEmpty_BO(o, "c13", hasO2_hcc?o2:null) ? "gone":"",//月薪年薪
								isEmpty_BO(o, "c11", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c11", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c11", hasO2_hcc?o2:null) ? "":"元/月",
								isEmpty_BO(o, "c12", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c12", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c12", hasO2_hcc?o2:null) ? "":"万/年",
								isEmpty_BO(o, "c13", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c13", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c18", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c18", "", "", "", hasO2_hcc?o2:null, 1, true),//公司介绍
								isEmpty_BO(o, "c14", hasO2_hcc?o2:null) && isEmpty_BO(o, "c15", hasO2_hcc?o2:null) ? "gone":"",
								isEmpty_BO(o, "c14", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c14", "", "", "", hasO2_hcc?o2:null, 1),//汇报对象
								isEmpty_BO(o, "c15", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c15", "", "", "", hasO2_hcc?o2:null, 1),//下属人数
								isEmpty_BO(o, "c24", true, hasO2_hcc?o2:null) ? "gone":"",
							    isEmpty_BO(o, "c24", true, hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c24", "", "", "天", hasO2_hcc?o2:null, 1),//离职期
								isEmpty_BO(o, "c26", hasO2_hcc?o2:null) ? "gone":"",
							    getDBString_BO(o, "c26", "", "", "", hasO2_hcc?o2:null, 1).replaceAll("1", "有").replaceAll("0", "没有"),//禁止竞业
							    getDBString(o, "c26").equals("0") ? "gone":"",
							    getDBString_BO(o, "c27", "", "", "年", hasO2_hcc?o2:null, 1),//禁止竞业年限
							    toBO(c16, c16_o2, 1, true), //工作职责   HIUtil.toHtmlStr()
								isEmpty_BO(o, "c17", true, hasO2_hcc?o2:null) ? "gone":"",
								workWeight_str, //工作权重
								isEmpty(c29) && isEmpty(c29_o2)? "gone":"",
								toBO(c29, c29_o2, 1, true), //工作业绩  HIUtil.toHtmlStr()
								isEmpty_BO(o, "c25", hasO2_hcc?o2:null) ? "gone":"",
								getDBString_BO(o, "c25", "", "", "", hasO2_hcc?o2:null, 1, true), //离职原因  HIUtil.toHtmlStr()
						}));
						break;
					case 8: // 简历-项目经验
						s.append(String.format(HTML, new Object[]{
								getDBString_BO(o, "c3", "", "", "", o2, 1),
								getDBString_BO(o, "c4", "", "", "", o2, 1),
								getDBString_BO(o, "c5", "", "", "", o2, 1).replaceAll("(.+?)\\[", "").replaceAll("\\]", ""),//公司名称
								isEmpty_BO(o, "c6", o2) ?"gone":"",
								getDBString_BO(o, "c6", "", "", "", o2, 1), //职位
								getDBString_BO(o, "c7", "", "", "", o2, 1, true), //项目介绍  HIUtil.toHtmlStr()
								getDBString_BO(o, "c8", "", "", "", o2, 1, true), //工作职责  HIUtil.toHtmlStr()
								isEmpty_BO(o, "c9", o2) ?"gone":"",
								getDBString_BO(o, "c9", "", "", "", o2, 1, true), //项目成就  HIUtil.toHtmlStr()
						}));
						break;

					case 9: // 简历-技能标签
						s.append(String.format(HTML, new Object[]{
								isEmpty_BO(o, "c52", hasO2_hcc?o2:null) && isEmpty_BO(o, "c53", hasO2_hcc?o2:null) && isEmpty_BO(o, "c54", hasO2_hcc?o2:null) ? "gone":"",
								isEmpty_BO(o, "c52", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c52", "", "", "", hasO2_hcc?o2:null, 1).replaceAll(",", "，"),
								isEmpty_BO(o, "c53", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c53", "", "", "", hasO2_hcc?o2:null, 1).replaceAll(",", "，"),
								isEmpty_BO(o, "c54", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c54", "", "", "", hasO2_hcc?o2:null, 1).replaceAll(",", "，"),
						}));
						break;

					case 10: // 简历-工作期望
						s.append(String.format(HTML, new Object[]{
								isEmpty_BO(o, "c37", hasO2_hcc?o2:null) && isEmpty_BO(o, "c46", hasO2_hcc?o2:null) && isEmpty_BO(o, "c47", hasO2_hcc?o2:null) && isEmpty_BO(o, "c48", hasO2_hcc?o2:null) && isEmpty_BO(o, "c49", hasO2_hcc?o2:null) && isEmpty_BO(o, "c50", hasO2_hcc?o2:null) && isEmpty_BO(o, "c51", hasO2_hcc?o2:null) && isEmpty_BO(o, "c63", hasO2_hcc?o2:null) && isEmpty_BO(o, "c64", hasO2_hcc?o2:null) ? "gone":"",
								isEmpty_BO(o, "c46", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c46", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c47", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString(o, "c47").startsWith("227") ? this.getDBDataParam_BO(o, "c47", "", "", "", hasO2_hcc?o2:null, 1) : this.getDBString_BO(o, "c47", "", "", "", hasO2_hcc?o2:null, 1),//职级
								isEmpty_BO(o, "c48", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c48", "", "", "", hasO2_hcc?o2:null, 1).replaceAll(",", "，"),
								isEmpty_BO(o, "c49", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c49", "", "", "", hasO2_hcc?o2:null, 1),
								isEmpty_BO(o, "c51", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c51", "", "", "万/年", hasO2_hcc?o2:null, 1),//期望薪资
								isEmpty_BO(o, "c37", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c37", "", "", "", hasO2_hcc?o2:null, 1).replaceAll("1", "接受").replaceAll("0", "不接受"),//Contractor
								isEmpty_BO(o, "c63", hasO2_hcc?o2:null) ? "gone" : "",
								this.getDBString_BO(o, "c63", "", "", "", hasO2_hcc?o2:null, 1).replaceAll("\\[(.+?)\\]", ""),//意向公司
								isHR() ? "gone" : (isEmpty_BO(o, "c64", hasO2_hcc?o2:null) ? "gone" : ""),
								isHR() ? "" :this.getDBString_BO(o, "c64", "", "", "", hasO2_hcc?o2:null, 1),//屏蔽公司
						}));
						break;
						
					case 11: // 简历-顾问面评
						html = HTML.replaceAll("<INDEX>", String.valueOf(index));
						
						String language = "";
						String[] ss = this.getDBString(o, "c15").split(SP1);
						for(String a: ss){
							language += "<br>" + "<span class=\"tab-dot-s\">"+ComBeanI_DataParamType.getTexts(a, SP2).replaceAll(SP2, " | ")+"</span>";
						}
						language = language.substring("<br>".length());

						String language2 = null;
						if(o2!=null){
							language2 = "";
							ss = this.getDBString(o2, "c15").split(SP1);
							for(String a: ss){
								language2 += "<br>" + "<span class=\"tab-dot-s\">"+ComBeanI_DataParamType.getTexts(a, SP2).replaceAll(SP2, " | ")+"</span>";
							}
							language2 = language2.substring("<br>".length());
						}
						
						String manageArea = ComBeanI_DataParamType.getTexts(getDBString(o, "c22"), ",");
						String manageArea2 = null;
						if(o2!=null){
							manageArea2 = ComBeanI_DataParamType.getTexts(getDBString(o2, "c22"), ",");
						}

						System.out.println("[CandiDetailService.getUserName]======================================="+this.getDBString(o, "c2"));
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "hcFace","../doc/images/gw_face.png","",""),// 加一个顾问的头像
						        ComBeanLoginUser.getUserName(this.getDBString(o, "c2")), //顾问
						        this.getDBDate(o, "uptdate", "yyyy-MM-dd"),
						        isEmpty_BO(o, "c13", o2) ? "gone" : "",
						        getDBString_BO(o, "c13", "", "", "", hasO2_hcc?o2:null, 1, true),//性格
						        isEmpty_BO(o, "c14", o2) ? "gone" : "",
						        this.getDBDataParam_BO(o, "c14", "", "", "", hasO2_hcc?o2:null, 1),//沟通能力
						        HIUtil.isEmpty(language+language2) ? "gone" : "",
						        toBO(language, hasO2_hcc?language2:null, 1, false),//语言水平
						        isEmpty_BO(o, "c16", o2) ? "gone" : "",// 缺点
						        getDBString_BO(o, "c16", "", "", "", hasO2_hcc?o2:null, 1, true),
						        isEmpty_BO(o, "c21", o2) && isEmpty_BO(o, "c22", o2) ? "gone" : "",//控制这个版块
						        this.getDBString_BO(o, "c20", "", "", "", hasO2_hcc?o2:null, 1),//行业经验 目前是gone
						        isEmpty_BO(o, "c21", o2) ? "gone" : "",
						        this.getDBString_BO(o, "c21", "", "", "", hasO2_hcc?o2:null, 1),//管理经验
						        isEmpty_BO(o, "c22", o2) ? "gone" : "",
						        toBO(manageArea, hasO2_hcc?manageArea2:null, 1, false),//管辖区域范围
						        isEmpty_BO(o, "c9", o2) ? "gone" : "",
						        this.getDBString_BO(o, "c9", "", "", "", hasO2_hcc?o2:null, 1, true),//备注  HIUtil.toHtmlStr()
						      }));
						break;
					case 12: // 简历-顾问面试
						s.append(String.format(HTML, new Object[]{
								this.getDBDate(o, "c7", "yyyy-MM-dd HH:mm"),
								this.getDBString(o, "c9"),
						}));
						break;
					}
				}
			}
			
			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return result;
	}
}
