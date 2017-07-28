/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.streq;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班-规培标准表Entity
 * @author Carrel
 * @version 2017-07-28
 */
public class TurnSTReqChild extends DataEntity<TurnSTReqChild> {
	
	private static final long serialVersionUID = 1L;
	private TurnSTReqMain requirementId;		// 所属标准编号 父类
	private String departmentId;		// 科室_id
	private String departmentName;		// 科室名
	private String timeLength;		// 时间长度
	
	public TurnSTReqChild() {
		super();
	}

	public TurnSTReqChild(String id){
		super(id);
	}

	public TurnSTReqChild(TurnSTReqMain requirementId){
		this.requirementId = requirementId;
	}

	@Length(min=0, max=64, message="所属标准编号长度必须介于 0 和 64 之间")
	public TurnSTReqMain getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(TurnSTReqMain requirementId) {
		this.requirementId = requirementId;
	}
	
	@Length(min=1, max=64, message="科室_id长度必须介于 1 和 64 之间")
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	
	@Length(min=1, max=64, message="科室名长度必须介于 1 和 64 之间")
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Length(min=1, max=64, message="时间长度长度必须介于 1 和 64 之间")
	public String getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}
	
}