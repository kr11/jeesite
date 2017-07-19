/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.myfirst.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.myfirst.entity.MyTestData;

/**
 * 尝试性的用户DAO接口
 * @author Carrel
 * @version 2017-07-19
 */
@MyBatisDao
public interface MyTestDataDao extends CrudDao<MyTestData> {
	
}