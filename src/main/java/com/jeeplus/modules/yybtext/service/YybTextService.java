/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yybtext.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.yybtext.entity.YybText;
import com.jeeplus.modules.yybtext.mapper.YybTextMapper;

/**
 * 文案配置Service
 * @author wenbin
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class YybTextService extends CrudService<YybTextMapper, YybText> {

	public YybText get(String id) {
		return super.get(id);
	}
	
	public List<YybText> findList(YybText yybText) {
		return super.findList(yybText);
	}
	
	public Page<YybText> findPage(Page<YybText> page, YybText yybText) {
		return super.findPage(page, yybText);
	}
	
	@Transactional(readOnly = false)
	public void save(YybText yybText) {
		super.save(yybText);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybText yybText) {
		super.delete(yybText);
	}
	
}