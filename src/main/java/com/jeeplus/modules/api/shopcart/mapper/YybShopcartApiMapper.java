/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.shopcart.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 购物车MAPPER接口
 * @author lwb
 * @version 2019-02-18
 */
@MyBatisMapper
public interface YybShopcartApiMapper extends BaseMapper<YybShopcart> {

    List<YybShopcart> shopcartList(Map<String, Object> param);

    int getCountByCodition(Map<String, Object> param);

    List<YybShopcart> getListByIds(@Param(value = "list") List<String> ids);

    void updateOrderId(Map<String, Object> shopcartParam);
}