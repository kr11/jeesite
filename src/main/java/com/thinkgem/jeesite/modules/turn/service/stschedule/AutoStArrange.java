package com.thinkgem.jeesite.modules.turn.service.stschedule;

import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.dao.stschedule.TurnStScheduleDao;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;

import java.util.*;

/**
 * 规培的智能排班系统
 */
public class AutoStArrange {

    private final TurnStSchedule turnStSchedule;
    private final List<TurnSTReqMain> stReqList;
    private final Map<String, List<TurnSTReqUserChild>> reqUserMap;
    private final Map<String, List<TurnSTReqDepChild>> reqDepMap;
    private final TurnStScheduleDao dao;

    public AutoStArrange(TurnStSchedule turnStSchedule, List<TurnSTReqMain> stReqList,
                         Map<String, List<TurnSTReqUserChild>> reqUserMap,
                         Map<String, List<TurnSTReqDepChild>> reqDepMap,
                         TurnStScheduleDao dao) {
        this.turnStSchedule = turnStSchedule;
        this.stReqList = stReqList;
        this.reqUserMap = reqUserMap;
        this.reqDepMap = reqDepMap;
        this.dao = dao;
    }


    public void backup() {

    }

    private Map<String, List<TriPair<String, Integer, Float>>> userMap() {
        //List<depId, <userId,科室占比>>，按照科室从高到底，然后按照人从高到底
        Map<String, List<TriPair<String, Integer, Float>>> userArrangeMap = new HashMap<>();
        for (TurnSTReqMain turnSTReqMain : stReqList) {
            List<TurnSTReqUserChild> reqUserList = reqUserMap.get(turnSTReqMain.getId());
            if (reqUserList.isEmpty())
                continue;
            List<TurnSTReqDepChild> reqDepList = reqDepMap.get(turnSTReqMain.getId());
            int reqLen = turnSTReqMain.getTotalLength();
            for (TurnSTReqDepChild dep : reqDepList) {
                String depId = dep.getId();
                if (!userArrangeMap.containsKey(depId)) {
                    userArrangeMap.put(depId, new ArrayList<>());
                }
                List<TriPair<String, Integer, Float>> depUserList = userArrangeMap.get(depId);
                int len = Integer.parseInt(dep.getTimeLength());
                for (TurnSTReqUserChild user : reqUserList) {
                    String userId = user.getId();
                    depUserList.add(new TriPair<>(userId, len, ((float) len) / reqLen));
                }
            }
        }
        return userArrangeMap;
    }

    private List<Pair<String, List<TriPair<String, Integer, Float>>>>
    sortDoubleLevel(Map<String, List<TriPair<String, Integer, Float>>> userArrangeMap) {
        //依次排序
        userArrangeMap.forEach((k, d) -> d.sort((s1, s2) -> {
            //由高到低
            float t = s1.right - s2.right;
            if (t > 0) return -1;
            if (t < 0) return 1;
            return 0;
        }));
        //转化为<depId, List>的形式
        List<Pair<String, List<TriPair<String, Integer, Float>>>> userArrangeList = new ArrayList<>();
        userArrangeMap.forEach((key, value) -> userArrangeList.add(new Pair<>(key, value)));
        //排序
        //现在我们的userArrangeList是按照dep的最高值从高到底，每个dep的人从高到底来排序的
        userArrangeList.sort((s1, s2) -> {
            //由高到低
            float t = s1.right.get(0).right - s2.right.get(0).right;
            if (t > 0) return -1;
            if (t < 0) return 1;
            return 0;
        });
        return userArrangeList;
    }

    private Map<String, AutoUser> getUserAutoMap() {
        // userid -> autoUser
        Map<String, AutoUser> userAutoMap = new HashMap<>();
        for (TurnSTReqMain turnSTReqMain : stReqList) {
            List<TurnSTReqUserChild> reqUserList = reqUserMap.get(turnSTReqMain.getId());
            int startInt = ReqTimeUnit.convertYYYY_MM_2Int(turnSTReqMain.getStartYAtM(), turnStSchedule.getTimeUnit()
                    , null);
            int reqLen = turnSTReqMain.getTotalLength();
            for (TurnSTReqUserChild userChild : reqUserList) {
                String userId = userChild.getId();
                AutoUser autoUser = new AutoUser(userId, startInt, reqLen);
                userAutoMap.put(userId, autoUser);
            }
        }
        return userAutoMap;
    }

    /**
     * 自动排班，返回null为正确，否则返回错误信息
     *
     * @return
     */
    public String autoArrange() {
        Map<String, List<TriPair<String, Integer, Float>>> userArrangeMap = userMap();
        // 构建所有人的AutoUser，用以更新:userId->autoUser
        Map<String, AutoUser> autoUserMap = getUserAutoMap();
        //depId ->{userId-> rate}
        List<Pair<String, List<TriPair<String, Integer, Float>>>> userArrangeList = sortDoubleLevel(userArrangeMap);
        //遍历寻找
        for (Pair<String, List<TriPair<String, Integer, Float>>> depUserPair : userArrangeList) {
            String depId = depUserPair.left;
            //left:userId, mid: length, right: float(现在没用了)
            List<TriPair<String, Integer, Float>> userIdSortedList = depUserPair.right;
            for (TriPair<String, Integer, Float> triPair : userIdSortedList) {
                String userId = triPair.left;
                int depLen = triPair.mid;
                AutoUser autoUser = autoUserMap.get(userId);
                autoUser.insert();
            }
            //排列depId

        }
    }

    private class AutoUser {
        public String userId;
        public List<RangeUnit> usedBlock;
        public List<RangeUnit> restBlock;
        public int startInt;
        public int totalLength;

        public AutoUser(String userId, int startInt, int totalLength) {
            this.userId = userId;
            this.usedBlock = new ArrayList<>();
            this.restBlock = new ArrayList<>();
            this.startInt = startInt;
            this.totalLength = totalLength;
        }

        protected void insert(String depLen, String depId, )

    }

    private class RangeUnit {
        public Integer startInt;
        public Integer rangeLength;
    }

    private class Pair<T, V> {
        public T left;
        public V right;

        public Pair(T left, V right) {
            this.left = left;
            this.right = right;
        }
    }

    private class TriPair<T, S, V> {
        public T left;
        public S mid;
        public V right;

        public TriPair(T left, S mid, V right) {
            this.left = left;
            this.mid = mid;
            this.right = right;
        }
    }
}
