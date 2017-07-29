package com.thinkgem.jeesite.modules.turn;

import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;

import java.util.concurrent.TimeUnit;

public enum ReqTimeUnit {
    halfmonth("半月"), onemonth("月"), fiveweek("五周");

    ReqTimeUnit(String name) {
        this.name = name;
    }

    private String name;

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

    public static void addYearAtMonth(boolean isStart, TurnStSchedule ret, String timeUnit, String year_month, int
            offset) {
        int t = convertYYYY_MM_2Int(year_month, timeUnit) + offset;
        String retString;
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                retString = convertInt_2_YYYY_MM(t, timeUnit);
                if (isStart)
                    ret.setStartMonthUpOrDown((((t & 1) == 1) ? "下半月" : "上半月"));
                else
                    ret.setEndMonthUpOrDown((((t & 1) == 1) ? "下半月" : "上半月"));
                break;
            case onemonth:
                retString = convertInt_2_YYYY_MM(t, timeUnit);
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

    public static int convertYYYY_MM_2Int(String startTime, String timeUnit) {
        String[] ym = startTime.split("-");
        int srcY = Integer.valueOf(ym[0]);
        int srcM = Integer.valueOf(ym[1]);
        assert srcM >= 1 && srcM <= 12;
        return isHalfMonth(timeUnit) ? (srcY * 12 + srcM - 1) * 2 : (srcY * 12 + srcM - 1);
    }


    private static String convertInt_2_YYYY_MM(int t, String timeUnit) {
        if (isHalfMonth(timeUnit))
            t /= 2;
        int y = t / 12;
        int m = t % 12;
        return addZeroAtHeadForInt(y, 4)
                + '-' + addZeroAtHeadForInt(m + 1, 2);
    }

    public static int calculate_Diff_YandM_2_Int(String startTime, String offsetTime,
                                                 String timeUnit, String monthUpOrDown) {
        int srcInt = convertYYYY_MM_2Int(startTime, timeUnit);
        int offInt = convertYYYY_MM_2Int(offsetTime, timeUnit);
        return isHalfMonth(timeUnit) ?
                (offInt - srcInt + ("上半月".equals(monthUpOrDown) ? 0 : 1)) :
                (offInt - srcInt);
    }

    public static String getName(String timeUnit) {

        return ReqTimeUnit.valueOf(timeUnit).name;
    }

    public static boolean isHalfMonth(String input) {
        return "halfmonth".equals(input);
    }
}
