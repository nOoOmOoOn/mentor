package com.mentor.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.mentor.dao.PageDao;
import com.mentor.entity.Page;

@Repository
public class PageDaoImpl extends JdbcTemplateBaseDao implements PageDao {

	@Override
	protected Class<?> getEntityClass() {
		return Page.class;
	}
	
	public long add(Page page) {
		return super.add(page);
	}

	public Page get(Long id) {
		return super.queryForObject(id);
	}
	
	public List<Page> getByArticle(Long articleId, Boolean isDraft) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from Page where 1=1");
		if (articleId != null) {
			sqlBuilder.andEqualTo("articleId", articleId);
		}
		if (isDraft != null) {
			sqlBuilder.andEqualTo("isDraft", isDraft);
		}
		return query(sqlBuilder);
	}

	public void update(Long id, String content, Integer number, Boolean isDraft) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update Page set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(content)) {
			sqlBuilder.set("content", content);
		}
		if (sqlBuilder.ifNotNull(number)) {
			sqlBuilder.set("number", number);
		}
		if (sqlBuilder.ifNotNull(isDraft)) {
			sqlBuilder.set("isDraft", isDraft);
		}
		super.update(id, sqlBuilder);
	}

	public void delete(Long id) {
		super.delete(id);
	}
	
}
