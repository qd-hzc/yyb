/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.api.order.entity.OrderDeatilApi;

import java.util.List;

/**
 * 订单详情MAPPER接口
 * @author lwb
 * @version 2019-02-20
 */
@MyBatisMapper
public interface YybOrderDeatilApiMapper extends BaseMapper<OrderDeatilApi> {
    List<OrderDeatilApi> listByOrderId(String orderId);
}