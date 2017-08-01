package com.thinkgem.jeesite.modules.turn.service.streq;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;

import java.util.List;

public class TurnSTReqMainUtils {
    private static TurnSTReqMainDao turnSTReqMainDao = SpringContextHolder.getBean(TurnSTReqMainDao.class);

    public static void transport(String lastestId, String newId) {
        //以前存档的无所属基地不加进来
        String lastEmpId = ArchiveUtils.getSpecifiedArchiveEmptyReq(lastestId);
        TurnSTReqMain turnSTReqMain = new TurnSTReqMain();
        turnSTReqMain.setArchiveId(lastestId);
        List<TurnSTReqMain> list = turnSTReqMainDao.findList(turnSTReqMain);
        for (TurnSTReqMain main : list) {
            if (!main.getId().equals(lastEmpId)) {
                //插入成新的
                main.setId("");
                main.setArchiveId(newId);
                main.preInsert();
                turnSTReqMainDao.insert(main);
            }
        }
    }

}
