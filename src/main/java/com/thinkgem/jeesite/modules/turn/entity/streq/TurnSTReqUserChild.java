/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.streq;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班-规培标准表Entity
 * @author Carrel
 * @version 2017-07-28
 */
public class TurnSTReqUserChild extends DataEntity<TurnSTReqUserChild> {
	
	private static final long serialVersionUID = 1L;
	private TurnSTReqMain requirementId;		// 所属标准编号 父类
	private User user;		// 人员系统id
	private String userName;		// 姓名
	private String sex;		// 性别
	private String userNumber;		// 学员编号
	private String grade;		// 年级
	private String userClass;		// 学员性质
	private String groupId;		// 大组编号
	
	public TurnSTReqUserChild() {
		super();
	}

	public TurnSTReqUserChild(String id){
		super(id);
	}

	public TurnSTReqUserChild(TurnSTReqMain requirementId){
		this.requirementId = requirementId;
	}

	@Length(min=1, max=64, message="所属标准编号长度必须介于 1 和 64 之间")
	public TurnSTReqMain getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(TurnSTReqMain requirementId) {
		this.requirementId = requirementId;
	}
	
	@NotNull(message="人员系统id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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
	
	@Length(min=0, max=64, message="学员编号长度必须介于 0 和 64 之间")
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
	
	@Length(min=1, max=64, message="学员性质长度必须介于 1 和 64 之间")
	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	
	@Length(min=0, max=64, message="大组编号长度必须介于 0 和 64 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}