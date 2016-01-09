package com.mentor.entity;

import javax.persistence.Entity;

import com.mentor.entity.base.BaseNormalUser;

@Entity
public class NormalUser extends BaseNormalUser{

	public NormalUser(){}
	
	public NormalUser(Long externalNo, String username, String avatar, Long fatherId) {
		super(externalNo, username, avatar, fatherId);
	}
	
	public void init(){}

	private static final long serialVersionUID = -4132321244838464773L;
}
