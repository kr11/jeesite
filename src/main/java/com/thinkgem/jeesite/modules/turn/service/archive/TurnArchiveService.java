/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.archive;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;

/**
 * 排版中存档的增删改查功能Service
 * @author Carrel
 * @version 2017-07-25
 */
@Service
@Transactional(readOnly = true)
public class TurnArchiveService extends CrudService<TurnArchiveDao, TurnArchive> {

	public TurnArchive get(String id) {
		return super.get(id);
	}
	
	public List<TurnArchive> findList(TurnArchive turnArchive) {
		return super.findList(turnArchive);
	}
	
	public Page<TurnArchive> findPage(Page<TurnArchive> page, TurnArchive turnArchive) {
		return super.findPage(page, turnArchive);
	}
	
	@Transactional(readOnly = false)
	public void save(TurnArchive turnArchive) {
		super.save(turnArchive);
	}
	
	@Transactional(readOnly = false)
	public void delete(TurnArchive turnArchive) {
		super.delete(turnArchive);
	}
	
}