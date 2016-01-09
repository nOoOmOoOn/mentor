package com.mentor.dao;

import java.util.Date;

import com.common.jdbc.page.Pagination;
import com.mentor.entity.Authentication;


public interface AuthenticationDao {

	public int deleteExpire(Date d);

	public Authentication getByUserId(Long userId);

	public Pagination getPage(int pageNo, int pageSize);

	public Authentication findById(String id);

	public void save(Authentication bean);

	public void update(String id,Date updateTime);

	public Authentication deleteById(String id);
}