/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.usage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.usage.entity.YybUsage;
import com.jeeplus.modules.usage.mapper.YybUsageMapper;

/**
 * 用途Service
 * @author lwb
 * @version 2019-02-17
 */
@Service
@Transactional(readOnly = true)
public class YybUsageService extends CrudService<YybUsageMapper, YybUsage> {

	public YybUsage get(String id) {
		return super.get(id);
	}
	
	public List<YybUsage> findList(YybUsage yybUsage) {
		return super.findList(yybUsage);
	}
	
	public Page<YybUsage> findPage(Page<YybUsage> page, YybUsage yybUsage) {
		return super.findPage(page, yybUsage);
	}
	
	@Transactional(readOnly = false)
	public void save(YybUsage yybUsage) {
		super.save(yybUsage);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybUsage yybUsage) {
		super.delete(yybUsage);
	}
	
}