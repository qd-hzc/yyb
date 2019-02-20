/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.browse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.browse.entity.YybBrowse;
import com.jeeplus.modules.browse.mapper.YybBrowseMapper;

/**
 * 个人浏览Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybBrowseService extends CrudService<YybBrowseMapper, YybBrowse> {

	public YybBrowse get(String id) {
		return super.get(id);
	}
	
	public List<YybBrowse> findList(YybBrowse yybBrowse) {
		return super.findList(yybBrowse);
	}
	
	public Page<YybBrowse> findPage(Page<YybBrowse> page, YybBrowse yybBrowse) {
		return super.findPage(page, yybBrowse);
	}
	
	@Transactional(readOnly = false)
	public void save(YybBrowse yybBrowse) {
		super.save(yybBrowse);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybBrowse yybBrowse) {
		super.delete(yybBrowse);
	}
	
}