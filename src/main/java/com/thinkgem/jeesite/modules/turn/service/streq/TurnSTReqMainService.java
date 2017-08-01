/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.streq;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.keymap.dao.KeyMapDao;
import com.thinkgem.jeesite.modules.keymap.entity.KeyMap;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqUserChildDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排班-规培标准表Service
 *
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
        for (TurnSTReqDepChild turnSTReqDepChild : turnSTReqMain.getTurnSTReqDepChildList()) {
            if (turnSTReqDepChild.getId() == null) {
                continue;
            }
            if (TurnSTReqDepChild.DEL_FLAG_NORMAL.equals(turnSTReqDepChild.getDelFlag())) {
                String[] idAndNames = turnSTReqDepChild.getDepartmentName().split("@");
                turnSTReqDepChild.setDepartmentId(idAndNames[0]);
                if (StringUtils.isBlank(turnSTReqDepChild.getId())) {
                    turnSTReqDepChild.setRequirementId(turnSTReqMain);
                    turnSTReqDepChild.preInsert();
                    turnSTReqDepChildDao.insert(turnSTReqDepChild);
                } else {
                    turnSTReqDepChild.preUpdate();
                    turnSTReqDepChildDao.update(turnSTReqDepChild);
                }
            } else {
                turnSTReqDepChildDao.delete(turnSTReqDepChild);
            }
        }
        for (TurnSTReqUserChild turnSTReqUserChild : turnSTReqMain.getTurnSTReqUserChildList()) {
            if (turnSTReqUserChild.getId() == null) {
                continue;
            }
            if (TurnSTReqUserChild.DEL_FLAG_NORMAL.equals(turnSTReqUserChild.getDelFlag())) {
                if (StringUtils.isBlank(turnSTReqUserChild.getId())) {
                    turnSTReqUserChild.setRequirementId(turnSTReqMain);
                    //temp：临时添加，用户userid=这个表里的id
                    turnSTReqUserChild.setUserId(turnSTReqUserChild.getId());
                    turnSTReqUserChild.preInsert();
                    turnSTReqUserChildDao.insert(turnSTReqUserChild);
                } else {
                    turnSTReqUserChild.preUpdate();
                    turnSTReqUserChildDao.update(turnSTReqUserChild);
                }
            } else {
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


    public TurnSTReqUserChild getUser(TurnSTReqUserChild turnSTReqUserChild) {
        if (StringUtils.isBlank(turnSTReqUserChild.getId())) {
            throw new UnsupportedOperationException();
        }
        String id = turnSTReqUserChild.getId();
        List<TurnSTReqUserChild> userList = turnSTReqUserChildDao.findList(new TurnSTReqUserChild(id));
        if (userList.isEmpty())
            throw new RuntimeException();
        List<TurnSTReqMain> reqList = findList(turnSTReqUserChild.getRequirementId());
        if (reqList.size() != 1)
            throw new RuntimeException();
        TurnSTReqUserChild ret = null;
        for (TurnSTReqUserChild p : userList) {
            if (p.getId().equals(id)) {
                ret = p;
                ret.setRequirementId(reqList.get(0));
                ret.setRequirementName(reqList.get(0).getName());
                ret.setReqBase(reqList.get(0).getReqBase());
                break;
            }
        }
        return ret;
    }

    /**
     * 找到的人，除了自身的限制之外，做两件事：1.当前archived之下，2.加上userId
     *
     * @param turnSTReqUserChild
     * @return
     */
    public List<TurnSTReqUserChild> findUser(TurnSTReqUserChild turnSTReqUserChild) {
        //找出当前archived的标准
        TurnArchive arch = new TurnArchive();
        arch.setBooleanIsOpen(true);
        List<TurnArchive> openArch = turnArchiveDao.findList(arch);
        TurnSTReqMain turnSTReqMain = new TurnSTReqMain();
        String archId = openArch.get(0).getId();
        turnSTReqMain.setArchiveId(archId);
        List<TurnSTReqMain> reqList = findList(turnSTReqMain);
        Map<String, TurnSTReqMain> reqIdMap = new HashMap<>();
        reqList.forEach(p -> reqIdMap.put(p.getId(), p));
        //主子表，必须设置了req才能查到东西？
        List<TurnSTReqUserChild> userList = new ArrayList<>();
        reqList.forEach(p -> {
            TurnSTReqUserChild cd = new TurnSTReqUserChild(turnSTReqUserChild);
            cd.setRequirementId(p);
            List<TurnSTReqUserChild> r = turnSTReqUserChildDao.findList(cd);
            r.forEach(e -> e.setRequirementId(p));
            userList.addAll(r);
        });
        //除了req寻找， 还要找没有归属的
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(archId + "_no_req_id");
        List<KeyMap> kvList = keyMapDao.findList(keyMap);
        for (KeyMap kv : kvList) {
            TurnSTReqUserChild child = new TurnSTReqUserChild(kv.getDictValue());
            List<TurnSTReqUserChild> r = turnSTReqUserChildDao.findList(child);
            userList.addAll(r);
        }
        //筛选，因为基地不是过滤条件，这里要手工过滤
        List<TurnSTReqUserChild> retList = new ArrayList<>();
        userList.forEach(p -> {
            String reqId = p.getRequirementId().getId();
            if (reqIdMap.containsKey(reqId)) {
                TurnSTReqMain main = reqIdMap.get(reqId);
                //如果turnSTReqUserChild的base属性是空或者等于当前属性，则加入
                if (StringUtils.isBlank(turnSTReqUserChild.getReqBase()) ||
                        turnSTReqUserChild.getReqBase().equals(main.getReqBase())) {
                    p.setReqBase(reqIdMap.get(reqId).getReqBase());
                    retList.add(p);
                }
            }
        });
        return retList;
    }

    @Transactional(readOnly = false)
    public void saveUser(TurnSTReqUserChild turnSTReqUserChild) {
        TurnArchive arch = new TurnArchive();
        arch.setBooleanIsOpen(true);
        List<TurnArchive> openArch = turnArchiveDao.findList(arch);
        String archivedId = openArch.get(0).getId();
        if (TurnSTReqUserChild.DEL_FLAG_NORMAL.equals(turnSTReqUserChild.getDelFlag())) {
            if (StringUtils.isBlank(turnSTReqUserChild.getId())) {
                //temp：临时添加，用户userid=这个表里的id
                turnSTReqUserChild.setUserId("deprecated_" + turnSTReqUserChild.getId());
                //判断requireId是否为空，如果为空，则没有标准归宿
                //此时，在KeyMap中存入archiveId + "_no_req_id"为键值，id为值，在获取人员列表的时候用
                turnSTReqUserChild.preInsert();
                turnSTReqUserChildDao.insert(turnSTReqUserChild);
                if (StringUtils.isBlank(turnSTReqUserChild.getRequirementId().getId())) {
                    KeyMap keyMap = new KeyMap();
                    keyMap.setDictKey(archivedId + "_no_req_id");
                    keyMap.setDictValue(turnSTReqUserChild.getId());
                    saveKeyMap(keyMap);
                }
            } else {
                turnSTReqUserChild.preUpdate();
                turnSTReqUserChildDao.update(turnSTReqUserChild);
            }
        } else {
            turnSTReqUserChildDao.delete(turnSTReqUserChild);
        }
    }

    @Autowired
    private KeyMapDao keyMapDao;

    private void saveKeyMap(String key, String value) {
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        keyMap.setDictValue(value);
        if (keyMap.getIsNewRecord()) {
            keyMap.preInsert();
            keyMapDao.insert(keyMap);
        } else {
            keyMap.preUpdate();
            keyMapDao.update(keyMap);
        }
    }
    private String getKeyMapValue(String key){
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        List<KeyMap> ret = keyMapDao.findList(keyMap);
        return ret.isEmpty() ? "" : ret.get(0).getDictValue();
    }

    @Transactional(readOnly = false)
    public void deleteUser(TurnSTReqUserChild turnSTReqUserChild) {
        turnSTReqUserChildDao.delete(turnSTReqUserChild);
    }
}