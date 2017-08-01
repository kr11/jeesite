package com.thinkgem.jeesite.modules.turn.service.department;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;

import java.util.List;

public class TurnDepartmentUtils {
    private static TurnDepartmentDao turnDepartmentDao = SpringContextHolder.getBean(TurnDepartmentDao.class);

    public static void transport(String lastestId, String newId) {
        TurnDepartment turnDepartment = new TurnDepartment();
        turnDepartment.setBelongArchiveId(lastestId);
        List<TurnDepartment> list = turnDepartmentDao.findList(turnDepartment);
        for (TurnDepartment department : list) {
            //插入成新的
            department.setId("");
            department.setBelongArchiveId(newId);
            department.preInsert();
            turnDepartmentDao.insert(department);
        }
    }

}
