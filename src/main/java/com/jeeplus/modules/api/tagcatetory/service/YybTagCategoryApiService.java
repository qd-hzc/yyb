/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.tagcatetory.service;

import java.util.List;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.api.tagcatetory.mapper.YybTagCategoryApiMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import com.jeeplus.modules.tagcatetory.mapper.YybTagCategoryMapper;

/**
 * 标签分类Service
 * @author l
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybTagCategoryApiService extends TreeService<YybTagCategoryApiMapper, YybTagCategory> {

	public YybTagCategory get(String id) {
		return super.get(id);
	}
	
	public List<YybTagCategory> findList(YybTagCategory yybTagCategory) {
		if (StringUtils.isNotBlank(yybTagCategory.getParentIds())){
			yybTagCategory.setParentIds(","+yybTagCategory.getParentIds()+",");
		}
		return super.findList(yybTagCategory);
	}



	@Transactional(readOnly = false)
	public void save(YybTagCategory yybTagCategory) {
		super.save(yybTagCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybTagCategory yybTagCategory) {
		super.delete(yybTagCategory);
	}

	public Page<YybTagCategory> findPageNoParent(Page<YybTagCategory> yybTagCategoryPage, YybTagCategory yybTagCategory) {
		dataRuleFilter(yybTagCategory);
		yybTagCategory.setPage(yybTagCategoryPage);
		yybTagCategoryPage.setList(mapper.findListNoParent(yybTagCategory));
		return yybTagCategoryPage;
	}

	public List<YybTagCategory> findAll(YybTagCategory yybTagCategory) {
		return mapper.findAll(yybTagCategory);
	}
}