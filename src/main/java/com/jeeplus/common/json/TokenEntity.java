package com.jeeplus.common.json;

import com.jeeplus.modules.member.entity.YybMember;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户Token
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public class TokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private YybMember member;
	//token
	private String token;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;

	public YybMember getMember() {
		return member;
	}

	public void setMember(YybMember member) {
		this.member = member;
	}

	/**
	 * 设置：token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 获取：token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 设置：过期时间
	 */
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	/**
	 * 获取：过期时间
	 */
	public Date getExpireTime() {
		return expireTime;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
