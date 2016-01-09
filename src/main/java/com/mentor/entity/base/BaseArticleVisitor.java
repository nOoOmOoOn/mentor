package com.mentor.entity.base;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BaseArticleVisitor extends BaseEntity{

	private static final long serialVersionUID = -4063085597477687360L;
	
	public BaseArticleVisitor(){}
	
	public BaseArticleVisitor(Long articleId, Long normalUserId){
		this.setArticleId(articleId);
		this.setNormalUserId(normalUserId);
	}

	private Long articleId;
	
	private Long normalUserId;
	
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	public Long getNormalUserId() {
		return normalUserId;
	}

	public void setNormalUserId(Long normalUserId) {
		this.normalUserId = normalUserId;
	}
	
	}
