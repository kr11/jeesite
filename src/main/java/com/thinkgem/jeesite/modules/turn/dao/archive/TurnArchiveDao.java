/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.dao.archive;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;

/**
 * 排版中存档的增删改查功能DAO接口
 * @author Carrel
 * @version 2017-07-25
 */
@MyBatisDao
public interface TurnArchiveDao extends CrudDao<TurnArchive> {
	
}