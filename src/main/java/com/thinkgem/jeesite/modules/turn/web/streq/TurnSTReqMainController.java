/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.streq;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.turn.ReqTimeUnit;
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqDepChild;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqUserChild;
import com.thinkgem.jeesite.modules.turn.service.department.TurnDepartmentService;
import com.thinkgem.jeesite.modules.turn.service.streq.TurnSTReqMainService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;

/**
 * 排班-规培标准表Controller
 *
 * @author Carrel
 * @version 2017-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/turn/streq/turnSTReqMain")
public class TurnSTReqMainController extends BaseController {

    @Autowired
    private TurnSTReqMainService turnSTReqMainService;

    @Autowired
    private TurnDepartmentService turnDepartmentService;


    @ModelAttribute
    public TurnSTReqMain get(@RequestParam(required = false) String id) {
        TurnSTReqMain entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = turnSTReqMainService.get(id);
        }
        if (entity == null) {
            entity = new TurnSTReqMain();
        }
        return entity;
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:view")
    @RequestMapping(value = {"list", ""})
    public String list(TurnSTReqMain turnSTReqMain, HttpServletRequest request, HttpServletResponse response, Model
            model) {
        Page<TurnSTReqMain> page = turnSTReqMainService.findPage(new Page<TurnSTReqMain>(request, response),
                turnSTReqMain);
        model.addAttribute("page", page);
        return "modules/turn/streq/turnSTReqMainList";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:view")
    @RequestMapping(value = "form")
    public String form(TurnSTReqMain turnSTReqMain, Model model) {
        TurnDepartment turnDepartment = new TurnDepartment();
        turnDepartment.setBooleanIsUsed(true);
        List<TurnDepartment> depList = turnDepartmentService.findDepartmentList(turnDepartment);
        model.addAttribute("departmentList", depList);
        model.addAttribute("turnSTReqMain", turnSTReqMain);
        return "modules/turn/streq/turnSTReqMainForm";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:edit")
    @RequestMapping(value = "save")
    public String save(TurnSTReqMain turnSTReqMain, Model model, RedirectAttributes redirectAttributes) {
        if (!ReqTimeUnit.checkYearAtMonth(turnSTReqMain.getStartYAtM()) ||
                !ReqTimeUnit.checkYearAtMonth(turnSTReqMain.getEndYAtM())) {
            addMessage(model, "开始/结束时间输入不合法");
            return form(turnSTReqMain, model);
        }
        if (turnSTReqMain.getStartYAtM().compareTo(
                turnSTReqMain.getEndYAtM()) >= 0) {
            addMessage(model, "开始时间>=结束时间");
            return form(turnSTReqMain, model);
        }
        int sum = 0;
        for (TurnSTReqDepChild child : turnSTReqMain.getTurnSTReqDepChildList()) {
            sum += Integer.parseInt(child.getTimeLength());
        }
        if(sum != turnSTReqMain.getTotalLength()){
            addMessage(model, "科室时长总和是"+sum+"，不等于规定时长"+turnSTReqMain.getTotalLength());
            return form(turnSTReqMain, model);
        }

        //时间长度约束
        if (!beanValidator(model, turnSTReqMain)) {
            return form(turnSTReqMain, model);
        }
        turnSTReqMainService.save(turnSTReqMain);
        addMessage(redirectAttributes, "保存规培标准表成功");
        return "redirect:" + Global.getAdminPath() + "/turn/streq/turnSTReqMain/?repage";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:edit")
    @RequestMapping(value = "delete")
    public String delete(TurnSTReqMain turnSTReqMain, RedirectAttributes redirectAttributes) {
        turnSTReqMainService.delete(turnSTReqMain);
        addMessage(redirectAttributes, "删除规培标准表成功");
        return "redirect:" + Global.getAdminPath() + "/turn/streq/turnSTReqMain/?repage";
    }

    //user的部分
    @RequiresPermissions("turn:streq:turnSTReqMain:view")
    @RequestMapping(value = {"userList"})
    public String userList(TurnSTReqMain turnSTReqMain, HttpServletRequest request, HttpServletResponse response,
                           Model model) {
        TurnSTReqUserChild child = turnSTReqMain.getTheChild();
        child.setRequirementId(turnSTReqMain);
        child.setReqBase(turnSTReqMain.getReqBase());
        List<TurnSTReqUserChild> page = turnSTReqMainService.findUser(child);
        model.addAttribute("userList", page);
        return "modules/turn/streq/StReqUserList";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:edit")
    @RequestMapping(value = "userForm")
    public String userForm(TurnSTReqMain turnSTReqMain, Model model) {
        //取了个巧，userForm的id就是要的userId
//        TurnSTReqUserChild child = turnSTReqMainService.getUser(turnSTReqMain.get());
//        turnSTReqMain.setTheChild(child);
        TurnSTReqUserChild t = turnSTReqMain.getTheChild();
        t.setRequirementId(turnSTReqMain);
        TurnSTReqUserChild tt = turnSTReqMainService.getUser(t);
        turnSTReqMain.setTheChild(tt);
//        model.addAttribute("turnSTReqMain", turnSTReqMain);
        return "modules/turn/streq/StReqUserForm";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:edit")
    @RequestMapping(value = "userSave")
    public String userSave(TurnSTReqMain turnSTReqMain, Model model, RedirectAttributes redirectAttributes) {
        TurnSTReqUserChild t = turnSTReqMain.getTheChild();
        t.setRequirementId(turnSTReqMain);
//        if (StringUtils.isBlank(t.getRequirementId().getId()))
//            t.setRequirementId(turnSTReqMain);
        if (StringUtils.isBlank(t.getRequirementName()))
            t.setRequirementName(turnSTReqMain.getName());
        turnSTReqMainService.saveUser(t);
        addMessage(redirectAttributes, "保存人员列表成功");
        return "redirect:" + Global.getAdminPath() + "/turn/streq/turnSTReqMain/userList?repage";
    }

    @RequiresPermissions("turn:streq:turnSTReqMain:edit")
    @RequestMapping(value = "userDelete")
    public String userDelete(TurnSTReqMain turnSTReqMain, RedirectAttributes redirectAttributes) {
        turnSTReqMainService.deleteUser(turnSTReqMain.getTheChild());
        addMessage(redirectAttributes, "删除复制人员子表成功");
        return "redirect:" + Global.getAdminPath() + "/turn/streq/turnSTReqMain/userList?repage";
    }
}