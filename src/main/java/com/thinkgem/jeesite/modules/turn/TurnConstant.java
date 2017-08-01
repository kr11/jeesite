package com.thinkgem.jeesite.modules.turn;

import com.thinkgem.jeesite.modules.turn.dao.archive.TurnArchiveDao;
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TurnConstant {
//    @Autowired
//    static private TurnArchiveDao turnArchiveDao;

//    public static String currentArchive = null;

    public static String currentStTableStartYAndM = null;

}
