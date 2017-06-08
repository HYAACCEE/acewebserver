package com.svv.dms.app.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;

import com.gs.db.dao.DaoUtil;
import com.gs.db.database.BizDBResult;
import com.gs.db.util.DBUtil;
import com.svv.dms.app.ACE;
import com.svv.dms.app.BMMOrgan;
import com.svv.dms.app.BUser;
import com.svv.dms.app.BitchImportUtil;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.DES;
import com.svv.dms.web.util.HIUtil;

public class MMService extends CandiBase {
	final public static String MARKER_HR_CONTACT_WAITING = "<待联系>";

	public String MMBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String MMSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String MMCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String MMMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}

	//校验公司简称
	public String checkShortName(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_checkShortName_M();
		return APPRETURN(result);
	}
	
	private LWResult ServiceDO_checkShortName_M() {
		LWResult result = null;
		String memo = "公司简称查重";
		try {
			String shortName = this.getParameter("shortName");
			
			List<Object> lists = dbQuery(String.format("select a.* from A_ORGAN a where a.c2='%s'", new Object[]{shortName}));
			if(lists!=null&&lists.size()>0){
				return result = new LWResult(LWResult.FAILURE, memo+"记录:"+lists.size());
			}else{
				return result = new LWResult(LWResult.SUCCESS, memo+"无记录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
	}
	//合并顾问
	public String combineHC(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_combineHC_M();
		return APPRETURN(result);
	}
	//最新添加HR
	public String insertHR(){
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_insertHR_M();
		return APPRETURN(result);
	}
	//批量导入HR
	public String importHR(){
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_uploadHR_M();
		return APPRETURN(result);
	}
	//新增合同
	public String addAgreeMent(){
		if(!ACCESS(true,true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddAgreeMent();
		return APPRETURN(result);
	}
	
	//用户注册
	public String registNewUser(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		int registMode = this.getParameter("registMode", -1);
		int newMMFlag = this.getParameter("newMMFlag", -1);
		if(isHR()) result = ServiceDO_AddOrEditUser(Constants.USER2_MMGROUP, true, registMode, newMMFlag);
		if(isHC()) result = ServiceDO_AddOrEditUser(Constants.USER3_MMGROUP, true, -1, -1);
		return APPRETURN(result);
	}
	//检查公司名是否存在，不存在就从AOrgan中添加到MMOrgan
	public String registCheckMM(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_AddByCompanyName(this.getParameter("companyName"));
		return APPRETURN(result);
	}
	
	//新增顾问
	public String addHC(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddOrEditUser(Constants.USER3_MMGROUP, true, -1, -1);
		return APPRETURN(result);
	}
	
	//新增客户
	public String addHR(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddOrEditUser(Constants.USER2_MMGROUP, true, -1, -1);
		return APPRETURN(result);
	}
	
	//新增公司
	public String addCompany(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_addCompany_HR_HC();
		if(isHC()) result = ServiceDO_addCompany_HR_HC();
		if(isM()) result = ServiceDO_addCompany_M();
		return APPRETURN(result);
	}
	//编辑顾问
	public String editHC(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddOrEditUser(Constants.USER3_MMGROUP, false, -1, -1);
		return APPRETURN(result);
	}
	//编辑客户
	public String editHR(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_AddOrEditUser(Constants.USER2_MMGROUP, false, -1, -1);
		return APPRETURN(result);
	}
    //客户列表
	public String listHR(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_HRList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览客户列表");
		return APPRETURN(result);
	}
    //顾问列表
	public String listHC(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_HCList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览顾问列表");
		return APPRETURN(result);
	}
    //顾问信息
	public String infoHC(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_userInfo_M(Constants.USER3_MMGROUP);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览顾问信息");
		return APPRETURN(result);
	}
    //客户信息
	public String infoHR(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_userInfo_M(Constants.USER2_MMGROUP);
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览客户信息");
		return APPRETURN(result);
	}
    //审核顾问信息
	public String cmtHC(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String id = this.getParameter("id");
		String mmAmid = this.getParameter("mmAmid");
		if(isM()) result = ServiceDO_userCmt_M(Constants.USER3_MMGROUP, id, mmAmid);
		return APPRETURN(result);
	}
    //审核客户信息
	public String cmtHR(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String id = this.getParameter("id");
		String mmAmid = this.getParameter("mmAmid");
		if(isM()) result = ServiceDO_userCmt_M(Constants.USER2_MMGROUP, id, mmAmid);
		return APPRETURN(result);
	}
	
	//查询公司信息
	public String infoCompany(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_detailCompany();
		if(isHC()) result = ServiceDO_detailCompany();
		if(isM()) result = ServiceDO_infoCompany_M();
		return APPRETURN(result);
	}
	//查询公司行业跟地址
	public String infoCompanyPart(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isHR()) result = ServiceDO_infoCompanyPart_HR();
		return APPRETURN(result);
	}
	
	//公司列表
	public String listMM(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_companyList_M();
        loggerM(ComBeanLogType.TYPE_QUERY, "浏览公司列表");
		return APPRETURN(result);
	}

	public String uploadFile(){
		System.out.println("[MMService.uploadFile]=======================================");
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("MM");
	}

	public String uploadPic(){
		System.out.println("[MMService.uploadPic]=======================================");
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("MM");
	}

	public String uploadFace(){
		System.out.println("[MMService.uploadFace]=======================================");
		if(!ACCESS(false, false, true)) return BConstants.MESSAGE;
		return F_UploadFace("MM", 240, 240);
	}
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;
	
	private LWResult ServiceDO_HCList_M(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //在线申请审核1，更新2，黑名单4
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
			if(queryMK==1) //在线申请审核
                list = dbQuery(YSSQL.LWHunter_QueryByC(ACE.MM_BizStatus_NEW, keyword, FIRSTID, LASTID));
			else if(queryMK==2) //更新
			    list = dbQuery(YSSQL.LWHunter_QueryByC("", keyword, FIRSTID, LASTID));
			else if(queryMK==4) //黑名单
			    list = dbQuery(YSSQL.LWHunter_QueryByC(ACE.MM_BizStatus_BLACKLIST, keyword, FIRSTID, LASTID));

			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String html = null;
			int n = 0;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				int index = 100;
				firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					index++;
					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
					
					//黑名单
					if(queryMK==4){
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4") + this.getDBString(o, "c33", "", " | ", ""), // 李大志
								"拉黑于"+this.getDBDate(o, "uptdate", DATETIME_FORMAT), // 拉黑于2016/12/23 23:01
						}));
					}else if(queryMK==2){
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4") + this.getDBString(o, "c33", "", " | ", ""), // 李大志
								"<div class=\"layui-inline pdr50\" style=\"width:500px;\">"+this.getDBString(o, "c9", "[Freelancer]")+"</div>" + "<div class=layui-inline>更新于"+this.getDBDate(o, "uptdate", DATETIME_FORMAT)+"</div>",
						}));
					}else if(queryMK==1){
						String remainTime = "";
						double completeTime = this.getDBDouble(o, "remainTime");
						String color = "bg-green";
						
						if(completeTime>=0){
							remainTime = "剩余"+HIUtil.NumFormat(completeTime, 0)+"小时";
							completeTime = 24 - completeTime;
						}else{
							if(completeTime>=-48) remainTime = "超过"+HIUtil.NumFormat(-1*completeTime, 0)+"小时";
							else remainTime = "超过"+HIUtil.NumFormat(-1*completeTime/24, 0)+"天";
							completeTime = 24;
						}
						
						s.append(String.format(html, new Object[]{
							this.getDBString(o, "c1"),//DATA
							this.getDBString(o, "c2"),//DATA1
							this.getDBString(o, "imge").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "imge"),//头像
							this.getDBString(o, "c4") + this.getDBString(o, "c33", "", " | ", ""), // 李大志
							"",
							"<span title=\""+remainTime+"\" class=\"inline-block h15 "+color+"\" style=\"width:120px\"><span class=\"border inline-block h15 bg-grey\" style=\"width:"+Math.min(120, completeTime*5)+"px\"></span></span>", // 80px  总长120 目前是6项
							"<div class=\"layui-inline pdr50\" style=\"width:500px;\">"+this.getDBString(o, "c9", "[Freelancer]")+"</div>" + "<div class=layui-inline>更新于"+this.getDBDate(o, "istdate", DATETIME_FORMAT)+"</div>"
						}));
					}else{
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4") + this.getDBString(o, "c33", "", " | ", ""), // 李大志
								"<div class=\"layui-inline pdr50\" style=\"width:500px;\">"+this.getDBString(o, "c9", "[Freelancer]")+"</div>" + "<div class=layui-inline>更新于"+this.getDBDate(o, "istdate", DATETIME_FORMAT)+"</div>",
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
	
	private LWResult ServiceDO_HRList_M(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //在线申请审核1，更新2，黑名单4
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
			if(queryMK==1) //在线申请审核
                list = dbQuery(YSSQL.LWHR_QueryByC(ACE.USER_CHECK_STATUS_NEW+","+ACE.USER_CHECK_STATUS_CONFIRMING, keyword, FIRSTID, LASTID));
			else if(queryMK==2) //更新
			    list = dbQuery(YSSQL.LWHR_QueryByC("", keyword, FIRSTID, LASTID));
			else if(queryMK==4) //黑名单
			    list = dbQuery(YSSQL.LWHR_QueryByC(ACE.MM_BizStatus_BLACKLIST, keyword, FIRSTID, LASTID));

			String HTML = ComBeanAPPModule.getHtml(objectMode);
			String html = null;
			int n = 0;
			String firstDataid = null, lastDataid = null;
			if(list!=null && list.size()>0){
				int index = 100;
				firstDataid = this.getDBString(list.get(0), "sortid");
				for(Object o : list){
					if(n == maxcount) break;
					lastDataid = this.getDBString(o, "sortid");
					
					index++;
					html = HTML.replaceAll("<INDEX>", String.valueOf(index));
					
					//黑名单
					if(queryMK==4){
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4"), // 李大志
								"拉黑于"+this.getDBDate(o, "uptdate", DATETIME_FORMAT), // 拉黑于2016/12/23 23:01
						}));
					}else if(queryMK==2){
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4"), // 李大志
								"<div class=\"layui-inline\" style=\"width:200px\">"+this.getDBString(o, "c7")+"</div>" + "<div class=\"layui-inline pd20\" style=\"width:200px\">"+this.getDBString(o, "c6")+"</div>" + "<div class=layui-inline>更新于"+this.getDBDate(o, "uptdate", DATETIME_FORMAT)+"</div>",
						}));
					}else if(queryMK==1){
						String remainTime = "";
						double completeTime = this.getDBDouble(o, "remainTime");
						String color = "bg-green";
						
						if(completeTime>=0){
							remainTime = "剩余"+HIUtil.NumFormat(completeTime, 0)+"小时";
							completeTime = 24 - completeTime;
						}else{
							if(completeTime>=-48) remainTime = "超过"+HIUtil.NumFormat(-1*completeTime, 0)+"小时";
							else remainTime = "超过"+HIUtil.NumFormat(-1*completeTime/24, 0)+"天";
							completeTime = 24;
						}
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"),//DATA
								this.getDBString(o, "c2"),//DATA1
								this.getDBString(o, "imge").isEmpty()?"../doc/images/M/face_c_s.png":this.getDBString(o, "imge"),//头像
								this.getDBString(o, "c9", "", "", " | ")+this.getDBString(o, "c4") , // 李大志
								"",
								"<span title=\""+remainTime+"\" class=\"inline-block h15 "+color+"\" style=\"width:120px\"><span class=\"border inline-block h15 bg-grey\" style=\"width:"+Math.min(120, completeTime*5)+"px\"></span></span>", // 80px  总长120 目前是6项
								"申请于"+this.getDBDate(o, "istdate", DATETIME_FORMAT)// 申请于2016/12/23 23:01
						}));
					}else{
						s.append(String.format(html, new Object[]{
								this.getDBString(o, "c1"), //DATA
								this.getDBString(o, "c2"), //DATA1
								HIUtil.lPad(this.getDBString(o, "c5"), "13200").substring(5).trim(), //性别 face_c1_m.png
								this.getDBString(o, "c4"), // 李大志
								"申请于"+this.getDBDate(o, "istdate", DATETIME_FORMAT), // 申请于2016/12/23 23:01
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
	
	private LWResult ServiceDO_AddByCompanyName(String companyName){
		LWResult result = null;
		//公司是否存在
        List<Object> list = dbQuery(String.format(YSSQL.LWMMOrgan_QueryByOrganName, new Object[]{companyName}));
        if(list!=null && list.size()>0){
        	Object o = list.get(0);
	        result = new  LWResult(LWResult.SUCCESS, this.getDBString(o, "c1"));
        }else{
        	list = dbQuery(String.format(YSSQL.LWAOrgan_QueryByOrganName, new Object[]{companyName}));
        	if(list!=null && list.size()>0){
        		String new_amid = UGID.createUGID();
        		dbExe(String.format(YSSQL.LWMMOrgan_AddFromAOrgan, new Object[]{
        				new_amid,
        				220010, //c5 机构类型
        				ACE.MM_BizStatus_NEW, //c6公司认证状态: 未认证
        				801, //c7行业
        				ACE.USER_STATUS_NEW, //c8有效状态
        				companyName
        	    }));
    	        result = new  LWResult(LWResult.SUCCESS, new_amid);
        	}else{
        		result = new LWResult(LWResult.SUCCESS, "");
        	}
        }
        return result;
	}
	
	private LWResult ServiceDO_AddOrEditUser(String userGroup, boolean addFlag, int registMode, int newMMFlag){
		LWResult result = null;
		String memo = addFlag?"新增":"更新";
		boolean userHC = userGroup.equals(Constants.USER3_MMGROUP);
		boolean userHR = userGroup.equals(Constants.USER2_MMGROUP);
		if(userHC) memo += "顾问";
		if(userHR) memo += "HR";
		try{
			String userAuid = this.getParameter("id");
			String newUserAuid = addFlag ? UGID.createUGID() : null;
			String[] vv = F_formParam();
			int k = 0;
			String loginName="", password="", nickName="", sex="", fullName="";
			String mmFlag="", comPhone="", companyName="", email="", face="", sidPhoto1="", sidPhoto2="", identityCardNO="", nameCardPhoto1="", nameCardPhoto2="", job="";
			String companySimpName="", companyNature="", companyIndustry="", city="", address="", abbreviation="", companyDetail="", foundDate="", companyScale="";
			
			if(userHR){
				if(isHR()){ 
					if(registMode == 0){ //hr登记待联系的用户信息
						nickName = this.getParameter("nickName", vv[k++]); //昵称
						fullName = nickName; //姓名
						sex = this.getParameter("sex", vv[k++]); //性别
						loginName = this.getParameter("a2", vv[k++]); //手机
						password = this.getParameter("a1", DES.md5("000000")); //初始密码
						companyName = this.getParameter("companyName", vv[k++]); //公司名称
						job = this.getParameter("job", vv[k++]); //职位
						
						nickName = nickName + MARKER_HR_CONTACT_WAITING + loginName;
						
					}else if(registMode == 1){ //HR正式注册
						
						fullName = this.getParameter("fullName", vv[k++]); //姓名
						sex = this.getParameter("sex", vv[k++]); //性别
						loginName = this.getParameter("phone", vv[k++]); //登录手机
						password = this.getParameter("a1", DES.md5(vv[k++])); //初始密码 ////待审核后发送密码给手机?
						nickName = fullName; ////////////////////////this.getParameter("nickName"); //昵称 ////暂不用
						companyName = this.getParameter("companyName", vv[k++]); //公司名称
						
						if(newMMFlag==1){
							abbreviation = vv[k++]; //公司简称
							companyIndustry = vv[k++]; //所属行业
							companyNature = vv[k++]; //公司性质
							companyScale = vv[k++]; //公司规模
							foundDate = vv[k++]; //成立时间
							city = vv[k++]; //公司地点
							address = vv[k++]; //详细地址
							companyDetail = vv[k++]; //公司介绍
						}

						email = this.getParameter("email", vv[k++]); //公司邮箱
						job = this.getParameter("job", vv[k++]); //职位
						comPhone = this.getParameter("comPhone", vv[k++]); //座机

					}
					
				}else if(isM()){ //ADMIN后台添加
					
					k++; //公司AMID
					companyName = this.getParameter("companyName", vv[k++]); //公司名称
					nickName = this.getParameter("nickName",vv[k++]); //昵称
					sex = this.getParameter("sex", vv[k++]); //性别
					fullName = nickName; //姓名
					loginName = this.getParameter("a2", vv[k++]); //登录手机
					password = this.getParameter("a1", DES.md5("123456")); //初始密码
					comPhone = this.getParameter("phone", vv[k++]); //座机
					email = this.getParameter("email", vv[k++]); //公司邮箱
					job = this.getParameter("job", vv[k++]); //职位
				}
			}
			if(userHC){
				if(isHC()){ //HC正式注册

					fullName = this.getParameter("fullName",  vv[k++]); //姓名
					sex = this.getParameter("sex",  vv[k++]); //性别
					loginName = this.getParameter("a2",  vv[k++]); //登录手机
					password = this.getParameter("a1", DES.md5( vv[k++])); //初始密码 ////待审核后发送密码给手机
					nickName = fullName; ////////////////////////this.getParameter("nickName"); //昵称 ////暂不用
					companyName = this.getParameter("companyName",  vv[k++]); //公司名称
					
					mmFlag = this.getParameter("mmFlag", vv[k++]); //标识个人还是公司
					if(mmFlag.equals("0")){ //个人
						email = this.getParameter("email", vv[k++]); //个人邮箱
						comPhone = this.getParameter("comPhone", vv[k++]); //座机
						identityCardNO = this.getParameter("cardnum", vv[k++]); //身份证号
						face = this.getParameter("face", vv[k++]); //头像
						sidPhoto1 = this.getParameter("sidPhoto1", vv[k++]);; //身份证照片
						sidPhoto2 = this.getParameter("sidPhoto2", vv[k++]);; //身份证照片
					}else{//猎头公司
						companyName = this.getParameter("companyName", vv[k++]); //公司名称
						email = this.getParameter("email", vv[k++]); //公司邮箱
						comPhone = this.getParameter("comPhone", vv[k++]); //座机
						identityCardNO = this.getParameter("cardnum", vv[k++]); //身份证号
						face = this.getParameter("face", vv[k++]);; //头像
						sidPhoto1 = this.getParameter("sidPhoto1", vv[k++]);; //身份证照片
						sidPhoto2 = this.getParameter("sidPhoto2", vv[k++]);; //身份证照片
						nameCardPhoto1 = this.getParameter("nameCardPhoto1", vv[k++]);; //名片正面
						nameCardPhoto2 = this.getParameter("nameCardPhoto2", vv[k++]);; //名片背面
					}
					
				}else if(isM()){ //ADMIN后台添加
					
					mmFlag = this.getParameter("mmFlag", vv[k++]); //标识个人还是公司
					k++; //公司AMID
					nickName = this.getParameter("nickName",vv[k++]); //昵称
					sex = this.getParameter("sex", vv[k++]); //性别
					fullName = this.getParameter("fullName", vv[k++]); //姓名
					identityCardNO = this.getParameter("personid", vv[k++]); //身份证号
					//////String phone1 = this.getParameter("phone1", vv[k++]); //手机区号
					loginName = this.getParameter("a2", vv[k++]); //登录手机
					password = this.getParameter("a1", DES.md5("123456")); //初始密码
					
					if(mmFlag.equals("1")){ //公司
						comPhone = this.getParameter("comPhone", vv[k++]); //座机
						companyName = this.getParameter("companyName", vv[k++]); //公司名称
						email = this.getParameter("email", vv[k++]); //公司邮箱
						face = vv[k++]; //头像 dES.decrypt(vv[k++]);
						sidPhoto1 = vv[k++]; //身份证照片
						sidPhoto2 = vv[k++]; //身份证照片
						nameCardPhoto1 = vv[k++]; //名片正面
						nameCardPhoto2 = vv[k++]; //名片背面
					}else{//个人
						comPhone = this.getParameter("comPhone", vv[k++]); //座机
						email = this.getParameter("email", vv[k++]); //个人邮箱
						face = vv[k++]; //头像
						sidPhoto1 = vv[k++]; //身份证照片
						sidPhoto2 = vv[k++]; //身份证照片
					}
				}
			}
			System.out.println("昵称: "+nickName);
			System.out.println("性别: "+sex);
			System.out.println("姓名: "+fullName);
			System.out.println("身份证号: "+identityCardNO);
			System.out.println("登录手机: "+loginName);
			System.out.println("座机: "+comPhone);
			System.out.println("公司名称: "+companyName);
			System.out.println("邮箱: "+email);
			System.out.println("头像: "+face);
			System.out.println("身份证照片: "+sidPhoto1);
			System.out.println("身份证照片反面: "+sidPhoto2);
			System.out.println("名片: "+nameCardPhoto1);
			System.out.println("名片反面: "+nameCardPhoto2);

			BUser auser = null;
			BMMOrgan mm = null;
			if(addFlag){
				if(loginName.equals("-1") || password.length()!=32){
					result = new LWResult(LWResult.FAILURE, "用户名与密码参数异常");
				}else{
					result = HIService.F_checkUser(Integer.parseInt(userGroup), "", loginName, email, identityCardNO);
				}
				if(result.isSuccess()){
					if(companyName.length() > 0){
						//公司是否存在
	                    List<Object> list = dbQuery(String.format(YSSQL.LWMMOrgan_QueryByOrganName, new Object[]{companyName}));
	                    if(list!=null && list.size()>0){
	                    	Object o = list.get(0);
					        mm = new BMMOrgan(userGroup, this.getDBLong(o, "dataid"), this.getDBString(o, "c1"), companyName);
	                    }else{
					        mm = new BMMOrgan(userGroup, -1, "", companyName);
	                    }
					}else{
				        mm = new BMMOrgan(userGroup, -1, "", "");
					}
					//添加用户之前先发送验证邮件
					if(this.getParameter("email").length()>0){
						String verifyEmail = HIService.F_sendEmail(true, this.getParameter("url"), newUserAuid, loginName, null, email);
					    if(!verifyEmail.equals("SUCCESS")){
					    	return new LWResult(LWResult.FAILURE, verifyEmail);
					    }
					}
                    //添加用户
					auser = HIService.addAUser(newUserAuid, userGroup, isM()?1:0, loginName, password, nickName, fullName, "", face, "", sex, comPhone, job, mmFlag, companyName, email, identityCardNO, sidPhoto1, sidPhoto2, nameCardPhoto1, nameCardPhoto2, "", mm);
				}
			}else{
				if(loginName.equals("-1")){
					result = new LWResult(LWResult.FAILURE, "用户名与密码参数异常");
				}else{
					result = HIService.F_checkUser(Integer.parseInt(userGroup), userAuid, loginName, email, identityCardNO);
				}
				if(result.isSuccess()){
                    List<Object> list = dbQuery(String.format(YSSQL.LWUserLogin_QueryByAUID, new Object[]{userAuid}));
                    if(list!=null && list.size()>0){
                    	Object o = list.get(0);
					    mm = new BMMOrgan(userGroup, this.getDBLong(o, "mid"), this.getDBString(o, "amid"), companyName);
						auser = HIService.editAUser(userAuid, userGroup, loginName, nickName, fullName, "", face, "", sex, comPhone, job, mmFlag, companyName, email, identityCardNO, sidPhoto1, sidPhoto2, nameCardPhoto1, nameCardPhoto2, "", mm);
					}else{
						System.out.println("[ServiceDO_AddOrEditUser_M] 用户不存在！");
						result = new LWResult(LWResult.FAILURE, "用户不存在");
					}
				}
			}
			if(auser!=null){
				if(addFlag){
					userAuid = getDBString(auser, "auid");
					String mmAmid = null;
					
					//所属公司需要新增
					if(mm.getMid()==-1 && companyName.length()>0){
						result = F_newCompany(userGroup, userAuid, companyNature, companyIndustry, city, address, abbreviation, companyDetail, foundDate, companyScale); //企业性质：220010其他
						if(result.getResult()==LWResult.SUCCESS){
							mmAmid = result.getInfo();
						}
					}
					if(isM() && userHC) ServiceDO_userCmt_M(userGroup, userAuid, mmAmid);

					if(isM()){
						result = new LWResult(LWResult.SUCCESS, memo+"成功");
					}else{
						result = new LWResult(LWResult.SUCCESS, userAuid);
					}
				}else{
					result = new LWResult(LWResult.SUCCESS, memo+"成功");
				}
				ComBeanMMOrgan.reload();
			}else if(result==null){
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
	        loggerM(ComBeanLogType.TYPE_EDIT, memo);
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	// 返回顾问/客户信息
	private LWResult ServiceDO_userInfo_M(String userGroup){
		LWResult result = null;
		String memo = "账户信息";
		boolean userHC = userGroup.equals(Constants.USER3_MMGROUP);
		boolean userHR = userGroup.equals(Constants.USER2_MMGROUP);
		try{
			StringBuilder s = new StringBuilder("");
			String id = this.getParameter("id");
		    List<Object> list = null;
		    if(userHC) list = dbQuery(String.format(YSSQL.LWHunter_QueryByAUID, new Object[]{id}));
		    if(userHR) list = dbQuery(String.format(YSSQL.LWHR_QueryByAUID, new Object[]{ACE.MM_BizStatus_ADDING,ACE.MM_BizStatus_NEW,id}));
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				if(userHR){
					s.append(this.getDBString(o, "c2")) //公司AMID
					 .append(Constants.SPLITER).append(this.getDBString(o, "c9")) //公司
					 .append(Constants.SPLITER).append(this.getDBString(o, "c4"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c5"))
					 .append(Constants.SPLITER).append(this.getDBString(o, "c6")) //手机号
					 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) //座机
					 .append(Constants.SPLITER).append(this.getDBString(o, "c7")) //邮箱
					 .append(Constants.SPLITER).append(this.getDBString(o, "c16")) //职位;
					 .append(Constants.SPLITER).append(this.getDBString(o, "flag"));//按钮控制
				}else if(userHC){
					int hcType = this.getDBInt(o, "c16");
					if(hcType==1){
						s.append(this.getDBString(o, "c16"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c2")) //公司AMID
						 .append(Constants.SPLITER).append(this.getDBString(o, "c4"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c5"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c33")) //真实姓名
						 .append(Constants.SPLITER).append(this.getDBString(o, "c34"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c6")) //手机号
						 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) //座机
						 .append(Constants.SPLITER).append(this.getDBString(o, "c9")) //所在公司
						 .append(Constants.SPLITER).append(this.getDBString(o, "c7")) //邮箱
						 .append(Constants.SPLITER).append(this.getDBString(o, "face"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c35"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c36"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c37"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c38"))
						 ;
					}else{
						s.append(this.getDBString(o, "c16"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c2")) //公司AMID
						 .append(Constants.SPLITER).append(this.getDBString(o, "c4"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c5"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c33")) //真实姓名
						 .append(Constants.SPLITER).append(this.getDBString(o, "c34"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c6")) //手机号
						 .append(Constants.SPLITER).append(this.getDBString(o, "c8")) //座机
						 //.append(Constants.SPLITER).append(this.getDBString(o, "c9")) //所在公司
						 .append(Constants.SPLITER).append(this.getDBString(o, "c7")) //邮箱
						 .append(Constants.SPLITER).append(this.getDBString(o, "face"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c35"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c36"))
						 //.append(Constants.SPLITER).append(this.getDBString(o, "c37"))
						 //.append(Constants.SPLITER).append(this.getDBString(o, "c38"))
						 ;
					}
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

	//审核顾问/客户信息
	private LWResult ServiceDO_userCmt_M(String userGroup, String userAuid, String mmAmid){
		System.out.println("[MMServiceBean.ServiceDO_userCheck_M]=======================================");
		LWResult result = null;
		String memo = null;
		boolean userHC = userGroup.equals(Constants.USER3_MMGROUP);
		boolean userHR = userGroup.equals(Constants.USER2_MMGROUP);
		if(userHC) memo = "审核顾问信息";
		if(userHR) memo = "审核客户信息";
		try{
			List<String> sqls = new ArrayList<String>();

			int cmtResult = this.getParameter("cmtResult", -1);
			if(cmtResult==0){
				memo += "不通过";
				sqls.add(String.format("update B_UserLogin set c9=%s,c55=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_STATUS_VALID, ACE.USER_CHECK_STATUS_NOPASS, userAuid}));
				sqls.add(this.sqlToZ("B_UserLogin", memo, "c1='"+userAuid+"'"));
				if(userHC){
					sqls.add(this.sqlToZ("B_Hunter", memo, "c1="+userAuid));
					sqls.add(String.format("update B_Hunter set c17=%s,c18=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_NOPASS, ACE.USER_STATUS_VALID, userAuid}));
				}
				if(userHR) {
					sqls.add(this.sqlToZ("B_HR", memo, "c1="+userAuid));
					sqls.add(String.format("update B_HR set c17=%s,c18=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_NOPASS, ACE.USER_STATUS_VALID, userAuid}));

				}
				//sqls.add(P_getActionSQL(actionCode, cid)); //========================================事件
				if(dbExe(sqls)){
					sqls.clear();
					result = new LWResult(LWResult.SUCCESS, memo+"成功");
				}else{
					result = new LWResult(LWResult.FAILURE, memo+"失败");
				}
				return result;
			}
			
			memo += "通过";			
			List<Object> list = dbQuery(String.format("select a.c11 nickName, a.c25 fullName, a.c2 phone from B_UserLogin a where a.c1='%s'", new Object[]{userAuid}));
			if(list==null || list.size()==0){
				result = new LWResult(LWResult.FAILURE, memo+"失败，记录不存在");
				return result;
			}
			Object user = list.get(0);
			String phone = getDBString(user, "phone");
			String nickName = getDBString(user, "nickName");
			String fullName = getDBString(user, "fullName");
			
			sqls.add(this.sqlToZ("B_UserLogin", memo, "c1='"+userAuid+"'"));
			sqls.add(String.format("update B_UserLogin set c9=%s,c55=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_STATUS_VALID, ACE.USER_CHECK_STATUS_CHECKED, userAuid}));
			
			if(userHC){
				sqls.add(this.sqlToZ("B_Hunter", memo, "c1='"+userAuid+"'"));
				sqls.add(String.format("update B_Hunter set c17=%s,c18=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_CHECKED, ACE.USER_STATUS_VALID, userAuid}));
				if(!HIUtil.isEmpty(mmAmid)) sqls.add(String.format("update B_MMOrgan set c6=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.MM_BizStatus_CHECKED, mmAmid}));

				//发送短信
				sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{phone, String.format(ACE.TXT_SMS_HC_CHECKED, new Object[]{fullName}), ACE.SMS_TYPE_LOGIN}));
			}
			if(userHR){
			    //待联系的用户，会生成邀请码
			    if(nickName.indexOf(MARKER_HR_CONTACT_WAITING) > 0){
			    	memo += "[发送邀请码->待联系客户]";
			    	String inviteCode  = RandomCode(2, 6);
				    sqls.add(String.format(YSSQL.LWHR_InviteCode_ADD, new Object[]{inviteCode, "", "", 1, 0, userAuid}));
					
					//发送短信
					sqls.add(String.format(YSSQL.SMS_INSERT, new Object[]{this.getDBString(list.get(0), "phone"), String.format(ACE.TXT_SMS_HR_CHECKED, new Object[]{fullName, inviteCode}), ACE.SMS_TYPE_LOGIN}));
			        sqls.add(this.sqlToZ("B_HR", memo, "c1='"+userAuid+"'"));
			        sqls.add("delete from B_HR where c1='"+userAuid+"'");
			        sqls.add(this.sqlToZ("B_UserLogin", memo, "c1='"+userAuid+"'"));
			        sqls.add("delete from B_UserLogin where c1='"+userAuid+"'");
					
			    }else{
			    	sqls.add(this.sqlToZ("B_HR", memo, "c1='"+userAuid+"'"));
					sqls.add(this.sqlToZ("B_MMOrgan", memo, "c1="+mmAmid));
			    	
					int organFlag = this.getParameter("organFlag", 0);
					String mmBizStatus = organFlag==1 ? ACE.MM_BizStatus_ADDING : ACE.MM_BizStatus_CHECKING;

					if(organFlag==2){
						String organName = this.getParameter("organName");
						//变更HR公司名称
						if(organName.length()>0){
							String newMM_mid, newMM_amid;
							List<Object> organList = dbQuery("select dataid from A_Organ where c5||'['||c1||']'='"+organName+"'");
							if(organList!=null && organList.size()>0){
								organList = dbQuery("select dataid mid,c1 amid from B_MMOrgan where c2='"+organName+"'");
								if(organList!=null && organList.size()>0){
									Object newMM = organList.get(0);
									newMM_mid = this.getDBString(newMM, "mid");
									newMM_amid = this.getDBString(newMM, "amid");
									sqls.add(String.format("update B_HR set c2='%s', c3='%s', c9='%s', uptdate=sysdate where c1='%s'", new Object[]{newMM_amid, newMM_mid, organName, userAuid}));
									sqls.add(String.format("update B_UserLogin set c6='%s', c5='%s', uptdate=sysdate where c1='%s'", new Object[]{newMM_amid, newMM_mid, userAuid}));
									sqls.add(String.format("delete from B_MMOrgan where c1='%s'", new Object[]{mmAmid}));
								}else{
									sqls.add(String.format("update B_HR set c9='%s',uptdate=sysdate where c1='%s'", new Object[]{organName, userAuid}));
									sqls.add(String.format("update B_MMOrgan set c2='%s',uptdate=sysdate where c1='%s'", new Object[]{organName, mmAmid}));
								}
							}else{
								sqls.clear();
								result = new LWResult(LWResult.FAILURE, memo+"失败，公司不存在");
								return result;
							}
						}
					}
					
					sqls.add(String.format("update B_HR set c17=%s,c18=%s,uptdate=sysdate where c1='%s'", new Object[]{ACE.USER_CHECK_STATUS_CHECKED, ACE.USER_STATUS_VALID, userAuid}));
					sqls.add(String.format("update B_MMOrgan set c6=%s,uptdate=sysdate where c1='%s' and c6<>%s", new Object[]{mmBizStatus, mmAmid, ACE.MM_BizStatus_CHECKED}));
			    }
				
		    }
			//sqls.add(P_getActionSQL(actionCode, cid)); //========================================事件
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
	
	// 公司行业及地址
	private LWResult ServiceDO_infoCompanyPart_HR(){
		System.out.println("[MMServiceBean.ServiceDO_infoCompanyPart_HR]=======================================");
		LWResult result = null;
		String memo = "获取公司行业和地址";
		try{
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(YSSQL.LWHROrgan_QueryBy_JobDetail(myamid, mymid, "", "", "", "", "",""));
			if(list!=null && list.size()>0){
				for(Object o: list){
					s.append(this.getDBString(o, "c7")) //行业
					 .append(Constants.SPLITER).append(this.getDBString(o, "c14"));//地址
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
	//查询公司信息
	public LWResult ServiceDO_detailCompany(){
		System.out.println("[MMServiceBean.ServiceDO_infoCompany]=======================================");
		LWResult result = null;
		String memo = "查询公司信息";
		try{
			String hrAmid; 
			if(isHC()){
				hrAmid = this.getParameter("hrAmid");
			}else{
				hrAmid = myamid;
			}
			String objectMode = this.getParameter("objectMode");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(YSSQL.LWHROrgan_QueryBy_JobDetail(hrAmid, "", "", "", "", "", "",""));
			if(list!=null && list.size()>0){
				for(Object o: list){
					
					String marker = "五险一金 领导好 扁平化管理 薪水高 年底旅游 年底百薪";//this.getDBString(o, "");
					String[] markerArray = marker.split(" ");
					for(int i=0; i< markerArray.length;i++){
						markerArray[i] = "<span class=\"btn-default f10 mgl10\" style=\"width: 50px;margin-top: 5px;\">"+markerArray[i]+"</span>";
					}
					String industry = this.getDBDataParam(o, "c7", "", "", "");//行业
					String nature = this.getDBDataParam(o, "c5", "", "", "");//性质c5
					String financing =  this.getDBString(o, "raise");//融资情况
					String size = this.getDBDataParam(o, "c35", "", "", "");//公司规模
					String product = this.getDBString(o, "product");//公司产品
					String competitor = this.getDBString(o, "compete");//竞争对手
					String website = this.getDBString(o, "c40");//公司网站
					String address = this.getDBString(o, "c14");//公司地址
					s.append(String.format(HTML, new Object[]{
							this.getDBString(o, "c32"),//logo
							this.getDBString(o, "c28"),//中文简称
							this.getDBString(o, "c2"),//中文名称
							this.getDBString(o, "c29"),//英文全称
							StringUtils.join(markerArray, ""),//标签
							industry.length()>0 ? "" : "gone",//行业
							industry,//行业
							nature.length()>0 ? "" : "gone",//性质
							nature,//性质
							financing.length()>0 ? "" : "gone",//融资情况
							financing,//性质
							size.length()>0 ? "" : "gone",//公司规模
							size,//公司规模
							product.length()>0 ? "" : "gone",//公司产品
							product,//公司产品
							competitor.length()>0 ? "" : "gone",//竞争对手
							competitor,//竞争对手
							website.length()>0 ? "" : "gone",//公司网站
							website,//公司网站
							website,//公司网站
							address.length()>0 ? "" : "gone",//公司地址
							address,//公司地址							
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
		return result;
	}
	//查询公司信息
	public LWResult ServiceDO_infoCompany_M(){
		System.out.println("[MMServiceBean.ServiceDO_infoCompany_M]=======================================");
		LWResult result = null;
		String memo = "查询公司信息";
		try{
			String id = this.getParameter("id");
			String fullName = this.getParameter("fullName");
			String shortName = this.getParameter("shortName");
			List<Object> list = null;
			
			StringBuilder s = new StringBuilder("");
			if(id!=null&&id.length()>0){
				list = dbQuery(YSSQL.LWHR_NEWMMORGAN_DETAIL(id));
			}
			if(shortName!=null&&shortName.length()>0){
				list = dbQuery(YSSQL.LWHR_AORGAN_Detail(fullName,shortName));
			}
			if(list!=null && list.size()>0){
				for(Object o: list){
					s.append(this.getDBString(o, "fullName"))
					.append(Constants.SPLITER).append(this.getDBString(o, "engName"))//英文全名
					.append(Constants.SPLITER).append(this.getDBString(o, "shortName"))//简称
					.append(Constants.SPLITER).append(this.getDBString(o, "nickName"))//别名
					.append(Constants.SPLITER).append(this.getDBString(o, "type"))//公司类型
					.append(Constants.SPLITER).append(this.getDBString(o, "mother"))//母公司
					.append(Constants.SPLITER).append(this.getDBString(o, "createTime"))//成立时间
					.append(Constants.SPLITER).append(this.getDBString(o, "amount"))//规模
					.append(Constants.SPLITER).append(this.getDBString(o, "introduce"))//公司介绍
					.append(Constants.SPLITER).append(this.getDBString(o, "factory"))//大行业
					.append(Constants.SPLITER).append(this.getDBString(o, "sortFactory"))//细分行业
					.append(Constants.SPLITER).append(this.getDBString(o, "city"))
					.append(Constants.SPLITER).append(this.getDBString(o, "code"))//邮编
					.append(Constants.SPLITER).append(this.getDBString(o, "telephone"))//总机
					.append(Constants.SPLITER).append(this.getDBString(o, "net"))//网址
					.append(Constants.SPLITER).append(this.getDBString(o, "addr1"))
					.append(Constants.SPLITER).append(this.getDBString(o, "logo"))//logo
					.append(Constants.SPLITER).append(this.getDBString(o, "license"))//营业执照
					.append(Constants.SPLITER).append(this.getDBString(o, "addr2"))
					.append(Constants.SPLITER).append(this.getDBString(o, "addr3"));
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
	
	//公司信息注册
	private LWResult ServiceDO_addCompany_HR_HC(){
		LWResult result = null;
		String memo = "公司信息注册";
		try{
			String[] vv = F_formParam();
			int i = 0;
			String abbreviation = vv[i++];
			String companyIndustry = vv[i++];
			String companyNature = vv[i++];
			String companyScale = vv[i++];
			String foundDate = vv[i++];
			String city = vv[i++];
			String address = vv[i++];
			String companyDetail = vv[i++];

			String userGroup = null;
			if(isHR()) userGroup = Constants.USER2_MMGROUP;
			if(isHC()) userGroup = Constants.USER3_MMGROUP;
			result = F_newCompany(userGroup, auid, companyNature, companyIndustry, city, address, abbreviation, companyDetail, foundDate, companyScale);
		    if(result.getResult()==LWResult.SUCCESS){
				ComBeanMMOrgan.reload();
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
		    }
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}

	public static LWResult F_newCompany(String userGroup, String userAuid, String companyNature, String companyIndustry,
			String city, String address, String abbreviation, String companyDetail, String foundDate, String companyScale){
		LWResult result = null;
		String memo = "公司注册";
		boolean userHC = userGroup.equals(Constants.USER3_MMGROUP);
		boolean userHR = userGroup.equals(Constants.USER2_MMGROUP);
		if(userHC) memo = "顾问"+memo;
		if(userHR) memo = "客户"+memo;
		try{
			String companyName = null;
			List<Object> userlist = HIUtil.dbQuery(String.format("select c9 from %s m where m.c1='%s'",new Object[]{userHC? "B_Hunter" : (userHR?"B_HR":""), userAuid}));
			if(userlist!=null && userlist.size()>0){
				for (Object o : userlist) {
					companyName = DBUtil.getDBString(o, "c9");
				}
			}else{
				return new LWResult(LWResult.FAILURE, "用户不存在");
			}
			
			String new_amid = UGID.createUGID();//c1
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWMMOrgan_Add, new Object[]{
					new_amid,//c1 
					companyName,
					HIUtil.isEmpty(companyNature, "220010"),//c5机构性质
					0, //c6 业务状态
					companyIndustry,//行业 c7
					0, //c8 有效状态
					city,//c9
					address,//c14
					abbreviation,//c28
					companyDetail,//c31
					foundDate,// c34
					companyScale,//35
			}));
		    sqls.add(String.format("update B_MMOrgan set c6=%s, c8=%s where c1='%s'", new Object[]{ACE.MM_BizStatus_NEW, ACE.MM_STATUS_NEW, new_amid}));
			sqls.add(String.format(YSSQL.LWUserLogin_UpdateMM, new Object[]{
					new_amid,
					userAuid,
			}));
			sqls.add(String.format(userHC? YSSQL.LWHunter_UpdateMM : (userHR? YSSQL.LWHR_UpdateMM : ""), new Object[]{
					new_amid,
					userAuid,
			}));
						
			if(DaoUtil.dbExe(sqls).getResult()){
				result = new LWResult(LWResult.SUCCESS, new_amid);
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}

	//公司入库
	private LWResult ServiceDO_addCompany_M(){
		LWResult result = null;
		String memo = "公司入库";
		try{
			String id = this.getParameter("id");
			String shortName = this.getParameter("shortName");
			String[] vv = F_formParam();
			String address2 = "";
			String address3 = "";
			int i = 0;
			String organName = vv[i++];//公司全称
			String organName_EN = vv[i++];//英文全称
			String organName_Simp = vv[i++];//简称
			String organName_Other = vv[i++];//别名
			String organType = vv[i++];//公司类型
			String parentOrgan = vv[i++];//关联母公司
			String createDate = vv[i++];//成立时间
			String empNum = vv[i++];//员工数量
			String organDesc = vv[i++];//公司介绍
			String industry = vv[i++];//大行业
			String industry2 = vv[i++];//细分行业
			String city = vv[i++];//城市
			String zipcode = vv[i++];//邮编
			String phone = vv[i++];//总机
			String website = vv[i++];//网址
			String address = vv[i++];//具体地址
			if(vv.length>18){
				address2 = vv[i++];
			} 
			if(vv.length>19){
				address3 = vv[i++];		
			}
			String logo = vv[i++];//公司logo
			String pic = vv[i++];//营业执照

			List<String> sqls = new ArrayList<String>();
			if(shortName!=null&&shortName.length()>0){
				sqls.add(String.format(YSSQL.LWMMOrgan_Edit, new Object[]{
						organName_Simp+"["+organName+"]", //公司名称 c2
						organType, //机构性质 c5
						industry, //行业 c7
						city, //城市 c9
						address, //地址 c14
						organName_Simp, //简称 c28
						organName_EN, //英文名称 c29
						organDesc, //公司简介 c31
						logo, //c32
						pic, //c33
						createDate, //c34
						zipcode, //c38
						phone, //c39
						website, //c40
						address2,//c36
						address3,//c37
						shortName
				}));
				sqls.add(String.format(YSSQL.LWAORGAN_EDIT, new Object[]{
						organName,
						organName_Simp,
						organName_EN,
						organName_Other,
						organType,
						parentOrgan,
						createDate,
						empNum,
						organDesc,
						industry,
						industry2,
						city,
						zipcode,
						phone,
						website,
						address,
						address2,
						address3,
						logo,
						pic,
						shortName
				}));
			}else{
				if(id!=null&&id.length()>0){
					//update
					sqls.add(String.format(YSSQL.LWHR_NEWMMORGAN_EDIT, new Object[]{
							organName,
							organName_Simp,
							organName_EN,
							organName_Other,
							organType,
							parentOrgan,
							createDate,
							empNum,
							organDesc,
							industry,
							city,
							zipcode,
							phone,
							website,
							address,
							address2,
							address3,
							logo,
							pic,
							ACE.MM_BizStatus_CHECKING,
							id
					}));
					sqls.add(String.format(YSSQL.LWHR_NEWAORGAN_ADD, new Object[]{
							organName,
							organName_Simp,
							organName_EN,
							organName_Other,
							organType,
							parentOrgan,
							createDate,
							empNum,
							organDesc,
							industry,
							industry2,
							city,
							zipcode,
							phone,
							website,
							address,
							address2,
							address3,
							logo,
							pic
					}));
				}else{
					sqls.add(String.format(YSSQL.LWHR_NEWMMORGAN_ADD, new Object[]{
							UGID.createUGID(),
							organName,
							organName_Simp,
							organName_EN,
							organName_Other,
							organType,
							parentOrgan,
							createDate,
							empNum,
							organDesc,
							industry,
							city,
							zipcode,
							phone,
							website,
							address,
							address2,
							address3,
							logo,
							pic,
							ACE.MM_BizStatus_CHECKING
					}));
					sqls.add(String.format(YSSQL.LWHR_NEWAORGAN_ADD, new Object[]{
							organName,
							organName_Simp,
							organName_EN,
							organName_Other,
							organType,
							parentOrgan,
							createDate,
							empNum,
							organDesc,
							industry,
							industry2,
							city,
							zipcode,
							phone,
							website,
							address,
							address2,
							address3,
							logo,
							pic,
					}));
				}
			}
			if(dbExe(sqls)){
				ComBeanMMOrgan.reload();
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

	private LWResult ServiceDO_companyList_M(){
		try{
			StringBuilder s = new StringBuilder("");
			String objectMode = this.getParameter("objectMode");
			int queryMK = this.getParameter("queryMK", -1); //新增1，更新2
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
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			int n = 0;
			String firstDataid = null, lastDataid = null;
			List<Long> ids = new ArrayList<Long>();
			if(queryMK==1){
				//新增公司列表
				String[] cols = {"a.c2","a.c28"}; 
				for (String c : cols) {
					list = dbQuery(YSSQL.LWHR_NEWMMORGAN_LIST( ACE.MM_BizStatus_ADDING,c, keyword));
					if(list!=null&&list.size()>0){
						firstDataid = this.getDBString(list.get(0), "sortid");
						for (Object o : list) {
							Long id = this.getDBLong(o, "sortid");
							if(n == maxcount) break;
							lastDataid = this.getDBString(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append(String.format(HTML, new Object[]{
									this.getDBString(o, "amid"), //DATA
									this.getDBString(o, "amid"), //DATA1
									this.getDBString(o, "logo").isEmpty()?"../doc/images/M/face_c%s_s.png":this.getDBString(o, "logo"),//公司Logo
											//this.getDBString(o, "shortName")+"["+this.getDBString(o, "fullName")+"]", // 公司名称
											this.getDBString(o, "fullName"),// 公司名称
											"新增于"+this.getDBDate(o, "time", DATETIME_FORMAT),
							}));
							n++;
						}
					}
				}
			} 
			else if(queryMK==2){
				//更新公司列表
				String[] cols = {"a.c1","a.c2"};
				for (String c : cols) {
					list = dbQuery(YSSQL.LWHR_AORGAN_List(c,keyword));
					if(list!=null && list.size()>0){
						firstDataid = this.getDBString(list.get(0), "sortid");
						for(Object o : list){
							Long id = this.getDBLong(o, "sortid");
							if(n == maxcount) break;
							lastDataid = this.getDBString(o, "sortid");
							if(ids.contains(id)) continue;
							ids.add(id);
							s.append(String.format(HTML, new Object[]{
									this.getDBString(o, "fullName"), //DATA
									this.getDBString(o, "shortName"), //DATA1
									this.getDBString(o, "logo").isEmpty()?"../doc/images/M/face_c%s_s.png":this.getDBString(o, "logo"),//公司Logo
//											this.getDBString(o, "shortName")+"["+this.getDBString(o, "fullName")+"]",
											this.getDBString(o, "fullName"),// 公司名称
											"新增于"+this.getDBDate(o, "time", DATETIME_FORMAT),
							}));
							n++;
						}
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

	private LWResult ServiceDO_AddAgreeMent() {
		LWResult result = null;
		String memo = "添加合同";
		try {
			int k = 0;
			String[] vv = F_formParam();
			String companyName = vv[k++];
			String agreeMentStart = vv[k++];
			String agreeMentStop = vv[k++];
			String agreeMentContent = vv[k++];
			
			String AMID = "";
			String parentdataid = "";
			
			List<Object> list = dbQuery(String.format("select c1,dataid from B_MMORGAN m where m.c2='%s' and m.c6='%s'",new Object[]{companyName,ACE.MM_BizStatus_CHECKING}));
			if(list!=null && list.size()>0){
				for (Object o : list) {
					parentdataid = this.getDBString(o, "dataid");
					AMID = this.getDBString(o, "c1");
				}
			}else{
				return new LWResult(LWResult.FAILURE, memo+"失败");
			}
			
			
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.ADD_AGREEMENT, new Object[]{parentdataid, AMID, agreeMentStart, agreeMentStop, agreeMentContent}));
			sqls.add(String.format("update B_MMORGAN set c6=%s where c1='%s'", new Object[]{ACE.MM_BizStatus_CHECKED, AMID}));
//			sqls.add(String.format("update B_HR set c17=%s where c2='%s'", new Object[]{ACE.USER_CHECK_STATUS_CHECKED, AMID}));
//			sqls.add(String.format("update B_UserLogin set c55=%s where c6='%s'", new Object[]{ACE.USER_CHECK_STATUS_CHECKED, AMID}));
			BizDBResult dbResult = DaoUtil.dbExe(sqls);
			
			if(dbResult.getResult()){
				result = new LWResult(LWResult.SUCCESS, memo+"成功!");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败!");
			}
		} catch (Exception e) {
			result = new LWResult(LWResult.FAILURE, memo+"异常!");
		}
		return result;
	}
	private LWResult ServiceDO_combineHC_M() {
		LWResult result = null;
		String memo = "合并顾问";
		try {
			String remainHCAuid = this.getParameter("val1");
			String oldHCAuid = this.getParameter("val2");
			
			List<String> sqls = new ArrayList<>();			
			sqls.add(this.sqlToZ("B_Candidate", memo, "c2='"+oldHCAuid+"'"));
			sqls.add(String.format("update B_Candidate set (c2,c67,c68)=(select c1,c2,c3 from B_Hunter where c1='%s') where c2='%s'", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("BO_Candidate", memo, "c2='"+oldHCAuid+"'"));
			sqls.add(String.format("update BO_Candidate set (c2,c67,c68)=(select c1,c2,c3 from B_Hunter where c1='%s') where c2='%s'", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("B_Candidate_HCComment", memo, "c2='"+oldHCAuid+"'"));
			sqls.add(String.format("update B_Candidate_HCComment set c2='%s' where c2='%s'", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("BO_Candidate_HCComment", memo, "c2='"+oldHCAuid+"'"));
			sqls.add(String.format("update BO_Candidate_HCComment set c2='%s' where c2='%s'", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("B_Hunter", memo, "c1='"+oldHCAuid+"'"));
			sqls.add(String.format("update B_Hunter set c18=130002 where c1='%s'", new Object[]{oldHCAuid}));
			sqls.add(this.sqlToZ("B_UserLogin", memo, "c1='"+oldHCAuid+"'"));
			sqls.add(String.format("update B_UserLogin set c9=130002 where c1='%s'", new Object[]{oldHCAuid}));
			sqls.add(this.sqlToZ("B_Candidate_Process", memo, "c3='"+oldHCAuid+"' and (c7 is null or c7="+ACE.CANDI_STATUS_ING+")"));
			sqls.add(String.format("update B_Candidate_Process set c3='%s' where c3='%s' and (c7 is null or c7="+ACE.CANDI_STATUS_ING+")", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("B_Candidate_InterView", memo, "c4='"+oldHCAuid+"' and c14=0"));
			sqls.add(String.format("update B_Candidate_InterView set c4='%s' where c4='%s' and c14=0", new Object[]{remainHCAuid, oldHCAuid}));
			sqls.add(this.sqlToZ("B_Action", memo, "c2='"+oldHCAuid+"'"));
			sqls.add(String.format("update B_Action set c2='%s' where c2='%s'", new Object[]{remainHCAuid, oldHCAuid}));
			
			if(dbExe(sqls)){
			    sqls.clear();
				List<Object> list = dbQuery(String.format("select a.c1 cid from B_Candidate a where a.c2='%s'", new Object[]{remainHCAuid}));
			    if(list!=null && list.size()>0){
					String oldHC = this.getSqlValue("select c4||' | '||NVL(c9, 'Freelancer') cc from B_Hunter where c1='"+oldHCAuid+"'", "cc");
					String newHC = this.getSqlValue("select c4||' | '||NVL(c9, 'Freelancer') cc from B_Hunter where c1='"+remainHCAuid+"'", "cc");
			    	for(Object o: list){
						sqls.add(P_getActionSQL("A111019", this.getDBString(o, "cid"), String.format("<span class=\"grey\">当前顾问: </span>%s<br><span class=\"grey\">之前顾问: </span>%s<br>", new Object[]{newHC, oldHC}))); //======================================事件
			    	}
			    	dbExe(sqls);
			    }
			    sqls.clear();
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		return result;
	}
	private LWResult ServiceDO_insertHR_M() {
		Hashtable files = this.getMultipartRequestHandler().getFileElements();
        FormFile formfile = (FormFile) files.get("file1");
        if(files==null || formfile==null || formfile.getFileSize()==0){
            logger.debug("[uploadFormFile] FILE [file1] is null");
        	return null;
        }
    	LWResult result = null;
		List<Object> list = HIUtil.dbQuery(String.format("select dataid,c1,c2,c14,c28 from B_MMOrgan"));
		try {
			InputStream in = formfile.getInputStream();
			return BitchImportUtil.bitchInsertHR(list, in);
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE,"数据导入HR数据表异常");
		}
	}
	private LWResult ServiceDO_uploadHR_M() {
		Hashtable files = this.getMultipartRequestHandler().getFileElements();
        FormFile formfile = (FormFile) files.get("file1");
        if(files==null || formfile==null || formfile.getFileSize()==0){
            logger.debug("[uploadFormFile] FILE [file1] is null");
        	return null;
        }
		LWResult result = null;
		List<Object> list = HIUtil.dbQuery(String.format("select dataid,c1,c2,c14,c28 from B_MMOrgan"));
		//导入之前 清空之前导入的数据
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from b_invitecode where dataid between 3000 and 10000");
		sqls.add("delete from b_Hr where dataid between 3000 and 5000");
		sqls.add("delete from b_userlogin where dataid between 3000 and 5000");
		dbExe(sqls);
		try {
			InputStream in = formfile.getInputStream();
			return BitchImportUtil.bitchImportHR(list, in);
		} catch (Exception e) {
			e.printStackTrace();
			return result = new LWResult(LWResult.FAILURE,"数据导入HR数据表异常");
		}
	}
}
