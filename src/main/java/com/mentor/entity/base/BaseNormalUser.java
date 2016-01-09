package com.mentor.entity.base;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BaseNormalUser extends BaseEntity{

	private static final long serialVersionUID = -4063085597477687360L;
	
	public BaseNormalUser(){}
	
	public BaseNormalUser(Long externalNo, String username, String avatar, Long fatherId){
		this.setExternalNo(externalNo);
		this.setUsername(username);
		this.setAvatar(avatar);
		this.setExternalNo(fatherId);
	}

	private Long externalNo;
	
	private String username;
	
	private String avatar;
	
	/**
	 * 记录的为unifiedUser的ID
	 */
	private Long fatherId;
	
	public Long getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(Long externalNo) {
		this.externalNo = externalNo;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Long getFatherId() {
		return fatherId;
	}

	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}

	}
