package com.data.dao.jdbc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.data.dao.bean.UserBean;
import com.data.dao.generic.GenericJdbcDao;

@Repository
@Qualifier("userDao")
public class UserDao extends GenericJdbcDao<UserBean> implements IUserDao {
	public UserDao() {
		super("user", UserBean.class);
	}
}
