package br.com.projetoaranau.zapros_III;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class Combinacao {

	private static int quantidadeCriterios;
	private static int totalAlternativas;
	private static String [] valoresCriterios;
	private static String [] combinacoes;
	private static Alternative alternativa;
	private static ArrayList<Alternative> listaAlternativasFicticias;
	private static ZaprosMethod zapros;

	public Combinacao() {
		zapros = ZaprosMethod.getInstance();

		//Inicializar o array valores com numero total de valores de crit�rios
		quantidadeCriterios = zapros.getCriteria().size();//por exemplo, 3
		//"1A1", "1A2", "1A3", "2B1", "2B2", "2B3", "3C1", "3C2", "3C3"
		int qtdeValoresCriterios = 0;
		for (int i = 0; i<quantidadeCriterios;i++){
			Criterion c = (Criterion)zapros.getCriteria().get(i);
			qtdeValoresCriterios += c.getQualitativeValues().size();
		}
		valoresCriterios = new String[qtdeValoresCriterios];

		//Inicializar o array valores com os c�digos dos valores de crit�rios
		int count = 0;
		for (int i = 0; i<quantidadeCriterios;i++){
			Criterion c = (Criterion)zapros.getCriteria().get(i);
			for(int j = 0; j<c.getQualitativeValues().size();j++){
				valoresCriterios[count] = c.getCode()+"/"+ (j+1);
				count++;
			}
		}

		listaAlternativasFicticias = new ArrayList<Alternative>();
		totalAlternativas = 1;
		combinacoes  = new String[quantidadeCriterios];
		gerarCombinacoes(0);
		zapros.setPossibleAlternatives(listaAlternativasFicticias);
	}

    private static void gerarCombinacoes(int index){
        if (quantidadeCriterios > index){
            for(int i = index; i + quantidadeCriterios - index <= valoresCriterios.length ; i++){
            	 combinacoes[index] = valoresCriterios[i];
	             gerarCombinacoes(index+1);
            }
            return;
        } else if(quantidadeCriterios == index) {
        	String aux = "";
        	String aux2 = "";
        	boolean imprime = true;
        	DefaultListModel lista = new DefaultListModel();
            for(int i=0;i<quantidadeCriterios;i++){
            	if(aux.equals("") && Integer.parseInt(combinacoes[i].charAt(0)+"") == 1){
            		aux = combinacoes[i];
            		aux2 = combinacoes[i];
            		lista.addElement(combinacoes[i]);
            	}else if(!aux.equals("") &&
            			 !aux.substring(0, 1).equals(combinacoes[i].substring(0, 1)) &&
            			   (Integer.parseInt(aux.substring(0, 1))+1)==Integer.parseInt(combinacoes[i].substring(0, 1))){
            		aux = combinacoes[i];
            		aux2 = aux2+" "+combinacoes[i];
            		lista.addElement(combinacoes[i]);
            	}else{
            		imprime = false;
            	}
            }
            if(imprime){
            	alternativa = new Alternative("", aux2 +" - "+ totalAlternativas, lista);
            	alternativa.setCode("imaginaryAlt "+totalAlternativas);
            	alternativa.setFiq(zapros.calculateFIQ(alternativa));
            	listaAlternativasFicticias.add(alternativa);
            	totalAlternativas++;
            }
            return;
        } else {
            System.err.println("Erro...");
            return;
        }
    }

}
