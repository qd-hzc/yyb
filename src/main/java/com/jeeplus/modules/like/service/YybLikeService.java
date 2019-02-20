/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.like.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.like.entity.YybLike;
import com.jeeplus.modules.like.mapper.YybLikeMapper;

/**
 * 个人喜欢Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybLikeService extends CrudService<YybLikeMapper, YybLike> {

	public YybLike get(String id) {
		return super.get(id);
	}
	
	public List<YybLike> findList(YybLike yybLike) {
		return super.findList(yybLike);
	}
	
	public Page<YybLike> findPage(Page<YybLike> page, YybLike yybLike) {
		return super.findPage(page, yybLike);
	}
	
	@Transactional(readOnly = false)
	public void save(YybLike yybLike) {
		super.save(yybLike);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybLike yybLike) {
		super.delete(yybLike);
	}
	
}