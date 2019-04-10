/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yyborderdetailone.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.modules.yyborderdetailone.entity.YybOrderDeatilOne;
import com.jeeplus.modules.yyborderdetailone.service.YybOrderDeatilOneService;

/**
 * 订单详情Controller
 * @author lwb
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/yyborderdetailone/yybOrderDeatilOne")
public class YybOrderDeatilOneController extends BaseController {

	@Autowired
	private YybOrderDeatilOneService yybOrderDeatilOneService;

		/**
	 * 订单详情列表数据
	 */
	@ResponseBody
	@RequiresPermissions("yyborderdetailone:yybOrderDeatilOne:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybOrderDeatilOne yybOrderDeatilOne, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		yybOrderDeatilOne.setCompanyId(user.getOffice().getId());
		Page<YybOrderDeatilOne> page = yybOrderDeatilOneService.findPage(new Page<YybOrderDeatilOne>(request, response), yybOrderDeatilOne);
		return getBootstrapData(page);
	}


	/**
	 * 订单详情列表页面
	 */
	@RequiresPermissions("yyborderdetailone:yybOrderDeatilOne:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybOrderDeatilOne yybOrderDeatilOne, Model model) {
		model.addAttribute("yybOrderDeatilOne", yybOrderDeatilOne);
		return "modules/yyborderdetailone/yybOrderDeatilOneList";
	}




}