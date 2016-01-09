package com.mentor.dao;

import com.mentor.entity.ArticleVisitor;

public interface ArticleVisitorDao {

	long add(ArticleVisitor articleVisitor);
	
	ArticleVisitor get(Long id);
	
	ArticleVisitor getByDetails(Long articleId, Long userId);
}
