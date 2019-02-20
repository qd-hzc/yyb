/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tagcatetory.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.core.persistence.Page;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import com.jeeplus.modules.tagcatetory.service.YybTagCategoryService;

/**
 * 标签分类Controller
 * @author l
 * @version 2019-02-18
 */
@Controller
@RequestMapping(value = "${adminPath}/tagcatetory/yybTagCategory")
public class YybTagCategoryController extends BaseController {

	@Autowired
	private YybTagCategoryService yybTagCategoryService;
	
	@ModelAttribute
	public YybTagCategory get(@RequestParam(required=false) String id) {
		YybTagCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybTagCategoryService.get(id);
		}
		if (entity == null){
			entity = new YybTagCategory();
		}
		return entity;
	}
	
	/**
	 * 标签分类列表页面
	 */
	@RequiresPermissions("tagcatetory:yybTagCategory:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybTagCategory yybTagCategory,  HttpServletRequest request, HttpServletResponse response, Model model) {
	
		model.addAttribute("yybTagCategory", yybTagCategory);
		return "modules/tagcatetory/yybTagCategoryList";
	}

	/**
	 * 查看，增加，编辑标签分类表单页面
	 */
	@RequiresPermissions(value={"tagcatetory:yybTagCategory:view","tagcatetory:yybTagCategory:add","tagcatetory:yybTagCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybTagCategory yybTagCategory, Model model) {
		if (yybTagCategory.getParent()!=null && StringUtils.isNotBlank(yybTagCategory.getParent().getId())){
			yybTagCategory.setParent(yybTagCategoryService.get(yybTagCategory.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(yybTagCategory.getId())){
				YybTagCategory yybTagCategoryChild = new YybTagCategory();
				yybTagCategoryChild.setParent(new YybTagCategory(yybTagCategory.getParent().getId()));
				List<YybTagCategory> list = yybTagCategoryService.findList(yybTagCategory); 
				if (list.size() > 0){
					yybTagCategory.setSort(list.get(list.size()-1).getSort());
					if (yybTagCategory.getSort() != null){
						yybTagCategory.setSort(yybTagCategory.getSort() + 30);
					}
				}
			}
		}
		if (yybTagCategory.getSort() == null){
			yybTagCategory.setSort(30);
		}
		model.addAttribute("yybTagCategory", yybTagCategory);
		return "modules/tagcatetory/yybTagCategoryForm";
	}

	/**
	 * 保存标签分类
	 */
	@ResponseBody
	@RequiresPermissions(value={"tagcatetory:yybTagCategory:add","tagcatetory:yybTagCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybTagCategory yybTagCategory, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybTagCategory);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		yybTagCategoryService.save(yybTagCategory);//保存
		j.setSuccess(true);
		j.put("yybTagCategory", yybTagCategory);
		j.setMsg("保存标签分类成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<YybTagCategory> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return yybTagCategoryService.getChildren(parentId);
	}
	
	/**
	 * 删除标签分类
	 */
	@ResponseBody
	@RequiresPermissions("tagcatetory:yybTagCategory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybTagCategory yybTagCategory) {
		AjaxJson j = new AjaxJson();
		yybTagCategoryService.delete(yybTagCategory);
		j.setSuccess(true);
		j.setMsg("删除标签分类成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<YybTagCategory> list = yybTagCategoryService.findList(new YybTagCategory());
		for (int i=0; i<list.size(); i++){
			YybTagCategory e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}



	/**
	 * 音乐人列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybTagCategory yybTagCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybTagCategory> page = yybTagCategoryService.findPage(new Page<YybTagCategory>(request, response), yybTagCategory);
		return getBootstrapData(page);
	}


	/**
	 * 音乐人列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "dataNoParent")
	public Map<String, Object> dataNoParent(YybTagCategory yybTagCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybTagCategory> page = yybTagCategoryService.findPageNoParent(new Page<YybTagCategory>(request, response), yybTagCategory);
		return getBootstrapData(page);
	}


}