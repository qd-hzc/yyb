/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.music.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.modules.api.music.entity.YybMusicVo;
import com.jeeplus.modules.api.music.mapper.YybMusicApiMapper;
import com.jeeplus.modules.api.musician.mapper.YybMusicianApiMapper;
import com.jeeplus.modules.musician.mapper.YybMusicianMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class YybMusicApiService extends CrudService<YybMusicApiMapper, YybMusic> {

	@Autowired
	YybMusicApiMapper yybMusicApiMapper;

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

	public List<YybMusic> getIndexMusicList(Map<String,Object> param) {
		return yybMusicApiMapper.getIndexMusicList(param);
	}

	public List<YybMusic> searchMusic(YybMusicVo yybMusicVo) {
		return  yybMusicApiMapper.searchMusic(yybMusicVo);
	}

	@Transactional(readOnly = false)
	public void updateAddLikeCount(String musicId) {
		yybMusicApiMapper.updateAddLikeCount(musicId);
	}

	@Transactional(readOnly = false)
	public void updateReduceLikeCount(String musicId) {
		yybMusicApiMapper.updateReduceLikeCount(musicId);
	}

	@Transactional(readOnly = false)
	public void updateAddPlayHistoryCount(String musicId) {
		yybMusicApiMapper.updateAddPlayHistoryCount(musicId);
	}

	public YybMusic getExcellentCase() {
		return mapper.getExcellentCase();
	}
}