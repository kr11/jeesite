/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.entity.archive;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 排版中存档的增删改查功能Entity
 * @author Carrel
 * @version 2017-07-25
 */
public class TurnArchive extends DataEntity<TurnArchive> {
	
	private static final long serialVersionUID = 1L;
	private String archiveName;		// 存档名
	private String isOpen;		// 是否开启
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginUpdateDate;		// 开始 最后更新时间
	private Date endUpdateDate;		// 结束 最后更新时间
	
	public TurnArchive() {
		super();
	}

	public TurnArchive(String id){
		super(id);
	}

	@Length(min=1, max=64, message="存档名长度必须介于 1 和 64 之间")
	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}
	
	@Length(min=1, max=1, message="是否开启长度必须介于 1 和 1 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public boolean getBooleanIsOpen() {
		return "1".equals(isOpen);
	}
	public void setBooleanIsOpen(boolean f) {
		this.isOpen = f ? "1" : "0";
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
	public Date getBeginUpdateDate() {
		return beginUpdateDate;
	}

	public void setBeginUpdateDate(Date beginUpdateDate) {
		this.beginUpdateDate = beginUpdateDate;
	}
	
	public Date getEndUpdateDate() {
		return endUpdateDate;
	}

	public void setEndUpdateDate(Date endUpdateDate) {
		this.endUpdateDate = endUpdateDate;
	}
		
}