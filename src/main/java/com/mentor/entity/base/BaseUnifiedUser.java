package com.mentor.entity.base;

import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

/**
 * 
 * 
 * 描述:统一用户
 * 
 * @author huangpengpeng
 * @version 1.0
 * @since 2013年9月6日 上午9:35:58
 */
@MappedSuperclass
public class BaseUnifiedUser extends BaseEntity {

	private static final long serialVersionUID = -4519183386307646257L;
	
	public BaseUnifiedUser(){}

	public BaseUnifiedUser(String username, String email,String phone, String password,
			Date registerTime, String registerIp, Date lastLoginTime,
			String lastLoginIp, Integer loginCount, String resetKey,
			String resetPwd, Date errorTime, Integer errorCount,
			String errorIp, Boolean activation, String activationCode,String wechat) {
		this.setUsername(username);
		this.setEmail(email);
		this.setPhone(phone);
		this.setPassword(password);
		this.setRegisterTime(registerTime);
		this.setRegisterIp(registerIp);
		this.setLoginCount(loginCount);
		this.setResetKey(resetKey);
		this.setResetPwd(resetPwd);
		this.setErrorTime(errorTime);
		this.setErrorCount(errorCount);
		this.setErrorIp(errorIp);
		this.setActivation(activation);
		this.setActivationCode(activationCode);
		this.setWechat(wechat);
	}

	@JoinColumn(unique=true)
	private java.lang.String username;
	private java.lang.String email;
	@JoinColumn(unique=true)
	private java.lang.String phone;
	private java.lang.String password;
	private java.util.Date registerTime;
	private java.lang.String registerIp;
	private java.util.Date lastLoginTime;
	private java.lang.String lastLoginIp;
	private java.lang.Integer loginCount;
	private java.lang.String resetKey;
	private java.lang.String resetPwd;
	private java.util.Date errorTime;
	private java.lang.Integer errorCount;
	private java.lang.String errorIp;
	private java.lang.Boolean activation;
	private java.lang.String activationCode;
	private java.lang.Boolean disabled;
	@JoinColumn(unique=true)
	private java.lang.String wechat;

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

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.util.Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(java.util.Date registerTime) {
		this.registerTime = registerTime;
	}

	public java.lang.String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(java.lang.String registerIp) {
		this.registerIp = registerIp;
	}

	public java.util.Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public java.lang.String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(java.lang.String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public java.lang.Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(java.lang.Integer loginCount) {
		this.loginCount = loginCount;
	}

	public java.lang.String getResetKey() {
		return resetKey;
	}

	public void setResetKey(java.lang.String resetKey) {
		this.resetKey = resetKey;
	}

	public java.lang.String getResetPwd() {
		return resetPwd;
	}

	public void setResetPwd(java.lang.String resetPwd) {
		this.resetPwd = resetPwd;
	}

	public java.util.Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(java.util.Date errorTime) {
		this.errorTime = errorTime;
	}

	public java.lang.Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(java.lang.Integer errorCount) {
		this.errorCount = errorCount;
	}

	public java.lang.String getErrorIp() {
		return errorIp;
	}

	public void setErrorIp(java.lang.String errorIp) {
		this.errorIp = errorIp;
	}

	public java.lang.Boolean getActivation() {
		return activation;
	}

	public void setActivation(java.lang.Boolean activation) {
		this.activation = activation;
	}

	public java.lang.String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(java.lang.String activationCode) {
		this.activationCode = activationCode;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.lang.String getWechat() {
		return wechat;
	}
	

	public java.lang.Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(java.lang.Boolean disabled) {
		this.disabled = disabled;
	}

	public void setWechat(java.lang.String wechat) {
		this.wechat = wechat;
	}
}
