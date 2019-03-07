/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.member.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.member.entity.YybMember;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 会员MAPPER接口
 * @author lwb
 * @version 2019-02-18
 */
@MyBatisMapper
public interface YybMemberApiMapper extends BaseMapper<YybMember> {

    YybMember getByLoginName(@Param("loginName") String loginName);

    void updatePass(YybMember yybMember);

    void bindTags(Map<String, Object> param);
}