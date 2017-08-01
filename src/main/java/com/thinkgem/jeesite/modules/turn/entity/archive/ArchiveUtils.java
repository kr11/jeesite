package com.thinkgem.jeesite.modules.turn.entity.archive;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.keymap.KeyMapUtils;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;

import java.util.List;

public class ArchiveUtils {
    public static String currentArchive = null;
    private static TurnArchiveDao turnArchiveDao = SpringContextHolder.getBean(TurnArchiveDao.class);

    public static String getOpenedArchiveId() {
        if(StringUtils.isBlank(currentArchive)) {
            TurnArchive arch = new TurnArchive();
            arch.setBooleanIsOpen(true);
            List<TurnArchive> openArch = turnArchiveDao.findList(arch);
            currentArchive =  openArch.get(0).getId();
        }
        return currentArchive;
    }

    public static String getOpenedArchiveEmptyReq() {
        String archId = getOpenedArchiveId();
        return KeyMapUtils.getKeyMapValue(archId + "@" + "emptyReq");
    }

    public static void saveOpenedArchiveEmptyReq(String emptyId) {
        String archId = getOpenedArchiveId();
        KeyMapUtils.saveKeyMap(archId + "@" + "emptyReq", emptyId);
    }

    public static List<String> getNowEmptyReqUser() {
        String archId = getOpenedArchiveId();
        return KeyMapUtils.getKeyMapValueArray(archId + "_no_req_id");
    }

    /**
     * 将无主之人的基地存入
     * @param empUserId
     * @param reqBaseName
     */
    public static void saveEmptyUserReqBase(String empUserId, String reqBaseName) {
        KeyMapUtils.saveKeyMap(empUserId + "@" + "emptyReqBase", reqBaseName);
    }
    /**
     * 将无主之人的基地读出
     * @param empUserId
     */
    public static String getEmptyUserReqBase(String empUserId) {
        return KeyMapUtils.getKeyMapValue(empUserId + "@" + "emptyReqBase");
    }
}
