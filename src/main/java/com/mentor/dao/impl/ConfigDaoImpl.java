package com.mentor.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.common.jdbc.JdbcTemplateBaseDao;
import com.common.jdbc.SqlBuilder;
import com.mentor.dao.ConfigDao;
import com.mentor.entity.Config;

@Repository
public class ConfigDaoImpl extends JdbcTemplateBaseDao implements ConfigDao {

	public List<Config> getList() {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from Config");
		return getJdbcTemplate().query(sqlBuilder.toString(),
				sqlBuilder.toParamArray(), new RowMapper<Config>() {
					@Override
					public Config mapRow(ResultSet arg0, int arg1)
							throws SQLException {
						Config bean = new Config();
						bean.setId(arg0.getString("configId"));
						bean.setValue(arg0.getString("value"));
						return bean;
					}
				});
	}

	public Config findById(String id) {
		SqlBuilder sqlBuilder = new SqlBuilder("select * from Config where 1=1");
		if (id != null) {
			sqlBuilder.andEqualTo("configId", id);
		}
		return (Config) getJdbcTemplate().queryForObject(sqlBuilder.toString(),
				sqlBuilder.toParamArray(), new RowMapper<Config>() {
					@Override
					public Config mapRow(ResultSet arg0, int arg1)
							throws SQLException {
						Config bean = new Config();
						bean.setId(arg0.getString("configId"));
						bean.setValue(arg0.getString("value"));
						return bean;
					}
				});
	}

	public Config save(Config bean) {
		super.getJdbcTemplate().update(
				"insert into Config (configId, value) values (?, ?)",
				bean.getId(), bean.getValue());
		return bean;
	}

	public Config deleteById(String id) {
		Config entity = findById(id);
		if (entity != null) {
			SqlBuilder sqlBuilder = new SqlBuilder(
					"delete from Config where 1=1");
			if (id != null) {
				sqlBuilder.andEqualTo("configId", id);
			}
			update(sqlBuilder);
		}
		return entity;
	}

	@Override
	protected Class<Config> getEntityClass() {
		return Config.class;
	}

	@Override
	public void update(String id, String value) {
		getJdbcTemplate().update("update Config set value=? where configId=?",
				new Object[] { value, id });
	}
}