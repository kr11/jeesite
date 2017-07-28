/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.streq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.service.department.TurnDepartmentService;
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
import com.thinkgem.jeesite.modules.turn.entity.streq.TurnSTReqMain;
import com.thinkgem.jeesite.modules.turn.service.streq.TurnSTReqMainService;

import java.util.List;

/**
 * 排班-规培标准表Controller
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
	public TurnSTReqMain get(@RequestParam(required=false) String id) {
		TurnSTReqMain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = turnSTReqMainService.get(id);
		}
		if (entity == null){
			entity = new TurnSTReqMain();
		}
		return entity;
	}
	
	@RequiresPermissions("turn:streq:turnSTReqMain:view")
	@RequestMapping(value = {"list", ""})
	public String list(TurnSTReqMain turnSTReqMain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TurnSTReqMain> page = turnSTReqMainService.findPage(new Page<TurnSTReqMain>(request, response), turnSTReqMain); 
		model.addAttribute("page", page);
		return "modules/turn/streq/turnSTReqMainList";
	}

	@RequiresPermissions("turn:streq:turnSTReqMain:view")
	@RequestMapping(value = "form")
	public String form(TurnSTReqMain turnSTReqMain, Model model) {
		model.addAttribute("turnSTReqMain", turnSTReqMain);
		TurnDepartment turnDepartment = new TurnDepartment();
		turnDepartment.setBooleanIsUsed(true);
		List<TurnDepartment> depList = turnDepartmentService.findDepartmentList(turnDepartment);
		model.addAttribute("departmentList", depList);
		return "modules/turn/streq/turnSTReqMainForm";
	}

	@RequiresPermissions("turn:streq:turnSTReqMain:edit")
	@RequestMapping(value = "save")
	public String save(TurnSTReqMain turnSTReqMain, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, turnSTReqMain)){
			return form(turnSTReqMain, model);
		}
		turnSTReqMainService.save(turnSTReqMain);
		addMessage(redirectAttributes, "保存规培标准表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/streq/turnSTReqMain/?repage";
	}
	
	@RequiresPermissions("turn:streq:turnSTReqMain:edit")
	@RequestMapping(value = "delete")
	public String delete(TurnSTReqMain turnSTReqMain, RedirectAttributes redirectAttributes) {
		turnSTReqMainService.delete(turnSTReqMain);
		addMessage(redirectAttributes, "删除规培标准表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/streq/turnSTReqMain/?repage";
	}

}