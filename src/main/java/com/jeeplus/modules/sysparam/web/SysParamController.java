/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysparam.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.core.web.Result;
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
import com.jeeplus.modules.sysparam.entity.SysParam;
import com.jeeplus.modules.sysparam.service.SysParamService;

/**
 * 参数配置Controller
 * @author lwb
 * @version 2019-02-26
 */
@Controller
@RequestMapping(value = "${adminPath}/sysparam/sysParam")
public class SysParamController extends BaseController {

	@Autowired
	private SysParamService sysParamService;
	
	@ModelAttribute
	public SysParam get(@RequestParam(required=false) String id) {
		SysParam entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysParamService.get(id);
		}
		if (entity == null){
			entity = new SysParam();
		}
		return entity;
	}

	public Boolean judgeMusicOrderAll(){
		return sysParamService.judgeMusicOrderAll();
	}
	
	/**
	 * 参数配置列表页面
	 */
	@RequiresPermissions("sysparam:sysParam:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysParam sysParam, Model model) {
		model.addAttribute("sysParam", sysParam);
		return "modules/sysparam/sysParamList";
	}
	
		/**
	 * 参数配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SysParam sysParam, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysParam> page = sysParamService.findPage(new Page<SysParam>(request, response), sysParam); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑参数配置表单页面
	 */
	@RequiresPermissions(value={"sysparam:sysParam:view","sysparam:sysParam:add","sysparam:sysParam:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, SysParam sysParam, Model model) {
		model.addAttribute("sysParam", sysParam);
		model.addAttribute("mode", mode);
		return "modules/sysparam/sysParamForm";
	}

	/**
	 * 保存参数配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"sysparam:sysParam:add","sysparam:sysParam:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SysParam sysParam, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(sysParam);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		sysParamService.save(sysParam);//保存
		j.setSuccess(true);
		j.setMsg("保存参数配置成功");
		return j;
	}
	
	/**
	 * 删除参数配置
	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysParam sysParam) {
		AjaxJson j = new AjaxJson();
		sysParamService.delete(sysParam);
		j.setMsg("删除参数配置成功");
		return j;
	}
	
	/**
	 * 批量删除参数配置
	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysParamService.delete(sysParamService.get(id));
		}
		j.setMsg("删除参数配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SysParam sysParam, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "参数配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysParam> page = sysParamService.findPage(new Page<SysParam>(request, response, -1), sysParam);
    		new ExportExcel("参数配置", SysParam.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出参数配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysParam> list = ei.getDataList(SysParam.class);
			for (SysParam sysParam : list){
				try{
					sysParamService.save(sysParam);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条参数配置记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条参数配置记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入参数配置失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入参数配置数据模板
	 */
	@ResponseBody
	@RequiresPermissions("sysparam:sysParam:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "参数配置数据导入模板.xlsx";
    		List<SysParam> list = Lists.newArrayList(); 
    		new ExportExcel("参数配置数据", SysParam.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }


}