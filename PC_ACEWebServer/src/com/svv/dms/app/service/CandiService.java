package com.svv.dms.app.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gs.db.dao.DaoUtil;
import com.gs.db.database.BizDBResult;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.LWUtil;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class CandiService extends CandiBase {

	public String CandiCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	
	//简历更新
	public String cvUpdate(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		BizDBResult dr = DaoUtil.dbExe("SP_CandidateCV_Update", new Object[]{cid});
		dbExe(P_getActionSQL("A111015", cid, "")); //=================================事件
		if(dr.getResult()){
			result = new LWResult(LWResult.SUCCESS, "简历更新完毕");
		}else{
			result = new LWResult(LWResult.FAILURE, dr.getInfo());
		}
		return APPRETURN(result);
	}
	//简历完善度检查
	public String cvCommitCheck(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		BizDBResult dr = DaoUtil.dbExe("SP_CandidateCVCheck", new Object[]{cid});
		if(dr.getResult()){
			result = new LWResult(LWResult.SUCCESS, "简历完善度检查完毕");
		}else{
			result = new LWResult(LWResult.FAILURE, "<div style=\"max-height:300px;overflow-y:auto\"><span class=\"red\">未通过原因：</span><br><span>"+dr.getInfo()+"</span></div>");
		}
		return APPRETURN(result);
	}

	/** 检验手机号是否已入库  **/
	public String checkPhoneIsOnline(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		String phone = this.getParameter("phone");
		LWResult result = F_checkPhoneIsOnline(phone);
		return APPRETURN(result);
	}

	/** 检验邮箱是否已入库  **/
	public String checkEmailIsOnline(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		String email = this.getParameter("email");
		LWResult result = F_checkEmailIsOnline(email);
		return APPRETURN(result);
	}

	public String uploadFile(){
		System.out.println("[CandiServiceBean.uploadFile]=======================================");
		if(!ACCESS(true, true, true)) return BConstants.MESSAGE;
		return F_UploadFile_Multi("CANDI");
	}

	public String uploadPic(){
		System.out.println("[CandiServiceBean.uploadPic]=======================================");
		if(!ACCESS(true, true, true)) return BConstants.MESSAGE;
		return F_UploadPic_Multi("CANDI");
	}

    
	//邀请面试查询
	public String invitedCandi(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result=null;
		if(isM()) result = ServiceDO_Candi_invited(); 
		return APPRETURN(result);
	}
	
	//被邀请候选人详细
	public String invitedCandi_detail(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_invitedCandi_detail();
		return APPRETURN(result);
	}
	
	
	private LWResult ServiceDO_invitedCandi_detail() {
		LWResult result = null;
		try {
			String cid = this.getParameter("id");
			List<Object> list = dbQuery(String.format("select a.* from B_T_CANDIDATE_INVITELOG a where a.dataid='%s'", new Object[]{cid}));
			
			StringBuilder s = new StringBuilder();
			if(list!=null&&list.size()>0){
				for (Object o : list) {
					s.append(this.getDBString(o, "c2"))//候选人名称
					.append(Constants.SPLITER).append(this.getDBString(o, "c4"))//HC名称
					.append(Constants.SPLITER).append(this.getDBString(o, "c8"))//HR公司名称
					.append(Constants.SPLITER).append(this.getDBString(o, "c10"))//职位名称
					.append(Constants.SPLITER).append(this.getDBString(o, "c6"))//HR名称
					.append(Constants.SPLITER).append(this.getDBDate(o, "uptdate",DATETIME_FORMAT))
					.append(Constants.SPLITER).append(this.getDBString(o, "c11"));
				}
			}
			return result = new LWResult(LWResult.SUCCESS, s.toString());
		} catch (Exception e) {
			return result = new LWResult(LWResult.FAILURE,"异常");
		}
	}
	
	//修改被邀请人的备注
	public String updateCandi(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		if(isM()) result = ServiceDO_invitedCandi_update();
		return APPRETURN(result);
	}

	//新增候选人
	public String add(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_Add();
		return APPRETURN(result);
	}
	
	//查询候选人简历详情
	public String candiInfo(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);		
		LWResult result = ServiceDO_CandiInfo(detailIndex);
		return APPRETURN(result);
	}
	
	//查询候选人原始简历
	public String candiFile(){
		if(!ACCESS(false, false)) return BConstants.MESSAGE;	
		LWResult result = ServiceDO_CandiCV();
		return APPRETURN(result);
	}	
	//编辑候选人简历详情
	public String editDetail(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);				
		LWResult result = ServiceDO_Candi_EditDetail(detailIndex);
		return APPRETURN(result);
	}
	
	//删除候选人
	public String del(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = ServiceDO_Candi_Del();
		return APPRETURN(result);
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;

	private LWResult ServiceDO_Candi_Add(){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_Add]=======================================");
		LWResult result = null;
		String memo = "添加候选人";

		String cid = this.getParameter("cid");
		if(cid.length()>0){
			result = new LWResult(LWResult.FAILURE, "该候选人已添加，请不要重复操作！");
			return result;
		}
		try{
			String email = this.getParameter("email");
			result = F_checkEmailIsOnline(email);			
			if(result!=null && !result.isSuccess()){
				result = new LWResult(LWResult.FAILURE, memo+"失败,"+result.getInfo());
				return result;
			}
			
			String phone = this.getParameter("phone");
			result = F_checkPhoneIsOnline(phone);
			if(result!=null && !result.isSuccess()){
				result = new LWResult(LWResult.FAILURE, memo+"失败,"+result.getInfo());
				return result;
			}
			
			List<String> sqls = new ArrayList<String>();
			String[] vv = F_formParam();
			cid = UGID.createUGID();
			Object[] oo = new Object[vv.length+9];
			int i = 0;
			int stMode = Integer.parseInt(vv[0]);
			oo[i++] = cid;
			oo[i++] = auid;
			boolean first = true;
			for(String v: vv){
				if(first){
					oo[i++] = ACE.CANDI_STStatus_NOTST;
					oo[i++] = ACE.CANDI_STCheckStatus_NOTST;
					first = false;
					continue;
				}
				oo[i++] = v;
			}
			oo[i++] = myamid;
			oo[i++] = mymid;
			oo[i++] = auid; //c77
			oo[i++] = ComBeanLoginUser.getUserName(auid);//c78
			oo[i++] = ""; //ACE_DataParam.CANDI_CVStatus_CHECKING;
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_Add[1], oo));
		    sqls.add(String.format(YSSQL.LWCandidate_addCVFile, new Object[]{cid}));
		    
		    //BUG725: 新增一条工作经验 add by wxl(需求walle 2017-04-20)
		    if(1==1){
				sqls.add(String.format("insert into B_Candidate_CVWork(dataid,parentDataid,c1,c2,c3,c4,c5,c9,c12,c21,c22,c23) select seqB_Candidate_CVWork.nextval,dataid,c1,130001,'','至今',c41,c42,c44,'%s','%s',sysdate from B_Candidate where c1='%s'", new Object[]{
						auid,
						ComBeanLoginUser.getUserName(auid),
						cid
				}));
		    }

		    //======================================事件
		    sqls.add(String.format("update B_Action set c3=130666,c11=444 where c2=%s and c64='A000002'", new Object[]{auid}));
			sqls.add(P_getActionSQL("A110001", cid, "")); //======================================事件
			if(dbExe(sqls)){
				DaoUtil.dbExe("SP_CandidateCV_Update", new Object[]{cid});
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
				if(stMode==1){
					result = Process_stApply(cid); //======================================流程事件
					if(result.getResult()!=LWResult.SUCCESS){
						result = new LWResult(3, "{\"cid\":\""+cid+"\", \"html\":\"候选人已添加成功，但未能申请入库！<br>"+result.getInfo().replaceAll("\"", "\\\\\"")+"\"}");
					}
				}
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
	private LWResult ServiceDO_Candi_Del(){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_Del]=======================================");
		LWResult result = null;
		String memo = "删除候选人";
		try{
			List<String> sqls = new ArrayList<String>();
			String cid = this.getParameter("cid");
			Object[] oo = new Object[]{cid, "dataid"};
			sqls.add(P_getActionSQL("A110041", cid, "")); //======================================事件
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[1], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[1], oo)));
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[3], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[3], oo)));
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[4], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[4], oo)));
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[5], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[5], oo)));
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[7], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[7], oo)));
			sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[8], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[8], oo)));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[1], oo));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[3], oo));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[4], oo));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[5], oo));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[7], oo));
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[8], oo));
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
	private LWResult ServiceDO_CandiCV(){
		LWResult result = null;
		String memo = "候选人原始简历文件查询";
		try{
			String cid = this.getParameter("cid");
			
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(String.format("select c62 from B_Candidate where c1='%s'", new Object[]{cid}));
			if(list!=null && list.size()>0){
				Object o = list.get(0);
				s.append(this.getDBString(o, "c62")); //dES.decrypt
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.FAILURE, memo+"异常");
		}
		
		return result;
	}
	private LWResult ServiceDO_CandiInfo(int detailIndex){
		LWResult result = null;
		String memo = LWCandidate_CVOtherDetails_MEMO[detailIndex];
		try{
			String cid = this.getParameter("cid");
			
		    boolean hasO2 = this.getSqlIntValue("select count(dataid) counter from BO_Candidate where c1='"+cid+"' and c69=130001", "counter") > 0;
			HashMap<Long, Object> O2_Map = null;
			
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{"", cid}));
		    List<Object> list2 = null;
		    if(hasO2) list2 = dbQuery(String.format(YSSQL.LWCandidate_CVDetails_Query[detailIndex], new Object[]{"O", cid}));
		   
		    boolean hasO2_hcc = isHR() ? false : this.getSqlIntValue("select count(dataid) counter from BO_Candidate_HCComment where c1='"+cid+"' and c3=130001", "counter") > 0;
			if(hasO2){
				O2_Map = new HashMap<Long, Object>();
				if(list2!=null && list2.size()>0){
					for(Object o: list2){
						O2_Map.put(this.getDBLong(o, "dataid"), o);
					}
				}
			}
			
			if(list!=null && list.size()>0){
				Object o2 = null;
				s.append("[");
				for(Object o: list){
					if(hasO2){
						o2 = O2_Map.get(this.getDBLong(o, "dataid"));
						if(o2==null) o2 = (Boolean)true;
					}

					
					s.append("\"");
					switch(detailIndex){
				    case 1:
						boolean powerForHC_checkingCV = powerForHC_checkingCV(getDBString(o, "c82"));
						boolean powerForHC_cmtCVUpdate = powerForHC_cmtCVUpdate(getDBString(o, "c3"), getDBString(o, "c71"), getDBString(o, "c82"));

						s.append(this.getDBString_JSON(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c8", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c9", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c13", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c17", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c10", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c22", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c20", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c21", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c23", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c15", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c16", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append( (isHC() && powerForHC_checkingCV) ? "2" : (isHC() && powerForHC_cmtCVUpdate ? "1":"0"));	
				    	break;
				    case 2:
				    	break;
				    case 3:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c4", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c5", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c6", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c7", "", "", "", o2, 2));
				    	break;
				    case 4:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c4", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c6", "", "", "", o2, 2));
				    	break;
				    case 5:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c4", "", "", "", o2, 2));
				    	break;
				    case 6:
				    	break;
				    case 7:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", o2, 2))//开始时间
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c4", "", "", "", o2, 2))//结束时间
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c5", "", "", "", o2, 2))//公司名称
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c7", "", "", "", o2, 2))//行业
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c19", "", "", "", o2, 2))//是否有多段工作经历
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c8", "", "", "", o2, 2))//部门
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c9", "", "", "", o2, 2))//职位
						 .append(Constants.SPLITER).append(this.getDBString(o, "c10").startsWith("227") ? this.getDBString_JSON_BO(o, "c10", "", "", "", o2, 2) :"")//职级
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c16", "", "", "", o2, 2, true));//工作职责
				    	break;
				    case 8:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c4", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c5", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c6", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c7", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c8", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c9", "", "", "", o2, 2));
				    	break;
					}
					s.append("\"");
					s.append(",");
				}
				s.deleteCharAt(s.length()-1);
				s.append("]");
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

	private LWResult ServiceDO_Candi_EditDetail(int index){
		System.out.println("[CandiServiceBean.ServiceDO_Candi_EditDetail]=======================================");
		LWResult result = null;
		String memo = LWCandidate_CVOtherDetails_MEMO[index];
		try{
			List<String> sqls = new ArrayList<String>();
			String cid = this.getParameter("cid");
			String[] vv = F_formParam();
			String[] vvc = F_formContent();
			List<String[]> clobArray = new ArrayList<String[]>();
			
			int N = this.getParameter("N", 0);
			Object[] oo = new Object[vv.length+(index==7?5:4)];
			sqls.add(String.format(YSSQL.LWCandidate_CVDetails_ResetState[index], new Object[]{cid}));
			if(N>0){
				int detailID = -1;			
				int i = 0;
				boolean first = true;
				int n = vv.length/N;
				int m = 0;
				String param = null;
				for(int k=0; k<N; k++){
					i = 0;
					detailID = -1;
					first = true;
					for(;m<vv.length; m++){
						param = vv[m];
						if(n>1 && i==n-1) break;
						
						if(first){
							if(param.length()>0){
								detailID = Integer.parseInt(param); 
							}
							first = false;
							continue;
						}
						oo[i++] = param;
					}
					oo[i++] = auid; //c10
					oo[i++] = ComBeanLoginUser.getUserName(auid);//c11
					if(index==7) oo[i++] = k+1;
					oo[i++] = cid; //c1
					oo[i++] = detailID;
					
					if(index!=1 && detailID==-1){
						System.out.println(index+"~~~~~~~~~~~~~~~~~~"+String.format(YSSQL.LWCandidate_CVDetails_Add[index], oo));
						sqls.add(String.format(YSSQL.LWCandidate_CVDetails_Add[index], oo));
					}else if(detailID > 0){
						System.out.println(index+"~~~~~~~~~~~~~~~~~~"+String.format(YSSQL.LWCandidate_CVDetails_Edit[index], oo));
    					sqls.add(this.sqlToZ(YSSQL.LWCandidate_CVDetails_TableName[index], memo, String.format(YSSQL.LWCandidate_CVDetails_toZWhere[index], new Object[]{cid, detailID})));
						sqls.add(String.format(YSSQL.LWCandidate_CVDetails_Edit[index], oo));
					}

    				if(index==7){
    					if(vvc.length>0){
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c16");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c16");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c16");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c16");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c16");
							clobArray.add(new String[]{"B_Candidate_CVWork", "c1='"+cid+"' and c30="+(k+1), "c16", vvc[k]});//工作职责
    					}
    				}

				}
				memo = memo + (index!=1 && detailID==-1?"添加" :"编辑");
			}else{
				memo = "编辑";
			}

			if(dbExe(sqls)){
				if(clobArray.size()>0){
					for(String[] cc: clobArray){
						DaoUtil.dbExe(cc[0], cc[1], cc[2], cc[3]);
					}
				}
				result = new LWResult(LWResult.SUCCESS, memo+"成功");
			}else{
				result = new LWResult(LWResult.FAILURE, memo+"失败");
			}

	        loggerM(ComBeanLogType.TYPE_ADD, memo);
		}catch(Exception e){
			System.out.println("[Exception] index="+index);
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return result;
	}

	/** 检验手机号是否已入库  **/
	private LWResult F_checkPhoneIsOnline(String phone){
		LWResult result = null;
		try{
			String status = ACE.CANDI_STStatus_NOTST; //未入库的否定
			if(!HIUtil.isEmpty(phone)){
				List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_CheckOnlineByPhone, new Object[]{status, phone}));
				if(list!=null && list.size()>0){
					
					Object o = list.get(0);
					String d = this.getDBDate(o, "c81", "yyyy-MM-dd HH:mm:ss");
					String ndate = LWUtil.toChatTime(d);
					String s = String.format(ACE.TXT_CANDIDATE_RECHECK, new Object[]{ //update by wxl 2017-04-24【Martin】
							this.getDBDataParam(o, "c3", "", "", ""),
							this.getDBString(o, "hcName"),
							ndate
					}); 
					result = new LWResult(LWResult.FAILURE, s);
				}else{
					result = new LWResult(LWResult.SUCCESS, "该手机号未入库");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return result;
	}

	/** 检验邮箱是否已入库 LWCandidate_CheckOnlineByEmail **/
	private LWResult F_checkEmailIsOnline(String email){
		LWResult result = null;
		try{
			String status = ACE.CANDI_STStatus_NOTST; //未入库的否定
			if(!HIUtil.isEmpty(email)){
				List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_CheckOnlineByEmail, new Object[]{status, email}));
				if(list!=null && list.size()>0){
					Object o = list.get(0);
					String d = this.getDBDate(o, "c81", "yyyy-MM-dd HH:mm:ss");
					String ndate = LWUtil.toChatTime(d);
					String s = String.format(ACE.TXT_CANDIDATE_RECHECK, new Object[]{ //update by wxl 2017-04-24【Martin】
							this.getDBDataParam(o, "c3", "", "", ""),
							this.getDBString(o, "hcName"),
							ndate
					});
					result = new LWResult(LWResult.FAILURE, s);
				}else{
					result = new LWResult(LWResult.SUCCESS, "该邮箱未入库");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return result;
		
	}
	
	private LWResult ServiceDO_invitedCandi_update() {
		LWResult result = null;
		String memo = "修改候选人备注";
		try {
			String[] vv = F_formParam();
			String remark = this.getParameter("remark",vv[vv.length-1]);
			String cid = this.getParameter("id");
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWCandidate_status_UpdateByC, new Object[]{remark,1,auid,ComBeanLoginUser.getUserName(auid),cid}));
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
	private LWResult ServiceDO_Candi_invited() {
		System.out.println("[CandiService.ServiceDO_Candi_invited]=======================================");
		try {
			String name = this.getParameter("keyword");
			String objectMode = this.getParameter("objectMode");
			/*
			String FIRSTID = this.getParameter("i0");
			String LASTID = this.getParameter("i1");
			this.page = 1;//有lastid, page永远为1//////////////////////////////////this.getParameter("i8", 1);*/
			String FIRSTID = "";
			String LASTID = "";
			this.page = this.getParameter("i8", 1);
			int maxcount = this.getParameter("i9", 50);
			pageFlag = 1; //使用分页
			pageRow = maxcount;
			int n = 0;

			StringBuilder s = new StringBuilder("");
			String HTML = ComBeanAPPModule.getHtml(objectMode);
			List<String> htmls = LWUtil.HTML_getFormatHTMLs(HTML);
			
			List<Object> list = dbQuery(YSSQL.LWCandidate_Invited_QueryByC("", "", "", "", name));
			String firstDataid = null, lastDataid = null;
			if(list!=null&&list.size()>0){
				for (Object o : list) {
					lastSortid = this.getDBString(o, "sortid");
					String status = "";
					if(this.getDBInt(o, "c12")==0){
						status = "未处理";
					}else{
						status = "已处理";
					}
					String logoImg = "";
					List<Object> logo = dbQuery(YSSQL.LWHROrgan_QueryBy_JobDetail("", "", "", "", "", "", "", this.getDBString(o, "c5")));
					if(logo!=null && logo.size()>0){
						for (Object log : logo) {
							logoImg = this.getDBString(log, "logo");
						}
					}
					s.append(String.format(htmls.get(1), new Object[]{
							this.getDBString(o, "sortid"),
							logoImg.equals("") ? "../doc/images/M/face_c_s.png" : logoImg,
							this.getDBString(o, "c8") + "|" + this.getDBString(o, "c6"),//hr公司名称|hr名称
							this.getDBString(o, "c10").equals("") ? "--" : this.getDBString(o, "c10"),//招聘职位
							this.getDBString(o, "c2"),//候选人名称
							this.getDBString(o, "c4"),//顾问名称
							this.getDBDate(o, "uptdate",DATETIME_FORMAT),
							status//处理状态
					}));
					n++;
				}
			}
			String rtnString = String.format(htmls.get(0), s.toString());
			if(n>0) return new LWResult(LWResult.SUCCESS, rtnString);
			return new LWResult(LWResult.FAILURE, "无记录");
		} catch (Exception e) {
			return new LWResult(LWResult.FAILURE, "异常");
		}
	}
	

}
