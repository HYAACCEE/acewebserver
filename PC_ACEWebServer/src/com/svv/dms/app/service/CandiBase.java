package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gs.db.dao.DaoUtil;
import com.gs.db.database.BizDBResult;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAOrgan;
import com.svv.dms.app.ComBeanAction;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.ComBeanProcess;
import com.svv.dms.app.EmailUtil;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.util.HIUtil;

public class CandiBase extends LWBaseBean {
	//queryMK //进行中 1 ，已关注2，已结束3
	
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1982996236041863843L;
	public final static String[] LWCandidate_CVOtherDetails_MEMO = 
			new String[]{"", "基本信息", "", "教育", "培训", "证书", "任职经历", "工作经验", "项目经验", "技能标签", "工作期望", "顾问面评", "顾问面试"};


	//HR: 转发与下载
	protected boolean powerForHR_export(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.length()>0 && Integer.parseInt(processInnerStatus) >= Integer.parseInt(ACE.CANDI_PROCESS_INNERSTATUS_WAIT_IV); //已上线、流程ing、同意面试
	}
	//HR: 邀请面试
	protected boolean powerForHR_inviteIV(String activeStatus, String candiStatus, String processCloseStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && (candiStatus.equals("") || candiStatus.equals(ACE.CANDI_STATUS_OVER) && !processCloseStatus.equals(ACE.CANDI_PROCESS_CLOSESTATUS_ENTRY) && !processCloseStatus.equals(ACE.CANDI_PROCESS_CLOSESTATUS_DIMISSION)); //已上线、未有流程/已结束
	}
	//HR: 已邀请面试
	protected boolean powerForHR_invited(String activeStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_INVITED); //已上线、已邀请面试
	}
	//HR: 安排面试
	protected boolean powerForHR_addIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_WAIT_IV); //已上线、进行中、待安排面试
	}
	//HR: 面试变更
	protected boolean powerForHR_editIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && (processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IVING) || processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_CONFIRM)); //已上线、进行中、已安排面试/已确定面试
	}
	//HR: 面试信息确定中
	protected boolean powerForHR_confirmingIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IVING); //已上线、进行中、已安排面试
	}
	//HR: 面试通过
	protected boolean powerForHR_setIVResult_Pass(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_OVER); //已上线、进行中、面试结束
	}
	//HR: 发Offer
	protected boolean powerForHR_Offer(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_SUCCESS); //已上线、进行中、面试通过
	}
	//HR: 上传Offer
	protected boolean powerForHR_addOffer(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER); //已上线、进行中、已offer
	}
	//HR: Offer变更
	protected boolean powerForHR_editOffer(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_UPLOADED); //已上线、进行中、已上传Offer
	}
	//HR: 入职
	protected boolean powerForHR_setEntryResult(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_ACCEPTED); //已上线、进行中、已接受Offer
	}
	//HR: 入职待确认中
	protected boolean powerForHR_entrying(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_ENTRY); //进行中、已接受Offer
	}
	//HR: 离职
	protected boolean powerForHR_setDimission(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && (ACE.CANDI_PROCESS_INNERSTATUS_BILLING+ACE.CANDI_PROCESS_INNERSTATUS_BILLED+ACE.CANDI_PROCESS_INNERSTATUS_PAYING_ToM+ACE.CANDI_PROCESS_INNERSTATUS_PAYED_ToM+ACE.CANDI_PROCESS_INNERSTATUS_PAYING_ToHC+ACE.CANDI_PROCESS_INNERSTATUS_PAYED_ToHC+ACE.CANDI_PROCESS_INNERSTATUS_COMPLETE).indexOf(processInnerStatus)>=0; //待开票 / 已开票 / 付款 / 已付款
	}
	//HR: 离职待确认中
	protected boolean powerForHR_dimissioning(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_DIMISSION_CHECKING); //离职待确认
	}
	//HR: 关注
	protected boolean powerForHR_collect(String collectStatus){
		return !collectStatus.equals(ACE.COLLECT_STATUS_COLLECTED); //未关注
	}
	//HR: 取消关注
	protected boolean powerForHR_unCollect(String collectStatus){
		return collectStatus.equals(ACE.COLLECT_STATUS_COLLECTED); //已关注
	}
	//HR: 加入黑名单
	protected boolean powerForHR_addBlackList(String candiStatus, String collectStatus){
		return !candiStatus.equals(ACE.CANDI_STATUS_ING) && !collectStatus.equals(ACE.COLLECT_STATUS_BLACKLIST); //非黑名单
	}
	//HR: 移出黑名单
	protected boolean powerForHR_delBlackList(String collectStatus){
		return collectStatus.equals(ACE.COLLECT_STATUS_BLACKLIST); //黑名单
	}
	//HR: 待开票
	protected boolean powerForHR_billing(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_ENTRY) || candiStatus.equals(ACE.CANDI_STATUS_OVER) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_BILLING);
	}
	//HR: 待付款到平台
	protected boolean powerForHR_billPayingToM(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_BILLED);
	}
	//HR: 付款给顾问
	protected boolean powerForHR_billPayedToM(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && (processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_PAYED_ToM));
	}
	//HR: 已付款
	protected boolean powerForHR_billPayedToHC(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && (processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_PAYING_ToHC) || processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_PAYED_ToHC));
	}
	//HC: 申请入库
	protected boolean powerForHC_applyST(String stStatus, String stCheckStatus){
		return stStatus.equals(ACE.CANDI_STStatus_NOTST) && (stCheckStatus.equals("") || stCheckStatus.equals(ACE.CANDI_STCheckStatus_NOTST) || stCheckStatus.equals(ACE.CANDI_STCheckStatus_STREFUSED)); //未入库、初始/审核未通过
	}
	//HC: 取消入库
	protected boolean powerForHC_cancelST(String stStatus, String stCheckStatus){
		return stStatus.equals(ACE.CANDI_STStatus_PREST) && (stCheckStatus.equals(ACE.CANDI_STCheckStatus_ST_CHECKING)); //未入库、审核中
	}
	//HC: 顾问面试
	protected static boolean powerForHC_addHCIV(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_NO_IV); //待安排顾问面试
	}
	//HC: 简历更新
	protected static boolean powerForHC_editCV(String stStatus, String stCheckStatus, String cvStatus){
		return powerForHC_editHCComment(stCheckStatus) || stStatus.equals(ACE.CANDI_STStatus_NOTST) || cvStatus.equals(ACE.CANDI_CVStatus_UNPASS) || stStatus.equals(ACE.CANDI_STStatus_ST) && cvStatus.equals(ACE.CANDI_CVStatus_ONLINE); //未入库  / (已入库、已上线)
	}
	//HC: 提交简历更新
	protected static boolean powerForHC_cmtCVUpdate(String stStatus, String stCheckStatus, String cvStatus){
		return powerForHC_editHCComment(stCheckStatus) || stStatus.equals(ACE.CANDI_STStatus_ST) && (cvStatus.equals(ACE.CANDI_CVStatus_ONLINE) || cvStatus.equals(ACE.CANDI_CVStatus_UNPASS)); //已入库、已上线/不通过
	}
	//HC: 简历更新审核中
	protected static boolean powerForHC_checkingCV(String cvStatus){
		return cvStatus.equals(ACE.CANDI_CVStatus_CHECKING); //简历审核中
	}
	//HC: 顾问面试变更
	protected static boolean powerForHC_editHCIV(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_IVING); //已安排顾问面试
	}
	//HC: 编辑顾问面评
	protected static boolean powerForHC_editHCComment(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_IVOVER) || stCheckStatus.equals(ACE.CANDI_STCheckStatus_NO_HCCOMMENT) || stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_REFUSED); //顾问面试已结束 / 待提交顾问面试 / 顾问面评未通过
	}
	//HC: 提交顾问面评
	protected static boolean powerForHC_commitHCComment(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_NO_HCCOMMENT) || stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_REFUSED); //待提交顾问面评 / 顾问面评未通过
	}
	//HC: 已提交顾问面评
	protected static boolean powerForHC_checkingHCComment(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_CHECKING); //已提交顾问面评审核中
	}
	//HC: 顾问面评已上线
	protected static boolean powerForHC_onlineHCComment(String stCheckStatus){
		return stCheckStatus.equals(ACE.CANDI_STCheckStatus_HCCOMMENT_ONLINE); //顾问面评已上线
	}
	//HC: 候选人面试反馈（回复邀请面试）
	protected boolean powerForHC_candidateReplyIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_INVITED); //已入库、进行中、已邀请面试
	}
	//HC: 安排面试
	protected boolean powerForHC_addHRIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_WAIT_IV); //已入库、进行中、待安排面试
	}
	//HC: 确定面试
	protected boolean powerForHC_confirmHRIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IVING); //已入库、进行中、已安排面试
	}
	//HC: 面试变更
	protected boolean powerForHC_editHRIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_CONFIRM); //已入库、进行中、已确定面试
	}
	//HC: 面试反馈（回复面试结果）
	protected boolean powerForHC_setIVResult(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_OVER); //已入库、进行中、面试结束
	}
	//HC: 确认下一轮面试
	protected boolean powerForHC_nextIV(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_SUCCESS); //已入库、进行中、面试通过
	}
	//HC: 候选人offer反馈（回复offer）
	protected boolean powerForHC_candidateReplyOffer(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_UPLOADED); //已入库、进行中、已上传offer
	}
	//HC: 候选人入职
	protected boolean powerForHC_CandidateReplyEntry(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_ACCEPTED); //已入库、进行中、已接收offer
	}
	//HC: 候选人确认入职
	protected boolean powerForHC_CandidateEntryConfirm(String activeStatus, String candiStatus, String processInnerStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE) && candiStatus.equals(ACE.CANDI_STATUS_ING) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_ENTRY); //已入库、进行中、已入职
	}
	//HC: 备注原因:离职原因
	protected boolean powerForHC_setDimissionReason(String processInnerStatus){
		return processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_DIMISSION_CONFIRM); //已离职
	}
	//HC: 备注原因:面试未通过原因
	protected boolean powerForHC_setUnPassIVReason(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_FAILED); //面试未通过
	}
	//HC: 备注原因:未入职
	protected boolean powerForHC_setUnEntryReason(String candiStatus, String processInnerStatus){
		return candiStatus.equals(ACE.CANDI_STATUS_OVER) && processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_NOTENTRY); //未入职
	}
	//HC: InActive
	protected boolean powerForHC_inActive(String activeStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE); //已上线
	}
	//HC: ActiveChecking
	protected boolean powerForHC_activeChecking(String activeStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_CHECKING_ACTIVE); //上线审核中
	}
	//HC: Active
	protected boolean powerForHC_activeApply(String activeStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_INACTIVE); //已入库不上线
	}
	//HC: UnBlock
	protected boolean powerForHC_unBlock(String activeStatus){
		return activeStatus.equals(ACE.CANDI_ActiveStatus_BLOCK); //已入职
	}
	
	/****************************************************************************************************/
	//Inactive候选人
	protected LWResult Process_inactive(String cid, String reason, String url){
		LWResult result = null;
		result = P_doProcess(ACE.PROCESS_CODE_INACTIVE, cid, "", "", reason); //=========================================流程事件
		if(result.isSuccess()){
			List<Object> list = dbQuery(String.format("select a.c5 pcid from B_Candidate_Process a where a.c1='%s' and a.c7=%s", new Object[]{cid, ACE.CANDI_STATUS_ING}));
		    if(list!=null && list.size()>0){
		    	for(Object o: list){
		    		this.P_doProcess(ACE.PROCESS_CODE_STOP,
                                     cid,
                                     this.getDBString(o, "pcid"),
                                     "",
                                     "状态变为Inactive"
                     );
		    	}
		    }
		    
		    if(!HIUtil.isEmpty(url)){
			    list = dbQuery(String.format("select distinct b.c7 email from B_Candidate_Process a, B_HR b where a.c4=b.c1 and a.c1='%s' and a.c21=%s", new Object[]{cid, ACE.COLLECT_STATUS_COLLECTED}));
			    if(list!=null && list.size()>0){
					Object c = dbQuery("select c8,c11,c13,c15,c21,c25,c39,c41,c42,p.c43/1000 yuexin_k from BO_Candidate p where c1='"+cid+"'").get(0);
			    	String email;
					for(Object o: list){
						email = getDBString(o, "email");
						if(!HIUtil.isEmpty(email)){
					    	StringBuilder emailContent = new StringBuilder()
					        .append("<html><body style=\"margin:0px; color:#565656; font-family:微软雅黑;\">")
		                    .append("<div><a href='"+url+"'><img src=\"http://www.ace-elite.com/doc/images/email_top.png\" style=\"width:900px;border:0px;\"></a></div>")
					        .append("<div style=\"height:40px\"></div>")
							.append("<div><span style=\"color: #565656;font-size:18px;font-weight: bold\">您收藏的候选人已不看机会</span></div>")
					        .append("<div style=\"height:40px\"></div>")
					        .append("<div>")   
					        .append("<span><a href=\""+url+"/acehr/candidate_detail_cv.html?g=098149516444715642701").append(cid).append("\" style=\"color:#4990B1; text-decoration:none; font-weight:bold; font-size:16px;\">").append(getDBString(c, "c41")).append(getDBString(c, "c42", "", " - ", "")).append("</a></span>") 
					        .append("</div>")
					        .append("<div>")  
					        .append("<span style=\"display:inline-block; width:560px; color:#A0A0A0;font-size:12px;\">").append(getDBDataParam(c, "c13", "", "", "")).append(getDBString(c, "c11",""," | ","岁")).append(getDBDataParam(c, "c25", "", " | ", "")).append(getDBInt(c, "c39", "", " | ", "年工作经验")).append(getDBString(c, "yuexin_k", "", " | ", "K/月")).append(getDBString(c, "c15", "", " | ", "")).append("</span>")
		                    .append("<span style=\"display:inline-block; padding-left:10px; padding-right:10px; height:30px; border:1px solid #e0e0e0; background:#e0e0e0; color:#a0a0a0;\">已不看机会</span>")
					        .append("</div>")
					        .append("<div style=\"height:20px;\"></div>")
					        .append("<div style=\"width:660px; height:1px; background:#c0c0c0;\"></div>")
					        .append("<div style=\"height:20px;\"></div>")
					        .append("<div style=\"color: #909090;\">为了给您呈现最精准的候选人信息，ACE会实时更新候选人的简历和求职状态<div>")
					        .append("<div style=\"height:20px;\"></div>")
					        .append("<div><a href=\""+url+"/acehr/my_candidate_process_list_collect.html\" style=\"text-decoration:none;\"><span style=\"display:inline-block; width:630px; height:20px; padding:15px; text-align:center; background:#FFA942; color:#ffffff; border:1px solid #FFA942;\">查看我收藏的候选人</span></a></div>")
					        .append("<div style=\"height:8px;\"></div>")
					        .append("<div><a href=\""+url+"/acehr/index.html\" style=\"text-decoration:none;\"><span style=\"display:inline-block; width:630px; height:20px; padding:15px; text-align:center; background:#ffffff; color:#ffa942; border:1px solid #FFA942;\">查看每周精选的候选人</span></a></div>")
					        .append("<div style=\"height:20px;\"></div>")
							.append("<div style=\"height:100px\"></div>")
							.append("<div><a style=\"color: #565656;font-size:14px;font-weight: bold\" href=\"www.ace-elite.com\">www.ace-elite.com</a></div>")
					        .append("<div style=\"color: #A0A0A0;font-size:12px;\">如有任何问题请联系我们，我们将尽快为您解答。 电话：4006-2828-35</div>")
					        .append("<div style=\"height:20px;\"></div>")
		                    .append("<div><img src=\"http://www.ace-elite.com/doc/images/email_bottom.png\" style=\"width:900px;border:0px;\"></div>")
					        .append("</body></html>");
							String sendEmailResult = EmailUtil.sendEmailFile(email, "【ACE】您收藏的候选人已不看机会", emailContent.toString(),"");
						}
					}
			    }
		    }
		}
		return result;
	}
	//候选人入职Block其他流程
	private void Process_entryBlock(String cid, String pcid){
		List<Object> list = dbQuery(String.format("select a.c5 pcid from B_Candidate_Process a where a.c1='%s' and a.c7=%s and a.c5<>%s", new Object[]{cid, ACE.CANDI_STATUS_ING, pcid}));
	    if(list!=null && list.size()>0){
	    	for(Object o: list){
	    		this.P_doProcess(ACE.PROCESS_CODE_STOP,
                                 cid,
                                 this.getDBString(o, "pcid"),
                                 "",
                                 "入职其他公司"
                 );
	    	}
	    }
	}
	//申请入库
	protected LWResult Process_stApply(String cid){
		LWResult result = null;
		DaoUtil.dbExe("SP_CandidateCVCheck", new Object[]{cid});
		BizDBResult dr = DaoUtil.dbExe("SP_stApplyCheck", new Object[]{cid});
		if(dr.getResult()){
			result = checkActiveNumLimit(auid);
			if(result==null){
			    result = P_doProcess(ACE.PROCESS_CODE_ST_APPLY, cid); //=========================================流程事件
			}
		}else{
			result = new LWResult(LWResult.FAILURE, "<span class=\"grey\">原因：</span><span class=\"red\">"+dr.getInfo()+"</span>");
		}
		return result;
	}
	/****************************************************************************************************/
	protected LWResult P_doProcess(String processCode, String cid){
		return F_doProcess(processCode, cid, "", "", "", "", "", "", new String[]{""});
	}
	protected LWResult P_doProcess(String processCode, String cid, String pcid, String tid, String detail){
		return F_doProcess(processCode, cid, pcid, tid, "", "", "", detail, new String[]{""});
	}
	protected LWResult P_doProcess(List<String> sqls_, String processCode, String cid, String pcid, String tid, String detail){
		return F_doProcess(sqls_, processCode, cid, pcid, tid, "", "", "", detail, new String[]{""});
	}
	protected String P_getActionSQL(String actionCode, String cid, String detail){
		ProcessParam = new ProcessParam("", "", detail);
		return this.F_getActionSQL(cid, "", "", "", "", new ComBeanAction(actionCode), "", true, detail);
	}
	protected String P_getActionSQL(String actionCode, String cid, String pid, String pcid, String tid, String processType){
		return this.F_getActionSQL(cid, pid, pcid, tid, "", new ComBeanAction(actionCode), "", true, "");
	}
	
	protected LWResult F_doProcess(String processCode, String cid, String pcid, String tid, String jobid, String job, String noticeTime, String detail, String[] vv){
	    return this.F_doProcess(null, processCode, cid, pcid, tid, jobid, job, noticeTime, detail, vv);
	}
	protected LWResult F_doProcess(List<String> sqls_, String processCode, String cid, String pcid, String tid, String jobid, String job, String noticeTime, String detail, String[] vv){
		List<String> sqls = sqls_==null ? new ArrayList<String>() : sqls_;
		String processContent = "";
		ProcessParam = new ProcessParam(job, noticeTime, detail);
		
		System.out.println("[CandiProcessServiceBean.F_doProcess]=======================================");
		LWResult result = null;
		String memo = "";
		try{
			if(processCode.length()==0 || cid.length()==0){
				result = new LWResult(LWResult.FAILURE, "失败，参数为空！");
				return result;
			}
			
			ComBeanProcess PROCESS = new ComBeanProcess(processCode);
			processContent  = String.format(HIUtil.isEmpty(PROCESS.FormatContent(), "%s"), vv); //流程详情/////////////////////////////////////////////////////////
			
			memo = PROCESS.name();
			if(memo.length()==0){
				result = new LWResult(LWResult.FAILURE, memo+"失败，未设置模块2！");
				return result;
			}
	
			//校验候选人状态
			List<Object> list_cp = null;
			if(PROCESS.type().equals(ACE.PROCESS_TYPE_ST)){ //入库流程
				if(isM()){
				    //list_cp = HIUtil.dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, this.getParameter("s_auid"), this.getParameter("s_amid")));
				    list_cp = HIUtil.dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, "", ""));
				}else if(isHC()){
				    list_cp = HIUtil.dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, auid, myamid));
				}
			}else{
				if(isHC()||isM()){
				    list_cp = HIUtil.dbQuery(YSSQL.LWCandidateProcess_QueryByCID("", cid, pcid, "", "", "", "", ""));
				}else if(isHR()){
				    list_cp = dbQuery(YSSQL.LWCandidateProcess_QueryByCID("O", cid, pcid, pcid.length()>0 ? "":"130001", "", "", auid, myamid));
				}
		        if(list_cp==null || list_cp.size()==0){
					result = new LWResult(LWResult.FAILURE, memo+"失败，候选人不存在或状态不符！");
					return result;
		        }
			}
	        Object cp = list_cp.get(0);
	        String pid = getDBString(cp, "pid");
	        pcid = getDBString(cp, "pcid");
	        String stStatus = getDBString(cp, "stStatus");
	        String stcheckStatus = getDBString(cp, "stcheckStatus");
	        String activeStatus = getDBString(cp, "activeStatus");
	        String cvStatus = getDBString(cp, "cvStatus");
	        String candiStatus = getDBString(cp, "candiStatus");
	        String processStatus = getDBString(cp, "processStatus");
	        String billStatus = getDBString(cp, "billStatus");
	        String collectStatus = getDBString(cp, "collectStatus");
	        String processCloseStatus = getDBString(cp, "processCloseStatus");
	        String processCloseStatusDesc = getDBString(cp, "processCloseStatusDesc");
	        String processInnerStatus = getDBString(cp, "processInnerStatus");
	        String processInnerStatusDesc = getDBString(cp, "processInnerStatusDesc");
	        
	        //校验候选人流程状态
	    	boolean pass = true;
	    	String needStatus = "";
	    	String errStatus = "";
	    	String[] conditions = {PROCESS.condition_stStatus(), PROCESS.condition_stcheckStatus(), PROCESS.condition_cStatus(), PROCESS.condition_pStatus(), PROCESS.condition_billStatus(), PROCESS.condition_collectStatus(), PROCESS.condition_activeStatus(), PROCESS.condition_processInnerStatus(), PROCESS.condition_cvStatus()};
	    	String[] checkStatus = {stStatus, stcheckStatus, candiStatus, processStatus, billStatus, collectStatus, activeStatus, processInnerStatus, cvStatus};
	    	System.out.println("[CONDITION] " + Arrays.toString(conditions));
	    	System.out.println("[mystatus] " + Arrays.toString(checkStatus));
	    	for(int k=0; k<conditions.length; k++){
	            if(conditions[k].length() > 0){
	    	    	System.out.println("[CONDITION]["+k+"] " + conditions[k]);
	    	    	System.out.println("[mystatus]["+k+"] " + checkStatus[k]);
	            	pass = false;
	            	String[] sss = conditions[k].split(",");
	            	for(String s: sss){
		    	    	System.out.println("[CONDITION]["+k+"] " + s);
		    	    	System.out.println("[mystatus]["+k+"] " + checkStatus[k]);
	            		if(!s.startsWith("!") && s.equals(checkStatus[k])) pass = true;
	            		if(s.startsWith("!") && !s.equals(checkStatus[k])) pass = true;
	            		if(s.startsWith("!") && s.substring(1).equals(checkStatus[k])){
	            			needStatus = ComBeanI_DataParamType.getTexts(s, ",");
	            			errStatus = ComBeanI_DataParamType.getTexts(checkStatus[k], ",");
	            			pass = false; break;
	            		}
	            		if(!s.startsWith("!") && !s.equals(checkStatus[k])){
	            			errStatus = ComBeanI_DataParamType.getTexts(checkStatus[k], ",");
	            		}
	            	}
	    	    	System.out.println("[errStatus]["+k+"] " + errStatus);
	            	if(!pass) break;
	            }
	    	}
	    	if(!pass){
				result = new LWResult(LWResult.FAILURE, memo+"失败<br><br><span class='f12 grey'>候选人状态 <span class='black'><"+errStatus+"></span> 不符！</span>");
				return result;
	    	}

	    	boolean isNewProcess = PROCESS.code().equals(ACE.PROCESS_CODE_INVITE);
	    	if(!isNewProcess){
		        if(jobid.length()==0) jobid = getDBString(cp, "jobID");
		        if(job.length()==0) job = getDBString(cp, "jobName");
	    	}
	        initParams(cid, pid, jobid, isNewProcess, job);//========================================================================初始化参数 
	        processContent = format(processContent);
			
			if(PROCESS.type().equals(ACE.PROCESS_TYPE_ST) //入库流程
					|| PROCESS.type().equals(ACE.PROCESS_TYPE_STATUS) //状态流程
					|| PROCESS.type().equals(ACE.PROCESS_TYPE_CV) //简历变更流程
					){
				//Z
				sqls.add(this.sqlToZ("B_Candidate", memo, "c1="+cid));
				//更新流程状态与原因
				sqls.add(String.format("update B_Candidate set c3=%s,c5='%s',c71=%s,c77='%s',c78='%s',c79=sysdate,c80='%s',c82='%s',c90='%s',uptdate=sysdate where c1='%s'", new Object[]{
						HIUtil.isEmpty(PROCESS.next_stStatus(), stStatus), //入库状态 c3
						HIUtil.isEmpty(PROCESS.next_activeStatus(), activeStatus), //上线状态 c5
						HIUtil.isEmpty(PROCESS.next_stcheckStatus(), stcheckStatus), //入库审核状态 c71
						auid, //操作人 c77
						ComBeanLoginUser.getUserName(auid), //操作人姓名 c78 (操作时间 c79)
						memo, //操作说明 c80
						HIUtil.isEmpty(PROCESS.next_cvStatus(), cvStatus), //简历状态 c82
						PROCESS.FormatTitle(), //入库审核状态说明 c90
						cid,
				}));
				
				if(PROCESS.code().equals(ACE.PROCESS_CODE_ST_APPLY)){ //申请入库：申请入库时间
					sqls.add(String.format("update B_Candidate set c81=sysdate where c1='%s'", new Object[]{cid}));
				}
				if(PROCESS.code().equals(ACE.PROCESS_CODE_ST_SUCCESS)){ //入库成功
					//入库时间
					sqls.add(String.format("update B_Candidate set c4=sysdate where c1='%s'", new Object[]{cid}));
				}
				if(PROCESS.code().equals(ACE.PROCESS_CODE_HCCOMMENT_SUCCESS)){ //顾问面评通过
					//顾问面评通过、顾问面评上线时间
					sqls.add(String.format("update B_Candidate set c85=9, c87=sysdate where c1='%s'", new Object[]{cid}));
				    sqls.add(this.sqlToZ("BO_Candidate_HCComment", memo, "c1="+cid));
				    sqls.add(String.format("delete from BO_Candidate_HCComment where c1='%s'", new Object[]{cid}));
				    sqls.add(String.format("insert into BO_Candidate_HCComment select * from B_Candidate_HCComment where c1='%s'", new Object[]{cid}));
				}
				if(!HIUtil.isEmpty(PROCESS.next_activeStatus())){ //上线状态
					//上下线时间
					sqls.add(String.format("update B_Candidate set c6=sysdate where c1='%s'", new Object[]{cid}));
				}
				if(PROCESS.next_activeStatus().equals(ACE.CANDI_ActiveStatus_ACTIVE)){ //候选人上线
					sqls.add(String.format("update B_Candidate set c95='待生成', c84=sysdate where c1='%s'", new Object[]{cid}));
					//精选///////////////////////////////////c91=1为精选 ，c91=0不是精选，2017-04-20 by walle 变更为0
					sqls.add(String.format("update B_Candidate set c91=0 where c1='%s'", new Object[]{cid}));
				}
				if(PROCESS.code().equals(ACE.PROCESS_CODE_HCIV) || PROCESS.code().equals(ACE.PROCESS_CODE_HCIV_EDIT)) //顾问面试：面试时间
					sqls.add(String.format("update B_Candidate set c72=to_date('%s','yyyy-mm-dd hh24:mi') where c1='%s'", new Object[]{noticeTime, cid}));
				if(PROCESS.code().equals(ACE.PROCESS_CODE_HCCOMMENT_COMMIT)) //提交顾问面评：需填提交面评时间
					sqls.add(String.format("update B_Candidate set c73=sysdate where c1='%s'", new Object[]{cid}));
				
			}else { //标准流程、(开票流程)
	//			if(processType.equals(ACE_DataParam.PROCESS_TYPE_HRSTANDARD)){ //HR标准流程
				
				if(pid.length()==0 || (PROCESS.code().equals(ACE.PROCESS_CODE_INVITE) && !candiStatus.equals(""))){ //新建
					pid = this.getSqlValue("select seqB_Candidate_Process.nextval cc from dual", "cc");
					sqls.add(String.format("update B_Candidate_Process set c2=130666, uptdate=sysdate where c1='%s' and c4='%s'", new Object[]{cid, auid}));
					sqls.add(String.format("insert into B_Candidate_Process(dataid,parentDataid,c1,c2,c3,c4,c6,c7,c8,c9,c10,c11,c12,c13,c16,c17,c18,c21, c27,c28,c29,c30,c31,c32,c33,c34,c35,c36,c38) select %s,dataid,c1,130001,c2,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', '%s','%s',sysdate,'%s','%s','%s','%s','%s','%s','%s',%s from B_Candidate where c1='%s'", new Object[]{
							pid,
							auid, //HR c4 //
							PROCESS.type(), //流程类型 c6 (流程PCID c5)
							HIUtil.isEmpty(PROCESS.next_cStatus(), candiStatus), //候选人状态 c7
							HIUtil.isEmpty(PROCESS.next_pStatus(), processStatus), //流程状态 c8
							format(PROCESS.FormatTitle()), //流程标题 c9
							format(PROCESS.FormatDesc()), //流程说明 c10
							processContent, //流程详情 c11  //PROCESS.FormatContent()
							PROCESS.next_pcloseStatus(), //结束状态 c12
							HIUtil.isEmpty(PROCESS.next_pcloseStatus()) ? processCloseStatusDesc: processContent, //结束状态说明 c13 /////////////////////////////////////
							HIUtil.isEmpty(PROCESS.next_processInnerStatus(), processInnerStatus), //流程内部状态 c16
							HIUtil.isEmpty(PROCESS.next_processInnerStatus()) ? processInnerStatusDesc: format(PROCESS.FormatDesc()), //流程内部状态说明 c17
							PROCESS.addIVTimesFlag() ? 1 : 0, //已面试轮次 c18
							HIUtil.isEmpty(PROCESS.next_collectStatus(), collectStatus), //关注或黑名单状态 c21
							auid, //操作人 c27
							ComBeanLoginUser.getUserName(auid), //操作人姓名 c28 (操作时间 c29)
							memo, //操作说明 c30
							myamid, //HR公司AMID c31
							mymid, //HR公司MID c32
							jobid, //附：职位ID c33
							ProcessParam.jobName, //附：职位名称 c34
							ProcessParam.hrName, //附：HR名称 c35
							ProcessParam.mmName, //附：公司简称 c36
							0, //流程流水号 c38
							cid, //c1
					}));
					
				}else{ //已有流程历史
					//Z
					sqls.add(this.sqlToZ("B_Candidate_Process", memo, "dataid="+pid));
	
					if(PROCESS.type().equals(ACE.PROCESS_TYPE_HR_STANDARD) || PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL)){ //HR标准流程
						//更新流程状态与原因
						sqls.add(String.format("update B_Candidate_Process set c6=%s,c7=%s,c8=%s,c9='%s',c10='%s',c11='%s',c12='%s',c13='%s',c16='%s',c17='%s',c18=NVL(c18,0)+%s, c19='%s',c20='%s',c21='%s', c27='%s',c28='%s',c29=sysdate,c30='%s',c33='%s',c34='%s',c35='%s',c36='%s',c37=%s,c38=NVL(c38,0)+1,uptdate=sysdate where dataid=%s and c1='%s'", new Object[]{
	    						PROCESS.type(), //流程类型 c6 (流程PCID c5)
								HIUtil.isEmpty(PROCESS.next_cStatus(), candiStatus), //候选人状态 c7
								HIUtil.isEmpty(PROCESS.next_pStatus(), processStatus), //流程状态 c8
								format(PROCESS.FormatTitle()), //流程标题 c9
								format(PROCESS.FormatDesc()), //流程说明 c10
								processContent, //流程详情 c11  //PROCESS.FormatContent()
								PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL) ? processCloseStatus : PROCESS.next_pcloseStatus(), //结束状态 c12
								PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL) || HIUtil.isEmpty(PROCESS.next_pcloseStatus()) ? processCloseStatusDesc: processContent, //结束状态说明 c13
								HIUtil.isEmpty(PROCESS.next_processInnerStatus(), processInnerStatus), //流程内部状态 c16
								HIUtil.isEmpty(PROCESS.next_processInnerStatus()) ? processInnerStatusDesc: format(PROCESS.FormatDesc()), //流程内部状态说明 c17
								PROCESS.addIVTimesFlag() ? 1 : 0, //已面试轮次
								HIUtil.isEmpty(PROCESS.next_billStatus(), billStatus), //票据状态 c19
								HIUtil.isEmpty(PROCESS.next_billStatus()) ? "" : (HIUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss") + "票据状态更新"), //票据说明 c20
								HIUtil.isEmpty(PROCESS.next_collectStatus(), collectStatus), //关注或黑名单状态 c21
								auid, //操作人 c27
								ComBeanLoginUser.getUserName(auid), //操作人姓名 c28 (操作时间 c29)
								memo, //操作说明 c30
	    						jobid, //附：职位ID c33
	    						ProcessParam.jobName, //附：职位名称 c34
	    						ProcessParam.hrName, //附：HR名称 c35
	    						ProcessParam.mmName, //附：公司简称 c36
	    						HIUtil.isEmpty(noticeTime) ? "''" : "to_date('"+noticeTime+"','yyyy-mm-dd hh24:mi')", //附：面试时间 c37
								pid, //dataid
								cid, //c1
						}));
						
					//开票流程 已改为 HR标准流程
//					}else if(PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL)){ //HR开票流程
//						//更新非标准流程状态
//						sqls.add(String.format("update B_Candidate_Process set c16='%s',c19='%s',c20='%s',c35='%s',c36='%s',c38=NVL(c38,0)+1,uptdate=sysdate where dataid=%s and c1='%s'", new Object[]{
//								HIUtil.isEmpty(PROCESS.next_processInnerStatus(), processInnerStatus), //流程内部状态 c16
//								HIUtil.isEmpty(PROCESS.next_billStatus(), billStatus), //票据状态 c19
//								HIUtil.isEmpty(PROCESS.next_billStatus()) ? "" : (HIUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss") + "票据状态更新"), //票据说明 c20
//	    						hrName, //附：HR名称 c35
//	    						mmName, //附：公司简称 c36
//								pid, //dataid
//								cid, //c1
//						}));

					}else if(PROCESS.type().equals(ACE.PROCESS_TYPE_HR_COLLECT)){ //HR关注流程
						//更新HR关注状态
						sqls.add(String.format("update B_Candidate_Process set c21='%s',c35='%s',c36='%s',c38=NVL(c38,0)+1,uptdate=sysdate where dataid=%s and c1='%s'", new Object[]{
								HIUtil.isEmpty(PROCESS.next_collectStatus(), collectStatus), //关注或黑名单状态 c21
								ProcessParam.hrName, //附：HR名称 c35
								ProcessParam.mmName, //附：公司简称 c36
								pid, //dataid
								cid, //c1
						}));

					}
				}
				//流程结束：设置结束时间
				if(PROCESS.next_cStatus().equals(ACE.CANDI_STATUS_OVER)){
					sqls.add(String.format("update B_Candidate_Process set c15=sysdate where c1='%s' and c5='%s'", new Object[]{cid, pcid}));
				}
				//邀请面试：设置流程开始时间
				if(PROCESS.code().equals(ACE.PROCESS_CODE_INVITE)){
					pcid = UGID.createUGID();
					sqls.add(String.format("update B_Candidate set c29=NVL(c29,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c21=NVL(c21,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
					sqls.add(String.format("update B_Candidate_Process set c5='%s',c13='',c14=sysdate,c18=0,c38=1 where dataid=%s and c1='%s' and c31='%s' and c4='%s'", new Object[]{pcid, pid, cid, myamid, auid}));

					//发送邀请面试的短信
					sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ProcessParam.hcPhone, format(ACE.TXT_SMS_INVITE), ACE.SMS_TYPE_HRPROCESS}));
					if(ACE.ONLINE) sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{ACE.ADMIN_PHONE, String.format(ACE.TXT_SMS_ADMIN_HR, new Object[]{ProcessParam.mmName, ProcessParam.hrName, "邀请面试『"+ProcessParam.cName+"』"}), ACE.SMS_TYPE_ADMIN}));
				}
				//关注、取消关注、黑名单：设置时间
				if(PROCESS.code().equals(ACE.PROCESS_CODE_COLLECT) || PROCESS.code().equals(ACE.PROCESS_CODE_UNCOLLECT)
						|| PROCESS.code().equals(ACE.PROCESS_CODE_ADDBLACKLIST)){
					sqls.add(String.format("update B_Candidate_Process set c22=sysdate where c1=%s and c31=%s and c4=%s", new Object[]{cid, myamid, auid}));
	
					if(PROCESS.code().equals(ACE.PROCESS_CODE_COLLECT)){
	    				sqls.add(String.format("update B_Candidate set c27=NVL(c27,0)+(%s) where c1='%s'", new Object[]{1, cid}));
	    				sqls.add(String.format("update B_HR set c20=NVL(c20,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
					}
					if(PROCESS.code().equals(ACE.PROCESS_CODE_UNCOLLECT)){
	    				sqls.add(String.format("update B_Candidate set c27=NVL(c27,0)+(%s) where c1='%s'", new Object[]{-1, cid}));
	    				sqls.add(String.format("update B_HR set c20=NVL(c20,0)+(%s) where c1='%s'", new Object[]{-1, ProcessParam.hrAuid}));
					}
				}
				//接受邀请
				if(PROCESS.code().equals(ACE.PROCESS_CODE_INVITE_SUCCESS)){
					sqls.add(String.format("update B_Candidate set c30=NVL(c30,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c22=NVL(c22,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
				}
				//Offer
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER)){
					sqls.add(String.format("update B_Candidate set c31=NVL(c31,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c23=NVL(c23,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
				}
				//接受Offer
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_ACCEPTED)){
					sqls.add(String.format("update B_Candidate set c32=NVL(c32,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c24=NVL(c24,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
				}
				//确认入职
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_BILLING)){
					sqls.add(String.format("update B_Candidate set c33=NVL(c33,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c25=NVL(c25,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
				}
				//保障期内离职
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_DIMISSION_CONFIRM)){
					//确认离职后，屏蔽公司
					////////////////////////////sqls.add(String.format("update B_Candidate set c64=decode(c64, null, '%s', c64||',%s') where c1='%s'", new Object[]{ProcessParam.mmAllName, ProcessParam.mmAllName, cid}));
					
					sqls.add(String.format("update B_Candidate set c34=NVL(c34,0)+(%s) where c1='%s'", new Object[]{1, cid}));
    				sqls.add(String.format("update B_HR set c26=NVL(c26,0)+(%s) where c1='%s'", new Object[]{1, ProcessParam.hrAuid}));
				}
				//待开票
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_BILLING)){
					sqls.add(String.format("insert into B_Bill(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13) select seqB_Bill.nextval, a.c1,a.c5,a.c31,a.c4,p.c67,a.c3, p.c8,a.c34,'0.00',%s, a.c36,a.c35,'%s' from B_Candidate_Process a, B_Candidate p where a.c1=p.c1 and a.c1='%s' and a.c5='%s'", new Object[]{
							ACE.CANDI_BILL_STATUS_BILLING,
							ComBeanLoginUser.getUserName(ProcessParam.hcAuid)+ComBeanLoginUser.getUserPhone(ProcessParam.hcAuid),
							cid,
							pcid
					}));
				}
				
				//拒绝面试
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_INVITE_FAILED)){
					sqls.add(String.format("update B_Candidate set c32=NVL(c32,0)+(%s) where c1='%s'", new Object[]{1, cid}));
				}
				//上传Offer
				if(PROCESS.next_processInnerStatus().equals(ACE.CANDI_PROCESS_INNERSTATUS_OFFER_UPLOADED)){
				    sqls.add(String.format(YSSQL.LWCandidate_addOfferFile, new Object[]{ProcessParam.hrAuid, ProcessParam.hrName, cid, pcid}));
				}
				//入职、离职
				if(PROCESS.next_activeStatus().length()>0){
					//更新候选人状态与原因
					sqls.add(String.format("update B_Candidate set c5=%s,c6=sysdate,c77='%s',c78='%s',c79=sysdate,c80='%s',uptdate=sysdate where c1='%s'", new Object[]{
							HIUtil.isEmpty(PROCESS.next_activeStatus(), stStatus), //active状态 c5
							auid, //操作人 c77
							ComBeanLoginUser.getUserName(auid), //操作人姓名 c78 (操作时间 c79)
							memo, //操作说明 c80
							cid,
					}));
				}
				//开票与付款
				if(PROCESS.next_billStatus().length()>0){
					sqls.add(String.format("update B_Bill set c10=%s, uptdate=sysdate where c1='%s' and c2='%s'", new Object[]{PROCESS.next_billStatus(), cid, pcid}));
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_BILLING)){ //7天超时
						sqls.add(String.format("update B_Bill set c14=sysdate+7 where c1='%s' and c2='%s'", new Object[]{cid, pcid}));
                    }
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_BILLED)){ //7天超时
						sqls.add(String.format("update B_Bill set c19=sysdate+7 where c1='%s' and c2='%s'", new Object[]{cid, pcid}));
	    				sqls.add(String.format("update B_HR set c27=NVL(c27,0)+(select c9 from B_Bill where c1='%s' and c2='%s') where c1='%s'", new Object[]{cid, pcid, ProcessParam.hrAuid}));
                    }
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_PayingToM)){ //7天超时
						sqls.add(String.format("update B_Bill set c19=sysdate+7 where c1='%s' and c2='%s'", new Object[]{cid, pcid}));
                    }
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_PayedToM)){ //180天超时
						sqls.add(String.format("update B_Bill set c21='%s', c22='%s', c23=sysdate, c24=sysdate+180 where c1='%s' and c2='%s'", new Object[]{auid, ProcessParam.myName, cid, pcid}));
	    				sqls.add(String.format("update B_HR set c28=NVL(c28,0)+(select c9 from B_Bill where c1='%s' and c2='%s') where c1='%s'", new Object[]{cid, pcid, ProcessParam.hrAuid}));
                    }
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_PayingToHC)){ //3天超时
						sqls.add(String.format("update B_Bill set c25=sysdate, c26=sysdate+3 where c1='%s' and c2='%s'", new Object[]{cid, pcid}));
                    }
                    if(PROCESS.next_billStatus().equals(ACE.CANDI_BILL_STATUS_PayingToHC)){
						sqls.add(String.format("update B_Bill set c28='%s', c29='%s', c30=sysdate where c1='%s' and c2='%s'", new Object[]{auid, ProcessParam.myName, cid, pcid}));
                    }
				}
	
				//流程历史
				if(PROCESS.addHisFlag()){
				    sqls.add(String.format("insert into B_Candidate_Process_His select * from B_Candidate_Process where c1='%s' and dataid=%s", new Object[]{cid, pid}));
				}
		    }
			 //简历上线、顾问面评上线
			if(PROCESS.next_cvStatus().equals(ACE.CANDI_CVStatus_ONLINE) || PROCESS.code().equals(ACE.PROCESS_CODE_HCCOMMENT_SUCCESS)){
				System.out.println("简历上线、顾问面评上线=============================== 同步候选人-O");
				String[] ss = {"", "_CVEducation", "_CVTrain", "_CVCertificate", "_CVWork", "_CVProject"};
				for(String s: ss){
				    sqls.add(this.sqlToZ("BO_Candidate"+s, memo, "c1="+cid));
				    sqls.add(String.format("delete from BO_Candidate%s where c1='%s'", new Object[]{s, cid}));
				    sqls.add(String.format("insert into BO_Candidate%s select * from B_Candidate%s where c1='%s'", new Object[]{s, s, cid}));
				}
			}else{
				//同步候选人的状态信息 -> 在线O
				sqls.add(String.format("update BO_Candidate t set uptdate=sysdate,(c2,c3,c4,c5,c6,c7,c27,c28,c29,c30,c31,c32,c61,c65,c66,c67,c68,c69,c70,c77,c78,c79,c80,c81,c82,c83,c84,c85,c86,c87,c88,c89,c90,c91,c92,c93,c94,c95,c96,c97,c98,c99,c100)=(select c2,c3,c4,c5,c6,c7,c27,c28,c29,c30,c31,c32,c61,c65,c66,c67,c68,c69,c70,c77,c78,c79,c80,c81,c82,c83,c84,c85,c86,c87,c88,c89,c90,c91,c92,c93,c94,c95,c96,c97,c98,c99,c100 from B_Candidate m where m.c1=t.c1) where c1='%s'", new Object[]{cid}));
			}
			
			//保存面试记录
			if(PROCESS.addIVFlag() || PROCESS.updateIVFlag()){
				String iv_user = PROCESS.type().equals(ACE.PROCESS_TYPE_ST) ? auid : ProcessParam.hrAuid;
				List<Object> ivList = dbQuery(String.format("select * from B_Candidate_Interview where c1='%s' and c4='%s' and c14=0", new Object[]{cid, iv_user}));
				
				Object[] ivoo = new Object[13];////////////////////////////////////////////
				int i = 0;
				ivoo[i++] = PROCESS.type().equals(ACE.PROCESS_TYPE_ST) ? 3 : 2; //c3
				if(PROCESS.type().equals(ACE.PROCESS_TYPE_ST)){
					ivoo[i++] = vv[0]; //候选人
					ivoo[i++] = vv[1]; //面试时间
					ivoo[i++] = vv[1]; //备选时间
					ivoo[i++] = "";    //职位
					ivoo[i++] = vv[2]; //面试地点
				}else{
					for(String v: vv){
						ivoo[i++] = v;
					}
				}
				ivoo[i++] = 0; //面试状态 c14
				ivoo[i++] = pcid; //c18
				ivoo[i++] = isHC() ? 1 : 0; //面试确认状态 c20
				ivoo[i++] = auid; //c23
				ivoo[i++] = ComBeanLoginUser.getUserName(auid);//c24
				ivoo[i++] = iv_user; //c4
				ivoo[i++] = cid; //c1
			
				if(ivList==null || ivList.size()==0){
					System.out.println(String.format("insert into B_Candidate_Interview(dataid,parentDataid,c1,c2,c3,c5,c7,c8,c6,c9,c14,c18,c20,c23,c24,c25,c4) select seqB_Candidate_Interview.nextval,dataid,c1,130001,%s,'%s',to_date('%s','yyyy-mm-dd hh24:mi') ivtime,to_date('%s','yyyy-mm-dd hh24:mi') ivtime2,'%s','%s','%s','%s','%s','%s','%s',sysdate,'%s' from B_Candidate p where c1='%s' ", ivoo));
					sqls.add(String.format("insert into B_Candidate_Interview(dataid,parentDataid,c1,c2,c3,c5,c7,c8,c6,c9,c14,c18,c20,c23,c24,c25,c4) select seqB_Candidate_Interview.nextval,dataid,c1,130001,%s,'%s',to_date('%s','yyyy-mm-dd hh24:mi') ivtime,to_date('%s','yyyy-mm-dd hh24:mi') ivtime2,'%s','%s','%s','%s','%s','%s','%s',sysdate,'%s' from B_Candidate p where c1='%s' ", ivoo));
				}else{
					System.out.println(String.format("update B_Candidate_Interview set c3=%s, c5='%s', c7=to_date('%s','yyyy-mm-dd hh24:mi'), c8=to_date('%s','yyyy-mm-dd hh24:mi'), c6='%s', c9='%s', c14=%s, c18='%s', c20=%s, c23='%s', c24='%s', c25=sysdate where c4=%s and c1='%s' and c14=0", ivoo));
					sqls.add(String.format("update B_Candidate_Interview set c3=%s, c5='%s', c7=to_date('%s','yyyy-mm-dd hh24:mi'), c8=to_date('%s','yyyy-mm-dd hh24:mi'), c6='%s', c9='%s', c14=%s, c18='%s', c20=%s, c23='%s', c24='%s', c25=sysdate where c4='%s' and c1='%s' and c14=0", ivoo));
				}
			}else if(PROCESS.ivOverFlag()){
				String iv_user = PROCESS.type().equals(ACE.PROCESS_TYPE_ST) ? (isM()?this.getParameter("s_auid"):ProcessParam.hcAuid) : ProcessParam.hrAuid;
				sqls.add(String.format("update B_Candidate_Interview set c14=9, uptdate=sysdate where c4='%s' and c1='%s' and c14=0", new Object[]{iv_user, cid}));
			}
			
			boolean tid666 = false;
			if(PROCESS.type().equals(ACE.PROCESS_TYPE_ST) || PROCESS.type().equals(ACE.PROCESS_TYPE_STATUS) || PROCESS.type().equals(ACE.PROCESS_TYPE_CV)){ //入库流程 状态流程 简历变更流程
				tid666 = true;
				sqls.add(this.sqlToZ("B_Action", "新事件["+memo+"]关闭原流程事件", String.format("c1='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{cid, PROCESS.type()})));
				sqls.add(String.format("update B_Action set c3=130666, c11=444, uptdate=sysdate where c1='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{cid, PROCESS.type()}));

			}else if(tid.length()>0 && (PROCESS.type().equals(ACE.PROCESS_TYPE_HR_STANDARD) || PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL))){ //HR标准流程  | HR开票流程
				tid666 = true;
				sqls.add(this.sqlToZ("B_Action", "新流程事件["+memo+"]关闭原流程事件", String.format("dataid=%s and c7='%s' and (c3=130001 or c11=0)", new Object[]{tid, ACE.PROCESS_TYPE_HR_STANDARD})));
				sqls.add(String.format("update B_Action set c3=130666, c11=444, uptdate=sysdate where dataid=%s and c7='%s' and (c3=130001 or c11=0)", new Object[]{tid, ACE.PROCESS_TYPE_HR_STANDARD}));
				//同时需要关闭【HR开票流程】
				sqls.add(this.sqlToZ("B_Action", "新流程事件["+memo+"]关闭原流程事件", String.format("dataid=%s and c7='%s' and (c3=130001 or c11=0)", new Object[]{tid, ACE.PROCESS_TYPE_HR_BILL})));
				sqls.add(String.format("update B_Action set c3=130666, c11=444, uptdate=sysdate where dataid=%s and c7='%s' and (c3=130001 or c11=0)", new Object[]{tid, ACE.PROCESS_TYPE_HR_BILL}));

			}else if(pcid.length()>0 && (PROCESS.type().equals(ACE.PROCESS_TYPE_HR_STANDARD) || PROCESS.type().equals(ACE.PROCESS_TYPE_HR_BILL))){ //HR标准流程  | HR开票流程
				tid666 = true;
				sqls.add(this.sqlToZ("B_Action", "新流程事件["+memo+"]关闭原流程事件", String.format("c4='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{pcid, ACE.PROCESS_TYPE_HR_STANDARD})));
				sqls.add(String.format("update B_Action set c3=130666, c11=444, uptdate=sysdate where c4='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{pcid, ACE.PROCESS_TYPE_HR_STANDARD}));
				//同时需要关闭【HR开票流程】
				sqls.add(this.sqlToZ("B_Action", "新流程事件["+memo+"]关闭原流程事件", String.format("c4='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{pcid, ACE.PROCESS_TYPE_HR_BILL})));
				sqls.add(String.format("update B_Action set c3=130666, c11=444, uptdate=sysdate where c4='%s' and c7='%s' and (c3=130001 or c11=0)", new Object[]{pcid, ACE.PROCESS_TYPE_HR_BILL}));

			}else if(pcid.length()>0 && PROCESS.type().equals(ACE.PROCESS_TYPE_HR_COLLECT)){ //HR关注流程
			}
			
			ComBeanAction ACTION = PROCESS.action();
			if(ACTION!=null){
				sqls.add(F_getActionSQL(cid, pid, pcid, tid, jobid, ACTION, PROCESS.type(), false, processContent));
			}
			
			if(dbExe(sqls)){
				if(PROCESS.code().equals(ACE.PROCESS_CODE_INVITE)){
					result = new LWResult(LWResult.SUCCESS, ProcessParam.hcAuid);
				}else{
					result = new LWResult(LWResult.SUCCESS, (tid666?"666":"") + memo+"成功");
				}
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);
			System.out.println("[CandiProcessServiceBean.F_doProcess]=======================================END");

			//候选人上线
			if(PROCESS.next_stcheckStatus().equals(ACE.CANDI_STCheckStatus_HCCOMMENT_ONLINE) //顾问面评通过
					&& stStatus.equals(ACE.CANDI_STStatus_PREST) //入库中
					){
				P_doProcess(ACE.PROCESS_CODE_ST_SUCCESS, cid);
			}
			//active状态
			if(PROCESS.next_activeStatus().equals(ACE.CANDI_ActiveStatus_ACTIVE)){
				LWResult result_1 = checkActiveNumLimit(ProcessParam.hcAuid);
				if(result_1!=null){
					this.Process_inactive(cid, result_1.getInfo(), null);
					result = new LWResult(LWResult.FAILURE, "该候选人状态变为Inactive！原因："+result_1.getInfo());
				}
			}
			//HC确定候选人入职
			if(PROCESS.code().equals(ACE.PROCESS_CODE_ENTRY_CONFIRM)){
				Process_entryBlock(cid, pcid);
			}
			//面试时间已过
			if((PROCESS.addIVFlag() || PROCESS.updateIVFlag()) && Long.parseLong(noticeTime.replaceAll("[^(0-9)]", "")) < Long.parseLong(HIUtil.getCurrentDate("yyyyMMddHHmm"))){
				P_doProcess(ACTION.overTimeProcess(), cid, pcid, "", "");
			}

		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}

	protected static String F_getActionSQL(String action, String hcAuid){
		ComBeanAction ACTION = new ComBeanAction(action);
		boolean no_notify = ACTION.notifyType().equals(ACE.NOTIFY_TYPE_NO_NOTIFY);
		return String.format("insert into B_Action(dataid,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32,c33,c34,c35,c36,c37,c38,c39,c40,c41,c42,c43,c44, c45,c46,c47,c48, c49,c50,c51,c52,c53,c54,c55,c56,c57,c58,c59, c60,c61,c62,c63,c64) values(seqB_Action.nextval,%s,%s,'%s','%s','%s','%s','%s',to_date('%s','yyyy-mm-dd hh24:mi:ss')+%s/(24),'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',to_date('%s','yyyy-mm-dd hh24:mi'),'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', '%s','%s','%s','%s', to_date('%s','yyyy-mm-dd hh24:mi'),'','%s','','%s','%s','%s','%s','%s','%s','%s', %s,'%s','%s','%s','%s')", new Object[]{
				hcAuid, //顾问
				no_notify ? 130666:130001, //状态 c3
				"", //pcid, //流程PCID c4
				ACTION.mode(), //事件分类 c5
				ACTION.type(), //事件类型 c6
				0, //processType, //流程分类 c7
				"253009", //事件状态 c8：253009已执行
				HIUtil.getCurrentDate("yyyy-MM-dd HH:mm"), //c9 限时执行截止时间
				ACTION.limitHour(), //c9 限时执行截止时间
				ACTION.overTimeProcess(), //c10 限时执行流程CODE
				ACTION.overTimeProcess().length()>0 ? 0:444, //c11 限时执行状态
				ACTION.title(), //事件标题（简明） c12
				ACTION.memoHC(), //processContent, //事件详情 c13 //detail
				ACTION.memoM(), //format(ACTION.memoM()), //c14  //.replaceAll("[DETAIL=(.+?)]", "")
				ACTION.memoBS(), //format(ACTION.memoBS()), //c15
				ACTION.memoHC(), //format(ACTION.memoHC()), //c16
				ACTION.memoHR(), //format(ACTION.memoHR()), //c17
				ACTION.memoC(), //format(ACTION.memoC()), //c18
				ACTION.visibleM(), //c19
				ACTION.visibleBS(), //c20
				ACTION.visibleHC(), //c21
				ACTION.visibleHR(), //c22
				ACTION.visibleC(), //c23
				HIUtil.getCurrentDate("yyyy-MM-dd HH:mm"), //通知时间 //c24
				ACTION.noticeFlagM()? "M" : "", //通知对象M //c25
				ACTION.noticeFlagBS()? "BS" : "", //通知对象BS //c26
				hcAuid, //ACTION.noticeFlagHC()? hcAuid : "", //通知对象HC //c27
				"", //ACTION.noticeFlagHR()? hrAuid : "", //通知对象HR //c28
				ACTION.noticeFlagC()? "C" : "", //通知对象C //c29
				ACTION.noticeM(), //format(ACTION.noticeM()), //通知标题M //c30
				ACTION.noticeBS(), //format(ACTION.noticeBS()), //通知标题BS //c31
				ACTION.noticeHC(), //format(ACTION.noticeHC()), //通知标题HC //c32
				ACTION.noticeHR(), //format(ACTION.noticeHR()), //通知标题HR //c33
				ACTION.noticeC(), //format(ACTION.noticeC()), //通知标题C //c34
				"", //任务状态M //c35
				"", //任务状态BS //c36
				"", //任务状态HC //c37
				"", //任务状态HR //c38
				"", //任务状态C //c39
				0,0,0,0,0, //已读标记 c40-c44
				
				"","","","", //mmAmid, mmMid, jobid, hrAuid,
				
				HIUtil.getCurrentDate("yyyy-MM-dd HH:mm"), //附：提醒日期 c49
				//附：候选人姓名  c50
				"", //getSqlValue("select c8 from B_Candidate_Files where c3=250001 and c1="+cid, "cc"), //附：候选人头像 c51
				//附：候选人当前职位 c52
				ACTION.notifyStatus(), //附：事件提醒状态 c53
				ACTION.notifyStatusDesc(), //附：事件提醒状态说明 c54
				"", //(processType.equals(ACE.PROCESS_TYPE_HRSTANDARD) || processType.equals(ACE.PROCESS_TYPE_HR_NOT_STANDARD)) ? format("[HR公司] [|JOB]  [HRFACE][HR]") : "", //附：公司职位HR c55
				ACTION.notifyDetail(), //format(ACTION.notifyDetail()), //附：事件提醒详情 c56
				ACTION.notifyType(), //附：事件通知类型 c57
				ACTION.notifyDesc(), //附：事件提醒说明c58
				ACTION.delayFlag(), //附：是否可推迟 c59
				"-1", //HIUtil.isEmpty(tid, "-1"),
				ACTION.queryStatus(), //事件搜索状态
				ACTION.url(), //关联链接 c62
				"", //关联对象 c63
				ACTION.code(), //事件code c64
			});
	}
	protected String F_getActionSQL(String cid, String pid, String pcid, String tid, String jobid, ComBeanAction ACTION, String processType, boolean initFlag, String processContent){
		if(ProcessParam==null){
			ProcessParam = new ProcessParam("", "", "");
		}
		if(initFlag) initParams(cid, pid, jobid, false, "");
		boolean no_notify = ACTION.notifyType().equals(ACE.NOTIFY_TYPE_NO_NOTIFY);
		boolean hc_ignore = ACTION.ignoreHCUserFlag() && isHC();
		return String.format("insert into B_Action(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30,c31,c32,c33,c34,c35,c36,c37,c38,c39,c40,c41,c42,c43,c44, c45,c46,c47,c48, c49,c50,c51,c52,c53,c54,c55,c56,c57,c58,c59, c60,c61,c62,c63,c64) select seqB_Action.nextval,c1,c2,%s,'%s','%s','%s','%s','%s',to_date('%s','yyyy-mm-dd hh24:mi:ss')+%s/(24),'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',to_date('%s','yyyy-mm-dd hh24:mi'),'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', '%s','%s','%s','%s', to_date('%s','yyyy-mm-dd hh24:mi'),c8,'%s','（'||c41||' | '||c42||'）','%s','%s','%s','%s','%s','%s','%s', %s,'%s','%s','%s','%s' from B_Candidate where c1='%s'", new Object[]{
				no_notify ? 130666:130001,
				pcid, //流程PCID c4
				ACTION.mode(), //事件分类 c5
				ACTION.type(), //事件类型 c6
				processType, //流程分类 c7
				"253009", //事件状态 c8：253009已执行
				HIUtil.isEmpty(ProcessParam.getP_noticeTime(), HIUtil.getCurrentDate("yyyy-MM-dd HH:mm")), //c9 限时执行截止时间
				ACTION.limitHour(), //c9 限时执行截止时间
				ACTION.overTimeProcess(), //c10 限时执行流程CODE
				ACTION.overTimeProcess().length()>0 ? 0:444, //c11 限时执行状态
				ACTION.title(), //事件标题（简明） c12
				processContent, //事件详情 c13 //detail
				format(ACTION.memoM()), //c14  //.replaceAll("[DETAIL=(.+?)]", "")
				format(ACTION.memoBS()), //c15
				format(ACTION.memoHC()), //c16
				format(ACTION.memoHR()), //c17
				format(ACTION.memoC()), //c18
				ACTION.visibleM(), //c19
				ACTION.visibleBS(), //c20
				hc_ignore ? 0 : ACTION.visibleHC(), //c21
				ACTION.visibleHR(), //c22
				ACTION.visibleC(), //c23
				HIUtil.isEmpty(ProcessParam.getP_noticeTime(), HIUtil.getCurrentDate("yyyy-MM-dd HH:mm")), //通知时间 //c24
				ACTION.noticeFlagM()? "M" : "", //通知对象M //c25
				ACTION.noticeFlagBS()? "BS" : "", //通知对象BS //c26
				ACTION.noticeFlagHC()? ProcessParam.hcAuid : "", //通知对象HC //c27
				ACTION.noticeFlagHR()? ProcessParam.hrAuid : "", //通知对象HR //c28
				ACTION.noticeFlagC()? "C" : "", //通知对象C //c29
				format(ACTION.noticeM()), //通知标题M //c30
				format(ACTION.noticeBS()), //通知标题BS //c31
				format(ACTION.noticeHC()), //通知标题HC //c32
				format(ACTION.noticeHR()), //通知标题HR //c33
				format(ACTION.noticeC()), //通知标题C //c34
				"", //任务状态M //c35
				"", //任务状态BS //c36
				"", //任务状态HC //c37
				"", //任务状态HR //c38
				"", //任务状态C //c39
				0,0,0,0,0, //已读标记 c40-c44
				
				ProcessParam.mmAmid, ProcessParam.mmMid, jobid, ProcessParam.hrAuid,
				
				ACTION.notify_planTimeFlag() && !HIUtil.isEmpty(ProcessParam.getP_noticeTime()) ? ProcessParam.getP_noticeTime() : HIUtil.getCurrentDate("yyyy-MM-dd HH:mm"), //附：提醒日期 c49
				//附：候选人姓名  c50
				getSqlValue("select c8 from B_Candidate_Files where c3=250001 and c1="+cid, "c8"), //附：候选人头像 c51
				//附：候选人当前职位 c52
				ACTION.notifyStatus(), //附：事件提醒状态 c53
				ACTION.notifyStatusDesc(), //附：事件提醒状态说明 c54
				(processType.equals(ACE.PROCESS_TYPE_HR_STANDARD) || processType.equals(ACE.PROCESS_TYPE_HR_BILL) || processType.equals(ACE.PROCESS_TYPE_HR_COLLECT)) ? format("<div class=\"layui-inline ellipsis HC_jobInfo_\">[HR公司] [|JOB]</div>  <div class=\"layui-inline ellipsis HC_hrInfo_\">[HRFACE][HR]</div>") : "", //附：公司职位HR c55
				format(ACTION.notifyDetail()), //附：事件提醒详情 c56
				ACTION.notifyType(), //附：事件通知类型 c57
				ACTION.notifyDesc(), //附：事件提醒说明c58
				ACTION.delayFlag(), //附：是否可推迟 c59
				HIUtil.isEmpty(tid, "-1"),
				ACTION.queryStatus(), //事件搜索状态
				ACTION.url(), //关联链接 c62
				"", //关联对象 c63
				ACTION.code(), //事件code c64
				cid, // 候选人 c1
		});
	}

	protected LWResult checkActiveNumLimit(String hcAuid){
		LWResult result = null;
		int counter = this.getSqlIntValue(String.format("select count(dataid) counter from B_Candidate where c2='%s' and c5=%s", new Object[]{hcAuid, ACE.CANDI_ActiveStatus_ACTIVE}), "counter");
		if(counter >=  ACE.HC_MAX_ACTIVE_NUM){
			result = new LWResult(LWResult.FAILURE, ACE.TXT_LIMIT_ACTIVE_NUM);
		}
		return result;
	}

	boolean flag = false;
	
	private void initParams(String cid, String pid, String jobid, boolean newHRProcess, String job){
		List<Object> userlist = null;
		if(pid.length()==0 || newHRProcess){
			if(isHR()){
				userlist = dbQuery("select '"+myamid+"' mmAmid, '' mmMid, NVL(a.c28,a.c2) mmOrganName, '' mmName, '' jobName, '"+auid+"' hrAuid, '' hrName, 0 ivn, p.c2 hcAuid, p.c8 cName from B_MMOrgan a, B_Candidate p where a.c1='"+myamid+"' and p.c1='"+cid+"'");
			}else{
				userlist = dbQuery("select p.c2 hcAuid, p.c8 cName from B_Candidate p where p.c1='"+cid+"'");
			}
		}else{
			userlist = dbQuery("select a.c31 mmAmid, a.c32 mmMid, a.c36 mmName, a.c34 jobName, a.c4 hrAuid, a.c35 hrName_, d.c11 hrName, a.c18 ivn, p.c2 hcAuid, p.c8 cName from B_Candidate_Process a, b_Userlogin d, B_Candidate p where a.c4=d.c1 and a.c1=p.c1 and a.dataid="+pid);
		}
		if(userlist!=null && userlist.size()>0){
			Object o = userlist.get(0);
			ProcessParam.mmAmid = this.getDBString(o, "mmAmid");
			ProcessParam.mmAllName = ComBeanMMOrgan.getFullName(ProcessParam.mmAmid);
			ProcessParam.mmName = this.getDBString(o, "mmName", ComBeanAOrgan.getSimpNameByAmid(ProcessParam.mmAmid)); //公司简称
			if(ProcessParam.mmName.length()==0) ProcessParam.mmName = this.getDBString(o, "mmOrganName");
			ProcessParam.mmMid = this.getDBString(o, "mmMid", ComBeanAOrgan.getMidByAmid(ProcessParam.mmAmid));
			ProcessParam.hcAuid = this.getDBString(o, "hcAuid");
			ProcessParam.hcName = ComBeanLoginUser.getUserName(ProcessParam.hcAuid);
			ProcessParam.hcPhone = ComBeanLoginUser.getUserPhone(ProcessParam.hcAuid);
			ProcessParam.hrAuid = this.getDBString(o, "hrAuid");
			ProcessParam.hrName = this.getDBString(o, "hrName", ComBeanLoginUser.getUserName(ProcessParam.hrAuid));
			ProcessParam.jobName = this.getDBString(o, "jobName"); if(job.length()>0) ProcessParam.jobName = job;
			if(ProcessParam.jobName.length()==0 && jobid.length()>0){ProcessParam.jobName = this.getSqlValue("select c1 jobName from B_Job where dataid="+jobid+"", "jobName"); }
			ProcessParam.cName = this.getDBString(o, "cName");
			ProcessParam.ivn = this.getDBString(o, "ivn", "0");
		}
		ProcessParam.myName = ComBeanLoginUser.getUserName(auid);
		flag = true;
	}
	private String format(String s){
		String rtn = s;
		System.out.println("[format] s="+s);
		rtn = rtn.replaceAll("\\[HR公司\\]", ProcessParam.mmName);
		rtn = rtn.replaceAll("\\[HR\\]", ProcessParam.hrName);
		rtn = rtn.replaceAll("\\[JOB\\]", ProcessParam.jobName);
		rtn = rtn.replaceAll("\\[\\|JOB\\]", HIUtil.isEmpty(ProcessParam.jobName) ? "" : (" | " + ProcessParam.jobName));
		rtn = rtn.replaceAll("\\[C\\]", ProcessParam.cName);
		rtn = rtn.replaceAll("\\[HC\\]", ProcessParam.hcName);
		rtn = rtn.replaceAll("\\[I\\]", ProcessParam.myName);
		rtn = rtn.replaceAll("\\[IVN\\]", ProcessParam.ivn);
		rtn = rtn.replaceAll("\\[NOTICETIME\\]", ProcessParam.getP_noticeTime());
		rtn = rtn.replaceAll("\\[DETAIL=(.+?)\\]", ProcessParam.getP_detail());
		System.out.println("[format] rtn="+rtn);
		return rtn;
	}
	
	private ProcessParam ProcessParam = null;
	class ProcessParam{
		String mmName="", mmAllName="", jobName="", hrName="", mine="", cName="", ivn="";
		String myName="", mmAmid="", mmMid="", hrAuid="", hcAuid="", hcName="", hcPhone="";
		
		private String p_noticeTime = "";
		private String p_detail = "";
		private String p_job = "";
		
		public ProcessParam(String job, String noticeTime, String detail){
			this.p_detail = detail;
			this.p_noticeTime = noticeTime;
			this.p_job = job;
		}
		
		public String getP_noticeTime() {
			return p_noticeTime;
		}
		public void setP_noticeTime(String p_noticeTime) {
			this.p_noticeTime = p_noticeTime;
		}
		public String getP_detail() {
			return p_detail;
		}
	}
}
