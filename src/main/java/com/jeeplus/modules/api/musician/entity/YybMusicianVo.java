/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.musician.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.musician.entity.YybMusicianAlbum;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 音乐人Entity
 * @author lwb
 * @version 2019-02-23
 */
public class YybMusicianVo implements Serializable {


	@NotEmpty(message = "姓名不能为空")
	private String name;		// 姓名

	@NotEmpty(message = "地址不能为空")
	private String address;		// 地址

	@NotEmpty(message = "手机不能为空")
	@Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式有误")
	private String phone;		// 手机

	@NotEmpty(message = "邮箱不能为空")
	@Email(message = "邮箱格式有误")
	private String mail;		// 邮箱

	@NotEmpty(message = "身份证号不能为空")
	@Pattern(regexp = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$", message = "身份证号格式有误")
	private String idCard;		// 身份证号

	@NotEmpty(message = "身份证附件不能为空")
	private String idCardAttach;		// 身份证附件

	@NotEmpty(message = "作品不能为空")
	private String production;		// 作品

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIdCardAttach() {
		return idCardAttach;
	}

	public void setIdCardAttach(String idCardAttach) {
		this.idCardAttach = idCardAttach;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}


}