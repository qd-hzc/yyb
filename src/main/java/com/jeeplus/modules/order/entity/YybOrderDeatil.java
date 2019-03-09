/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.order.entity;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.right.entity.YybRight;
import com.jeeplus.modules.api.shopcart.entity.YybShopcart;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.usage.entity.YybUsage;

import java.util.List;

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
	private List<YybUsage> usageList;		// 用途
	private String usageSelectName;

	private String rightSelect;		// 权利
	private List<YybRight> rightList;		// 权利
	private String rightSelectName;

	private Double musicTotal;		// 总价

	private String musicianName;
	
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


	
	@ExcelField(title="权利", align=2, sort=12)
	public String getRightSelect() {
		return rightSelect;
	}

	public void setUsageSelect(String usageSelect) {
		this.usageSelect = usageSelect;
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

	public List<YybUsage> getUsageList() {
		return usageList;
	}

	public void setUsageList(List<YybUsage> usageList) {
		this.usageList = usageList;
	}

	public List<YybRight> getRightList() {
		return rightList;
	}

	public void setRightList(List<YybRight> rightList) {
		this.rightList = rightList;
	}

	public String getUsageSelectName() {
		return usageSelectName;
	}

	public void setUsageSelectName(String usageSelectName) {
		this.usageSelectName = usageSelectName;
	}

	public String getRightSelectName() {
		return rightSelectName;
	}

	public void setRightSelectName(String rightSelectName) {
		this.rightSelectName = rightSelectName;
	}


	public String getMusicianName() {
		return musicianName;
	}

	public void setMusicianName(String musicianName) {
		this.musicianName = musicianName;
	}
}