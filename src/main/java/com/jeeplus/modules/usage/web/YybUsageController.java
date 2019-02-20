/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.usage.web;

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
import com.jeeplus.modules.usage.entity.YybUsage;
import com.jeeplus.modules.usage.service.YybUsageService;

/**
 * 用途Controller
 * @author lwb
 * @version 2019-02-17
 */
@Controller
@RequestMapping(value = "${adminPath}/usage/yybUsage")
public class YybUsageController extends BaseController {

	@Autowired
	private YybUsageService yybUsageService;
	
	@ModelAttribute
	public YybUsage get(@RequestParam(required=false) String id) {
		YybUsage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybUsageService.get(id);
		}
		if (entity == null){
			entity = new YybUsage();
		}
		return entity;
	}
	
	/**
	 * 用途列表页面
	 */
	@RequiresPermissions("usage:yybUsage:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybUsage yybUsage, Model model) {
		model.addAttribute("yybUsage", yybUsage);
		return "modules/usage/yybUsageList";
	}
	
		/**
	 * 用途列表数据
	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybUsage yybUsage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybUsage> page = yybUsageService.findPage(new Page<YybUsage>(request, response), yybUsage); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用途表单页面
	 */
	@RequiresPermissions(value={"usage:yybUsage:view","usage:yybUsage:add","usage:yybUsage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybUsage yybUsage, Model model) {
		model.addAttribute("yybUsage", yybUsage);
		return "modules/usage/yybUsageForm";
	}

	/**
	 * 保存用途
	 */
	@ResponseBody
	@RequiresPermissions(value={"usage:yybUsage:add","usage:yybUsage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybUsage yybUsage, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybUsage);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybUsageService.save(yybUsage);//保存
		j.setSuccess(true);
		j.setMsg("保存用途成功");
		return j;
	}
	
	/**
	 * 删除用途
	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybUsage yybUsage) {
		AjaxJson j = new AjaxJson();
		yybUsageService.delete(yybUsage);
		j.setMsg("删除用途成功");
		return j;
	}
	
	/**
	 * 批量删除用途
	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybUsageService.delete(yybUsageService.get(id));
		}
		j.setMsg("删除用途成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybUsage yybUsage, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用途"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybUsage> page = yybUsageService.findPage(new Page<YybUsage>(request, response, -1), yybUsage);
    		new ExportExcel("用途", YybUsage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用途记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybUsage> list = ei.getDataList(YybUsage.class);
			for (YybUsage yybUsage : list){
				try{
					yybUsageService.save(yybUsage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用途记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条用途记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入用途失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入用途数据模板
	 */
	@ResponseBody
	@RequiresPermissions("usage:yybUsage:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用途数据导入模板.xlsx";
    		List<YybUsage> list = Lists.newArrayList(); 
    		new ExportExcel("用途数据", YybUsage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}