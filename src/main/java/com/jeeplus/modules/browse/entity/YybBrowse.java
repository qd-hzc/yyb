/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.browse.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 个人浏览Entity
 * @author lwb
 * @version 2019-02-18
 */
public class YybBrowse extends DataEntity<YybBrowse> {
	
	private static final long serialVersionUID = 1L;
	private String 用户;		// 用户
	private String 音乐;		// 音乐
	
	public YybBrowse() {
		super();
	}

	public YybBrowse(String id){
		super(id);
	}

	@ExcelField(title="用户", align=2, sort=7)
	public String get用户() {
		return 用户;
	}

	public void set用户(String 用户) {
		this.用户 = 用户;
	}
	
	@ExcelField(title="音乐", align=2, sort=8)
	public String get音乐() {
		return 音乐;
	}

	public void set音乐(String 音乐) {
		this.音乐 = 音乐;
	}
	
}