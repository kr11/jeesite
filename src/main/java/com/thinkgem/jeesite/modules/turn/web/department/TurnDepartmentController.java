/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.department;

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
import com.thinkgem.jeesite.modules.turn.entity.department.TurnDepartment;
import com.thinkgem.jeesite.modules.turn.service.department.TurnDepartmentService;

import java.util.List;

/**
 * 排班科室表Controller
 * @author Carrel
 * @version 2017-07-27
 */
@Controller
@RequestMapping(value = "${adminPath}/turn/department/turnDepartment")
public class TurnDepartmentController extends BaseController {

	@Autowired
	private TurnDepartmentService turnDepartmentService;
	
	@ModelAttribute
	public TurnDepartment get(@RequestParam(required=false) String id) {
		TurnDepartment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = turnDepartmentService.get(id);
		}
		if (entity == null){
			entity = new TurnDepartment();
		}
		return entity;
	}
	
	@RequiresPermissions("turn:department:turnDepartment:view")
	@RequestMapping(value = {"list", ""})
	public String list(TurnDepartment turnDepartment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TurnDepartment> page = turnDepartmentService.findPage(new Page<TurnDepartment>(request, response), turnDepartment); 
		model.addAttribute("page", page);
		return "modules/turn/department/turnDepartmentList";
	}

	@RequiresPermissions("turn:department:turnDepartment:view")
	@RequestMapping(value = "form")
	public String form(TurnDepartment turnDepartment, Model model) {
		List<TurnDepartment> depList = turnDepartmentService.findDepartmentList(turnDepartment);
		TurnDepartment zero = new TurnDepartment();
		zero.setDepartmentName("");
		depList.add(0,zero);
		model.addAttribute("departmentList", depList);
		model.addAttribute("turnDepartment", turnDepartment);
		return "modules/turn/department/turnDepartmentForm";
	}

	@RequiresPermissions("turn:department:turnDepartment:edit")
	@RequestMapping(value = "save")
	public String save(TurnDepartment turnDepartment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, turnDepartment)){
			return form(turnDepartment, model);
		}
		turnDepartmentService.save(turnDepartment);
		addMessage(redirectAttributes, "保存科室信息表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/department/turnDepartment/?repage";
	}
	
	@RequiresPermissions("turn:department:turnDepartment:edit")
	@RequestMapping(value = "delete")
	public String delete(TurnDepartment turnDepartment, RedirectAttributes redirectAttributes) {
		turnDepartmentService.delete(turnDepartment);
		addMessage(redirectAttributes, "删除科室信息表成功");
		return "redirect:"+Global.getAdminPath()+"/turn/department/turnDepartment/?repage";
	}

}