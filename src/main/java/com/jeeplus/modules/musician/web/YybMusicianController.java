/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.musician.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.musician.service.YybMusicianService;

/**
 * 音乐人Controller
 * @author lwb
 * @version 2019-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/musician/yybMusician")
public class YybMusicianController extends BaseController {

	@Autowired
	private YybMusicianService yybMusicianService;
	
	@ModelAttribute
	public YybMusician get(@RequestParam(required=false) String id) {
		YybMusician entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybMusicianService.get(id);
		}
		if (entity == null){
			entity = new YybMusician();
		}
		return entity;
	}
	
	/**
	 * 音乐人列表页面
	 */
	@RequiresPermissions("musician:yybMusician:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybMusician yybMusician, Model model) {
		model.addAttribute("yybMusician", yybMusician);
		return "modules/musician/yybMusicianList";
	}
	
		/**
	 * 音乐人列表数据
	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybMusician yybMusician, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		System.out.println(Global.getConfig("admin.loginName"));
		if (!Global.getConfig("admin.loginName").equals(user.getLoginName())) {
			yybMusician.setCompanyId(user.getOffice().getId());
		}
		Page<YybMusician> page = yybMusicianService.findPage(new Page<YybMusician>(request, response), yybMusician);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑音乐人表单页面
	 */
	@RequiresPermissions(value={"musician:yybMusician:view","musician:yybMusician:add","musician:yybMusician:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybMusician yybMusician, Model model) {
		model.addAttribute("yybMusician", yybMusician);
		return "modules/musician/yybMusicianForm";
	}

	/**
	 * 保存音乐人
	 */
	@ResponseBody
	@RequiresPermissions(value={"musician:yybMusician:add","musician:yybMusician:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybMusician yybMusician, Model model) throws Exception{
		User user = UserUtils.getUser();
		if (!Global.getConfig("admin.loginName").equals(user.getLoginName())) {
			yybMusician.setCompanyId(user.getOffice().getId());
		}
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybMusician);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybMusicianService.save(yybMusician);//保存
		j.setSuccess(true);
		j.setMsg("保存音乐人成功");
		return j;
	}
	
	/**
	 * 删除音乐人
	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybMusician yybMusician) {
		AjaxJson j = new AjaxJson();
		yybMusicianService.delete(yybMusician);
		j.setMsg("删除音乐人成功");
		return j;
	}
	
	/**
	 * 批量删除音乐人
	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybMusicianService.delete(yybMusicianService.get(id));
		}
		j.setMsg("删除音乐人成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybMusician yybMusician, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "音乐人"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybMusician> page = yybMusicianService.findPage(new Page<YybMusician>(request, response, -1), yybMusician);
    		new ExportExcel("音乐人", YybMusician.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出音乐人记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public YybMusician detail(String id) {
		return yybMusicianService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybMusician> list = ei.getDataList(YybMusician.class);
			for (YybMusician yybMusician : list){
				try{
					yybMusicianService.save(yybMusician);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条音乐人记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条音乐人记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入音乐人失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入音乐人数据模板
	 */
	@ResponseBody
	@RequiresPermissions("musician:yybMusician:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "音乐人数据导入模板.xlsx";
    		List<YybMusician> list = Lists.newArrayList(); 
    		new ExportExcel("音乐人数据", YybMusician.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	

}