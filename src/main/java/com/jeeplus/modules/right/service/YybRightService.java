/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.right.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.right.mapper.YybRightMapper;

/**
 * 权利Service
 * @author lwb
 * @version 2019-02-17
 */
@Service
@Transactional(readOnly = true)
public class YybRightService extends CrudService<YybRightMapper, YybRight> {

	public YybRight get(String id) {
		return super.get(id);
	}
	
	public List<YybRight> findList(YybRight yybRight) {
		return super.findList(yybRight);
	}
	
	public Page<YybRight> findPage(Page<YybRight> page, YybRight yybRight) {
		return super.findPage(page, yybRight);
	}
	
	@Transactional(readOnly = false)
	public void save(YybRight yybRight) {
		super.save(yybRight);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybRight yybRight) {
		super.delete(yybRight);
	}
	
}