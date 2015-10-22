package com.data.dao.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import com.google.common.base.Joiner;

public class GenericJdbcDao<T> implements IGenericDao<T> {

	private static final Logger logger = Logger.getLogger(GenericJdbcDao.class);

	private static Joiner comma = Joiner.on(",").skipNulls();

	@Autowired
	protected NamedParameterJdbcTemplate namedJdbcTemplate;

	private String table;

	private Class<T> clazz;

	protected GenericJdbcDao(String table, Class<T> clazz) {
		this.table = table;
		this.clazz = clazz;
	}

	@Override
	public int count() {
		String sql = String.format("SELECT count(*) FROM %s", table);
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		return namedJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
	}

	@Override
	public T find(int id) {
		String sql = String.format("SELECT * FROM %s WHERE id=:id", table);
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
		BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(clazz);
		T t;

		try {
			t = namedJdbcTemplate.queryForObject(sql, namedParameters, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return t;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(T t) throws Exception {
		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		for (Field field : t.getClass().getDeclaredFields()) {

			String fieldName = field.getName();
			field.setAccessible(true);
			Object value = field.get(t);

			columns.add(fieldName);
			values.add(String.format(":%s", fieldName));
			namedParameters.addValue(fieldName, value);
		}

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table);
		sql.append(" (").append(comma.join(columns)).append(")");
		sql.append(" VALUES");
		sql.append(" (").append(comma.join(values)).append(")");

		namedJdbcTemplate.update(sql.toString(), namedParameters);
		logger.info(sql);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(T t) throws Exception {
		List<String> columns = new ArrayList<String>();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		for (Field field : t.getClass().getDeclaredFields()) {
			String fieldName = field.getName();
			field.setAccessible(true);
			Object value = field.get(t);

			if (!"id".equals(fieldName)) {
				columns.add(String.format("%s=:%s", fieldName, fieldName));
			}
			namedParameters.addValue(fieldName, value);
		}

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(table);
		sql.append(" SET ").append(comma.join(columns));
		sql.append(" WHERE id=:id");

		namedJdbcTemplate.update(sql.toString(), namedParameters);
		logger.info(sql);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(int id) throws Exception {
		String sql = String.format("DELETE FROM %s WHERE id=:id", table);
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
		namedJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		
		List<T> objList=null;
		String sql = String.format("SELECT * FROM %s", table);
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(clazz);
		//BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource();

		//BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>(clazz);
				try {
			objList = (List<T>) namedJdbcTemplate.query(sql, namedParameters, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return objList;
	}
	
}