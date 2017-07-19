/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.myfirst.web;

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
import com.thinkgem.jeesite.modules.myfirst.entity.MyTestData;
import com.thinkgem.jeesite.modules.myfirst.service.MyTestDataService;

/**
 * 尝试性的用户Controller
 * @author Carrel
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/myfirst/myTestData")
public class MyTestDataController extends BaseController {

	@Autowired
	private MyTestDataService myTestDataService;
	
	@ModelAttribute
	public MyTestData get(@RequestParam(required=false) String id) {
		MyTestData entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = myTestDataService.get(id);
		}
		if (entity == null){
			entity = new MyTestData();
		}
		return entity;
	}
	
	@RequiresPermissions("myfirst:myTestData:view")
	@RequestMapping(value = {"list", ""})
	public String list(MyTestData myTestData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MyTestData> page = myTestDataService.findPage(new Page<MyTestData>(request, response), myTestData); 
		model.addAttribute("page", page);
		return "modules/myfirst/myTestDataList";
	}

	@RequiresPermissions("myfirst:myTestData:view")
	@RequestMapping(value = "form")
	public String form(MyTestData myTestData, Model model) {
		model.addAttribute("myTestData", myTestData);
		return "modules/myfirst/myTestDataForm";
	}

	@RequiresPermissions("myfirst:myTestData:edit")
	@RequestMapping(value = "save")
	public String save(MyTestData myTestData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, myTestData)){
			return form(myTestData, model);
		}
		myTestDataService.save(myTestData);
		addMessage(redirectAttributes, "保存我的第一张表成功");
		return "redirect:"+Global.getAdminPath()+"/myfirst/myTestData/?repage";
	}
	
	@RequiresPermissions("myfirst:myTestData:edit")
	@RequestMapping(value = "delete")
	public String delete(MyTestData myTestData, RedirectAttributes redirectAttributes) {
		myTestDataService.delete(myTestData);
		addMessage(redirectAttributes, "删除我的第一张表成功");
		return "redirect:"+Global.getAdminPath()+"/myfirst/myTestData/?repage";
	}

}