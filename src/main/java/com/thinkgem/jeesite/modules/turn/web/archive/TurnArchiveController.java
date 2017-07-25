/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.turn.web.archive;

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
import com.thinkgem.jeesite.modules.turn.entity.archive.TurnArchive;
import com.thinkgem.jeesite.modules.turn.service.archive.TurnArchiveService;

/**
 * 排版中存档的增删改查功能Controller
 * @author Carrel
 * @version 2017-07-25
 */
@Controller
@RequestMapping(value = "${adminPath}/turn/archive/turnArchive")
public class TurnArchiveController extends BaseController {

	@Autowired
	private TurnArchiveService turnArchiveService;
	
	@ModelAttribute
	public TurnArchive get(@RequestParam(required=false) String id) {
		TurnArchive entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = turnArchiveService.get(id);
		}
		if (entity == null){
			entity = new TurnArchive();
		}
		return entity;
	}
	
	@RequiresPermissions("turn:archive:turnArchive:view")
	@RequestMapping(value = {"list", ""})
	public String list(TurnArchive turnArchive, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TurnArchive> page = turnArchiveService.findPage(new Page<TurnArchive>(request, response), turnArchive); 
		model.addAttribute("page", page);
		return "modules/turn/archive/turnArchiveList";
	}

	@RequiresPermissions("turn:archive:turnArchive:view")
	@RequestMapping(value = "form")
	public String form(TurnArchive turnArchive, Model model) {
		model.addAttribute("turnArchive", turnArchive);
		return "modules/turn/archive/turnArchiveForm";
	}

	@RequiresPermissions("turn:archive:turnArchive:edit")
	@RequestMapping(value = "save")
	public String save(TurnArchive turnArchive, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, turnArchive)){
			return form(turnArchive, model);
		}
		turnArchiveService.save(turnArchive);
		addMessage(redirectAttributes, "保存排班-存档成功");
		return "redirect:"+Global.getAdminPath()+"/turn/archive/turnArchive/?repage";
	}
	
	@RequiresPermissions("turn:archive:turnArchive:edit")
	@RequestMapping(value = "delete")
	public String delete(TurnArchive turnArchive, RedirectAttributes redirectAttributes) {
		turnArchiveService.delete(turnArchive);
		addMessage(redirectAttributes, "删除排班-存档成功");
		return "redirect:"+Global.getAdminPath()+"/turn/archive/turnArchive/?repage";
	}

}