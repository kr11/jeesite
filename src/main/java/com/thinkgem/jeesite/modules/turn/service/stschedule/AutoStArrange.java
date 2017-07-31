package com.thinkgem.jeesite.modules.turn.service.stschedule;

import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;

import java.util.*;


/**
 * 规培的智能排班系统
 */
class AutoStArrange {

    private final TurnStSchedule turnStSchedule;
    private final List<TurnSTReqMain> stReqList;
    private final Map<String, List<TurnSTReqUserChild>> reqUserMap;
    private final Map<String, List<TurnSTReqDepChild>> reqDepMap;
    private final TurnStScheduleService service;
    private final String timeUnit;
    private Random random;

    private Map<String, String> userIdToReqIdMap = new HashMap<>();

    AutoStArrange(TurnStSchedule turnStSchedule, List<TurnSTReqMain> stReqList,
                  Map<String, List<TurnSTReqUserChild>> reqUserMap,
                  Map<String, List<TurnSTReqDepChild>> reqDepMap,
                  TurnStScheduleService service) {
        this.turnStSchedule = turnStSchedule;
        this.timeUnit = turnStSchedule.getTimeUnit();
        this.stReqList = stReqList;
        this.reqUserMap = reqUserMap;
        this.reqDepMap = reqDepMap;
        this.service = service;
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
                String depId = dep.getDepartmentId();
                if (!userArrangeMap.containsKey(depId)) {
                    userArrangeMap.put(depId, new ArrayList<>());
                }
                List<TriPair<String, Integer, Float>> depUserList = userArrangeMap.get(depId);
                int len = Integer.parseInt(dep.getTimeLength());
                for (TurnSTReqUserChild user : reqUserList) {
                    String userId = user.getId();
                    depUserList.add(new TriPair<>(userId, len, ((float) len) / reqLen));
                    userIdToReqIdMap.put(userId, turnSTReqMain.getId());
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
    public String autoArrange(long randomSeed) {
        random = new Random(randomSeed);
        //获取最早和最晚时间点
        int earliestInt = Integer.MAX_VALUE;
        for (TurnSTReqMain p : stReqList) {
            int start = ReqTimeUnit.convertYYYY_MM_2Int(p.getStartYAtM(), timeUnit, null);
            if (earliestInt > start)
                earliestInt = start;
        }
        int latestInt = Integer.MAX_VALUE;
        for (TurnSTReqMain p : stReqList) {
            int end = ReqTimeUnit.convertYYYY_MM_2Int(p.getEndYAtM(), timeUnit, null);
            if (latestInt > end)
                latestInt = end;
        }
        Map<String, List<TriPair<String, Integer, Float>>> userArrangeMap = userMap();
        // 构建所有人的AutoUser，用以更新:userId->autoUser
        Map<String, AutoUser> autoUserMap = getUserAutoMap();
        //depId ->{userId-> rate}
        List<Pair<String, List<TriPair<String, Integer, Float>>>> userArrangeList = sortDoubleLevel(userArrangeMap);
        //遍历寻找
        for (Pair<String, List<TriPair<String, Integer, Float>>> depUserPair : userArrangeList) {
            String depId = depUserPair.left;
            //分配一个offsetArray记录当前的排班情况
            OffsetArray offsetArray = new OffsetArray(earliestInt, latestInt);
            //left:userId, mid: length, right: float(现在没用了)
            List<TriPair<String, Integer, Float>> userIdSortedList = depUserPair.right;
            for (TriPair<String, Integer, Float> triPair : userIdSortedList) {
                String userId = triPair.left;
                int depLen = triPair.mid;
                AutoUser autoUser = autoUserMap.get(userId);
                autoUser.insert(depId, depLen, offsetArray);
            }
        }
        insertAll(autoUserMap);
        return null;
    }

    /**
     * 将所有排好的班插入
     *
     * @param autoUserMap
     */
    private void insertAll(Map<String, AutoUser> autoUserMap) {
        //删除所有已有的排班
        TurnStSchedule deleteT = new TurnStSchedule();
        String archiveId = turnStSchedule.getArchiveId();
        deleteT.setArchiveId(archiveId);
        deleteT.setTimeUnit(turnStSchedule.getTimeUnit());
        List<TurnStSchedule> scheList = service.findList(deleteT);
        scheList.forEach(service::delete);
        //插入所有排班
        TurnStSchedule tt = new TurnStSchedule();
        tt.setArchiveId(archiveId);
        tt.setTimeUnit(timeUnit);
        tt.setDelFlag("0");
        autoUserMap.forEach((k, v) -> {
            {
                tt.setUser(v.userId);
                tt.setRequirementId(userIdToReqIdMap.get(v.userId));
                for (RangeUnit rangeUnit : v.usedBlock) {
                    tt.setId("");
                    tt.setUserName("deprecated_user_" + random.nextInt());
                    tt.setDepId(rangeUnit.depId);
                    tt.setDepName("deprecated_dep_" + random.nextInt());
                    tt.setStartInt(rangeUnit.startInt);
                    tt.setEndInt(rangeUnit.startInt + rangeUnit.rangeLength);
                    service.save(tt);
                }
            }
        });
    }


    private class AutoUser {
        public String userId;
        public List<RangeUnit> usedBlock;
        public List<RangeUnit> restBlock;
        public int startInt;
        public int totalLength;

        public AutoUser(String userId, int startInt, int totalLength) {
            this.userId = userId;
            this.usedBlock = new LinkedList<>();
            this.restBlock = new LinkedList<>();
            this.startInt = startInt;
            this.totalLength = totalLength;
        }

        /**
         * 给定信息，插入一个元素，更新自己和depCovered
         *
         * @param depId：待插入的科室id
         * @param depLen：待插入的科室时长
         * @param depCovered：这个科室的全局占用情况
         */
        protected void insert(String depId, int depLen, OffsetArray depCovered) {
            List<Integer> candidates = makeRoom(depLen);
            //decided是决定排班的地方，如20121到20122，插入两个月
            int decided = determineInsert(depLen, candidates, depCovered);
            //插入到usedBlock中，insertIndex是指插入到usedBlock的index
            int insertIndex;
            for (insertIndex = 0; insertIndex < usedBlock.size(); insertIndex++) {
                int current = usedBlock.get(insertIndex).startInt;
                if (current == decided)
                    throw new RuntimeException("why equal?");
                if (current > decided)
                    break;
            }
            usedBlock.add(insertIndex, new RangeUnit(depId, decided, depLen));
            //更新depCovered
            depCovered.addRange(decided, depLen, 1);
        }

        /**
         * 给定了可以插入的地方，然后寻找合适的分布
         * 这里的原则是，选择最低哇的一段，即，从s到s+reqLen，已有的部分值总和最小
         *
         * @param depLen
         * @param candidates
         * @param depCovered
         * @return
         */
        protected int determineInsert(int depLen, List<Integer> candidates, OffsetArray depCovered) {
            assert !candidates.isEmpty();
            int startI = candidates.get(0);
            //初始化状态
            int sum = 0;
            int minSum = Integer.MAX_VALUE;
            List<Integer> selectedList = new ArrayList<>();
            for (int i = startI; i < startI + depLen - 1; i++) {
                sum += depCovered.get(i);
            }
            for (int i = startI; i <= candidates.get(candidates.size() - 1); i++) {
                sum += depCovered.get(i + depLen - 1);
                if (sum < minSum) {
                    selectedList.clear();
                    minSum = sum;
                    selectedList.add(i);
                } else if (sum == minSum) {
                    selectedList.add(i);
                }
                sum -= depCovered.get(i);
            }
            return chooseRuleForEqualCandidate(selectedList);
        }

        /**
         * 对于没有差别的候选集选择哪个？可选
         * 这里的策略：随机选择
         *
         * @param list
         * @return
         */
        protected int chooseRuleForEqualCandidate(List<Integer> list) {
            assert !list.isEmpty();
//            return list.get(random.nextInt(list.size()));
            return list.get(0);
        }

        /**
         * 如果没有空间就腾出来，如果腾不出来就报错
         *
         * @param requiredLen
         * @return
         */
        private List<Integer> makeRoom(int requiredLen) {
            List<Integer> candidate = findRoom(requiredLen);
            if (candidate.isEmpty()) {
                //合并空间，全部前推
                compactRoom(requiredLen);
                candidate = findRoom(requiredLen);
            }
            return candidate;
        }

        /**
         * 要腾出一个requiredLen长度的地方，策略自选
         * 这里的策略是全部往前堆
         *
         * @param requiredLen
         */
        void compactRoom(int requiredLen) {
            if (requiredLen > totalLength)
                throw new RuntimeException("要求的空间大于已有的空间！出错了！");
            List<RangeUnit> newUsedBlock = new ArrayList<>();
            int lastStart = startInt;
            for (RangeUnit ru : usedBlock) {
                RangeUnit n = new RangeUnit(ru.depId, lastStart, ru.rangeLength);
                newUsedBlock.add(n);
                lastStart += ru.rangeLength;
            }
            this.usedBlock = newUsedBlock;
        }

        /**
         * 搜索[startInt, startInt+totalLength)的区间，找可空长度，形成候选列表
         *
         * @param requiredLen
         * @return return null if makes room failed.
         */
        private List<Integer> findRoom(int requiredLen) {
            List<Integer> candidate = new ArrayList<>();
            if (usedBlock.isEmpty()) {
                addRange(startInt, startInt + totalLength, requiredLen, candidate);
            } else {
                //开始的
                addRange(startInt, usedBlock.get(0).startInt, requiredLen, candidate);
                //usedBlock是排好序的
                int i;
                for (i = 1; i < usedBlock.size(); i++) {
                    addRange(usedBlock.get(i - 1).startInt + usedBlock.get(i - 1).rangeLength,
                            usedBlock.get(i).startInt, requiredLen, candidate);
                }
                //最后的
                addRange(usedBlock.get(i - 1).startInt + usedBlock.get(i - 1).rangeLength,
                        startInt + totalLength, requiredLen, candidate);
            }
            return candidate;
        }
    }


    private void addRange(int start, int end, int requiredLen, List<Integer> candidate) {
        if (end - start < requiredLen)
            return;
        for (int i = 0; i <= end - start - requiredLen; i++) {
            candidate.add(start + i);
        }
    }

    /**
     * 偏移数组，给定开始和终点，然后像正常数组一样处理
     */
    public class OffsetArray {
        private final int earliestInt;
        private final int latestInt;
        private final int len;
        private final int[] data;
        private int highest;

        public OffsetArray(int earliestInt, int latestInt) {
            assert earliestInt < latestInt;
            this.earliestInt = earliestInt;
            this.latestInt = latestInt;
            len = latestInt - earliestInt;
            data = new int[len];
            highest = 0;
        }

        public int get(int i) {
            return data[i - earliestInt];
        }

        /**
         * 在一段范围内的所有值增加add
         *
         * @param start
         * @param l
         * @param add
         */
        public void addRange(int start, int l, int add) {
            assert start >= earliestInt && start + l <= earliestInt + len;
            for (int i = start; i < start + l; i++) {
                data[i - earliestInt] += add;
            }
        }
    }

    private class RangeUnit {
        String depId;
        Integer startInt;

        public RangeUnit(String depId, Integer startInt, Integer rangeLength) {
            this.depId = depId;
            this.startInt = startInt;
            this.rangeLength = rangeLength;
        }

        Integer rangeLength;


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
