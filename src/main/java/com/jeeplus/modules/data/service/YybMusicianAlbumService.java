/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.data.service;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.musician.entity.YybMusicianAlbum;
import com.jeeplus.modules.musician.mapper.YybMusicianAlbumMapper;
import com.jeeplus.modules.musician.mapper.YybMusicianMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 音乐人Service
 * @author lwb
 * @version 2019-02-18
 */
@Service
@ComponentScan
@Transactional(readOnly = true)
public class YybMusicianAlbumService extends CrudService<YybMusicianAlbumMapper, YybMusicianAlbum> {

	@Autowired
	private YybMusicianAlbumMapper yybMusicianAlbumMapper;

	public List<YybMusicianAlbum> findList(YybMusicianAlbum yybMusicianAlbum) {
		return super.findList(yybMusicianAlbum);
	}
	

	
}