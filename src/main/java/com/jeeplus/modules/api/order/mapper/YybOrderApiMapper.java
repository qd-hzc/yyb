/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.order.entity.YybOrder;

/**
 * 订单MAPPER接口
 * @author lwb
 * @version 2019-02-20
 */
@MyBatisMapper
public interface YybOrderApiMapper extends BaseMapper<YybOrder> {

    void save(YybOrder yybOrder);
}