/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.playhistory.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.api.playhistory.service.YybPlayHistoryApiService;
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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.jeeplus.modules.playhistory.entity.YybPlayHistory;
import com.jeeplus.modules.playhistory.service.YybPlayHistoryService;

/**
 * 播放历史Controller
 * @author lwb
 * @version 2019-03-06
 */
@Controller
@RequestMapping(value = "/api/playhistory")
public class YybPlayHistoryApiController extends BaseController {

	@Autowired
	private YybPlayHistoryApiService yybPlayHistoryService;
	

	/**
	 * 播放历史列表页面
	 */
	@RequiresPermissions("playhistory:yybPlayHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybPlayHistory yybPlayHistory, Model model) {
		model.addAttribute("yybPlayHistory", yybPlayHistory);
		return "modules/playhistory/yybPlayHistoryList";
	}
	


	/**
	 * 保存播放历史
	 */
	@ResponseBody
	@RequiresPermissions(value={"playhistory:yybPlayHistory:add","playhistory:yybPlayHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybPlayHistory yybPlayHistory, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybPlayHistory);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybPlayHistoryService.save(yybPlayHistory);//保存
		j.setSuccess(true);
		j.setMsg("保存播放历史成功");
		return j;
	}
	


}