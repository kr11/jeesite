/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.archive;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.turn.TurnConstant;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.service.department.TurnDepartmentUtils;
import com.thinkgem.jeesite.modules.turn.service.streq.TurnSTReqMainUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;

/**
 * 排版中存档的增删改查功能Service
 *
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
        page.setOrderBy("createDate");
        return super.findPage(page, turnArchive);
    }

    /**
     * 有创建一个新的的时候，将旧的拿过来：不管新的是开启还是关闭
     */
    private void thansportDepAndReq(TurnArchive turnArchive) {
        //如果是新的，找最新的一个，复制
        Page<TurnArchive> page = new Page<>();
        page.setOrderBy("createDate");
        turnArchive.setPage(page);
        List<TurnArchive> allArch = dao.findAllList(turnArchive);
        if (allArch.isEmpty())
            return;
        //最近创建的id
        if (allArch.size() < 2) {
            System.out.println("新加的是第一个，不迁移");
            return;
        }
        String lastestId = allArch.get(allArch.size() - 2).getId();
        String newId = turnArchive.getId();
        Map<String, String> oldToNewDepIdMap = TurnDepartmentUtils.transport(lastestId, newId);
        TurnSTReqMainUtils.transport(lastestId, newId, oldToNewDepIdMap);
    }

    @Transactional(readOnly = false)
    public void save(TurnArchive turnArchive) {
        boolean isNew = turnArchive.getIsNewRecord();
        if (turnArchive.getBooleanIsOpen()) {
            //开启状态，检查所有，把他们全关掉，然后设置自己
            List<TurnArchive> allArch = dao.findAllList(turnArchive);
            for (TurnArchive arch : allArch) {
                if (arch.getBooleanIsOpen()) {
                    arch.setBooleanIsOpen(false);
                    dao.update(arch);
                }
            }
            super.save(turnArchive);
//            ArchiveUtils.currentArchive = turnArchive.getId();
        } else {
            //关闭状态
            super.save(turnArchive);
            openLatest();
        }
        if (isNew)
            thansportDepAndReq(turnArchive);
    }

    private void openLatest() {
        List<TurnArchive> allArch = dao.findList(new TurnArchive());
        allArch.sort(Comparator.comparing(DataEntity::getCreateDate));
        //找出所有
        //找一个
        boolean hasOpen = false;
        for (TurnArchive arch : allArch) {
            if (arch.getBooleanIsOpen()) {
                if (!hasOpen) {
                    hasOpen = true;
//                        ArchiveUtils.currentArchive = arch.getId();
                } else
                    throw new UnsupportedOperationException("there are more than one opened arch!");
            }
        }
        if (!hasOpen) {
            //全部关掉了，设置最近一个打开
            allArch.get(0).setBooleanIsOpen(true);
            dao.update(allArch.get(0));
//                ArchiveUtils.currentArchive = allArch.get(0).getId();
        }
    }

    @Transactional(readOnly = false)
    public void delete(TurnArchive turnArchive) {
        super.delete(turnArchive);
        openLatest();
    }
}