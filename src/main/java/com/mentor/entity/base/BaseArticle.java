package com.mentor.entity.base;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.common.jdbc.BaseEntity;

@MappedSuperclass
public class BaseArticle extends BaseEntity{

	private static final long serialVersionUID = -4063085597477687360L;
	
	public BaseArticle(){}
	
	public BaseArticle(Long normalUserId, Integer pagesNum, String title, String intro, 
			String cover, String status, Date createdTime, Date publishTime, Integer visitorNum, Boolean isDelete, String menuButton){
		this.setNormalUserId(normalUserId);
		this.setPagesNum(pagesNum);
		this.setTitle(title);
		this.setIntro(intro);
		this.setCover(cover);
		this.setStatus(status);
		this.setCreatedTime(createdTime);
		this.setPublishTime(publishTime);
		this.setVisitorNum(visitorNum);
		this.setIsDelete(isDelete);
		this.setMenuButton(menuButton);
	}

	private Long normalUserId;
	
	private Integer pagesNum;
	
	private String title;
	
	private String intro;
	
	private String cover;
	
	private String status;
	
	private Date createdTime;
	
	private Date publishTime;
	
	private Integer visitorNum;
	
	private Boolean isDelete;
	
	private String menuButton;
	
	public Long getNormalUserId() {
		return normalUserId;
	}

	public void setNormalUserId(Long normalUserId) {
		this.normalUserId = normalUserId;
	}
	
	public Integer getPagesNum() {
		return pagesNum;
	}

	public void setPagesNum(Integer pagesNum) {
		this.pagesNum = pagesNum;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	
	public Integer getVisitorNum() {
		return visitorNum;
	}

	public void setVisitorNum(Integer visitorNum) {
		this.visitorNum = visitorNum;
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	public String getMenuButton() {
		return menuButton;
	}

	public void setMenuButton(String menuButton) {
		this.menuButton = menuButton;
	}
	
	}
