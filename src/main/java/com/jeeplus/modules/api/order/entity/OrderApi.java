/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.api.order.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单Entity
 * @author lwb
 * @version 2019-02-20
 */
public class OrderApi extends DataEntity<OrderApi> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private String tradeNo;		// 交易流水号
	private Integer status;		// 状态 1未支付 2已取消 3已支付
	private Date orderTime;		// 下单时间
	private Date payTime;		// 支付时间
	private Integer payType;		// 支付类型
	private Double orderAmount;		// 订单金额
	private Double payAmount;		// 支付金额
	private String memberId;		// 会员
	private String memberName;		// 购买人
	private String memberAddress;		// 地址
	private String idCard;		// 身份证
	private String idCardAttach;		// 身份证附件
	private String orgCode;		// 组织机构
	private String orgCodeAttach;		// 组织机构附件
	private Integer memberType;		// 用户类型
	private String phone;		// 手机号
	private String companyId;
	private String companyName;
	private String musicianName;
	private String albumName;
	private Integer memberSex;
	private List<OrderDeatilApi> detailList = Lists.newArrayList();		// 子表列表
	
	public OrderApi() {
		super();
	}

	public OrderApi(String id){
		super(id);
	}

	@ExcelField(title="订单号", align=2, sort=6)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="交易流水号", align=2, sort=7)
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	@ExcelField(title="状态", dictType="order_status", align=2, sort=8)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="下单时间", align=2, sort=9)
	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="支付时间", align=2, sort=10)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@ExcelField(title="支付类型", dictType="pay_type", align=2, sort=11)
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	@ExcelField(title="订单金额", align=2, sort=12)
	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	@ExcelField(title="支付金额", align=2, sort=13)
	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	
	@ExcelField(title="会员", align=2, sort=14)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@ExcelField(title="购买人", align=2, sort=15)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	@ExcelField(title="地址", align=2, sort=16)
	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}
	
	@ExcelField(title="身份证", align=2, sort=17)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@ExcelField(title="身份证附件", align=2, sort=18)
	public String getIdCardAttach() {
		return idCardAttach;
	}

	public void setIdCardAttach(String idCardAttach) {
		this.idCardAttach = idCardAttach;
	}
	
	@ExcelField(title="组织机构", align=2, sort=19)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	@ExcelField(title="组织机构附件", align=2, sort=20)
	public String getOrgCodeAttach() {
		return orgCodeAttach;
	}

	public void setOrgCodeAttach(String orgCodeAttach) {
		this.orgCodeAttach = orgCodeAttach;
	}
	
	@ExcelField(title="用户类型", dictType="user_type", align=2, sort=21)
	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	
	@ExcelField(title="手机号", align=2, sort=22)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<OrderDeatilApi> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<OrderDeatilApi> detailList) {
		this.detailList = detailList;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(Integer memberSex) {
		this.memberSex = memberSex;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMusicianName() {
		return musicianName;
	}

	public void setMusicianName(String musicianName) {
		this.musicianName = musicianName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
}