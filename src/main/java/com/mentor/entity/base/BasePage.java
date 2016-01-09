package com.mentor.entity.base;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BasePage extends BaseEntity{

	private static final long serialVersionUID = -4063085597477687360L;
	
	public BasePage(){}
	
	public BasePage(Long articleId, Integer number, String content, Boolean isDraft){
		this.setArticleId(articleId);
		this.setNumber(number);
		this.setContent(content);
		this.setIsDraft(isDraft);
	}

	private Long articleId;
	
	private Integer number;
	
	private String content;
	
	private Boolean isDraft;
	
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	}
