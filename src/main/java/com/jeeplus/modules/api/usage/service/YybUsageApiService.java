/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.usage.service;

import java.util.List;

import com.jeeplus.modules.api.usage.mapper.YybUsageApiMapper;
import com.jeeplus.modules.right.entity.YybRight;
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
public class YybUsageApiService extends CrudService<YybUsageApiMapper, YybUsage> {

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


	public List<YybUsage> findAllList(YybUsage yybUsage) {
		return super.findAllList(yybUsage);
	}


	public List<YybUsage> getListByIds(List<String> usageSelectList) {
		return mapper.getListByIds(usageSelectList);
	}
}