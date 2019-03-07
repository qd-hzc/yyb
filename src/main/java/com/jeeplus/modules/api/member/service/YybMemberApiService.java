/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.member.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.member.mapper.YybMemberApiMapper;
import com.jeeplus.modules.member.entity.YybMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 会员Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybMemberApiService extends CrudService<YybMemberApiMapper, YybMember> {

	public YybMember get(String id) {
		return super.get(id);
	}

	public List<YybMember> findList(YybMember yybMember) {
		return super.findList(yybMember);
	}

	public Page<YybMember> findPage(Page<YybMember> page, YybMember yybMember) {
		return super.findPage(page, yybMember);
	}

	@Transactional(readOnly = false)
	public void save(YybMember yybMember) {
		super.save(yybMember);
	}

	@Transactional(readOnly = false)
	public void delete(YybMember yybMember) {
		super.delete(yybMember);
	}

    public YybMember getByLoginName(String loginName) {
		return mapper.getByLoginName(loginName);
    }

	@Transactional(readOnly = false)
    public void updatePass(YybMember yybMember) {

		mapper.updatePass(yybMember);
    }
	@Transactional(readOnly = false)
	public void bindTags(Map<String, Object> param) {
		mapper.bindTags(param);
	}
}