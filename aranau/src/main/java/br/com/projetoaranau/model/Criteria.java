package br.com.projetoaranau.model;

import java.util.ArrayList;
import java.util.List;

public class Criteria {
	
	private Long id;
	private String name;
	private List<String> values = new ArrayList<String>();
	

	public Criteria(){
		super();
	}
	
	public Criteria(Criteria criteria){
		this.name = criteria.getName();
		this.values = criteria.getValues();
	}
	
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

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
