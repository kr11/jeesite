package com.thinkgem.jeesite.modules.turn;

import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public enum ReqTimeUnit {
    halfmonth("半月", "助理全科"), onemonth("月", "规培（无助理）"), fiveweek("五周", "实习");

    ReqTimeUnit(String name, String turnSysName) {
        this.name = name;
        this.turnSysName = turnSysName;
    }

    private String name;
    private String turnSysName;

    public static int getConvertedTimeLengthInt(String timeLength, String timeUnit) {
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                return Integer.valueOf(timeLength);
            case onemonth:
                return Integer.valueOf(timeLength) * 2;
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }

    }

    public static boolean checkYearAtMonth(String input) {
        if (input.indexOf("-") != 4 || input.length() != 4 + 1 + 2)
            return false;
        String[] ym = input.split("-");
        try {
            int y = Integer.parseInt(ym[0]);
            if (y < 1)
                return false;
            int m = Integer.parseInt(ym[1]);
            return m >= 1 && m <= 12;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setYY_MM_UP_DD(boolean isStart, TurnStSchedule ret, String timeUnit, int
            timeInt) {
        String retString;
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                retString = convertInt_2_YYYY_MM(timeInt, timeUnit);
                if (isStart)
                    ret.setStartMonthUpOrDown(oneZero_convertTo_UpOrDown(timeInt));
                else
                    ret.setEndMonthUpOrDown(oneZero_convertTo_UpOrDown(timeInt));
                break;
            case onemonth:
                retString = convertInt_2_YYYY_MM(timeInt, timeUnit);
                ret.setStartMonthUpOrDown(null);
                ret.setEndMonthUpOrDown(null);
                break;
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }
        if (isStart)
            ret.setStartYandM(retString);
        else
            ret.setEndYandM(retString);
    }


    public static String addZeroAtHeadForInt(int number, int length) {
        String n = Integer.valueOf(number).toString();
        assert n.length() <= length;
        char[] c = new char[length];
        for (int i = 0; i < c.length; i++) {
            c[i] = '0';
        }
        n.getChars(0, n.length(), c, length - n.length());
        return new String(c);
    }

    public static int convertYYYY_MM_2Int(String startTime, String timeUnit, String halfUpDown) {
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                String[] ym = startTime.split("-");
                int srcY = Integer.valueOf(ym[0]);
                int srcM = Integer.valueOf(ym[1]);
                assert srcM >= 1 && srcM <= 12;
                return (srcY * 12 + srcM - 1) * 2 + upOrDown_convertTo_oneZero(halfUpDown);
            case onemonth:
                ym = startTime.split("-");
                srcY = Integer.valueOf(ym[0]);
                srcM = Integer.valueOf(ym[1]);
                assert srcM >= 1 && srcM <= 12;
                return (srcY * 12 + srcM - 1);
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 仅将int转化为年月形式，半月的上下，五周的日期都会都掉，仅内部
     *
     * @param t
     * @param timeUnit
     * @return
     */
    private static String convertInt_2_YYYY_MM(int t, String timeUnit) {
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                t /= 2;
            case onemonth:
                int y = t / 12;
                int m = t % 12;
                return addZeroAtHeadForInt(y, 4)
                        + '-' + addZeroAtHeadForInt(m + 1, 2);
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * int转化为:
     * month：年月形式
     * 半月：年月-上下半月，五周的日期都会都掉，仅内部
     *
     * @param t
     * @param timeUnit
     * @return
     */
    public static String convertInt_2_YYYY_MM_detail(int t, String timeUnit) {
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                int y = t / 24;
                int m = t % 24;
                return addZeroAtHeadForInt(y, 4)
                        + '-' + addZeroAtHeadForInt(m + 1, 2)
                        + '-' + oneZero_convertTo_UpOrDown(t);
            case onemonth:
                y = t / 12;
                m = t % 12;
                return addZeroAtHeadForInt(y, 4)
                        + '-' + addZeroAtHeadForInt(m + 1, 2);
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }
    }

//    public static int calculate_Diff_YandM_2_Int(String startTime, String offsetTime,
//                                                 String timeUnit, String monthUpOrDown) {
//        int srcInt = convertYYYY_MM_2Int(startTime, timeUnit);
//        int offInt = convertYYYY_MM_2Int(offsetTime, timeUnit);
//        return isHalfMonth(timeUnit) ?
//                (offInt - srcInt + ("上半月".equals(monthUpOrDown) ? 0 : 1)) :
//                (offInt - srcInt);
//    }

    public static String getName(String timeUnit) {

        return ReqTimeUnit.valueOf(timeUnit).name;
    }

    public static boolean isHalfMonth(String input) {
        return "halfmonth".equals(input);
    }

    public static String oneZero_convertTo_UpOrDown(int i) {
        return (i & 1) == 1 ? "下半月" : "上半月";
    }

    public static int upOrDown_convertTo_oneZero(String i) {
        return (i == null || "上半月".equals(i)) ? 0 : 1;
    }

    public String getTurnSysName() {
        return turnSysName;
    }

    public static String getTurnSysName(String timeUnit) {
        return ReqTimeUnit.valueOf(timeUnit).getTurnSysName();
    }

    public void setTurnSysName(String turnSysName) {
        this.turnSysName = turnSysName;
    }

    public static int getCurrentYYYYMM(String timeUnit) {
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
            case onemonth:
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                return ReqTimeUnit.convertYYYY_MM_2Int(dateString.substring(0,7), timeUnit, "上半月");
            case fiveweek:
            default:
                throw new RuntimeException("getCurrentYYYYMM error");
        }
//        return ReqTimeUnit.convertYYYY_MM_2Int(start, timeUnit, "上半月");
    }
}
