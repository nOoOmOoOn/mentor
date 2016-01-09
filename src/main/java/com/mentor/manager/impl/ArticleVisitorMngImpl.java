package com.mentor.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentor.dao.ArticleVisitorDao;
import com.mentor.entity.ArticleVisitor;
import com.mentor.manager.ArticleVisitorMng;

@Service
@Transactional
public class ArticleVisitorMngImpl implements ArticleVisitorMng {

	public ArticleVisitor get(Long id) {
		ArticleVisitor entity = dao.get(id);
		return entity;
	}
	
	public ArticleVisitor getByDetails(Long articleId, Long userId) {
		return dao.getByDetails(articleId, userId);
	}

	public void add(Long articleId, Long userId) {
		ArticleVisitor articleVisitor = new ArticleVisitor();
		articleVisitor.setArticleId(articleId);
		articleVisitor.setUserId(userId);
		
		dao.add(articleVisitor); 
	}
	
	@Autowired
	private ArticleVisitorDao dao;

}