/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.keymap.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 键值对表Entity
 * @author Carrel
 * @version 2017-07-31
 */
public class KeyMap extends DataEntity<KeyMap> {
	
	private static final long serialVersionUID = 1L;
	private String dictKey;		// 键
	private String dictValue;		// 值
	
	public KeyMap() {
		super();
	}

	public KeyMap(String id){
		super(id);
	}

	@Length(min=1, max=64, message="键长度必须介于 1 和 64 之间")
	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}
	
	@Length(min=1, max=64, message="值长度必须介于 1 和 64 之间")
	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
	
}