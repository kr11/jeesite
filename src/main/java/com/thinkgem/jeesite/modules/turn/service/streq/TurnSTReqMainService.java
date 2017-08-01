/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.streq;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.keymap.KeyMapUtils;
import com.thinkgem.jeesite.modules.keymap.dao.KeyMapDao;
import com.thinkgem.jeesite.modules.keymap.entity.KeyMap;
import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqUserChildDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import javafx.scene.shape.Arc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Keymap;
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
@Transactional(readOnly = false)
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

    /**
     * 去掉空
     *
     * @param turnSTReqMain
     * @return
     */
    public List<TurnSTReqMain> findList(TurnSTReqMain turnSTReqMain) {
        String empId = ArchiveUtils.getOpenedArchiveEmptyReq();
        turnSTReqMain.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        List<TurnSTReqMain> rr = super.findList(turnSTReqMain);
        List<TurnSTReqMain> ret = new ArrayList<>();
        rr.forEach(p -> {
            if (!p.getId().equals(empId)) ret.add(p);
        });
        return ret;
    }

    public Page<TurnSTReqMain> findPage(Page<TurnSTReqMain> page, TurnSTReqMain turnSTReqMain) {
        turnSTReqMain.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        turnSTReqMain.setPage(page);
        page.setList(findList(turnSTReqMain));
        return page;
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

    private TurnSTReqMain createEmptyReq() {
        TurnSTReqMain noReq = new TurnSTReqMain();
        String archId = ArchiveUtils.getOpenedArchiveId();
        noReq.setArchiveId(archId);
        noReq.setTimeUnit(ReqTimeUnit.onemonth.toString());
        noReq.setTotalLength(0);
        noReq.setName(Global.getConfig("turn.noreq.reqname"));
        noReq.setDelFlag("0");
        super.save(noReq);
        //将生成的当前存档的空标准加入keyMap
        ArchiveUtils.saveOpenedArchiveEmptyReq(noReq.getId());

        return noReq;
    }

    /**
     * 获取一个人
     * 注意，不是查询，而是form的时候用到
     *
     * @param turnSTReqUserChild
     * @return
     */
    public TurnSTReqUserChild getUser(TurnSTReqUserChild turnSTReqUserChild) {
        TurnSTReqMain noReq;
        String emptyReqId = ArchiveUtils.getOpenedArchiveEmptyReq();
        if (StringUtils.isBlank(turnSTReqUserChild.getId())) {
            //所以如果id为空，找当前存档的空指标。注意，新创建存档后复制标准的时候，注册复制后的req
            if (StringUtils.isBlank(emptyReqId)) {
//                没有空标准，先注册一个名叫"无培训标准"的标准
                noReq = createEmptyReq();
            } else {
                noReq = findList(new TurnSTReqMain(emptyReqId)).get(0);
            }
            TurnSTReqUserChild ret = new TurnSTReqUserChild(noReq);
            ret.setRequirementName(noReq.getName());
            ret.setReqBase(noReq.getReqBase());
            return ret;
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
        if (turnSTReqUserChild.getRequirementId().getId().equals(emptyReqId)) {
            assert ret != null;
            ret.setReqBase(ArchiveUtils.getEmptyUserReqBase(turnSTReqUserChild.getId()));
            ret.getRequirementId().setReqBase(ArchiveUtils.getEmptyUserReqBase(turnSTReqUserChild.getId()));
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
        String archId = ArchiveUtils.getOpenedArchiveId();
        TurnSTReqMain turnSTReqMain = new TurnSTReqMain();
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
        String emptyReqId = ArchiveUtils.getOpenedArchiveEmptyReq();
        //筛选，因为基地不是过滤条件，这里要手工过滤
        List<TurnSTReqUserChild> retList = new ArrayList<>();
        userList.forEach(p -> {
            String reqId = p.getRequirementId().getId();
            if (reqIdMap.containsKey(reqId)) {
                if (emptyReqId.equals(reqId)) {
                    String reqBase = ArchiveUtils.getEmptyUserReqBase(p.getId());
                    if (StringUtils.isBlank(turnSTReqUserChild.getReqBase()) ||
                            turnSTReqUserChild.getReqBase().equals(reqBase)) {
                        p.setReqBase(reqBase);
                        retList.add(p);
                    }
                } else {
                    TurnSTReqMain main = reqIdMap.get(reqId);
                    //如果turnSTReqUserChild的base属性是空或者等于当前属性，则加入
                    if (StringUtils.isBlank(turnSTReqUserChild.getReqBase()) ||
                            turnSTReqUserChild.getReqBase().equals(main.getReqBase())) {
                        p.setReqBase(reqIdMap.get(reqId).getReqBase());
                        retList.add(p);
                    }
                }
            }
        });
        //除了req寻找， 还要找没有归属的，这个不受reqId的限制
//        List<String> childIdList = ArchiveUtils.getNowEmptyReqUser();
//        for (String childId : childIdList) {
//            //找到他的reqBase
//            TurnSTReqUserChild child = new TurnSTReqUserChild(childId);
//            child.setRequirementId(new TurnSTReqMain(emptyReqId));
//            List<TurnSTReqUserChild> r = turnSTReqUserChildDao.findList(child);
//            if (!r.isEmpty()) {
//                TurnSTReqUserChild add = r.get(0);
//                add.setReqBase(ArchiveUtils.getEmptyUserReqBase(add.getId()));
//                userList.add(add);
//            }
//        }
        return retList;
    }

    @Transactional(readOnly = false)
    public void saveUser(TurnSTReqUserChild turnSTReqUserChild) {
        String archivedId = ArchiveUtils.getOpenedArchiveId();
        String currentEmptyReqId = ArchiveUtils.getOpenedArchiveEmptyReq();
        TurnSTReqMain emReq = findList(new TurnSTReqMain(currentEmptyReqId)).get(0);
        if (TurnSTReqUserChild.DEL_FLAG_NORMAL.equals(turnSTReqUserChild.getDelFlag())) {
            if (StringUtils.isBlank(turnSTReqUserChild.getId())) {
                //temp：临时添加，用户userid=这个表里的id
                turnSTReqUserChild.setUserId("deprecated_" + turnSTReqUserChild.getId());
                //判断requireId是否为空，如果为空，则没有标准归宿
                //空是在新建的时候，非空但等于empty是在修改的时候
                //此时，在KeyMap中存入archiveId + "_no_req_id"为键值，id为值，在获取人员列表的时候用
                if (StringUtils.isBlank(turnSTReqUserChild.getRequirementId().getId())
                        || turnSTReqUserChild.getRequirementId().getId().equals(currentEmptyReqId)) {
                    turnSTReqUserChild.setRequirementId(emReq);
                }
                turnSTReqUserChild.preInsert();
                turnSTReqUserChildDao.insert(turnSTReqUserChild);
                if (StringUtils.isBlank(turnSTReqUserChild.getRequirementId().getId())
                        || turnSTReqUserChild.getRequirementId().getId().equals(currentEmptyReqId)) {
                    KeyMapUtils.saveKeyMap(archivedId + "_no_req_id", turnSTReqUserChild.getId());
                    String reqBase = StringUtils.isNotBlank(turnSTReqUserChild.getReqBase()) ?
                            turnSTReqUserChild.getReqBase() :
                            turnSTReqUserChild.getRequirementId().getReqBase();
                    ArchiveUtils.saveEmptyUserReqBase(turnSTReqUserChild.getId(), reqBase);
                }
            } else {
                if (StringUtils.isBlank(turnSTReqUserChild.getRequirementId().getId())
                        || turnSTReqUserChild.getRequirementId().getId().equals(currentEmptyReqId)) {
                    turnSTReqUserChild.setRequirementId(emReq);
                }
                turnSTReqUserChild.preUpdate();
                turnSTReqUserChildDao.update(turnSTReqUserChild);
            }
        } else {
            turnSTReqUserChildDao.delete(turnSTReqUserChild);
        }
    }


    @Transactional(readOnly = false)
    public void deleteUser(TurnSTReqUserChild turnSTReqUserChild) {
        turnSTReqUserChildDao.delete(turnSTReqUserChild);
    }
}