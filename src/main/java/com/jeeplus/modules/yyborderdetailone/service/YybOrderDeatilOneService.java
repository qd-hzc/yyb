/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yyborderdetailone.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.yyborderdetailone.entity.YybOrderDeatilOne;
import com.jeeplus.modules.yyborderdetailone.mapper.YybOrderDeatilOneMapper;

/**
 * 订单详情Service
 * @author lwb
 * @version 2019-03-11
 */
@Service
@Transactional(readOnly = true)
public class YybOrderDeatilOneService extends CrudService<YybOrderDeatilOneMapper, YybOrderDeatilOne> {

	public YybOrderDeatilOne get(String id) {
		return super.get(id);
	}
	
	public List<YybOrderDeatilOne> findList(YybOrderDeatilOne yybOrderDeatilOne) {
		return super.findList(yybOrderDeatilOne);
	}
	
	public Page<YybOrderDeatilOne> findPage(Page<YybOrderDeatilOne> page, YybOrderDeatilOne yybOrderDeatilOne) {
		return super.findPage(page, yybOrderDeatilOne);
	}
	
	@Transactional(readOnly = false)
	public void save(YybOrderDeatilOne yybOrderDeatilOne) {
		super.save(yybOrderDeatilOne);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybOrderDeatilOne yybOrderDeatilOne) {
		super.delete(yybOrderDeatilOne);
	}
	
}