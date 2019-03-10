/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.order.entity.YybOrder;

import java.util.Map;

/**
 * 订单MAPPER接口
 * @author lwb
 * @version 2019-02-20
 */
@MyBatisMapper
public interface YybOrderMapper extends BaseMapper<YybOrder> {

    void updateStatus(Map<String, Object> param);

    void cancelOvertimeOrder();
}