/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yyborderdetailone.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单详情Entity
 * @author lwb
 * @version 2019-03-11
 */
public class YybOrderDeatilOne extends DataEntity<YybOrderDeatilOne> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单
	private String musicTitle;		// 音乐名称
	private Double musicPrice;		// 音乐单价
	private String musicianName;		// 音乐人
	private String albumName;		// 专辑
	private String companyName;		// 公司
	private Double musicTotal;		// 总价
	private String rightSelectName;		// 权利
	private String usageSelectName;		// 用途
	
	public YybOrderDeatilOne() {
		super();
	}

	public YybOrderDeatilOne(String id){
		super(id);
	}

	@ExcelField(title="订单", align=2, sort=6)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="音乐名称", align=2, sort=7)
	public String getMusicTitle() {
		return musicTitle;
	}

	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}
	
	@ExcelField(title="音乐单价", align=2, sort=8)
	public Double getMusicPrice() {
		return musicPrice;
	}

	public void setMusicPrice(Double musicPrice) {
		this.musicPrice = musicPrice;
	}
	
	@ExcelField(title="音乐人", align=2, sort=9)
	public String getMusicianName() {
		return musicianName;
	}

	public void setMusicianName(String musicianName) {
		this.musicianName = musicianName;
	}
	
	@ExcelField(title="专辑", align=2, sort=10)
	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	@ExcelField(title="公司", align=2, sort=11)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@ExcelField(title="总价", align=2, sort=12)
	public Double getMusicTotal() {
		return musicTotal;
	}

	public void setMusicTotal(Double musicTotal) {
		this.musicTotal = musicTotal;
	}
	
	@ExcelField(title="权利", align=2, sort=13)
	public String getRightSelectName() {
		return rightSelectName;
	}

	public void setRightSelectName(String rightSelectName) {
		this.rightSelectName = rightSelectName;
	}
	
	@ExcelField(title="用途", align=2, sort=14)
	public String getUsageSelectName() {
		return usageSelectName;
	}

	public void setUsageSelectName(String usageSelectName) {
		this.usageSelectName = usageSelectName;
	}
	
}