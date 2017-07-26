/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.dao.department;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;

/**
 * 排班科室表DAO接口
 * @author Carrel
 * @version 2017-07-26
 */
@MyBatisDao
public interface TurnDepartmentDao extends CrudDao<TurnDepartment> {
	
}