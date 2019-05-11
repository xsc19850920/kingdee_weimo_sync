package com.dhsy.async.entity;

import java.util.Date;

public class UserForWeimo {
	private Long mid;// 唯一标示
	private String phone;// 手机号
	private String code; // 会员卡号
	private Long widl;// 用户wid
	private Date gmtCreate; // 成为客户时间
	private Date cardPublishTime;// 领卡时间
	private String name;// 姓名
	private Date birthday;// 生日
	private String idCard;// 身份证
	private String address;//  地址
	private String mail;//  邮箱
	private Integer sex;// 性别
	private String education;//  教育背景
	private String hobby;//  爱好
	private String income;//  收入
	private String industry;//  行业
	private String logo;//  会员头像地址
	private String rankName;//  会员等级名称
	private Long rankId; // 会员等级Id
	private Long currentPoint;// 当前积分
	private Long currentAmount = 0l; // Long 当前余额
	private Long currentGrowth;// Long 当前成长值

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getWidl() {
		return widl;
	}

	public void setWidl(Long widl) {
		this.widl = widl;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getCardPublishTime() {
		return cardPublishTime;
	}

	public void setCardPublishTime(Date cardPublishTime) {
		this.cardPublishTime = cardPublishTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	

	public Long getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(Long currentPoint) {
		this.currentPoint = currentPoint;
	}

	public Long getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(Long currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Long getCurrentGrowth() {
		return currentGrowth;
	}

	public void setCurrentGrowth(Long currentGrowth) {
		this.currentGrowth = currentGrowth;
	}

}
