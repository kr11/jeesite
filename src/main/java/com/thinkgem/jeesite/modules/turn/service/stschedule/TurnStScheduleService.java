/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.service.stschedule;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.modules.keymap.dao.KeyMapDao;
import com.thinkgem.jeesite.modules.keymap.entity.KeyMap;
import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.dao.department.TurnDepartmentDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqDepChildDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqMainDao;
import com.thinkgem.jeesite.modules.turn.dao.streq.TurnSTReqUserChildDao;
import com.thinkgem.jeesite.modules.turn.dao.stschedule.TurnStScheduleDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.ArchiveUtils;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;
import com.thinkgem.jeesite.modules.turn.service.stschedule.TurnStTable.StTableCell;
import com.thinkgem.jeesite.modules.turn.service.stschedule.TurnStTable.StTableLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 排班-规培调度表Service
 *
 * @author Carrel
 * @version 2017-07-29
 */
@Service
@Transactional(readOnly = false)
public class TurnStScheduleService extends CrudService<TurnStScheduleDao, TurnStSchedule> {
    @Autowired
    private TurnDepartmentDao turnDepartmentDao;

    @Autowired
    private TurnSTReqDepChildDao depChildDao;

    @Autowired
    private TurnSTReqMainDao reqMainDao;

    @Autowired
    private TurnSTReqUserChildDao userChildDao;

    @Autowired
    private TurnArchiveDao turnArchiveDao;

    @Autowired
    private KeyMapDao keyMapDao;

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
        turnStSchedule.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        //除了id，其他的都已经有了，唯一可以编辑的就是start和end，这里处理一下
        //更新的同时，把自己的其他部分找出来，然后覆盖掉
        coverSelfOtherSche(turnStSchedule);
    }

    /**
     * 基于当前的修改，找出该用户其他的调度，并覆盖掉冲突调度
     * 左右区间是左闭右开，即：8月~9月，长度为8月这1个月。
     *
     * @param turnStSchedule
     */
    private void coverSelfOtherSche(TurnStSchedule turnStSchedule) {
        TurnStSchedule query = new TurnStSchedule();
        query.setArchiveId(turnStSchedule.getArchiveId());
        query.setUser(turnStSchedule.getUser());
        List<TurnStSchedule> userList = super.findList(query);
        for (TurnStSchedule other : userList) {
            //不同的部分只能以科室区分，自己不算，管其他人
            if (!other.getDepId().equals(turnStSchedule.getDepId())) {
                //判断两个左闭右开区间的重叠
                int oStart = other.getStartIntInt();
                int oEnd = other.getEndIntInt();
                int tStart = turnStSchedule.getStartIntInt();
                int tEnd = turnStSchedule.getEndIntInt();
                //不相邻，不处理
                if (oStart >= tEnd || tStart >= oEnd) {
                } else if (tStart <= oStart && tEnd >= oEnd) {
                    //完全覆盖，删除other
                    delete(other);
                } else if (tStart > oStart && tEnd < oEnd) {
                    //错误：被截断成两个，更新一个，插入一个
                    //更改为：被截断成两个，更新前面的，删掉后面的
//                    TurnStSchedule added = new TurnStSchedule(other);
                    other.setEndInt(tStart);
                    super.save(other);
//                    added.setId(null);
//                    added.setStartInt(tEnd);
//                    super.save(added);
                } else if (oStart < tStart) {
                    //other在左边
                    other.setEndInt(tStart);
                    super.save(other);
                } else if (oEnd > tEnd) {
                    //other在右边
                    other.setStartInt(tEnd);
                    super.save(other);
                } else {
                    throw new UnsupportedOperationException("覆盖情况错误");
                }
            }
        }
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
        turnStSchedule.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        turnStSchedule.setUserName("");
        turnStSchedule.setDepName("");
        turnStSchedule.setUser("");
        TurnStSchedule temp = setIntersect(turnStSchedule);
        final List<TurnStSchedule> currentSche = findList(temp);
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
    private List<TurnSTReqMain> getReqList(TurnStSchedule turnStSchedule) {
        TurnSTReqMain main = new TurnSTReqMain();
        String archId = ArchiveUtils.getOpenedArchiveId();
        main.setArchiveId(archId);
        main.setTimeUnit(turnStSchedule.getTimeUnit());
        List<TurnSTReqMain> rr = reqMainDao.findList(main);
        //获取当前archid的空id
        String emptyReqId = ArchiveUtils.getOpenedArchiveEmptyReq();
        if (StringUtils.isBlank(emptyReqId))
            return rr;
        List<TurnSTReqMain> ret = new ArrayList<>();
        rr.forEach(p -> {
            if (!p.getId().equals(emptyReqId))
                ret.add(p);
        });
        return ret;
    }

    /**
     * 获取指定需求的部门需求,id->depChild
     *
     * @return
     */
    private List<TurnSTReqDepChild> getReqDepList(String stReqMainId, TurnStSchedule turnStSchedule) {
        TurnSTReqDepChild depChild = new TurnSTReqDepChild();
        depChild.setRequirementId(new TurnSTReqMain(stReqMainId));
        depChild.setDepartmentId(turnStSchedule.getDepId());
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
        List<TurnSTReqMain> stReqList = getReqList(turnStSchedule);
        Map<String, String> depIdMap = getDepIdToNameMap();

        //获取所有的当年的标准并放入map
        List<TurnStSchedule> diffRet = new ArrayList<>();
        //这一年的所有标准
        for (TurnSTReqMain turnSTReqMain : stReqList) {
            String timeUnit = turnSTReqMain.getTimeUnit();
            //每一个标准的所有人和科室
            List<TurnSTReqUserChild> reqUserList = getReqUserList(turnSTReqMain.getId());
            List<TurnSTReqDepChild> reqDepList = getReqDepList(turnSTReqMain.getId(), turnStSchedule);
            for (TurnSTReqUserChild user : reqUserList) {
                String userId = user.getId();
                for (TurnSTReqDepChild dep : reqDepList) {
                    String oughtLength = dep.getTimeLength() + " 个 " + ReqTimeUnit.getName(timeUnit);
                    if (!scheMap.containsKey(userId) || !scheMap.get(userId).containsKey(dep.getDepartmentId())) {
                        //完全不包含个人或者部门，直接标记
                        //但是注意，如果日期限制不为空，说明是单个表格点击，这时候只显示相关的列，这样的话不写入不存在的列
                        if (StringUtils.isBlank(turnStSchedule.getIsFromCellClick()))
                            diffRet.add(diffRequirement(false, turnSTReqMain, user, dep, depIdMap,null, oughtLength));
                    } else {
                        //包含要求的科室，则判断长短
                        TurnStSchedule userDepSche = scheMap.get(userId).get(dep.getDepartmentId());
                        int actualLen = userDepSche.getEndIntInt() - userDepSche.getStartIntInt();
//                        if (ReqTimeUnit.getConvertedTimeLengthInt(dep.getTimeLength(), timeUnit) != actualLen) {
                        //如果设置为显示正确，则直接添加
                        if ("1".equals(turnStSchedule.getIsShowCorrect()) || Integer.parseInt(dep.getTimeLength()) !=
                                actualLen) {
                            //长度不同，标记
                            boolean isValid = Integer.parseInt(dep.getTimeLength()) == actualLen;
                            diffRet.add(diffRequirement(isValid, turnSTReqMain, user, dep, depIdMap, userDepSche, oughtLength));
                        }
                    }
                }
            }
        }
        return diffRet;
    }

    private TurnStSchedule diffRequirement(
            boolean isValid, TurnSTReqMain turnSTReqMain,
            TurnSTReqUserChild turnSTReqUserChild,
            TurnSTReqDepChild turnSTReqDepChild, Map<String, String> depIdMap, TurnStSchedule userDepSche,
            String oughtLength) {
        TurnStSchedule ret = (userDepSche == null) ?
                new TurnStSchedule() : new TurnStSchedule(userDepSche.getId());
        ret.setArchiveId(turnSTReqMain.getArchiveId());
        ret.setBooleanIsCorrect(isValid);
        ret.setDepId(turnSTReqDepChild.getDepartmentId());
        ret.setDepName(depIdMap.get(turnSTReqDepChild.getDepartmentId()));
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
        String endYAtM = turnSTReqMain.getEndYAtM();
        String timeUnit = turnSTReqMain.getTimeUnit();
        ret.setReqStartYAndM(startYAtM);
        ret.setReqEndYAndM(endYAtM);

        ret.setTimeUnit(timeUnit);
        if (userDepSche != null) {
            ReqTimeUnit.setYY_MM_UP_DD(true, ret, timeUnit, userDepSche.getStartIntInt());
            ReqTimeUnit.setYY_MM_UP_DD(false, ret, timeUnit, userDepSche.getEndIntInt());
        } else {
            ret.setStartYandM("未设置");
            ret.setEndYandM("未设置");
        }
        return ret;
    }


    private int getTableStartInt(String timeUnit) {
        TurnSTReqMain main = new TurnSTReqMain();
        main.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        main.setTimeUnit(timeUnit);
        List<TurnSTReqMain> archMain = reqMainDao.findList(main);
        if (archMain.isEmpty())
            return -1;
        String start = "";
        for (TurnSTReqMain turnSTReqMain : archMain) {
            if (StringUtils.isNotBlank(turnSTReqMain.getStartYAtM())) {
                start = turnSTReqMain.getStartYAtM();
                break;
            }
        }
        //规培的开始阶段一定是整数，所以halfmonth传一个上半月即可
        return ReqTimeUnit.convertYYYY_MM_2Int(start, timeUnit, "上半月");
//        TurnConstant.currentStTableStartYAndM = ;
//        return TurnConstant.currentStTableStartYAndM;
    }


    public String convertStartAndEndTime(Model model, TurnStSchedule turnStSchedule) {
        String reqStart = turnStSchedule.getReqStartYAndM();
        String reqEnd = turnStSchedule.getReqEndYAndM();
        String thisStart = turnStSchedule.getStartYandM();
        String thisEnd = turnStSchedule.getEndYandM();
        if (reqStart.compareTo(thisStart) > 0 ||
                reqStart.compareTo(thisEnd) >= 0)
            return "本调度的开始/结束时间 早于 标准的开始时间：" + reqStart;
        if (reqEnd.compareTo(thisStart) <= 0 ||
                reqEnd.compareTo(thisEnd) < 0)
            return "本调度的开始/结束时间 晚于 标准的结束时间：" + reqEnd;

        if (!ReqTimeUnit.checkYearAtMonth(thisStart) ||
                !ReqTimeUnit.checkYearAtMonth(thisStart)) {
            return "开始/结束时间输入不合法";
        }

        int startInt = ReqTimeUnit.convertYYYY_MM_2Int(thisStart, turnStSchedule.getTimeUnit(),
                turnStSchedule.getStartMonthUpOrDown());

        int endInt = ReqTimeUnit.convertYYYY_MM_2Int(thisEnd, turnStSchedule.getTimeUnit(),
                turnStSchedule.getEndMonthUpOrDown());
        if (endInt <= startInt)
            return "本调度的开始时间晚于结束时间";
        turnStSchedule.setStartInt(Integer.valueOf(startInt).toString());
        turnStSchedule.setEndInt(Integer.valueOf(endInt).toString());
        return null;
    }

    /**
     * 返回指定时间段表格，可编辑。
     *
     * @param turnStSchedule
     * @return
     */
    public TurnStTable calculateCurrentTable(boolean isNoArch, TurnStSchedule turnStSchedule) {
        //如果没有设置，则设置页面的时间类型，默认是onemonth
        if (StringUtils.isBlank(turnStSchedule.getTimeUnit()))
            turnStSchedule.setTimeUnit(ReqTimeUnit.onemonth.toString());
        //如果没有设置，则设置页面的页面大小，允许在table页面设置，默认是properties中的值
        if (turnStSchedule.getTablePageSize() == -1)
            turnStSchedule.setTablePageSize(Integer.parseInt(Global.getConfig("turnTable.defaultSize")));
        //错：如果没有设置，则设置页面的开始时间：改为：不允许设置，直接自动获取
        if (turnStSchedule.getTableStart() == -1) {
            int start = getTableStartInt(turnStSchedule.getTimeUnit());
            if (start == -1) {
//                仍为-1，说明没有指定任何标准，设为当前日期值
                start = ReqTimeUnit.getCurrentYYYYMM(turnStSchedule.getTimeUnit());
            }
            turnStSchedule.setTableStart(start);
        }
        //原则是：指定存档，指定类型（timeUnit）,开始时间和结束时间，找出来所有人，分科室排开
        if (isNoArch)
            turnStSchedule.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        else
            turnStSchedule.setArchiveId("");
        //开始时间结束时间的查询条件设置：
        TurnStSchedule temp = setIntersect(turnStSchedule);
        List<TurnStSchedule> scheListTemp = findList(temp);
        //获取科室的depId->depName
        Map<String, String> depIdToNameMap = getDepIdToNameMap();
        //获取人的id->name
        TurnSTReqUserChild depChild = new TurnSTReqUserChild();
        List<TurnSTReqUserChild> userList = userChildDao.findList(depChild);
        //找出timeUnit的标准，过滤掉记录
        TurnSTReqMain main = new TurnSTReqMain();
        main.setTimeUnit(turnStSchedule.getTimeUnit());
        List<TurnSTReqMain> reqList = reqMainDao.findList(main);
        Set<String> validReqSet = new HashSet<>();
        reqList.forEach(p -> validReqSet.add(p.getId()));
        List<TurnStSchedule> scheList = new ArrayList<>();
        scheListTemp.forEach(p -> {
            if (validReqSet.contains(p.getRequirementId()))
                scheList.add(p);
        });
        //过滤完成
        Map<String, String> userIdNameMap = new HashMap<>();
        userList.forEach(u -> userIdNameMap.put(u.getId(), u.getUserName()));
        TurnStTable tableList = constructEditTable(turnStSchedule, depIdToNameMap, scheList, userIdNameMap);
        return tableList;
    }

    public List<TurnDepartment> getDepartmentList() {
        TurnDepartment turnDepartment = new TurnDepartment();
        turnDepartment.setBelongArchiveId(ArchiveUtils.getOpenedArchiveId());
        return turnDepartmentDao.findList(turnDepartment);
    }

    private Map<String, String> getDepIdToNameMap() {
        List<TurnDepartment> list = getDepartmentList();
        Map<String, String> ret = new HashMap<>();
        list.forEach(l -> ret.put(l.getId(), l.getDepartmentName()));
        return ret;
    }



    private TurnStTable constructEditTable(TurnStSchedule turnStSchedule, Map<String, String> depIdToNameMap,
                                           List<TurnStSchedule> scheList, Map<String,
            String> userIdNameMap) {
        TurnStTable retTable = new TurnStTable();
        int startInt = turnStSchedule.getTableStart();
        int tableSize = turnStSchedule.getTablePageSize();
        String timeUnit = turnStSchedule.getTimeUnit();
        //时间表头
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < turnStSchedule.getTablePageSize(); i++) {
            dateList.add(ReqTimeUnit.convertInt_2_YYYY_MM_detail(startInt + i, timeUnit));
        }
        retTable.setDateList(dateList);
        //每行表身
        Map<String, StTableLine> depLineMap = new HashMap<>();
        for (TurnStSchedule stSchedule : scheList) {
            String depId = stSchedule.getDepId();
//            String userName = stSchedule.getUserName();
            String userName = userIdNameMap.get(stSchedule.getUser());
            StTableLine line;
            if (!depLineMap.containsKey(depId)) {
                line = new StTableLine();
                //line header:0：depId，1：depName
                line.addLineHeader(depId);
                line.addLineHeader(depIdToNameMap.get(depId));
                for (int i = 0; i < tableSize; i++) {
                    line.addCell(new StTableCell());
                }
                depLineMap.put(depId, line);
            } else {
                line = depLineMap.get(depId);
            }
            List<StTableCell> cellList = line.getCellList();
            for (int i = Math.max(startInt, stSchedule.getStartIntInt());
                 i < Math.min(startInt + tableSize, stSchedule.getEndIntInt()); i++) {
                StTableCell cell = cellList.get(i - startInt);
                if (cell.getCellHeaderList() == null || cell.getCellHeaderList().isEmpty()) {
                    //初始化cell头
                    //cell 在st中的标准：header:0：cellTimeInt，绝对时间整数
                    cell.addCellHeader(Integer.valueOf(i).toString());
                }
                //加入名字
                cell.addCellContent(userName);
            }
        }
        retTable.setLineList(new ArrayList<>(depLineMap.values()));
        return retTable;
    }

    /**
     * 开始时间结束时间的查询条件设置
     *
     * @param turnStSchedule
     */
    private TurnStSchedule setIntersect(TurnStSchedule turnStSchedule) {
        // 对于aSt,aEnd,以及查询条件的s，e,找出与[s,e)相交的部分，条件为s<aEnd and e > aSt
        //sql里的条件是：a.start_int < #{startInt} AND a.end_int > #{endInt}
        TurnStSchedule turn = new TurnStSchedule(turnStSchedule);
        String temp = turn.getStartInt();
        turn.setStartInt(turn.getEndInt());
        turn.setEndInt(temp);
        return turn;
    }

    @Transactional(readOnly = false)
    public String createAutoArrange(long seed, TurnStSchedule turnStSchedule) {
        //获取timeUnit，archive下所有的标准
        //重新排班
        List<TurnSTReqMain> stReqList = getReqList(turnStSchedule);
        Map<String, List<TurnSTReqUserChild>> reqUserMap = new HashMap<>();
        Map<String, List<TurnSTReqDepChild>> reqDepMap = new HashMap<>();
        for (TurnSTReqMain turnSTReqMain : stReqList) {
            //每一个标准的所有人和科室
            List<TurnSTReqUserChild> reqUserList = getReqUserList(turnSTReqMain.getId());
            List<TurnSTReqDepChild> reqDepList = getReqDepList(turnSTReqMain.getId(), turnStSchedule);
            reqUserMap.put(turnSTReqMain.getId(), reqUserList);
            reqDepMap.put(turnSTReqMain.getId(), reqDepList);
        }
        turnStSchedule.setArchiveId(ArchiveUtils.getOpenedArchiveId());
        AutoStArrange range = new AutoStArrange(turnStSchedule, stReqList, reqUserMap, reqDepMap, this);
        return range.autoArrange(seed);
    }

    /**
     * 原则：
     * 输入等于-1或者其他；
     * 等于-1，重新生成，写入数据库；
     * 等于其他，去数据库找，没有则重新生成
     * 有，不存储
     *
     * @param request
     * @return
     */
    public long getSeed(HttpServletRequest request, TurnStSchedule turnStSchedule) {
        String seedStr = request.getParameter("randomSeed");
        String key = ArchiveUtils.getOpenedArchiveId() + "_" + turnStSchedule.getTimeUnit() + "_seed";
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        List<KeyMap> ret = keyMapDao.findList(keyMap);
        long seed;

        if (StringUtils.isNotBlank(seedStr) && Long.parseLong(seedStr) == -1) {
            //-1，重新生成
            seed = System.nanoTime();
            if (!ret.isEmpty())
                keyMap.setId(ret.get(0).getId());
            keyMap.setDictValue(Long.valueOf(seed).toString());
            save(keyMap);
            //取反，告诉外面是重置还是重生
            seed = -seed;
        } else if (ret.isEmpty()) {
            //不是重生，但是原来没有，重新设置一个
            seed = System.nanoTime();
            keyMap.setDictValue(Long.valueOf(seed).toString());
            save(keyMap);
        } else {
//            原来已有，直接获取即可
            seed = Long.parseLong(ret.get(0).getDictValue());
        }
        return seed;
    }

    private void save(KeyMap keyMap) {
        if (keyMap.getIsNewRecord()) {
            keyMap.preInsert();
            keyMapDao.insert(keyMap);
        } else {
            keyMap.preUpdate();
            keyMapDao.update(keyMap);
        }
    }

    public ExportExcel generateExcel(TurnStSchedule turnStSchedule) {
        String timeUnit = turnStSchedule.getTimeUnit();
        //默认存档，空基地已经被去除
        List<TurnSTReqMain> stReqList = getReqList(turnStSchedule);
        int earliestInt = Integer.MAX_VALUE;
        for (TurnSTReqMain p : stReqList) {
            int start = ReqTimeUnit.convertYYYY_MM_2Int(p.getStartYAtM(), timeUnit, null);
            if (earliestInt > start)
                earliestInt = start;
        }
        int latestInt = -1;
        for (TurnSTReqMain p : stReqList) {
            int end = ReqTimeUnit.convertYYYY_MM_2Int(p.getEndYAtM(), timeUnit, null);
            if (latestInt < end)
                latestInt = end;
        }
        //不断向前生成一张张表，每张表宽度是pageSize，最后的不足没关系
        //起点是earliestInt，earliestInt+pageSize，+2*page,....
        List<TurnStTable> tableList = new ArrayList<>();
        int pageSize = turnStSchedule.getTablePageSize();
        for (int i = earliestInt; i < latestInt; i += pageSize) {
            turnStSchedule.setTableStart(i);
            turnStSchedule.setTablePageSize(pageSize);
            tableList.add(calculateCurrentTable(true, turnStSchedule));
        }
        if (tableList.isEmpty())
            return null;
        String name = ArchiveUtils.getOpenedArchiveName() + "_排班总表.xlsx";
        return TurnStTable.convertToExcel(name, tableList);
    }
}