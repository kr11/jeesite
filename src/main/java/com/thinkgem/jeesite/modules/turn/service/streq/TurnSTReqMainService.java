/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.streq;

import java.util.List;

import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqChild;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqChildDao;

/**
 * 排班-规培标准表Service
 * @author Carrel
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class TurnSTReqMainService extends CrudService<TurnSTReqMainDao, TurnSTReqMain> {

	@Autowired
    private TurnArchiveDao turnArchiveDao;

	@Autowired
	private TurnSTReqChildDao turnSTReqChildDao;

    @Autowired
    private TurnDepartmentDao turnDepartmentDao;

	public TurnSTReqMain get(String id) {
		TurnSTReqMain turnSTReqMain = super.get(id);
		turnSTReqMain.setTurnSTReqChildList(turnSTReqChildDao.findList(new TurnSTReqChild(turnSTReqMain)));
		return turnSTReqMain;
	}
	
	public List<TurnSTReqMain> findList(TurnSTReqMain turnSTReqMain) {
		return super.findList(turnSTReqMain);
	}
	
	public Page<TurnSTReqMain> findPage(Page<TurnSTReqMain> page, TurnSTReqMain turnSTReqMain) {
		return super.findPage(page, turnSTReqMain);
	}
	
	@Transactional(readOnly = false)
	public void save(TurnSTReqMain turnSTReqMain) {
        TurnArchive arch = new TurnArchive();
        arch.setBooleanIsOpen(true);
        List<TurnArchive> openArch = turnArchiveDao.findList(arch);
        turnSTReqMain.setArchiveId(openArch.get(0).getId());
		super.save(turnSTReqMain);
		for (TurnSTReqChild turnSTReqChild : turnSTReqMain.getTurnSTReqChildList()){
			if (turnSTReqChild.getId() == null){
				continue;
			}
			if (TurnSTReqChild.DEL_FLAG_NORMAL.equals(turnSTReqChild.getDelFlag())){
			    String[] idAndNames = turnSTReqChild.getDepartmentName().split("@");
                turnSTReqChild.setDepartmentId(idAndNames[0]);
                turnSTReqChild.setDepartmentName(idAndNames[1]);
				if (StringUtils.isBlank(turnSTReqChild.getId())){
					turnSTReqChild.setRequirementId(turnSTReqMain);
					turnSTReqChild.preInsert();
					turnSTReqChildDao.insert(turnSTReqChild);
				}else{
					turnSTReqChild.preUpdate();
					turnSTReqChildDao.update(turnSTReqChild);
				}
			}else{
				turnSTReqChildDao.delete(turnSTReqChild);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TurnSTReqMain turnSTReqMain) {
		super.delete(turnSTReqMain);
		turnSTReqChildDao.delete(new TurnSTReqChild(turnSTReqMain));
	}

    public List<TurnDepartment> findDepartmentList() {
        //返回所有科室，其实只要名字，量不大，不要紧
        return turnDepartmentDao.findAllList(new TurnDepartment());
    }
	
}