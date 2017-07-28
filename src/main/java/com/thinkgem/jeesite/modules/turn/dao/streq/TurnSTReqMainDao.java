/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.dao.streq;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;

/**
 * 排班-规培标准表DAO接口
 * @author Carrel
 * @version 2017-07-28
 */
@MyBatisDao
public interface TurnSTReqMainDao extends CrudDao<TurnSTReqMain> {
	
}