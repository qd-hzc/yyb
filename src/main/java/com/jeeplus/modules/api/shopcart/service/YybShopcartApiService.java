/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.shopcart.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import com.jeeplus.modules.api.shopcart.mapper.YybShopcartApiMapper;

/**
 * 购物车Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybShopcartApiService extends CrudService<YybShopcartApiMapper, YybShopcart> {

	public YybShopcart get(String id) {
		return super.get(id);
	}
	
	public List<YybShopcart> findList(YybShopcart yybShopcart) {
		return super.findList(yybShopcart);
	}
	
	public Page<YybShopcart> findPage(Page<YybShopcart> page, YybShopcart yybShopcart) {
		return super.findPage(page, yybShopcart);
	}
	
	@Transactional(readOnly = false)
	public void save(YybShopcart yybShopcart) {
		super.save(yybShopcart);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybShopcart yybShopcart) {
		super.delete(yybShopcart);
	}

	public List<YybShopcart> shopcartList(Map<String, Object> param) {
		return mapper.shopcartList(param);
	}

    public int getCountByCodition(Map<String, Object> param) {
		return mapper.getCountByCodition(param);
    }

	public List<YybShopcart> getListByIds(List<String> ids) {
		return mapper.getListByIds(ids);
	}

	public Integer shopcartListCount(Map<String, Object> param) {
		return mapper.shopcartListCount(param);

	}
}