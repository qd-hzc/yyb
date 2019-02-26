/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysparam.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 参数配置Entity
 * @author lwb
 * @version 2019-02-26
 */
public class SysParam extends DataEntity<SysParam> {
	
	private static final long serialVersionUID = 1L;
	private String paramKey;		// 键
	private String paramValue;		// 值
	
	public SysParam() {
		super();
	}

	public SysParam(String id){
		super(id);
	}

	@ExcelField(title="键", align=2, sort=2)
	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	
	@ExcelField(title="值", align=2, sort=3)
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
}