/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.music.entity;

import javax.validation.constraints.NotNull;
import com.jeeplus.modules.musician.entity.YybMusician;
import com.jeeplus.modules.musician.entity.YybMusicianAlbum;
import com.jeeplus.modules.tagcatetory.entity.YybTagCategory;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 音乐Entity
 * @author lwb
 * @version 2019-02-19
 */
public class YybMusic extends DataEntity<YybMusic> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 歌名
	private Double price;		// 价格
	private YybMusician yybMusician;		// 歌手
	private YybMusicianAlbum yybMusicianAlbum;		// 专辑
	private String url;		// 地址
	private String img;		// 图片
	private YybTagCategory yybTagCategory;		// 标签
	private String musicTime;		// 时长
	private Date publishTime;		// 发布时间
	private Integer playCount;		// 播放量
	private Integer likeCount;		// 收藏量
	
	public YybMusic() {
		super();
	}

	public YybMusic(String id){
		super(id);
	}

	@ExcelField(title="歌名", align=2, sort=6)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotNull(message="价格不能为空")
	@ExcelField(title="价格", align=2, sort=7)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@NotNull(message="歌手不能为空")
	@ExcelField(title="歌手", fieldType=YybMusician.class, value="yybMusician.name", align=2, sort=8)
	public YybMusician getYybMusician() {
		return yybMusician;
	}

	public void setYybMusician(YybMusician yybMusician) {
		this.yybMusician = yybMusician;
	}
	
	@NotNull(message="专辑不能为空")
	@ExcelField(title="专辑", fieldType=YybMusicianAlbum.class, value="yybMusicianAlbum.name", align=2, sort=9)
	public YybMusicianAlbum getYybMusicianAlbum() {
		return yybMusicianAlbum;
	}

	public void setYybMusicianAlbum(YybMusicianAlbum yybMusicianAlbum) {
		this.yybMusicianAlbum = yybMusicianAlbum;
	}
	
	@ExcelField(title="地址", align=2, sort=10)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="图片", align=2, sort=11)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@ExcelField(title="标签", fieldType=YybTagCategory.class, value="yybTagCategory.name", align=2, sort=12)
	public YybTagCategory getYybTagCategory() {
		return yybTagCategory;
	}

	public void setYybTagCategory(YybTagCategory yybTagCategory) {
		this.yybTagCategory = yybTagCategory;
	}
	
	@ExcelField(title="时长", align=2, sort=13)
	public String getMusicTime() {
		return musicTime;
	}

	public void setMusicTime(String musicTime) {
		this.musicTime = musicTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="发布时间不能为空")
	@ExcelField(title="发布时间", align=2, sort=14)
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	
	@ExcelField(title="播放量", align=2, sort=16)
	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}
	
	@ExcelField(title="收藏量", align=2, sort=17)
	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	
}