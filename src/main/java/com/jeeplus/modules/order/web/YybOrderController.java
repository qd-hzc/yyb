/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web;

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
import com.jeeplus.modules.order.entity.YybOrder;
import com.jeeplus.modules.order.service.YybOrderService;

/**
 * 订单Controller
 * @author lwb
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/order/yybOrder")
public class YybOrderController extends BaseController {

	@Autowired
	private YybOrderService yybOrderService;
	
	@ModelAttribute
	public YybOrder get(@RequestParam(required=false) String id) {
		YybOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybOrderService.get(id);
		}
		if (entity == null){
			entity = new YybOrder();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("order:yybOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybOrder yybOrder, Model model) {
		model.addAttribute("yybOrder", yybOrder);
		return "modules/order/yybOrderList";
	}
	
		/**
	 * 订单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybOrder yybOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybOrder> page = yybOrderService.findPage(new Page<YybOrder>(request, response), yybOrder); 
		return getBootstrapData(page);
	}


	@ResponseBody
	@RequestMapping(value = "detail")
	public YybOrder detail(String id) {
		return yybOrderService.get(id);
	}
	

}