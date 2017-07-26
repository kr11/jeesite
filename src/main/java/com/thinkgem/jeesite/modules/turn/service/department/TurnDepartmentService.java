/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.department;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;

/**
 * 排班科室表Service
 * @author Carrel
 * @version 2017-07-26
 */
@Service
@Transactional(readOnly = true)
public class TurnDepartmentService extends CrudService<TurnDepartmentDao, TurnDepartment> {

	public TurnDepartment get(String id) {
		return super.get(id);
	}
	
	public List<TurnDepartment> findList(TurnDepartment turnDepartment) {
		return super.findList(turnDepartment);
	}
	
	public Page<TurnDepartment> findPage(Page<TurnDepartment> page, TurnDepartment turnDepartment) {
		return super.findPage(page, turnDepartment);
	}
	
	@Transactional(readOnly = false)
	public void save(TurnDepartment turnDepartment) {
		super.save(turnDepartment);
	}
	
	@Transactional(readOnly = false)
	public void delete(TurnDepartment turnDepartment) {
		super.delete(turnDepartment);
	}
	
}