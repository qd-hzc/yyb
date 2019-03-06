/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.member.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;

/**
 * 会员Entity
 * @author lwb
 * @version 2019-02-18
 */
public class ApiMember {
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String remarks;	// 备注
	private String createBy;	// 创建者
	private Date createDate;	// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;	// 更新日期
	private String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
	private String name;		// 名称
	private String phone;		// 手机号
	private Integer type;		// 用户类型
	private Integer sex;		// 性别
	private String idCard;		// 身份证
	private String idCardAttach;		// 身份证附件
	private String address;		// 居住地址
	private String orgCode;		// 机构代码
	private String orgCodeAttach;		// 营业执照
	private Integer status;		// 状态
	private String remark;		// 备注信息
	private String password;		// 密码
	private String token;		// token


	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="手机号", align=2, sort=7)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="用户类型", dictType="user_type", align=2, sort=8)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=9)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="身份证", align=2, sort=10)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@ExcelField(title="身份证附件", align=2, sort=11)
	public String getIdCardAttach() {
		return idCardAttach;
	}

	public void setIdCardAttach(String idCardAttach) {
		this.idCardAttach = idCardAttach;
	}
	
	@ExcelField(title="居住地址", align=2, sort=12)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="机构代码", align=2, sort=13)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	@ExcelField(title="营业执照", align=2, sort=14)
	public String getOrgCodeAttach() {
		return orgCodeAttach;
	}

	public void setOrgCodeAttach(String orgCodeAttach) {
		this.orgCodeAttach = orgCodeAttach;
	}
	
	@ExcelField(title="状态", dictType="user_status", align=2, sort=15)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="备注信息", align=2, sort=16)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}