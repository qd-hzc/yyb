/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.text.mapper.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.text.mapper.YybTextApiMapper;
import com.jeeplus.modules.yybtext.entity.YybText;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文案配置Service
 * @author wenbin
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class YybTextApiService extends CrudService<YybTextApiMapper, YybText> {

	public YybText get(String id) {
		return super.get(id);
	}

	public YybText getByKey(String key)
	{
		return mapper.getByKey(key);
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