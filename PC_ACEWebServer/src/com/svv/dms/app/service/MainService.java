package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.entity.I_DataParamType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class MainService extends CandiBase {

	public String MainBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String MainSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String MainCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String MainMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	
	//关闭顶部通知栏
	public String closeTopNotice(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(dbExe(String.format("update B_UserLogin set c36='0'||substr(c36,2) where c1='%s'", new Object[]{auid}))){
    		result = new LWResult(LWResult.SUCCESS, "顶部通知栏已关闭");
		}
		return APPRETURN(result);
	}
	//关闭收藏提示栏
	public String closeCollectNotice(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(dbExe(String.format("update B_UserLogin set c36=substr(c36,1,1)||'0' where c1='%s'", new Object[]{auid}))){
    		result = new LWResult(LWResult.SUCCESS, "收藏提示栏已关闭");
		}
		return APPRETURN(result);
	}
	
	//新消息
	public String newNotice(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
        String c = "", user_c = "";
        if(isM()){ c = "c20"; user_c = "(c26="+auid+" or c26='BS')"; }
        if(isHC()){ c = "c21"; user_c = "c2="+auid; }
        if(isHR()){ c = "c22"; user_c = "c48="+auid; }
		List<Object> list = dbQuery(String.format("select count(*) c from B_Action where c5=%s and %s and c3=130001 and %s=1 and c42=0 and (c49<=trunc(sysdate+1) or c32 is null or instr(c32,'[MODE=面试日历]')<=0)", new Object[]{ACE.ACTION_TYPE_TASK, user_c, c}));
	    StringBuilder s = new StringBuilder();
		if(list!=null && list.size()>0){
	    	for(Object o: list){
	    		s.append(this.getDBString(o, "c"));
	    		break;
	    	}
    		result = new LWResult(LWResult.SUCCESS, s.toString());
	    }else{
    		result = new LWResult(LWResult.FAILURE, "无记录");
	    }
		return APPRETURN(result);
	}
	
	//NOTICE列表
	public String listNotice(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_NoticeList_HR();
		if(isHC()) result = ServiceDO_NoticeList_HC();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览我的通知列表");
		return APPRETURN(result);
	}

	//TASK日列表
	public String listTaskDay(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_TaskDayList();
		if(isM()) result = ServiceDO_TaskDayList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览我的待办事项日列表");
		return APPRETURN(result);
	}
	//TASK列表
	public String listTask(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_TaskList();
		if(isM()) result = ServiceDO_TaskList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览我的待办事项列表");
		return APPRETURN(result);
	}
	// 关闭TASK
	public String closeTask(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_TaskClose();
		if(isM()) result = ServiceDO_TaskClose();
        loggerM(ComBeanLogType.TYPE_EDIT, "关闭任务");
		return APPRETURN(result);
	}
	// 推迟TASK
	public String delayTask(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_delayTask_HC();
        loggerM(ComBeanLogType.TYPE_EDIT, "推迟任务");
		return APPRETURN(result);
	}

	//待办事件列表
	public String listAction(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_ActionList_HC();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览我的待办事件列表");
		return APPRETURN(result);
	}
	//我的统计数据
	public String listStatData(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHC()) result = ServiceDO_StatData();
		if(isM()) result = ServiceDO_StatData();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览我的统计数据");
		return APPRETURN(result);
	}
	// 私人定制
	public String interest(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_Interest_HR();
        loggerM(ComBeanLogType.TYPE_QUERY, "下周职位投票");
		return APPRETURN(result);
	}
	// 查询私人定制
	public String selectedInterest(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_selectedInterest_HR();
        loggerM(ComBeanLogType.TYPE_QUERY, "查询职位投票");
		return APPRETURN(result);
	}
	public String uploadFile(){
		System.out.println("[MainService.uploadFile]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("ACEFILE");
	}

	public String uploadPic(){
		System.out.println("[MainService.uploadPic]=======================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("ACEPIC");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8580290784939767566L;


	private LWResult ServiceDO_NoticeList_HC(){
		System.out.println("[MainServiceBean.ServiceDO_NoticeList_HC]=======================================");
		String memo = "我的通知";
		try{
			StringBuilder s = new StringBuilder("");
//			String objectMode = this.getParameter("objectMode");
//			int maxcount = this.getParameter("i9", 5);
//			String HTML = ComBeanAPPModule.getHtml(objectMode);
//			List<Object> list = dbQuery(String.format("select * from B_Action where c2=%s and c5=%s", new Object[]{auid, ACE.ACTION_TYPE_NOTICE}));
//			int n = 0;
//			if(list!=null && list.size()>0){
//				for(Object o : list){
//					if(n == maxcount) break;
//					s.append(String.format(HTML, new Object[]{
//                         //////////////////////////////////////
//							
//							
//					}));
//					n++;
//				}
//			}
//			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
//			return new LWResult(LWResult.FAILURE, "");
			
			s.append("请使用最新版本的Chrome浏览器以获得最佳的使用体验！");
			return new LWResult(LWResult.SUCCESS, s.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}	
	private LWResult ServiceDO_NoticeList_HR(){
		System.out.println("[MainServiceBean.ServiceDO_NoticeList_HR]=======================================");
		String memo = "我的通知";
		try{
			StringBuilder s = new StringBuilder("");
//			String objectMode = this.getParameter("objectMode");
//			int maxcount = this.getParameter("i9", 5);
//			String HTML = ComBeanAPPModule.getHtml(objectMode);
//			List<Object> list = dbQuery(String.format("select * from B_Action where c2=%s and c5=%s", new Object[]{auid, ACE.ACTION_TYPE_NOTICE}));
//			int n = 0;
//			if(list!=null && list.size()>0){
//				for(Object o : list){
//					if(n == maxcount) break;
//					s.append(String.format(HTML, new Object[]{
//                         //////////////////////////////////////
//							
//							
//					}));
//					n++;
//				}
//			}
//			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
//			return new LWResult(LWResult.FAILURE, "");
			
			s.append("请使用最新版本的Chrome浏览器以获得最佳的使用体验！");
			return new LWResult(LWResult.SUCCESS, s.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}	

	private LWResult ServiceDO_TaskDayList(){
		System.out.println("[MainServiceBean.ServiceDO_TaskDayList]=======================================");
		String memo = "我的待办事项列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			//c49<=trunc(sysdate)+1 and 
			List<Object> list = null;
			if(isHC()) list = dbQuery(String.format("select decode(sign(c49-trunc(sysdate+1)), -1, trunc(sysdate), trunc(c49)) day, count(dataid) sum1, 255001 data2, NVL(sum(decode(c61,255001,1,0)),0) sum2, 244001 data3, NVL(sum(decode(c61,244001,1,0)),0) sum3, 241021 data4, NVL(sum(decode(c61,241021,1,0)),0) sum4, 244005 data5, NVL(sum(decode(c61,244005,1,0)),0) sum5, 244020 data6, NVL(sum(decode(c61,244020,1,0)),0) sum6, 244052 data7, NVL(sum(decode(c61,244052,1,0)),0) sum7, 242001 data8, NVL(sum(decode(c61,242001,1,0)),0) sum8, 246054 data9, NVL(sum(decode(c61,246054,1,0)),0) sum9 from B_Action a where a.c3=130001 and a.c2='%s' and c5=%s and c21="+(isHC()?1:99999)+" and a.c42=0 and (a.c49<trunc(sysdate+1) or a.c32 is null or instr(a.c32,'[MODE=面试日历]')<=0) group by decode(sign(c49-trunc(sysdate+1)), -1, trunc(sysdate), trunc(c49)) order by day", new Object[]{isHC()?auid:"xxxxxx", ACE.ACTION_TYPE_TASK}));
			if(isM()) list = dbQuery(String.format("select decode(sign(c49-trunc(sysdate+1)), -1, trunc(sysdate), trunc(c49)) day, count(dataid) sum1, 255001 data2, NVL(sum(decode(c61,255001,1,0)),0) sum2, 244001 data3, NVL(sum(decode(c61,244001,1,0)),0) sum3, 241021 data4, NVL(sum(decode(c61,241021,1,0)),0) sum4, 244005 data5, NVL(sum(decode(c61,244005,1,0)),0) sum5, 244020 data6, NVL(sum(decode(c61,244020,1,0)),0) sum6, 244052 data7, NVL(sum(decode(c61,244052,1,0)),0) sum7, 242001 data8, NVL(sum(decode(c61,242001,1,0)),0) sum8, 246054 data9, NVL(sum(decode(c61,246054,1,0)),0) sum9 from B_Action a where a.c3=130001 and c2='%s' and c5=%s and c20="+(isM()?1:99999)+" and c42=0 and (c26="+auid+" or c26='BS') and (a.c49<trunc(sysdate+1) or a.c32 is null or instr(a.c32,'[MODE=面试日历]')<=0) group by decode(sign(c49-trunc(sysdate+1)), -1, trunc(sysdate), trunc(c49)) order by day", new Object[]{isM()?"c2":"xxxxxx", ACE.ACTION_TYPE_TASK}));

			int n = 0;
			if(list!=null && list.size()>0){
				String day = null;
				String curday = HIUtil.getCurrentDate("yyyy-MM-dd");
				int sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9;
				for(Object o : list){
					day = getDBDate(o, "day", "yyyy-MM-dd");
					sum1 = getDBInt(o, "sum1");
					sum2 = getDBInt(o, "sum2");
					sum3 = getDBInt(o, "sum3");
					sum4 = getDBInt(o, "sum4");
					sum5 = getDBInt(o, "sum5");
					sum6 = getDBInt(o, "sum6");
					sum7 = getDBInt(o, "sum7");
					sum8 = getDBInt(o, "sum8");
					sum9 = getDBInt(o, "sum9");
					if(n==0 && !day.equals(curday)){
						s.append("<div class=\"bg-white\"><img class=\"middle\" style=\"width:44px;height:22px\" src=\"../doc/images/HC/complete.png\"> 恭喜！今日待办事项已全部完成</div>")
						 .append("<div class=\"h20\"></div>");
					}
					s.append(String.format(HTML, new Object[]{
							//顾问面试 面试 Offer 入职 关注 离职
							day.equals(curday) ? "今日" : (day.substring(5, 7)+"月"+day.substring(8, 10)+"日"),
							sum1 > 0 ? "("+sum1+")" : "",
									
							sum2 > 0 ? "" :"gone",		
							this.getDBString(o, "data2"),
							"通知",						
							sum2 > 0 ? "("+sum2+")" : "",
							
							sum3 > 0 ? "" :"gone",
							this.getDBString(o, "data3"),//244001
							"邀请面试",						
							sum3 > 0 ? "("+sum3+")" : "",
							
							sum4 > 0 ? "" :"gone",		
							this.getDBString(o, "data4"),//241021
							"顾问面试",
							sum4 > 0 ? "("+sum4+")" : "",
									
							sum5 > 0 ? "" :"gone",
							this.getDBString(o, "data5"),//244005
							"面试",						
							sum5 > 0 ? "("+sum5+")" : "",
							
							sum6 > 0 ? "" :"gone",		
							this.getDBString(o, "data6"),//244020
							"Offer",						
							sum6 > 0 ? "("+sum6+")" : "",
							
							sum7 > 0 ? "" :"gone",		
							this.getDBString(o, "data7"),//244052
							"入职",						
							sum7 > 0 ? "("+sum7+")" : "",
							
							sum8 > 0 ? "" :"gone",		
							this.getDBString(o, "data8"),//242001
							"收藏",						
							sum8 > 0 ? "("+sum8+")" : "",
							
							sum9 > 0 ? "" :"gone",
							this.getDBString(o, "data9"),//246054
							"离职",						
							sum9 > 0 ? "("+sum9+")" : "",
									
							day,
					}));
					n++;
				}
			}
			if(n==0){
				s.append("<div class=\"bg-white\"><img class=\"middle\" style=\"width:44px;height:22px\" src=\"../doc/images/HC/complete.png\"> 恭喜！今日待办事项已全部完成</div>")
				 .append("<div class=\"h20\"></div>");
				n++;
			}

			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}
	private LWResult ServiceDO_TaskList(){
		System.out.println("[MainServiceBean.ServiceDO_TaskList]=======================================");
		String memo = "我的任务列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String oldtid = this.getParameter("oldtid");
			String sortMK = this.getParameter("sortMK"); //排序
			String day = this.getParameter("day");
			String taskType = this.getParameter("taskType");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
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
			if(isHC()){
				list = dbQuery(YSSQL.LWMain_Action_QueryByC("", "1", auid, ACE.ACTION_TYPE_TASK, day, taskType, oldtid, sortMK, FIRSTID, LASTID));
			}
			if(isM()){
				list = dbQuery(YSSQL.LWMain_Action_QueryByC(auid, "", "", ACE.ACTION_TYPE_TASK, day, taskType, oldtid, sortMK, FIRSTID, LASTID));
			}
			int n = 0;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					String stStatus = this.getDBString(o, "stStatus");
					String stCheckStatus = this.getDBString(o, "stCheckStatus");
					String activeStatus = this.getDBString(o, "activeStatus");
					String candiStatus = this.getDBString(o, "candiStatus");
					String collectStatus = this.getDBString(o, "collectStatus");
					String processInnerStatus = this.getDBString(o, "processInnerStatus");
					String processCloseStatus = this.getDBString(o, "processCloseStatus");
					String processDetail = this.getDBString(o, "processDetail", this.getDBString(o, "c13")); //详情
					String actionStatus = this.getDBString(o, "c53"); //提醒状态
					String processType = this.getDBString(o, "c7"); // 流程分类
					String notifyType = this.getDBString(o, "c57");
					String notifyDesc = this.getDBString(o, "c58"); //提醒备注说明
					String hrInfo = this.getDBString(o, "c55");
					String notifyDetail = this.getDBString(o, "c56"); //提醒详情
					String notifyStatusDesc = this.getDBString(o, "c54"); //提醒状态说明
					int delayFlag = this.getDBInt(o, "c59"); //是否允许推迟
					String actionCode = this.getDBString(o, "c64"); //事件code
					
					String ivTime = ""; //面试时间
					if((processType==ACE.PROCESS_TYPE_ST || processType==ACE.PROCESS_TYPE_HR_STANDARD) &&
							(stCheckStatus.equals(ACE.CANDI_STCheckStatus_IVING) //已安排顾问面试
							|| processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IVING) //已安排面试
							|| processInnerStatus.equals(ACE.CANDI_PROCESS_INNERSTATUS_IV_CONFIRM) //已确定面试
							)){
						ivTime = "<br>"+this.getDBString(o, "ivTime");
					}
					hrInfo = "<span class=\"positiontitle pointer\" mm=\""+this.getDBString(o, "c45")+"\" job=\""+this.getDBString(o, "c47")+"\" hr=\""+this.getDBString(o, "c48")+"\">"+hrInfo.replaceAll("\\[HRFACE\\]", "<img class=\"pdl10 pdr5\" style=\"width: 10px;height: 11px;margin-top: -2px\" src=\"../doc/images/HC/face_hr_s.png\">");
					long limitMinutes = DBUtil.getDBDouble(o, "limitMinutes").longValue();
					String limitTimer = null;
					if(limitMinutes != -1) {
						boolean limitTimeFlag = notifyDesc.indexOf("[LIMITTIME]")>=0;
						if(limitTimeFlag){
							if(limitMinutes > 24*60){
								limitTimer = (limitMinutes/(24*60)) + "天";
							}else{
							    limitTimer = (limitMinutes/60) + "时" + Math.abs(limitMinutes%60) + "分";
							    delayFlag = 0; //BUG-232:有效时间少于24小时的无法推迟
							    
							}
							notifyDesc = notifyDesc.replaceAll("\\[LIMITTIME\\]", limitTimer+" ");
						}
					}
					if(notifyDetail.indexOf("[LOCATION]")>=0){
						notifyDetail = notifyDetail.replaceAll("\\[LOCATION\\]", "<img class=\"pdr2\" style=\"width:16px;\" src=\"../doc/images/location.png\">");
					}
					if(notifyDetail.indexOf("查看原因[OPEN")>=0){
						notifyDetail = notifyDetail.replaceAll("查看原因\\[OPEN(.+?)\\]", "<span class=\"pointer dark-grey HC_showReason_\">查看原因 ></span>");
					}
					String[] btn11Class = {"gone","gone"}, btn11 = {"",""}, btn22Class = {"gone","gone","gone","gone"}, btn22 = {"","","",""};
					if(isHC()){
						boolean STING_FLAG = processType.equals(ACE.PROCESS_TYPE_ST);// 入库事件
						boolean powerForHC_addHRIV = powerForHC_addHRIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_editHRIV = powerForHC_editHRIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_confirmHRIV = powerForHC_confirmHRIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_setIVResult = powerForHC_setIVResult(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_nextIV = powerForHC_nextIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_applyActive = powerForHC_applyST(stStatus, stCheckStatus);
						boolean powerForHC_addHCIV = powerForHC_addHCIV(stCheckStatus);
						boolean powerForHC_editHCIV = powerForHC_editHCIV(stCheckStatus);
						boolean powerForHC_editHCComment = powerForHC_editHCComment(stCheckStatus);
						boolean powerForHC_commitHCComment = powerForHC_commitHCComment(stCheckStatus);
						boolean powerForHC_setDimissionReason = powerForHC_setDimissionReason(processInnerStatus);
						boolean powerForHC_setUnPassIVReason = powerForHC_setUnPassIVReason(candiStatus, processInnerStatus);
						boolean powerForHC_setUnEntryReason = powerForHC_setUnEntryReason(candiStatus, processInnerStatus);
						boolean powerForHC_candidateReplyIV = powerForHC_candidateReplyIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_CandidateReplyEntry = powerForHC_CandidateReplyEntry(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_CandidateEntryConfirm = powerForHC_CandidateEntryConfirm(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_candidateReplyOffer = powerForHC_candidateReplyOffer(activeStatus, candiStatus, processInnerStatus);

						int m = 0;
						if(ACE.NOTIFY_TYPE_NOTICE.equals(notifyType)) { btn11Class[m] = ""; btn11[m] = ""; }
						if(ACE.NOTIFY_TYPE_NO_NOTIFY.equals(notifyType)) { btn11Class[m] = ""; btn11[m] = " 不提醒"; }
						m++;
						if(ACE.NOTIFY_TYPE_NOTICE .equals(notifyType)){
							
						}else if(ACE.NOTIFY_TYPE_NOTICE_CONFIRM .equals(notifyType)){
							btn11Class[m] = "close_"; btn11[m] = "知道了";
							
						}else{
							if(actionCode.equals(ACE.ACTION_CODE_002)) { btn11Class[m] = "HC_addCandiPage_"; btn11[m] = "新增候选人"; }
							if(powerForHC_applyActive) { btn11Class[m] = "HC_applyST_"; btn11[m] = "申请入库"; }
							if(powerForHC_addHCIV){ btn11Class[m] = "HC_addHCIV_"; btn11[m] = "顾问面试"; }
							if(powerForHC_editHCComment && STING_FLAG){ btn11Class[m] = "HC_editHCComment_"; btn11[m] = "编辑顾问面评"; }
							//if(powerForHC_commitHCComment && STING_FLAG){ btn11Class[m] = "HC_commitHCComment_"; btn11[m] = "提交顾问面评"; btn22Class[m] = "HC_editHCComment_"; btn22[m] = "编辑顾问面评";} 3-31 注释了,需要跟产品核对
							if(powerForHC_candidateReplyOffer){ btn11Class[m] = "HC_setCandidateReplyOffer_"; btn11[m] = "候选人反馈"; }
							if(powerForHC_addHRIV) { btn11Class[m] = "HC_addHRIV_"; btn11[m] = "安排面试"; }
							if(powerForHC_confirmHRIV) { btn11Class[m] = "HC_confirmHRIV_"; btn11[m] = "确定面试信息"; }
							if(powerForHC_setIVResult) { btn11Class[m] = "HC_setHRIVResult_"; btn11[m] = "面试反馈"; }
							if(powerForHC_nextIV) { btn11Class[m] = "HC_setHRIVNext_"; btn11[m] = "面试反馈"; }
							if(powerForHC_candidateReplyIV){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "候选人反馈"; }
							if(powerForHR_addOffer) { btn11Class[m] = "HC_uploadOffer_"; btn11[m] = "上传Offer"; }
							if(powerForHC_CandidateReplyEntry) { btn11Class[m] = "HC_setCandidateReplyEntry_"; btn11[m] = "候选人入职"; }
							if(powerForHC_CandidateEntryConfirm) { btn11Class[m] = "HC_setCandidateEntryConfirm_"; btn11[m] = "确定入职"; }
							if(powerForHC_setDimissionReason){ btn11Class[m] = "HC_setDimission_"; btn11[m] = processDetail.length()>0?"修改原因":"备注原因"; }//离职
							if(powerForHC_setUnPassIVReason) { btn11Class[m] = "HC_setUnPassIVReason_"; btn11[m] = processDetail.length()>0?"修改原因":"备注原因"; }//面试未通过
							if(powerForHC_setUnEntryReason) { btn11Class[m] = "HC_setUnEntryReason_"; btn11[m] = processDetail.length()>0?"修改原因":"备注原因"; }// 未入职							
						}
						m = 0;
						if(notifyDesc.length() > 0 && notifyDesc.indexOf("[DETAIL=")<0) { btn22Class[m] = ""; btn22[m] = notifyDesc; }
						m++;
						if(powerForHR_editOffer){ btn22Class[m] = "HC_showOffer_"; btn22[m] = "Offer查看"; }
						if(notifyDesc.indexOf("[DETAIL=")>=0 && processDetail.length()>0){ btn22Class[m] = "HC_showReason_"; btn22[m] = notifyDesc.replaceAll("\\[DETAIL=(.+?)\\]", ""); }
						m++;
						if(powerForHC_editHRIV){ btn22Class[m] = "HC_editHRIV_"; btn22[m] = "变更"; }
						if(powerForHC_editHCIV && STING_FLAG){ btn22Class[m] = "HC_editHCIV_"; btn22[m] = "变更"; }
						if(powerForHR_editOffer){ btn22Class[m] = "HC_editOffer_"; btn22[m] = "Offer变更"; }
						m++;
						if(delayFlag == 1){ btn22Class[m] = "HC_delay_"; btn22[m] = ""; }
						
					}

					String strbtn11 = "";
					for(String b: btn11){
						strbtn11 += b;
					}
					boolean isLongBtns11 = strbtn11.length() > 5;
					String strbtn22 = "";
					for(String b: btn22){
						strbtn22 += b;
					}
					boolean isLongBtns22 = strbtn22.length() > 8;
						
					I_DataParamType actionStatus_ = ComBeanI_DataParamType.get(Integer.parseInt(actionStatus));
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "tid"),//line%s
							ACE.NOTIFY_TYPE_NOTICE .equals(notifyType) ? "" : "gone",
							this.getDBString(o, "cid"),//data
							this.getDBString(o, "c4"),//data1:pcid
							this.getDBString(o, "tid"),//data2:tid
							ACE.NOTIFY_TYPE_NOTICE .equals(notifyType) ? "notice" : actionStatus.substring(2),
							HIUtil.isEmpty(actionStatus) ? "" : (ComBeanI_DataParamType.get(Integer.parseInt(actionStatus)).getMemo()+ivTime),
							actionCode.equals(ACE.ACTION_CODE_001) || actionCode.equals(ACE.ACTION_CODE_002) ? "1" :"0",	
							actionCode.equals(ACE.ACTION_CODE_001) || actionCode.equals(ACE.ACTION_CODE_002) ? "../doc/images/HC/call_icon.png" : this.getDBString(o, "face", "../doc/images/HC/face_c"+this.getDBString(o, "sex", "13200 ").substring(5).trim()+"_m.png"), //头像
	    					isLongBtns11 ? "limit_width1_short" : "",
	    					"<span title=\"" + (IFACE_DBBUG_FLAG ? " ["+stStatus+"-"+ComBeanI_DataParamType.getText(stStatus)+"]["+stCheckStatus+"-"+ComBeanI_DataParamType.getText(stCheckStatus)+"]["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"<"+this.getDBString(o, "pid")+">"+"]["+collectStatus+"-"+ComBeanI_DataParamType.getText(collectStatus)+"]" : "") + "\">" + this.getDBString(o, "c50") + "</span>",
							HIUtil.isEmpty(notifyStatusDesc) ? this.getDBString(o, "c52") : "",//当前职位
							notifyStatusDesc,//提醒状态说明
							HIUtil.isEmpty(notifyStatusDesc) || HIUtil.isEmpty(notifyDetail) ? "" :hrInfo,//HR信息
							btn11Class[0], // 样式
							btn11[0], // 文本
							btn11Class[1], // 样式
							btn11[1], // 文本
			    			isLongBtns22 ? "limit_width2_short" : "",
							!HIUtil.isEmpty(notifyDetail) ? "" :hrInfo,//HR信息
			    			isLongBtns22 ? "limit_width2_short" : "",
							notifyDetail,//提醒详情
							btn22Class[0], // 样式
							btn22[0], // 文本
							btn22Class[1], // 样式
							btn22[1], // 文本
							btn22Class[2], // 样式
							btn22[2], // 文本
							btn22Class[3], // 样式
							btn22[3], // 文本
							
					}));
					n++;
				}
			}

			if(n>0) return new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "", s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}	
	private LWResult ServiceDO_TaskClose(){
		LWResult result = null;
		String memo = "关闭事件";
		try{
			String tid = this.getParameter("tid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(this.sqlToZ("B_Action", memo, "dataid="+tid));
			sqls.add(String.format("update B_Action set c42=1 where dataid=%s", new Object[]{tid}));
			
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
	private LWResult ServiceDO_delayTask_HC(){
		LWResult result = null;
		String memo = "推迟任务";
		try{
			String tid = this.getParameter("tid");
			String delayTime = this.getParameter("delayTime");
			List<String> sqls = new ArrayList<String>();
			sqls.add(this.sqlToZ("B_Action", memo, "dataid="+tid));
			sqls.add(String.format("update B_Action set c49=to_date('%s','yyyy-mm-dd') where dataid=%s", new Object[]{
					delayTime,
					tid
			}));
			
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, "666"+memo+"成功");
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
	
	
	private LWResult ServiceDO_ActionList_HC(){
		System.out.println("[MainServiceBean.ServiceDO_ActionList_HC]=======================================");
		String memo = "我的面试日历";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = dbQuery(String.format("select a.c3,a.c5,a.c7,a.c9, b.c52,b.c55 from B_Candidate_Interview a, B_Action b where a.c1=b.c1 and a.c14=0 and a.c20=1 and b.c3=130001 and b.c2='%s' and instr(b.c32,'[MODE=面试日历]')>0", new Object[]{auid}));
			int n = 0;
			if(list!=null && list.size()>0){
				String[] ivtime;
				boolean isHCIV;
				for(Object o : list){
					isHCIV = this.getDBInt(o, "c3")==3;
					ivtime = this.getDBDate(o, "c7", "yyyy-MM-dd HH:mm").split(" ");
					s.append(String.format(HTML, new Object[]{
						 ivtime[0],
						 ivtime[1],
                         this.getDBString(o, "c5") + ("<span class=f9><span class=\"pd10 grey\">" + (isHCIV?"":"面试") + "</span>" + (isHCIV ? this.getDBString(o, "c52") : this.getDBString(o, "c55").substring(0, this.getDBString(o, "c55").indexOf("[HRFACE"))) + "</span>").replaceAll("\"", "'"),
                         this.getDBString(o, "c9").replaceAll("\\[LOCATION\\]", ""),
                         isHCIV ? "1" : "0",
					}));
					n++;
				}
			}
			
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}

	private LWResult ServiceDO_StatData(){
		System.out.println("[MainServiceBean.ServiceDO_StatData]=======================================");
		String memo = "我的统计数据";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String queryMK = this.getParameter("queryMK");
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
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			if(isHC()) list = dbQuery(String.format("select * from B_Stat_HC where c1=26000%s and c3=%s order by dataid", new Object[]{queryMK, auid}));
			if(isM()) list = dbQuery(String.format("select * from B_Stat where c1=26000%s order by dataid", new Object[]{queryMK}));
			int n = 0;
			if(list!=null && list.size()>0){
				for(Object o : list){
					if(n == maxcount) break;
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "c4"),
							this.getDBString(o, "c5"),
							this.getDBString(o, "c6"),
							this.getDBString(o, "c7"),
							this.getDBString(o, "c8"),
							this.getDBString(o, "c9"),
							this.getDBString(o, "c10"),
							this.getDBString(o, "c11"),
							this.getDBString(o, "c12"),
							this.getDBString(o, "c13"),
							this.getDBString(o, "c14"),
							this.getDBString(o, "c15"),
							this.getDBString(o, "c16"),
							this.getDBString(o, "c17"),
					}));
					n++;
					break;
				}
			}
			
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}

	private LWResult ServiceDO_Interest_HR(){
		LWResult result = null;
		System.out.println("[MainServiceBean.ServiceDO_StatData_HC]=======================================");
		String memo = "下周职位投票";
		try{
			String formParam = this.getParameter("formParam");
			String formContent = this.getParameter("formContent");
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format("insert into B_Vote_JobTop(dataid,c1,c2,c4,c5) values(seqB_Vote_JobTop.nextval,'%s','%s','%s','%s')", new Object[]{
					auid,
					myamid,
					formContent,
					formParam
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

	private LWResult ServiceDO_selectedInterest_HR(){
		LWResult result = null;
		System.out.println("[MainServiceBean.ServiceDO_selectedInterest_HR]=======================================");
		String memo = "查询下周职位投票";
		try{
			StringBuilder s = new StringBuilder("");
			List<Object> list = dbQuery(String.format("select * from B_Vote_JobTop where c1='%s' order by dataid desc", new Object[]{auid}));
			int n = 0;
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(this.getDBString(o, "c5").replaceAll(",", " \\| "));
					n++;
					break;
				}
			}
			if(n>0){
				result = new LWResult(LWResult.SUCCESS, s.toString());
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"无记录");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, memo);

		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}

}
