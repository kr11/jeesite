/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.stschedule;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.TurnConstant;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqUserChildDao;
import com.thinkgem.jeesite.modules.turn.dao.stschedule.TurnStScheduleDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thinkgem.jeesite.modules.turn.ReqTimeUnit.addZeroAtHeadForInt;

/**
 * 排班-规培调度表Service
 *
 * @author Carrel
 * @version 2017-07-29
 */
@Service
@Transactional(readOnly = true)
public class TurnStScheduleService extends CrudService<TurnStScheduleDao, TurnStSchedule> {

    @Autowired
    private TurnSTReqDepChildDao depChildDao;

    @Autowired
    private TurnSTReqMainDao reqMainDao;

    @Autowired
    private TurnSTReqUserChildDao userChildDao;

    @Autowired
    private TurnArchiveDao turnArchiveDao;

    public TurnStSchedule get(String id) {
        return super.get(id);
    }

    public List<TurnStSchedule> findList(TurnStSchedule turnStSchedule) {
        return super.findList(turnStSchedule);
    }

    public Page<TurnStSchedule> findPage(Page<TurnStSchedule> page, TurnStSchedule turnStSchedule) {
        return super.findPage(page, turnStSchedule);
    }

    @Transactional(readOnly = false)
    public void save(TurnStSchedule turnStSchedule) {
        turnStSchedule.setArchiveId(getArchiveId());
        //除了id，其他的都已经有了，唯一可以编辑的就是start和end，这里处理一下
        //更新的同时，把自己的其他部分找出来，然后覆盖掉
        super.save(turnStSchedule);
    }

    @Transactional(readOnly = false)
    public void delete(TurnStSchedule turnStSchedule) {
        super.delete(turnStSchedule);
    }


    /**
     * 查询当前存档的所有记录，并组织成user->List<sche>的形式；同时构造用户->标准的id的映射
     */
    private Map<String, Map<String, TurnStSchedule>> getUserToScheList(
            TurnStSchedule turnStSchedule) {
        turnStSchedule.setArchiveId(getArchiveId());
        final List<TurnStSchedule> currentSche = findList(turnStSchedule);
        //组织成userid->List<sche>的形式
        Map<String, Map<String, TurnStSchedule>> scheMap = new HashMap<>();
        for (final TurnStSchedule stSchedule : currentSche) {
            String userId = stSchedule.getUser();
            if (!scheMap.containsKey(userId)) {
                scheMap.put(userId, new HashMap<>());
//                userToReqIdMap.put(userId, stSchedule.getRequirementId());
            }
            scheMap.get(userId).put(stSchedule.getDepId(), stSchedule);
        }
        return scheMap;
    }


    /**
     * 获取默认存档的requirement,id->main
     *
     * @return
     */
    private List<TurnSTReqMain> getReqMap() {
        TurnSTReqMain main = new TurnSTReqMain();
        main.setArchiveId(TurnConstant.currentArchive);
        return reqMainDao.findList(main);
    }

    /**
     * 获取指定需求的部门需求,id->depChild
     *
     * @return
     */
    private List<TurnSTReqDepChild> getReqDepList(String stReqMainId) {
        TurnSTReqDepChild depChild = new TurnSTReqDepChild();
        depChild.setRequirementId(new TurnSTReqMain(stReqMainId));
        return depChildDao.findList(depChild);
    }

    /**
     * 获取指定需求的人员需求,id->userChild
     *
     * @return
     */
    private List<TurnSTReqUserChild> getReqUserList(String stReqMainId) {
        TurnSTReqUserChild depChild = new TurnSTReqUserChild();
        depChild.setRequirementId(new TurnSTReqMain(stReqMainId));
        return userChildDao.findList(depChild);
    }

    /**
     * 计算当前的排班和标准规则之间的差距
     */
    public List<TurnStSchedule> calculateDiff(TurnStSchedule turnStSchedule) {
        //user->List<sche>的形式
        Map<String, Map<String, TurnStSchedule>> scheMap = getUserToScheList(turnStSchedule);
        List<TurnSTReqMain> stReqList = getReqMap();
        //获取所有的当年的标准并放入map
        List<TurnStSchedule> diffRet = new ArrayList<>();
        //这一年的所有标准
        for (TurnSTReqMain turnSTReqMain : stReqList) {
            String timeUnit = turnSTReqMain.getTimgUnit();
            //每一个标准的所有人和科室
            List<TurnSTReqUserChild> reqUserList = getReqUserList(turnSTReqMain.getId());
            List<TurnSTReqDepChild> reqDepList = getReqDepList(turnSTReqMain.getId());
            for (TurnSTReqUserChild user : reqUserList) {
                String userId = user.getId();
                for (TurnSTReqDepChild dep : reqDepList) {
                    String oughtLength = dep.getTimeLength() + " 个 " + ReqTimeUnit.getName(timeUnit);
                    if (!scheMap.containsKey(userId) || scheMap.get(userId).containsKey(dep.getDepartmentId())) {
                        //完全不包含个人或者部门，直接标记
                        diffRet.add(diffRequirement(turnSTReqMain, user, dep, null, oughtLength));
                    } else {
                        //包含要求的科室，则判断长短
                        TurnStSchedule userDepSche = scheMap.get(userId).get(dep.getDepartmentId());
                        int actualLen = userDepSche.getEndIntInt() - userDepSche.getStartIntInt();
                        if (ReqTimeUnit.getConvertedTimeLengthInt(dep.getTimeLength(), timeUnit) != actualLen) {
                            //长度不同，标记
                            diffRet.add(diffRequirement(turnSTReqMain, user, dep, userDepSche, oughtLength));
                        }
                    }
                }
            }
        }
        return diffRet;
    }

    private TurnStSchedule diffRequirement(
            TurnSTReqMain turnSTReqMain,
            TurnSTReqUserChild turnSTReqUserChild,
            TurnSTReqDepChild turnSTReqDepChild, TurnStSchedule userDepSche,
            String oughtLength) {
        TurnStSchedule ret = (userDepSche == null) ?
                new TurnStSchedule() : new TurnStSchedule(userDepSche.getId());
        ret.setArchiveId(turnSTReqMain.getArchiveId());
        ret.setDepId(turnSTReqDepChild.getDepartmentId());
        ret.setDepName(turnSTReqDepChild.getDepartmentName().split("@")[1]);
        ret.setUser(turnSTReqUserChild.getId());
        ret.setUserName(turnSTReqUserChild.getUserName());
        ret.setRequirementId(turnSTReqMain.getId());
        if (userDepSche != null) {
            ret.setStartInt(userDepSche.getStartInt());
            ret.setEndInt(userDepSche.getEndInt());
        } else {
            ret.setStartInt("");
            ret.setEndInt("");
        }
        ret.setOughtTimeLength(oughtLength);
        String startYAtM = turnSTReqMain.getStartYAtM();
        String timeUnit = turnSTReqMain.getTimgUnit();
        ret.setReqStartYAndM(startYAtM);

        ret.setTimeUnit(timeUnit);
        if (userDepSche != null) {
            ReqTimeUnit.addYearAtMonth(true, ret, timeUnit, startYAtM, userDepSche.getStartIntInt());
            ReqTimeUnit.addYearAtMonth(false, ret, timeUnit, startYAtM, userDepSche.getEndIntInt());
        } else {
            ret.setStartYandM("未设置");
            ret.setEndYandM("未设置");
        }
        return ret;
    }

    private String getArchiveId() {
        if (TurnConstant.currentArchive == null) {
            TurnArchive arch = new TurnArchive();
            arch.setBooleanIsOpen(true);
            List<TurnArchive> openArch = turnArchiveDao.findList(arch);
            TurnConstant.currentArchive = openArch.get(0).getId();
        }
        return TurnConstant.currentArchive;
    }


    public String convertStartAndEndTime(Model model, TurnStSchedule turnStSchedule) {
        if (!ReqTimeUnit.checkYearAtMonth(turnStSchedule.getStartYandM()) ||
                !ReqTimeUnit.checkYearAtMonth(turnStSchedule.getStartYandM())) {
            return "开始/结束时间输入不合法";
        }
        int startInt = ReqTimeUnit.calculate_Diff_YandM_2_Int(
                turnStSchedule.getReqStartYAndM(), turnStSchedule.getStartYandM(),
                turnStSchedule.getTimeUnit(), turnStSchedule.getStartMonthUpOrDown());

            turnStSchedule.setStartInt(Integer.valueOf(startInt).toString());
        int endInt = ReqTimeUnit.calculate_Diff_YandM_2_Int(
                turnStSchedule.getReqStartYAndM(), turnStSchedule.getEndYandM(),
                turnStSchedule.getTimeUnit(), turnStSchedule.getEndMonthUpOrDown());
        turnStSchedule.setEndInt(Integer.valueOf(endInt).toString());
        return null;
    }
}