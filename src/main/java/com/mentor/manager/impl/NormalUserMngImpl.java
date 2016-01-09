package com.mentor.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentor.dao.NormalUserDao;
import com.mentor.entity.NormalUser;
import com.mentor.manager.NormalUserMng;
import com.mentor.manager.TreeMng;
import com.mentor.manager.UnifiedUserMng;
import com.wechat.manager.WechatUserMng;

@Service
@Transactional
public class NormalUserMngImpl implements NormalUserMng {

	public NormalUser getByUsername(String username) {
		return dao.getByUsername(username);
	}

	public NormalUser get(Long id) {
		NormalUser entity = dao.get(id);
		return entity;
	}
	
	public NormalUser getByExternalNo(Long externalNo) {
		NormalUser entity = dao.getByExternalNo(externalNo);
		return entity;
	}
	
	public long add(NormalUser normalUser) {
		return dao.add(normalUser);
	}
	
	public void update(Long id, Long externalNo, String username, String avatar) {
		dao.update(id, externalNo, username, avatar);
	}
	
	public void delete(Long id) {
		dao.delete(id);
	}

	@Autowired
	private TreeMng treeMng;
	@Autowired
	private NormalUserDao dao;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private WechatUserMng wechatUserMng;
}