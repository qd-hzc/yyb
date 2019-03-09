/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.shopcart.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车Entity
 * @author lwb
 * @version 2019-02-18
 */
public class YybShopcartVo  implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "音乐不能为空")
	private String musicId;		// 音乐

	@DecimalMin(value="0.01", message = "音乐价格不能少于0.01")
	private BigDecimal musicPrice;		// 音乐价格

	@NotEmpty(message = "选择的权利不能为空")
	private List<String> rightSelectList;		// 选择的权利

	@NotEmpty(message = "选择的用途不能为空")
	private List<String> usageSelectList;		// 选择的用途

	@DecimalMin(value="0.01", message = "总额不能为空不能少于0.01")
	private BigDecimal musicTotal;		// 总额


	public String getMusicId() {
		return musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public BigDecimal getMusicPrice() {
		return musicPrice;
	}

	public void setMusicPrice(BigDecimal musicPrice) {
		this.musicPrice = musicPrice;
	}

	public List<String> getRightSelectList() {
		return rightSelectList;
	}

	public void setRightSelectList(List<String> rightSelectList) {
		this.rightSelectList = rightSelectList;
	}

	public List<String> getUsageSelectList() {
		return usageSelectList;
	}

	public void setUsageSelectList(List<String> usageSelectList) {
		this.usageSelectList = usageSelectList;
	}

	public BigDecimal getMusicTotal() {
		return musicTotal;
	}

	public void setMusicTotal(BigDecimal musicTotal) {
		this.musicTotal = musicTotal;
	}
}