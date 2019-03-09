/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.shopcart.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 购物车Entity
 * @author lwb
 * @version 2019-02-18
 */
public class YybShopcart extends DataEntity<YybShopcart> {
	
	private static final long serialVersionUID = 1L;
	private String memberId;		// 用户
	private String musicId;		// 音乐
	private String musicTitle;		// 音乐名称
	private Double musicPrice;		// 音乐价格
	private String rightSelect;		// 选择的权利
	private String rightSelectName;		// 选择的权利
	private String usageSelect;		// 选择的用途
	private String usageSelectName;		// 选择的用途
	private Double musicTotal;		// 总额
	private String musicianName;    //音乐人
	
	public YybShopcart() {
		super();
	}

	public YybShopcart(String id){
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
	
	@ExcelField(title="音乐名称", align=2, sort=9)
	public String getMusicTitle() {
		return musicTitle;
	}

	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}
	
	@ExcelField(title="音乐价格", align=2, sort=10)
	public Double getMusicPrice() {
		return musicPrice;
	}

	public void setMusicPrice(Double musicPrice) {
		this.musicPrice = musicPrice;
	}
	
	@ExcelField(title="选择的权利", align=2, sort=11)
	public String getRightSelect() {
		return rightSelect;
	}

	public void setRightSelect(String rightSelect) {
		this.rightSelect = rightSelect;
	}
	
	@ExcelField(title="选择的用途", align=2, sort=12)
	public String getUsageSelect() {
		return usageSelect;
	}

	public void setUsageSelect(String usageSelect) {
		this.usageSelect = usageSelect;
	}
	
	@ExcelField(title="总额", align=2, sort=13)
	public Double getMusicTotal() {
		return musicTotal;
	}

	public void setMusicTotal(Double musicTotal) {
		this.musicTotal = musicTotal;
	}

	public String getMusicianName() {
		return musicianName;
	}

	public void setMusicianName(String musicianName) {
		this.musicianName = musicianName;
	}

	public String getRightSelectName() {
		return rightSelectName;
	}

	public void setRightSelectName(String rightSelectName) {
		this.rightSelectName = rightSelectName;
	}

	public String getUsageSelectName() {
		return usageSelectName;
	}

	public void setUsageSelectName(String usageSelectName) {
		this.usageSelectName = usageSelectName;
	}
}