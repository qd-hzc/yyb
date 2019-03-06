/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.playhistory.web;

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
@RequestMapping(value = "${adminPath}/playhistory/yybPlayHistory")
public class YybPlayHistoryController extends BaseController {

	@Autowired
	private YybPlayHistoryService yybPlayHistoryService;
	
	@ModelAttribute
	public YybPlayHistory get(@RequestParam(required=false) String id) {
		YybPlayHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybPlayHistoryService.get(id);
		}
		if (entity == null){
			entity = new YybPlayHistory();
		}
		return entity;
	}
	
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
	 * 播放历史列表数据
	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybPlayHistory yybPlayHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybPlayHistory> page = yybPlayHistoryService.findPage(new Page<YybPlayHistory>(request, response), yybPlayHistory); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑播放历史表单页面
	 */
	@RequiresPermissions(value={"playhistory:yybPlayHistory:view","playhistory:yybPlayHistory:add","playhistory:yybPlayHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, YybPlayHistory yybPlayHistory, Model model) {
		model.addAttribute("yybPlayHistory", yybPlayHistory);
		model.addAttribute("mode", mode);
		return "modules/playhistory/yybPlayHistoryForm";
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
	
	/**
	 * 删除播放历史
	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybPlayHistory yybPlayHistory) {
		AjaxJson j = new AjaxJson();
		yybPlayHistoryService.delete(yybPlayHistory);
		j.setMsg("删除播放历史成功");
		return j;
	}
	
	/**
	 * 批量删除播放历史
	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybPlayHistoryService.delete(yybPlayHistoryService.get(id));
		}
		j.setMsg("删除播放历史成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybPlayHistory yybPlayHistory, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "播放历史"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybPlayHistory> page = yybPlayHistoryService.findPage(new Page<YybPlayHistory>(request, response, -1), yybPlayHistory);
    		new ExportExcel("播放历史", YybPlayHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出播放历史记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybPlayHistory> list = ei.getDataList(YybPlayHistory.class);
			for (YybPlayHistory yybPlayHistory : list){
				try{
					yybPlayHistoryService.save(yybPlayHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条播放历史记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条播放历史记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入播放历史失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入播放历史数据模板
	 */
	@ResponseBody
	@RequiresPermissions("playhistory:yybPlayHistory:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "播放历史数据导入模板.xlsx";
    		List<YybPlayHistory> list = Lists.newArrayList(); 
    		new ExportExcel("播放历史数据", YybPlayHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}