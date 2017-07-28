/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.schedule;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.entity.schedule.TurnStSchedule;
import com.thinkgem.jeesite.modules.turn.dao.schedule.TurnStScheduleDao;

/**
 * 排班-规培调度表Service
 * @author Carrel
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class TurnStScheduleService extends CrudService<TurnStScheduleDao, TurnStSchedule> {

	public TurnStSchedule get(String id) {
		return super.get(id);
	}
	
	public List<TurnStSchedule> findList(TurnStSchedule turnStSchedule) {
		return super.findList(turnStSchedule);
	}
	
	public Page<TurnStSchedule> findPage(Page<TurnStSchedule> page, TurnStSchedule turnStSchedule) {
		return super.findPage(page, turnStSchedule);
	}
	
	@Transactional(readOnly = false)
	public void save(TurnStSchedule turnStSchedule) {
		super.save(turnStSchedule);
	}
	
	@Transactional(readOnly = false)
	public void delete(TurnStSchedule turnStSchedule) {
		super.delete(turnStSchedule);
	}
	
}