package br.com.projetoaranau.model;

import java.util.Collection;

public class Criteria {
	
	private Long id;
	private String name;
	private Collection<String> values;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<String> getValues() {
		return values;
	}

	public void setValues(Collection<String> value) {
		this.values = value;
	}

}