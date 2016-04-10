package br.com.projetoaranau.view;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.projetoaranau.model.Criteria;

@SessionScoped
@ManagedBean(name = "criteriaBean")
public class CriteriaBean extends GenericBean {
	
	private static final long serialVersionUID = 4233571693095444330L;
	private Collection<Criteria> listCriteria = new ArrayList<Criteria>();
	private Criteria criteria = new Criteria();
	private String value;

	
	public CriteriaBean() {
		super();
	}

	public String criteria() {
		return "criteria/criteria";
	}
	
	public String addNewCriteria() {
		criteria = new Criteria();
		return "criteria/critera-new?faces-redirect=true";
	}
	
	public void save() {
		listCriteria.add(criteria);
		criteria.setValues( new ArrayList<String>() );
		//criteria = new Criteria();
		
		FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		message.setSummary("Critério salvo com sucesso!");
		FacesContext.getCurrentInstance().addMessage("defaultForm", message);
	}
	
	public void addValue(){
		this.criteria.getValues().add(this.value);
	}

	public Collection<Criteria> getListCriteria() {
		return listCriteria;
	}

	public void setListCriteria(Collection<Criteria> listCriteria) {
		this.listCriteria = listCriteria;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
