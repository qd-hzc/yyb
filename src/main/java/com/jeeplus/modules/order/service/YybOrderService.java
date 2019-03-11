/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.order.entity.YybOrder;
import com.jeeplus.modules.order.mapper.YybOrderMapper;
import com.jeeplus.modules.order.entity.YybOrderDeatil;
import com.jeeplus.modules.order.mapper.YybOrderDeatilMapper;

/**
 * 订单Service
 * @author lwb
 * @version 2019-03-11
 */
@Service
@Transactional(readOnly = true)
public class YybOrderService extends CrudService<YybOrderMapper, YybOrder> {

	@Autowired
	private YybOrderDeatilMapper yybOrderDeatilMapper;
	
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
	
}