package com.mentor.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;

import com.mentor.entity.base.BaseArticle;

@Entity
public class Article extends BaseArticle{

	public Article(){}
	
	public Article(Long normalUserId, Integer pagesNum, String title, String intro, 
			String cover, String status, Date createdTime, Date publishTime, Integer visitorNum, Boolean isDelete, String menuButton) {
		super(normalUserId, pagesNum, title, intro, cover, status, createdTime, publishTime, visitorNum, isDelete, menuButton);
	}
	
	public void init(){
		if(getPagesNum()==null){
			setPagesNum(1);
		}
		if(getVisitorNum()==null){
			setVisitorNum(0);
		}
		if(getCreatedTime()==null){
			setCreatedTime(new Timestamp(System.currentTimeMillis()));
		}
		if(getMenuButton()==null){
			setMenuButton(Article.menuButton.分享.toString());
		}
		
		setIsDelete(false);
	}

	public enum PublishStatus {
		未发布, 已发布, 修改未发布
	}
	
	public enum menuButton {
		分享, 导购
	}
	
	private static final long serialVersionUID = -4132321244838464773L;
}
