/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.music.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import com.jeeplus.modules.tagcatetory.service.YybTagCategoryService;
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
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.music.service.YybMusicService;

/**
 * 音乐Controller
 * @author lwb
 * @version 2019-02-19
 */
@Controller
@RequestMapping(value = "${adminPath}/music/yybMusic")
public class YybMusicController extends BaseController {

	@Autowired
	private YybMusicService yybMusicService;

	@Autowired
	private YybTagCategoryService yybTagCategoryService;

	@ModelAttribute
	public YybMusic get(@RequestParam(required=false) String id) {
		YybMusic entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybMusicService.get(id);
		}
		if (entity == null){
			entity = new YybMusic();
		}
		return entity;
	}
	
	/**
	 * 音乐列表页面
	 */
	@RequiresPermissions("music:yybMusic:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybMusic yybMusic, Model model) {
		model.addAttribute("yybMusic", yybMusic);
		return "modules/music/yybMusicList";
	}

	/**
	 * 音乐列表数据
	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybMusic yybMusic, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybMusic> page = yybMusicService.findPage(new Page<YybMusic>(request, response), yybMusic);
		List<YybMusic> list = page.getList();
		for (YybMusic yybMusic1 : list) {
			YybTagCategory yybTagCategory = yybMusic1.getYybTagCategory();
			if (yybTagCategory != null) {
				String id = yybTagCategory.getId();
				List<String> ids = Arrays.asList(id.split(","));
				String name = "";
				for (String tagId : ids) {
					YybTagCategory yybTagCategoryId = yybTagCategoryService.get(tagId);
					if (yybTagCategoryId != null){
						name = name + yybTagCategoryId.getName()+";";
					}
				}
				yybTagCategory.setName(name);

			}
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑音乐表单页面
	 */
	@RequiresPermissions(value={"music:yybMusic:view","music:yybMusic:add","music:yybMusic:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybMusic yybMusic, Model model) {
		YybTagCategory yybTagCategory = yybMusic.getYybTagCategory();
		if (yybTagCategory != null) {
			String id = yybTagCategory.getId();
			List<String> ids = Arrays.asList(id.split(","));
			String name = "";
			for (String tagId : ids) {
				YybTagCategory yybTagCategoryId = yybTagCategoryService.get(tagId);
				if (yybTagCategoryId != null){
					name = name + yybTagCategoryId.getName()+";";
				}
			}
			yybTagCategory.setName(name);

		}
		model.addAttribute("yybMusic", yybMusic);
		return "modules/music/yybMusicForm";
	}

	/**
	 * 保存音乐
	 */
	@ResponseBody
	@RequiresPermissions(value={"music:yybMusic:add","music:yybMusic:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybMusic yybMusic, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybMusic);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybMusicService.save(yybMusic);//保存
		j.setSuccess(true);
		j.setMsg("保存音乐成功");
		return j;
	}
	
	/**
	 * 删除音乐
	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybMusic yybMusic) {
		AjaxJson j = new AjaxJson();
		yybMusicService.delete(yybMusic);
		j.setMsg("删除音乐成功");
		return j;
	}
	
	/**
	 * 批量删除音乐
	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybMusicService.delete(yybMusicService.get(id));
		}
		j.setMsg("删除音乐成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybMusic yybMusic, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "音乐"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybMusic> page = yybMusicService.findPage(new Page<YybMusic>(request, response, -1), yybMusic);
    		new ExportExcel("音乐", YybMusic.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出音乐记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybMusic> list = ei.getDataList(YybMusic.class);
			for (YybMusic yybMusic : list){
				try{
					yybMusicService.save(yybMusic);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条音乐记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条音乐记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入音乐失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入音乐数据模板
	 */
	@ResponseBody
	@RequiresPermissions("music:yybMusic:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "音乐数据导入模板.xlsx";
    		List<YybMusic> list = Lists.newArrayList(); 
    		new ExportExcel("音乐数据", YybMusic.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}