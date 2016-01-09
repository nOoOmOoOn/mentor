package com.mentor.entity;

import javax.persistence.Entity;

import com.mentor.entity.base.BaseArticleVisitor;

@Entity
public class ArticleVisitor extends BaseArticleVisitor{

	public ArticleVisitor(){}
	
	public ArticleVisitor(Long articleId, Long userId) {
		super(articleId, userId);
	}
	
	public void init(){}

	private static final long serialVersionUID = -4132321244838464773L;
}
