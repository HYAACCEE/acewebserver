package com.svv.dms.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gs.db.dao.DaoUtil;
import com.gs.db.database.BizDBResult;
import com.svv.dms.app.ACE;
import com.svv.dms.app.ComBeanLoginUser;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class CandiCommentService extends CandiBase {

	public final static String[] LWCandidate_HCComment_MEMO = 
			new String[]{"", "工作经验", "工作期望","意向公司","屏蔽公司", "技能标签", "工作总结与整体印象"};

	public String CandiCommentCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiCommentMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiCommentBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String CandiCommentSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	//顾问面评更新
	public String commentUpdate(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		String hcid = isHC()? auid:this.getParameter("hcid");
		BizDBResult dr = DaoUtil.dbExe("SP_CandidateCV_Update", new Object[]{cid});
		P_doProcess("220004", cid); //=========================================流程事件
		if(dr.getResult()){
			result = new LWResult(LWResult.SUCCESS, "顾问面评更新完毕");
		}else{
			result = new LWResult(LWResult.FAILURE, dr.getInfo());
		}
		return APPRETURN(result);
	}
	//顾问面评完善度检查
	public String commentCommitCheck(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		String hcid = this.getParameter("hcid", auid);
		int commitFlag = this.getParameter("commitFlag", 0); //是否提交顾问面评
		BizDBResult dr = DaoUtil.dbExe("SP_HCCommentCheck", new Object[]{cid, hcid});
		if(commitFlag==1){
			if(dr.getResult()){
				result = P_doProcess("220005", cid); //=========================================流程事件
			}else{
				result = new LWResult(LWResult.FAILURE, "<div style=\"max-height:300px;overflow-y:auto\"><span class=\"red\">未通过原因：</span><br><span>"+dr.getInfo()+"</span></div>");
			}
		}else{
			result = new LWResult(LWResult.SUCCESS, "顾问面评已保存");
		}
		return APPRETURN(result);
	}
	//顾问面评上线
	public String commentOnline(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		LWResult result = null;
		String cid = this.getParameter("cid");
		String hcid = this.getParameter("hcid", auid);
	    List<Object> list_cp = HIUtil.dbQuery(YSSQL.LWCandidate_QueryByCID("", cid, hcid, ""));
		if(list_cp!=null && list_cp.size()>0){
			Object o = list_cp.get(0);
			String stStatus = this.getDBString(o, "stStatus");
			result = P_doProcess(stStatus.equals(ACE.CANDI_STStatus_PREST)?"220006":"220026", cid); //=========================================流程事件
		}else{
			result = new LWResult(LWResult.FAILURE, "候选人不存在");
		}
		return APPRETURN(result);
	}
	//查询顾问面评详情
	public String commentInfo(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		int detailIndex = this.getParameter("index", 0);		
		LWResult result = ServiceDO_CommentInfo(detailIndex);
		return APPRETURN(result);
	}	
	//编辑顾问面评详情
	public String edit(){
		if(!ACCESS(true, true)) return BConstants.MESSAGE;
		int index = this.getParameter("index", 0);				
		LWResult result = ServiceDO_Comment_Edit(index);
		return APPRETURN(result);
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019891007159376L;

	private LWResult ServiceDO_CommentInfo(int index){
		LWResult result = null;
		String memo = LWCandidate_HCComment_MEMO[index];
		try{
			String cid = this.getParameter("cid");
			String hcid = this.getParameter("hcid", auid);
			
		    boolean hasO2 = isHC() ? false : this.getSqlIntValue("select count(dataid) counter from BO_Candidate_HCComment where c1='"+cid+"' and c2='"+hcid+"' and c3=130001", "counter") > 0;
			
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_HCComment_Query[index], new Object[]{"", cid, hcid}));
		    List<Object> list2 = null;
		    if(hasO2) list2 = dbQuery(String.format(YSSQL.LWCandidate_HCComment_Query[index], new Object[]{"O", cid, hcid}));
			HashMap<Long, Object> O2_Map = null;
			if(hasO2){
				O2_Map = new HashMap<Long, Object>();
				if(list2!=null && list2.size()>0){
					for(Object o: list2){
						O2_Map.put(this.getDBLong(o, "dataid"), o);
					}
				}
			}
			
		    int i = 0;
			if(list!=null && list.size()>0){
				Object o2 = null;
				s.append("[");
				for(Object o: list){
					if(index == 1){
				    	if(i == 3)break;
						i++;
					}
					
					if(hasO2){
						o2 = O2_Map.get(this.getDBLong(o, "dataid"));
						if(o2==null) o2 = (Boolean)true;
					}
					s.append("\"");
					
					System.out.println("[CandiCommentServiceBean.info--linda]======================================="+this.getDBString_JSON_BO(o, "c8", "", "", "", o2, 2) + this.getDBString_JSON_BO(o, "c9", "", " | ", "", o2, 2));
					switch(index){
				    case 1:
						String workWeight_value = getDBString_JSON(o, "c17");
						String workWeight_str = workWeight_value;
						if(o2!=null){
							if(o2 instanceof Boolean){
								if((Boolean)o2){
									workWeight_str = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{"", workWeight_value});
								}
							}else{
								String checkValue = getDBString_JSON(o2, "c17");
								if(!checkValue.equals(workWeight_value)){
									workWeight_str = String.format(ACE_CHECK_CV_COLUMN_INPUT, new Object[]{checkValue, workWeight_value});
								}
							}
						}
				    	
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c3", "", "", "", null, 2) + this.getDBString_JSON_BO(o, "c4", "", " - ", "", null, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c5", "", "", "", null, 2) + this.getDBString_JSON_BO(o, "c8", "", " | ", "", null, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c9", "", "", "", null, 2) + (this.getDBString(o, "c10").startsWith("227") ? this.getDBDataParam_BO(o, "c10", "", "(", ")", o2, 2) : this.getDBString_BO(o, "c10", "", "(", ")", o2, 2))) //职位+职级
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c18", "", "", "", o2, 2)) //公司介绍
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c7", "", "", "", o2, 2)) //行业
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c20", "", "", "", o2, 2)) //职位类型
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c11", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c12", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c13", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c14", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c15", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c24", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(workWeight_str) //工作权重
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c29", "", "", "", o2, 2, true)) //工作业绩
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c25", "", "", "", o2, 2)) //离职原因
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c26", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c27", "", "", "", o2, 2));
				    	break;
				    case 2:
						s.append(this.getDBString_JSON_BO(o, "c8", "", "", "", o2, 2) + this.getDBString_JSON_BO(o, "c9", "", " | ", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c46", "", "", "", o2, 2))//职位
						 .append(Constants.SPLITER).append(this.getDBString(o, "c47").startsWith("227") ? this.getDBString_JSON_BO(o, "c47", "", "", "", o2, 2) :"")//职级
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c48", "", "", "", o2, 2))//行业
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c49", "", "", "", o2, 2))//期望城市
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c51", "", "", "", o2, 2))//期望年薪
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c37", "", "", "", o2, 2));//是否接受Contractor
				    	break;
				    case 3:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c63", "", "", "", o2, 2));
				    	break;
				    case 4:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c64", "", "", "", o2, 2));
				    	break;
				    case 5:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c52", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c53", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c54", "", "", "", o2, 2));
				    	break;
				    case 6:
						s.append(this.getDBString(o, "dataid"))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c20", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c21", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c22", "", "", "", o2, 2))						 
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c10", "", "", "", o2, 2)) //婚姻状况
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c14", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c13", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c15", "", "", "", o2, 2))
						 .append(Constants.SPLITER).append(this.getDBString_JSON_BO(o, "c16", "", "", "", o2, 2))
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

	private LWResult ServiceDO_Comment_Edit(int index){
		System.out.println("[CandiCommentServiceBean.ServiceDO_Edit]=======================================");
		LWResult result = null;
		String memo = LWCandidate_HCComment_MEMO[index];
		try{
			List<String> sqls = new ArrayList<String>();
			String cid = this.getParameter("cid");
			String[] vv = F_formParam();
			String[] vvc = F_formContent();
			List<String[]> clobArray = new ArrayList<String[]>();
			
			int N = this.getParameter("N", 0);
            if(N>0){
    			int detailID = -1;			
    			int i = 0;
    			boolean first = true;
    			int n = vv.length/N;
    			Object[] oo = new Object[n+4];
    			int m = 0;
    			int paramNum = 0;
    			String param = null;
    			for(int k=0; k<N; k++){
    				i = 0;
    				paramNum = 0;
    				detailID = -1;
    				first = true;
    				for(;m<vv.length; m++){
    					param = vv[m];
    					if(n>1 && paramNum==n) break;
    					paramNum++;
    					if(first){
    						if(param.length()>0){
    							detailID = Integer.parseInt(param); 
    						}
    						first = false;
    						continue;
    					}
    					if(index==1 && paramNum<5){
    						continue;
    					}
    					oo[i++] = param;
    				}
    			    if(index==6) {
    			    	List<Object> list = dbQuery(String.format(YSSQL.LWCandidate_HCComment_Query[index], new Object[]{"", cid, auid}));
    			        if(list!=null && list.size()>0){
    			        	detailID = this.getDBInt(list.get(0), "dataid");
    			        }
    			    }

    				oo[i++] = auid; //c10
    				oo[i++] = ComBeanLoginUser.getUserName(auid);//c11
    				oo[i++] = cid; //c1
    				oo[i++] = detailID;
    				if(detailID==-1 && index==6){
    					System.out.println(index+"~~~~~~~~~~~~~~~~~~"+String.format(YSSQL.LWCandidate_CVComment_Add[index], oo));
    					sqls.add(String.format(YSSQL.LWCandidate_CVComment_Add[index], oo));
    					
    				}else{
    					System.out.println(index+"~~~~~~~~~~~~~~~~~~"+String.format(YSSQL.LWCandidate_CVComment_Edit[index], oo));
    					sqls.add(this.sqlToZ(YSSQL.LWCandidate_HCComment_TableName[index], memo, String.format(YSSQL.LWCandidate_HCComment_toZWhere[index], new Object[]{cid, detailID})));
    					sqls.add(String.format(YSSQL.LWCandidate_CVComment_Edit[index], oo));
    				}
    				
    				if(index==1){
    					if(vvc.length>0){
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c29");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c29");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c29");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c29");
							System.out.println(index+"~~~~~~~~~~~~~~~~~~修改CLOB字段 c29");
							clobArray.add(new String[]{"B_Candidate_CVWork", "c1='"+cid+"' and dataid="+detailID, "c29", vvc[k]});//工作业绩
    					}
    				}
    			}
    			if(detailID==-1 && index==6){
    				memo = memo + "添加";
    			}else{
    				memo = memo + "编辑";
    			}
            }
			sqls.add(String.format("update B_Candidate set c85=1, c86=sysdate where c1='%s'", new Object[]{cid}));
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
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, memo+"异常");
		}
		return result;
	}
}
