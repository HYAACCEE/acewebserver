package com.svv.dms.app;

public class BUser{
	private String auid = "";
	private String phone = "";
	private String nickname = "";
	private String face = "";
	private String sex = "";
	private long mid = -1;
	private String amid = "";
	private String mmGroup = "";
	
	public BUser(String mmGroup, String auid, String phone, long mid, String amid, String nickname){
		this.mmGroup = mmGroup;
		this.auid = auid;
		this.phone = phone;
		this.mid = mid;
		this.amid = amid;
		this.nickname = nickname;
	}
	public BUser(String mmGroup, String auid, String phone, long mid, String amid, String nickname, String face, String sex){
		this.mmGroup = mmGroup;
		this.auid = auid;
		this.phone = phone;
		this.mid = mid;
		this.amid = amid;
		this.nickname = nickname;
		this.face = face;
		this.sex = sex;
	}

	public String getAuid() {
		return auid;
	}

	public void setAuid(String auid) {
		this.auid = auid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getAmid() {
		return amid;
	}
	public void setAmid(String amid) {
		this.amid = amid;
	}
	public String getMmGroup() {
		return mmGroup;
	}
	public void setMmGroup(String mmGroup) {
		this.mmGroup = mmGroup;
	}
	
}
