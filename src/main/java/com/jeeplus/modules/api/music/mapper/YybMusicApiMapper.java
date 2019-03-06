/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.music.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.api.music.entity.YybMusicVo;
import com.jeeplus.modules.music.entity.YybMusic;

import java.util.List;
import java.util.Map;

/**
 * 音乐MAPPER接口
 * @author lwb
 * @version 2019-02-19
 */
@MyBatisMapper
public interface YybMusicApiMapper extends BaseMapper<YybMusic> {

    List<YybMusic> getIndexMusicList(Map<String,Object> param);

    List<YybMusic> searchMusic(YybMusicVo yybMusicVo);

    void updateLikeCount(String musicId);
}