package com.thinkgem.jeesite.modules.turn.service.streq;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;

import java.util.List;

public class TurnSTReqMainUtils {
    private static TurnSTReqMainDao turnSTReqMainDao = SpringContextHolder.getBean(TurnSTReqMainDao.class);
    private static TurnSTReqDepChildDao turnSTDepChildDao = SpringContextHolder.getBean(TurnSTReqDepChildDao.class);

    public static void transport(String lastestId, String newId) {
        //以前存档的无所属基地不加进来
        String lastEmpId = ArchiveUtils.getSpecifiedArchiveEmptyReq(lastestId);
        TurnSTReqMain turnSTReqMain = new TurnSTReqMain();
        turnSTReqMain.setArchiveId(lastestId);
        List<TurnSTReqMain> list = turnSTReqMainDao.findList(turnSTReqMain);
        for (TurnSTReqMain main : list) {
            if (!main.getId().equals(lastEmpId)) {
                //先找到过去的reqmain
                TurnSTReqDepChild depChild = new TurnSTReqDepChild();
                depChild.setRequirementId(main);
                List<TurnSTReqDepChild> childList = turnSTDepChildDao.findList(depChild);
                //插入成新的main
                main.setId("");
                main.setArchiveId(newId);
                main.preInsert();
                turnSTReqMainDao.insert(main);
                //插入标准下的科室，把dep的req换成新的
                for (TurnSTReqDepChild dep : childList) {
                    dep.setId("");
                    dep.setRequirementId(main);
                    dep.preInsert();
                    turnSTDepChildDao.insert(dep);
                }
            }
        }
    }

}
