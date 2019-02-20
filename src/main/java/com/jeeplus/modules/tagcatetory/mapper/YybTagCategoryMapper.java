/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tagcatetory.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;

import java.util.List;

/**
 * 标签分类MAPPER接口
 * @author l
 * @version 2019-02-18
 */
@MyBatisMapper
public interface YybTagCategoryMapper extends TreeMapper<YybTagCategory> {

    List<YybTagCategory> findListNoParent(YybTagCategory yybTagCategory);
}