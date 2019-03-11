/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.service;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.order.entity.YybOrderVo;
import com.jeeplus.modules.api.order.mapper.YybOrderApiMapper;
import com.jeeplus.modules.api.order.mapper.YybOrderDeatilApiMapper;
import com.jeeplus.modules.api.right.entity.YybRightDto;
import com.jeeplus.modules.api.right.mapper.YybRightApiMapper;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.mapper.YybShopcartApiMapper;
import com.jeeplus.modules.api.usage.entity.YybUsageDto;
import com.jeeplus.modules.api.usage.mapper.YybUsageApiMapper;
import com.jeeplus.modules.api.order.entity.YybOrder;
import com.jeeplus.modules.api.order.entity.YybOrderDeatil;
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
public class YybOrderApiService extends CrudService<YybOrderApiMapper, YybOrder> {

	@Autowired
	private YybOrderDeatilApiMapper yybOrderDeatilMapper;
	@Autowired
	private YybShopcartApiMapper yybShopcartApiMapper;
	@Autowired
	private YybRightApiMapper yybRightApiMapper;
	@Autowired
	private YybUsageApiMapper yybUsageApiMapper;

	
	public YybOrder get(String id) {
		YybOrder yybOrder = super.get(id);
		yybOrder.setYybOrderDeatilList(yybOrderDeatilMapper.findList(new YybOrderDeatil(yybOrder)));
		return yybOrder;
	}
	
	public List<YybOrder> findList(YybOrder yybOrder) {
		return super.findList(yybOrder);
	}
	
	public Page<YybOrder> findPage(Page<YybOrder> page, YybOrder yybOrder) {
		return super.findPage(page, yybOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(YybOrder yybOrder) {
		super.save(yybOrder);
		for (YybOrderDeatil yybOrderDeatil : yybOrder.getYybOrderDeatilList()){
			if (yybOrderDeatil.getId() == null){
				continue;
			}
			if (YybOrderDeatil.DEL_FLAG_NORMAL.equals(yybOrderDeatil.getDelFlag())){
				if (StringUtils.isBlank(yybOrderDeatil.getId())){
					yybOrderDeatil.setYybOrder(yybOrder);
					yybOrderDeatil.preInsert();
					yybOrderDeatilMapper.insert(yybOrderDeatil);
				}else{
					yybOrderDeatil.preUpdate();
					yybOrderDeatilMapper.update(yybOrderDeatil);
				}
			}else{
				yybOrderDeatilMapper.delete(yybOrderDeatil);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(YybOrder yybOrder) {
		super.delete(yybOrder);
		yybOrderDeatilMapper.delete(new YybOrderDeatil(yybOrder));
	}


	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String toOrder(YybOrderVo yybOrderVo, List<YybShopcart> shopcartList, String memberId) {

		YybOrder yybOrder = new YybOrder();
		BeanUtils.copyProperties(yybOrderVo, yybOrder);

		//订单id
		String orderId = UUID.randomUUID().toString().replace("-","");
		yybOrder.setId(orderId);
		//支付状态:未支付
		yybOrder.setStatus(1);
		String orderNo = "yyb" + System.currentTimeMillis() + RandomStringUtils.random(6, false, true);
		yybOrder.setOrderNo(orderNo);
		yybOrder.setOrderTime(new Date());
		yybOrder.setOrderAmount(yybOrderVo.getOrderAmount().doubleValue());
		yybOrder.setMemberId(memberId);


		List<YybOrderDeatil> yybOrderDeatils = new ArrayList<>();
		YybOrderDeatil yybOrderDeatil;
		for (YybShopcart shopcart : shopcartList) {

			List<String> rightIds = Arrays.asList(shopcart.getRightSelect().split(","));
			List<String> usageIds = Arrays.asList(shopcart.getUsageSelect().split(","));
			List<YybRight> rightList = yybRightApiMapper.getListByIds(rightIds);
			List<YybUsage> usageList = yybUsageApiMapper.getListByIds(usageIds);


			//赋值属性购物车到订单明细
			yybOrderDeatil = new YybOrderDeatil();
			BeanUtils.copyProperties(shopcart, yybOrderDeatil);

			//保存所选中的权利跟用途
			String body = this.getRightUsageBody(rightList, usageList);

			yybOrderDeatil.setYybOrder(yybOrder);
			yybOrderDeatil.setYybShopcart(shopcart);
			yybOrderDeatil.setRightAndUsageBody(body);
			yybOrderDeatils.add(yybOrderDeatil);
			//插入订单明细
			yybOrderDeatilMapper.insert(yybOrderDeatil);
		}
		//插入订单
		mapper.insert(yybOrder);

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

	public List<YybOrder> list(Map<String, Object> param) {
		return mapper.list(param);
	}
}