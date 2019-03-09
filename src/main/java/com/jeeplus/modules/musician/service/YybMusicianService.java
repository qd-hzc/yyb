/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.musician.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.musician.mapper.YybMusicianMapper;
import com.jeeplus.modules.musician.entity.YybMusicianAlbum;
import com.jeeplus.modules.musician.mapper.YybMusicianAlbumMapper;

/**
 * 音乐人Service
 * @author lwb
 * @version 2019-02-23
 */
@Service
@Transactional(readOnly = true)
public class YybMusicianService extends CrudService<YybMusicianMapper, YybMusician> {

	@Autowired
	private YybMusicianAlbumMapper yybMusicianAlbumMapper;

		@Autowired
	private YybMusicianMapper yybMusicianMapper;

	public YybMusician get(String id) {
		YybMusician yybMusician = super.get(id);
		yybMusician.setYybMusicianAlbumList(yybMusicianAlbumMapper.findList(new YybMusicianAlbum(yybMusician)));
		return yybMusician;
	}
	
	public List<YybMusician> findList(YybMusician yybMusician) {
		return super.findList(yybMusician);
	}
	
	public Page<YybMusician> findPage(Page<YybMusician> page, YybMusician yybMusician) {
		return super.findPage(page, yybMusician);
	}
	
	@Transactional(readOnly = false)
	public void save(YybMusician yybMusician) {
		super.save(yybMusician);
		for (YybMusicianAlbum yybMusicianAlbum : yybMusician.getYybMusicianAlbumList()){
			if (yybMusicianAlbum.getId() == null){
				continue;
			}
			if (YybMusicianAlbum.DEL_FLAG_NORMAL.equals(yybMusicianAlbum.getDelFlag())){
				if (StringUtils.isBlank(yybMusicianAlbum.getId())){
					yybMusicianAlbum.setYybMusician(yybMusician);
					yybMusicianAlbum.preInsert();
					yybMusicianAlbumMapper.insert(yybMusicianAlbum);
				}else{
					yybMusicianAlbum.preUpdate();
					yybMusicianAlbumMapper.update(yybMusicianAlbum);
				}
			}else{
				yybMusicianAlbumMapper.delete(yybMusicianAlbum);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(YybMusician yybMusician) {
		super.delete(yybMusician);
		yybMusicianAlbumMapper.delete(new YybMusicianAlbum(yybMusician));
	}

	@Transactional(readOnly = false)
	public void updateStatus(YybMusician yybMusician) {
		yybMusicianMapper.updateStatus(yybMusician);
	}

    public int getMemberApplyHis(Map<String, Object> param) {
		return mapper.getMemberApplyHis(param);
    }
}