/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tagcatetory.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 标签分类Entity
 * @author l
 * @version 2019-02-18
 */
public class YybTagCategory extends TreeEntity<YybTagCategory> {
	
	private static final long serialVersionUID = 1L;
	
	
	public YybTagCategory() {
		super();
	}

	public YybTagCategory(String id){
		super(id);
	}

	public  YybTagCategory getParent() {
			return parent;
	}
	
	@Override
	public void setParent(YybTagCategory parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}