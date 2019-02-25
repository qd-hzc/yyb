/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.musician.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Email;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 音乐人Entity
 * @author lwb
 * @version 2019-02-23
 */
public class YybMusician extends DataEntity<YybMusician> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String headPhoto;		// 头像
	private String stageName;		// 艺名
	private String address;		// 地址
	private String phone;		// 手机
	private String mail;		// 邮箱
	private String idCard;		// 身份证号
	private String idCardAttach;		// 身份证附件
	private String production;		// 作品
	private String companyId;		// 归属公司
	private Integer type;		// 类型
	private String companyName;		// 归属公司
	private Integer status;		// 音乐人状态
	private List<YybMusicianAlbum> yybMusicianAlbumList = Lists.newArrayList();		// 子表列表
	
	public YybMusician() {
		super();
	}

	public YybMusician(String id){
		super(id);
	}

	@Length(min=2, max=12, message="姓名长度必须介于 2 和 12 之间")
	@ExcelField(title="姓名", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="头像", align=2, sort=7)
	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
	
	@ExcelField(title="艺名", align=2, sort=8)
	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	@ExcelField(title="地址", align=2, sort=9)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="手机", align=2, sort=10)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Email(message="邮箱必须为合法邮箱")
	@ExcelField(title="邮箱", align=2, sort=11)
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@ExcelField(title="身份证号", align=2, sort=12)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@ExcelField(title="身份证附件", align=2, sort=13)
	public String getIdCardAttach() {
		return idCardAttach;
	}

	public void setIdCardAttach(String idCardAttach) {
		this.idCardAttach = idCardAttach;
	}
	
	@ExcelField(title="作品", align=2, sort=14)
	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}
	
	@ExcelField(title="归属公司", align=2, sort=16)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@ExcelField(title="类型", dictType="musician_type", align=2, sort=17)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="归属公司", align=2, sort=18)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@ExcelField(title="音乐人状态", dictType="musician_status", align=2, sort=19)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public List<YybMusicianAlbum> getYybMusicianAlbumList() {
		return yybMusicianAlbumList;
	}

	public void setYybMusicianAlbumList(List<YybMusicianAlbum> yybMusicianAlbumList) {
		this.yybMusicianAlbumList = yybMusicianAlbumList;
	}
}