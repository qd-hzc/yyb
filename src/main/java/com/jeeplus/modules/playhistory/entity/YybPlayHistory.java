/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.playhistory.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.music.entity.YybMusic;

/**
 * 播放历史Entity
 * @author lwb
 * @version 2019-03-06
 */
public class YybPlayHistory extends DataEntity<YybPlayHistory> {
	
	private static final long serialVersionUID = 1L;
	private String memberId;		// 用户
	private String musicId;		// 音乐
	private YybMusic yybMusic;
	
	public YybPlayHistory() {
		super();
	}

	public YybPlayHistory(String id){
		super(id);
	}

	@ExcelField(title="用户", align=2, sort=7)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@ExcelField(title="音乐", align=2, sort=8)
	public String getMusicId() {
		return musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public YybMusic getYybMusic() {
		return yybMusic;
	}

	public void setYybMusic(YybMusic yybMusic) {
		this.yybMusic = yybMusic;
	}
}