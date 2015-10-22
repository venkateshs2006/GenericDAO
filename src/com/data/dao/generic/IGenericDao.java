package com.data.dao.generic;

import java.util.List;

public interface IGenericDao<T> {
	int count();

	T find(int id);
	
	List<T> findAll();

	void insert(T t) throws Exception;

	void update(T t) throws Exception;

	void delete(int id) throws Exception;
}
