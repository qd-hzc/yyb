package com.jeeplus.modules.api.order.entity;


import com.jeeplus.common.utils.base.annotation.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class YybOrderVo implements Serializable {

    @NotEmpty(message = "购物车不能为空")
    private List<String> shopcartIds;

    @NotNull
    @DecimalMin(value = "0.01", message = "订单金额有误")
    private BigDecimal orderAmount;

    @NotNull
    @Min(value = 1, message = "用户类型值有误")
    @Max(value = 2, message = "用户类型值有误")
    private Integer memberType;//1个人 2公司

    @NotEmpty(message = "姓名不能为空")
    private String memberName;		// 购买人姓名

    private Integer memberSex;  //1男 2女

    @NotEmpty(message = "地址不能为空")
    private String memberAddress;		// 地址

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式有误")
    private String phone;		// 手机号

    @Pattern(regexp = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$", message = "身份证号格式有误")
    private String idCard;		// 身份证

    private String idCardAttach;		// 身份证附件

    private String orgCode;		// 组织机构

    private String orgCodeAttach;		// 组织机构附件

    public List<String> getShopcartIds() {
        return shopcartIds;
    }

    public void setShopcartIds(List<String> shopcartIds) {
        this.shopcartIds = shopcartIds;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(Integer memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCodeAttach() {
        return orgCodeAttach;
    }

    public void setOrgCodeAttach(String orgCodeAttach) {
        this.orgCodeAttach = orgCodeAttach;
    }
}
