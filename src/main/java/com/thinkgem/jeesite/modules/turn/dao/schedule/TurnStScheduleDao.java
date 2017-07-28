/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.dao.schedule;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.turn.entity.schedule.TurnStSchedule;

/**
 * 排班-规培调度表DAO接口
 * @author Carrel
 * @version 2017-07-28
 */
@MyBatisDao
public interface TurnStScheduleDao extends CrudDao<TurnStSchedule> {
	
}