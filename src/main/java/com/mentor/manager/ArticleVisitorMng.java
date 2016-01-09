package com.mentor.manager;

import com.mentor.entity.ArticleVisitor;

public interface ArticleVisitorMng {
	
	ArticleVisitor get(Long id);
	
	ArticleVisitor getByDetails(Long articleId, Long normalUserId);
	
	void add(Long articleId, Long normalUserId);
	
}