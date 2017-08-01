/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.copy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.copy.entity.CopyUser;
import com.thinkgem.jeesite.modules.copy.dao.CopyUserDao;

/**
 * 复制人员子表Service
 * @author Carrel
 * @version 2017-07-31
 */
@Service
@Transactional(readOnly = true)
public class CopyUserService extends CrudService<CopyUserDao, CopyUser> {

	public CopyUser get(String id) {
		return super.get(id);
	}
	
	public List<CopyUser> findList(CopyUser copyUser) {
		return super.findList(copyUser);
	}
	
	public Page<CopyUser> findPage(Page<CopyUser> page, CopyUser copyUser) {
		return super.findPage(page, copyUser);
	}
	
	@Transactional(readOnly = false)
	public void save(CopyUser copyUser) {
		super.save(copyUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(CopyUser copyUser) {
		super.delete(copyUser);
	}
	
}