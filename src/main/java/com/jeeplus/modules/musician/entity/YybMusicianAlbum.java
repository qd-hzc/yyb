/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.musician.entity;

import com.jeeplus.modules.musician.entity.YybMusician;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 专辑Entity
 * @author lwb
 * @version 2019-02-22
 */
public class YybMusicianAlbum extends DataEntity<YybMusicianAlbum> {
	
	private static final long serialVersionUID = 1L;
	private YybMusician yybMusician;		// 音乐人 父类
	private String name;		// 专辑名
	private String img;		// 封面
	
	public YybMusicianAlbum() {
		super();
	}

	public YybMusicianAlbum(String id){
		super(id);
	}

	public YybMusicianAlbum(YybMusician yybMusician){
		this.yybMusician = yybMusician;
	}

	public YybMusician getYybMusician() {
		return yybMusician;
	}

	public void setYybMusician(YybMusician yybMusician) {
		this.yybMusician = yybMusician;
	}
	
	@ExcelField(title="专辑名", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="封面", align=2, sort=8)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
}