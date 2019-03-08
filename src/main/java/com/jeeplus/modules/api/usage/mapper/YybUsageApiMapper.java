/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.usage.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.usage.entity.YybUsage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用途MAPPER接口
 * @author lwb
 * @version 2019-02-17
 */
@MyBatisMapper
public interface YybUsageApiMapper extends BaseMapper<YybUsage> {

    List<YybUsage> getListByIds(@Param(value = "list") List<String> usageSelectList);
}