package com.thinkgem.jeesite.modules.turn;

import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.junit.Test;

import java.util.List;

public class DictTest {
    @Test
    public void testDict() {
        String type = "onemonth" + 123123123;
        List<Dict> ret = DictUtils.getDictList(type);
        System.out.println(ret);
    }
}
