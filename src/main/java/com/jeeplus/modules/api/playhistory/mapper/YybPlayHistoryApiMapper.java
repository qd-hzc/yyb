/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.playhistory.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.playhistory.entity.YybPlayHistory;

import java.util.List;
import java.util.Map;

/**
 * 播放历史MAPPER接口
 * @author lwb
 * @version 2019-03-06
 */
@MyBatisMapper
public interface YybPlayHistoryApiMapper extends BaseMapper<YybPlayHistory> {

    List<YybPlayHistory> memberPlayHistoryList(Map<String, Object> param);

    YybPlayHistory getByCondition(Map<String, Object> param);

}