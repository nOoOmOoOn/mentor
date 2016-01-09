package com.mentor.manager;

import com.mentor.entity.NormalUser;


public interface NormalUserMng {
	
	public NormalUser getByUsername(String username);
	
	public NormalUser get(Long id);
	
	public NormalUser getByExternalNo(Long externalNo);
	
	public long add(NormalUser normalUser);

	void update(Long id, Long externalNo, String username, String avatar);
	
	void delete(Long id);

}