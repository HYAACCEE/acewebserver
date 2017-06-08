package com.svv.dms.app;

import java.util.HashMap;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanI_DataTable;
import com.svv.dms.web.dao.SQL_0;
import com.svv.dms.web.entity.I_DataTable;
import com.svv.dms.web.util.HIUtil;

public class YSSQL extends SQL_0{

	public final static String LWServer_Query = "select c1 serverID, c2 url, c3 realPath from IS_Server where c4=130001";
	public final static String SMS_INSERT = "insert into T_Sms_Ing(dataid,c1,c2,c3,c4,c5,c6) values(seqT_Sms_Ing.Nextval,'10001313','%s','%s',0,'',%d)";
	public final static String PUSHSMS_BySYS___ = "insert into T_PushSms_Ing(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9) values(seqT_PushSms_Ing.Nextval,'20001313','%s','%s',0,'',%d,%d,'%s','')";
	public final static String PUSHSMS_ByUSER = "insert into T_PushSms_Ing(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9) select seqT_PushSms_Ing.Nextval,c1,'%s',decode(c4,1,'"+Constants.USER1_MMGROUP_DESC+"'||c11,2,c11||'"+Constants.USER2_MMGROUP_DESC+"',3,c11||'"+Constants.USER3_MMGROUP_DESC+"',c11)||'%s',0,'',%d,%d,'%s','%s' from B_UserLogin where c1='%s'";
	public final static String PUSHSMS_RESET = "update T_PushSms_Ing set c4=0 where c4=22 and c2='%s'";
	
	/** lw app **/
	public final static String SEQ_Next = "select seq%s.nextval id from dual";
	public final static String LWUserLogin_CHECKPASS_ByAUID = "select count(*) counter from B_UserLogin a where dataStatus<>-2 and c1='%s' and c3='%s' and c9=130001";
	public final static String LWUserLogin_LOGIN_ByPHONE = "select a.c1 auid, a.c2 phone, a.c4 mmGroup, a.c5 mid, a.c6 amid, m.c2 mmName, NVL(m.c5,-1) mmType, a.c11 nickName, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, a.c25 fullName, a.c32 email, NVL(a.c55,0) power from B_UserLogin a, B_MMOrgan m where a.c6=m.c1(+) and a.c2='%s' and a.c3='%s' and a.c4='%s' and a.c9=130001";
	public final static String LWUserLogin_LOGIN_ByEmail = "select a.c1 auid, a.c2 phone, a.c4 mmGroup, a.c5 mid, a.c6 amid, m.c2 mmName, NVL(m.c5,-1) mmType, a.c11 nickName, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, a.c25 fullName, a.c32 email, NVL(a.c55,0) power from B_UserLogin a, B_MMOrgan m where a.c6=m.c1(+) and a.c32='%s' and a.c3='%s' and a.c4='%s' and a.c9=130001";
	public final static String LWUserLogin_LOGIN_ByOPENID = "select a.c1 auid, a.c2 phone, a.c4 mmGroup, a.c5 mid, a.c6 amid, m.c2 mmName, NVL(m.c5,-1) mmType, a.c11 nickName, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, a.c25 fullName, a.c32 email, NVL(a.c55,0) power from B_UserLogin a, B_MMOrgan m where a.c6=m.c1(+) and a.c19='%s' and a.c9=130001";
	public final static String LWUserLogin_LOGIN_ByAUID = "select a.c1 auid, a.c2 phone, a.c4 mmGroup, a.c5 mid, a.c6 amid, m.c2 mmName, NVL(m.c5,-1) mmType, a.c11 nickName, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, a.c25 fullName, a.c32 email, NVL(a.c55,0) power from B_UserLogin a, B_MMOrgan m where a.c6=m.c1(+) and a.c1='%s' and a.c9=130001";
	public final static String LWUserLogin_QueryByAUID = "select a.c1 auid, a.c2 phone, a.c4 mmGroup, a.c5 mid, a.c6 amid, m.c2 mmName, NVL(m.c5,-1) mmType, a.c11 nickName, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, a.c25 fullName, a.c32 email, NVL(a.c55,0) power from B_UserLogin a, B_MMOrgan m where a.c6=m.c1(+) and a.c1='%s'";
	public final static String LWUserLogin_EXIST_PHONE = "select c1 auid, c3 password, c5 mid, c6 amid, c9 state from B_UserLogin a where c2='%s' and c4='%s' and a.c9=130001";
	public final static String LWUserLogin_EXIST_NICKNAME = "select c1 auid, c5 mid, c6 amid, c9 state from B_UserLogin a where c11='%s' and a.c9=130001";
	public final static String LWUserLogin_EXIST_EMAIL = "select c1 auid, c5 mid, c6 amid, c9 state from B_UserLogin a where c32='%s' and c4='%s' and a.c9=130001";
	public final static String LWUserLogin_EXIST_SIDNUM = "select c1 auid, c5 mid, c6 amid, c9 state from B_UserLogin a where c44='%s' and c4='%s' and a.c9=130001";
	public final static String LWUserLogin_ADD = "insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c25,c12,c14,c17,c19,c31,c32,c33,c36,c40,c44,c55) values(seqB_UserLogin.nextval,'%s','%s','%s',%s,%s,'%s',130000,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','11','%s','%s',0)";	public final static String LWUserLogin_ADDByMM = "insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c29,c12,c14,c17,c19,c20) select seqB_UserLogin.nextval,'%s' auid, c13 phone, '%s' password, %s mmGroup, dataid, c1 amid, 130001, c12, '', c2 mmName, '', '', '', '',%s from B_MMorgan where c13='%s'";
	public final static String LWUserLogin_LOGON_SET = "update B_UserLogin set c22=c22+1,c7=sysdate,c37=NVL(c37,sysdate) where c1='%s'";
	public final static String LWUserLogin_LOGOUT = "update B_UserLogin set c8=0,c10='',uptdate=sysdate where c1='%s'";
	public final static String LWUserLogin_Information = "update B_UserLogin set c32='%s',c33='%s',c31='%s' where c1='%s'";
	public final static String LWUserLogin_UpdatePSWD = "update B_UserLogin set c3='%s' where c1='%s'";
	public final static String LWUserLogin_EditFace = "update B_UserLogin set c12='%s' where c1='%s'";
	public final static String LWUserLogin_EditNickName = "update B_UserLogin set c11='%s' where c1='%s'";
	public final static String LWUserLogin_Recheck = "update B_UserLogin set c21=3,c42='%s',c43='%s',c44='%s',c45='%s',c46='%s',c47='%s',c48='%s',c49='%s',c54=sysdate where c1='%s'";
    
	
	//登录用户状态
	public final static String LWMMDataVersion_QueryByAMID = "select c.c4 VN_dishes,c.c5 VN_goods,c.c6 VN_mgoods,c.c7 VN_order from B_MMDataVersion c where c.c1=%s";
	public final static String LWMMUser_QueryByAUID_AMID = "select a.c9 userStatus, NVL(a.c21,0) status,a.c4 mmGroup,NVL(a.c55,-1) bizStatus, a.c11||a.c2 userName, a.c11 nickname, a.c12 face, NVL(a.c20,-1) userRole, a.c25 fullName, a.c36 noticeFlag from B_UserLogin a,B_MMOrgan b where a.c5=b.dataid(+) and a.c1='%s' and b.c1(+)='%s'";	public final static String LWMMUser_QueryByPHONE = "select (select count(a.dataid) from B_UserLogin a where a.c2='%s') c1, (select count(b.dataid) from B_MMOrgan b where b.c13='%s') c2 from dual";
	public final static String LWDotUser_QueryByAUID = "select a.c9 userStatus,b.c2 mmGroup from YS_DotUser a, YS_DotRole b where a.c4=b.dataid and a.c1='%s'";
//	public final static String LWUSER_QueryByAUID = "select a.c1 auid, a.c2 phone, a.c9 userStatus,a.c4 mmGroup, a.c5 mid, a.c6 amid, a.c11 nickname, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, NVL(a.c20,0) status, a.c25 fullName, a.c30 goodsNum, a.c31 buyerNum, a.c32 supplyNum, a.c33 addressNum, b.c2 mmName from B_UserLogin a,B_MMOrgan b where a.c5=b.dataid(+) and a.c1='%s'";
//	public final static String LWUSER_QueryByPHONE = "select a.c1 auid, a.c2 phone, a.c9 userStatus,a.c4 mmGroup, a.c5 mid, a.c11 nickname, a.c12 face, a.c13 signing, a.c14 sex, a.c15 province, NVL(a.c21,0) status, a.c25 fullName, a.c30 goodsNum, a.c31 buyerNum, a.c32 supplyNum, a.c33 addressNum from B_UserLogin a where a.c2='%s'";
	//查询公司
	public final static String LWMMOrgan_QueryByOrganName = "select dataid,c1,c2 from B_MMOrgan where c2='%s'";
	public final static String LWMMOrgan_QueryByOrganSimpleName = "select dataid,c1,c2,c28 from B_MMOrgan where c28='%s'";
	public final static String LWAOrgan_QueryByOrganName = "select dataid,c1,c5 from A_Organ where c5||'['||c1||']'='%s'";
	public final static String LWMMOrgan_QueryByAMID = "select * from B_MMOrgan where c1='%s'";
	//添加公司 by AOrgan
	public final static String LWMMOrgan_AddByAOrgan = "insert into ";
	
	//app模块
	public final static String LWAppModule_QueryByRole = "select a.c1 moduleID, a.c2 moduleType, a.c3 moduleSeq from IS_AppModule a, IS_AppRoleModule c where a.c1=c.c1 and c.parentDataid='%s' and a.c4=130001 order by a.c3";
	public final static String LWAppModuleDetail_QueryByRole = "select a.c1 moduleID, a.c2 moduleType, a.c3 moduleSeq, b.dataid id,b.c2 mdTitle,b.c3 mdType,b.c4 mdImg,b.c5 mdParam,b.c6 mdParam2,b.c7 mdParam3,b.c8 mdAction, b.c10 mdSeq from IS_AppModule a, IS_AppModuleDetail b, IS_AppRoleModule c where a.c1=b.c1 and a.c1=c.c1 and c.parentDataid='%s' and a.c4=130001 and b.c9=130001 order by a.c3,b.c10";
	
	// 查询用户资料
	public final static String LWUser_QueryByAUID(String auid){
		StringBuilder rtn = new StringBuilder("select c.*")
				.append(" from B_UserLogin c where 1=1 ")
				.append(getWhere_number("c.c1", auid));
		return rtn.toString();
	}	
	public final static String LWUser_QueryByC(String mmGroup, String keyword, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select c.dataid sortid, c.dataid, c.c1 auid, c.c4 mmGroup, c.c11 nickName, c.c12 face, c.c13 signing, c.c27 starLevel ")
				.append(" from B_UserLogin c where 1=1 ")
				.append(getWhere_number("c.c4", mmGroup))
				.append(getWhere_like("c.c11", keyword))
				.append(getWhere_startNumber("c.dataid", mindataid, false))
				.append(getWhere_endNumber("c.dataid", maxdataid, false))
				.append(" order by sortid desc");
		return rtn.toString();
	}
	public final static String LWUser_QueryByNear = "select c1 auid, '1111' power, c4 nickname, c5 sex, c11 face, c19 signing from B_UserLogin";
	public final static String LWUserFriend_QueryByC(String auid, int friendMode, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select a.dataid sortid, a.dataid, a.c3 friendStatus, a.istdate, c.c1 auid, c.c4 mmGroup, c.c11 nickName, c.c12 face, c.c13 signing, c.c27 starLevel ")
				.append(" from B_Friend a, B_UserLogin c where 1=1 ");
		if(friendMode==1){rtn = rtn.append(" and a.c2=c.c1 and a.c1='").append(auid).append("' and a.c3>0 ");
		}else if(friendMode==2){rtn = rtn.append(" and a.c1=c.c1 and a.c2='").append(auid).append("' and a.c3>0");
		}else if(friendMode==9){rtn = rtn.append(" and (a.c2=c.c1 and a.c1='").append(auid).append("' or and a.c1=c.c1 and a.c2='").append(auid).append("') and a.c3=9");}		
		rtn = rtn.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc");
		return rtn.toString();
	}

	//对象属性
	public final static String LWModuleObjectProperties_QueryByC(String objectMode, String moduleType){
		StringBuilder rtn = new StringBuilder("select a.c5 tableID, b.c1 title, b.c2 ctype, b.c4 cname, b.c5 defvalue, b.c6 param2, b.c7 action, b.c8 relatid, NVL(b.c11,0) power ")
		.append(" from IS_AppModule a, IS_AppModuleDetail b where a.dataid=b.parentDataid and b.c9=130001 ")
		.append(objectMode.startsWith("M") ? getWhere_string("a.c1", objectMode) : getWhere_number("a.c5", objectMode))
		.append(getWhere_number("a.c2", moduleType))
		.append(" order by b.c10 ");
        return rtn.toString();
	}
	//对象
	public final static String LWObject_QueryByDataid = "select * from %s where dataid=%s";
	//对象列表
	public final static String LWObjectList_QueryByModule(String myamid, String objectMode, String dataid, String relationQuery, String keyword, String condition, String orderDesc, String title, String groupby, String mindataid, String maxdataid){
		HashMap<String, String> map = new HashMap<String, String>();
		List<Object> prolist = HIUtil.dbQuery(YSSQL.LWModuleObjectProperties_QueryByC(objectMode, relationQuery.length()>0?"180015":"180013"));
		map.put("ppk", "''");
		map.put("ptitle", "''");
		map.put("ppic", "''");
		map.put("ppro1", "''");
		map.put("ppro2", "''");
		map.put("ppro3", "''");
		map.put("ppro4", "''");
		map.put("pdata1", "''");
		map.put("pdata2", "''");
		map.put("pdata3", "''");
		map.put("pdata4", "''");
		map.put("pcontent", "''");
		map.put("pbtnValue", "''");
		map.put("pbtnAction", "''");
		map.put("pbtnActionURL", "''");
		map.put("pk", "''");
		map.put("title", "''");
		map.put("pic", "''");
		map.put("pro1", "''");
		map.put("pro2", "''");
		map.put("pro3", "''");
		map.put("pro4", "''");
		map.put("data1", "''");
		map.put("data2", "''");
		map.put("data3", "''");
		map.put("data4", "''");
		map.put("content", "''");
		map.put("btnValue", "''");
		map.put("btnAction", "''");
		map.put("btnActionURL", "''");
		map.put("query", "");
		map.put("keyword", "''");
		map.put("orderby", "");
		map.put("state", "130001");
		map.put("table2", "");
		map.put("condition", "");
		
		//查询的表1，表2
		String[] tableIDs = DBUtil.getDBString(prolist.get(0), "tableID").split(",");
		String tableNames = ", "+ ComBeanI_DataTable.get(Integer.parseInt(tableIDs[0])).getTableName() + " m";
		if(tableIDs.length>1) tableNames += ", "+ ComBeanI_DataTable.get(Integer.parseInt(tableIDs[1])).getTableName() + " n";
		if(tableIDs.length>2) tableNames += ", "+ ComBeanI_DataTable.get(Integer.parseInt(tableIDs[2])).getTableName() + " n";
		tableNames = tableNames.substring(1);
		
		I_DataTable table2 = null;
		String defvalue, param2;
		for(Object o: prolist){
			defvalue = DBUtil.getDBString(o, "defvalue");
			param2 = DBUtil.getDBString(o, "param2").replaceAll("MYAMID", myamid);
			if(defvalue.equals("=TITLE")) defvalue = HIUtil.isEmpty(title, "'合计'");
			if(defvalue.equals("=GROUPBY")) defvalue = groupby;
			if(DBUtil.getDBString(o, "relatid").length()>0){
				table2 = ComBeanI_DataTable.get(Integer.parseInt(DBUtil.getDBString(o, "relatid")));
				map.put(DBUtil.getDBString(o, "cname"), "(select "+defvalue.replaceAll(table2.getTableID()+"\\.", "n.")+" from "+table2.getTableName()+" n where "+ param2.replaceAll(table2.getTableID()+"\\.", "n.")+")");
			}else{
				map.put(DBUtil.getDBString(o, "cname"), defvalue);
			}
		}
		
		StringBuilder rtn = new StringBuilder("select ").append(map.containsKey("groupby")?"''":"m.dataid").append(" sortid, ").append(map.containsKey("groupby")?"''":"m.dataid").append(" dataid, ").append(map.get("ppk")).append(" ppk, ")
				.append(map.get("ptitle")).append(" ptitle, ").append(map.get("ppic")).append(" ppic,").append(map.get("ppro1")).append(" ppro1,  ").append(map.get("ppro2")).append(" ppro2,  ").append(map.get("ppro3")).append(" ppro3,  ").append(map.get("ppro4")).append(" ppro4,  ").append(map.get("pdata1")).append(" pdata1, ").append(map.get("pdata2")).append(" pdata2, ").append(map.get("pdata3")).append(" pdata3, ").append(map.get("pdata4")).append(" pdata4, ").append(map.get("pcontent")).append(" pcontent, ").append(map.get("pbtnValue")).append(" pbtnValue,").append(map.get("pbtnAction")).append(" pbtnAction,").append(map.get("pbtnActionURL")).append(" pbtnActionURL,")
				.append(map.get("pk")).append(" pk, ").append(map.get("title")).append(" title, ").append(map.get("pic")).append(" pic,").append(map.get("pro1")).append(" pro1, ").append(map.get("pro2")).append(" pro2, ").append(map.get("pro3")).append(" pro3,").append(map.get("pro4")).append(" pro4, ").append(map.get("data1")).append(" data1, ").append(map.get("data2")).append(" data2, ").append(map.get("data3")).append(" data3, ").append(map.get("data4")).append(" data4, ").append(map.get("content")).append(" content, ").append(map.get("btnValue")).append(" btnValue,").append(map.get("btnAction")).append(" btnAction,").append(map.get("btnActionURL")).append(" btnActionURL,")
				.append(" 1 ")
		.append(" from ").append(tableNames).append(" where m.dataStatus<>-2 and ").append(map.get("state")).append("=130001 ")
		.append(getWhere_string("m.dataid", dataid))
        .append(condition.length()>0 ? " and ".concat(condition) :"")
        .append(map.get("condition").length()>0 ? " and ".concat(map.get("condition")) :"")
		.append(getWhere_like(map.get("query"), relationQuery))
		//.append(getWhere_like(map.get("keyword"), keyword))
		.append(getWhere_like("m.keyword", keyword))
		.append(getWhere_startNumber("m.dataid", mindataid, false))
		.append(getWhere_endNumber("m.dataid", maxdataid, false))
		.append(groupby.length()>0?(" group by "+map.get("groupby")):"")
		.append(" order by ").append(map.get("orderby")).append(" sortid ").append(orderDesc);
        return rtn.toString();
	}
	
	//消息
	public final static String LWMessage_QueryByC(String auid, String msgType, String msgLevel, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select a.dataid sortid, a.dataid, a.c3 content, a.c6 msgType, a.c7 msgSubType, a.c8 objectID, a.istdate, c.c12 face, a.c9 msgImg ")
		.append(" from T_PushSms_Ing a, B_UserLogin c where a.c1=c.c1 ")
		.append(getWhere_string("a.c2", auid))
		.append(getWhere_string("a.c6", msgType))
		.append(getWhere_string("a.c7", msgLevel))
		.append(getWhere_startNumber("a.dataid", mindataid, false))
		.append(getWhere_endNumber("a.dataid", maxdataid, false))
		.append(" union all ")
		.append(" select a.dataid sortid, a.dataid, a.c3 content, a.c6 msgType, a.c7 msgSubType, a.c8 objectID, a.istdate, c.c12 face, a.c9 msgImg ")
		.append(" from T_PushSmsHis a, B_UserLogin c where a.c1=c.c1 ")
		.append(getWhere_string("a.c2", auid))
		.append(getWhere_string("a.c6", msgType))
		.append(getWhere_string("a.c7", msgLevel))
		.append(getWhere_startNumber("a.dataid", mindataid, false))
		.append(getWhere_endNumber("a.dataid", maxdataid, false))
		.append(" order by sortid desc");
        return rtn.toString();
	}
	//删除消息
	public final static String LWMessage_DelByC(String auid, String msgType, String dataid){
		StringBuilder rtn = new StringBuilder("delete from T_PushSmsHis b where 1=1 ")
		.append(getWhere_string("b.c2", auid))
		.append(getWhere_string("b.c6", msgType))
		.append(getWhere_string("b.dataid", dataid));
        return rtn.toString();
	}
	//广告
	public final static String LWAd_QueryByC(String organID){
		return new StringBuilder("select dataid, c1 title, c2 pic, c3 pic_s, c4 duration ")
				.append(" from B_Advert where c6=130001")
				.append(getWhere_number("c7", organID))
				.append(" order by c5").toString();
	}
	//公司库
	public final static String LWAOrgan_QueryByC(String organID, String col, String keyword, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, a.* ")
				.append(" from A_Organ a where a.c26=130001")
				.append(getWhere_number("dataid", organID))
				.append(getWhere_like(col, keyword))
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc").toString();
	}
	//HC公司
	public final static String LWCompany_QueryByC(String organID, String col, String keyword, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, a.* ")
				.append(" from B_MMORGAN a,B_Hunter b where a.c8=130001 and a.c1=b.c2")
				.append(getWhere_number("dataid", organID))
				.append(getWhere_like(col, keyword))
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc").toString();
	}

	// 账户
	public final static String LWHR_ADD = "insert into B_HR(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c16,c18,c33,c34,c35,c36,c37,c38) values(seqB_HR.nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', 130001,'%s','%s','%s','%s','%s','%s')";
	public final static String LWHunter_ADD = "insert into B_Hunter(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c14,c16,c18,c33,c34,c35,c36,c37,c38) values(seqB_Hunter.nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',130001,'%s', '%s', '%s', '%s','%s','%s')";

	public final static String LWUserLogin_EDIT = "update B_UserLogin set c2='%s',c11='%s',c23='%s',c25='%s',c12='%s',c14='%s',c17='%s',c19='%s',c31='%s',c32='%s',c33='%s',c44='%s',uptdate=sysdate where c1='%s'";
	public final static String LWHR_EDIT = "update B_HR set c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c9='%s',c16='%s',c33='%s',c34='%s',c35='%s',c36='%s',c37='%s',c38='%s',uptdate=sysdate where c1='%s'";
	public final static String LWHunter_EDIT = "update B_Hunter set c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c9='%s',c14='%s',c16='%s',c33='%s',c34='%s',c35='%s',c36='%s',c37='%s',c38='%s',uptdate=sysdate where c1='%s'";

	public final static String LWUserLogin_EditByUser = "update B_UserLogin set c2='%s',c11='%s',c14='%s',c31='%s',c32='%s',c33='%s', uptdate=sysdate where c1='%s'";
	public final static String LWHR_EditByUser = "update B_HR set c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c16='%s',uptdate=sysdate where c1='%s'";
	public final static String LWHunter_EditByUser = "update B_Hunter set c4='%s',c5='%s',c8='%s',c10='%s',uptdate=sysdate where c1='%s'";

	public final static String LWUserLogin_UpdateMM = "update B_UserLogin set (c6,c5) = (select c1,dataid from B_MMOrgan where c1='%s') where c1='%s'";
	public final static String LWHR_UpdateMM = "update B_HR set (c2,c3) = (select c1,dataid from B_MMOrgan where c1='%s') where c1='%s'";
	public final static String LWHunter_UpdateMM = "update B_Hunter set (c2,c3) = (select c1,dataid from B_MMOrgan where c1='%s') where c1='%s'";

	public final static String LWHR_QueryByAUID = "select a.*, b.c12 face,NVL((select count(t.c1) from B_MMORGAN t where t.c1=a.c2 and t.c6 not in('%s','%s')),0) flag from B_HR a, B_UserLogin b where a.c1=b.c1 and a.c1='%s'";
	public final static String LWHunter_QueryByAUID = "select a.*, b.c12 face from B_Hunter a, B_UserLogin b where a.c1=b.c1 and a.c1='%s'";
	public final static String LWHR_InviteCode_ADD = "insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values(seqB_InviteCode.nextval,'%s','%s','%s','%s',130001,sysdate+90,'%s','%s')";

	//EMAIL
	public final static String LWUserLogin_EditEmail = "update B_UserLogin set c32='%s', uptdate=sysdate where c1='%s'";
    public final static String LWHR_EditEmail = "update B_HR set c7='%s', uptdate=sysdate where c1='%s'";
    public final static String LWHunter_EditEmail = "update B_Hunter set c7='%s', uptdate=sysdate where c1='%s'";
    
	public final static String LWHR_QueryByC(String status, String keyword, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, a.*,b.c12 imge,decode(a.istdate,null,0,(a.istdate+1-sysdate)*24) remainTime ")
				.append(" from B_HR a,B_USERLOGIN b where a.c18=130001 and a.c1=b.c1(+)")
				.append(getWhere_in("a.c17", status))
				.append(getWhere_like("a.c4||a.c6||a.c7||a.c9", keyword)) //姓名 电话 邮箱 公司
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc").toString();
	}
	// 职位类型信息
	public final static String LWCandidateJobType_QueryByC(String b_amid, String stStatus, String activeStatus, String isGood, String keyword){
		return new StringBuilder("select distinct p.c36 as cname")
				.append(" from B_Candidate p where p.c69=130001")
				.append(b_amid.length()==0 ? "":" and F_isContain(p.c64, '"+ComBeanMMOrgan.getFullName(b_amid)+"', '"+b_amid+"')=0 ") //屏蔽公司
				.append(getWhere_like("p.c36", keyword)) //职位类型
				.append(getWhere_like("p.c91", isGood)) //精选标识
				.append(getWhere_number("p.c3", stStatus))//入库
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(" order by cname")
				.toString();
	}
	// HR发布职位信息
	public final static String LWCandidateJob_QueryByC(String auid){
		return new StringBuilder("select distinct a.c1 as cname")
				.append(" from B_Job a where a.c4=1")
				.append(getWhere_string("a.c2", auid))
				.append(getWhere_number("a.c3", "130001"))
				.append(getWhere_string("a.c21", "0")) // 是否是JD
				.append(" order by cname")
				.toString();
	}

	//城市信息
	public final static String LWHRCity_QueryByC(String b_amid, String stStatus, String activeStatus, String isGood, String keyword){
		return new StringBuilder("select distinct p.c15 cname")
				.append(" from B_Candidate p where p.c69=130001")
				.append(b_amid.length()==0 ? "":" and F_isContain(p.c64, '"+ComBeanMMOrgan.getFullName(b_amid)+"', '"+b_amid+"')=0 ") //屏蔽公司
				.append(getWhere_like("p.c15", keyword)) //城市
				.append(getWhere_like("p.c91", isGood)) //精选标识
				.append(getWhere_number("p.c3", stStatus)) //入库状态
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(" order by cname")
				.toString();
	}
	//HR公司
	public final static String LWHROrgan_QueryByC(String b_amid, String b_mid, String mmName, String status, String keyword, String mindataid, String maxdataid,String auid){
		return new StringBuilder("select distinct a.dataid sortid, a.c1 amid,a.c2 companyName,c.c30 raise,c.c29 compete, c.c28 product,a.c32 logo,c.c18 addr1,c.c19 addr2,c.c20 addr3")
				.append(" from B_MMOrgan a, B_HR b, A_Organ c where a.c1=b.c2 and a.c8=130001 and b.c18=130001 and a.c2=c.c5(+)||'['||c.c1(+)||']'")
				.append(getWhere_number("a.dataid", b_mid))
				.append(getWhere_string("a.c1", b_amid))
				.append(getWhere_number("a.c6", status))
				.append(getWhere_like("a.c2", mmName))
				.append(getWhere_like("a.c2", keyword)) //企业名称
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(getWhere_string("b.c1", auid))
				.append(" order by sortid desc").toString();
	}
	//HR公司
	public final static String LWHROrgan_QueryByC1(String keyword,String status){
		return new StringBuilder("select distinct a.dataid sortid, a.c1 amid,a.c2 companyName,c.c30 raise,c.c29 compete, c.c28 product,a.c32 logo,c.c18 addr1,c.c19 addr2,c.c20 addr3")
				.append(" from B_MMOrgan a, B_HR b, A_Organ c where a.c1=b.c2 and a.c8=130001 and b.c18=130001 and a.c2=c.c5(+)||'['||c.c1(+)||']'")
				.append(getWhere_like("a.c2", keyword))
				.append(getWhere_in("a.c6", status))
				.append(" order by sortid desc").toString();
	}
	// 查询HR公司详情
	public final static String LWHROrgan_QueryBy_JobDetail(String b_amid, String b_mid, String mmName, String status, String keyword, String mindataid, String maxdataid,String auid){
		return new StringBuilder("select distinct a.dataid sortid, a.*,c.c30 raise,c.c29 compete, c.c28 product,a.c32 logo")
				.append(" from B_MMOrgan a, B_HR b, A_Organ c where a.c1=b.c2 and a.c8=130001 and b.c18=130001 and a.c2=c.c1(+)")
				.append(getWhere_number("a.dataid", b_mid))
				.append(getWhere_string("a.c1", b_amid))
				.append(getWhere_number("a.c6", status))
				.append(getWhere_like("a.c2", mmName))
				.append(getWhere_like("a.c2", keyword)) //企业名称
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(getWhere_string("b.c1", auid))
				.append(" order by sortid desc").toString();
	}
	
	//Hunter
	public final static String LWHunter_QueryByC(String status, String keyword, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, a.*,b.c12 imge,decode(a.istdate,null,0,(a.istdate+1-sysdate)*24) remainTime ")
				.append(" from B_Hunter a ,B_USERLOGIN b where a.c18=130001 and a.c1=b.c1(+)")
				.append(getWhere_number("a.c17", status))
				.append(getWhere_like("a.c4||a.c6||a.c7||a.c9", keyword)) //姓名 电话 邮箱 公司
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc").toString();
	}

	//候选人是否已入库在线
	public final static String LWCandidate_CheckOnlineByPhone = "select dataid, c3, c81, (select c11 from B_UserLogin m where m.c1=p.c2) hcName, (select c2 from B_MMOrgan m where m.c1=p.c67) hcMMName from B_Candidate p where c3!=%s and c21='%s'";
	//候选人邮箱已入库在线
	public final static String LWCandidate_CheckOnlineByEmail = "select dataid, c3, c81, (select c11 from B_UserLogin m where m.c1=p.c2) hcName, (select c2 from B_MMOrgan m where m.c1=p.c67) hcMMName from B_Candidate p where c3!=%s and c22='%s'";	
	// 候选人流程进度
	public final static String LWCandidate_Process(String cid, String pcid){
		StringBuilder rtn = new StringBuilder("select a.dataid, a.c9 title, a.c29 time, a.c11 detail, a.c38 processSeq")
				.append(" from B_Candidate_Process_His a where 1=1")
				.append(getWhere_string("a.c1", cid)) //候选人AUID
				.append(getWhere_string("a.c5", pcid)) //流程PICD
				.append(" order by uptdate desc");
				return rtn.toString();
	}
	// 候选人Pub列表 - HR已登录
	public final static String LWCandidatePub_QueryByC(String b_auid, String b_amid, String stStatus, String activeStatus, String skillMarker, String isGood, String city, String job, String keyword, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select F_isRecommend(p.c1,'"+b_auid+"') isRecommend, p.dataid sortid, p.*, p.c1 cid, F_isViewed(p.c1,'"+b_auid+"') isViewed, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k,")
		        .append("a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c18 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c33 jobID, a.c34 jobName, a.c35 hrName, a.c36 mmName")
				.append(" from BO_Candidate p, B_Candidate_Process a where p.c1=a.c1(+) and a.c2(+)=130001 and p.c69=130001")
				.append(getWhere_number("a.c4(+)", b_auid))
				.append(getWhere_number("a.c31(+)", b_amid))
				.append(b_amid.length()==0 ? "":" and F_isContain(p.c64, '"+ComBeanMMOrgan.getFullName(b_amid)+"', '"+b_amid+"')=0 ") //屏蔽公司
				.append(getWhere_number("p.c3", stStatus)) //入库状态
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(" and (F_isRecommend(p.c1,'"+b_auid+"')=1 or p.c91 like '%1%') ") //精选标识
				.append(getWhere_string("p.c15", city)) //城市
				.append(getWhere_string("p.c36", job)) //职位类型
				.append(getWhere_like("p.c8||p.c36", keyword)) //姓名 职位类型
				.append(getWhere_startNumber("p.dataid", mindataid, false))
				.append(getWhere_endNumber("p.dataid", maxdataid, false))
				.append(" order by isRecommend desc, sortid desc");
				return rtn.toString();
	}	
	// 候选人Pub列表 - HR 未登录
	public final static String LWCandidatePub_QueryByC(String stStatus, String activeStatus, String skillMarker, String isGood, String city, String job, String keyword, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select 0 isRecommend, p.dataid sortid, p.*, p.c1 cid, 0 isViewed, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k")
				.append(" from BO_Candidate p where p.c69=130001")
				.append(getWhere_number("p.c3", stStatus)) //入库状态
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(getWhere_like("p.c91", isGood)) //精选标识
				.append(getWhere_string("p.c15", city)) //城市
				.append(getWhere_string("p.c36", job)) //职位类型
				.append(getWhere_like("p.c8||p.c36", keyword)) //姓名 职位类型
				.append(getWhere_startNumber("p.dataid", mindataid, false))
				.append(getWhere_endNumber("p.dataid", maxdataid, false))
				.append(" order by sortid desc");
				return rtn.toString();
	}	
	//候选人列表
	public final static String LWCandidate_QueryByC(String s_auid, String s_amid, String b_amid, String stStatus, String stCheckStatus, String activeStatus, String skillMarker, String goodQueryStatus, String isCompleted, String editCVFlag, String keyword, String mindataid, String maxdataid, String sortMK){
		String sort = "sortid desc";
		String sortid = "p.dataid";
		if(sortMK.equals("浏览")){
			sort = "c28 desc";
		}else if(sortMK.equals("关注")){
			sort = "c27 desc";
		}else if(sortMK.equals("邀请面试")){
			sort = "c29 desc";
		}else if(sortMK.equals("入库时间")){
			sort = "c4 desc";
		}else if(sortMK.equals("姓名")){
			sort = "c8";
		}else if(sortMK.equals("公司名")){
			sort = "c41";
		}else if(sortMK.equals("更新时间")){
			sort = "uptdate desc";
			sortid = "to_char(p.uptdate,'yyyymmddhh24miss')||lpad(p.dataid,10,'0')";
		}
		StringBuilder rtn = new StringBuilder("select "+sortid+" sortid, p.*, p.c1 cid, p.c3 stStatus, p.c71 stCheckStatus, p.c72 ivTime, p.c82 cvStatus, decode(p.c81,null,0,(p.c81+1-sysdate)*24) completeRemainTime, decode(p.c6,null,0,(p.c6+1-sysdate)*24) activeRemainTime, decode(p.c83,null,0,(p.c83+1-sysdate)*24) cvUpdateRemainTime, decode(p.c86,null,0,(p.c86+1-sysdate)*24) hccommentRemainTime, round(p.c43*10/1000)/10 yuexin_k ")
				.append(" from B_Candidate p where c69=130001")
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.append(getWhere_notlike("p.c52", b_amid)) //屏蔽公司
				.append(getWhere_number("p.c3", stStatus)) //入库状态
				.append(getWhere_number("p.c71", stCheckStatus)) //入库审核状态
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(getWhere_number("p.c65", isCompleted)) //完善标识
				.append(getWhere_like("p.c91", goodQueryStatus)) //精选标识
				.append(getWhere_number("p.c82", editCVFlag)) //简历更新标识
				.append(getWhere_like("p.c8||p.c42||p.c41||p.c9", keyword)) //姓名 职位
				.append(getWhere_startNumber(sortid, mindataid, false))
				.append(getWhere_endNumber(sortid, maxdataid, false))
				.append(" order by ").append(sort);
				return rtn.toString();
	}
	//查询某一个候选人
	public final static String LWCandidate_QueryByCID(String onlineTable, String cid, String s_auid, String s_amid){
		return new StringBuilder("select p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k")
				.append(" from B").append(onlineTable).append("_Candidate p where p.c69=130001")
				.append(" and p.c1=").append(cid) //CID
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.toString();
	}
	//查询某一个候选人
	 public final static String LWCandidateProcess_QueryByCID(String onlineTable, String cid, String pcid, String cp_state, String s_auid, String s_amid, String b_auid, String b_amid){
	  return new StringBuilder("select p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k,")
	          .append("a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c18 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c33 jobID, a.c34 jobName, a.c35 hrName, a.c36 mmName, a.c37 ivTime")
	    .append(" from B").append(onlineTable).append("_Candidate p, B_Candidate_Process a where p.c1=a.c1(+) and p.c69=130001")
	    .append(" and p.c1=").append(cid) //CID
	    .append(getWhere_number("a.c2(+)", cp_state)) //流程的有效状态
	    .append(getWhere_string("a.c5(+)", pcid)) //PCID
	    .append(getWhere_string("p.c2", s_auid)) //猎头顾问
	    .append(getWhere_string("p.c67", s_amid)) //猎头公司
	    .append(getWhere_number("a.c4(+)", b_auid))
	    .append(getWhere_number("a.c31(+)", b_amid))
	    .append(" order by pid desc")
	    .toString();
	 }
	// 新增候选人备忘录
	public static final String LWCandidate_addMemo = "insert into B_Candidate_Memo(dataid,parentDataid,c1,c2,c3,c6,c9,c10,c11) select seqB_Candidate_Memo.nextval,dataid, c1, '%s', 130001,'%s','%s','%s',sysdate from B_Candidate where c1='%s'";
	// 候选人附件上传
	public final static String LWCandidate_addCVFile = "insert into B_Candidate_Files(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14) select seqB_Candidate_Files.nextval,dataid,c1,130001,'250002','简历','简历','简历','PDF/DOC',c62,c77,c78,sysdate,c77,c78,sysdate from B_Candidate where c1='%s' and c62 is not null";
	public final static String LWCandidate_addOfferFile = "insert into B_Candidate_Files(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14) select seqB_Candidate_Files.nextval,dataid,c1,130001,'250009','Offer','Offer','Offer','PDF/DOC',c11,%s,'%s',sysdate,c27,c28,sysdate from B_Candidate_Process where c1='%s' and c5='%s'";
	public final static String LWCandidate_addAttachFile = "insert into B_Candidate_Files(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14) select seqB_Candidate_Files.nextval,dataid,c1,130001,'%s','%s','%s','%s','%s','%s','%s','%s',sysdate,'%s','%s',sysdate from B_Candidate where c1='%s'";
	public final static String LWCandidate_editAttachFile = "update B_Candidate_Files set c2=130001,c3='%s',c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c9='%s',c10='%s',c11=sysdate,c12='%s',c13='%s',c14=sysdate,uptdate=sysdate where c1='%s' and dataid=%s";
	public final static String LWCandidate_delAttachFile = "update B_Candidate_Files set c2=130004,uptdate=sysdate where c1='%s' and dataid=%s";
	
	// HR候选人简历详情查询相识候选人
	public final static String LWSimilarCandidate_ByHR(String cid, String b_auid, String b_amid, String stStatus, String activeStatus, String isGood){
		StringBuilder rtn = new StringBuilder("select * from (select F_SimilarCandidate(p.c1, '"+cid+"') similar, p.dataid sortid, p.*, p.c1 cid, F_isViewed(p.c1,'"+b_auid+"') isViewed, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k,")
		        .append("a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c18 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c33 jobID, a.c34 jobName, a.c35 hrName, a.c36 mmName")
				.append(" from BO_Candidate p, B_Candidate_Process a where p.c1=a.c1(+) and a.c2(+)=130001 and p.c69=130001")
				.append(getWhere_number("a.c4(+)", b_auid))
				.append(getWhere_number("a.c31(+)", b_amid))
				.append(" and p.c1 != '"+cid+"'")
				.append(b_amid.length()==0 ? "":" and F_isContain(p.c64, '"+ComBeanMMOrgan.getFullName(b_amid)+"', '"+b_amid+"')=0 ") //屏蔽公司
				.append(getWhere_number("p.c3", stStatus)) //入库状态
				.append(getWhere_number("p.c5", activeStatus)) //在线状态
				.append(getWhere_like("p.c91", isGood)) //精选标识
				.append(" ) where similar>=1 and candiStatus is null order by similar");
				return rtn.toString();
	}	
	//候选人简历详情
	public final static String[] LWCandidate_CVDetails_Query = new String[]{
		"",
		"select * from B%s_Candidate where c1='%s'",
		"",
		"select * from B%s_Candidate_CVEducation where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate_CVTrain where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate_CVCertificate where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate_CVWork where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate_CVWork where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate_CVProject where c1='%s' and c2=130001 order by c3 desc",
		"select dataid,c52,c53,c54,c55,c56 from B%s_Candidate where c1='%s' and c69=130001",
		"select dataid,c37,c46,c47,c48,c49,c50,c51,c63,c64 from B%s_Candidate where c1='%s' and c69=130001",
		"select a.*, b.c12 hcFace from B%s_Candidate_HCComment a, B_UserLogin b where a.c1='%s' and a.c3=130001 and b.c1=a.c2 order by a.istdate desc",
		"select * from B%s_Candidate_Interview t where c1='%s' and c2=130001 and c14<>9 and c4=(select c2 from B_Candidate a where a.c1=t.c1)",
	};
	public final static String[] LWCandidate_CVDetails_TableName = new String[]{
		"",
		"B_Candidate",
		"",
		"B_Candidate_CVEducation",
		"B_Candidate_CVTrain",
		"B_Candidate_CVCertificate",
		"",
		"B_Candidate_CVWork",
		"B_Candidate_CVProject",
		
	};
	public final static String[] LWCandidate_CVDetails_toZWhere = new String[]{
		"",
		"c1='%s'",
		"",
		"c1='%s' and dataid=%s",
		"c1='%s' and dataid=%s",
		"c1='%s' and dataid=%s",
		"",
		"c1='%s' and dataid=%s",
		"c1='%s' and dataid=%s",
		"c1='%s' and dataid=%s",
	};
	public final static String[] LWCandidate_CVDetails_Add = new String[]{//oo[i++] = auid;c10 oo[i++] = auidName;c11 oo[i++] = cid;
		"",
		"insert into B_Candidate(dataid,c1,c2,c3,c71,c8,c20,c21,c22,c44,c41,c42,c62,c65,c67,c68,c69,c77,c78,c79,c82,c83) values(seqB_Candidate.nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','1000000000','%s','%s',130001,'%s','%s',sysdate,'%s',sysdate)",
		"",
		"insert into B_Candidate_CVEducation(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c10,c11,c12) select seqB_Candidate_CVEducation.nextval,dataid,c1,130001,'%s','%s','%s','%s','%s','%s','%s',sysdate from B_Candidate where c1='%s'",
		"insert into B_Candidate_CVTrain(dataid,parentDataid,c1,c2,c3,c4,c6,c9,c10,c11) select seqB_Candidate_CVTrain.nextval,dataid,c1,130001,'%s','%s','%s','%s','%s',sysdate from B_Candidate where c1='%s'",
		"insert into B_Candidate_CVCertificate(dataid,parentDataid,c1,c2,c3,c4,c8,c9,c10) select seqB_Candidate_CVCertificate.nextval,dataid,c1,130001,'%s','%s','%s','%s',sysdate from B_Candidate where c1='%s'",
		"",
		"insert into B_Candidate_CVWork(dataid,parentDataid,c1,c2,c3,c4,c5,c7,c19,c8,c9,c10,c16,c21,c22,c23,c30) select seqB_Candidate_CVWork.nextval,dataid,c1,130001,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',sysdate,%s from B_Candidate where c1='%s'",
		"insert into B_Candidate_CVProject(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c12,c13,c14) select seqB_Candidate_CVProject.nextval,dataid,c1,130001,'%s','%s','%s','%s','%s','%s','%s','%s','%s',sysdate from B_Candidate where c1='%s'",
	};
	public final static String[] LWCandidate_CVDetails_Edit = new String[]{
		"",
		"update B_Candidate set c8='%s',c9='%s',c13='%s',c17='%s',c10='%s',c22='%s',c20='%s',c21='%s',c23='%s',c15='%s',c16='%s',c69=130001,c77='%s',c78='%s',c79=sysdate,c83=sysdate,uptdate=sysdate where c1='%s'",
		"",
		"update B_Candidate_CVEducation set c2=130001, c3='%s',c4='%s',c5='%s',c6='%s',c7='%s',c10='%s',c11='%s',c12=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",
		"update B_Candidate_CVTrain set c2=130001,c3='%s',c4='%s',c6='%s',c9='%s',c10='%s',c11=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",
		"update B_Candidate_CVCertificate set c2=130001,c3='%s',c4='%s',c8='%s',c9='%s',c10=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",
		"",
		"update B_Candidate_CVWork set c2=130001,c3='%s',c4='%s',c5='%s',c7='%s',c19='%s',c8='%s',c9='%s',c10='%s',c16='%s',c21='%s',c22='%s',c23=sysdate,c30=%s,uptdate=sysdate where c1='%s' and dataid=%s",
		"update B_Candidate_CVProject set c2=130001,c3='%s',c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c9='%s',c12='%s',c13='%s',c14=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",
	};
	public final static String[] LWCandidate_CVDetails_ResetState = new String[]{
		"",
		"update B_Candidate set c69=130004,uptdate=sysdate where c1='%s'",
		"",
		"update B_Candidate_CVEducation set c2=130004,uptdate=sysdate where c1='%s'",
		"update B_Candidate_CVTrain set c2=130004,uptdate=sysdate where c1='%s'",
		"update B_Candidate_CVCertificate set c2=130004,uptdate=sysdate where c1='%s'",
		"",
		"update B_Candidate_CVWork set c2=130004,uptdate=sysdate where c1='%s'",
		"update B_Candidate_CVProject set c2=130004,uptdate=sysdate where c1='%s'",
	};
	// 顾问面评 "工作经验", "工作期望", "意向公司", "屏蔽公司", "技能标签", "工作总结与整体印象"
	public final static String[] LWCandidate_HCComment_Query = new String[]{
		"",
		"select * from B%s_Candidate_CVWork where c1='%s' and c2=130001 order by c3 desc",
		"select * from B%s_Candidate where c1='%s' and c69=130001",
		"select * from B%s_Candidate where c1='%s' and c69=130001",
		"select * from B%s_Candidate where c1='%s' and c69=130001",
		"select * from B%s_Candidate where c1='%s' and c69=130001",
		"select * from B%s_Candidate_HCComment where c1='%s' and c2='%s' and c3=130001",
	};
	public final static String[] LWCandidate_HCComment_toZWhere = new String[]{
		"",
		"c1='%s' and dataid=%s",
		"c1='%s'",
		"c1='%s'",
		"c1='%s'",
		"c1='%s'",
		"c1='%s' and dataid=%s",
	};
	public final static String[] LWCandidate_HCComment_TableName = new String[]{
		"",
		"B_Candidate_CVWork",
		"B_Candidate",
		"B_Candidate",
		"B_Candidate",
		"B_Candidate",
		"B_Candidate_HCComment",
	};
	public final static String[] LWCandidate_CVComment_Add = new String[]{
		"",
		"",
		"",
		"",
		"",
		"",
		"insert into B_Candidate_HCComment(dataid,parentDataid,c1,c2,c3,c5,c20,c21,c22,c10,c14,c13,c15,c16,c9,c28,c29,c30) select seqB_Candidate_HCComment.nextval,dataid,c1,c2,130001,'241023','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',sysdate from B_Candidate where c1='%s'",		
	};
	public final static String[] LWCandidate_CVComment_Edit = new String[]{
		"",
		"update B_Candidate_CVWork set c2=130001,c18='%s',c7='%s',c20='%s',c11='%s',c12='%s',c13='%s',c14='%s',c15='%s',c24='%s',c17='%s',c29='%s',c25='%s',c26='%s',c27='%s',c21='%s',c22='%s',c23=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",		
		"update B_Candidate set c46='%s',c47='%s',c48='%s',c49='%s',c51='%s',c37='%s',c77='%s',c78='%s',c79=sysdate,c86=sysdate,uptdate=sysdate where c1='%s'",
		"update B_Candidate set c63='%s',c77='%s',c78='%s',c79=sysdate,uptdate=sysdate where c1='%s'",
		"update B_Candidate set c64='%s',c77='%s',c78='%s',c79=sysdate,uptdate=sysdate where c1='%s'",
		"update B_Candidate set c52='%s',c53='%s',c54='%s',c77='%s',c78='%s',c79=sysdate,uptdate=sysdate where c1='%s'",
		"update B_Candidate_HCComment set c20='%s',c21='%s',c22='%s',c10='%s',c14='%s',c13='%s',c15='%s',c16='%s',c9='%s',c28='%s',c29='%s',c30=sysdate,uptdate=sysdate where c1='%s' and dataid=%s",
	};
	//特别推荐候选人状态列表
	public final static String LWCandidateBetter_Status_QueryByC(){
		return new StringBuilder("select '2' data1,NVL(count(decode(c5,'1',1)),0) sum1,'3' data2,NVL(count(decode(c5,'2',1)),0) sum2")
		.append(" from B_CANDIDATE_ORGAN_RELATION a")
		.toString();
	}
	//精选候选人状态列表
	public final static String LWCandidateGood_Status_QueryByC(){
		return new StringBuilder("select '247001-1' data1, NVL(sum(decode(c5||'-'||instr(c91,'1'),'247001-0',0,1)),0) sum1, '247001-2' data2, NVL(sum(decode(c5||'-'||instr(c91,'2'),'247001-0',0,1)),0) sum2, '247001-0' data3, NVL(sum(decode(c5||'-'||instr(c91,'0'),'247001-0',0,1)),0) sum3, '247001' data9, NVL(sum(decode(c5,'247001',1,0)),0) sum9")
				.append(" from B_Candidate p where c69=130001")
				.append(getWhere_string("p.c3", ACE.CANDI_STStatus_ST)) //已入库
				.append(getWhere_string("p.c5", ACE.CANDI_ActiveStatus_ACTIVE)) //Active
				.toString();
	}
	//HC的候选人状态列表
	public final static String LWCandidate_Status_QueryByC(String s_auid, String s_amid){
		return new StringBuilder("select count(*) sum0, 240000 data1, NVL(sum(decode(c3,240000,1,0)),0) sum1, 240020 data2, NVL(sum(decode(c3,240020,1,0)),0) sum2,240090 data3, NVL(sum(decode(c3,240090,1,0)),0) sum3")
				.append(" from B_Candidate p where c69=130001")
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.toString();
	}
	//候选人面试记录查询
	public final static String LWCandidate_Interview_QueryByC(String s_auid,String status){
		return new StringBuilder("select a.c7 time,a.c5 name, a.c6 postion, a.c9 address")
				.append(" from B_Candidate_Interview a where a.c14=0 and a.c20=1")
				.append(getWhere_string("c4", s_auid)) //auid
				.append(getWhere_number("c14", status)) //面试状态				
				.append(" order by c7").toString();
	}	
	// 候选人面试流程状态列表
	public final static String LWCandidate_ProcessStatus_QueryByC(String cid, String s_auid, String s_amid, String b_auid, String b_amid, String candiStatus, String processCloseStatus, String collectStatus){
		return new StringBuilder("select count(dataid) sum0, 244001 data1, NVL(sum(decode(c8,244001,1,0)),0) sum1, 244005 data2, NVL(sum(decode(c8,244005,1,0)),0) sum2,244020 data3, NVL(sum(decode(c8,244020,1,0)),0) sum3,244052 data4, NVL(sum(decode(c8,244052,1,0)),0) sum4")
				.append(" from B_Candidate_Process a where a.c2=130001")
				.append(getWhere_string("c1", cid)) //CID
				.append(getWhere_string("c2", s_auid)) //猎头顾问
				.append(getWhere_string("c67", s_amid)) //猎头公司
				.append(getWhere_number("c4", b_auid))
				.append(getWhere_number("c31", b_amid))
				.append(getWhere_number("c7", candiStatus))//候选人流程状态：241001进行中  241009已完成
				.append(getWhere_number("c21", collectStatus))
				.toString();
	}
	//候选人流程
	public final static String LWCandidate_Process_Add = "insert into B_Candidate_Process(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10) values(seqB_Candidate_Process.nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
	public final static String LWCandidate_Process_QueryByC(String onlineTable, String state, String cid, String s_auid, String s_amid, String b_auid, String b_amid, String candiStatus, String processStatus, String processCloseStatus, String processInnerStatus, String collectStatus, String job, String keyword, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k, ")
				.append(" d.c1 hunterID, d.c11 hunterNickName, d.c12 hunterFace, a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, a.c37 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c29 operateTime, a.c31 processCompanyID, a.c33 processJobID, a.c34 jobName, a.c35 hrName, a.c36 mmName, a.c37 ivTime")
				.append(" from B_Candidate_Process a, B").append(onlineTable).append("_Candidate p, B_UserLogin d where a.c1=p.c1 and p.c2=d.c1 and p.c69=130001")
				.append(getWhere_string("p.c1", cid)) //CID
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.append(getWhere_number("a.c2", state))
				.append(getWhere_number("a.c4", b_auid))
				.append(getWhere_number("a.c31", b_amid))
				.append(getWhere_number("a.c7", candiStatus)) //候选人流程状态：241001进行中  241009已完成
				.append(getWhere_number("a.c8", processStatus)) //候选人面试流程状态
				.append(getWhere_number("a.c12", processCloseStatus)) //候选人面试流程结束状态
				.append(getWhere_number("a.c16", processInnerStatus))
				.append(getWhere_string("a.c33", job))
				.append(getWhere_number("a.c21", collectStatus))
				.append(getWhere_like("p.c8||p.c42", keyword)) //姓名 职位
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by cid,candiStatus,sortid desc").toString();
	}
	public final static String LWCandidate_Process_QueryByC_HR(String onlineTable, String state, String cid, String s_auid, String s_amid, String b_auid, String b_amid, String candiStatus, String processStatus, String processCloseStatus, String processInnerStatus, String collectStatus, String job, String keyword, String mindataid, String maxdataid){

		System.out.println("[CandiService.ServiceDO_CandiList_HR测试]======================================="+job);
		return new StringBuilder("select a.dataid sortid, p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, round(p.c43*10/1000)/10 yuexin_k, ")
				.append(" d.c1 hunterID, d.c11 hunterNickName, d.c12 hunterFace, a.uptdate processUptdate, a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, a.c37 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, NVL(a.c22,sysdate-30000) collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c29 operateTime, a.c31 processCompanyID, a.c33 processJobID, a.c34 jobName, a.c35 hrName, a.c36 mmName, a.c37 ivTime")
				.append(" from B_Candidate_Process a, B").append(onlineTable).append("_Candidate p, B_UserLogin d where a.c1=p.c1 and p.c2=d.c1 and p.c69=130001")
				.append(getWhere_string("p.c1", cid)) //CID
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.append(getWhere_number("a.c2", state))
				.append(getWhere_number("a.c4", b_auid))
				.append(getWhere_number("a.c31", b_amid))
				.append((collectStatus.length()==0 || b_amid.length()==0) ? "":" and F_isContain(p.c64, '"+ComBeanMMOrgan.getFullName(b_amid)+"', '"+b_amid+"')=0 ") //屏蔽公司			
				.append(getWhere_number("a.c7", candiStatus)) //候选人流程状态：241001进行中  241009已完成
				.append(getWhere_number("a.c8", processStatus)) //候选人面试流程状态
				.append(getWhere_number("a.c12", processCloseStatus)) //候选人面试流程结束状态
				.append(getWhere_number("a.c16", processInnerStatus))
				.append(getWhere_number("p.c36", job))//职位类型
				.append(getWhere_number("a.c21", collectStatus))
				.append(getWhere_like("p.c8||p.c42", keyword)) //姓名 职位
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by ").append(collectStatus.length()==0 ? "processUptdate" : "collectDate").append(" desc").toString();
	}	
	
	//流程详情
	public final static String LWCandidate_ProcessDetailList_QueryByC(String onlineTable, String cid, String s_auid, String s_amid, String b_auid, String b_amid, String pcid, String processStatus){
		return new StringBuilder("select a.dataid sortid, p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, a.c18 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c29 operateTime, a.c33 processJobID, a.c34 jobName, a.c35 hrName, a.c36 mmName")
		.append(" from B_Candidate_Process a, B").append(onlineTable).append("_Candidate p where a.c1=p.c1 and a.c2=130001 and p.c69=130001")
		.append(getWhere_string("p.c1", cid)) //CID
		.append(getWhere_string("p.c2", s_auid)) //猎头顾问
		.append(getWhere_string("p.c67", s_amid)) //猎头公司
		.append(getWhere_number("a.c5", pcid))
		.append(getWhere_number("a.c4", b_auid))
		.append(getWhere_number("a.c31", b_amid))
		.append(processStatus.length()==0 ? "" : " and exists (select dataid from B_Candidate_Process_His m where m.c5=a.c5 and m.c8='"+processStatus+"')") //候选人面试流程状态
		.append(" order by cid,operateTime desc").toString();
	}
	public final static String LWCandidate_ProcessDetailHis_QueryByC(String onlineTable, String cid, String s_auid, String s_amid, String b_auid, String b_amid, String pcid, String processStatus){
		return new StringBuilder("select a.dataid sortid, p.*, p.c1 cid, p.c2 hcid, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, a.dataid pid, a.c4 hrAuid, a.c5 pcid, a.c6 processType, a.c7 candiStatus, a.c8 processStatus, a.c9 processTitle, a.c10 processDesc, a.c11 processDetail, a.c12 processCloseStatus, a.c13 processCloseStatusDesc, a.c14 processStartTime, a.c15 processEndTime, (NVL(a.c29,sysdate)+1-sysdate)*24*60*60 processSecond, a.c16 processInnerStatus, a.c17 processInnerStatusDesc, a.c18 ivTimes, a.c19 billStatus, a.c20 billStatusDesc, a.c21 collectStatus, a.c22 collectDate, a.c23 processDesc_M, a.c24 processDesc_HC, a.c25 processDesc_HR, a.c25 processDesc_C, a.c29 operateTime, a.c33 processJobID, a.c34 jobName, a.c35 hrName, a.c36 mmName")
				.append(" from B_Candidate_Process_His a, B").append(onlineTable).append("_Candidate p where a.c1=p.c1 and a.c2=130001 and p.c69=130001")
				.append(getWhere_string("p.c1", cid)) //CID
				.append(getWhere_string("p.c2", s_auid)) //猎头顾问
				.append(getWhere_string("p.c67", s_amid)) //猎头公司
				.append(getWhere_number("a.c5", pcid))
				.append(getWhere_number("a.c4", b_auid))
				.append(getWhere_number("a.c31", b_amid))
				.append(getWhere_number("a.c8", processStatus)) //候选人面试流程状态
				.append(" order by cid,operateTime desc").toString();
	}
	//候选人事件
	public final static String LWCandidate_Action_QueryByC(String cid, String s_auid, String b_auid, String b_amid, String actionType, String startDate, String endDate, String b_jobid, String isM, String isBS, String isHC, String isHR, String isC, String mindataid, String maxdataid){
		return new StringBuilder("select a.dataid sortid, a.*")
				.append(" from B_Action a where (a.c2<>130444)")
				.append(getWhere_number("a.c1", cid))
				.append(getWhere_string("a.c2", s_auid)) //猎头顾问
				.append(getWhere_string("a.c45", b_amid)) //hr公司
				.append(getWhere_string("a.c6", actionType))
				.append(getWhere_string("a.c47", b_jobid))
				.append(getWhere_string("a.c48", b_auid)) //hr
				.append(getWhere_number("a.c19", isM))
				.append(getWhere_number("a.c20", isBS))
				.append(getWhere_number("a.c21", isHC))
				.append(getWhere_number("a.c22", isHR))
				.append(getWhere_number("a.c23", isC))
				.append(getWhere_seDate("a.istdate", startDate, endDate))
				.append(getWhere_startNumber("a.dataid", mindataid, false))
				.append(getWhere_endNumber("a.dataid", maxdataid, false))
				.append(" order by sortid desc").toString();
	}
	//候选人附件
	public final static String LWCandidate_Files_QueryByC(String cid, String fileType, String fileid){
		return new StringBuilder("select a.dataid sortid, a.*")
				.append(" from B_Candidate_Files a where a.c2=130001")
				.append(getWhere_number("a.dataid", fileid))
				.append(getWhere_number("a.c1", cid))
				.append(getWhere_number("a.c3", fileType))
				.append(" order by a.c3,sortid").toString();
	}
	
//	//候选人关注
//	public final static String LWCandidate_Collect_QueryByC(String s_auid, String b_auid, String keyword, String mindataid, String maxdataid){
//		return new StringBuilder("select a.dataid sortid, p.*,a.dataid dataid_ ")
//				.append(" from B_Candidate_Collect a, B_Candidate p where a.c1=p.c1 and p.c69=130001 and a.c5=130001")
//				.append(getWhere_number("p.c2", s_auid))
//				.append(getWhere_number("a.c2", b_auid))
//				.append(getWhere_like("p.c8||p.c42", keyword)) //姓名 职位
//				.append(getWhere_startNumber("a.dataid", mindataid, false))
//				.append(getWhere_endNumber("a.dataid", maxdataid, false))
//				.append(" order by sortid desc").toString();
//	}
	
	// 公司注册
	public final static String LWMMOrgan_AddFromAOrgan = "insert into B_MMOrgan(dataid,c1,c2,c5,c6,c7,c8,c9,c14,c28,c31,c34,c35) select seqB_MMOrgan.Nextval,'%s',c5||'['||c1||']' mmName,%s,%s,%s,%s,c10,c18,c2,c12,c16,c17 from A_Organ where c5||'['||c1||']'='%s'";
	public final static String LWMMOrgan_Add = "insert into B_MMOrgan(dataid,c1,c2,c5,c6,c7,c8,c9,c14,c28,c31,c34,c35) values(seqB_MMOrgan.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
	public final static String LWMMOrgan_Edit = "update B_MMOrgan set c2='%s',c5='%s',c7='%s',c9='%s',c14='%s',c28='%s',c29='%s',c31='%s',c32='%s',c33='%s',c34='%s',c38='%s',c39='%s',c40='%s',c36='%s',c37='%s',uptdate=sysdate where c28='%s'";
	// 公司入库
	public final static String LWAOrgan_Add = "insert into A_Organ(dataid,c1,c2,c3,c6,c8,c9,c10,c12,c13,c14,c15,c16,c17,c18,c21,c22,c24,c25,c26) values(pkg_pcace_web.SF_GetMaxID('A_Organ','dataid'), '%s','%s','%s','%s','%s', '%s','%s','%s','%s','%s', '%s','%s','%s','%s','%s', '%s','%s','%s',130001)";
	//HR的职位记录
	public final static String LWJob_JD = "insert into B_Job(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c17,c18,c19,c20,c21) values(seqB_Job.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s',sysdate,'%s','%s','%s')";

	public final static String LWJob_Add = "insert into B_Job(dataid,parentDataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c21) values(seqB_Job.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','0')";
	public final static String LWJob_Edit = "update B_Job set c1='%s',c2='%s',c3='%s',c4='%s',c5='%s',c6='%s',c7='%s',c8='%s',c9='%s',c10='%s',c11='%s',c12='%s',c13='%s',c14='%s',c15='%s',c16='%s' where dataid=%s";
	public final static String LWJob_AddJD = "insert into B_Job(dataid,c2,c3,c5,c6,c17,c18,c19.c20,c21) values(seqB_Job.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','1')";
	public final static String LWJob_UpdateStatus = "update B_job set c4='%s' where dataid=%s";
	public final static String LWJob_QueryByC(String jobid, String b_amid, String b_auid, String isJD, String status, String mindataid, String maxdataid){
		StringBuilder rtn = new StringBuilder("select a.dataid sortid, a.* ")
		.append(" from B_Job a , B_MMOrgan b where 1=1 and a.c5 = b.c1(+) ")
		.append(getWhere_string("a.dataid", jobid))		
		.append(getWhere_string("a.c5", b_amid))
		.append(getWhere_string("a.c2", b_auid))
		.append(getWhere_number("a.c4", status))
		.append(getWhere_number("a.c21", isJD))		
		.append(getWhere_startNumber("a.dataid", mindataid, false))
		.append(getWhere_endNumber("a.dataid", maxdataid, false))
		.append(" order by sortid desc");
        return rtn.toString();
	}
	//我的职位统计数量查询
	public final static String LWJob_QueryByC(String b_auid, String b_amid){
		return new StringBuilder("select count(*) sum, 1 data1, NVL(sum(decode(c4,1,1,0)),0) sum1, 0 data1, NVL(sum(decode(c4,0,1,0)),0) sum2")
				.append(" from B_Job a where c21=0")		
				.append(getWhere_string("a.c5", b_amid))
				.append(getWhere_string("a.c2", b_auid))
				.toString();
	}
	//开票记录
	public final static String LWBill_setBill = "update B_Bill set c9='%s',c10='%s',c15='%s',c16='%s',c17='%s',c18=to_date('%s','yyyy-mm-dd'),uptdate=sysdate where c2=%s";//NVL(a.c24-sysdate,sysdate)
	public final static String LWBill_QueryByC(String pcid, String b_amid, String b_auid, String s_amid, String s_auid, String processCloseStatus, String status, String keyword, String mindataid, String maxdataid,String innerPcid){
		StringBuilder rtn = new StringBuilder("select a.dataid sortid, a.*, NVL(a.c14,sysdate)-sysdate rtime_bill, NVL(a.c19,sysdate)-sysdate rtime_pay2M, NVL(a.c24,sysdate)-sysdate rtime_hr2hc, NVL(a.c26,sysdate)-sysdate rtime_pay2hc ")
		.append(" from B_Bill a, B_Candidate_Process b where a.c2=b.c5 ")
		.append(getWhere_string("b.c16", innerPcid))
		.append(getWhere_string("b.c12", processCloseStatus))
		.append(getWhere_string("a.c2", pcid))
		.append(getWhere_string("a.c3", b_amid))
		.append(getWhere_string("a.c4", b_auid))
		.append(getWhere_string("a.c5", s_amid))
		.append(getWhere_string("a.c6", s_auid))
		.append(getWhere_in("a.c10", status))
		.append(getWhere_like("a.c7||' '||a.c8||' '||a.c11||' '||a.c12", keyword))
		.append(getWhere_startNumber("a.dataid", mindataid, false))
		.append(getWhere_endNumber("a.dataid", maxdataid, false))
		.append(" order by sortid desc");
        return rtn.toString();
	}
	
	//HC:Main
	public final static String LWMain_Action_QueryByC(String isM, String isHC, String s_auid, String actionType, String day, String taskType, String oldtid, String sortMK, String mindataid, String maxdataid){
		boolean isToday = day.equals(HIUtil.getCurrentDate("yyyy-MM-dd"));
		String sortid = "", orderby = "";
		int i_sortMK = Integer.parseInt(sortMK);
		if(i_sortMK==1){ sortid = "a.dataid"; orderby = "tid desc"; }
		if(i_sortMK==2){ sortid = "a.c45||LPAD(a.dataid,10,'0')"; orderby = "b_amid,tid desc"; }
		if(i_sortMK==3){ sortid = "a.c1||LPAD(a.dataid,10,'0')"; orderby = "cid,tid desc"; }
		
		StringBuilder rtn = new StringBuilder("select "+sortid+" sortid, a.dataid tid, NVL(a.c45,'AMID') b_amid, p.c1 cid, p.c13 sex, p.c61 face, a.*, decode(a.c10,null,-1,(a.c9-sysdate)*24*60) limitMinutes, decode(sign(a.c49-sysdate+1),1,to_char(a.c49,'hh24:mi'),'') ivTime, p.c3 stStatus, p.c5 activeStatus, p.c71 stCheckStatus, p.c82 cvStatus, b.c7 candiStatus, b.c8 processStatus, b.c12 processCloseStatus, b.c16 processInnerStatus, b.c21 collectStatus, b.c19 billStatus, b.c13 processDetail ")
		.append(" from B_Action a, B_Candidate_Process b, B_Candidate p where a.c1=p.c1(+) and a.c4=b.c5(+) and a.c3=130001 and a.c42=0")
		.append(getWhere_string("a.c2", s_auid))
		.append(getWhere_string("a.c5", actionType))
		.append(getWhere_number("a.c20", HIUtil.isEmpty(isM) ? "":"1"))
		.append(getWhere_number("a.c21", isHC))
		.append(HIUtil.isEmpty(isM) ? "" : "and (a.c26="+isM+" or a.c26='BS') ")
		.append(getWhere_like("a.c57||a.c61", taskType))
		.append(isToday ? " and a.c49<trunc(sysdate+1)" : (" and to_char(a.c49,'yyyy-mm-dd')='"+day+"'"))
		.append(!isToday ? " and (a.c32 is null or instr(a.c32,'[MODE=面试日历]')<=0)" : "")
		.append(getWhere_string("a.c60", oldtid))
		.append(getWhere_startNumber(sortid, mindataid, false))
		.append(getWhere_endNumber(sortid, maxdataid, false))
		.append(" order by ").append(orderby);
        return rtn.toString();
	}
	
	//事件
	public final static String ACTION_HR = "insert into B_Action(dataid,c1,c2,c3,c4,c5,c6) values(seqB_Action.Nextval,'%s','%s','%s','%s','%s','%s')";
	
	//添加合同
	public final static String ADD_AGREEMENT = "insert into B_MMORGAN_AGREEMENT(dataid,parentDataid,c1,c3,c4,c10) values(seqB_MMORGAN_AGREEMENT.Nextval,'%s','%s','%s','%s','%s')";

	//HR名称
	public final static String LWHR_QueryByC(String keyword, String AMID,int status){
		return new StringBuilder("select a.c4 hrName, a.c1 auid,a.c2 amid ")
		.append(" from B_HR a where a.c18=130001")
		.append(getWhere_like("a.c4", keyword)) //姓名
		.append(getWhere_string("a.c2", AMID))
		.append(" and a.c17="+status)
		.toString();
	}
	//添加候选人被邀请列表
	public final static String LWBT_Candidate_InviteLog_ADD = "insert into B_T_Candidate_InviteLog(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10) values(seqB_T_Candidate_InviteLog.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
	public final static String LWBT_Candidate_InviteLog_UPDATE = "update B_T_Candidate_InviteLog set c9=%s,c10='%s',uptdate=sysdate where c1='%s' and c5='%s' and c12=0";
	//被邀请候选人列表
	public final static String LWCandidate_Invited_QueryByC(String cid, String hrAuid, String status, String keyword, String name){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.* ")
		.append("from B_T_CANDIDATE_INVITELOG a where 1=1")
		.append(getWhere_string("a.c1", cid))
		.append(getWhere_string("a.c5", hrAuid))
		.append(getWhere_number("a.c12", status))
		.append(getWhere_string("a.keyword", keyword))
		.append(getWhere_like("a.c6||a.c8", name))
		.append(" order by sortid desc");
		return rtn.toString();
	};
	//填充学校名称
	public final static String LWSchool_QueryByC(String col, String keyword, String status){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.* ")
		.append("from A_SCHOOL a where 1=1")
		.append(getWhere_like(col, keyword))
		.append(getWhere_string("c2", status))
		.append(" order by sortid desc");
		return rtn.toString();
	}
	//查询猎头公司
	public static String LWHCCompany_QueryByC(String CompanyStatus, String HCStatus,
			String keyword) {
		StringBuilder rtn = new StringBuilder();
		rtn.append("select distinct a.dataid sortid,a.c2 companyName ")
		.append("from B_MMORGAN a, B_HUNTER b where a.c1 = b.c2")
		.append(getWhere_like("a.c2", keyword))
		.append(" order by sortid desc");
		return rtn.toString();
	}
	//候选人备注修改
	public final static String LWCandidate_status_UpdateByC = "update B_T_CANDIDATE_INVITELOG set c11='%s',c12='%s',c13='%s',c14='%s',c15=sysdate where c1='%s'";
	
	//候选人列表
	public final static String LWCandidate_QueryByC = "select a.* from B_CANDIDATE a where a.c1='%s'";
	//添加用户问题反馈
	public final static String LWUserQuestion_Add = "insert into B_T_USERQUESTION(dataid,c1,c2,c3,c4,c5,c6,c7,c8) values(seqb_t_userquestion.Nextval,'%s','%s','%s','%s','%s','%s','%s',0)";
	//统计候选人信息
	public static String LWCandidate_Stat(String condition,String keyword,String status,String mindataid,String maxdataid,String order){
		if(order.length()<=0){
			order = "sortid desc";
		}
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.* ")
		.append("from B_CANDIDATE a where a.c69=130001")
		.append(getWhere_like(condition, keyword))
		.append(getWhere_in("c3", status))
		.append(getWhere_startNumber("a.dataid", mindataid, false))
		.append(getWhere_endNumber("a.dataid", maxdataid, false))
		.append(" order by ").append(order);
		return rtn.toString();
	}
	
	//统计客户公司
	public static String LWCompany_Stat(String keyword,String order){
		if(order==null || order.length()<=0){
			order = "sortid desc";
		}
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,(select count(t.c1) from B_JOB t where t.c4 = 1 and t.c5=a.c1) nums,(select to_date(t.c4,'yyyy-MM-dd')-sysdate from B_MMORGAN_AGREEMENT t where t.c1=a.c1 and rownum=1) rtn_time,a.c1 amid,a.c2 companyName,count(b.dataid) hrNum,sum(b.c19) totalJob,sum(b.c20) storeNum,sum(b.c21) inviteNum,sum(b.c22) acceptNum,sum(b.c23) offerNum,sum(b.c24) acceptOffer,sum(b.c25) entryNum,sum(b.c26) dimission,sum(b.c27) bill,sum(b.c28) pay ")
		.append(" from b_Mmorgan a,b_hr b where a.c1=b.c2 ")
		.append(getWhere_like("a.c2", keyword))
		.append("group by a.c1,a.c2,a.dataid")
		.append(" order by ").append(order);
		return rtn.toString();
	}
	
	//统计公司合同状态
	public static String LWAgreeMent_Company_Stat(String amid){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select to_date(t.c4,'yyyy-MM-dd')-sysdate rtn_time from B_MMORGAN_AGREEMENT t where 1=1")
		.append(getWhere_string("t.c1", amid));
		return rtn.toString();
	}
	
	//统计公司详细信息
	public static final String LWOrgan_Detail_Stat = "select b.dataid sortid,b.c14 logo,b.c2 shortName,b.c1 name,b.c3 englishName,b.c13 nature,b.c16 createtime,b.c17 amount,a.c3 parent,b.c8 bigFactory,b.c9 factory,b.c10 city,b.c21 code,b.c22 switchboard,b.c24 netAddress,b.c18 addr1,b.c19 addr2,b.c20 addr3,b.c12 introduce,b.c15 license from B_MMORGAN a,A_ORGAN b where a.c2=b.c5||'['||b.c1||']' and a.c1='%s'";
	//统计公司详细信息 - from MMORGAN
	public static final String LWMMOrgan_Detail_Stat = "select a.dataid sortid,a.c32 logo,a.c28 shortName,a.c2 name,a.c29 englishName,a.c5 nature,a.c34 createtime,a.c35 amount,a.c3 parent,'' bigfactory,a.c7 factory,a.c9 city,a.c38 code,a.c39 switchboard,a.c40 netAddress,a.c14 addr1,a.c36 addr2,a.c37 addr3,a.c31 introduce,a.c33 license from B_MMORGAN a where a.c1='%s'";
	
	//统计HR信息
	public static String LWHR_List_Stat(String keyword,String order){
		if(order==null || order.length()<=0){
			order = "lastLogin desc";
		}
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,d.c39 lastLogin,a.c1 auid,a.c4 HrName,b.c28 short_name,to_date(c.c4,'yyyy-MM-dd')-sysdate agreement,a.c19 hrJob,a.c20 overview,a.c21 inviteNum,a.c22 acceptNum,a.c23 offerNum,a.c24 acceptofferNum,a.c25 entryNum,a.c26 dimission,a.c27 bill,a.c28 pay,a.c29 views from B_HR a,B_MMORGAN b,b_Mmorgan_Agreement c,b_userlogin d where a.c2=b.c1(+) and b.c1=c.c1(+) and a.c1=d.c1(+) and d.c39 is not null")
		.append(getWhere_like("a.c4", keyword))
		.append(" group by a.c1,a.c4,b.c28,c.c4,a.c19,a.c20,a.c21,a.c22,a.c23,a.c24,a.c25,a.c26,a.c27,a.c28,a.c29,a.dataid,d.c39")
		.append(" order by ").append(order);
		return rtn.toString();
	};
	
	//统计职位信息
	public static String LWJob_List_Stat(String keyword,String order){
		if(order==null || order.length()<=0){
			order = "sortid desc";
		}
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.c1 jobName,a.c4 status,a.uptdate updateTime,c.c4 hrName,b.c2 short_name,to_date(d.c4,'yyyy-MM-dd')-sysdate agreement,c.c21 inviteNum,c.c22 acceptNum,c.c23 offerNum,c.c24 acceptofferNum,c.c25 entryNum,c.c26 dimission from b_job a,b_mmorgan b,b_hr c,b_mmorgan_agreement d where a.c5=b.c1(+) and a.c2=c.c1(+) and b.c1=d.c1(+)")
		.append(getWhere_like("a.c1", keyword))
		.append("order by ").append(order);
		return rtn.toString();
	};
	
	//精确查询HC
	public static String LWHC_QueryByc(String c,String keyword){
		return new StringBuilder("select a.dataid sortid,a.c1 HCAuid,a.c4 HCName,b.c2 CompanyName,b.c28 shortName,a.c7 email,a.c6 phone")
		   .append(" from b_hunter a, b_mmorgan b where a.c2=b.c1(+) and a.c18='130001'")
		   .append(getWhere_like(c, keyword))
		   .append(" order by sortid")
		   .toString();
	}
	
	//特别推荐HR列表
	public static final String LWCandidate_Command_HRList = "select a.c4 HRAuid,count(a.c1) sum,b.c4 HRName,c.c28 short_Name,c.c2 CompanyName,b.c7 email,c.c32 logo from b_Candidate_Organ_Relation a,b_hr b,b_mmorgan c where a.c4=b.c1 and b.c2=c.c1 and a.c5=1  group by a.c4,b.c4,c.c28,c.c2,b.c7,c.c32";
	//下周预备特别推荐HR列表
	public static final String LWCandidate_Command_HRListNextWeek = "select a.c4 HRAuid,count(a.c1) sum,b.c4 HRName,c.c28 short_Name,c.c2 CompanyName,b.c7 email,c.c32 logo from b_Candidate_Organ_Relation a,b_hr b,b_mmorgan c where a.c4=b.c1 and b.c2=c.c1 and a.c5=2  group by a.c4,b.c4,c.c28,c.c2,b.c7,c.c32";
	
	//特别推荐候选人列表
	public static String LWCandidate_Command_CandidateList(String auid){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select b.c1 auid,b.c61 photo,b.c8 chiName,b.c9 engName,b.c41 recentCompany,b.c42 recentJob from B_CANDIDATE_ORGAN_RELATION a,b_candidate b where a.c1=b.c1 and a.c5=1")
		.append(getWhere_string("a.c4", auid));
		return rtn.toString();
	}
	public static String LWCandidate_Command_CandidateListNextWeek(String auid){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select b.c1 auid,b.c61 photo,b.c8 chiName,b.c9 engName,b.c41 recentCompany,b.c42 recentJob from B_CANDIDATE_ORGAN_RELATION a,b_candidate b where a.c1=b.c1 and a.c5=2")
		.append(getWhere_string("a.c4", auid));
		return rtn.toString();
	}
	
	//候选人移出特别推荐
	public static final String LWCandidate_Command_removeCandidate = "delete from  B_CANDIDATE_ORGAN_RELATION a where c1='%s' and c4='%s' and c5='%s'";
	
	//移出所有下周推荐候选人
	public static final String LWCandidate_Command_deleteHR_NextWeek = "delete from B_CANDIDATE_ORGAN_RELATION where c4='%s' and c5=2";
	
	//查询添加HR是否存在
	public static final String LWCandidate_SpecialCommand_existHR = "select a.*,a.c5 status from B_CANDIDATE_ORGAN_RELATION a where a.c4='%s' and a.c5 in(0,2,3)";
	
	//新增预备候选人HR查询
	public static String LWCandidate_Command_queryHR(String c,String keyword){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select c.c5 status,a.dataid sortid,a.c1 hrAuid,b.c32 logo,b.c2 CompanyName,b.c28 shortName,a.c4 HRName,a.c7 email")
		.append(" from B_HR a,B_MMORGAN b,B_CANDIDATE_ORGAN_RELATION c where a.c2=b.c1(+) and a.c18=130001 and a.c1=c.c4(+)")
		.append(getWhere_string(c, keyword))
		.append(" order by sortid");
		return rtn.toString();
	}
	
	//新增特别推荐——添加HR
	public static final String LWCandidate_SpecialCommand_addHR = "insert into B_CANDIDATE_ORGAN_RELATION(dataid,parentdataid,c2,c4,c5) select seqb_candidate_organ_relation.nextval,a.dataid,a.c2,a.c1,0 from B_HR a where a.c1='%s'";
	//新增特别推荐-更新HR
	public static final String LWCandidate_SpecialCommand_updateHR = "update B_CANDIDATE_ORGAN_RELATION set c5=0 where c4='%s'";
	
	//新增特别推荐-查询已添加HR
	public static final String LWCandidate_SpecialCommand_queryHR = "select a.c4 HRAuid,count(a.c1) sum,b.c4 HRName,c.c28 short_Name,c.c2 CompanyName,b.c7 email,c.c32 logo from b_Candidate_Organ_Relation a,b_hr b,b_mmorgan c where a.c4=b.c1 and b.c2=c.c1 and a.c5 in(0,2) group by a.c4,b.c4,c.c28,c.c2,b.c7,c.c32";
	
	//新增特别推荐-删除HR
	public static final String LWCandidate_SpecialCommand_deleteHR = "delete from  B_CANDIDATE_ORGAN_RELATION where c4='%s' and c5 in(0,2)";
	
	//新增特别推荐-查询所有候选人
	public static String LWCandidate_SpecialCommand_queryCandidate(String c,String keyword,String c3Status,String c5Status){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select b.dataid sortid,b.c91 ace,b.c92 aceTime,b.c1 auid,b.c61 photo,b.c8 chiName,b.c9 engName,b.c41 recentCompany,b.c42 recentJob ")
		.append("from B_Candidate b where b.c69='130001'")
		.append(getWhere_string("b.c3", c3Status))
		.append(getWhere_string("b.c5", c5Status))
		.append(getWhere_string(c, keyword))
		.append(" order by sortid");
		return rtn.toString();
	}
	
	//新增特别推荐-查询HR是否已添加
	public static final String LWCandidate_SpecialCommand_queryExitsHR = "select a.* from B_CANDIDATE_ORGAN_RELATION a where a.c4='%s' and a.c5=0";
	
	//新增特别推荐-更新HR-推荐候选人
	public static final String LWCandidate_SpecialCommand_updateHRCandi = "update B_CANDIDATE_ORGAN_RELATION set c5=2 where c4='%s' and c1 is null";
	
	//新增特别推荐-新增HR-推荐候选人
	public static final String LWCandidate_SpecialCommand_insertHRCandi = "insert into B_CANDIDATE_ORGAN_RELATION(dataid,parentdataid,c1,c2,c4,c5) select seqb_candidate_organ_relation.nextval,a.dataid,'%s',a.c2,a.c1,2 from B_HR a where a.c1='%s'";
 	
	//新增特别推荐-校验记录已存在
	public static final String LWCandidate_SpecialCommand_checkExitsRecord = "select a.* from B_CANDIDATE_ORGAN_RELATION a where a.c1='%s' and a.c4='%s' and a.c5=2";
	
	//新增特别推荐-移除添加下周预备候选人
	public static final String LWCandidate_SpecialCommand_removeCandidateFromHR = "delete from  B_CANDIDATE_ORGAN_RELATION where c1='%s' and c4='%s' and c5=2";
	
	//新增特别推荐-校验候选人是否已经特别推荐给HR
	public static final String LWCandidate_SpecialCommand_checkCandidate = "select a.* from B_CANDIDATE_ORGAN_RELATION a where a.c1='%s' and a.c4='%s' and a.c5=2";
	
	//特别推荐列表-统计本周推荐HR
	public static final String LWCandidate_SpecialCommand_thisWeekHRAmount = "select count(t.amount) amounts from (select count(a.c4) amount from B_CANDIDATE_ORGAN_RELATION a where a.c5=1 group by a.c4) t ";
	
	//特别推荐列表-统计下周推荐HR
	public static final String LWCandidate_SpecialCommand_nextWeekHRAmount = "select count(t.amount) amounts from (select count(a.c4) amount from B_CANDIDATE_ORGAN_RELATION a where a.c5=2 group by a.c4) t ";
	
	//HR数据导出-候选人浏览记录
	public static String LWStatHR_exportBrowse_M(String begin,String end){
		StringBuilder rtn = new StringBuilder();
		String c1 = "";
		String c2 = "";
		if(begin.length()>0){
			c1 = " and a.uptdate >= to_date('"+begin+"','YYYY-MM-DD')";
		}
		if(end.length()>0){
			c2 = " and a.uptdate <= to_date('"+end+"','YYYY-MM-DD')+1";
		}
		rtn.append("select distinct a.dataid sortid,d.c2 companyName,b.c4 HRName,b.c6 phone,b.c7 email,c.c8 candiName,a.UPTDATE overViewTime,c.c42 job,c.c41 company from B_T_CANDIDATE_VIEWLOG a,B_HR b,B_CANDIDATE c,B_MMORGAN d where a.c1=c.c1(+) and a.c2=b.c1(+) and b.c2=d.c1")
		.append(c1).append(c2);
		return rtn.toString();
	} 
	
	//HR数据导出-候选人收藏记录
	public static String LWStatHR_exportStore_M(String begin,String end){
		StringBuilder rtn = new StringBuilder();
		String c1 = "";
		String c2 = "";
		if(begin.length()>0){
			c1 = " and c.c22 >= to_date('"+begin+"','YYYY-MM-DD')";
		}
		if(end.length()>0){
			c2 = " and c.c22 <= to_date('"+end+"','YYYY-MM-DD')+1";
		}
		rtn.append("select distinct c.dataid sortid,a.c4 hrName, d.c2 companyName,a.c6 phone,a.c7 email,b.c8 candiName,c.c22 storeTime,b.c41 company,b.c42 job from B_HR a,B_CANDIDATE b,B_CANDIDATE_PROCESS c,B_MMORGAN d where a.c1=c.c4 and b.c1=c.c1 and a.c2=d.c1 and c.c21='242001'")
		.append(c1).append(c2);
		return rtn.toString();
	} 
	
	//HR数据导出-候选人被邀请面试记录
	public static String LWStatHR_exportInvite_M(String begin,String end){
		StringBuilder rtn = new StringBuilder();
		String c1 = "";
		String c2 = "";
		if(begin.length()>0){
			c1 = " and c.istdate >= to_date('"+begin+"','YYYY-MM-DD')";
		}
		if(end.length()>0){
			c2 = " and c.istdate <= to_date('"+end+"','YYYY-MM-DD')+1";
		}
		rtn.append("select distinct c.dataid sortid,a.c4 hrName, d.c2 companyName,a.c6 phone,a.c7 email,b.c8 candiName,c.istdate inviteTime,b.c41 company,b.c42 job from B_HR a,B_CANDIDATE b,B_T_CANDIDATE_INVITELOG c,B_MMORGAN d where a.c1=c.c5 and b.c1=c.c1 and a.c2=d.c1")
		.append(c1).append(c2);
		return rtn.toString();
	}
	
	//HR数据统计-日数据
	public static final String LWStat_HRData_DAY = "select a.dataid sortid,to_date(a.c2,'yyyy-MM-dd') day,a.c3 LoginHR,a.c4 addHR,a.c6 amountLoginHR,a.c8 overview,a.c9 store,a.c10 invite from B_STAT_HRDATA a where a.c1=1 and a.c2>=20170424 order by day desc";
	
	//HR数据统计-周数据
	public static final String LWStat_HRData_Week = "select a.dataid sortid,a.c2 week,a.c11 dateTime,a.c3 loginHR,a.c4 addHR,a.c5 HRRemain,a.c6 totalHR,a.c7 goodCandiThisWeek,a.c8 overview,a.c9 store,a.c10 invite from B_STAT_HRDATA a where a.c1=2 and a.c2 >= 1 order by week desc";
	
	//HR数据统计-月数据
	public static final String LWStat_HRData_Month = "select a.dataid sortid,a.c2 month,a.c3 LoginHR,a.c4 addHR,a.c5 remainHRlastMon,a.c6 totalHR,a.c8 overview,a.c9 store,a.c10 invite from B_STAT_HRDATA a where a.c1=3 order by month desc";
	
	//M-更新公司-列表
	public static String LWHR_AORGAN_List(String c,String keyword){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.c1 fullName,a.c2 shortName,a.c14 logo,a.istdate time")
		.append(" from A_ORGAN a where a.c26=130001")
		.append(getWhere_like(c, keyword))
		.append("order by time desc");
		return rtn.toString();
	}
	//M-更新公司-详情
	public static String LWHR_AORGAN_Detail(String fullName,String shortName){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.c1 fullName,a.c2 shortName,a.c3 engName,a.c4 nickName,a.c13 type,a.c6 mother,a.c16 createTime,a.c17 amount,a.c12 introduce,a.c8 factory,a.c9 sortFactory,a.c10 city,a.c21 code,a.c22 telephone,a.c24 net,a.c18 addr1,a.c19 addr2,a.c20 addr3,a.c14 logo,a.c15 license")
		.append(" from A_ORGAN a where a.c26=130001")
		.append(getWhere_string("a.c1", fullName))
		.append(getWhere_string("a.c2", shortName));
		return rtn.toString();
	}
	
	//更新A_Organ
	public static final String LWAORGAN_EDIT = "update A_ORGAN a set a.c1='%s',a.c2='%s',a.c3='%s',a.c4='%s',a.c13='%s',a.c6='%s',a.c16='%s',a.c17='%s',a.c12='%s',a.c8='%s',a.c9='%s',a.c10='%s',a.c21='%s',a.c22='%s',a.c24='%s',a.c18='%s',a.c19='%s',a.c20='%s',a.c14='%s',a.c15='%s' where a.c2='%s'";
	
	//M-新增公司-列表
	public static String LWHR_NEWMMORGAN_LIST(String status,String c,String keyword){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.dataid sortid,a.c1 amid,a.c2 fullName,a.c28 shortName,a.c32 logo,a.istdate time")
		.append(" from B_MMORgan a where a.c8=130001")
		.append(getWhere_like(c, keyword))
		.append(getWhere_string("a.c6", status));
		return rtn.toString();
	}
	
	//M-新增公司-详情
	public static String LWHR_NEWMMORGAN_DETAIL(String amid){
		StringBuilder rtn = new StringBuilder();
		rtn.append("select a.c1 amid,a.c2 fullName,a.c28 shortName,a.c29 engName,a.c30 nickName,a.c5 type,a.c3 mother,a.c34 createTime,a.c35 amount,a.c31 introduce,a.c7 factory,'' sortFactoy,a.c9 city,a.c38 code,a.c39 telephone,a.c40 net,a.c14 addr1,a.c36 addr2,a.c37 addr3,a.c32 logo,a.c33 license")
		.append(" from B_MMORGAN a where a.c8=130001")
		.append(getWhere_string("a.c1", amid));
		return rtn.toString();
	}
	
	//M-新增公司-AORGAN
	public static final String LWHR_NEWMMORGAN_ADD = "insert into B_MMORGAN(dataid,c1,c2,c28,c29,c30,c5,c3,c34,c35,c31,c7,c9,c38,c39,c40,c14,c36,c37,c32,c33,c6,c8) values(seqB_MMOrgan.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',130001)";
	
	//M-新增公司
	public static final String LWHR_NEWAORGAN_ADD = "insert into A_ORGAN(dataid,c1,c2,c3,c4,c13,c6,c16,c17,c12,c8,c9,c10,c21,c22,c24,c18,c19,c20,c14,c15,c26) values(seqA_ORGAN.Nextval,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',130001)";
	
	//M-新增公司-更新MMorgan
	public static final String LWHR_NEWMMORGAN_EDIT = "update B_MMORGAN set c2='%s',c28='%s',c29='%s',c30='%s',c5='%s',c3='%s',c34='%s',c35='%s',c31='%s',c7='%s',c9='%s',c38='%s',c39='%s',c40='%s',c14='%s',c36='%s',c37='%s',c32='%s',c33='%s',c6='%s' where c1='%s'";
	
	//M-检查HR公司是否入库
	public static final String LWHR_EXITORGAN = "select a.* from A_ORGAN a where a.c1='%s'";
	
}
