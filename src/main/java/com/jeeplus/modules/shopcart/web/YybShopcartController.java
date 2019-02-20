/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shopcart.web;

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
import com.jeeplus.modules.shopcart.entity.YybShopcart;
import com.jeeplus.modules.shopcart.service.YybShopcartService;

/**
 * 购物车Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/shopcart/yybShopcart")
public class YybShopcartController extends BaseController {

	@Autowired
	private YybShopcartService yybShopcartService;
	
	@ModelAttribute
	public YybShopcart get(@RequestParam(required=false) String id) {
		YybShopcart entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybShopcartService.get(id);
		}
		if (entity == null){
			entity = new YybShopcart();
		}
		return entity;
	}
	
	/**
	 * 购物车列表页面
	 */
	@RequiresPermissions("shopcart:yybShopcart:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybShopcart yybShopcart, Model model) {
		model.addAttribute("yybShopcart", yybShopcart);
		return "modules/shopcart/yybShopcartList";
	}
	
		/**
	 * 购物车列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybShopcart yybShopcart, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybShopcart> page = yybShopcartService.findPage(new Page<YybShopcart>(request, response), yybShopcart); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑购物车表单页面
	 */
	@RequiresPermissions(value={"shopcart:yybShopcart:view","shopcart:yybShopcart:add","shopcart:yybShopcart:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybShopcart yybShopcart, Model model) {
		model.addAttribute("yybShopcart", yybShopcart);
		return "modules/shopcart/yybShopcartForm";
	}

	/**
	 * 保存购物车
	 */
	@ResponseBody
	@RequiresPermissions(value={"shopcart:yybShopcart:add","shopcart:yybShopcart:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybShopcart yybShopcart, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybShopcart);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybShopcartService.save(yybShopcart);//保存
		j.setSuccess(true);
		j.setMsg("保存购物车成功");
		return j;
	}
	
	/**
	 * 删除购物车
	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybShopcart yybShopcart) {
		AjaxJson j = new AjaxJson();
		yybShopcartService.delete(yybShopcart);
		j.setMsg("删除购物车成功");
		return j;
	}
	
	/**
	 * 批量删除购物车
	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybShopcartService.delete(yybShopcartService.get(id));
		}
		j.setMsg("删除购物车成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybShopcart yybShopcart, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "购物车"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybShopcart> page = yybShopcartService.findPage(new Page<YybShopcart>(request, response, -1), yybShopcart);
    		new ExportExcel("购物车", YybShopcart.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出购物车记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybShopcart> list = ei.getDataList(YybShopcart.class);
			for (YybShopcart yybShopcart : list){
				try{
					yybShopcartService.save(yybShopcart);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条购物车记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条购物车记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入购物车失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入购物车数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shopcart:yybShopcart:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "购物车数据导入模板.xlsx";
    		List<YybShopcart> list = Lists.newArrayList(); 
    		new ExportExcel("购物车数据", YybShopcart.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}