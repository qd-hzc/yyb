/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.member.service.YybMemberService;

/**
 * 会员Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/member/yybMember")
public class YybMemberController extends BaseController {

	@Autowired
	private YybMemberService yybMemberService;
	
	@ModelAttribute
	public YybMember get(@RequestParam(required=false) String id) {
		YybMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybMemberService.get(id);
		}
		if (entity == null){
			entity = new YybMember();
		}
		return entity;
	}
	
	/**
	 * 会员列表页面
	 */
	@RequiresPermissions("member:yybMember:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybMember yybMember, Model model) {
		model.addAttribute("yybMember", yybMember);
		return "modules/member/yybMemberList";
	}
	
		/**
	 * 会员列表数据
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybMember yybMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybMember> page = yybMemberService.findPage(new Page<YybMember>(request, response), yybMember); 
		return getBootstrapData(page);
	}

}