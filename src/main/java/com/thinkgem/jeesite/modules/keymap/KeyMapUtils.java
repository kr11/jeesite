package com.thinkgem.jeesite.modules.keymap;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.keymap.dao.KeyMapDao;
import com.thinkgem.jeesite.modules.keymap.entity.KeyMap;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class KeyMapUtils {
    private static KeyMapDao keyMapDao = SpringContextHolder.getBean(KeyMapDao.class);

    public static List<String> getKeyMapValueArray(String key){
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        List<KeyMap> ret = keyMapDao.findList(keyMap);
        List<String> rettt = new ArrayList<>();
        ret.forEach(p->rettt.add(p.getDictValue()));
        return rettt;
    }

    public static void saveKeyMap(String key, String value) {
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        keyMap.setDictValue(value);
        if (keyMap.getIsNewRecord()) {
            keyMap.preInsert();
            keyMapDao.insert(keyMap);
        } else {
            keyMap.preUpdate();
            keyMapDao.update(keyMap);
        }
    }

    public static String getKeyMapValue(String key) {
        KeyMap keyMap = new KeyMap();
        keyMap.setDictKey(key);
        List<KeyMap> ret = keyMapDao.findList(keyMap);
        return ret.isEmpty() ? "" : ret.get(0).getDictValue();
    }
}
