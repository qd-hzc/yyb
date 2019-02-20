/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.web;

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
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.member.service.YybMemberService;

/**
 * 会员Controller
 * @author lwb
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/member/yybMember")
public class YybMemberController extends BaseController {

	@Autowired
	private YybMemberService yybMemberService;
	
	@ModelAttribute
	public YybMember get(@RequestParam(required=false) String id) {
		YybMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybMemberService.get(id);
		}
		if (entity == null){
			entity = new YybMember();
		}
		return entity;
	}
	
	/**
	 * 会员列表页面
	 */
	@RequiresPermissions("member:yybMember:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybMember yybMember, Model model) {
		model.addAttribute("yybMember", yybMember);
		return "modules/member/yybMemberList";
	}
	
		/**
	 * 会员列表数据
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybMember yybMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybMember> page = yybMemberService.findPage(new Page<YybMember>(request, response), yybMember); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑会员表单页面
	 */
	@RequiresPermissions(value={"member:yybMember:view","member:yybMember:add","member:yybMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybMember yybMember, Model model) {
		model.addAttribute("yybMember", yybMember);
		return "modules/member/yybMemberForm";
	}

	/**
	 * 保存会员
	 */
	@ResponseBody
	@RequiresPermissions(value={"member:yybMember:add","member:yybMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybMember yybMember, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybMember);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybMemberService.save(yybMember);//保存
		j.setSuccess(true);
		j.setMsg("保存会员成功");
		return j;
	}
	
	/**
	 * 删除会员
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybMember yybMember) {
		AjaxJson j = new AjaxJson();
		yybMemberService.delete(yybMember);
		j.setMsg("删除会员成功");
		return j;
	}
	
	/**
	 * 批量删除会员
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybMemberService.delete(yybMemberService.get(id));
		}
		j.setMsg("删除会员成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybMember yybMember, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "会员"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybMember> page = yybMemberService.findPage(new Page<YybMember>(request, response, -1), yybMember);
    		new ExportExcel("会员", YybMember.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出会员记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybMember> list = ei.getDataList(YybMember.class);
			for (YybMember yybMember : list){
				try{
					yybMemberService.save(yybMember);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条会员记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条会员记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入会员失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入会员数据模板
	 */
	@ResponseBody
	@RequiresPermissions("member:yybMember:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "会员数据导入模板.xlsx";
    		List<YybMember> list = Lists.newArrayList(); 
    		new ExportExcel("会员数据", YybMember.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}