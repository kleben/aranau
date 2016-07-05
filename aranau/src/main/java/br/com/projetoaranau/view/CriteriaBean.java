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
	private Collection<Criteria> listCriteria;
	private Criteria criteria = new Criteria();
	private String value;
	
	
	public CriteriaBean() {
		super();
		
		
		listCriteria = new ArrayList<Criteria>();
		
		criteria = new Criteria();
		criteria.setName("A. Segurança");
		criteria.getValues().add( new String("A1. Alta") );
		criteria.getValues().add( new String("A2. Média") );
		criteria.getValues().add( new String("A3. Baixa") );
		listCriteria.add( criteria );
		
		criteria = new Criteria();
		criteria.setName("B. Estética");
		criteria.getValues().add( new String("B1. Muito bonito") );
		criteria.getValues().add( new String("B2. Beleza média") );
		criteria.getValues().add( new String("B3. Feio") );
		listCriteria.add( criteria );

		criteria = new Criteria();
		criteria.setName("C. Motorizacao");
		criteria.getValues().add( new String("C1. Potente") );
		criteria.getValues().add( new String("C2. Potencia média") );
		criteria.getValues().add( new String("C3. Potencia baixa") );
		listCriteria.add( criteria );
		
		for (Criteria criteria : listCriteria) {		
			zapros.addCriterion(criteria.getName(), criteria.getValues());
		}
		
	}

	public String criteria() {
		return "criteria/criteria";
	}
	
	public String linkAddNewCriteria() {
		criteria = new Criteria();
		value = "";
		return "criteria-new?faces-redirect=true";
	}
	
	public void newCriteria(){
		if (listCriteria == null)
			listCriteria = new ArrayList<Criteria>();
		listCriteria.add( new Criteria(criteria) );
        FacesMessage message = new FacesMessage("", "Critério adicionado. Adicione os valores.");
        //message.setSeverity(FacesMessage.SEVERITY_FATAL);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public String save() {
		for (Criteria criteria : listCriteria) {		
			zapros.addCriterion(criteria.getName(), criteria.getValues());
		}
		
		FacesMessage message = new FacesMessage("", "Critério salvo com sucesso");
		FacesContext.getCurrentInstance().addMessage(null, message);
		return "criteria";
	}
	
	public void addValue(){
		this.criteria.getValues().add( new String(this.value) );
		this.value = "";
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
