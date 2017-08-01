/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.department;

import java.util.List;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.dao.OaNotifyRecordDao;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;

/**
 * 排班科室表Service
 *
 * @author Carrel
 * @version 2017-07-27
 */
@Service
@Transactional(readOnly = false)
public class TurnDepartmentService extends CrudService<TurnDepartmentDao, TurnDepartment> {
    @Autowired
    private TurnArchiveDao turnArchiveDao;

    public TurnDepartment get(String id) {
        return super.get(id);
    }

    public List<TurnDepartment> findList(TurnDepartment turnDepartment) {
        turnDepartment.setBelongArchiveId(ArchiveUtils.getOpenedArchiveId());
        return super.findList(turnDepartment);
    }

    public Page<TurnDepartment> findPage(Page<TurnDepartment> page, TurnDepartment turnDepartment) {
        turnDepartment.setBelongArchiveId(ArchiveUtils.getOpenedArchiveId());
        return super.findPage(page, turnDepartment);
    }

    @Transactional(readOnly = false)
    public void save(TurnDepartment turnDepartment) {
        if (turnDepartment.getIsNewRecord()) {
            TurnArchive arch = new TurnArchive();
            arch.setBooleanIsOpen(true);
            List<TurnArchive> openArch = turnArchiveDao.findList(arch);
            turnDepartment.setBelongArchiveId(openArch.get(0).getId());
//            turnDepartment.setBooleanIsUsed(true);
        }
        super.save(turnDepartment);
    }

    @Transactional(readOnly = false)
    public void delete(TurnDepartment turnDepartment) {
        super.delete(turnDepartment);
    }

    public List<TurnDepartment> findDepartmentList() {
        //返回所有科室，其实只要名字，量不大，不要紧
        return dao.findAllList(new TurnDepartment());
    }

    public List<TurnDepartment> findDepartmentList(TurnDepartment turnDepartment) {
        return dao.findAllList(turnDepartment);
    }
}