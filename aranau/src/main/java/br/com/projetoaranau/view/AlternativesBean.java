package br.com.projetoaranau.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UISelectOne;
import javax.swing.DefaultListModel;

import br.com.projetoaranau.zapros_III.Criterion;

@SessionScoped
@ManagedBean(name = "alternativesBean")
public class AlternativesBean extends GenericBean{
	
	private static final long serialVersionUID = 3131547833151764385L;
	private String alternativeName;
	private HashMap<Integer,UISelectOne> listSelectOfAlternatives = new HashMap<Integer,UISelectOne>();
	private HashMap<Integer,String> listAlternativesSelected = new HashMap<Integer,String>();
	private List<String> listAlternativesSelected2 = new ArrayList<String>();
	private String[] arrayValores;
	public String alternativaSelecionada;
	
	private List<Criterion> listCriteria = new ArrayList<Criterion>();
	private int numberOfCriterias;
	private String alternativeSelected;
	
	
	public AlternativesBean() {
		super();
	}
	
	@PostConstruct
	public void init(){
		
		if (listCriteria.isEmpty())
		{
			numberOfCriterias = zapros.getCriteria().size();
			for(int i = 0; i<numberOfCriterias; i++){
				Criterion c = (Criterion)zapros.getCriteria().get(i);
				listCriteria.add(c);
				c.getDescription();
				listSelectOfAlternatives.put(new Integer(i), new UISelectOne());
				listAlternativesSelected.put(new Integer(i), new String());
				listAlternativesSelected2.add(new String());
			}
			arrayValores = new String[numberOfCriterias];
		}
	}
	
    public void save() {
    	DefaultListModel listaValoresAlt = new DefaultListModel();
    	for(int i = 0; i<numberOfCriterias; i++){
    		ArrayList listaCriterios = zapros.getCriteria();
    		String value = new String();
    		int numberValues = ((Criterion)listaCriterios.get(i)).getValues().size();
    		int selectedAlternative = 0;
    		
    		for(int j = 0; j<numberValues; j++){
    			value = ((Criterion)listaCriterios.get(i)).getValues().get(j);
    			
    			if (arrayValores[i].equals(value))
    				selectedAlternative = j+1;
    		}
			String valor = ((Criterion)listaCriterios.get(i)).getCode() + "/" +selectedAlternative;
			System.out.println("Valor: "+valor);
			listaValoresAlt.add(i, valor);
		}
    	
    	zapros.addAlternative(alternativeName, listaValoresAlt);
    }
    
    public void setAlternative(int index){
    	System.out.println(index);
    }
    
    public String voltar(){
    	
    	return "/views/index.xhtml?faces-redirect=true";
    }
    
    
	public String getAlternativeName() {
		return alternativeName;
	}

	public void setAlternativeName(String alternativeName) {
		this.alternativeName = alternativeName;
	}

	public String getAlternativeSelected() {
		return alternativeSelected;
	}

	public void setAlternativeSelected(String alternativeSelected) {
		this.alternativeSelected = alternativeSelected;
	}

	public int getNumberOfCriterias() {
		return numberOfCriterias;
	}

	public void setNumberOfCriterias(int numberOfCriterias) {
		this.numberOfCriterias = numberOfCriterias;
	}

	public List<Criterion> getListCriteria() {
		return listCriteria;
	}

	public void setListCriteria(List<Criterion> listCriteria) {
		this.listCriteria = listCriteria;
	}

	public HashMap<Integer,UISelectOne> getListSelectOfAlternatives() {
		return listSelectOfAlternatives;
	}

	public void setListSelectOfAlternatives(HashMap<Integer,UISelectOne> listSelectOfAlternatives) {
		this.listSelectOfAlternatives = listSelectOfAlternatives;
	}

	public HashMap<Integer,String> getListAlternativesSelected() {
		return listAlternativesSelected;
	}

	public void setListAlternativesSelected(HashMap<Integer,String> listAlternativesSelected) {
		this.listAlternativesSelected = listAlternativesSelected;
	}

	public List<String> getListAlternativesSelected2() {
		return listAlternativesSelected2;
	}

	public void setListAlternativesSelected2(List<String> listAlternativesSelected2) {
		this.listAlternativesSelected2 = listAlternativesSelected2;
	}

	public String[] getArrayValores() {
		return arrayValores;
	}

	public void setArrayValores(String[] arrayValores) {
		this.arrayValores = arrayValores;
	}

	public String getAlternativaSelecionada() {
		return alternativaSelecionada;
	}

	public void setAlternativaSelecionada(String alternativaSelecionada) {
		this.alternativaSelecionada = alternativaSelecionada;
	}
	
}
