/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.playhistory.service;

import java.util.List;

import com.jeeplus.modules.api.playhistory.mapper.YybPlayHistoryApiMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.playhistory.entity.YybPlayHistory;
import com.jeeplus.modules.playhistory.mapper.YybPlayHistoryMapper;

/**
 * 播放历史Service
 * @author lwb
 * @version 2019-03-06
 */
@Service
@Transactional(readOnly = true)
public class YybPlayHistoryApiService extends CrudService<YybPlayHistoryApiMapper, YybPlayHistory> {

	public YybPlayHistory get(String id) {
		return super.get(id);
	}
	
	public List<YybPlayHistory> findList(YybPlayHistory yybPlayHistory) {
		return super.findList(yybPlayHistory);
	}
	
	public Page<YybPlayHistory> findPage(Page<YybPlayHistory> page, YybPlayHistory yybPlayHistory) {
		return super.findPage(page, yybPlayHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(YybPlayHistory yybPlayHistory) {
		super.save(yybPlayHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybPlayHistory yybPlayHistory) {
		super.delete(yybPlayHistory);
	}
	
}