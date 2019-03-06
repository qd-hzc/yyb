/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.like.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.like.entity.YybLike;

/**
 * 个人喜欢MAPPER接口
 * @author lwb
 * @version 2019-02-18
 */
@MyBatisMapper
public interface YybLikeApiMapper extends BaseMapper<YybLike> {
	
}