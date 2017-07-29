package com.thinkgem.jeesite.modules.turn;

import org.junit.Test;

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
        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",24));
        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",13));
        System.out.println(ReqTimeUnit.addYearAtMonth("halfmonth","2017-07",13));
        System.out.println(ReqTimeUnit.addYearAtMonth("halfmonth","2017-07",12));
        System.out.println(ReqTimeUnit.addYearAtMonth("onemonth","2017-07",6));
    }
}