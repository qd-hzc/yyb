/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.right.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 权利Entity
 * @author lwb
 * @version 2019-02-17
 */
public class YybRight extends DataEntity<YybRight> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private Double rate;		// 增长率
	private Double beginRate;		// 开始 增长率
	private Double endRate;		// 结束 增长率
	
	public YybRight() {
		super();
	}

	public YybRight(String id){
		super(id);
	}

	@Length(min=2, max=10, message="名称长度必须介于 2 和 10 之间")
	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="增长率不能为空")
	@ExcelField(title="增长率", align=2, sort=7)
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	public Double getBeginRate() {
		return beginRate;
	}

	public void setBeginRate(Double beginRate) {
		this.beginRate = beginRate;
	}
	
	public Double getEndRate() {
		return endRate;
	}

	public void setEndRate(Double endRate) {
		this.endRate = endRate;
	}
		
}