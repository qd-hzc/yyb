/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yybtext.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 文案配置Entity
 * @author wenbin
 * @version 2019-05-05
 */
public class YybText extends DataEntity<YybText> {
	
	private static final long serialVersionUID = 1L;
	private String textKey;		// 对应键
	private String textValue;		// 文案
	
	public YybText() {
		super();
	}

	public YybText(String id){
		super(id);
	}

	@ExcelField(title="对应键", align=2, sort=6)
	public String getTextKey() {
		return textKey;
	}

	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	
	@ExcelField(title="文案", align=2, sort=8)
	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	
}