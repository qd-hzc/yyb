/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.musician.entity;

import org.hibernate.validator.constraints.Length;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 音乐人Entity
 * @author lwb
 * @version 2019-02-18
 */
public class YybMusician extends DataEntity<YybMusician> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String headPhoto;		// 头像
	private List<YybMusicianAlbum> yybMusicianAlbumList = Lists.newArrayList();		// 子表列表
	
	public YybMusician() {
		super();
	}

	public YybMusician(String id){
		super(id);
	}

	@Length(min=2, max=12, message="姓名长度必须介于 2 和 12 之间")
	@ExcelField(title="姓名", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="头像", align=2, sort=7)
	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
	
	public List<YybMusicianAlbum> getYybMusicianAlbumList() {
		return yybMusicianAlbumList;
	}

	public void setYybMusicianAlbumList(List<YybMusicianAlbum> yybMusicianAlbumList) {
		this.yybMusicianAlbumList = yybMusicianAlbumList;
	}
}