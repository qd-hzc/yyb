/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;

import com.jeeplus.modules.order.entity.YybOrder;
import com.jeeplus.modules.shopcart.entity.YybShopcart;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单详情Entity
 * @author lwb
 * @version 2019-02-20
 */
public class YybOrderDeatil extends DataEntity<YybOrderDeatil> {
	
	private static final long serialVersionUID = 1L;
	private YybOrder yybOrder;		// 订单 父类
	private YybShopcart yybShopcart;		// 购物车
	private String musicId;		// 音乐
	private String musicTitle;		// 音乐名称
	private Double musicPrice;		// 音乐单价
	private String usageSelect;		// 用途
	private String rightSelect;		// 权利
	private Double musicTotal;		// 总价
	
	public YybOrderDeatil() {
		super();
	}

	public YybOrderDeatil(String id){
		super(id);
	}

	public YybOrderDeatil(YybOrder yybOrder){
		this.yybOrder = yybOrder;
	}

	public YybOrder getYybOrder() {
		return yybOrder;
	}

	public void setYybOrder(YybOrder yybOrder) {
		this.yybOrder = yybOrder;
	}
	
	@ExcelField(title="购物车", align=2, sort=7)
	public YybShopcart getYybShopcart() {
		return yybShopcart;
	}

	public void setYybShopcart(YybShopcart yybShopcart) {
		this.yybShopcart = yybShopcart;
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
	
	@ExcelField(title="音乐单价", align=2, sort=10)
	public Double getMusicPrice() {
		return musicPrice;
	}

	public void setMusicPrice(Double musicPrice) {
		this.musicPrice = musicPrice;
	}
	
	@ExcelField(title="用途", align=2, sort=11)
	public String getUsageSelect() {
		return usageSelect;
	}

	public void setUsageSelect(String usageSelect) {
		this.usageSelect = usageSelect;
	}
	
	@ExcelField(title="权利", align=2, sort=12)
	public String getRightSelect() {
		return rightSelect;
	}

	public void setRightSelect(String rightSelect) {
		this.rightSelect = rightSelect;
	}
	
	@ExcelField(title="总价", align=2, sort=13)
	public Double getMusicTotal() {
		return musicTotal;
	}

	public void setMusicTotal(Double musicTotal) {
		this.musicTotal = musicTotal;
	}
	
}