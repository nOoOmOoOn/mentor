package com.mentor.dao.impl;

import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.mentor.dao.NormalUserDao;
import com.mentor.entity.NormalUser;

@Repository
public class NormalUserDaoImpl extends JdbcTemplateBaseDao implements NormalUserDao {

	public long add(NormalUser normalUser) {
		return super.add(normalUser);
	}

	public NormalUser get(Long id) {
		return super.queryForObject(id);
	}
	
	public NormalUser getByExternalNo(Long externalNo) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"select * from NormalUser where 1=1");
		if (externalNo != null) {
			sqlBuilder.andEqualTo("externalNo", externalNo);
		}
		return queryForObject(sqlBuilder);
	}

	public void update(Long id, Long externalNo, String username,
			String avatar) {
		SqlBuilder sqlBuilder = new SqlBuilder(
				"update NormalUser set gmtModify=current_timestamp()");
		if (sqlBuilder.ifNotNull(externalNo)) {
			sqlBuilder.set("externalNo", externalNo);
		}
		if (sqlBuilder.ifNotNull(username)) {
			sqlBuilder.set("username", username);
		}
		if (sqlBuilder.ifNotNull(avatar)) {
			sqlBuilder.set("avatar", avatar);
		}
		super.update(id, sqlBuilder);
	}

	@Override
	protected Class<?> getEntityClass() {
		return NormalUser.class;
	}

	public NormalUser getByUsername(String username) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from NormalUser where 1=1");
		if (sqlBuilder.ifNotNull(username)) {
			sqlBuilder.andEqualTo("username", username);
		}
		return super.queryForObject(sqlBuilder);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	
}
