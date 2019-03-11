/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.service;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.order.entity.OrderApi;
import com.jeeplus.modules.api.order.entity.YybOrderVo;
import com.jeeplus.modules.api.order.mapper.YybOrderApiMapper;
import com.jeeplus.modules.api.order.mapper.YybOrderDeatilApiMapper;
import com.jeeplus.modules.api.right.entity.YybRightDto;
import com.jeeplus.modules.api.right.mapper.YybRightApiMapper;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.mapper.YybShopcartApiMapper;
import com.jeeplus.modules.api.usage.entity.YybUsageDto;
import com.jeeplus.modules.api.usage.mapper.YybUsageApiMapper;
import com.jeeplus.modules.api.order.entity.OrderDeatilApi;
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.usage.entity.YybUsage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 订单Service
 * @author lwb
 * @version 2019-02-20
 */
@Service
@Transactional(readOnly = true)
public class YybOrderApiService extends CrudService<YybOrderApiMapper, OrderApi> {

	@Autowired
	private YybOrderDeatilApiMapper yybOrderDeatilMapper;
	@Autowired
	private YybShopcartApiMapper yybShopcartApiMapper;
	@Autowired
	private YybRightApiMapper yybRightApiMapper;
	@Autowired
	private YybUsageApiMapper yybUsageApiMapper;

	
	public OrderApi get(String id) {
		OrderApi orderApi = super.get(id);
		orderApi.setDetailList(yybOrderDeatilMapper.findList(new OrderDeatilApi(orderApi)));
		return orderApi;
	}
	
	public List<OrderApi> findList(OrderApi orderApi) {
		return super.findList(orderApi);
	}
	
	public Page<OrderApi> findPage(Page<OrderApi> page, OrderApi orderApi) {
		return super.findPage(page, orderApi);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderApi orderApi) {
		super.save(orderApi);
		for (OrderDeatilApi orderDeatilApi : orderApi.getDetailList()){
			if (orderDeatilApi.getId() == null){
				continue;
			}
			if (OrderDeatilApi.DEL_FLAG_NORMAL.equals(orderDeatilApi.getDelFlag())){
				if (StringUtils.isBlank(orderDeatilApi.getId())){
					orderDeatilApi.setOrderApi(orderApi);
					orderDeatilApi.preInsert();
					yybOrderDeatilMapper.insert(orderDeatilApi);
				}else{
					orderDeatilApi.preUpdate();
					yybOrderDeatilMapper.update(orderDeatilApi);
				}
			}else{
				yybOrderDeatilMapper.delete(orderDeatilApi);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderApi orderApi) {
		super.delete(orderApi);
		yybOrderDeatilMapper.delete(new OrderDeatilApi(orderApi));
	}


	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String toOrder(YybOrderVo yybOrderVo, List<YybShopcart> shopcartList, String memberId) {

		OrderApi orderApi = new OrderApi();
		BeanUtils.copyProperties(yybOrderVo, orderApi);

		//订单id
		String orderId = UUID.randomUUID().toString().replace("-","");
		orderApi.setId(orderId);
		//支付状态:未支付
		orderApi.setStatus(1);
		String orderNo = "yyb" + System.currentTimeMillis() + RandomStringUtils.random(6, false, true);
		orderApi.setOrderNo(orderNo);
		orderApi.setOrderTime(new Date());
		orderApi.setOrderAmount(yybOrderVo.getOrderAmount().doubleValue());
		orderApi.setMemberId(memberId);


		List<OrderDeatilApi> orderDeatilApis = new ArrayList<>();
		OrderDeatilApi orderDeatilApi;
		for (YybShopcart shopcart : shopcartList) {

			List<String> rightIds = Arrays.asList(shopcart.getRightSelect().split(","));
			List<String> usageIds = Arrays.asList(shopcart.getUsageSelect().split(","));
			List<YybRight> rightList = yybRightApiMapper.getListByIds(rightIds);
			List<YybUsage> usageList = yybUsageApiMapper.getListByIds(usageIds);


			//赋值属性购物车到订单明细
			orderDeatilApi = new OrderDeatilApi();
			BeanUtils.copyProperties(shopcart, orderDeatilApi);

			//保存所选中的权利跟用途
			String body = this.getRightUsageBody(rightList, usageList);

			orderDeatilApi.setOrderApi(orderApi);
			orderDeatilApi.setYybShopcart(shopcart);
			orderDeatilApi.setRightAndUsageBody(body);
			orderDeatilApi.setOrderNo(orderNo);
			orderDeatilApis.add(orderDeatilApi);
			//插入订单明细
			yybOrderDeatilMapper.insert(orderDeatilApi);
		}
		//插入订单
		mapper.insert(orderApi);

		Map<String, Object> shopcartParam;

		for (YybShopcart shopcart : shopcartList) {
			shopcartParam = new HashMap<>();
			shopcartParam.put("orderId", orderId);
			shopcartParam.put("shopcartId", shopcart.getId());
			yybShopcartApiMapper.updateOrderId(shopcartParam);
		}

		return orderNo;
	}


	private String getRightUsageBody(List<YybRight> yybRights, List<YybUsage> yybUsages){
		List<YybRightDto> yybRightDtoList = new ArrayList<>();
		List<YybUsageDto> yybUsageDtoList = new ArrayList<>();

		YybRightDto yybRightDto;
		YybUsageDto yybUsageDto;
		for (YybRight yybRight : yybRights) {
			yybRightDto = new YybRightDto();
			BeanUtils.copyProperties(yybRight, yybRightDto);
			yybRightDtoList.add(yybRightDto);
		}
		for (YybUsage yybUsage : yybUsages) {
			yybUsageDto = new YybUsageDto();
			BeanUtils.copyProperties(yybUsage, yybUsageDto);
			yybUsageDtoList.add(yybUsageDto);
		}

		return JSON.toJSONString(yybRightDtoList) + JSON.toJSONString(yybUsageDtoList);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateStatus(Map<String, Object> param) {
		mapper.updateStatus(param);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelOvertimeOrder() {
		mapper.cancelOvertimeOrder();
	}

	public List<OrderApi> list(Map<String, Object> param) {
		return mapper.list(param);
	}
}