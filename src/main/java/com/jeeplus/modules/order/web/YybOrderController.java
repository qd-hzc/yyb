/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.web;

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
import com.jeeplus.modules.order.entity.YybOrder;
import com.jeeplus.modules.order.service.YybOrderService;

/**
 * 订单Controller
 * @author lwb
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/order/yybOrder")
public class YybOrderController extends BaseController {

	@Autowired
	private YybOrderService yybOrderService;
	
	@ModelAttribute
	public YybOrder get(@RequestParam(required=false) String id) {
		YybOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yybOrderService.get(id);
		}
		if (entity == null){
			entity = new YybOrder();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("order:yybOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(YybOrder yybOrder, Model model) {
		model.addAttribute("yybOrder", yybOrder);
		return "modules/order/yybOrderList";
	}
	
		/**
	 * 订单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(YybOrder yybOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YybOrder> page = yybOrderService.findPage(new Page<YybOrder>(request, response), yybOrder); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单表单页面
	 */
	@RequiresPermissions(value={"order:yybOrder:view","order:yybOrder:add","order:yybOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YybOrder yybOrder, Model model) {
		model.addAttribute("yybOrder", yybOrder);
		return "modules/order/yybOrderForm";
	}

	/**
	 * 保存订单
	 */
	@ResponseBody
	@RequiresPermissions(value={"order:yybOrder:add","order:yybOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(YybOrder yybOrder, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(yybOrder);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		yybOrderService.save(yybOrder);//保存
		j.setSuccess(true);
		j.setMsg("保存订单成功");
		return j;
	}
	
	/**
	 * 删除订单
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(YybOrder yybOrder) {
		AjaxJson j = new AjaxJson();
		yybOrderService.delete(yybOrder);
		j.setMsg("删除订单成功");
		return j;
	}
	
	/**
	 * 批量删除订单
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yybOrderService.delete(yybOrderService.get(id));
		}
		j.setMsg("删除订单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(YybOrder yybOrder, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YybOrder> page = yybOrderService.findPage(new Page<YybOrder>(request, response, -1), yybOrder);
    		new ExportExcel("订单", YybOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public YybOrder detail(String id) {
		return yybOrderService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YybOrder> list = ei.getDataList(YybOrder.class);
			for (YybOrder yybOrder : list){
				try{
					yybOrderService.save(yybOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条订单记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入订单失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入订单数据模板
	 */
	@ResponseBody
	@RequiresPermissions("order:yybOrder:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单数据导入模板.xlsx";
    		List<YybOrder> list = Lists.newArrayList(); 
    		new ExportExcel("订单数据", YybOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	

}