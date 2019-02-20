/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.music.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.music.entity.YybMusic;
import com.jeeplus.modules.music.mapper.YybMusicMapper;

/**
 * 音乐Service
 * @author lwb
 * @version 2019-02-19
 */
@Service
@Transactional(readOnly = true)
public class YybMusicService extends CrudService<YybMusicMapper, YybMusic> {

	public YybMusic get(String id) {
		return super.get(id);
	}
	
	public List<YybMusic> findList(YybMusic yybMusic) {
		return super.findList(yybMusic);
	}
	
	public Page<YybMusic> findPage(Page<YybMusic> page, YybMusic yybMusic) {
		return super.findPage(page, yybMusic);
	}
	
	@Transactional(readOnly = false)
	public void save(YybMusic yybMusic) {
		super.save(yybMusic);
	}
	
	@Transactional(readOnly = false)
	public void delete(YybMusic yybMusic) {
		super.delete(yybMusic);
	}
	
}