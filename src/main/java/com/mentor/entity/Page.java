package com.mentor.entity;

import javax.persistence.Entity;

import com.mentor.entity.base.BasePage;

@Entity
public class Page extends BasePage{

	public Page(){}
	
	public Page(Long articleId, Integer number, String content, Boolean isDraft) {
		super(articleId, number, content, isDraft);
	}
	
	public void init(){}

	private static final long serialVersionUID = -4132321244838464773L;
}
