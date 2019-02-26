/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysparam.service;

import java.util.Arrays;
import java.util.List;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sysparam.entity.SysParam;
import com.jeeplus.modules.sysparam.mapper.SysParamMapper;

/**
 * 参数配置Service
 * @author lwb
 * @version 2019-02-26
 */
@Service
@Transactional(readOnly = true)
public class SysParamService extends CrudService<SysParamMapper, SysParam> {

	@Autowired
	private SysParamMapper sysParamMapper;

	public SysParam get(String id) {
		return super.get(id);
	}

	public String getValueByKey(String key){
		return sysParamMapper.getValueByKey(key);
	}
	
	public List<SysParam> findList(SysParam sysParam) {
		return super.findList(sysParam);
	}
	
	public Page<SysParam> findPage(Page<SysParam> page, SysParam sysParam) {
		return super.findPage(page, sysParam);
	}
	
	@Transactional(readOnly = false)
	public void save(SysParam sysParam) {
		super.save(sysParam);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysParam sysParam) {
		super.delete(sysParam);
	}

	public Boolean judgeMusicOrderAll() {
		String key = Global.getConfig("paramKey.musicOrderAll");
		String value = sysParamMapper.getValueByKey(key);
		List<String> list = Arrays.asList(value.split(","));
		String loginName = UserUtils.getUser().getLoginName();
		if (list.contains(loginName)) {
			return true;
		} else {
			return false;
		}
	}
}