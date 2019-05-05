/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yybtext.web;

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
import com.jeeplus.modules.yybtext.entity.YybText;
import com.jeeplus.modules.yybtext.service.YybTextService;

/**
 * 文案配置Controller
 * @author wenbin
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/yybtext/yybText")
public class YybTextController extends BaseController {

	@Autowired
	private YybTextService yybTextService;
	
	@ModelAttribute
	public YybText get(@RequestParam(required=false) String id) {
		YybText entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybTextService.get(id);
		}
		if (entity == null){
			entity = new YybText();
		}
		return entity;
	}
	
	/**
	 * 文案配置列表页面
	 */
	@RequiresPermissions("yybtext:yybText:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybText yybText, Model model) {
		model.addAttribute("yybText", yybText);
		return "modules/yybtext/yybTextList";
	}
	
		/**
	 * 文案配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybText yybText, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybText> page = yybTextService.findPage(new Page<YybText>(request, response), yybText); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑文案配置表单页面
	 */
	@RequiresPermissions(value={"yybtext:yybText:view","yybtext:yybText:add","yybtext:yybText:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybText yybText, Model model) {
		model.addAttribute("yybText", yybText);
		return "modules/yybtext/yybTextForm";
	}

	/**
	 * 保存文案配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"yybtext:yybText:add","yybtext:yybText:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybText yybText, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybText);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybTextService.save(yybText);//保存
		j.setSuccess(true);
		j.setMsg("保存文案配置成功");
		return j;
	}
	
	/**
	 * 删除文案配置
	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybText yybText) {
		AjaxJson j = new AjaxJson();
		yybTextService.delete(yybText);
		j.setMsg("删除文案配置成功");
		return j;
	}
	
	/**
	 * 批量删除文案配置
	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybTextService.delete(yybTextService.get(id));
		}
		j.setMsg("删除文案配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybText yybText, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "文案配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybText> page = yybTextService.findPage(new Page<YybText>(request, response, -1), yybText);
    		new ExportExcel("文案配置", YybText.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出文案配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybText> list = ei.getDataList(YybText.class);
			for (YybText yybText : list){
				try{
					yybTextService.save(yybText);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条文案配置记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条文案配置记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入文案配置失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入文案配置数据模板
	 */
	@ResponseBody
	@RequiresPermissions("yybtext:yybText:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "文案配置数据导入模板.xlsx";
    		List<YybText> list = Lists.newArrayList(); 
    		new ExportExcel("文案配置数据", YybText.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}