package br.com.projetoaranau.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.projetoaranau.zapros_III.ZaprosMethod;

@SessionScoped
@ManagedBean(name = "problemBean")
public class ProblemBean implements Serializable{

	private static final long serialVersionUID = 5996466266139116836L;
	private static ZaprosMethod zapros;
	
	
	@PostConstruct
	public void init(){
		zapros = ZaprosMethod.getInstance();
		
		if(zapros == null || zapros.getProblemName()==null){
			zapros.getInstance().setProblemName("Análise de carro");
//			
//			List<String> listaValores = new ArrayList<String>();
//			listaValores.add("A1. Alta");
//			listaValores.add("A2. Média");
//			listaValores.add("A3. Baixa");
//			zapros.addCriterion("A. Segurança", listaValores);
//			
//			List<String> listaValores2 = new ArrayList<String>();
//			listaValores2.add("B1. Muito bonito");
//			listaValores2.add("B2. Beleza média");
//			listaValores2.add("B3. Feio");
//			zapros.addCriterion("B. Estetica", listaValores2);
//
//			List<String> listaValores3 = new ArrayList<String>();
//			listaValores3.add("C1. Potente");
//			listaValores3.add("C2. Potencia média");
//			listaValores3.add("C3. Potencia baixa");
//			zapros.addCriterion("C. Motorizacao", listaValores3);
		}
	}

}
