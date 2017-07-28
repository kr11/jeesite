/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.schedule;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班-规培调度表Entity
 * @author Carrel
 * @version 2017-07-28
 */
public class TurnStSchedule extends DataEntity<TurnStSchedule> {
	
	private static final long serialVersionUID = 1L;
	private String archiveId;		// 所属存档id
	private User user;		// 用户id
	private String userName;		// 用户名
	private String requirementId;		// 所属标准id
	private String depId;		// 科室id
	private String depName;		// 科室名
	private String startInt;		// 开始时间半月整数
	private String endInt;		// 结束时间半月整数
	
	public TurnStSchedule() {
		super();
	}

	public TurnStSchedule(String id){
		super(id);
	}

	@Length(min=1, max=64, message="所属存档id长度必须介于 1 和 64 之间")
	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
	
	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=64, message="用户名长度必须介于 1 和 64 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=64, message="所属标准id长度必须介于 1 和 64 之间")
	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}
	
	@Length(min=1, max=64, message="科室id长度必须介于 1 和 64 之间")
	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}
	
	@Length(min=1, max=64, message="科室名长度必须介于 1 和 64 之间")
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	@Length(min=1, max=64, message="开始时间半月整数长度必须介于 1 和 64 之间")
	public String getStartInt() {
		return startInt;
	}

	public void setStartInt(String startInt) {
		this.startInt = startInt;
	}
	
	@Length(min=1, max=64, message="结束时间半月整数长度必须介于 1 和 64 之间")
	public String getEndInt() {
		return endInt;
	}

	public void setEndInt(String endInt) {
		this.endInt = endInt;
	}
	
}