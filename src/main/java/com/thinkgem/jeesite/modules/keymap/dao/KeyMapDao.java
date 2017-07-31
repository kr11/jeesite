/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.keymap.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.keymap.entity.KeyMap;

/**
 * 键值对表DAO接口
 * @author Carrel
 * @version 2017-07-31
 */
@MyBatisDao
public interface KeyMapDao extends CrudDao<KeyMap> {
	
}