package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;

import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class BillService extends CandiBase {

	public String BillCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String BillBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String BillSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String BillMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
    //开票记录
	public String listBill(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_BillList();
		if(isHC()) result = ServiceDO_BillList();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览开票记录列表");
		return APPRETURN(result);
	}
    //待开票记录
	public String listBilling(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_BillingList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览待开票候选人列表");
		return APPRETURN(result);
	}
	//待开票候选人信息
	public String candiInfoForBill(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_candiInfoForBill_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览待待开票候选人信息");
		return APPRETURN(result);
	}
	//开票
	public String bill(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_bill_M();
		return APPRETURN(result);
	}
	//待付款候选人信息
	public String candiInfoForPay(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_candiInfoForPay_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览待付款候选人信息");
		return APPRETURN(result);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 2057485622604438904L;

	
	private LWResult ServiceDO_BillList(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String status = ACE.CANDI_BILL_STATUS_BILLED+","+ACE.CANDI_BILL_STATUS_PayingToM+","+ACE.CANDI_BILL_STATUS_PayingToM_OVERTIME+","+ACE.CANDI_BILL_STATUS_PayedToM+","+ACE.CANDI_BILL_STATUS_PayingToHC+","+ACE.CANDI_BILL_STATUS_PayingToHC_OVERTIME+","+ACE.CANDI_BILL_STATUS_PayedToHC;
			List<Object> list = null;
			if(isHR()) list = dbQuery(YSSQL.LWBill_QueryByC("", myamid, auid, "", "", "", status, "", FIRSTID, LASTID,""));
			if(isHC()) list = dbQuery(YSSQL.LWBill_QueryByC("", "", "", myamid, auid, "", status, "", FIRSTID, LASTID,""));
			
			int n = 0;
			String rtnString = null;
			if(list!=null && list.size()>0){
				for(Object o : list){
					if(n == maxcount) break;
					s.append(String.format(htmls.get(1), new Object[]{
	                     this.getDBDate(o, "istdate", "yyyy-MM-dd"),
	                     this.getDBString(o, "c8"), //职位
	                     this.getDBString(o, "c7"), //候选人
	                     "￥"+HIUtil.NumFormat(getDBString(o, "c9"), 2), //招聘服务费
	                     this.getDBDataParam(o, "c10", "", "", ""), //费用状态
	                     isHC()? (this.getDBString(o, "c11") + " | " + this.getDBString(o, "c12")) : this.getDBString(o, "c13").split("1")[0], //公司|HR、顾问
					}));
					n++;
				}
				rtnString = String.format(htmls.get(0), s.toString());
			}
			
			if(n>0) return new LWResult(LWResult.SUCCESS, rtnString);
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LWResult(LWResult.FAILURE, "异常");
	}
	
	//开票记录
	private LWResult ServiceDO_BillingList_M(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1);
			String c_keyword = this.getParameter("keyword");
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			int maxcount = this.getParameter("i9", 20);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String rtnString = null;
			String innerStatus = "";
			String processCloseStatus = ACE.CANDI_PROCESS_CLOSESTATUS_ENTRY; //已入职
			String status = null;
			if(queryMK==1) status = ACE.CANDI_BILL_STATUS_BILLING; //待开票
			if(queryMK==2) status = ACE.CANDI_BILL_STATUS_BILLED+","+ACE.CANDI_BILL_STATUS_PayingToM+","+ACE.CANDI_BILL_STATUS_PayingToM_OVERTIME+","+ACE.CANDI_BILL_STATUS_PayedToM+","+ACE.CANDI_BILL_STATUS_PayingToHC+","+ACE.CANDI_BILL_STATUS_PayingToHC_OVERTIME+","+ACE.CANDI_BILL_STATUS_PayedToHC; //待付款+待付款到顾问
			if(queryMK==3){
				status = ""; //保障期内离职
				processCloseStatus = ACE.CANDI_PROCESS_CLOSESTATUS_DIMISSION; //离职待确认
				innerStatus = ACE.CANDI_PROCESS_INNERSTATUS_DIMISSION_CHECKING;
			} 
			List<Object> list = dbQuery(YSSQL.LWBill_QueryByC("", "", "", "", "", processCloseStatus, status, c_keyword, FIRSTID, LASTID,innerStatus));
			
			int n = 0;
			if(list!=null && list.size()>0){
				String billStatus = null, billStatusStr = null, remainTime = null;
				boolean isRed = false;
				for(Object o : list){
					if(n == maxcount) break;
					if(queryMK==1){
						s.append(String.format(htmls.get(1), new Object[]{
		                     this.getDBString(o, "c2"),
		                     this.getDBString(o, "c11") + " | " + this.getDBString(o, "c12"), //公司|HR
		                     this.getDBString(o, "c8"), //职位
		                     this.getDBString(o, "c7"), //候选人
		                     this.getDBString(o, "c13"), //顾问
		                     this.getDBDate(o, "istdate", "yyyy-MM-dd"),
						}));
					}
					if(queryMK==2){ //付款
						isRed = false;
						billStatus = this.getDBString(o, "c10");
						billStatusStr = this.getDBDataParam(o, "c10", "", "", "");
						
						switch(billStatus){
						case ACE.CANDI_BILL_STATUS_BILLING: //待开票 - M
							remainTime = this.getDBString(o, "rtime_bill");
							break;
						case ACE.CANDI_BILL_STATUS_BILLED: //已开票 - HR - M
							remainTime = this.getDBString(o, "rtime_pay2M");
							if(Double.parseDouble(remainTime) <= 0){
								isRed = true;
								billStatusStr = "付款超时";
							}else{
								billStatusStr = "未付款";
							}
							break;
						case ACE.CANDI_BILL_STATUS_PayingToM: //待付款到平台 - HR - M (同上)
							remainTime = this.getDBString(o, "rtime_pay2M");
							if(Double.parseDouble(remainTime) <= 0){
								isRed = true;
								billStatusStr = "付款超时";
							}else{
								billStatusStr = "未付款";
							}
							break;
						case ACE.CANDI_BILL_STATUS_PayingToM_OVERTIME: //付款到平台超时
							isRed = true;
							remainTime = this.getDBString(o, "rtime_pay2M");
							break;
						case ACE.CANDI_BILL_STATUS_PayedToM: //已付款到平台
							remainTime = this.getDBString(o, "rtime_hr2hc");
							break;
						case ACE.CANDI_BILL_STATUS_PayingToHC: //待付款到顾问
							remainTime = this.getDBString(o, "rtime_pay2hc");
							if(Double.parseDouble(remainTime) <= 0){
								isRed = true;
							}
							break;
						case ACE.CANDI_BILL_STATUS_PayedToHC: //已付款到顾问
							remainTime = "";
							break;
						case ACE.CANDI_BILL_STATUS_PayingToHC_OVERTIME: //付款到顾问超时
							isRed = true;
							remainTime = this.getDBString(o, "rtime_pay2hc");
							break;
						}
						
						if(remainTime.length()==0) remainTime = "-";
						else remainTime = Math.floor(Double.parseDouble(remainTime)) + "天";
						if(isRed){
							billStatusStr = String.format("<span class=\"red\">%s</span>", billStatusStr);
							remainTime = String.format("<span class=\"red\">%s</span>", remainTime);
						}
						
						s.append(String.format(htmls.get(1), new Object[]{
		                     this.getDBString(o, "c2"),
		                     this.getDBString(o, "c11") + " | " + this.getDBString(o, "c12"), //公司|HR
		                     this.getDBString(o, "c8"), //职位
		                     this.getDBString(o, "c7"), //候选人
		                     this.getDBString(o, "c13"), //顾问
		                     this.getDBString(o, "c9") + "元", //费用
		                     billStatusStr, //费用状态
		                     remainTime, //剩余时长
						}));
					}
					if(queryMK==3){
						s.append(String.format(htmls.get(1), new Object[]{
		                     this.getDBString(o, "c2"),//pcid
		                     this.getDBString(o, "c11") + " | " + this.getDBString(o, "c12"), //公司|HR
		                     this.getDBString(o, "c8"), //职位
		                     this.getDBString(o, "c7"), //候选人
		                     this.getDBString(o, "c13"), //顾问
		                     this.getDBString(o, "c9") + "元", //费用
		                     this.getDBDataParam(o, "c10", "", "", ""), //费用状态
						}));
					}
					n++;
				}
				rtnString = String.format(htmls.get(0), s.toString());
			}
			if(n>0) return new LWResult(LWResult.SUCCESS, rtnString);
			return new LWResult(LWResult.FAILURE, "无记录");
		}catch(Exception e){
			e.printStackTrace();
			return new LWResult(LWResult.FAILURE, "异常");
		}
	}
	
	private LWResult ServiceDO_candiInfoForPay_M() {
		LWResult result = null;
		String memo = "查询候选人付款信息";
		try {
			StringBuilder s = new StringBuilder("");
			String pcid = this.getParameter("id");
			List<Object> list = dbQuery(YSSQL.LWBill_QueryByC(pcid, "", "", "", "", "", "", "", "", "",""));
			
			if(list!=null && list.size()>0){
				String billStatus = null, btn = "", btnClass = "gone";
				for(Object o: list){
					billStatus = this.getDBString(o, "c10");
					switch(billStatus){
					case ACE.CANDI_BILL_STATUS_BILLING: //待开票 - M
						break;
					case ACE.CANDI_BILL_STATUS_BILLED: //已开票 - HR - M
						btnClass = "M_Pay2M_";
						btn = "付款到平台";
						break;
					case ACE.CANDI_BILL_STATUS_PayingToM: //待付款到平台 - HR - M (同上)
						btnClass = "M_Pay2M_";
						btn = "付款到平台";
						break;
					case ACE.CANDI_BILL_STATUS_PayingToM_OVERTIME: //付款到平台超时
						btnClass = "M_Pay2M_";
						btn = "付款到平台";
						break;
					case ACE.CANDI_BILL_STATUS_PayedToM: //已付款到平台
						btnClass = "M_Pay2HC_";
						btn = "付款到顾问";
						break;
					case ACE.CANDI_BILL_STATUS_PayingToHC: //待付款到顾问
						btnClass = "M_Pay2HC_";
						btn = "付款到顾问";
						break;
					case ACE.CANDI_BILL_STATUS_PayedToHC: //已付款到顾问
						break;
					case ACE.CANDI_BILL_STATUS_PayingToHC_OVERTIME: //付款到顾问超时
						btnClass = "M_Pay2HC_";
						btn = "付款到顾问";
						break;
					}
					
					s.append(this.getDBString(o, "c1"))//cid
					 .append(Constants.SPLITER).append(this.getDBString(o, "c7")) //候选人姓名
					 .append(Constants.SPLITER).append(this.getDBString(o, "c13")) //顾问
					 .append(Constants.SPLITER).append(this.getDBString(o, "c11")) //公司
					 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) //职位
					 .append(Constants.SPLITER).append(this.getDBString(o, "c12"))//HR
					 .append(Constants.SPLITER).append(this.getDBString(o, "c9"))//招聘服务费
					 .append(Constants.SPLITER).append(this.getDBDataParam(o, "c10", "", "", ""))//票据状态
					 .append(Constants.SPLITER).append(btnClass)
					 .append(Constants.SPLITER).append(btn)
					 ;
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
	
	// 返回待开票候选人信息
	private LWResult ServiceDO_candiInfoForBill_M(){
		LWResult result = null;
		String memo = "查询待开票候选人信息";
		try{
			StringBuilder s = new StringBuilder("");
			String pcid = this.getParameter("id");
		    List<Object> list = dbQuery(YSSQL.LWBill_QueryByC(pcid, "", "", "", "", "", ACE.CANDI_BILL_STATUS_BILLING, "", "", "",""));
		    List<Long> ids = new ArrayList<Long>();
			if(list!=null && list.size()>0){
				Long id;
				for(Object o: list){
					id = this.getDBLong(o, "sortid");
					if(ids.contains(id)) continue;
					ids.add(id);
					s.append(this.getDBString(o, "c1"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c7")) //候选人姓名
					 .append(Constants.SPLITER).append(this.getDBString(o, "c13")) //顾问
					 .append(Constants.SPLITER).append(this.getDBString(o, "c11")) //公司
					 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) //职位
					 .append(Constants.SPLITER).append(this.getDBString(o, "c12")); //HR
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

	//开票
	private LWResult ServiceDO_bill_M(){
		LWResult result = null;
		String memo = "开票";
		try{
			String pcid = this.getParameter("id");
			String[] vv = F_formParam();
			int i = 0;
			String billAmount = vv[i++]; //开票金额
			String billDate = vv[i++]; //开票日期
			
			List<Object> list = this.dbQuery(String.format("select * from B_Bill where c2=%s and c10=%s", new Object[]{pcid, ACE.CANDI_BILL_STATUS_BILLING}));
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				String cid = this.getDBString(o, "c1");

				List<String> sqls = new ArrayList<String>();
				sqls.add(String.format(YSSQL.LWBill_setBill, new Object[]{
						billAmount,//招聘服务费 c9
						ACE.CANDI_BILL_STATUS_BILLED,//票据状态 c10
						"AC00001", //开票单号 c14
						auid, //开票人AUID c16
						ComBeanLoginUser.getUserName(auid), //开票人姓名 c17
						billDate, //开票单号 c18
						pcid,//id
				}));
				result = this.P_doProcess(sqls, ACE.PROCESS_CODE_BILL, cid, pcid, "", "");
				if(result.isSuccess()){
				    result = new LWResult(LWResult.SUCCESS, memo+"成功");
				}				
			}

	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}

	
}
