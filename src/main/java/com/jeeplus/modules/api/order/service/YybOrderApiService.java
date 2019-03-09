/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.service;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.order.entity.YybOrderVo;
import com.jeeplus.modules.api.order.mapper.YybOrderDeatilApiMapper;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.mapper.YybShopcartApiMapper;
import com.jeeplus.modules.order.entity.YybOrder;
import com.jeeplus.modules.order.entity.YybOrderDeatil;
import com.jeeplus.modules.order.mapper.YybOrderDeatilMapper;
import com.jeeplus.modules.order.mapper.YybOrderMapper;
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
public class YybOrderApiService extends CrudService<YybOrderMapper, YybOrder> {

	@Autowired
	private YybOrderDeatilApiMapper yybOrderDeatilMapper;
	@Autowired
	private YybShopcartApiMapper yybShopcartApiMapper;
	
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
	public void toOrder(YybOrderVo yybOrderVo, List<YybShopcart> shopcartList) {

		YybOrder yybOrder = new YybOrder();
		BeanUtils.copyProperties(yybOrderVo, yybOrder);

		//支付状态:未支付
		yybOrder.setStatus(1);
		yybOrder.setOrderNo("yyb" + System.currentTimeMillis() + RandomStringUtils.random(6, false, true));
		yybOrder.setOrderTime(new Date());
		yybOrder.setOrderAmount(yybOrderVo.getOrderAmount().doubleValue());

		List<YybOrderDeatil> yybOrderDeatils = new ArrayList<>();
		YybOrderDeatil yybOrderDeatil;
		for (YybShopcart shopcart : shopcartList) {
			yybOrderDeatil = new YybOrderDeatil();
			BeanUtils.copyProperties(shopcart, yybOrderDeatil);

			yybOrderDeatil.setYybShopcart(shopcart);
			yybOrderDeatils.add(yybOrderDeatil);
		}

		yybOrder.setYybOrderDeatilList(yybOrderDeatils);
		this.save(yybOrder);

		Map<String, Object> shopcartParam;

		for (YybShopcart shopcart : shopcartList) {
			shopcartParam = new HashMap<>();
			shopcartParam.put("orderId", yybOrder.getId());
			shopcartParam.put("shopcartId", shopcart.getId());
			yybShopcartApiMapper.updateOrderId(shopcartParam);
		}

	}
	
}