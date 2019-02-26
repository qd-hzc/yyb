/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysparam.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sysparam.entity.SysParam;
import org.apache.ibatis.annotations.Param;

/**
 * 参数配置MAPPER接口
 * @author lwb
 * @version 2019-02-26
 */
@MyBatisMapper
public interface SysParamMapper extends BaseMapper<SysParam> {

    String getValueByKey(@Param(value = "key") String key);
}