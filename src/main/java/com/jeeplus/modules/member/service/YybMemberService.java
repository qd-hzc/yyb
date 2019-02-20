/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.member.entity.YybMember;
import com.jeeplus.modules.member.mapper.YybMemberMapper;

/**
 * 会员Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@Transactional(readOnly = true)
public class YybMemberService extends CrudService<YybMemberMapper, YybMember> {



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

}