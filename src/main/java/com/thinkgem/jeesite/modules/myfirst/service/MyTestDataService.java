/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.myfirst.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.myfirst.entity.MyTestData;
import com.thinkgem.jeesite.modules.myfirst.dao.MyTestDataDao;

/**
 * 尝试性的用户Service
 * @author Carrel
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class MyTestDataService extends CrudService<MyTestDataDao, MyTestData> {

	public MyTestData get(String id) {
		return super.get(id);
	}
	
	public List<MyTestData> findList(MyTestData myTestData) {
		return super.findList(myTestData);
	}
	
	public Page<MyTestData> findPage(Page<MyTestData> page, MyTestData myTestData) {
		return super.findPage(page, myTestData);
	}
	
	@Transactional(readOnly = false)
	public void save(MyTestData myTestData) {
		super.save(myTestData);
	}
	
	@Transactional(readOnly = false)
	public void delete(MyTestData myTestData) {
		super.delete(myTestData);
	}
	
}