package com.mentor.entity.base;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BaseArticleVisitor extends BaseEntity{

	private static final long serialVersionUID = -4063085597477687360L;
	
	public BaseArticleVisitor(){}
	
	public BaseArticleVisitor(Long articleId, Long userId){
		this.setArticleId(articleId);
		this.setUserId(userId);
	}

	private Long articleId;
	
	private Long userId;
	
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	}
