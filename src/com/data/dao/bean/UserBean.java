package com.data.dao.bean;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserBean {

	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("UserBean [id=%s, name=%s, email=%s]", id, name, email);
	}
}
