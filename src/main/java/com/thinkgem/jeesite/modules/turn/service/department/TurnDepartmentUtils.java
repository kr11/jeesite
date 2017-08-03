package com.thinkgem.jeesite.modules.turn.service.department;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnDepartmentUtils {
    private static TurnDepartmentDao turnDepartmentDao = SpringContextHolder.getBean(TurnDepartmentDao.class);

    public static Map<String, String> transport(String lastestId, String newId) {
        TurnDepartment turnDepartment = new TurnDepartment();
        turnDepartment.setBelongArchiveId(lastestId);
        List<TurnDepartment> list = turnDepartmentDao.findList(turnDepartment);
        Map<String, String> retMap = new HashMap<>();
        for (TurnDepartment department : list) {
            String oldId = department.getId();
            //插入成新的
            department.setId("");
            department.setBelongArchiveId(newId);
            department.preInsert();
            turnDepartmentDao.insert(department);
            retMap.put(oldId, department.getId());
        }
        return retMap;
    }
}
