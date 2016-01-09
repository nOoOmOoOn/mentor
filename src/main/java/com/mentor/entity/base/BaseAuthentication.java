package com.mentor.entity.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseAuthentication implements Serializable {

	private static final long serialVersionUID = 595135016528076901L;

	public BaseAuthentication(){}
	
	public BaseAuthentication(String id,Long uid, String username, String email,
			Date loginTime, String loginIp, Date updateTime) {
		this.setId(id);
		this.setUid(uid);
		this.setUsername(username);
		this.setEmail(email);
		this.setLoginTime(loginTime);
		this.setLoginIp(loginIp);
		this.setUpdateTime(updateTime);
	}

	// primary key
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "authId", unique = true, nullable = false, insertable = true, updatable = false)
	private java.lang.String id;
	
	// fields
	private java.lang.Long uid;
	private java.lang.String username;
	private java.lang.String email;
	private java.util.Date loginTime;
	private java.lang.String loginIp;
	private java.util.Date updateTime;

	public java.lang.Long getUid() {
		return uid;
	}

	public void setUid(java.lang.Long uid) {
		this.uid = uid;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.util.Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}

	public java.lang.String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(java.lang.String loginIp) {
		this.loginIp = loginIp;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}
}
