package com.thinkgem.jeesite.modules.turn;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ReqTimeUnitTest {
    @Test
    public void addZeroAtHeadForInt() throws Exception {
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(107,4));
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(07,4));
        System.out.println(ReqTimeUnit.addZeroAtHeadForInt(3,2));

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