/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.text.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.yybtext.entity.YybText;

/**
 * 文案配置MAPPER接口
 * @author wenbin
 * @version 2019-05-05
 */
@MyBatisMapper
public interface YybTextApiMapper extends BaseMapper<YybText> {
    YybText getByKey(String key);
}