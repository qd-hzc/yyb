/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.right.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.right.entity.YybRight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权利MAPPER接口
 * @author lwb
 * @version 2019-02-17
 */
@MyBatisMapper
public interface YybRightApiMapper extends BaseMapper<YybRight> {

    List<YybRight> getListByIds(@Param(value = "list") List<String> rightSelectList);
}