/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.copy.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 复制人员子表Entity
 * @author Carrel
 * @version 2017-07-31
 */
public class CopyUser extends DataEntity<CopyUser> {
	
	private static final long serialVersionUID = 1L;
	private String requirementId;		// 所属标准id
	private String user;		// 人员系统id
	private String userName;		// 姓名
	private String sex;		// 性别
	private String reqBase;		// 所属基地
	private String userNumber;		// 学号
	private String grade;		// 年级
	private String studentClass;		// 学员性质
	private String groupId;		// 大组编号
	
	public CopyUser() {
		super();
	}

	public CopyUser(String id){
		super(id);
	}

	@Length(min=1, max=64, message="所属标准id长度必须介于 1 和 64 之间")
	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}
	
	@Length(min=0, max=64, message="人员系统id长度必须介于 0 和 64 之间")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	@Length(min=1, max=64, message="姓名长度必须介于 1 和 64 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=1, message="性别长度必须介于 1 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=64, message="所属基地长度必须介于 0 和 64 之间")
	public String getReqBase() {
		return reqBase;
	}

	public void setReqBase(String reqBase) {
		this.reqBase = reqBase;
	}
	
	@Length(min=0, max=64, message="学号长度必须介于 0 和 64 之间")
	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	
	@Length(min=0, max=64, message="年级长度必须介于 0 和 64 之间")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@Length(min=0, max=64, message="学员性质长度必须介于 0 和 64 之间")
	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	
	@Length(min=0, max=64, message="大组编号长度必须介于 0 和 64 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}