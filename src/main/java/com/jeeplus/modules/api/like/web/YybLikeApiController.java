/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.like.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.api.like.service.YybLikeApiService;
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
import com.jeeplus.modules.like.entity.YybLike;
import com.jeeplus.modules.like.service.YybLikeService;

/**
 * 个人喜欢Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/like/yybLike")
public class YybLikeApiController extends BaseController {

	@Autowired
	private YybLikeApiService yybLikeService;
	
	@ModelAttribute
	public YybLike get(@RequestParam(required=false) String id) {
		YybLike entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybLikeService.get(id);
		}
		if (entity == null){
			entity = new YybLike();
		}
		return entity;
	}
	
	/**
	 * 个人喜欢列表页面
	 */
	@RequiresPermissions("like:yybLike:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybLike yybLike, Model model) {
		model.addAttribute("yybLike", yybLike);
		return "modules/like/yybLikeList";
	}
	
		/**
	 * 个人喜欢列表数据
	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybLike yybLike, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybLike> page = yybLikeService.findPage(new Page<YybLike>(request, response), yybLike); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑个人喜欢表单页面
	 */
	@RequiresPermissions(value={"like:yybLike:view","like:yybLike:add","like:yybLike:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybLike yybLike, Model model) {
		model.addAttribute("yybLike", yybLike);
		return "modules/like/yybLikeForm";
	}

	/**
	 * 保存个人喜欢
	 */
	@ResponseBody
	@RequiresPermissions(value={"like:yybLike:add","like:yybLike:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybLike yybLike, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybLike);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybLikeService.save(yybLike);//保存
		j.setSuccess(true);
		j.setMsg("保存个人喜欢成功");
		return j;
	}
	
	/**
	 * 删除个人喜欢
	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybLike yybLike) {
		AjaxJson j = new AjaxJson();
		yybLikeService.delete(yybLike);
		j.setMsg("删除个人喜欢成功");
		return j;
	}
	
	/**
	 * 批量删除个人喜欢
	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybLikeService.delete(yybLikeService.get(id));
		}
		j.setMsg("删除个人喜欢成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybLike yybLike, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "个人喜欢"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybLike> page = yybLikeService.findPage(new Page<YybLike>(request, response, -1), yybLike);
    		new ExportExcel("个人喜欢", YybLike.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出个人喜欢记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybLike> list = ei.getDataList(YybLike.class);
			for (YybLike yybLike : list){
				try{
					yybLikeService.save(yybLike);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条个人喜欢记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条个人喜欢记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入个人喜欢失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入个人喜欢数据模板
	 */
	@ResponseBody
	@RequiresPermissions("like:yybLike:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "个人喜欢数据导入模板.xlsx";
    		List<YybLike> list = Lists.newArrayList(); 
    		new ExportExcel("个人喜欢数据", YybLike.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}