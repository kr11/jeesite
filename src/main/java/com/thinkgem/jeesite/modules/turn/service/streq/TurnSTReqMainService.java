/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.streq;

import java.util.List;

import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqUserChildDao;

/**
 * 排班-规培标准表Service
 * @author Carrel
 * @version 2017-07-28
 */
@Service
@Transactional(readOnly = true)
public class TurnSTReqMainService extends CrudService<TurnSTReqMainDao, TurnSTReqMain> {

	@Autowired
	private TurnSTReqDepChildDao turnSTReqDepChildDao;
	@Autowired
	private TurnSTReqUserChildDao turnSTReqUserChildDao;

    @Autowired
    private TurnArchiveDao turnArchiveDao;

    public TurnSTReqMain get(String id) {
		TurnSTReqMain turnSTReqMain = super.get(id);
		turnSTReqMain.setTurnSTReqDepChildList(turnSTReqDepChildDao.findList(new TurnSTReqDepChild(turnSTReqMain)));
		turnSTReqMain.setTurnSTReqUserChildList(turnSTReqUserChildDao.findList(new TurnSTReqUserChild(turnSTReqMain)));
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
		for (TurnSTReqDepChild turnSTReqDepChild : turnSTReqMain.getTurnSTReqDepChildList()){
			if (turnSTReqDepChild.getId() == null){
				continue;
			}
			if (TurnSTReqDepChild.DEL_FLAG_NORMAL.equals(turnSTReqDepChild.getDelFlag())){
                String[] idAndNames = turnSTReqDepChild.getDepartmentName().split("@");
                turnSTReqDepChild.setDepartmentId(idAndNames[0]);
                if (StringUtils.isBlank(turnSTReqDepChild.getId())){
					turnSTReqDepChild.setRequirementId(turnSTReqMain);
					turnSTReqDepChild.preInsert();
					turnSTReqDepChildDao.insert(turnSTReqDepChild);
				}else{
					turnSTReqDepChild.preUpdate();
					turnSTReqDepChildDao.update(turnSTReqDepChild);
				}
			}else{
				turnSTReqDepChildDao.delete(turnSTReqDepChild);
			}
		}
		for (TurnSTReqUserChild turnSTReqUserChild : turnSTReqMain.getTurnSTReqUserChildList()){
			if (turnSTReqUserChild.getId() == null){
				continue;
			}
			if (TurnSTReqUserChild.DEL_FLAG_NORMAL.equals(turnSTReqUserChild.getDelFlag())){
				if (StringUtils.isBlank(turnSTReqUserChild.getId())){
					turnSTReqUserChild.setRequirementId(turnSTReqMain);
					//temp：临时添加，用户userid=这个表里的id
                    turnSTReqUserChild.setUserId(turnSTReqUserChild.getId());
					turnSTReqUserChild.preInsert();
					turnSTReqUserChildDao.insert(turnSTReqUserChild);
				}else{
					turnSTReqUserChild.preUpdate();
					turnSTReqUserChildDao.update(turnSTReqUserChild);
				}
			}else{
				turnSTReqUserChildDao.delete(turnSTReqUserChild);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TurnSTReqMain turnSTReqMain) {
		super.delete(turnSTReqMain);
		turnSTReqDepChildDao.delete(new TurnSTReqDepChild(turnSTReqMain));
		turnSTReqUserChildDao.delete(new TurnSTReqUserChild(turnSTReqMain));
	}
	
}