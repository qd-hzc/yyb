/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.like.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 个人喜欢Entity
 * @author lwb
 * @version 2019-02-18
 */
public class YybLike extends DataEntity<YybLike> {
	
	private static final long serialVersionUID = 1L;
	private String memberId;		// 用户
	private String musicId;		// 音乐
	
	public YybLike() {
		super();
	}

	public YybLike(String id){
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
	
}