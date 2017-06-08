package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;
import com.svv.dms.web.util.PoiUtil;
import com.svv.dms.web.util.TColumn;

public class StatService  extends LWBaseBean {

	public String StatMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}

	//候选人被邀请面试记录
	public String exportInvite(){
		String result = null;
		if(isM()) result = ServiceDO_exportInvite_M();
		return result;
	}

	//收藏记录统计导出
	public String exportStore(){
		String result = null;
		if(isM()) result = ServiceDO_exportStore_M();
		return result;
	}

	//浏览记录导出
	public String exportBrowse(){
		String result = null;
		if(isM()) result = ServiceDO_exportBrowse_M();
		return result;
	}

	//HR数据统计
	public String HRDataCount(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_HRDataCount_M();
		return APPRETURN(result);
	}

	//统计HC
	public String statHC(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_statHCList_M();
		return APPRETURN(result);
	}
	
	public String statCompany(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_statHRList_M();
		return APPRETURN(result);
	}
	
	public String statCandidate(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_statCandidate_M();
		return APPRETURN(result);
	}

	public String statCandidateDetail(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_statCandidateDetail_M();
		return APPRETURN(result);
	}
	//客户公司详细信息
	public String statCompanyDetail(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_statCompanyDetail_M();
		return APPRETURN(result);
	}
	/******************* 此处以上写 public 接口方法  *****************/

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342031354696488147L;
	
	/******************* 此处开始写 private 内部方法 ServiceDO_  *****************/
	
	private LWResult ServiceDO_statHCList_M() {
		LWResult result = null;
		String memo = "HC统计数据";
		try {
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String keyword = this.getParameter("keyword");
			int maxcount = this.getParameter("i9", 20);
			String order = this.getParameter("order","");//排序
			String FIRSTID = "";
			String LASTID = "";
			this.page = this.getParameter("i8", 1);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			
			
			String firstDataid = "";
			String lastDataid = "";
			String amid = "";
			String jobTotal = "0";//职位总数
			String rtn_time = "";//合同剩余时间
			String rtnString = "";
			int n = 0;
			
			
			
			
		} catch (Exception e) {

		}
		return null;
	}
	
	private LWResult ServiceDO_statCompanyDetail_M() {
		LWResult result = null;
		String memo = "查询客户公司详细信息";
		try {
			String amid = this.getParameter("id");
			StringBuilder son = new StringBuilder("");//子公司
			StringBuilder parent = new StringBuilder("");//母公司
			StringBuilder ss = new StringBuilder("");
			
			int n = 0;
			List<Object> organs = dbQuery(String.format(YSSQL.LWOrgan_Detail_Stat,new Object[]{amid}));
			if(organs==null || organs.size()<=0){
				organs = dbQuery(String.format(YSSQL.LWMMOrgan_Detail_Stat, new Object[]{amid}));
			}
			if(organs!=null&&organs.size()>0){
				List<Object> sons = dbQuery(String.format("select a.c2 name from B_MMORGAN a where a.c3='%s'", new Object[]{amid}));
				if(sons!=null&&sons.size()>0){
					for (Object s : sons) {
						son.append(this.getDBString(s, "name")).append(",");
					}
					son.deleteCharAt(son.length()-1);
				}
				for (Object o : organs) {
					List<Object> parents = dbQuery(String.format("select a.c2 name from B_MMORGAN a where a.c1='%s'", new Object[]{this.getDBString(o, "parent")}));
					if(parents!=null&&parents.size()>0){
						for (Object p : parents) {
							parent.append(this.getDBString(p, "name")).append(",");
						}
						parent.deleteCharAt(son.length()-1);
					}
					ss.append(this.getDBString(o, "logo").isEmpty()? "../doc/images/face_c2_b.png":this.getDBString(o, "logo"))//logo
					.append(Constants.SPLITER).append(this.getDBString(o, "shortName"))//简称
					.append(Constants.SPLITER).append(this.getDBString(o, "name"))//中文全称
					.append(Constants.SPLITER).append(this.getDBString(o, "englishName"))//英文全称
					.append(Constants.SPLITER).append(this.getDBString(o, "nature").startsWith("220")?this.getDBDataParam(o, "nature", "", "",""):this.getDBString(o, "nature"))//公司类型
					.append(Constants.SPLITER).append(this.getDBString(o, "createtime"))//成立时间
					.append(Constants.SPLITER).append(this.getDBString(o, "amount").startsWith("222")?this.getDBDataParam(o, "amount", "", "",""):this.getDBString(o, "amount"))//公司规模
					.append(Constants.SPLITER).append(parent.toString())//母公司
					.append(Constants.SPLITER).append(son.toString())//子公司
					.append(Constants.SPLITER).append(this.getDBString(o, "bigFactory").startsWith("801")?this.getDBDataParam(o, "bigFactory", "", "",""):this.getDBString(o, "bigFactory"))//大行业
					.append(Constants.SPLITER).append(this.getDBString(o, "factory").startsWith("801")?this.getDBDataParam(o, "factory", "", "",""):this.getDBString(o, "factory"))//所属行业
					.append(Constants.SPLITER).append(this.getDBString(o, "city"))//所在城市
					.append(Constants.SPLITER).append(this.getDBString(o, "code"))//邮编
					.append(Constants.SPLITER).append(this.getDBString(o, "switchboard"))//总机
					.append(Constants.SPLITER).append(this.getDBString(o, "netAddress"))//网址
					.append(Constants.SPLITER).append(this.getDBString(o, "addr1"))//具体地址
					.append(Constants.SPLITER).append(this.getDBString(o, "addr2"))
				    .append(Constants.SPLITER).append(this.getDBString(o, "addr3"))
				    .append(Constants.SPLITER).append(this.getDBString(o, "introduce"))//公司简介
				    .append(Constants.SPLITER).append(this.getDBString(o, "license"));//公司执照
				}
				n++;
			}
			if(n>0) return result = new LWResult(LWResult.SUCCESS, ss.toString());
			return result = new LWResult(LWResult.FAILURE, memo+"无记录");
		} catch (Exception e) {
			e.printStackTrace();
			return result =  new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	
	
	private LWResult ServiceDO_statHRList_M() {
		LWResult result = null;
		String memo = "加载客户列表";
		try {
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");;
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String submk = this.getParameter("submk");
			String keyword = this.getParameter("keyword");
			int maxcount = this.getParameter("i9", 20);
			String order = this.getParameter("order","");//排序
			this.page = this.getParameter("i8", 1);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			
			List<Object> ids = new ArrayList<>();
			String firstDataid = "";
			String lastDataid = "";
			String amid = "";
			String jobTotal = "0";//职位总数
			String rtn_time = "";//合同剩余时间
			String rtnString = "";
			int n = 0;
			//加载公司列表
			if(submk.equals("2")){
				List<Object> lists = dbQuery(YSSQL.LWCompany_Stat(keyword,order));
				if(lists!=null&&lists.size()>0){
					firstDataid = this.getDBString(lists.get(0), "sortid");
					for (Object o : lists) {
						Long id = this.getDBLong(o, "sortid");
						if(ids.contains(id)) continue;
						ids.add(id);
						lastDataid = this.getDBString(o, "sortid");
						amid = this.getDBString(o, "amid");
						if(this.getDBString(o, "rtn_time")!=null&&!this.getDBString(o, "rtn_time").isEmpty()){
								rtn_time = Math.round(Double.parseDouble(this.getDBString(o, "rtn_time")))+"天";
						}else{
							rtn_time = "未签署";
						}
						String totalJob = this.getDBString(o, "totalJob");
						if(totalJob.isEmpty()){
							totalJob = "0";
						}
						s.append(String.format(htmls.get(1), new Object[]{
							amid,
							this.getDBString(o, "companyName"),//企业名称
									rtn_time,//合同状态
									this.getDBString(o, "hrnum"),//hr账户
									this.getDBString(o, "nums")+"/"+totalJob,//在招职位/总职位
									this.getDBString(o, "storeNum"),//关注数
									this.getDBString(o, "inviteNum"),//邀请面试次数
									this.getDBString(o, "acceptNum"),//接受邀请数目
									this.getDBString(o, "offerNum"),//面试通过数目
									this.getDBString(o, "acceptOffer"),//接受offer数目
									this.getDBString(o, "entryNum"),//确认入职数目
									this.getDBString(o, "dimission"),//保障期离职数
									this.getDBString(o, "bill"),//开票金额
									this.getDBString(o, "pay")//付款金额
						}));
						n++;
					}
				}
			}
			if(submk.equals("3")){
				String activeJob = "0";
				List<Object> HR_stat = dbQuery(YSSQL.LWHR_List_Stat(keyword,order));
				if(HR_stat!=null&&HR_stat.size()>0){
					firstDataid = this.getDBString(HR_stat.get(0), "sortid");
					for (Object o : HR_stat) {
						lastDataid = this.getDBString(o, "sortid");
						if(ids.contains(this.getDBLong(o, "sortid"))) continue;
						ids.add(this.getDBLong(o, "sortid"));
						rtn_time = this.getDBString(o, "agreement");
						if(rtn_time!=null&&rtn_time.length()>0){
							rtn_time =  Math.round(Double.parseDouble(rtn_time))+"天";
						}else{
							rtn_time = "未签署";
						}
						List<Object> actJob = dbQuery(String.format("select count(a.c1) actJobs from B_JOB a where a.c4=1 and a.c2='%s'",new Object[]{this.getDBString(o, "auid")}));
						if(actJob!=null&&actJob.size()>0){
							for (Object job : actJob) {
								activeJob = this.getDBString(job, "actJobs");
							}
						}
						String totalJob = this.getDBString(o, "hrJob");
						if(totalJob.isEmpty()){
							totalJob = "0";
						}
						s.append(String.format(htmls.get(1), new Object[]{
							this.getDBString(o, "HrName"),//hr名称
							this.getDBString(o, "short_name"),//公司名称
							rtn_time,
							activeJob+"/"+totalJob,
							this.getDBDate(o, "lastLogin", "yyyy-MM-dd HH:mm"),//最后登录时间
							this.getDBString(o, "views"),//浏览数
							this.getDBString(o, "overview"),//关注数
							this.getDBString(o, "inviteNum"),//邀请面试数
							this.getDBString(o, "acceptNum"),//接受邀请数
							this.getDBString(o, "offerNum"),//面试通过数
							this.getDBString(o, "acceptofferNum"),//接受offer数
							this.getDBString(o, "entryNum"),//确认入职数
							this.getDBString(o, "dimission"),//保障期内离职数
							this.getDBString(o, "bill"),//开票金额
							this.getDBString(o, "pay")//付款金额
						}));
						n++;
					}
				}
			}
			if(submk.equals("4")){
				List<Object> jobs = dbQuery(YSSQL.LWJob_List_Stat(keyword,order));
				if(jobs!=null&jobs.size()>0){
					firstDataid = this.getDBString(jobs.get(0), "sortid");
					for (Object o : jobs) {
						lastDataid = this.getDBString(o, "sortid");
						if(ids.contains(this.getDBLong(o, "sortid"))) continue;
						ids.add(this.getDBLong(o, "sortid"));
						rtn_time = this.getDBString(o, "agreement");
						if(rtn_time!=null&&rtn_time.length()>0){
							rtn_time = "<span class='act'>合同</span>";
						}
						s.append(String.format(htmls.get(1), new Object[]{
							this.getDBString(o, "jobName"),//职位名称
							(this.getDBString(o, "status").equals("1")?"招聘中":"已下线") + "/" + this.getDBDate(o, "updateTime", "yyyy-MM-dd"),//当前状态/操作时间
							this.getDBString(o, "hrName"),//HR账户
							this.getDBString(o, "short_name"),//公司名称
							rtn_time,//合同状态
							this.getDBString(o, "inviteNum"),//邀请面试次数
							this.getDBString(o, "acceptNum"),//接受邀请次数
							this.getDBString(o, "offerNum"),//面试通过数目
							this.getDBString(o, "acceptofferNum"),//接受offer数目
							this.getDBString(o, "entryNum"),//确认入职数目
							this.getDBString(o, "dimission")//保障期内离职
						}));
						n++;
					}
				}
			}
			
			rtnString = String.format(htmls.get(0),s.toString());
			if(n>0) return result = new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "",rtnString); 
			return result = new LWResult(LWResult.FAILURE, memo+"无记录");
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	
	
	private LWResult ServiceDO_statCandidateDetail_M() {
		LWResult result = null;
		try {
			StringBuilder s = new StringBuilder("");
			String cid = this.getParameter("cid");
			String rtnString = "";
			
			List<Object> list = dbQuery(String.format("select a.*,sysdate-to_date(a.c10,'yyyy-MM-dd') age from B_CANDIDATE a where a.c1='%s'", new Object[]{cid}));
			int n = 0;
			if(list!=null&&list.size()>0){
				for (Object o : list) {
					String status = "";
					if(this.getDBString(o, "c3").equals("240444")){
						status = "<span class='bla'>BLA</span>";
					}else if(this.getDBString(o, "c5").equals("247001")){
						status = "<span class='act'>ACT</span>";
					}else if(this.getDBString(o, "c5").equals("247002")){
						status = "<span class='blo'>BLO</span>";
					}else{
						status = "<span class='ina'>INA</span>";
					}
					s.append(this.getDBString(o, "c61").isEmpty()? "../doc/images/M/face_c_s.png":this.getDBString(o, "c61"))//候选人头像
					.append(Constants.SPLITER).append(status)//候选人状态
					.append(Constants.SPLITER).append(this.getDBString(o, "c8").isEmpty()? this.getDBString(o, "c9"):this.getDBString(o, "c8"))//候选人名称
					.append(Constants.SPLITER).append(this.getDBString(o, "c41"))//最近所在行业
					.append(Constants.SPLITER).append(this.getDBString(o, "c42"))//最近职位
					.append(Constants.SPLITER).append(this.getDBString(o, "c13").equals("132001")? "男":"女")//性别
					.append(Constants.SPLITER).append((int)Math.floor(Double.parseDouble(this.getDBString(o, "age"))/365)+1)//年龄
					.append(Constants.SPLITER).append(this.getDBString(o, "c39"))//工作年限
					.append(Constants.SPLITER).append(Double.parseDouble(this.getDBString(o, "c43"))/1000)//月薪
					.append(Constants.SPLITER).append(this.getDBString(o, "c15"))//居住城市
					.append(Constants.SPLITER).append(ComBeanLoginUser.getUserName(this.getDBString(o, "c2")))//顾问
					.append(Constants.SPLITER).append(this.getDBDate(o, "c4", "yyyy-MM-dd"));//入库时间
				}
				n++;
			}
			if(n>0) return result = new LWResult(LWResult.SUCCESS, s.toString());
			return result = new LWResult(LWResult.FAILURE, "无数据");
		} catch (Exception e) {
			return result = new LWResult(LWResult.FAILURE, "异常");
		}
	}
	
	
	private LWResult ServiceDO_statCandidate_M() {
		LWResult result = null;
		try {
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			String c_keyword = this.getParameter("keyword");//公司名/候选人/顾问
			int maxcount = this.getParameter("i9", 20);
			String orderBy = this.getParameter("order");
			String acstatus = ACE.CANDI_STStatus_ST+","+ACE.CANDI_STStatus_BLACKLIST;
			String FIRSTID = "";
			String LASTID = "";
			this.page = this.getParameter("i8", 1);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String rtnString = null;
			String[] cols = {"c2","c8","c9","c41"};
			List<Long> ids = new ArrayList<Long>();
			int n = 0;
			String firstDataid = "";
			String lastDataid = "";
			for (String c : cols) {
				List<Object> list = dbQuery(YSSQL.LWCandidate_Stat(c, c_keyword,acstatus,FIRSTID,LASTID,orderBy));
				if(list!=null&&list.size()>0){
					long id;
					firstDataid = this.getDBString(list.get(0), "sortid");
					for (Object o : list) {
						if(n == maxcount) break;
						lastDataid = this.getDBString(o, "sortid");
						id = this.getDBLong(o, "sortid");
						if(ids.contains(id)) continue;
						ids.add(id);
						String status = "";
						if(this.getDBString(o, "c3").equals("240444")){
							status = "<span class='bla'>BLA</span>";
						}else if(this.getDBString(o, "c5").equals("247001")){
							status = "<span class='act'>ACT</span>";
						}else if(this.getDBString(o, "c5").equals("247002")){
							status = "<span class='blo'>BLO</span>";
						}else{
							status = "<span class='ina'>INA</span>";
						}
						s.append(String.format(htmls.get(1), new Object[]{
							this.getDBString(o, "c1"),
							//this.getDBString(o, "c61").isEmpty()? "../doc/images/M/face_c_s.png":this.getDBString(o, "c61"),//候选人头像
							this.getDBString(o, "c8"),
							this.getDBString(o, "c9"),//候选人名称
							status,//候选人状态
							this.getDBString(o, "c41"),//最近公司
							this.getDBString(o, "c42"),//最近职位
							ComBeanLoginUser.getUserName(this.getDBString(o, "c2")),//顾问
							this.getDBString(o, "c28"),//浏览次数
							this.getDBString(o, "c27"),//关注次数
							this.getDBString(o, "c29"),//邀请面试次数
							this.getDBString(o, "c30"),//接受邀请
							this.getDBString(o, "c31"),//面试通过
							this.getDBString(o, "c32"),//接受offer
							this.getDBString(o, "c33"),//确认入职
							this.getDBString(o, "c34")//保障期离职
						}));
						n++;
					}
				}
			}
			rtnString = String.format(htmls.get(0), s.toString());
			if(n>0) return result = new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "",rtnString);
			return new LWResult(LWResult.FAILURE, "无记录");
		} catch (Exception e) {
			return new LWResult(LWResult.FAILURE, "异常");
		}
	}
	private String ServiceDO_exportBrowse_M() {
		String result = null;
		try {
			String beginTime = this.getParameter("begin");
			String end = this.getParameter("end");
			Object[] objs = null;
			List<Long> ids = new ArrayList<>();
			
			List<Object> lists = dbQuery(YSSQL.LWStatHR_exportBrowse_M(beginTime,end));
			if(lists==null || lists.size()==0){
	    		this.setScript("parent.setError('无数据！');");
	        	return null;
	    	}
			
			if(lists!=null&&lists.size()>0){
				int a = 0;
				for (Object o : lists) {
					String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		a++;
	            	}
				}
				int i = 0;
	            objs = new Object[lists.size()-a+2];
	            objs[i++] = this.getParameter("tableHeight", BConstants.DEF_TABLE_HEIGHT);
	            
	            int j = 0;
	            Object[] tmp = new Object[8];
	            tmp[j++] = new TColumn("公司名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR手机号码", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR邮箱", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("候选人", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("职位", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("当前公司", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("浏览时间", null, TColumn.ALIGN_CENTER);
	            
	            //tmp[j++] = new TColumn(B_Data.dataSctLevel_desc, null, TColumn.ALIGN_CENTER);
	            objs[i++] = tmp;
	            
	            //long id = 0;
	            for (Object o : lists) {
	            	/*id = this.getDBLong(o, "sortid");
	            	if(ids.contains(id)) continue;
	            	
	            	ids.add(id);*/
	            	tmp = new Object[9];
	            	/*String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		j = 0;
		            	tmp[j++] = "";
		            	tmp[j++] = "";//公司名称
		            	tmp[j++] = "";//HR名称
		            	tmp[j++] = "";//手机号码
		            	tmp[j++] = "";//邮箱地址
		            	tmp[j++] = "";//候选人姓名
		            	tmp[j++] = "";//浏览时间
	            	}else{*/
	            	String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		continue;
	            	}
	            		j = 0;
	            		tmp[j++] = "";
	            		tmp[j++] = this.getDBString(o, "companyName");//公司名称
	            		tmp[j++] = this.getDBString(o, "HRName");//HR名称
	            		tmp[j++] = this.getDBString(o, "phone");//手机号码
	            		tmp[j++] = this.getDBString(o, "email");//邮箱地址
	            		tmp[j++] = this.getDBString(o, "candiName");//候选人姓名
	            		tmp[j++] = this.getDBString(o, "job");
	            		tmp[j++] = this.getDBString(o, "company");
	            		tmp[j++] = this.getDBString(o, "overViewTime");//浏览时间
	            	//}
	            	objs[i++] = tmp;
				}
			}
			String title = "";
			this.setResultPoi("HR Browse situation"+HIUtil.getCurrentDate("yyyyMMddHHmmss"), new PoiUtil().createXssf(title, objs));
			return BConstants.POI;
		} catch (Exception e) {
			e.printStackTrace();
			this.setScript("parent.setError('异常！');");
        	return BConstants.POI;
		}
	}
	
	private String ServiceDO_exportStore_M() {
		String result = null;
		try {
			String beginTime = this.getParameter("begin");
			String end = this.getParameter("end");
			Object[] objs = null;
			List<Long> ids = new ArrayList<>();
			
			List<Object> lists = dbQuery(YSSQL.LWStatHR_exportStore_M(beginTime,end));
			if(lists==null || lists.size()==0){
	    		this.setScript("parent.setError('无数据！');");
	        	return null;
	    	}
			
			if(lists!=null&&lists.size()>0){
				
				int a = 0;
				for (Object o : lists) {
					String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		a++;
	            	}
				}
				int i = 0;
	            objs = new Object[lists.size()-a+2];
	            objs[i++] = this.getParameter("tableHeight", BConstants.DEF_TABLE_HEIGHT);
	            
	            int j = 0;
	            Object[] tmp = new Object[8];
	            tmp[j++] = new TColumn("公司名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR手机号码", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR邮箱", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("候选人", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("职位", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("当前公司", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("收藏时间", null, TColumn.ALIGN_CENTER);
	            
	           // tmp[j++] = new TColumn(B_Data.dataSctLevel_desc, null, TColumn.ALIGN_CENTER);
	            objs[i++] = tmp;
	            
	            //long id = 0;
	            for (Object o : lists) {
	            	/*id = this.getDBLong(o, "sortid");
	            	if(ids.contains(id)) continue;
	            	
	            	ids.add(id);*/
	            	String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		continue;
	            	}
	            	tmp = new Object[9];
	            	j = 0;
	            	tmp[j++] = "";
	            	tmp[j++] = this.getDBString(o, "companyName");//公司名称
	            	tmp[j++] = this.getDBString(o, "hrName");//HR名称
	            	tmp[j++] = this.getDBString(o, "phone");//手机号码
	            	tmp[j++] = this.getDBString(o, "email");//邮箱地址
	            	tmp[j++] = this.getDBString(o, "candiName");//候选人姓名
	            	tmp[j++] = this.getDBString(o, "job");
            		tmp[j++] = this.getDBString(o, "company");
	            	tmp[j++] = this.getDBString(o, "storeTime");//浏览时间
	            	
	            	objs[i++] = tmp;
				}
			}
			String title = "";
			this.setResultPoi("HR Store situation"+HIUtil.getCurrentDate("yyyyMMddHHmmss"), new PoiUtil().createXssf(title, objs));
			return BConstants.POI;
		} catch (Exception e) {
			e.printStackTrace();
			this.setScript("parent.setError('异常！');");
        	return BConstants.POI;
		}
	}
	private String ServiceDO_exportInvite_M() {
		String result = null;
		try {
			String beginTime = this.getParameter("begin");
			String end = this.getParameter("end");
			Object[] objs = null;
			List<Long> ids = new ArrayList<>();
			
			List<Object> lists = dbQuery(YSSQL.LWStatHR_exportInvite_M(beginTime,end));
			if(lists==null || lists.size()==0){
	    		this.setScript("parent.setError('无数据！');");
	        	return null;
	    	}
			
			if(lists!=null&&lists.size()>0){
				
				int a = 0;
				for (Object o : lists) {
					String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		a++;
	            	}
				}
				int i = 0;
	            objs = new Object[lists.size()-a+2];
	            objs[i++] = this.getParameter("tableHeight", BConstants.DEF_TABLE_HEIGHT);
	            
	            int j = 0;
	            Object[] tmp = new Object[8];
	            tmp[j++] = new TColumn("公司名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR名称", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR手机号码", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("HR邮箱", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("候选人", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("职位", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("当前公司", null, TColumn.ALIGN_CENTER);
	            tmp[j++] = new TColumn("被邀请面试时间", null, TColumn.ALIGN_CENTER);
	            
	           // tmp[j++] = new TColumn(B_Data.dataSctLevel_desc, null, TColumn.ALIGN_CENTER);
	            objs[i++] = tmp;
	            
	            //long id = 0;
	            for (Object o : lists) {
	            	/*id = this.getDBLong(o, "sortid");
	            	if(ids.contains(id)) continue;
	            	
	            	ids.add(id);*/
	            	String email = this.getDBString(o, "email");
	            	if(Pattern.matches(("^177.*[0-6]$"),this.getDBString(o, "phone"))||email.indexOf("@ace-elite.com")!=-1){
	            		continue;
	            	}
	            	tmp = new Object[9];
	            	j = 0;
	            	tmp[j++] = "";
	            	tmp[j++] = this.getDBString(o, "companyName");//公司名称
	            	tmp[j++] = this.getDBString(o, "hrName");//HR名称
	            	tmp[j++] = this.getDBString(o, "phone");//手机号码
	            	tmp[j++] = this.getDBString(o, "email");//邮箱地址
	            	tmp[j++] = this.getDBString(o, "candiName");//候选人姓名
	            	tmp[j++] = this.getDBString(o, "job");
            		tmp[j++] = this.getDBString(o, "company");
	            	tmp[j++] = this.getDBString(o, "inviteTime");//浏览时间
	            	
	            	objs[i++] = tmp;
				}
			}
			String title = "";
			this.setResultPoi("HR FaceToFace situation"+HIUtil.getCurrentDate("yyyyMMddHHmmss"), new PoiUtil().createXssf(title, objs));
			return BConstants.POI;
		} catch (Exception e) {
			e.printStackTrace();
			this.setScript("parent.setError('异常！');");
        	return BConstants.POI;
		}
	}
	private LWResult ServiceDO_HRDataCount_M() {
		LWResult result = null;
		String memo = "";
		try {
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			String submk = this.getParameter("submk","");
			StringBuilder s = new StringBuilder();
			int maxcount = this.getParameter("i9", 20);
			String rtnString = "";
			
			this.page = this.getParameter("i8", 1);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			
			int n = 0;
			String firstDataid = "";
			String lastDataid = "";
			if(submk.equals("1")){
				memo = "HR数据统计-日数据";
				List<Object> lists = dbQuery(YSSQL.LWStat_HRData_DAY);
				if(lists!=null&&lists.size()>0){
					firstDataid = this.getDBString(lists.get(0), "sortid");
					for (Object o : lists) {
						if(n == maxcount) break;
						lastDataid = this.getDBString(o, "sortid");
						s.append(String.format(htmls.get(1), new Object[]{
							this.getDBDate(o, "day", "yyyy-MM-dd"),//日期
							this.getDBString(o, "LoginHR"),//登录HR
							this.getDBString(o, "addHR"),//新增HR
							this.getDBString(o, "amountLoginHR"),//总计登录HR
							this.getDBString(o, "overview"),//关注数
							this.getDBString(o, "store"),//储存数
							this.getDBString(o, "invite")//邀面数
						}));
						n++;
					}
				}
			}
			if(submk.equals("2")){
				memo = "HR数据统计-周数据";
				List<Object> lists = dbQuery(YSSQL.LWStat_HRData_Week);
				if(lists!=null&&lists.size()>0){
					firstDataid = this.getDBString(lists.get(0), "sortid");
					for (Object o : lists) {
						if(n == maxcount) break;
						lastDataid = this.getDBString(o, "sortid");
						s.append(String.format(htmls.get(1), new Object[]{
							this.getDBString(o, "week"),//周数
							this.getDBString(o, "dateTime"),//日期
							this.getDBString(o, "loginHR"),//本周登录HR
							this.getDBString(o, "addHR"),//本周新增HR
							this.getDBString(o, "HRRemain"),//上周HR留存率
							this.getDBString(o, "totalHR"),//总登录过的HR数
							this.getDBString(o, "goodCandiThisWeek"),//本周精选人数
							this.getDBString(o, "overview"),//候选人浏览数
							this.getDBString(o, "store"),//候选人收藏数
							this.getDBString(o, "invite")//候选人邀面数
						}));
						n++;
					}
				}
			}
			if(submk.equals("3")){
				memo = "HR数据统计-月数据";
				List<Object> lists = dbQuery(YSSQL.LWStat_HRData_Month);
				if(lists!=null&&lists.size()>0){
					firstDataid = this.getDBString(lists.get(0), "sortid");
					for (Object o : lists) {
						if(n == maxcount) break;
						lastDataid = this.getDBString(o, "sortid");
						s.append(String.format(htmls.get(1), new Object[]{
								this.getDBString(o, "month"),//月份
								this.getDBString(o, "LoginHR"),//本月登录HR数
								this.getDBString(o, "addHR"),//本月新增HR
								this.getDBString(o, "remainHRlastMon"),//上月留存
								this.getDBString(o, "totalHR"),//总登录HR
								this.getDBString(o, "overview"),//候选人浏览
								this.getDBString(o, "store"),//候选人收藏
								this.getDBString(o, "invite")//候选人邀面
						}));
						n++;
					}
				}
			}
			rtnString = String.format(htmls.get(0), s.toString());
			if(n>0) return result = new LWResult(LWResult.SUCCESS, this.totalRow, firstDataid, lastDataid, "",rtnString);
			return result = new LWResult(LWResult.FAILURE, memo+"无记录");
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
}
