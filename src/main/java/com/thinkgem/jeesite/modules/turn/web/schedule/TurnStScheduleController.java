/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.schedule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.turn.entity.schedule.TurnStSchedule;
import com.thinkgem.jeesite.modules.turn.service.schedule.TurnStScheduleService;

/**
 * 排班-规培调度表Controller
 * @author Carrel
 * @version 2017-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/turn/schedule/turnStSchedule")
public class TurnStScheduleController extends BaseController {

	@Autowired
	private TurnStScheduleService turnStScheduleService;
	
	@ModelAttribute
	public TurnStSchedule get(@RequestParam(required=false) String id) {
		TurnStSchedule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = turnStScheduleService.get(id);
		}
		if (entity == null){
			entity = new TurnStSchedule();
		}
		return entity;
	}
	
	@RequiresPermissions("turn:schedule:turnStSchedule:view")
	@RequestMapping(value = {"list", ""})
	public String list(TurnStSchedule turnStSchedule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TurnStSchedule> page = turnStScheduleService.findPage(new Page<TurnStSchedule>(request, response), turnStSchedule); 
		model.addAttribute("page", page);
		return "modules/turn/schedule/turnStScheduleList";
	}

	@RequiresPermissions("turn:schedule:turnStSchedule:view")
	@RequestMapping(value = "form")
	public String form(TurnStSchedule turnStSchedule, Model model) {
		model.addAttribute("turnStSchedule", turnStSchedule);
		return "modules/turn/schedule/turnStScheduleForm";
	}

	@RequiresPermissions("turn:schedule:turnStSchedule:edit")
	@RequestMapping(value = "save")
	public String save(TurnStSchedule turnStSchedule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, turnStSchedule)){
			return form(turnStSchedule, model);
		}
		turnStScheduleService.save(turnStSchedule);
		addMessage(redirectAttributes, "保存排班-规培调度表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/schedule/turnStSchedule/?repage";
	}
	
	@RequiresPermissions("turn:schedule:turnStSchedule:edit")
	@RequestMapping(value = "delete")
	public String delete(TurnStSchedule turnStSchedule, RedirectAttributes redirectAttributes) {
		turnStScheduleService.delete(turnStSchedule);
		addMessage(redirectAttributes, "删除排班-规培调度表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/schedule/turnStSchedule/?repage";
	}

}