package com.data.dao.main;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.data.dao.bean.UserBean;
import com.data.dao.jdbc.IUserDao;

public class App {
	private static final Logger logger = Logger.getLogger(App.class);
	private static ApplicationContext context = new ClassPathXmlApplicationContext(
			"spring-data.xml");

	public static void main(String[] args) throws Exception {
		IUserDao userDao = (IUserDao) context.getBean("userDao");

		UserBean userBean = new UserBean();
		userBean.setName("kite");
		userBean.setEmail("xxx@gmail.com");

		userDao.insert(userBean);
		logger.info(String.format("count:%s", userDao.count()));

		UserBean userDbBean = userDao.find(1);
		logger.info(userDbBean == null ? null : userDbBean.toString());

		userBean.setId(1);
		userBean.setEmail("yyy@gmail.com");
		userDao.update(userBean);
		List<UserBean> userDbBeanList = userDao.findAll();
		logger.info(userDbBeanList == null ? null : userDbBeanList.toString());

		//userDao.delete(1);
		logger.info(String.format("count:%s", userDao.count()));
	}
}