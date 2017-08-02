package com.thinkgem.jeesite.modules.turn;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.junit.Test;

import java.text.Collator;
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
    public void testSortChina() {
        String[] ss = {"急诊综合病房",
        "急诊科",
        "重症医学科（中心ICU）",
        "消化内科",
        "内分泌内科",
        "心内科",
        "感染科",
        "呼吸内科",
        "神经内科",
        "肾内血液科",
        "肿瘤科",
        "儿科新生儿科",
        "中西医结合科",
        "皮肤科",
        "普外科",
        "神经外科",
        "口腔科",
        "肝胆外科",
        "胸心外科",
        "疼痛科",
        "骨一科",
        "骨二科",
        "泌尿外科",
        "妇产科",
        "眼科",
        "耳鼻咽喉科",
        "麻醉科",
        "放射科",
        "医学检验科",
        "病理科",
        "输血科",
        "超声科",
        "药学部",
        "消毒供应中心",
        "精神科",
        "凤城社区",
        "江南社区",};
//        String[] ss = {"孙", "孟", "宋", "尹", "廖", "张", "徐", "昆", "曹", "曾", "怡"};
        List<String> ssList = Arrays.asList(ss);
        ssList.sort((String o1, String o2) ->
                Collator.getInstance(Locale.CHINESE).compare(o1, o2));
        ssList.forEach(System.out::println);

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
        try {
            List<String> ret = new ArrayList<>();
            ret.add(PinyinHelper.convertToPinyinString("急诊科", ",", PinyinFormat.WITH_TONE_NUMBER));
            ret.add(PinyinHelper.convertToPinyinString("技术科", ",", PinyinFormat.WITH_TONE_NUMBER));
            ret.add(PinyinHelper.convertToPinyinString("急忙科", ",", PinyinFormat.WITH_TONE_NUMBER));
            ret.add(PinyinHelper.convertToPinyinString("将来忙科", ",", PinyinFormat.WITH_TONE_NUMBER));
            ret.add(PinyinHelper.convertToPinyinString("科教科", ",", PinyinFormat.WITH_TONE_NUMBER));
            ret.add(PinyinHelper.convertToPinyinString("核磁科", ",", PinyinFormat.WITH_TONE_NUMBER));
            Collections.sort(ret);
            System.out.println(ret);
//            System.out.println(s1);
//            System.out.println(s2);
//            System.out.println(s3);
//            System.out.println(s4);
//            System.out.println(s5);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
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