package com.thinkgem.jeesite.modules.turn;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class ReqTimeUnitTest {
    @Test
    public void addZeroAtHeadForInt() throws Exception {
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(107,4));
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(07,4));
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(3,2));
        Calendar c = Calendar.getInstance();
        ;
//        System.out.println(date.getYear());
        System.out.println(c.get(Calendar.YEAR));
        System.out.println(c.get(Calendar.MONTH));

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        System.out.println(dateString.substring(0,7));
    }

    @Test
    public void addYearAtMonth() {
//        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",24));
//        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",13));
//        System.out.println(ReqTimeUnit.addYearAtMonth("halfmonth","2017-07",13));
//        System.out.println(ReqTimeUnit.addYearAtMonth("halfmonth","2017-07",12));
//        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",6));
//        System.out.println("adsasd");
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            list.add(i);
//        }
//        System.out.println(list);
//        System.out.println(list.size());
    }

    @Test
    public void convert() {
        System.out.println("adsasd");
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        System.out.println(list.size());
        list.add(list.size(),123);
        System.out.println(list);
    }
}