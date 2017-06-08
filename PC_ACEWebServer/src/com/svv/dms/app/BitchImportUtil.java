package com.svv.dms.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gs.db.dao.DaoUtil;
import com.gs.db.util.DBUtil;
import com.svv.dms.web.UGID;
import com.svv.dms.web.dao.SQL;
import com.svv.dms.web.entity.I_DataParamType;
import com.svv.dms.web.util.DES;
import com.svv.dms.web.util.HIUtil;

/**
 * Excel数据表格批量导入工具类
 * @author gq289
 *
 */
public class BitchImportUtil extends LWBaseBean{
	
	public static LWResult bitchInsertHR(List list, InputStream in) {
		LWResult result;
		String memo = "批量导入HR";
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			String hrName;
			String hrJob;
			String hrCompany;
			String addr;
			String email;
			String mobile;
			String phone;
			String region;
			String code1;
			String code2;
			BMMOrgan mm = null;
			String password = DES.md5("123456");
			
			 int a = 0;
			 int b = 0;
			 int c = 0;
			 int i = 0;
			 
			List<Object> nums = HIUtil.dbQuery("select min(dataid) num from B_MMOrgan where dataid between 2000 and 5000");
			if(nums!=null&&nums.size()>0){
				for (Object o : nums) {
					i = DBUtil.getDBInt(o, "num")-1;
				}
			}
			 List<Object> hrs = HIUtil.dbQuery("select min(dataid) num from B_HR where dataid between 2000 and 5000");
			 if(hrs!=null&&hrs.size()>0){
				 for (Object o : hrs) {
					a = DBUtil.getDBInt(o, "num")-1;
				}
			 }
			 List<Object> users = HIUtil.dbQuery("select min(dataid) num from B_UserLogin where dataid between 2000 and 5000");
			 if(users!=null&&users.size()>0){
				 for (Object o : users) {
					b = DBUtil.getDBInt(o, "num")-1;
				}
			 }
			 
			 List<Object> codes = HIUtil.dbQuery("select min(dataid) num from B_InviteCode where dataid between 5000 and 10000");
			 if(codes!=null&&codes.size()>0){
				 for (Object o : codes) {
					c = DBUtil.getDBInt(o, "num")-1;
				}
			 }
			if(sheet==null){
				return result = new LWResult(LWResult.FAILURE,memo+"数据表为空");
			}
			ArrayList<String> sqls1 = new ArrayList<String>();
			Map<String, String> map = new HashMap<String,String>();
			Map<String, String> map1 = new HashMap<String,String>();
			//查询库中已存在的公司
			
			if(list!=null && list.size()>0){
				for (Object o : list) {
					map.put(DBUtil.getDBString(o, "c28").replace("'", ""), DBUtil.getDBString(o, "c14"));
				}
			}
			for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
				XSSFRow row = sheet.getRow(rowNum); 
				if (row == null) {
			        continue;
			    }
				if(rowNum<2) continue;
				
				XSSFCell xssfCell;
				xssfCell = row.getCell(2); hrCompany = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司
				xssfCell = row.getCell(3); addr = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司具体地址
				
				//公司是否存在
				if(!hrCompany.equals("") && hrCompany!=null){
					if(map.get(hrCompany.replace("'", ""))==null){
						map.put(hrCompany.replace("'", ""), addr);
						map1.put(hrCompany, addr);
					}
				}
			}
			if(map1.size()>=1){
				Set<Entry<String, String>> entrySet = map1.entrySet();
				String organName = "";
				for (Entry<String, String> entry : entrySet) {
					String hrCompanyName = entry.getKey();
					String address = entry.getValue();
					String amid = UGID.createUGID();
					List<Object> fullNames = HIUtil.dbQuery(String.format("select a.c1 from A_ORGAN a where a.c2='%s'", new Object[]{hrCompanyName}));
					if(fullNames!=null&&fullNames.size()>0){
						for (Object o : fullNames) {
							organName = DBUtil.getDBString(o, "c1");
							System.out.println("公司全称 ++++++++"+organName);
						}
					}
					sqls1.add(String.format("insert into B_MMOrgan(dataid,c1,c2,c28,c5,c6,c8,c14) values('%s','%s','%s','%s','%s','%s','%s','%s')",new Object[]{i--,amid,organName,hrCompanyName,"220001","203001","130001",address}));
				}
			}
				if(sqls1.size()>0){
					DaoUtil.dbExe(sqls1);
				}	
				 List<String> sqls2 = new ArrayList<String>();
				
				 
				 Map<String, String> user = new HashMap<String,String>();
				 StringBuilder s = new StringBuilder();
				 for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
					XSSFRow row = sheet.getRow(rowNum);
					if (row == null) {
			             continue;
			         }
					if(rowNum<2) continue;
					
					XSSFCell xssfCell;
					xssfCell = row.getCell(0); hrName = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr名称
					xssfCell = row.getCell(1); hrJob = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr职位
					xssfCell = row.getCell(2); hrCompany = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司
					xssfCell = row.getCell(4); email = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr邮箱
					xssfCell = row.getCell(5); mobile = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr手机号码
					xssfCell = row.getCell(6); phone = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr座机号码
					xssfCell = row.getCell(7); code1 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//主邀请码
					xssfCell = row.getCell(8); code2 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//二次邀请码
					
					
					List<Object> list1 = HIUtil.dbQuery(String.format(YSSQL.LWMMOrgan_QueryByOrganSimpleName, new Object[]{hrCompany}));
					if(list1!=null && list1.size()>0){
						Object o = list1.get(0);
						mm = new BMMOrgan(Constants.USER2_MMGROUP, DBUtil.getDBLong(o, "dataid"), DBUtil.getDBString(o, "c1"), hrCompany);
					}
					//如果email,或者电话号码重复，则不添加
					String auid = "";
					if(mobile.length()>0){
						if(user.get(email)==null && user.get(mobile)==null){
							//添加用户
							if(!hrName.equals("") && hrName!=null){
								auid = UGID.createUGID();
								// HR c1auid,c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
								sqls2.add(String.format("insert into B_HR(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c16,c18,c33,c34,c35,c36,c37,c38) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', 130001,'%s','%s','%s','%s','%s','%s')", new Object[]{a--,auid,mm.getAmid(),mm.getMid(),hrName,"",mobile,email,phone,hrCompany,hrJob,"","","","","",""}));
								sqls2.add(String.format("insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c25,c12,c14,c17,c19,c31,c32,c33, c40) values('%s','%s','%s','%s',%s,%s,'%s',130000,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', %s)",new Object[]{b--,auid,mobile,password,Constants.USER2_MMGROUP,mm.getMid(),mm.getAmid(),"","","","","","","", phone, email, hrJob, 1}));
								//插入一级邀请码
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')",new Object[]{c--,code1,mm.getAmid(),auid,1,0,""}));
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')", new Object[]{c--,code2,mm.getAmid(),"",1,1,auid}));
							}
							if(email!=null || !email.equals("")){
								user.put(email, hrName);
							}
							if(mobile!=null || !mobile.equals("")){
								user.put(mobile, hrName);
							}
						}else{
							s.append(hrName).append(Constants.SPLITER);
						}
					}else{
						if(user.get(email)==null){
							//添加用户
							if(!hrName.equals("") && hrName!=null){
								auid = UGID.createUGID();
								// HR c1auid,c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
								sqls2.add(String.format("insert into B_HR(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c16,c18,c33,c34,c35,c36,c37,c38) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', 130001,'%s','%s','%s','%s','%s','%s')", new Object[]{a--,auid,mm.getAmid(),mm.getMid(),hrName,"",mobile,email,phone,hrCompany,hrJob,"","","","","",""}));
								sqls2.add(String.format("insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c25,c12,c14,c17,c19,c31,c32,c33, c40) values('%s','%s','%s','%s',%s,%s,'%s',130000,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', %s)",new Object[]{b--,auid,mobile,password,Constants.USER2_MMGROUP,mm.getMid(),mm.getAmid(),"","","","","","","", phone, email, hrJob, 1}));
								//插入一级邀请码
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')",new Object[]{a--,code1,mm.getAmid(),auid,1,0,""}));
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')", new Object[]{a--,code2,mm.getAmid(),"",1,1,auid}));
							}
							if(email!=null || !email.equals("")){
								user.put(email, hrName);
							}
						}
					}
				 }
				 if(s.length()>0){
					 s.deleteCharAt(s.length()-1);
				 }
				 if(DaoUtil.dbExe(sqls2)!=null){
					 StringBuilder s1 = new StringBuilder();
					 for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
							XSSFRow row = sheet.getRow(rowNum);
							if (row == null) {
					             continue;
					         }
							if(rowNum<2) continue;
							
							XSSFCell xssfCell;
							xssfCell = row.getCell(0); hrName = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr名称
							xssfCell = row.getCell(7); code1 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//主邀请码
							xssfCell = row.getCell(8); code2 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//二次邀请码
							
							List<Object> code1s = HIUtil.dbQuery(String.format("select * from B_InviteCode where c1='%s'", new Object[]{code1}));
							List<Object> code2s = HIUtil.dbQuery(String.format("select * from B_InviteCode where c1='%s'", new Object[]{code2}));
							if(code1s.size()>1 || code2s.size()>1){
								s1.append(hrName).append(Constants.SPLITER);
							}
					 }
					 if(s1.length()>0){
						 s1.deleteCharAt(s1.length()-1);
					 }
					 return result = new LWResult(LWResult.SUCCESS ,memo+"数据表成功,其中"+s.toString()+"的邮箱或手机号重复"+",其中"+s1.toString()+"的邀请码重复");
				 }else{
					 return result = new LWResult(LWResult.FAILURE,memo+"数据表失败");
				 }
		} catch (IOException e) {
			return result = new LWResult(LWResult.FAILURE,memo+"数据表异常");
		}
	}
	
	public static LWResult bitchImportHR(List list, InputStream in) {
		LWResult result;
		String memo = "批量导入HR";
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			String hrName;
			String hrJob;
			String hrCompany;
			String addr;
			String email;
			String mobile;
			String phone;
			String region;
			String code1;
			String code2;
			BMMOrgan mm = null;
			String password = DES.md5("123456");
			if(sheet==null){
				return result = new LWResult(LWResult.FAILURE,memo+"数据表为空");
			}
			ArrayList<String> sqls1 = new ArrayList<String>();
			Map<String, String> map = new HashMap<String,String>();
			Map<String, String> map1 = new HashMap<String,String>();
			//查询库中已存在的公司
			
			if(list!=null && list.size()>0){
				for (Object o : list) {
					map.put(DBUtil.getDBString(o, "c28").replace("'", ""), DBUtil.getDBString(o, "c14"));
				}
			}
			for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
				XSSFRow row = sheet.getRow(rowNum); 
				if (row == null) {
			        continue;
			    }
				if(rowNum<2) continue;
				
				XSSFCell xssfCell;
				xssfCell = row.getCell(2); hrCompany = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司
				xssfCell = row.getCell(3); addr = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司具体地址
				
				//公司是否存在
				if(!hrCompany.equals("") && hrCompany!=null){
					if(map.get(hrCompany.replace("'", ""))==null){
						map.put(hrCompany.replace("'", ""), addr);
						map1.put(hrCompany, addr);
					}
				}
			}
			if(map1.size()>=1){
				Set<Entry<String, String>> entrySet = map1.entrySet();
				int i = 5000;
				String organName = "";
				for (Entry<String, String> entry : entrySet) {
					String hrCompanyName = entry.getKey();
					String address = entry.getValue();
					String amid = UGID.createUGID();
					List<Object> fullNames = HIUtil.dbQuery(String.format("select a.c1 from A_ORGAN a where a.c2='%s'", new Object[]{hrCompanyName}));
					if(fullNames!=null&&fullNames.size()>0){
						for (Object o : fullNames) {
							organName = DBUtil.getDBString(o, "c1");
							System.out.println("公司全称 ++++++++"+organName);
						}
					}
					sqls1.add(String.format("insert into B_MMOrgan(dataid,c1,c2,c28,c5,c6,c8,c14) values('%s','%s','%s','%s','%s','%s','%s','%s')",new Object[]{i--,amid,organName,hrCompanyName,"220001","203001","130001",address}));
				}
			}
				if(sqls1.size()>0){
					DaoUtil.dbExe(sqls1);
				}	
				 List<String> sqls2 = new ArrayList<String>();
				 int i = 5000;
				 int j = 5000;
				 int a = 10000;
				 Map<String, String> user = new HashMap<String,String>();
				 StringBuilder s = new StringBuilder();
				 for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
					XSSFRow row = sheet.getRow(rowNum);
					if (row == null) {
			             continue;
			         }
					if(rowNum<2) continue;
					
					XSSFCell xssfCell;
					xssfCell = row.getCell(0); hrName = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr名称
					xssfCell = row.getCell(1); hrJob = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr职位
					xssfCell = row.getCell(2); hrCompany = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr所在公司
					xssfCell = row.getCell(4); email = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr邮箱
					xssfCell = row.getCell(5); mobile = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr手机号码
					xssfCell = row.getCell(6); phone = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr座机号码
					xssfCell = row.getCell(7); code1 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//主邀请码
					xssfCell = row.getCell(8); code2 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//二次邀请码
					
					
					List<Object> list1 = HIUtil.dbQuery(String.format(YSSQL.LWMMOrgan_QueryByOrganSimpleName, new Object[]{hrCompany}));
					if(list1!=null && list1.size()>0){
						Object o = list1.get(0);
						mm = new BMMOrgan(Constants.USER2_MMGROUP, DBUtil.getDBLong(o, "dataid"), DBUtil.getDBString(o, "c1"), hrCompany);
					}
					//如果email,或者电话号码重复，则不添加
					String auid = "";
					if(mobile.length()>0){
						if(user.get(email)==null && user.get(mobile)==null){
							//添加用户
							if(!hrName.equals("") && hrName!=null){
								auid = UGID.createUGID();
								// HR c1auid,c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
								sqls2.add(String.format("insert into B_HR(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c16,c18,c33,c34,c35,c36,c37,c38) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', 130001,'%s','%s','%s','%s','%s','%s')", new Object[]{i--,auid,mm.getAmid(),mm.getMid(),hrName,"",mobile,email,phone,hrCompany,hrJob,"","","","","",""}));
								sqls2.add(String.format("insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c25,c12,c14,c17,c19,c31,c32,c33, c40) values('%s','%s','%s','%s',%s,%s,'%s',130000,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', %s)",new Object[]{j--,auid,mobile,password,Constants.USER2_MMGROUP,mm.getMid(),mm.getAmid(),"","","","","","","", phone, email, hrJob, 1}));
								//插入一级邀请码
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')",new Object[]{a--,code1,mm.getAmid(),auid,1,0,""}));
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')", new Object[]{a--,code2,mm.getAmid(),"",1,1,auid}));
							}
							if(email!=null || !email.equals("")){
								user.put(email, hrName);
							}
							if(mobile!=null || !mobile.equals("")){
								user.put(mobile, hrName);
							}
						}else{
							s.append(hrName).append(Constants.SPLITER);
						}
					}else{
						if(user.get(email)==null){
							//添加用户
							if(!hrName.equals("") && hrName!=null){
								auid = UGID.createUGID();
								// HR c1auid,c2amid,c3 mid,c4姓名,c5性别,c6手机号,c7邮箱,c8座机,c16职位,c18状态,c33,c34,c35,c36,c37,c38
								sqls2.add(String.format("insert into B_HR(dataid,c1,c2,c3,c4,c5,c6,c7,c8,c9,c16,c18,c33,c34,c35,c36,c37,c38) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', 130001,'%s','%s','%s','%s','%s','%s')", new Object[]{i--,auid,mm.getAmid(),mm.getMid(),hrName,"",mobile,email,phone,hrCompany,hrJob,"","","","","",""}));
								sqls2.add(String.format("insert into B_UserLogin(dataid,c1,c2,c3,c4,c5,c6,c9,c11,c23,c25,c12,c14,c17,c19,c31,c32,c33, c40) values('%s','%s','%s','%s',%s,%s,'%s',130000,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', %s)",new Object[]{j--,auid,mobile,password,Constants.USER2_MMGROUP,mm.getMid(),mm.getAmid(),"","","","","","","", phone, email, hrJob, 1}));
								//插入一级邀请码
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')",new Object[]{a--,code1,mm.getAmid(),auid,1,0,""}));
								sqls2.add(String.format("insert into B_InviteCode(dataid,c1,c2,c3,c4,c5,c6,c9,c10) values('%s','%s','%s','%s','%s',130001,sysdate+90,'%s','%s')", new Object[]{a--,code2,mm.getAmid(),"",1,1,auid}));
							}
							if(email!=null || !email.equals("")){
								user.put(email, hrName);
							}
						}
					}
				 }
				 if(s.length()>0){
					 s.deleteCharAt(s.length()-1);
				 }
				 if(DaoUtil.dbExe(sqls2)!=null){
					 StringBuilder s1 = new StringBuilder();
					 for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
							XSSFRow row = sheet.getRow(rowNum);
							if (row == null) {
					             continue;
					         }
							if(rowNum<2) continue;
							
							XSSFCell xssfCell;
							xssfCell = row.getCell(0); hrName = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//hr名称
							xssfCell = row.getCell(7); code1 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//主邀请码
							xssfCell = row.getCell(8); code2 = xssfCell == null? "" : BitchImportUtil.getValue(xssfCell);//二次邀请码
							
							List<Object> code1s = HIUtil.dbQuery(String.format("select * from B_InviteCode where c1='%s'", new Object[]{code1}));
							List<Object> code2s = HIUtil.dbQuery(String.format("select * from B_InviteCode where c1='%s'", new Object[]{code2}));
							if(code1s.size()>1 || code2s.size()>1){
								s1.append(hrName).append(Constants.SPLITER);
							}
					 }
					 if(s1.length()>0){
						 s1.deleteCharAt(s1.length()-1);
					 }
					 return result = new LWResult(LWResult.SUCCESS ,memo+"数据表成功,其中"+s.toString()+"的邮箱或手机号重复"+",其中"+s1.toString()+"的邀请码重复");
				 }else{
					 return result = new LWResult(LWResult.FAILURE,memo+"数据表失败");
				 }
		} catch (IOException e) {
			return result = new LWResult(LWResult.FAILURE,memo+"数据表异常");
		}
	}
	
	
	public static String getValue(XSSFCell xssfCell, boolean stringFlag) {
    	String value = null;
    	if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
        	Double v = xssfCell.getNumericCellValue();
        	value = String.valueOf(v.intValue()).trim();
        } else {
        	value = String.valueOf(xssfCell.getStringCellValue()).trim();
        }
        return HIUtil.toDBStr(value);
    }
    @SuppressWarnings("static-access")
    public static String getValue(XSSFCell xssfCell) {
    	String value = null;
        if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
        	value = String.valueOf(xssfCell.getBooleanCellValue()).trim();
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
        	value = String.valueOf(xssfCell.getNumericCellValue()).trim();
        } else {
        	value = String.valueOf(xssfCell.getStringCellValue()).trim();
        }
        return HIUtil.toDBStr(value);
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getI_DataParamTypeMap(){
        HashMap<String, String> map = new HashMap<String, String>();
        List<I_DataParamType> list = HIUtil.getList(I_DataParamType.class, HIUtil.dbQuery(SQL.SP_I_DataParamTypeQueryByC("","")));
        if (list != null && list.size()>0){
            for (I_DataParamType o : list) {
                map.put(o.getClassName(), o.getParamClassID()+"");
            }
        }       
        return map;
    }
}
