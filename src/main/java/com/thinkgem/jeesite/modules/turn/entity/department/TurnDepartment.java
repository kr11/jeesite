/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.department;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班科室表Entity
 * @author Carrel
 * @version 2017-07-26
 */
public class TurnDepartment extends DataEntity<TurnDepartment> {
	
	private static final long serialVersionUID = 1L;
	private String belongArchiveId;		// 所属存档id
	private String departmentName;		// 科室名
	private String practiceClass;		// 实习所属大类
	private String isUsed;		// 是否启用
	private String exchangeDepartmentId;		// 可互换科室编号
	private String nowAliasName;		// 科室变迁现在名字
	
	public TurnDepartment() {
		super();
	}

	public TurnDepartment(String id){
		super(id);
	}

	@Length(min=1, max=64, message="所属存档id长度必须介于 1 和 64 之间")
	public String getBelongArchiveId() {
		return belongArchiveId;
	}

	public void setBelongArchiveId(String belongArchiveId) {
		this.belongArchiveId = belongArchiveId;
	}
	
	@Length(min=1, max=64, message="科室名长度必须介于 1 和 64 之间")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Length(min=1, max=64, message="实习所属大类长度必须介于 1 和 64 之间")
	public String getPracticeClass() {
		return practiceClass;
	}

	public void setPracticeClass(String practiceClass) {
		this.practiceClass = practiceClass;
	}
	
	@Length(min=1, max=1, message="是否启用长度必须介于 1 和 1 之间")
	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	
	@Length(min=1, max=64, message="可互换科室编号长度必须介于 1 和 64 之间")
	public String getExchangeDepartmentId() {
		return exchangeDepartmentId;
	}

	public void setExchangeDepartmentId(String exchangeDepartmentId) {
		this.exchangeDepartmentId = exchangeDepartmentId;
	}
	
	@Length(min=1, max=64, message="科室变迁现在名字长度必须介于 1 和 64 之间")
	public String getNowAliasName() {
		return nowAliasName;
	}

	public void setNowAliasName(String nowAliasName) {
		this.nowAliasName = nowAliasName;
	}
	
}