package com.thinkgem.jeesite.modules.turn;

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

    /**
     * @param year_month year_month格式为YYYY-MM
     * @param offset     直接调用getStartInt的结果，而不是上面翻译过的，上面的函数统一成了半月，不对
     * @return
     */
    public static String addYearAtMonth(String timeUnit, String year_month, int offset) {
        String[] ym = year_month.split("-");
        int addedY = Integer.valueOf(ym[0]);
        int addedM = Integer.valueOf(ym[1]);
        switch (ReqTimeUnit.valueOf(timeUnit)) {
            case halfmonth:
                int t = addedY * 24 + addedM * 2 + offset;
                return addZeroAtHeadForInt(t / 24, 4)
                        + '-' + addZeroAtHeadForInt((t % 24) / 2, 2) +
                        '-' + (((t & 1) == 1) ? "下旬" : "上旬");
            case onemonth:
                t = addedY * 12 + addedM + offset;
                return addZeroAtHeadForInt(t / 12, 4)
                        + '-' + addZeroAtHeadForInt((t % 12), 2);
            case fiveweek:
            default:
                throw new UnsupportedOperationException();
        }
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

    public static String getName(String timeUnit) {

        return ReqTimeUnit.valueOf(timeUnit).name;
    }
}
