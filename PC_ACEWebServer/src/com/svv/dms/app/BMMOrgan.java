package com.svv.dms.app;

import java.util.ArrayList;
import java.util.List;

import com.gs.db.util.DBUtil;
import com.svv.dms.web.common.ComBeanI_DataTable;
import com.svv.dms.web.util.HIUtil;



public class BMMOrgan{
	private long mid = -1L;
	private String mmName;
	private String amid;
	private int mmType = -1;
	private int mmGrade;
	private String city;
	private String address;
	private String mmPic = "";
	private String mmContact;
	private String mmPhone;
	private String creator;
	private String bizOperator;
	private int bizStatus;
	private String xy;
	private String openTime;
	private String shopTime;
	private String mmGroup;
	
	public BMMOrgan(String mmGroup, long mid, String amid, String mmName){
		this.mmGroup = mmGroup;
		this.mid = mid;
		this.amid = amid;
		this.mmName = mmName;
		this.city = "";
		this.mmType = -1;
		if(mmName.length()>0 && mid==-1){
		    List<Object> list = HIUtil.dbQuery(String.format(YSSQL.LWMMOrgan_QueryByOrganName, new Object[]{mmName}));
		    if(list!=null && list.size()>0){
		    	Object o = list.get(0);
				this.mid = DBUtil.getDBLong(o, "dataid");
				this.amid = DBUtil.getDBString(o, "c1");
				this.city = DBUtil.getDBString(o, "c9");
				this.mmType = DBUtil.getDBInt(o, "c5");
		    }
		}
	}
	
	public BMMOrgan(String mmGroup, long mid, String mmName, String city, int mmType,
			String address, String mmContact, String mmPhone, int bizStatus, String xy, String openTime, String shopTime, String creator, String bizOperator){
		this.mmGroup = mmGroup;
		this.mid = mid;
		this.mmName = mmName;
		this.city = city;
		this.address = address;
		this.mmContact = mmContact;
		this.mmPhone = mmPhone;
		this.bizStatus = bizStatus;
		this.xy = xy;
		this.openTime = openTime;
		this.shopTime = shopTime;
		this.creator = creator;
		this.bizOperator = bizOperator;
		this.mmType = mmType; //机构分类
		this.mmGrade = 202001; //机构级别：大众 高级
	}
	public List<String> getInsertSql(){
		List<String> sqls = new ArrayList<String>();
		//sqls.add(String.format(YSSQL.MMOrgan_ADD, new Object[]{mid, mmName, amid, mmType, city, address, mmPic, mmContact, mmPhone, creator, bizStatus, mmGrade, bizOperator, openTime, shopTime}));
		return sqls;
	}
	public List<String> getUpdateSql(){
		List<String> sqls = new ArrayList<String>();
		sqls.add(ComBeanI_DataTable.sqlToZ("B_MMBuyer", "[AWebServer]编辑机构", "dataid="+mid));
		//sqls.add(String.format(YSSQL.MMOrgan_Edit, new Object[]{mmName, mmType, city, address, mmPic, mmContact, mmPhone, openTime, shopTime, mid})); //, bizStatus
		return sqls;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getMmName() {
		return mmName;
	}
	public void setMmName(String mmName) {
		this.mmName = mmName;
	}
	public String getAmid() {
		return amid;
	}
	public void setAmid(String amid) {
		this.amid = amid;
	}
	public int getMmType() {
		return mmType;
	}
	public void setMmType(int mmType) {
		this.mmType = mmType;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMmPic() {
		return mmPic;
	}
	public void setMmPic(String mmPic) {
		this.mmPic = mmPic;
	}
	public String getMmContact() {
		return mmContact;
	}
	public void setMmContact(String mmContact) {
		this.mmContact = mmContact;
	}
	public String getMmPhone() {
		return mmPhone;
	}
	public void setMmPhone(String mmPhone) {
		this.mmPhone = mmPhone;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getMmGroup() {
		return mmGroup;
	}
	public void setMmGroup(String mmGroup) {
		this.mmGroup = mmGroup;
	}
	public int getBizStatus() {
		return bizStatus;
	}
	public void setBizStatus(int bizStatus) {
		this.bizStatus = bizStatus;
	}	
	public String getXy() {
		return xy;
	}
	public void setXy(String xy) {
		this.xy = xy;
	}
	public int getMmGrade() {
		return mmGrade;
	}
	public void setMmGrade(int mmGrade) {
		this.mmGrade = mmGrade;
	}
	public String getBizOperator() {
		return bizOperator;
	}
	public void setBizOperator(String bizOperator) {
		this.bizOperator = bizOperator;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getShopTime() {
		return shopTime;
	}
	public void setShopTime(String shopTime) {
		this.shopTime = shopTime;
	}
	
}
