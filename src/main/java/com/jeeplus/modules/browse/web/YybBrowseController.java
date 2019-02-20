/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.browse.web;

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
import com.jeeplus.modules.browse.entity.YybBrowse;
import com.jeeplus.modules.browse.service.YybBrowseService;

/**
 * 个人浏览Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/browse/yybBrowse")
public class YybBrowseController extends BaseController {

	@Autowired
	private YybBrowseService yybBrowseService;
	
	@ModelAttribute
	public YybBrowse get(@RequestParam(required=false) String id) {
		YybBrowse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybBrowseService.get(id);
		}
		if (entity == null){
			entity = new YybBrowse();
		}
		return entity;
	}
	
	/**
	 * 个人浏览列表页面
	 */
	@RequiresPermissions("browse:yybBrowse:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybBrowse yybBrowse, Model model) {
		model.addAttribute("yybBrowse", yybBrowse);
		return "modules/browse/yybBrowseList";
	}
	
		/**
	 * 个人浏览列表数据
	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybBrowse yybBrowse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybBrowse> page = yybBrowseService.findPage(new Page<YybBrowse>(request, response), yybBrowse); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑个人浏览表单页面
	 */
	@RequiresPermissions(value={"browse:yybBrowse:view","browse:yybBrowse:add","browse:yybBrowse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybBrowse yybBrowse, Model model) {
		model.addAttribute("yybBrowse", yybBrowse);
		return "modules/browse/yybBrowseForm";
	}

	/**
	 * 保存个人浏览
	 */
	@ResponseBody
	@RequiresPermissions(value={"browse:yybBrowse:add","browse:yybBrowse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybBrowse yybBrowse, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybBrowse);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybBrowseService.save(yybBrowse);//保存
		j.setSuccess(true);
		j.setMsg("保存个人浏览成功");
		return j;
	}
	
	/**
	 * 删除个人浏览
	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybBrowse yybBrowse) {
		AjaxJson j = new AjaxJson();
		yybBrowseService.delete(yybBrowse);
		j.setMsg("删除个人浏览成功");
		return j;
	}
	
	/**
	 * 批量删除个人浏览
	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybBrowseService.delete(yybBrowseService.get(id));
		}
		j.setMsg("删除个人浏览成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybBrowse yybBrowse, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "个人浏览"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybBrowse> page = yybBrowseService.findPage(new Page<YybBrowse>(request, response, -1), yybBrowse);
    		new ExportExcel("个人浏览", YybBrowse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出个人浏览记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybBrowse> list = ei.getDataList(YybBrowse.class);
			for (YybBrowse yybBrowse : list){
				try{
					yybBrowseService.save(yybBrowse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条个人浏览记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条个人浏览记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入个人浏览失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入个人浏览数据模板
	 */
	@ResponseBody
	@RequiresPermissions("browse:yybBrowse:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "个人浏览数据导入模板.xlsx";
    		List<YybBrowse> list = Lists.newArrayList(); 
    		new ExportExcel("个人浏览数据", YybBrowse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}