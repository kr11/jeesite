/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.streq;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排班-规培标准表Entity
 * @author Carrel
 * @version 2017-07-28
 */
public class TurnSTReqMain extends DataEntity<TurnSTReqMain> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 规培标准名
	private String archiveId;		// 所属存档id
	private Integer totalLength;		// 总时长
	private String timgUnit;		// 时间单位
	private List<TurnSTReqChild> turnSTReqChildList = Lists.newArrayList();		// 子表列表
	
	public TurnSTReqMain() {
		super();
	}

	public TurnSTReqMain(String id){
		super(id);
	}

	@Length(min=1, max=64, message="规培标准名长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=64, message="所属存档id长度必须介于 1 和 64 之间")
	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
	
	@NotNull(message="总时长不能为空")
	public Integer getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(Integer totalLength) {
		this.totalLength = totalLength;
	}
	
	@Length(min=1, max=64, message="时间单位长度必须介于 1 和 64 之间")
	public String getTimgUnit() {
		return timgUnit;
	}

	public void setTimgUnit(String timgUnit) {
		this.timgUnit = timgUnit;
	}
	
	public List<TurnSTReqChild> getTurnSTReqChildList() {
		return turnSTReqChildList;
	}

	public void setTurnSTReqChildList(List<TurnSTReqChild> turnSTReqChildList) {
		this.turnSTReqChildList = turnSTReqChildList;
	}
	@Override
	public String toString() {
		return id;
	}
}