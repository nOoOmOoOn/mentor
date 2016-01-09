package com.mentor.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.mentor.dao.ArticleDao;
import com.mentor.entity.Article;

@Repository
public class ArticleDaoImpl extends JdbcTemplateBaseDao implements ArticleDao {

	@Override
	protected Class<?> getEntityClass() {
		return Article.class;
	}
	
	public long add(Article article) {
		return super.add(article);
	}

	public Article get(Long id) {
		return super.queryForObject(id);
	}
	
	public List<Article> getByUser(Long userId) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from Article where 1 = 1");
		sqlBuilder.andEqualTo("isDelete", false);
		if (userId != null) {
			sqlBuilder.andEqualTo("userId", userId);
		}
		return query(sqlBuilder);
	}

	public void update(Long id, String status, String title, String intro, String cover, Date publishTime, Integer visitorNum) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update Article set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(status)) {
			sqlBuilder.set("status", status);
		}
		if (sqlBuilder.ifNotNull(title)) {
			sqlBuilder.set("title", title);
		}
		if (sqlBuilder.ifNotNull(intro)) {
			sqlBuilder.set("intro", intro);
		}
		if (sqlBuilder.ifNotNull(cover)) {
			sqlBuilder.set("cover", cover);
		}
		if (sqlBuilder.ifNotNull(publishTime)) {
			sqlBuilder.set("publishTime", publishTime);
		}
		if (sqlBuilder.ifNotNull(visitorNum)) {
			sqlBuilder.set("visitorNum", visitorNum);
		}
		super.update(id, sqlBuilder);
	}

	public void softDelete(Long id) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update Article set gmtModify=current_timestamp()");
		sqlBuilder.set("isDelete", true);
		super.update(id, sqlBuilder);
	}
	
	public void delete(Long id) {
		super.delete(id);
	}

	
}
