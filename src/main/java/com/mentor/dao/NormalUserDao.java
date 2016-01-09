package com.mentor.dao;

import com.mentor.entity.NormalUser;

public interface NormalUserDao {

	long add(NormalUser normalUser);
	
	NormalUser get(Long id);
	
	NormalUser getByExternalNo(Long externalNo);
	
	void update(Long id, Long externalNo, String username, String avatar);
	
	NormalUser getByUsername(String username);
	
	void delete(Long id);
}
