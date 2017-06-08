package com.svv.dms.app.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanI_DataParamType;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class CandiListService extends CandiBase {
	//queryMK //进行中 1 ，已收藏2，已结束3

	public String CandiListCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiListMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiListBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiListSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	
	//新增特别推荐--移除添加下周预备候选人
	public String removeCandidateFromHR(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_removeCandidateFromHR_SpecialCommand_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//新增特别推荐--候选人
	public String clickAddCandi(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_addCandidateToHR_SpecialCommand_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//从新增列表中删除HR
	public String deleteHRFromNextWeek(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_deleteHR_SpecialCommand_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);	
	}
	
	//添加HR到特别推荐
	public String clickAddHR(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_addHR_SpecialCommand_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);	
	}
	//全部移出
	public String deleteHRCommandList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_HRList_Better_Delete_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//单个移出特别推荐
	public String removeSpecialCommand(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_CandiList_Better_Remove_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	
	//特别推荐HR列表
	public String specialCommandHRList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_CandiList_Better_HRList_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	
	//特别推荐候选人列表
	public String specialCommandCandidateList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_CandiList_Better_CandidateList_M();
		loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//特别推荐候选人状态列表
	public String betterStatusItemList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_CandiList_better_StatusList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//候选人状态列表
	public String statusList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_CandiList_StatusList_HR();
		if(isHC()) result = ServiceDO_CandiList_StatusList();
		if(isM()) result = ServiceDO_CandiList_StatusList();
        loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//精选候选人状态列表
	public String goodStatusList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_CandiList_Good_StatusList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, " 候选人状态列表");
		return APPRETURN(result);
	}
	//HR: 候选人面试记录
	public String interviewList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_CandiList_interviewList_HR();
        loggerM(ComBeanLogType.TYPE_QUERY, " 候选人面试记录");
		return APPRETURN(result);
	}
	//HR: 候选人更多面试记录
	public String interviewMoreList(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_CandiList_interviewMoreList_HR();
        loggerM(ComBeanLogType.TYPE_QUERY, " 候选人面试记录");
		return APPRETURN(result);
	}
	//HR:每周推荐列表
	public String listPub(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_CandiPubList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览每周推荐候选人列表");
		return APPRETURN(result);
	}

	//候选人列表
	public String list(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_CandiList_HR();
		if(isHC()) result = ServiceDO_CandiList_HC();
		if(isM()) result = ServiceDO_CandiList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人列表");
		return APPRETURN(result);
	}
	//候选人流程进度
	public String process(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_CandiList_Process();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览候选人流程进度条");
		return APPRETURN(result);
	}
	// 相似候选人查询
	public String selectSimilarCandidate(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		result = ServiceDO_selectSimilarCandidate();
        loggerM(ComBeanLogType.TYPE_QUERY, "相似候选人查询");
		return APPRETURN(result);
	}
	private LWResult ServiceDO_selectSimilarCandidate(){
		System.out.println("[CandiListService.ServiceDO_selectSimilarCandidate]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String cid = this.getParameter("cid");
			String objectMode = this.getParameter("objectMode");
			List<Object> list = dbQuery(YSSQL.LWSimilarCandidate_ByHR(cid, auid, myamid, ACE.CANDI_STStatus_ST, ACE.CANDI_ActiveStatus_ACTIVE, "1"));
			int n = 0;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String barClass;
			if(list!=null && list.size()>0){
				for(Object o : list){
					if(n==4) break;
					 // 简历-相似优质候选人
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "cid"), //DATA
							this.getDBString(o, "pcid"), //DATA1
							this.getDBString(o, "c42")+"-"+this.getDBString(o, "c41"),
							this.getDBDataParam(o, "c13", "", "", "")+"｜"+this.getDBString(o, "c11","","","岁")+"｜"+this.getDBInt(o, "c39", "", "", "年工作经验")+"｜"+this.getDBString(o, "yuexin_k","","","K/月"),
							this.getDBString(o, "c15"),
							n== list.size()-1? "gone":""
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;
	
	private LWResult ServiceDO_CandiList_Process(){
		System.out.println("[CandiService.ServiceDO_CandiList_Process]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String cid = this.getParameter("cid");
			String pcid = this.getParameter("pcid");
			String objectMode = this.getParameter("objectMode");
			List<Object> list = dbQuery(YSSQL.LWCandidate_Process(cid, pcid));
			int n = 0;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String barClass;
			if(list!=null && list.size()>0){
				for(Object o : list){
					if(n==0){
						barClass = "processbar-first";
					}else if(n==list.size()-1){
						barClass = "processbar-last";
				    }else{
				    	barClass = "processbar";
				    }

					s.append(String.format(HTML, new Object[]{
							barClass,
							this.getDBString(o, "detail").length() >0 ? "HR_showDetail_ HC_showDetail_ pointer":"",
							this.getDBString(o, "detail").length() >0 ? "process-info" :"",
							this.getDBString(o, "processSeq"),
							this.getDBString(o, "title"),
							this.getDBDate(o, "time", "yyyy-MM-dd").replaceAll("-", "."),
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
	
	private LWResult ServiceDO_CandiPubList(){
		System.out.println("[CandiService.ServiceDO_CandiPubList]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String city = this.getParameter("city");
			String job = this.getParameter("job");
			String keyword = this.getParameter("keyword");
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
			String isGood = "1"; // 只有精选的才列入【每周精选】
			if(auid.length()>20) list = dbQuery(YSSQL.LWCandidatePub_QueryByC(auid, myamid, ACE.CANDI_STStatus_ST, ACE.CANDI_ActiveStatus_ACTIVE, "", isGood, city, job, keyword, FIRSTID, LASTID));
			else list = dbQuery(YSSQL.LWCandidatePub_QueryByC(ACE.CANDI_STStatus_ST, ACE.CANDI_ActiveStatus_ACTIVE, "", isGood, city, job, keyword, FIRSTID, LASTID));

			int n = 0;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				String html = ComBeanAPPModule.getHtml((auid.length()>20) ? "H00102-1" : "H00101-1");
				List<String> html_formats = LWUtil.HTML_getFormatHTMLs(html, new String[]{"EDUCATION", "JOB"});
				html = html_formats.get(0);
				String html_EDUCATION = html_formats.get(1);
				String html_JOB = html_formats.get(2);
				String[] ss;
				String education_str, job_str;
				int i = 0;
				
				firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					education_str = this.getDBString(o, "c57");
					if(education_str.length()>0){
						ss = education_str.split("\\|");
						i = 0;
						education_str = LWUtil.HTML_toDatalist(html_EDUCATION, new String[][]{{ss[i++], ss[i++]}}, "", "");
					}
					
					job_str = this.getDBString(o, "c60");
					if(job_str.length()>0){
						ss = job_str.split(SP1);
						i = 0;
						job_str = LWUtil.HTML_toDatalist(html_JOB, new String[][]{ss[i++].split("\\|"), ss.length>i?ss[i++].split("\\|"):null, ss.length>i?ss[i++].split("\\|"):null}, "", "\\.\\.\\.");
					}

					String candiStatus = this.getDBString(o, "candiStatus");
					String collectStatus = this.getDBString(o, "collectStatus");
					boolean isCollected = collectStatus.equals(ACE.COLLECT_STATUS_COLLECTED);//收藏
					boolean isCandiStatus = candiStatus.equals(ACE.CANDI_STATUS_ING);//进行中
					String[] btnClass = {"gone","gone","gone"}, btnText = {"null","",""};
//					String showImg = "";
					if(HIUtil.isEmpty(auid)){
						btnClass[0] = "";
						btnText[0] = "marker_new";

					}else{
						if(isCandiStatus){ // 是邀请面试
							btnClass[0] = "";
							btnText[0] ="marker_yaoqing";
							btnClass[1] = "gone";
							btnText[1] ="";
							btnClass[2] = "";
							btnText[2] ="已邀请面试";
						}else if(isCollected){ // 收藏
							btnClass[0] = "";
							btnText[0] ="marker_guanzhu";
							btnClass[1] = "gone";
							btnText[1] ="";
							btnClass[2] = "";
							btnText[2] ="已收藏";
						}else{
							btnClass[1] = "";
							btnText[1] ="";
						}
					}
					s.append(String.format(html, new Object[]{
							this.getDBString(o, "c1"),
							this.getDBInt(o, "isRecommend") == 1 ? "" :btnClass[0],
							this.getDBInt(o, "isRecommend") == 1 ? "hr_relation" : btnText[0],
							this.getDBInt(o, "isViewed") == 1 ? "grey" : "",
							this.getDBDataParam(o, "c36", "["+this.getDBString(o, "c42")+"]", "", ""), // 职位类型
							HIUtil.isEmpty(this.getDBString(o, "c13")) ? "gone" : "",
							this.getDBDataParam(o, "c13", "", "", ""), // 男
							HIUtil.isEmpty(this.getDBString(o, "c11")) ? "gone" : "",
							this.getDBString(o, "c11","","","岁"), // 27岁
							HIUtil.isEmpty(this.getDBInt(o, "c39", "", "", "")) ? "gone" : "",
							this.getDBInt(o, "c39", "", "", "年工作经验"),// 3年工作经验
							HIUtil.isEmpty(this.getDBString(o, "c43", "", "", "")) ? "gone" : "",
							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
							HIUtil.isEmpty(this.getDBString(o, "c15")) ? "gone" : "",
							this.getDBString(o, "c15"), // 上海
							"T"+HIUtil.lPad(this.getDBString(o, "dataid"), "00000"), // #74554  //this.getDBString(o, "c7")
							isCollected ? "1" : "0",
							HIUtil.isEmpty(this.getDBString(o, "c57")) ? "gone" : "",
							education_str, // 苏州大学
							HIUtil.isEmpty(this.getDBString(o, "c60")) ? "gone" : "",
							job_str, // 上海XXXXXXX公司
							btnClass[1],
							this.getDBString(o, "c28"), // 浏览数
							btnClass[2],
							btnText[2],
					}));
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
	
	private LWResult ServiceDO_CandiList_HC(){
		System.out.println("[CandiService.ServiceDO_CandiList_HC]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String objectMode2 = this.getParameter("objectMode2");
			int queryMK = this.getParameter("queryMK", -1); //候选人0 ，进行中 1 ，已收藏2，已结束3
			String c_stStatus = this.getParameter("stStatus"); //入库状态
			String c_skillMarker = this.getParameter("sm"); //技能标签
			String c_status = this.getParameter("status").trim(); //查询状态
			String c_keyword = this.getParameter("keyword"); //关键字
			String sortMK = this.getParameter("sortMK"); //排序
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
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			int index = 100;
			String html = null;
			String cid = null;
			String showMore = "<div class=\"text-c bg-white showmore_\" data=\"%s\" style=\"margin-top:-5px\"><span class=\"pdl10 dark-grey pointer\">显示所有%s</span></div>";

			int n = 0;
			String firstDataid = null, lastDataid = null;
			if(queryMK==0){ //候选人：HC端
                list = dbQuery(YSSQL.LWCandidate_QueryByC(auid, "", "", c_stStatus, "", c_status, c_skillMarker, "", "", "", c_keyword, FIRSTID, LASTID, sortMK));
                if(list!=null && list.size()>0){
                	firstDataid = this.getDBString(list.get(0), "sortid");
    				for(Object o : list){
    					if(n == maxcount) break;
    					lastDataid = this.getDBString(o, "sortid");
    					
    					if(HTML.indexOf("<INDEX>")>=0){
    						index++;
    						html = HTML.replaceAll("<INDEX>", String.valueOf(index));
    					}else{
    						html = HTML;
    					}
    					cid = this.getDBString(o, "c1");
    					String stStatus = this.getDBString(o, "stStatus");
    					String stCheckStatus = this.getDBString(o, "stCheckStatus");
    					boolean powerForHC_applyActive = powerForHC_applyST(stStatus, stCheckStatus);
    					boolean powerForHC_cancelST = powerForHC_cancelST(stStatus, stCheckStatus);
    					boolean powerForHC_addHCIV = powerForHC_addHCIV(stCheckStatus);
    					boolean powerForHC_editHCIV = powerForHC_editHCIV(stCheckStatus);
    					boolean powerForHC_editHCComment = powerForHC_editHCComment(stCheckStatus);
    					boolean powerForHC_commitHCComment = powerForHC_commitHCComment(stCheckStatus);
    					boolean powerForHC_checkingHCComment = powerForHC_checkingHCComment(stCheckStatus);
    					boolean powerForHC_onlineHCComment = powerForHC_onlineHCComment(stCheckStatus);
						String[] btnClass = {"gone","gone","gone","gone","gone"}, btn = {"","","","",""};
    					if(isHC()){
    						int m = 0;
    						if(powerForHC_cancelST) { btnClass[m] = "null"; btn[m] = "入库审核中...";  }
    						if(powerForHC_checkingHCComment) {btnClass[m] = "null"; btn[m] = "顾问面评审核中...";}
    						if(powerForHC_applyActive){ btnClass[m] = "null"; btn[m] = this.getDBString(o, "c90"); }
    						m++;
    						if(powerForHC_editHCIV) {
	    						Calendar calendar = Calendar.getInstance();
	    						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    						String time = this.getDBDate(o, "ivTime", "yyyy-MM-dd HH:mm");
	    						String showTime="";
	    						if(time.length() >0){
	    							Date date = format.parse(time);
	    					  		calendar.setTime(date);
	    							String newTime = LWUtil.getWeekOfDate(time);
									if(newTime.equals("月份")){
										 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
									}
									showTime = "面试：" + newTime + " " +time.split(" ")[1];
	    						}else{
	    							showTime = "";
	    						}
	    						if(powerForHC_editHCIV) { btnClass[m] = "HC_showDetail_"; btn[m] = showTime;}//一会需要加的3-31
    						}
    						m++;
    						if(powerForHC_applyActive){ btnClass[m] = "HC_applyST_"; btn[m] = "申请入库"; }
    						if(powerForHC_cancelST) { btnClass[m] = "HC_cancelST_"; btn[m] = "取消入库";}
    						if(powerForHC_addHCIV) { btnClass[m] = "HC_addHCIV_"; btn[m] = "顾问面试"; }
    						if(powerForHC_editHCIV) { btnClass[m] = "HC_editHCIV_";btn[m] = "面试变更"; }
    						if(powerForHC_editHCComment) { btnClass[m] = "HC_editHCComment_"; btn[m] = "编辑顾问面评";}
    						m++;
    						if(powerForHC_commitHCComment) { btnClass[m] = "HC_commitHCComment_"; btn[m] = "提交审核";}
    						m++;
    						if(powerForHC_onlineHCComment) { btnClass[m] = "null"; btn[m] = "";};
    					}
    					String status = stStatus.substring(3,5);
    					if(stStatus.equals(ACE.CANDI_STStatus_ST)){ status += this.getDBString(o, "c5").substring(4,6);}
    					s.append(String.format(html, new Object[]{
    							cid,
    							this.getDBString(o, "c91").indexOf("1")>=0 ? "" :"gone",  // 是否有精选图../doc/images/HC/face_c
								this.getDBString(o, "c61", "../doc/images/HC/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_m.png"), //性别 face_c1_m.png
    							status, //入库状态 status_001_txt.png
    							this.getDBString(o, "c8"), //姓名  李大志
    							this.getDBString(o, "c41"),
    							this.getDBString(o, "c42"),
    							this.getDBString(o, "c15").length() >0 ? "" :"gone",
    							this.getDBString(o, "c15"), //上海
    							"<span title=\"" + (IFACE_DBBUG_FLAG ? " ["+stStatus+"-"+ComBeanI_DataParamType.getText(stStatus)+"]["+stCheckStatus+"-"+ComBeanI_DataParamType.getText(stCheckStatus)+"]" : "") + "\">" + "T"+HIUtil.lPad(this.getDBString(o, "dataid"), "00000") + "</span>", // #74554  //this.getDBString(o, "c7")
    							this.getDBDataParam(o, "c13", "", "", ""), // 男
    							this.getDBString(o, "c11","","","岁"), // 27岁
    							this.getDBDataParam(o, "c25", "", "", ""), // 本科
    							this.getDBString(o, "c39").equals("0") ? "gone" :"",
    							this.getDBInt(o, "c39", "", "", "年工作经验"),// 3年工作经验
    							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
    							btnClass[0], //
    							btn[0],
    							btnClass[1],
    							btn[1],
    							btnClass[2],
    							btn[2],
    							btnClass[3],
    							btn[3],
    							btnClass[4],
    							btn[4], //
    							this.getDBString(o, "c28"), // 99 浏览
    							this.getDBString(o, "c27"), // 66 收藏
    							this.getDBString(o, "c29"), // 33 流程
    					}));					
    					n++;
    				}
    			}

			}else if(queryMK==1){ //进行中
				String HTML2 = ComBeanAPPModule.getHtml(objectMode2);
				list = dbQuery(YSSQL.LWCandidate_Process_QueryByC("O", "130001", "", auid, "", "", "", ACE.CANDI_STATUS_ING, c_status, "", "", "", c_skillMarker, c_keyword, FIRSTID, LASTID));

				if(list!=null && list.size()>0){
    				String html2 = null;
    				String tmp_cid = "";
    				int subNum = 0;
    				int MAX_SUBNUM = 3;
                	firstDataid = this.getDBString(list.get(0), "sortid");
    				for(Object o : list){
    					if(n == maxcount) break;
    					lastDataid = this.getDBString(o, "sortid");
    					
    					index++;
    					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
    					if(HTML2.indexOf("<INDEX>")>=0){//////////////////////////////////////////////////////////////////////////HTML2
    						index++;
    						html2 = HTML2.replaceAll("<INDEX>", String.valueOf(index));
    					}else{
    						html2 = HTML2;
    					}

    					cid = this.getDBString(o, "c1");
    					String candiStatus = this.getDBString(o, "candiStatus");
    					String activeStatus = this.getDBString(o, "activeStatus");
    					String processInnerStatus = this.getDBString(o, "processInnerStatus");
    					boolean powerForHC_addHRIV = powerForHC_addHRIV(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_editHRIV = powerForHC_editHRIV(activeStatus, candiStatus, processInnerStatus);
						boolean powerForHC_confirmHRIV = powerForHC_confirmHRIV(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_candidateReplyIV = powerForHC_candidateReplyIV(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_candidateReplyOffer = powerForHC_candidateReplyOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_setIVResult = powerForHC_setIVResult(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_nextIV = powerForHC_nextIV(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_CandidateReplyEntry = powerForHC_CandidateReplyEntry(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_CandidateEntryConfirm = powerForHC_CandidateEntryConfirm(activeStatus, candiStatus, processInnerStatus);
    					String[] btn11Class = {"gone","gone","gone"}, btn11 = {"","",""};
    					
    					if(isHC()){
    						int m = 0;
    						if(powerForHR_editOffer){ btn11Class[m] = "HC_showOffer_"; btn11[m] = "查看Offer"; }
    						if(powerForHC_editHRIV && !powerForHC_confirmHRIV) {
	    						Calendar calendar = Calendar.getInstance();
	    						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    						String time = this.getDBDate(o, "ivTime", "yyyy-MM-dd HH:mm");
	    						String showTime="";
	    						if(time.length() >0){
	    							Date date = format.parse(time);
	    					  		calendar.setTime(date);
	    							String newTime = LWUtil.getWeekOfDate(time);
									if(newTime.equals("月份")){
										 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
									}
									showTime = "面试：" + newTime + " " +time.split(" ")[1];
	    						}else{
	    							showTime = "";
	    						}
	    						if(powerForHC_editHRIV) { btn11Class[m] = "HC_showDetail_"; btn11[m] = showTime;}
    						}
    						if(powerForHC_confirmHRIV){ btn11Class[m] = "HR_showDetail_"; btn11[m] = "面试信息确定中"; }
    						m++;
    						if(powerForHR_editOffer){ btn11Class[m] = "HC_editOffer_"; btn11[m] = "Offer变更"; }
    						m++;
    						if(powerForHC_candidateReplyOffer){ btn11Class[m] = "HC_setCandidateReplyOffer_"; btn11[m] = "候选人反馈"; }
							if(powerForHC_addHRIV) { btn11Class[m] = "HC_addHRIV_"; btn11[m] = "安排面试"; }
							if(powerForHC_confirmHRIV) { btn11Class[m] = "HC_confirmHRIV_"; btn11[m] = "确定面试"; }
							if(powerForHC_editHRIV) { btn11Class[m] = "HC_editHRIV_"; btn11[m] = "变更面试"; }
							if(powerForHC_setIVResult) { btn11Class[m] = "HC_setHRIVResult_"; btn11[m] = "面试反馈"; }
							if(powerForHC_nextIV) { btn11Class[m] = "HC_setHRIVNext_"; btn11[m] = "面试反馈"; }
							if(powerForHR_addOffer) { btn11Class[m] = "HC_uploadOffer_"; btn11[m] = "上传Offer"; }
							if(powerForHC_candidateReplyIV){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "候选人反馈"; }
							if(powerForHC_CandidateReplyEntry){ btn11Class[m] = "HC_setCandidateReplyEntry_"; btn11[m] = "候选人入职"; }
							if(powerForHC_CandidateEntryConfirm){ btn11Class[m] = "HC_setCandidateEntryConfirm_"; btn11[m] = "确定入职"; }
							
    					}
    					
    					if(!cid.equals(tmp_cid)){
    						tmp_cid = cid;
    						if(subNum > MAX_SUBNUM){
    							s.append("</div>");
    							s.append(String.format(showMore, new Object[]{cid, "流程"}));
    						}
    						subNum = 0;
        					s.append(String.format(html, new Object[]{
        							cid,//data
        							this.getDBString(o, "pcid"),//data1
        							this.getDBString(o, "c91").equals("1") ? "" :"gone",  // 是否有精选图
									this.getDBString(o, "c61", "../doc/images/HC/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_m.png"), //face_c1_m.png
        							this.getDBString(o, "c8"), // 李大志
        							this.getDBString(o, "c41"), // LV
        							this.getDBString(o, "c42"), // 品牌经理
        							this.getDBString(o, "c15"), // 上海
        							"T"+HIUtil.lPad(this.getDBString(o, "dataid"), "00000"), // #74554  //this.getDBString(o, "c7")
        							this.getDBDataParam(o, "c13", "", "", ""), // 男
        							this.getDBString(o, "c11","","","岁"), // 岁
        							this.getDBDataParam(o, "c25", "", "", ""),
        							this.getDBInt(o, "c39", "", "", "年工作经验"),
        							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
        							this.getDBString(o, "c28"), // 浏览数
        							this.getDBString(o, "c29"), // 邀请面试数
        							this.getDBString(o, "c27"), // 213 收藏
        					}));
    					}
						if(subNum == MAX_SUBNUM){//等于3的时候
							s.append("<div class=\"gone\">");	
						}
    					s.append(String.format(html2, new Object[]{
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    							processInnerStatus.substring(2),
    							this.getDBString(o, "mmName") ,// 公司名称
    							this.getDBString(o, "processJobID"),//应聘的jobID
    							this.getDBString(o, "processCompanyID"),//应聘的processCompanyID
    							this.getDBString(o, "jobName", "", " | ", ""), // 公司名称 公司职位
    							this.getDBString(o, "hrAuid"),
    							"<span title=\"" + (IFACE_DBBUG_FLAG ? "["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"<"+this.getDBString(o, "pid")+">"+"]" : "") + "\">" + this.getDBString(o, "hrName")+"</span>",//
    							btn11Class[0], // 样式
    							btn11[0], // 文本
    							btn11Class[1], // 样式
    							btn11[1], // 文本
    							btn11Class[2], // 样式
    							btn11[2], // 文本
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    					}));
    					n++;
    					subNum++;
    				}
					if(subNum > MAX_SUBNUM){
						s.append("</div>");
						s.append(String.format(showMore, new Object[]{cid, "流程"}));
					}
					
    			}
			
			}else if(queryMK==2){ //被收藏
				String HTML2 = ComBeanAPPModule.getHtml(objectMode2);
				String c_candiStatus = null;
				//没面试过
				if(c_status.equals("0")){
					c_candiStatus = "";
				}
				//进行中
				if(c_status.equals("1")){
					c_candiStatus = ACE.CANDI_STATUS_ING;
				}
				//已结束
				if(c_status.equals("9")){
					c_candiStatus = ACE.CANDI_STATUS_OVER;
				}
				list = dbQuery(YSSQL.LWCandidate_Process_QueryByC("O", "130001", "", auid, myamid, "", "", c_candiStatus, "", "", "", ACE.COLLECT_STATUS_COLLECTED, c_skillMarker, c_keyword, FIRSTID, LASTID));

				if(list!=null && list.size()>0){
    				String html2 = null;
    				String tmp_cid = "";
    				int subNum = 0;
    				int MAX_SUBNUM = 3;
                	firstDataid = this.getDBString(list.get(0), "sortid");
    				for(Object o : list){
    					if(n == maxcount) break;
    					lastDataid = this.getDBString(o, "sortid");
    					
    					index++;
    					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
    					if(HTML2.indexOf("<INDEX>")>=0){//////////////////////////////////////////////////////////////////////////HTML2
    						index++;
    						html2 = HTML2.replaceAll("<INDEX>", String.valueOf(index));
    					}else{
    						html2 = HTML2;
    					}

    					cid = this.getDBString(o, "c1");
    					String activeStatus = this.getDBString(o, "activeStatus");
    					String candiStatus = this.getDBString(o, "candiStatus");
    					String collectStatus = this.getDBString(o, "collectStatus");
    					String processInnerStatus = this.getDBString(o, "processInnerStatus");
    					boolean powerForHC_candidateReplyIV = powerForHC_candidateReplyIV(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_candidateReplyOffer = powerForHC_candidateReplyOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_setIVResult = powerForHC_setIVResult(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_CandidateReplyEntry = powerForHC_CandidateReplyEntry(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_CandidateEntryConfirm = powerForHC_CandidateEntryConfirm(activeStatus, candiStatus, processInnerStatus);
    					String[] btn11Class = {"gone","gone","gone"}, btn11 = {"","",""};
    					 
						int m = 0;
						if(powerForHR_editOffer){ btn11Class[m] = "HC_showOffer_"; btn11[m] = "查看Offer"; }
						m++;
						if(powerForHR_editOffer){ btn11Class[m] = "HC_showOffer_"; btn11[m] = "Offer变更"; }
						m++;
						if(powerForHC_candidateReplyOffer){ btn11Class[m] = "HC_setCandidateReplyOffer_"; btn11[m] = "候选人反馈"; }
						if(powerForHC_setIVResult) { btn11Class[m] = "HC_setHRIVResult_"; btn11[m] = "面试反馈"; }
						if(powerForHR_addOffer) { btn11Class[m] = "HC_uploadOffer_"; btn11[m] = "上传Offer"; }
						if(powerForHC_candidateReplyIV){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "候选人反馈"; }
						if(powerForHC_CandidateReplyEntry){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "候选人入职"; }
						if(powerForHC_CandidateEntryConfirm){ btn11Class[m] = "HC_setCandidateReplyIV_"; btn11[m] = "确定入职"; }

    					
    					if(!this.getDBString(o, "c1").equals(tmp_cid)){
    						tmp_cid = this.getDBString(o, "c1");
    						
    						if(subNum > MAX_SUBNUM){
    							s.append("</div>");
    							s.append(String.format(showMore, new Object[]{cid, "收藏"}));
    						}
    						subNum = 0;
    						
        					s.append(String.format(html, new Object[]{
        							cid,//data
        							this.getDBString(o, "pcid"),//data1
        							this.getDBString(o, "c91").equals("1") ? "" :"gone",  // 是否有精选图
									this.getDBString(o, "c61", "../doc/images/HC/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_m.png"), //face_c1_m.png
        							this.getDBString(o, "c8"), // 李大志
        							this.getDBString(o, "c41"), // LV
        							this.getDBString(o, "c42"), // 品牌经理
        							this.getDBString(o, "c15"), // 上海
        							this.getDBDataParam(o, "c13", "", "", ""), // 男
        							this.getDBString(o, "c11","","","岁"), // 27岁
        							this.getDBDataParam(o, "c25", "", "", ""), // 学历
        							this.getDBInt(o, "c39", "", "", "年工作经验"),
        							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
        							"T"+HIUtil.lPad(this.getDBString(o, "dataid"), "00000"), // #74554  //this.getDBString(o, "c7")
        							this.getDBString(o, "c28"), // 浏览数
        							this.getDBString(o, "c29"), // 邀请面试数
        							this.getDBString(o, "c27"), // 213 收藏
        					}));
    					}
						if(subNum == MAX_SUBNUM){
							s.append("<div class=\"gone\">");
						}
    					s.append(String.format(html2, new Object[]{
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    							HIUtil.isEmpty(processInnerStatus, collectStatus).substring(2),
    							this.getDBString(o, "mmName"), // 公司名称 公司职位
    							"<span title=\"" + (IFACE_DBBUG_FLAG ? " ["+activeStatus+"-"+ComBeanI_DataParamType.getText(activeStatus)+"]["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"<"+this.getDBString(o, "pid")+">"+"]["+collectStatus+"-"+ComBeanI_DataParamType.getText(collectStatus)+"]" : "") + "\">" + this.getDBString(o, "hrName") + "</span>",//HRname
    							HIUtil.isEmpty(processInnerStatus) ? "" : "gone",// 职位div
    							this.getDBString(o, "jobName"),
    							this.getDBDate(o, "collectDate", DATE_FORMAT),//日期
    							btn11Class[0], // 样式
    							btn11[0], // 文本
    							btn11Class[1], // 样式
    							btn11[1], // 文本
    							btn11Class[2], // 样式
    							btn11[2], // 文本
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    					}));
    					n++;
    					subNum++;
    				}
					if(subNum > MAX_SUBNUM){
						s.append("</div>");
						s.append(String.format(showMore, new Object[]{cid, "收藏"}));
					}
    			}
			}else if(queryMK==3){ //已结束
				String HTML2 = ComBeanAPPModule.getHtml(objectMode2);
				list = dbQuery(YSSQL.LWCandidate_Process_QueryByC("O", "", "", auid, "", "", "", ACE.CANDI_STATUS_OVER, "", c_status, "", "", c_skillMarker, c_keyword, FIRSTID, LASTID));

				if(list!=null && list.size()>0){
    				String html2 = null;
    				String tmp_cid = "";
    				int subNum = 0;
    				int MAX_SUBNUM = 3;
                	firstDataid = this.getDBString(list.get(0), "sortid");
    				for(Object o : list){
    					if(n == maxcount) break;
    					lastDataid = this.getDBString(o, "sortid");
    					index++;
    					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
    					if(HTML2.indexOf("<INDEX>")>=0){//////////////////////////////////////////////////////////////////////////HTML2
    						index++;
    						html2 = HTML2.replaceAll("<INDEX>", String.valueOf(index));
    					}else{
    						html2 = HTML2;
    					}
    					cid = this.getDBString(o, "cid");
    					String activeStatus = this.getDBString(o, "activeStatus");
    					String processCloseStatus = this.getDBString(o, "processCloseStatus");
    					String candiStatus = this.getDBString(o, "candiStatus");
    					String processInnerStatus = this.getDBString(o, "processInnerStatus");
    					boolean powerForHC_candidateReplyOffer = powerForHC_candidateReplyOffer(activeStatus, candiStatus, processInnerStatus);
    					boolean powerForHC_setDimissionReason = powerForHC_setDimissionReason(processInnerStatus);
    					boolean powerForHC_setUnPassIVReason = powerForHC_setUnPassIVReason(candiStatus, processInnerStatus);
    					boolean powerForHC_setUnEntryReason = powerForHC_setUnEntryReason(candiStatus, processInnerStatus);
    					boolean powerForHR_billing = powerForHR_billing(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayingToM = powerForHR_billPayingToM(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayedToM = powerForHR_billPayedToM(candiStatus, processInnerStatus);
    					boolean powerForHR_billPayedToHC = powerForHR_billPayedToHC(candiStatus, processInnerStatus);
    					String[] btn11Class = {"gone","gone","gone"}, btn11 = {"","",""};
    					 
    					if(isHC()){
    						int m = 0;
    						if(this.getDBString(o, "processDetail").length()>0){
    						    btn11Class[m] = "HC_showReason_"; btn11[m] = "查看原因";
    						}
    						m++;
    						if(this.getDBString(o, "processDetail").length()>0){
    							if(powerForHC_setDimissionReason){ btn11Class[m] = "HC_setDimission_"; btn11[m] = "修改原因"; }//离职
    							if(powerForHC_setUnPassIVReason) { btn11Class[m] = "HC_setUnPassIVReason_"; btn11[m] = "修改原因"; }//面试未通过
    							if(powerForHC_setUnEntryReason) { btn11Class[m] = "HC_setUnEntryReason_"; btn11[m] = "修改原因"; }// 未入职
    							
    						}else{
    							if(powerForHC_setDimissionReason){ btn11Class[m] = "HC_setDimission_"; btn11[m] = "备注原因"; }//离职
    							if(powerForHC_setUnPassIVReason) { btn11Class[m] = "HC_setUnPassIVReason_"; btn11[m] = "备注原因"; }//面试未通过
    							if(powerForHC_setUnEntryReason) { btn11Class[m] = "HC_setUnEntryReason_"; btn11[m] = "备注原因"; }// 未入职
    						}
    						m++;
    						if(powerForHR_billing){ btn11Class[m] = ""; btn11[m] = "待开票"; }
    						if(powerForHR_billPayingToM){ btn11Class[m] = ""; btn11[m] = "待付款到平台"; }
							if(powerForHR_billPayedToM) { btn11Class[m] = ""; btn11[m] = "待付款到您的账户"; }
							if(powerForHR_billPayedToHC) { btn11Class[m] = ""; btn11[m] = "已付款到您的账户"; }
							
    					}
    					
    					if(!cid.equals(tmp_cid)){ 
    						tmp_cid = cid;
    						
    						if(subNum >= MAX_SUBNUM){
    							s.append("</div>");
    							s.append(String.format(showMore, new Object[]{cid, "流程"}));
    						}
    						subNum = 0;
        					s.append(String.format(html, new Object[]{
        							cid,//data
        							this.getDBString(o, "pcid"),//data1
        							this.getDBString(o, "c91").equals("1") ? "" :"gone",  // 是否有精选图
									this.getDBString(o, "c61", "../doc/images/HC/face_c"+this.getDBString(o, "c13", "13200 ").substring(5).trim()+"_m.png"), //face_c1_m.png
        							this.getDBString(o, "c8"), // 李大志
        							this.getDBString(o, "c41"), // LV
        							this.getDBString(o, "c42"), // 品牌经理
        							this.getDBString(o, "c15"), // 上海
        							"T"+HIUtil.lPad(this.getDBString(o, "dataid"), "00000"), // #74554  //this.getDBString(o, "c7")
        							this.getDBDataParam(o, "c13", "", "", ""), // 男
        							this.getDBString(o, "c11","","","岁"), // 27岁
        							this.getDBDataParam(o, "c25", "", "", ""), // 本科
        							this.getDBInt(o, "c39", "", "", "年工作经验"),// 3年工作经验
        							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
        							this.getDBString(o, "c28"), // 浏览数
        							this.getDBString(o, "c29"), // 邀请面试数
        							this.getDBString(o, "c27"), // 213 收藏
        					}));
    					}
						if(subNum == MAX_SUBNUM){
							s.append("<div class=\"gone\">");
						}
						
    					s.append(String.format(html2, new Object[]{
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    							processCloseStatus.substring(2),
    							this.getDBString(o, "mmName") ,// 公司名称
    							this.getDBString(o, "processJobID"),//应聘的jobID
    							this.getDBString(o, "processCompanyID"),//应聘的processCompanyID
    							this.getDBString(o, "jobName", "", " | ", ""), // 公司名称 公司职位
    							"<span title=\"" + (IFACE_DBBUG_FLAG ? "<"+this.getDBString(o, "sortid")+">"+" ["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"]" : "") + "\">" + this.getDBDate(o, "processEndTime", DATE_FORMAT) + "</span>",//日期
    							btn11Class[0], // 样式
    							btn11[0], // 文本
    							btn11Class[1], // 样式
    							btn11[1], // 文本
    							btn11Class[2], // 样式
    							btn11[2], // 文本
    							cid,//data
    							this.getDBString(o, "pcid"),//data1
    					}));
    					n++;
    					subNum++;
    				}
					if(subNum >= MAX_SUBNUM){
						s.append("</div>");
						s.append(String.format(showMore, new Object[]{cid, "流程"}));
					}
    				
    			}
			}
			
		    if(n>0) return new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "", s.toString());
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}

    
	private LWResult ServiceDO_CandiList_HR(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //进行中 1 ，已收藏2，已结束3
			String c_status = this.getParameter("status"); //流程状态
			String job = this.getParameter("job"); //职位
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
			if(queryMK==1){ //进行中
				if(c_status.length()>0){ job = ""; keyword = "";}
                list = dbQuery(YSSQL.LWCandidate_Process_QueryByC_HR("O", "130001", "", "", "", auid, myamid, ACE.CANDI_STATUS_ING, c_status, "", "", "", job, keyword, FIRSTID, LASTID));
			}else if(queryMK==2){ //已收藏
			    list = dbQuery(YSSQL.LWCandidate_Process_QueryByC_HR("O", "130001", "", "", "", auid, myamid, "", "", "", "", ACE.COLLECT_STATUS_COLLECTED, job, keyword, FIRSTID, LASTID));
			}else if(queryMK==3){ //已结束
			    list = dbQuery(YSSQL.LWCandidate_Process_QueryByC_HR("O", "", "", "", "", auid, myamid, ACE.CANDI_STATUS_OVER, "", "", "", "", job, keyword, FIRSTID, LASTID));
			}
			int n = 0;

			String HTML = ComBeanAPPModule.getHtml(objectMode);
			int index = 100;
			String html = null;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				
				List<String> html_formats = LWUtil.HTML_getFormatHTMLs(HTML, new String[]{"EDUCATION", "JOB"});
				HTML = html_formats.get(0);
				String html_EDUCATION = html_formats.get(1);
				String html_JOB = html_formats.get(2);
				String[] ss;
				String education_str, job_str;
				int i = 0;

            	firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					if(HTML.indexOf("<INDEX>")>=0){
						index++;
						html = HTML.replaceAll("<INDEX>", String.valueOf(index));
					}else{
						html = HTML;
					}
					
					education_str = this.getDBString(o, "c57");
					if(education_str.length()>0){
						ss = education_str.split("\\|");
						i = 0;
						education_str = LWUtil.HTML_toDatalist(html_EDUCATION, new String[][]{{ss[i++], ss[i++]}}, "", "");
					}
					job_str = this.getDBString(o, "c60");
					if(job_str.length()>0){
						ss = job_str.split(SP1);
						i = 0;
						job_str = LWUtil.HTML_toDatalist(html_JOB, new String[][]{ss[i++].split("\\|"), ss.length>i?ss[i++].split("\\|"):null, ss.length>i?ss[i++].split("\\|"):null}, "", "\\.\\.\\.");
					}

					String activeStatus = this.getDBString(o, "activeStatus");
					String processDetail = this.getDBString(o, "processDetail");
					String candiStatus = getDBString(o, "candiStatus");
					String processCloseStatus = getDBString(o, "processCloseStatus");
					String processInnerStatus = getDBString(o, "processInnerStatus");
					String collectStatus = this.getDBString(o, "collectStatus");
					String status = null;
					long processSecond = DBUtil.getDBDouble(o, "processSecond").longValue();
					String processTimer = null;
					if(processSecond>0) {
						processTimer = HIUtil.lPad(processSecond/60/60+"", "00") + ":" + HIUtil.lPad((processSecond/60)%60+"", "00");
					}

					boolean powerForHR_inviteIV = powerForHR_inviteIV(activeStatus, candiStatus, processCloseStatus);
					boolean powerForHR_addIV = powerForHR_addIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_editIV = powerForHR_editIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_confirmingIV = powerForHR_confirmingIV(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_setIVResultPass = powerForHR_setIVResult_Pass(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_setEntryResult = powerForHR_setEntryResult(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_entrying = powerForHR_entrying(candiStatus, processInnerStatus);
					boolean powerForHR_unCollect = powerForHR_unCollect(collectStatus);
					boolean powerForHR_Offer = powerForHR_Offer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_addOffer = powerForHR_addOffer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_editOffer = powerForHR_editOffer(activeStatus, candiStatus, processInnerStatus);
					boolean powerForHR_setDimission = powerForHR_setDimission(candiStatus, processInnerStatus);
					boolean powerForHR_dimissioning = powerForHR_dimissioning(candiStatus, processInnerStatus);
					boolean powerForHR_billing = powerForHR_billing(candiStatus, processInnerStatus);
					boolean powerForHR_billPayingToM = powerForHR_billPayingToM(candiStatus, processInnerStatus);
					boolean powerForHR_billPayedToM = powerForHR_billPayedToM(candiStatus, processInnerStatus);
					boolean powerForHR_billPayedToHC = powerForHR_billPayedToHC(candiStatus, processInnerStatus);
					
					String[] btn11Class = {"gone","gone","gone","gone","gone","gone"}, btn11 = {"","","","","",""}, btn22Class = {"gone","gone","gone","gone"}, btn22 = {"","","",""};
					boolean hasButton = false;
					if(isHR() && queryMK==1){ //进行中
						status = processInnerStatus;
						
						int m = 0;
						m++;
						if(powerForHR_editIV && !powerForHR_confirmingIV){
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String time = this.getDBDate(o, "ivTime", "yyyy-MM-dd HH:mm");
							String showTime="";
							if(time.length() >0){
								Date date = format.parse(time);
						  		calendar.setTime(date);
								String newTime = LWUtil.getWeekOfDate(time);
								if(newTime.equals("月份")){
									 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
								}
								showTime = "面试：" + newTime + " " +time.split(" ")[1];
							}else{
								showTime = "";
							}
							hasButton = true; btn11Class[m] = "HR_showDetail_"; btn11[m] = showTime;btn22Class[m] = "HR_editIV_"; btn22[m] = "面试变更"; 
						}
						if(powerForHR_confirmingIV){hasButton = true; btn11Class[m] = "HR_showDetail_"; btn11[m] = "面试信息确定中"; }
						if(powerForHR_editOffer){hasButton = true; btn11Class[m] = "HR_showOffer_"; btn11[m] =  "Offer查看"; }
						m++;
						if(powerForHR_editOffer){hasButton = true; }
						if(powerForHR_addIV) {hasButton = true; btn11Class[m] = "HR_addIV_"; btn11[m] = "安排面试";}
						if(powerForHR_Offer) {hasButton = true; btn11Class[m] = "HR_addIV_"; btn11[m] = "安排面试"; btn22Class[m] = "HR_addOffer_"; btn22[m] = "上传Offer";}
						if(powerForHR_addOffer) {hasButton = true; btn11Class[m] = "HR_addOffer_"; btn11[m] = "上传Offer";}
						if(powerForHR_setIVResultPass) {hasButton = true; btn11Class[m] = "HR_setIVPass_"; btn11[m] = "面试通过"; }
						if(powerForHR_setEntryResult) {hasButton = true; btn11Class[m] = "HR_setEntry_"; btn11[m] = "确认入职";}
						m++;
						if(powerForHR_setIVResultPass) {hasButton = true;btn22Class[m] = "HR_setIVUnPass_"; btn22[m] = "面试未通过";}
						if(powerForHR_setEntryResult) {hasButton = true;btn22Class[m] = "HR_setUnEntry_"; btn22[m] = "未能入职";}
						if(powerForHR_editOffer) {hasButton = true;btn22Class[m] = "HR_editOffer_"; btn22[m] = "Offer变更";}
						if(powerForHR_entrying) { hasButton = true; btn22Class[m] = ""; btn22[m] = "入职待确认..."; }
						m++;
						m++;
						if(!hasButton){ btn11Class[0] = ""; btn11[0] = this.getDBString(o, "c7"); }
						
					}
					if(isHR() && queryMK==2){ //已收藏
						status = HIUtil.isEmpty(HIUtil.isEmpty(processCloseStatus, processInnerStatus), collectStatus);
						
						int m = 0;
						m++;
						m++;
						m++;
					    if(powerForHR_inviteIV){ btn11Class[m] = "HR_inviteIV_"; btn11[m] = "邀请面试"; }
						m++;

						System.out.println("[CandiService.activeStatus]======================================="+activeStatus+this.getDBDataParam(o, "c36","","",""));
					    if(!activeStatus.equals(ACE.CANDI_ActiveStatus_ACTIVE)){ btn11Class[m] = ""; btn11[m] = "已不看机会"; }
						m++;
						if(!hasButton){ btn11Class[0] = ""; btn11[0] = this.getDBString(o, "c7"); }
					}
					if(isHR() && queryMK==3){ //已结束
						status = processCloseStatus;
						
						int m = 0;
						if(powerForHR_dimissioning) { hasButton = true; btn22Class[m] = ""; btn22[m] = "离职待确认..."; }
						if(processDetail.length()>0){ hasButton = true; btn22Class[m] = "HR_showReason_"; btn22[m] =  "查看原因";}
						m++;
						m++;
						if(powerForHR_inviteIV){ hasButton = true; btn11Class[m] = "HR_inviteIV_"; btn11[m] = "邀请面试"; }
						m++;
						if(powerForHR_setDimission){ hasButton = true; btn22Class[m] = "HR_setDimission_"; btn22[m] = "候选人已离职？";}
						m++;
						if(powerForHR_billing){ hasButton = true; btn11Class[m] = ""; btn11[m] = "待开票"; }
						if(powerForHR_billPayingToM){ hasButton = true; btn11Class[m] = ""; btn11[m] = "待付款到平台"; }
						if(powerForHR_billPayedToHC) { hasButton = true; btn11Class[m] = ""; btn11[m] = "已付款"; }
						m++;
						if(powerForHR_billPayedToM) { hasButton = true; btn11Class[m] = "HR_billPayToHC_"; btn11[m] = "付款给顾问"; }

						if(!hasButton){  btn11Class[0] = ""; btn11[0] = this.getDBString(o, "c7"); }
					}
					s.append(String.format(html, new Object[]{
							"", //已结束未入职的候选人，加灰色
							this.getDBString(o, "cid"), //DATA
							this.getDBString(o, "pcid"), //DATA1
							status.substring(2,6), //status_4020.png
							ComBeanI_DataParamType.get(Integer.parseInt(status)).getMemo(), //已入职
							status.equals(ACE.CANDI_PROCESS_INNERSTATUS_INVITED) ? processTimer : "", //23:09
							(processCloseStatus.length()>0 && !processCloseStatus.equals(ACE.CANDI_PROCESS_CLOSESTATUS_ENTRY)) ? "" : "detail_",
							candiStatus.length() > 0 ? this.getDBString(o, "c8", "", "", "") : this.getDBDataParam(o, "c36","","",""), // 李大志
							this.getDBString(o, "c42"), // 品牌经理
							this.getDBDataParam(o, "c13", "", "", ""), // 男
							this.getDBString(o, "c11","","","岁"), //岁
							this.getDBString(o, "c39").equals("0") ? "" :this.getDBString(o, "c39","","","年工作经验"),
							this.getDBString(o, "yuexin_k","","","K/月"), // 25K
							this.getDBString(o, "c15"), // 上海
							"<span title=\"" + (IFACE_DBBUG_FLAG ? "<"+this.getDBString(o, "sortid")+">"+" ["+candiStatus+"-"+ComBeanI_DataParamType.getText(candiStatus)+"]["+processInnerStatus+"-"+ComBeanI_DataParamType.getText(processInnerStatus)+"]["+collectStatus+"-"+ComBeanI_DataParamType.getText(collectStatus)+"]" : "") + "\">" + education_str + "</span>", // 上海国际经贸大学
							job_str,                    // 公司 | 职位
							btn11Class[0], // 样式
							btn11[0], // 文本
							btn11Class[1], // 样式
							btn11[1], // 文本
							btn11Class[2], // 样式
							btn11[2], // 文本
							btn11Class[3], // 样式
							btn11[3], // 文本
							btn11Class[4], // 样式
							btn11[4], // 文本
							btn11Class[5], // 样式
							btn11[5], // 文本
							btn22Class[0], // 样式
							btn22[0], // 文本
							btn22Class[1], // 样式
							btn22[1], // 文本
							btn22Class[2], // 样式
							btn22[2], // 文本
							btn22Class[3], // 样式
							btn22[3], // 文本
							this.getDBString(o, "hunterID"), // 顾问
							this.getDBString(o, "hunterNickName"), // 顾问
							this.getDBString(o, "hunterFace"), // 顾问
							this.getDBString(o, "cid"), //DATA
							this.getDBString(o, "pcid"), //DATA1
					}));					
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
	private LWResult ServiceDO_CandiList_M(){
		System.out.println("[CandiService.ServiceDO_CandiList_M]=======================================");
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //简历待完善 1，简历更新待审核2，顾问面评待审核3，黑名单4，Active状态审核5，精选6
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
			if(queryMK==1){ //简历待完善
				String isComplete = this.getParameter("isComplete");
                list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", "", ACE.CANDI_STCheckStatus_ST_CHECKING, "", "", "", isComplete, "", keyword, FIRSTID, LASTID, ""));
			}else if(queryMK==2){ //简历更新待审核
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", "", "", "", "", "", "", ACE.CANDI_CVStatus_CHECKING, keyword, FIRSTID, LASTID, "更新时间"));
			}else if(queryMK==3){ //顾问面评待审核
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", "", ACE.CANDI_STCheckStatus_HCCOMMENT_CHECKING, "", "", "", "", "", keyword, FIRSTID, LASTID, ""));
			}else if(queryMK==4){ //黑名单
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", ACE.CANDI_STStatus_BLACKLIST, "", "", "", "", "", "", keyword, FIRSTID, LASTID, ""));
			}else if(queryMK==5){ //Active状态审核
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", "", "", ACE.CANDI_ActiveStatus_CHECKING_ACTIVE, "", "", "", "", keyword, FIRSTID, LASTID, ""));
			}else if(queryMK==6){ //精选
				String[] submk = this.getParameter("submk").split("-");
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", ACE.CANDI_STStatus_ST, "", submk[0], "", submk.length==1?"":submk[1], "", "", keyword, FIRSTID, LASTID, ""));
			}else if(queryMK==999 && auid.startsWith("20161101000000999000000091")){ //全部
				String submk = this.getParameter("submk");
			    list = dbQuery(YSSQL.LWCandidate_QueryByC("", "", "", submk, "", "", "", "", "", "", keyword, FIRSTID, LASTID, ""));
			}
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
					
					String button = "";
					if(queryMK==4){ //黑名单
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "cid"), //DATA
								this.getDBString(o, "c2"), //DATA1
								this.getDBString(o, "c13", "13200 ").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c8"), // 李大志
								this.getDBString(o, "c41") + " | " + this.getDBString(o, "c42"),
								this.getDBString(o, "c65").replaceAll("0", "").length()*10, // 20%
								"拉黑于"+this.getDBDate(o, "c81", DATETIME_FORMAT),
						}));
					} else if(queryMK==6){ //精选
						String isGood = this.getDBString(o, "c91");
						String goodDate = this.getDBDate(o, "c92", DATE_FORMAT);
						button = "<button class=\"btn-default pd5 M_detail_\">查看详情</button>";
						if(isGood.indexOf("1")>=0){
							button += "<button class=\"btn-default pd5 mgl10 M_removeGood_\">移出精选</button>";
						}else{
							button += "<button class=\"btn-default pd5 mgl10 M_toGood_\">加精</button>";
						}
						if(isGood.indexOf("2")>=0){
							button += "<button class=\"btn-default pd5 mgl10 M_removeGoodBak_\">移出备选</button>";
						}else{
							button += "<button class=\"btn-default pd5 mgl10 M_toGoodBak_\">预备</button>";
						}
						if(isGood.indexOf("0")>=0 || isGood.indexOf("6")>=0){
						}
						button += " " + getDBString(o, "c28","","浏览"," ") + getDBString(o, "c27","","收藏"," ") + getDBString(o, "c29","","邀请","");
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "cid"), //DATA
								this.getDBString(o, "c2"), //DATA1
								this.getDBString(o, "c13", "13200 ").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c8"), // 李大志
								this.getDBString(o, "c41") + " | " + this.getDBString(o, "c42"),
								button,
								"gone",
								"", // 20%
								isGood.indexOf("1")>=0 ? "精选于"+goodDate : (isGood.indexOf("6")>=0 ? "曾精选于"+goodDate : ""),
								"",
						}));
					} else if(queryMK==999){ //全部候选人
						String stStatus = this.getDBString(o, "stStatus");

						button = "<button class=\"btn-default pd5 M_detail_\">查看详情</button>";
						button += "<span class=\"pointer pd5 mgl5 M_cvUpdate_\">更新</span>";
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "cid"), //DATA
								this.getDBString(o, "c2"), //DATA1
								this.getDBString(o, "c13", "13200 ").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c8")+ "<span class=\"pdl20 light-grey\">" +this.getDBString(o, "c9")+"</span>", // 李大志
								this.getDBString(o, "c41") + " | " + this.getDBString(o, "c42"),
								"<span class='pdr20 f9 grey'><"+this.getDBDataParam(o, "stCheckStatus", this.getDBDataParam(o, "activeStatus", "", "", ""), "", "")+"></span>"+ button,
								"gone",
								"", // 20%
								(HIUtil.isEmpty(this.getDBString(o, "c6")) ? "新建于"+this.getDBDate(o, "istdate", DATETIME_FORMAT) : "申请于"+this.getDBDate(o, "c6", DATETIME_FORMAT)),
								"<span class='dark-grey pdr10' title='"+ComBeanLoginUser.getUserPhone(getDBString(o, "c2"))+"'>"+ComBeanLoginUser.getUserName(getDBString(o, "c2"))+" | "+(HIUtil.isEmpty(getDBString(o, "c67"))?"Freelancer":ComBeanMMOrgan.getFullNameByAmid(getDBString(o, "c67")))+"</span>", //顾问
						}));
					}else{
						//int c65 = 8 - this.getDBString(o, "c65").replaceAll("0", "").length();
						String date = "";
						double completeTime = 0;
						String color = "bg-green";
						String remainTime = "";
						
						if(queryMK==1){
							date = this.getDBDate(o, "c81", DATETIME_FORMAT);
							completeTime = this.getDBDouble(o, "completeRemainTime");
						}else if(queryMK==2){
							date = this.getDBDate(o, "c83", DATETIME_FORMAT);
							completeTime = this.getDBDouble(o, "cvUpdateRemainTime");
						}else if(queryMK==3){
							date = this.getDBDate(o, "c86", DATETIME_FORMAT);
							completeTime = this.getDBDouble(o, "hccommentRemainTime");
						}else if(queryMK==5){
							date = this.getDBDate(o, "c6", DATETIME_FORMAT);
							completeTime = this.getDBDouble(o, "activeRemainTime");
						}
						
						System.out.println("completeRemainTime==========="+completeTime);
						if(completeTime >= 0){
							remainTime = "剩余"+HIUtil.NumFormat(completeTime, 0)+"小时";
							completeTime = 24 - completeTime;
						}else{
							if(completeTime>=-48) remainTime = "超过"+HIUtil.NumFormat(-1*completeTime, 0)+"小时";
							else remainTime = "超过"+HIUtil.NumFormat(-1*completeTime/24, 0)+"天";
							completeTime = 24;
						}
						if(completeTime>18) color = "bg-red";
						else if(completeTime>12) color = "bg-focus";
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "cid"), //DATA
								this.getDBString(o, "c2"), //DATA1
								this.getDBString(o, "c13", "13200 ").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c8")+ "<span class=\"pdl20 light-grey\">" +this.getDBString(o, "c9")+"</span>", // 李大志
								this.getDBString(o, "c41") + " | " + this.getDBString(o, "c42"),
								"",
								"", //gone
								"<span title=\""+remainTime+"\" class=\"inline-block h15 "+color+"\" style=\"width:120px\"><span class=\"border inline-block h15 bg-grey\" style=\"width:"+Math.min(120, completeTime*5)+"px\"></span></span>", // 80px  总长120 目前是6项
								"申请于"+date,
								"<span class='dark-grey pdr10' title='"+ComBeanLoginUser.getUserPhone(getDBString(o, "c2"))+"'>"+ComBeanLoginUser.getUserName(getDBString(o, "c2"))+" | "+(HIUtil.isEmpty(getDBString(o, "c67"))?"Freelancer":ComBeanMMOrgan.getFullNameByAmid(getDBString(o, "c67")))+"</span>", //顾问
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
	// 候选人状态列表
	private LWResult ServiceDO_CandiList_StatusList(){
		System.out.println("[CandiListService.ServiceDO_CandiList_StatusList]=======================================");
		String memo = "候选人状态列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			if(isHC()) list = dbQuery(YSSQL.LWCandidate_Status_QueryByC(auid, ""));
			if(isM()) list = dbQuery(YSSQL.LWCandidate_Status_QueryByC("", ""));
			
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(String.format(HTML, new Object[]{
							"",
							"所有",
							"",
							
							this.getDBString(o, "data1"),
                            "未入库",//this.getDBDataParam(o, "data1", "", "", ""),							
							getDBInt(o, "sum1") > 0 ? this.getDBString(o, "sum1") : "",
							
							this.getDBString(o, "data2"),
							"入库中",//this.getDBDataParam(o, "data2", "", "", ""),							
							getDBInt(o, "sum2") > 0 ? this.getDBString(o, "sum2") : "",
							
							this.getDBString(o, "data3"),
							"已入库",//this.getDBDataParam(o, "data3", "", "", ""),							
							getDBInt(o, "sum3") > 0 ? this.getDBString(o, "sum3") : "",
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
    // 候选人状态列表
	private LWResult ServiceDO_CandiList_StatusList_HR(){
		System.out.println("[CandiListService.CandiList_StatusList]=======================================");
		String memo = "候选人状态列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //候选人0 ，进行中 1 ，已收藏2，已结束3
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			if(queryMK==1) //进行中
				list = dbQuery(YSSQL.LWCandidate_ProcessStatus_QueryByC("", "", "", auid, myamid, ACE.CANDI_STATUS_ING, "", ""));
			else if(queryMK==2) //已收藏
			    list = dbQuery(YSSQL.LWCandidate_ProcessStatus_QueryByC("", "", "", auid, myamid, "", "",ACE.COLLECT_STATUS_COLLECTED));
			else if(queryMK==3) //已结束
			    list = dbQuery(YSSQL.LWCandidate_ProcessStatus_QueryByC("", "", "", auid, myamid, "", ACE.CANDI_STATUS_OVER,""));
			
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(String.format(HTML, new Object[]{
							"",
							"所有进行中",
							"",
							
							this.getDBString(o, "data1"),
                            "邀请待反馈",//this.getDBDataParam(o, "data1", "", "", ""),							
							getDBInt(o, "sum1") > 0 ? this.getDBString(o, "sum1") : "",
							
							this.getDBString(o, "data2"),
							"面试",//this.getDBDataParam(o, "data2", "", "", ""),							
							getDBInt(o, "sum2") > 0 ? this.getDBString(o, "sum2") : "",
							
							this.getDBString(o, "data3"),
							"Offer",//this.getDBDataParam(o, "data3", "", "", ""),							
							getDBInt(o, "sum3") > 0 ? this.getDBString(o, "sum3") : "",

							this.getDBString(o, "data4"),
							"待入职",//this.getDBDataParam(o, "data4", "", "", ""),							
							getDBInt(o, "sum4") > 0 ? this.getDBString(o, "sum4") : "",
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

	// 候选人精选状态列表
	private LWResult ServiceDO_CandiList_Good_StatusList_M(){
		System.out.println("[CandiListService.ServiceDO_CandiList_Good_StatusList_M]=======================================");
		String memo = "候选人精选状态列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			list = dbQuery(YSSQL.LWCandidateGood_Status_QueryByC());
			
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "data1"),
                            "本周精选",						
                            getDBInt(o, "sum1", "", "(", ")"),
							
							this.getDBString(o, "data2"),
							"下周预备",					
							getDBInt(o, "sum2", "", "(", ")"),
							
							this.getDBString(o, "data3"),
							"非精选Active",							
							getDBInt(o, "sum3", "", "(", ")"),
									
							this.getDBString(o, "data9"),
							"所有Active",							
							getDBInt(o, "sum9", "", "(", ")"),
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
	// 候选人状态列表
	private LWResult ServiceDO_CandiList_StatusList_M(){
		System.out.println("[CandiListService.ServiceDO_CandiList_StatusList_M]=======================================");
		String memo = "候选人状态列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			list = dbQuery(YSSQL.LWCandidateGood_Status_QueryByC());
			
			if(list!=null && list.size()>0){
				for(Object o : list){
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "data1"),
                            "本周精选",						
                            getDBInt(o, "sum1", "", "(", ")"),
							
							this.getDBString(o, "data2"),
							"下周预备",					
							getDBInt(o, "sum2", "", "(", ")"),
							
							this.getDBString(o, "data3"),
							"非精选Active",							
							getDBInt(o, "sum3", "", "(", ")"),
									
							this.getDBString(o, "data9"),
							"所有Active",							
							getDBInt(o, "sum9", "", "(", ")"),
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
	public static String getFirstDayOfWeek(String date) throws java.text.ParseException{        
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(sdf.parse(date));
		// Calendar默认周日为第一天, 所以设置为1
		cal.set(Calendar.DAY_OF_WEEK, 1);
		// 如果要返回00点0分0秒
		//cal.set(Calendar.HOUR_OF_DAY, 0);
		//cal.set(Calendar.MINUTE, 0);
		//cal.set(Calendar.SECOND, 0);
		//cal.set(Calendar.MILLISECOND, 0); 
	    return sdf.format(cal.getTime());
	}	
	// 候选人面试更多记录查询
	private LWResult ServiceDO_CandiList_interviewMoreList_HR(){
		System.out.println("[ServiceDO_CandiList_taskList_HR]=======================================");
		String memo = "候选人更多面试记录";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String html = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			    list = dbQuery(YSSQL.LWCandidate_Interview_QueryByC(auid,"0"));

			    if(list!=null && list.size()>0){	
					for(Object o : list){
						if(n<=3){
						}else{
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String time = this.getDBDate(o, "time", "yyyy-MM-dd HH:mm");
					  		Date date = format.parse(time);
					  		calendar.setTime(date);
							String newTime = LWUtil.getWeekOfDate(time);
							if(newTime.equals("月份")){
								 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
								}
	
							s.append(String.format(html, new Object[]{
									newTime +" ("+ (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日) "+time.split(" ")[1],
									this.getDBString(o, "name"),
									this.getDBString(o, "postion")
	
							}));
						}
						n++;
					}
				}
			    if(n>0) return new LWResult(LWResult.SUCCESS,s.toString());
				return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
		
	}
	// 候选人面试记录
	private LWResult ServiceDO_CandiList_interviewList_HR(){
		System.out.println("[ServiceDO_CandiList_interviewList_HR]=======================================");
		String memo = "候选人面试记录";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list = null;
			int n = 0;
			    list = dbQuery(YSSQL.LWCandidate_Interview_QueryByC(auid,"0"));	
			    Object[] oo = new Object[]{//
						"gone",
						"",
						"",
						"",
						"",
						"",
						"",
						"gone",
						"",
						"",
						"",
						"",
						"",
						"",
						"gone",
						"",
						"",
						"",
						"",
						"",
						"",
						"gone",
						"",
						"",
						"",
						"",
						"",
						"",
						"gone",
				};
			    if(list!=null && list.size()>0){	
					for(Object o : list){
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						String time = this.getDBDate(o, "time", "yyyy-MM-dd HH:mm");
				  		Date date = format.parse(time);
				  		calendar.setTime(date);
						String newTime = LWUtil.getWeekOfDate(time);
						if(newTime.equals("月份")){
						 newTime = (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
						}
						if(n==0){
							if(newTime.equals("今天")){
								oo[0] = "first";
							}else{
								oo[0] = "";
							}
							oo[1] = newTime +" ("+ (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日) "+time.split(" ")[1];
							oo[2] = this.getDBString(o, "name");
							oo[3] = this.getDBString(o, "postion");
							oo[4] = this.getDBString(o, "address");
							oo[5] = newTime;
							oo[6] = time.split(" ")[1];
						}else if(n==1){
							oo[7] = "";
							oo[8] = newTime +" ("+ (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日) "+time.split(" ")[1];
							oo[9] = this.getDBString(o, "name");
							oo[10] = this.getDBString(o, "postion");
							oo[11] = this.getDBString(o, "address");
							oo[12] = newTime;
							oo[13] = time.split(" ")[1];
						}else if(n==2){
							oo[14] = "";
							oo[15] = newTime +" ("+ (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日) "+time.split(" ")[1];
							oo[16] = this.getDBString(o, "name");
							oo[17] = this.getDBString(o, "postion");
							oo[18] = this.getDBString(o, "address");
							oo[19] = newTime;
							oo[20] = time.split(" ")[1];							
						}else if(n==3){
							oo[21] = "";
							oo[22] = newTime +" ("+ (calendar.get(Calendar.MONTH) + 1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日) "+time.split(" ")[1];
							oo[23] = this.getDBString(o, "name");
							oo[24] = this.getDBString(o, "postion");
							oo[25] = this.getDBString(o, "address");
							oo[26] = newTime;
							oo[27] = time.split(" ")[1];					
						}else{
							oo[28] = "";
						}
						n++;
					}
					s.append(String.format(HTML,oo));
			}

			if(list.size()>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, memo+"异常");
	}
	private LWResult ServiceDO_CandiList_better_StatusList_M() {
		String memo = "候选人特别推荐状态列表";
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<Object> list1 = null;
			int n = 0;
			list1 = dbQuery(YSSQL.LWCandidate_SpecialCommand_thisWeekHRAmount);
			int thisWeek = 0;
			int nextWeek = 0;
			if(list1!=null && list1.size()>0){
				for(Object o : list1){
					thisWeek = this.getDBInt(o, "amounts");
					n++;
				}
			}
			List<Object> list2 = dbQuery(YSSQL.LWCandidate_SpecialCommand_nextWeekHRAmount);
			if (list2!=null&&list2.size()>0) {
				for (Object o : list2) {
					nextWeek = this.getDBInt(o, "amounts");
				}
			}
			s.append(String.format(HTML, new Object[]{
					'2',
                    "本周特别推荐",						
                    "("+thisWeek+")",
					
					'3',
					"下周预备",					
					"("+nextWeek+")"
			}));
			if(n>0) return new LWResult(LWResult.SUCCESS, s.toString());
			return new LWResult(LWResult.FAILURE, memo+"无记录");
		}catch(Exception e){
			e.printStackTrace();
			return new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	
	private LWResult ServiceDO_CandiList_Better_HRList_M() {
		LWResult result = null;
		String memo = "特别推荐候选人HR列表";
		try {
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String queryMK = this.getParameter("queryMK");
			StringBuilder s = new StringBuilder();
			int n = 0;
			List<Object> lists = null;
			switch(queryMK){
				case "2":
					lists = dbQuery(YSSQL.LWCandidate_Command_HRList);
					break;
				case "3":
					lists = dbQuery(YSSQL.LWCandidate_Command_HRListNextWeek);
					break;
				case "4":
					lists = dbQuery(YSSQL.LWCandidate_SpecialCommand_queryHR);
			}
			if(lists!=null&&lists.size()>0){
				for (Object o : lists) {
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "HRAuid"),//hrauid
							this.getDBString(o, "sum"),//关联候选人个数
							this.getDBString(o, "logo").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "logo"),//公司Logo
							this.getDBString(o, "short_Name").isEmpty()?this.getDBString(o, "CompanyNmae"):this.getDBString(o, "short_Name"),//公司名称
							this.getDBString(o, "HRName"),//HR名称
							this.getDBString(o, "email")//邮箱
					}));
					n++;
				}
			}
			if(n>0) return result = new LWResult(LWResult.SUCCESS, s.toString());
			return result = new LWResult(LWResult.FAILURE, memo+"无记录");
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	private LWResult ServiceDO_CandiList_Better_CandidateList_M() {
		LWResult result = null;
		try {
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String hrauid = this.getParameter("HRid");
			String queryMK = this.getParameter("queryMK");
			StringBuilder s = new StringBuilder();
			
			int n = 0;
			List<Object> lists = null;
			if(queryMK.equals("2")){
				lists = dbQuery(YSSQL.LWCandidate_Command_CandidateList(hrauid));
			}
			if(queryMK.equals("3")){
				lists = dbQuery(YSSQL.LWCandidate_Command_CandidateListNextWeek(hrauid));
			}
			 
			if(lists!=null&&lists.size()>0){
				for (Object o : lists) {
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "auid"),
							this.getDBString(o, "photo").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "photo"),//照片
							this.getDBString(o, "engName").isEmpty()?this.getDBString(o, "chiName"):this.getDBString(o, "chiName")+"("+this.getDBString(o, "engName")+")",
							this.getDBString(o, "recentCompany"),
							this.getDBString(o, "recentJob")
					}));
					n++;
				}
			}
			if(n>0) return result = new LWResult(LWResult.SUCCESS, s.toString());
			else return result = new LWResult(LWResult.FAILURE, "无记录");
		} catch (Exception e) {
			e.printStackTrace();
			return result =  new LWResult(LWResult.FAILURE, "异常");
		}
	}
	private LWResult ServiceDO_CandiList_Better_Remove_M() {
		LWResult result = null;
		String memo = "候选人移出特别推荐";
		try {
			String CandAuid = this.getParameter("CandidateAuid");
			String hrAuid = this.getParameter("hrAuid");
			String queryMK = this.getParameter("queryMK");
			List<String> sqls = new ArrayList<String>();
			switch(queryMK){
				case "2":
					sqls.add(String.format(YSSQL.LWCandidate_Command_removeCandidate, new Object[]{CandAuid,hrAuid,'1'}));
					break;
				case "3":
					sqls.add(String.format(YSSQL.LWCandidate_Command_removeCandidate, new Object[]{CandAuid,hrAuid,'2'}));
					break;
			}
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.SUCCESS,memo+"异常");
		}
	}
	private LWResult ServiceDO_HRList_Better_Delete_M() {
		LWResult result = null;
		String memo = "删除特别推荐下周预备HR";
		try {
			String HRAuid = this.getParameter("HRAuid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWCandidate_Command_deleteHR_NextWeek, new Object[]{HRAuid}));
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.SUCCESS,memo+"异常");
		}
	}
	private LWResult ServiceDO_addHR_SpecialCommand_M() {
		LWResult result = null;
		String memo = "添加HR到特别推荐";
		try {
			String hrauid = this.getParameter("hrid");
			List<Object> lists = dbQuery(String.format(YSSQL.LWCandidate_SpecialCommand_existHR, new Object[]{hrauid}));
			List<String> sqls = new ArrayList<String>();
			if(lists!=null&&lists.size()>0){
				for (Object o : lists) {
					if(this.getDBString(o, "status").equals("0") || this.getDBString(o, "status").equals("2")){
						return result = new LWResult(LWResult.SUCCESS, memo+"已存在");
					}else{
						sqls.add(String.format(YSSQL.LWCandidate_SpecialCommand_updateHR, new Object[]{hrauid}));
					}
				}
			}else{
				sqls.add(String.format(YSSQL.LWCandidate_SpecialCommand_addHR, new Object[]{hrauid}));
			}
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.SUCCESS, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result =  new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	private LWResult ServiceDO_deleteHR_SpecialCommand_M() {
		LWResult result = null;
		String memo = "删除新增特别推荐HR";
		try {
			String hrAuid =  this.getParameter("HRAuid");
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWCandidate_SpecialCommand_deleteHR, new Object[]{hrAuid}));
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result =  new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	private LWResult ServiceDO_addCandidateToHR_SpecialCommand_M() {
		LWResult result = null;
		String memo = "新增候选人特别推荐";
		try {
			String candId = this.getParameter("candId");
			String hrAuid = this.getParameter("hrauid");
			ArrayList<String> sqls = new ArrayList<String>();
			List<Object> records = dbQuery(String.format(YSSQL.LWCandidate_SpecialCommand_checkExitsRecord, new Object[]{candId,hrAuid}));
			if(records!=null&&records.size()>0){
			     return result = new LWResult(LWResult.FAILURE, memo+"记录已存在");
			}
			sqls.add(String.format(YSSQL.LWCandidate_SpecialCommand_insertHRCandi, new Object[]{candId,hrAuid}));
			
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	private LWResult ServiceDO_removeCandidateFromHR_SpecialCommand_M() {
		LWResult result = null;
		String memo = "移除下周预备候选人";
		try {
			String candID = this.getParameter("CandidateAuid");
			String hrAuid = this.getParameter("id");
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWCandidate_SpecialCommand_removeCandidateFromHR, new Object[]{candID,hrAuid}));
			
			if(dbExe(sqls)){
				return result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				return result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
}
