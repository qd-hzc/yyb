/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.tagcatetory.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;

import java.util.List;
import java.util.Map;

/**
 * 标签分类MAPPER接口
 * @author l
 * @version 2019-02-18
 */
@MyBatisMapper
public interface YybTagCategoryApiMapper extends TreeMapper<YybTagCategory> {

    List<YybTagCategory> findListNoParent(YybTagCategory yybTagCategory);

    List<YybTagCategory> findAll(YybTagCategory yybTagCategory);

    List<YybTagCategory> getTagData(Map<String, Object> map);
}