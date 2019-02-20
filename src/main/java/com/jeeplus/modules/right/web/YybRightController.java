/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.right.web;

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
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.right.service.YybRightService;

/**
 * 权利Controller
 * @author lwb
 * @version 2019-02-17
 */
@Controller
@RequestMapping(value = "${adminPath}/right/yybRight")
public class YybRightController extends BaseController {

	@Autowired
	private YybRightService yybRightService;
	
	@ModelAttribute
	public YybRight get(@RequestParam(required=false) String id) {
		YybRight entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybRightService.get(id);
		}
		if (entity == null){
			entity = new YybRight();
		}
		return entity;
	}
	
	/**
	 * 权利列表页面
	 */
	@RequiresPermissions("right:yybRight:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybRight yybRight, Model model) {
		model.addAttribute("yybRight", yybRight);
		return "modules/right/yybRightList";
	}
	
		/**
	 * 权利列表数据
	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybRight yybRight, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybRight> page = yybRightService.findPage(new Page<YybRight>(request, response), yybRight); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑权利表单页面
	 */
	@RequiresPermissions(value={"right:yybRight:view","right:yybRight:add","right:yybRight:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybRight yybRight, Model model) {
		model.addAttribute("yybRight", yybRight);
		return "modules/right/yybRightForm";
	}

	/**
	 * 保存权利
	 */
	@ResponseBody
	@RequiresPermissions(value={"right:yybRight:add","right:yybRight:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybRight yybRight, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybRight);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybRightService.save(yybRight);//保存
		j.setSuccess(true);
		j.setMsg("保存权利成功");
		return j;
	}
	
	/**
	 * 删除权利
	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybRight yybRight) {
		AjaxJson j = new AjaxJson();
		yybRightService.delete(yybRight);
		j.setMsg("删除权利成功");
		return j;
	}
	
	/**
	 * 批量删除权利
	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybRightService.delete(yybRightService.get(id));
		}
		j.setMsg("删除权利成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybRight yybRight, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "权利"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybRight> page = yybRightService.findPage(new Page<YybRight>(request, response, -1), yybRight);
    		new ExportExcel("权利", YybRight.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出权利记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybRight> list = ei.getDataList(YybRight.class);
			for (YybRight yybRight : list){
				try{
					yybRightService.save(yybRight);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条权利记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条权利记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入权利失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入权利数据模板
	 */
	@ResponseBody
	@RequiresPermissions("right:yybRight:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "权利数据导入模板.xlsx";
    		List<YybRight> list = Lists.newArrayList(); 
    		new ExportExcel("权利数据", YybRight.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}