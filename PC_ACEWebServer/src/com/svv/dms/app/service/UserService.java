package com.svv.dms.app.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.app.ComBeanAPPModule;
import com.svv.dms.app.ComBeanMMOrgan;
import com.svv.dms.app.Constants;
import com.svv.dms.app.LWBaseBean;
import com.svv.dms.app.LWResult;
import com.svv.dms.app.YSSQL;
import com.svv.dms.web.UGID;
import com.svv.dms.web.common.ComBeanLogType;
import com.svv.dms.web.service.base.BConstants;
import com.svv.dms.web.util.HIUtil;

public class UserService extends LWBaseBean {

	public String UserBService(){
		this.mmGroup = Integer.parseInt(Constants.USER2_MMGROUP);
		return this.exeByCmd("");
	}
	public String UserSService(){
		this.mmGroup = Integer.parseInt(Constants.USER3_MMGROUP);
		return this.exeByCmd("");
	}
	public String UserCService(){
		this.mmGroup = Integer.parseInt(Constants.USER1_MMGROUP);
		return this.exeByCmd("");
	}
	public String UserMService(){
		this.mmGroup = Integer.parseInt(Constants.USER9_MMGROUP);
		return this.exeByCmd("");
	}
	
	public String editForm(){
		logger.debug("[editForm]====================================");
		if(!ACCESS(false, false)) return BConstants.MESSAGE;
		LWResult result;
		String objectMode = this.getParameter("objectMode");
		String HTML = ComBeanAPPModule.getHtml(objectMode);
		
		List<Object> list = dbQuery(String.format(YSSQL.LWUserLogin_QueryByAUID, new Object[]{auid}));
		Object[] data = new Object[5];
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			int i = 0;
			data[i++] = DBUtil.getDBString(o, "auid");
			data[i++] = DBUtil.getDBString(o, "sex");
			data[i++] = DBUtil.getDBString(o, "phone");
			data[i++] = DBUtil.getDBString(o, "comPhone");
			data[i++] = DBUtil.getDBString(o, "comEmail");
			data[i++] = DBUtil.getDBString(o, "job");
		}
		String html = String.format(HTML, data);      
        result = new LWResult(LWResult.SUCCESS, html);
		return APPRETURN(result);
	}

	public String nickName(){
		logger.debug("[nickName]====================================");
		if(!ACCESS(true, false, getParameter("ww", 0)==1)) return BConstants.MESSAGE;		
		LWResult result;
		List<Object> list = dbQuery(String.format(YSSQL.LWUserLogin_QueryByAUID, new Object[]{auid}));
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			result = new LWResult(LWResult.SUCCESS, DBUtil.getDBString(o, "nickName"));
		}else{
			result = new LWResult(LWResult.FAILURE, "用户不存在");
		}
		return APPRETURN(result);
	}
	
	//认证
	public String recheck(){
			logger.debug("[recheck]====================================");
			if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
			LWResult result = null;
			try{
				String name = this.getParameter("name");
				String sex = this.getParameter("sex");
				String sid = this.getParameter("sid");
				String school = this.getParameter("school");
				String job = this.getParameter("school");
				try{ name = dES.decrypt(name); }catch(Exception e){}
				try{ sex = dES.decrypt(sex); }catch(Exception e){}
				try{ school = dES.decrypt(school); }catch(Exception e){}
				try{ job = dES.decrypt(job); }catch(Exception e){}
				try{ sid = dES.decrypt(sid); }catch(Exception e){}
				int N = this.getParameter("N", 0);

	        	System.out.println("["+auid+"]name："+name );
	        	System.out.println("["+auid+"]sex："+sex );
	        	System.out.println("["+auid+"]sid："+sid );
	        	System.out.println("["+auid+"]school："+school );
	        	System.out.println("["+auid+"]job："+job );
	        	System.out.println("["+auid+"]接收文件数目："+N );
	        	
				String[] files = new String[]{"", "", ""};
				for(int i=0; i<N; i++){
			    	String picid = UGID.createUGID();
					if(this.getParameter("uploadFlag", 1)==1){
						String fileName = this.uploadPic("file"+(i+1), PATH_DATAIMG, picid+".jpg", 500*1024, false, false, false, false, -1, -1);
						this.saveToResizeImageFile(PATH_DATAIMG+File.separator+fileName, PATH_DATAIMG+File.separator+fileName.replaceAll(".jpg", "_s.jpg"), 180, 180, false); //生成缩略图
						files[i] = Service_GetUpfileWebPath(PATH_DATAIMG)+fileName;
					}else{
						files[i] = this.getParameter("file"+(i+1));
						System.out.println("files["+(i+1)+"] url:"+files[i]);
					}
				}
				List<String> sqls = new ArrayList<String>();
				sqls.add(String.format(YSSQL.LWUserLogin_Recheck, new Object[]{name, sex, sid, school, job, files[0], files[1], files[2], auid}));
				if(dbExe(sqls)){
					result = new LWResult(LWResult.SUCCESS, "认证成功");
				}else{
					result = new LWResult(LWResult.FAILURE, "认证失败");
				}
		        loggerM(ComBeanLogType.TYPE_ADD, "认证");
			}catch(Exception e){
				e.printStackTrace();
				result = new LWResult(LWResult.EXCEPTION, "异常");
			}
			return APPRETURN(result);
	}
	
	//修改昵称
	public String editInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
			String nickName = this.getParameter("nickName");
			//if(!nickname.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_u4e00-u9fa5]+$")){
			if(!nickName.matches("[\u4e00-\u9fa5\\w]+")){
				result = new LWResult(LWResult.FAILURE, "非法字符");
			}else{
			
				List<String> sqls = new ArrayList<String>();
				sqls.add(String.format(YSSQL.LWUserLogin_EditNickName, new Object[]{nickName, auid}));
				if(dbExe(sqls)){
					result = new LWResult(LWResult.SUCCESS, "修改昵称成功");
				}else{
					result = new LWResult(LWResult.FAILURE, "修改昵称失败");
				}
		        loggerM(ComBeanLogType.TYPE_ADD, "修改昵称");
			}
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}
	
	// 修改账户信息
	public String edit(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "编辑账户信息";
		try{
			String[] vv = F_formParam();
			int i = 0;
			String userName = vv[i++];
			String sex= vv[i++];
			String phone = vv[i++];
			String landlinePhone = vv[i++];
			String email = vv[i++];
			String job = "";
			if(isHR()){
				job = vv[i++];		
			}
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWUserLogin_EditByUser, new Object[]{
					phone,
					userName,
					sex,
					landlinePhone,
					email,
					job,
					auid,
					
			}));
			if(isHR()){ // HR
				sqls.add(String.format(YSSQL.LWHR_EditByUser, new Object[]{
						phone,
						userName,
						sex,
						email,
						landlinePhone,
						job,
						auid,
				}));
			}else if(isHC()){ // 猎头
				sqls.add(String.format(YSSQL.LWHunter_EditByUser, new Object[]{
						userName,
						sex,
						landlinePhone,
						email,
						auid,
				}));				
			}

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
		return APPRETURN(result);
	}	

	//上传头像
	public String uploadFace(){
		logger.debug("[uploadFace]=================================");
		if(!ACCESS(true, false, true)) return BConstants.MESSAGE;
		LWResult result = null;
		try{
    		logger.debug("[uploadFace]上传头像 "+auid);
	    	String fileName = this.uploadPic("file1", "userface"+"/"+HIUtil.getCurrentDate("yyyy"), UGID.createUGID(), 200*1024, false, false, true, true, 150, 150); //保存到路径userface
			
			List<String> sqls = new ArrayList<String>();
			sqls.add(String.format(YSSQL.LWUserLogin_EditFace, new Object[]{fileName, auid}));
			if(dbExe(sqls)){
				result = new LWResult(LWResult.SUCCESS, this.getParameter("json", 0)==1?fileName:"上传头像成功");
			}else{
				result = new LWResult(LWResult.FAILURE, "上传头像失败");
			}
	        loggerM(ComBeanLogType.TYPE_ADD, "上传头像");
		}catch(Exception e){
			e.printStackTrace();
			result = new LWResult(LWResult.EXCEPTION, "异常");
		}
		return APPRETURN(result);
	}

	// 返回账户信息
	public String userInfo(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		LWResult result = null;
		String memo = "账户信息";
		try{
			StringBuilder s = new StringBuilder("");
		    List<Object> list = dbQuery(YSSQL.LWUser_QueryByAUID(auid));
			if(list!=null && list.size()>0){
				for(Object o: list){
					if(isHR()){
						s.append(this.getDBString(o, "c11"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c14"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c2"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c31"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c32"))
						 .append(Constants.SPLITER).append(ComBeanMMOrgan.getFullNameByAmid(getDBString(o, "c6")))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c33"));						
					}else if(isHC()){
						s.append(this.getDBString(o, "c11"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c14"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c2"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c31"))
						 .append(Constants.SPLITER).append(this.getDBString(o, "c32"));
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
		return APPRETURN(result);
	}
	
	//请求用户资料
	@SuppressWarnings("rawtypes")
	public String userMoule(){
		if(!ACCESS(true, false)) return BConstants.MESSAGE;
		String userAuid = this.getParameter("userAuid");
		
		LWResult result;
		StringBuilder s = new StringBuilder();
		List list = dbQuery(String.format(YSSQL.LWUserLogin_QueryByAUID, new Object[]{auid, HIUtil.isEmpty(userAuid, auid)}));

		/*
		 * 通用模块param1:  1有标题有图   2只有标题   3有图有详情   4只有详情   5有图有属性  6只有图 7大图有属性
		 * 通用模块power: 增 删 改 查 选 倒序 列数
		 */
		if(list!=null && list.size()>0){
			int status;
			String statusDesc;
			for(Object o: list){
				status = DBUtil.getDBInt(o, "status");
				statusDesc = status==0 ? "普通会员" : (status==1 ? "钻石会员" : "皇冠会员");
				s.append("{");
				s.append(" \"ad\":[]");
				s.append(",\"userinfo\":{\"faceAlign\":\"center\", \"nickName\":\"").append(DBUtil.getDBString(o, "nickName")).append("\",\"face\":\"").append(DBUtil.getDBString(o, "face")).append("\",\"status\":\"").append(status).append("\",\"statusDesc\":\"").append(statusDesc).append("\",\"userName\":\"").append(DBUtil.getDBString(o, "phone")).append("\",\"userDesc\":\"").append(statusDesc).append("\"}");
				s.append(",\"mylist\":[]");
				s.append(",\"menu1\":[")
				 .append(" {\"COLS\":3, \"title\":\"").append("我的订单").append("\",\"pro1\":\"\",\"pro2\":\"\",\"param\":\"\",\"param1\":\"\",\"param2\":\"\",\"ctype\":\"\", \"power\":\"\", \"img\":\"\",\"action\":\"ACTION_MY_ORDER\"}")
				 .append(",{\"COLS\":3, \"title\":\"").append("我的地址").append("\",\"pro1\":\"\",\"pro2\":\"\",\"param\":\"124\",\"param1\":\"c1=").append(auid).append("\", \"param2\":\"\", \"ctype\":\"2\", \"power\":\"1111001\", \"img\":\"\", \"action\":\"ACTION_CommonObject\"}")
				 .append(",{\"COLS\":3, \"title\":\"").append("我的消息").append("\",\"pro1\":\"\",\"pro2\":\"\",\"param\":\"16\",\"param1\":\"c2=").append(auid).append("\", \"param2\":\"\", \"ctype\":\"2\", \"power\":\"0100011\", \"img\":\"\", \"action\":\"ACTION_CommonObject\"}")
				 .append(",{\"COLS\":3, \"title\":\"").append("联系客服").append("\",\"pro1\":\"\",\"pro2\":\"\",\"param\":\"127\",\"param1\":\"\",\"param2\":\"\",\"ctype\":\"4\", \"power\":\"0000001\", \"img\":\"\",\"action\":\"ACTION_CommonObject\"}")
				 .append("]");
				s.append(",\"menu2\":[]");
				s.append(",\"menu3\":[]");
				s.append("}");
				break;
			}
			result = new LWResult(LWResult.SUCCESS, s.toString());
		}else{
			result = new LWResult(LWResult.FAILURE, "用户不存在");
		}
		return APPRETURN(result);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -585009600008017194L;
	
}
