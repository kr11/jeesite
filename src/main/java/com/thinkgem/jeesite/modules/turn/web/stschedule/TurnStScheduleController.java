/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.stschedule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.service.stschedule.TurnStTable;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.turn.entity.stschedule.TurnStSchedule;
import com.thinkgem.jeesite.modules.turn.service.stschedule.TurnStScheduleService;

import java.util.List;

/**
 * 排班-规培调度表Controller
 *
 * @author Carrel
 * @version 2017-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/turn/stschedule/turnStSchedule")
public class TurnStScheduleController extends BaseController {

    @Autowired
    private TurnStScheduleService turnStScheduleService;

    @ModelAttribute
    public TurnStSchedule get(@RequestParam(required = false) String id) {
        TurnStSchedule entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = turnStScheduleService.get(id);
        }
        if (entity == null) {
            entity = new TurnStSchedule();
        }
        return entity;
    }

    @RequiresPermissions("turn:stschedule:turnStSchedule:view")
    @RequestMapping(value = {"list", ""})
    public String list(TurnStSchedule turnStSchedule, Model model) {
//		Page<TurnStSchedule> page = turnStScheduleService.findPage(new Page<TurnStSchedule>(request, response),
// turnStSchedule);
        List<TurnStSchedule> diffList = turnStScheduleService.calculateDiff(turnStSchedule);
        model.addAttribute("diffList", diffList);
        return "modules/turn/stschedule/turnStScheduleList";
    }

    @RequiresPermissions("turn:stschedule:turnStSchedule:view")
    @RequestMapping(value = "form")
    public String form(TurnStSchedule turnStSchedule, Model model) {
        if (StringUtils.isBlank(turnStSchedule.getTimeUnit()))
            throw new UnsupportedOperationException("timeunit can never be null");
        if (StringUtils.isBlank(turnStSchedule.getTimeUnit())) {
            turnStSchedule.setTimeUnit(turnStSchedule.getTimeUnit());
        }
        model.addAttribute("turnStSchedule", turnStSchedule);
        return "modules/turn/stschedule/turnStScheduleForm";
    }

    @RequiresPermissions("turn:stschedule:turnStSchedule:edit")
    @RequestMapping(value = "save")
    public String save(TurnStSchedule turnStSchedule, Model model) {
        //在验证最终写入的数据之前，先转化起止时间，然后验证，然后save的时候去更改自己人的其他东西
        String ret = turnStScheduleService.convertStartAndEndTime(model, turnStSchedule);
        if (ret != null) {
            addMessage(model, ret);
            return form(turnStSchedule, model);
        }
        if (!beanValidator(model, turnStSchedule)) {
            return form(turnStSchedule, model);
        }
        turnStScheduleService.save(turnStSchedule);
        addMessage(model, "保存排班-规培调度表成功");
//        return "redirect:" + Global.getAdminPath() + "/turn/stschedule/turnStSchedule/?repage";
        //刷新turnStSchedule，用户、科室、起止时间都去掉，只剩下timeUnit和archiveId
        TurnStSchedule newTurn = new TurnStSchedule();
        newTurn.setTimeUnit(turnStSchedule.getTimeUnit());
        return list(newTurn, model);
    }

    @RequiresPermissions("turn:stschedule:turnStSchedule:edit")
    @RequestMapping(value = "delete")
    public String delete(TurnStSchedule turnStSchedule, Model model) {
        turnStScheduleService.delete(turnStSchedule);
        addMessage(model, "删除排班-规培调度表成功");
//        return "redirect:" + Global.getAdminPath() + "/turn/stschedule/turnStSchedule/?repage";
        TurnStSchedule newTurn = new TurnStSchedule();
        newTurn.setTimeUnit(turnStSchedule.getTimeUnit());
        return list(newTurn, model);
    }

    /**
     * 一月，即规培的非助理全科部分
     *
     * @param turnStSchedule
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("turn:stschedule:turnStSchedule:view")
    @RequestMapping(value = {"tableEdit"})
    public String tableEdit(TurnStSchedule turnStSchedule, HttpServletRequest request, HttpServletResponse response,
                            Model model) {
        TurnStTable editTableList = turnStScheduleService.calculateCurrentTable(turnStSchedule);
        model.addAttribute("editTableList", editTableList);
        //老的已经没有作用了，用一个新的代替，避免什么参数被影响到
        TurnStSchedule turn = new TurnStSchedule();
        turn.setTimeUnit(turnStSchedule.getTimeUnit());
        turn.setTablePageSize(turnStSchedule.getTablePageSize());
        turn.setTableStart(turnStSchedule.getTableStart());
        model.addAttribute("turnStSchedule", turn);
        return "modules/turn/stschedule/turnStScheduleTable";
    }

    @RequiresPermissions("turn:stschedule:turnStSchedule:edit")
    @RequestMapping(value = {"autoArrange"})
    public String autoArrange(TurnStSchedule turnStSchedule, HttpServletRequest request, HttpServletResponse response,
                              Model model) {
        
        TurnStTable editTableList = turnStScheduleService.calculateCurrentTable(turnStSchedule);
        model.addAttribute("editTableList", editTableList);
        //老的已经没有作用了，用一个新的代替，避免什么参数被影响到
        TurnStSchedule turn = new TurnStSchedule();
        turn.setTimeUnit(turnStSchedule.getTimeUnit());
        turn.setTablePageSize(turnStSchedule.getTablePageSize());
        turn.setTableStart(turnStSchedule.getTableStart());
        model.addAttribute("turnStSchedule", turn);
        return "modules/turn/stschedule/turnStScheduleTable";
    }

}