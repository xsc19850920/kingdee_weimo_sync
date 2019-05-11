package com.dhsy.async.entity;

public class UserForKingdee {
	private String mobile;
	private long id;//	false	number	会员ID，系统内码
	private String number;//	true	string	会员number
	private String balamt;//	true	number	余额
	private String rechargeamt;//	true	number	充值余额
	private String giftamt;//	true	number	赠送余额
	private String cardid;//	true	string	会员卡号码
//	private String phone;//	true	string	电话2
	private String name;//	true	string	会员名称
	private String email;//	false	string	email
	private String referrerid;//	false	number	推荐人会员ID
	private String referrerName	;//false	string	推荐人名称
	private String typeid;//	true	string	会员类型ID
	private String typeName	;//true	string	会员类型
	private String createtime;//	true	date	创建日期
	private String modifytime;//	true	date	修改日期
	private String countryname;//	false	string	国家
	private String districtname	;//false	string	区
	private String cityname	;//false	string	市
	private String provincename;//	false	string	省
	private String educname;//	false	string	学历
	private String industryname;//	false	string	行业
	private String addr;//	false	string	街道地址
	private String shopId;//	false	string	门店来源id
	private String shopname;//	false	string	门店名称
	private String oldcard;//	false	string	旧的卡号
	private String balpoints = "0"; //用户积分
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBalamt() {
		return balamt;
	}

	public void setBalamt(String balamt) {
		this.balamt = balamt;
	}

	public String getRechargeamt() {
		return rechargeamt;
	}

	public void setRechargeamt(String rechargeamt) {
		this.rechargeamt = rechargeamt;
	}

	public String getGiftamt() {
		return giftamt;
	}

	public void setGiftamt(String giftamt) {
		this.giftamt = giftamt;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReferrerid() {
		return referrerid;
	}

	public void setReferrerid(String referrerid) {
		this.referrerid = referrerid;
	}

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	public String getEducname() {
		return educname;
	}

	public void setEducname(String educname) {
		this.educname = educname;
	}

	public String getIndustryname() {
		return industryname;
	}

	public void setIndustryname(String industryname) {
		this.industryname = industryname;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getOldcard() {
		return oldcard;
	}

	public void setOldcard(String oldcard) {
		this.oldcard = oldcard;
	}

	public String getBalpoints() {
		return balpoints;
	}

	public void setBalpoints(String balpoints) {
		this.balpoints = balpoints;
	}
	
}
