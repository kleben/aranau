package br.com.projetoaranau.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;

import br.com.projetoaranau.zapros_III.Criterion;
import br.com.projetoaranau.zapros_III.JSQV;
import br.com.projetoaranau.zapros_III.QV;
import br.com.projetoaranau.zapros_III.ZaprosMethod;

@SessionScoped
@ManagedBean(name = "preferencesBean")
public class PreferencesBean extends GenericBean{

	private static final long serialVersionUID = 769920659517018451L;
	//critério em teste de preferencia
	private Criterion c1;  			
	//critério em teste de preferencia
    private Criterion c2;
    private String texto1;
    private String texto2;
    private String textAreaSitRef;
    
    private String situationDescription;
    private String situationDescription2;
    private String labelSituation1;
    private String labelSituation2;
    private String selectedPreference;
    private List<String> preferencesOptions;
    
    private UISelectOne selectOfPreferences;
    public UISelectItem preference1;
    private boolean flagElicitMesmoCrit = false;
    private boolean preferenciasElicitadas = false;
    
	
	
	public PreferencesBean() {
		super();
	}
	
	public void init(){
		
		zapros = ZaprosMethod.getInstance();
		
		if(zapros.getJsqvScale() == null){
    		int numeroCriterios = zapros.getCriteria().size();
    		int totalValoresEscalaJSQV = 0;
    		for(int i = 0; i<numeroCriterios;i++){
    			Criterion c = (Criterion) zapros.getCriteria().get(i);
    			totalValoresEscalaJSQV += c.getValues().size();
    		}
    		int numeroEscalas = (int)(0.5*numeroCriterios*(numeroCriterios-1));
    		zapros.setJsqvScale(new JSQV(totalValoresEscalaJSQV, numeroEscalas));
    		
    		// ##############
    		ArrayList questoes = new ArrayList();
            QV qv1=null;
            QV qv2=null;
            for(int i = 0; i<zapros.getCriteria().size();i++){
    	        c1 = (Criterion)zapros.getCriteria().get(i);
    	        if(c1.getQualitativeValues().size()>1){
    		        qv1 = (QV)c1.getQualitativeValues().get(0);
    		        qv2 = (QV)c1.getQualitativeValues().get(1);
    		        break;
    	        }else{
    	        	ArrayList lista = new ArrayList();
    	        	String qv = ((QV)c1.getQualitativeValues().get(0)).getCode();
    	        	lista.add(qv);
    	        	c1.setQvsPreferenceOrder(lista);
    	        }
            }
            if(qv1==null){
            	montarPerguntaPara2Criterios();
            }else{
            	questoes.add(qv1.getCode());
                questoes.add(qv2.getCode());
                flagElicitMesmoCrit = true;
                montarTelaPara1Criterio(questoes);
            }
    	}
	}	
	
	private void montarPerguntaPara2Criterios(){
    	c1 = (Criterion)zapros.getCriteria().get(0);
    	c2 = (Criterion)zapros.getCriteria().get(1);
    	QV qv1 = c1.findQV(c1.getQvsPreferenceOrder().get(0));
    	QV qv2 = c2.findQV(c2.getQvsPreferenceOrder().get(0));
        texto1 = qv1.getChoiceText();
        texto2 = qv2.getChoiceText();
        
        System.out.println("Texto 1: "+ texto1);
        System.out.println("Texto 2: "+ texto2);
        
        //descricoes dos criterios
        String descCriterio1 = texto1.substring(0,texto1.indexOf(":"));
        String descCriterio2 = texto2.substring(0,texto2.indexOf(":"));
        System.out.println("Label sel alternativa: "+"Com base nos critérios \""+descCriterio1+"\" e \""+descCriterio2+"\", selecione a alternativa mais preferível para as duas situações:");
        labelSituation1 = "Com base nos critérios \""+descCriterio1+"\" e \""+descCriterio2+"\", selecione a alternativa mais preferível para as duas situações:";

        //variacoes
        preferencesOptions = new ArrayList<String>();
        preferencesOptions.add("Alternativa 1 Para: "+texto2.substring(texto2.indexOf(":")+1, texto2.indexOf("*FIM*"))+
        		"Alternativa 1 De: "+texto1.substring(texto1.indexOf("*FIM*")+5, texto1.length()));
        preferencesOptions.add("Alternativa 2 De: "+texto1.substring(texto1.indexOf(":")+1, texto1.indexOf("*FIM*"))+
        		"Alternativa 2 De: "+texto2.substring(texto2.indexOf("*FIM*")+5, texto2.length()) );
        preferencesOptions.add("As opções acima são equivalentes");
    	
        System.out.println("Alternativa 1 Para: "+texto2.substring(texto2.indexOf(":")+1, texto2.indexOf("*FIM*")));
        System.out.println("Alternativa 1 De: "+texto1.substring(texto1.indexOf("*FIM*")+5, texto1.length()));
        System.out.println("Alternativa 2 De: "+texto1.substring(texto1.indexOf(":")+1, texto1.indexOf("*FIM*")));
        System.out.println("Alternativa 2 De: "+texto2.substring(texto2.indexOf("*FIM*")+5, texto2.length()));

        /**** Sit Hipotetica ****/
        gerarSituacoesReferencia(descCriterio1, descCriterio2);

    }
	

    private void montarTelaPara1Criterio(ArrayList questoes){

    	QV qv1 = (QV)zapros.findQV(questoes.get(0).toString());
    	QV qv2 = (QV)zapros.findQV(questoes.get(1).toString());
        texto1 = qv1.getChoiceText();
        texto2 = qv2.getChoiceText();

    	String descCriterio = texto1.substring(0,texto1.indexOf(":"));
    	labelSituation1 = "Comparando duas variações de qualidade para o critério "+descCriterio+", selecione a alternativa mais preferível:";
    	System.out.println("Label sel alternativa: "+"Comparando duas variações de qualidade para o critério \""+descCriterio+"\", selecione a alternativa mais preferível:");
    	
    	preferencesOptions = new ArrayList<String>();
    	preferencesOptions.add("De: " +texto1.substring(texto1.indexOf(":")+1, texto1.indexOf("*FIM*"))
		    	+" Para: " +texto1.substring(texto1.indexOf("*FIM*")+5, texto1.length())
		    	);
    	preferencesOptions.add("De: " +texto2.substring(texto2.indexOf(":")+1, texto2.indexOf("*FIM*"))
    			+" Para: " +texto2.substring(texto2.indexOf("*FIM*")+5, texto2.length())
    			);
    	preferencesOptions.add("As opções acima são equivalentes");
    	
    	System.out.println("De     : " +texto1.substring(texto1.indexOf(":")+1, texto1.indexOf("*FIM*")));
    	System.out.println("Para : " +texto1.substring(texto1.indexOf("*FIM*")+5, texto1.length()));
    	System.out.println("De     : " +texto2.substring(texto2.indexOf(":")+1, texto2.indexOf("*FIM*")));
    	System.out.println();

        gerarSituacoesReferencia(descCriterio, null);
    }
    
    public void gerarSituacoesReferencia(String descCriterio1, String descCriterio2){
    	situationDescription = "";
    	StringBuffer textAreaSitRef = new StringBuffer();
    	StringBuffer textAreaSitRef2 = new StringBuffer();
        String textoSituacao;
        situationDescription = new String();
        situationDescription2 = new String();

        if(zapros.getCriteria().size() == 2 && descCriterio2 != null){
        	textAreaSitRef.append("Nenhuma situação gerada quando há apenas dois critérios");
        }else{
        	ArrayList pioresValores = new ArrayList();
        	textAreaSitRef.append("Situação 1: \n");

	        for(int i = 0; i<zapros.getCriteria().size(); i++){
	        	int codCriterio = i+1;
	        	Criterion c = zapros.findCriterionByCode(codCriterio+"");

	        	if(!c.getDescription().equals(descCriterio1) && !c.getDescription().equals(descCriterio2)){
					Criterion criterio = (Criterion)zapros.getCriteria().get(i);
					textoSituacao = "'"+criterio.getDescription()+"'" + ": " + criterio.getValues().get(0);
					pioresValores.add("'"+criterio.getDescription()+"'" + ": " + criterio.getValues().get(criterio.getValues().size()-1));
					textAreaSitRef.append(textoSituacao);
					//if(i<zapros.getCriterios().size()-1){ //se nao for o ultimo, acrescenta espaco
						textAreaSitRef.append("\n");
					//}
					situationDescription = textAreaSitRef.toString();
				}
			}
	        
	        textAreaSitRef2.append("Situação 2: \n");
        	for (int i = 0; i<pioresValores.size();i++){
        		textAreaSitRef2.append(pioresValores.get(i).toString());
				if(i<zapros.getCriteria().size()-1){ //se nao for o ultimo, acrescenta espaco
					textAreaSitRef2.append("\n");
				}
				situationDescription2 = textAreaSitRef2.toString();
        	}
        }
    }
    
    public void salvarQVs1Criterio() {
    	
    	if (selectedPreference == null || selectedPreference.isEmpty() ){
    		FacesMessage message = new FacesMessage("", "Selecione uma opção!");
	        message.setSeverity(FacesMessage.SEVERITY_WARN);
	        FacesContext.getCurrentInstance().addMessage(null, message);
     	}
    	
    	final ArrayList questoes;
    	boolean resposta = true;
    	
    	System.out.println(selectedPreference);
    	if( preferencesOptions.get(0).equalsIgnoreCase(selectedPreference) ){
    		questoes = zapros.montarEscalaQVsPara1Criterio(texto1, texto2, false);
    	}else if( preferencesOptions.get(1).equalsIgnoreCase(selectedPreference) ){
    		questoes = zapros.montarEscalaQVsPara1Criterio(texto2, texto1, false);
    	}else if( selectedPreference.equalsIgnoreCase("As opções acima são equivalentes") ){
    		questoes = zapros.montarEscalaQVsPara1Criterio(texto1, texto2, true);
    	}else{
    		questoes = new ArrayList();
//    		JOptionPane.showMessageDialog(null, "Selecione uma preferência!","Aviso",JOptionPane.WARNING_MESSAGE);
    		resposta = false;
    	}
    	if(questoes != null && resposta){
    		montarTelaPara1Criterio(questoes);
    	}else{
    		flagElicitMesmoCrit = false;
    		montarPerguntaPara2Criterios();
    	}
    	
    	selectOfPreferences.setValue(null);
    }
    
    public String salvarPreferenciasQVs(){
    	
    	final ArrayList questoes;
    	boolean resposta = true;
    	if( preferencesOptions.get(0).equalsIgnoreCase(selectedPreference) ){
    		questoes = zapros.montarEscalaJSQV(texto1, texto2, false);
    	}else if( preferencesOptions.get(1).equalsIgnoreCase(selectedPreference) ){
    		questoes = zapros.montarEscalaJSQV(texto2, texto1, false);
    	}else if( selectedPreference.equalsIgnoreCase("As opções acima são equivalentes") ){
    		questoes = zapros.montarEscalaJSQV(texto1, texto2, true);
    	}else{
    		FacesMessage message = new FacesMessage("", "Selecione uma preferência!. Adicione os valores.");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
    		questoes = new ArrayList();
    		resposta = false;
    	}
    	if(questoes != null && resposta){
	    	texto1 = questoes.get(0).toString();
	    	if(!texto1.equals("contradicao")){
		        texto2 = questoes.get(1).toString();
		        //descricoes dos criterios
		        String descCriterio1 = texto1.substring(0,texto1.indexOf(":"));
		        String descCriterio2 = texto2.substring(0,texto2.indexOf(":"));
		        
		        labelSituation1 = "Com base nos critérios \""+descCriterio1+"\" e \""+descCriterio2+"\", selecione a alternativa mais preferível:";
//		        labelSelecionarAlt.setText("Com base nos critérios \""+descCriterio1+"\" e \""+descCriterio2+"\", selecione a alternativa mais preferível:" );
		        
		        //variacoes
		        preferencesOptions = new ArrayList<String>();
		    	preferencesOptions.add( texto2.substring(texto2.indexOf(":")+1, texto2.indexOf("*FIM*")) 
				    	+ texto1.substring(texto1.indexOf("*FIM*")+5, texto1.length())
				);
		    	preferencesOptions.add(texto1.substring(texto1.indexOf(":")+1, texto1.indexOf("*FIM*"))
		    			+ texto2.substring(texto2.indexOf("*FIM*")+5, texto2.length())
		    			);
		    	preferencesOptions.add("As opções acima são equivalentes");
		    	
		        /**** Sit Hipotetica ****/
		        gerarSituacoesReferencia(descCriterio1, descCriterio2);
	    	}else{
	    		FacesMessage message = new FacesMessage("", "Frame eliminar contradicao!");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	    	}
    	}else if (questoes == null){
    		FacesMessage message = new FacesMessage("", "Elicitação de preferências concluída!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            preferenciasElicitadas = true;
    	}
    	return null;
    }
    
    public void salvar() {
    	if(flagElicitMesmoCrit){
    		salvarQVs1Criterio();
    	}else{
    		salvarPreferenciasQVs();
    	}
    }
    
    public String voltar(){
    	
    	return "/views/index.xhtml?faces-redirect=true";
    }
    
    
	public String getLabelSituation1() {
		return labelSituation1;
	}

	public void setLabelSituation1(String labelSituation1) {
		this.labelSituation1 = labelSituation1;
	}

	public String getSelectedPreference() {
		return selectedPreference;
	}

	public void setSelectedPreference(String selectedPreference) {
		this.selectedPreference = selectedPreference;
	}

	public List<String> getPreferencesOptions() {
		return preferencesOptions;
	}

	public void setPreferencesOptions(List<String> preferencesOptions) {
		this.preferencesOptions = preferencesOptions;
	}

	public String getLabelSituation2() {
		return labelSituation2;
	}

	public void setLabelSituation2(String labelSituation2) {
		this.labelSituation2 = labelSituation2;
	}

	public String getSituation1Description() {
		return situationDescription;
	}

	public void setSituation1Description(String situation1Description) {
		this.situationDescription = situation1Description;
	}

	public String getSituationDescription() {
		return situationDescription;
	}

	public void setSituationDescription(String situationDescription) {
		this.situationDescription = situationDescription;
	}

	public String getSituationDescription2() {
		return situationDescription2;
	}

	public void setSituationDescription2(String situationDescription2) {
		this.situationDescription2 = situationDescription2;
	}

	public UISelectOne getSelectOfPreferences() {
		return selectOfPreferences;
	}

	public void setSelectOfPreferences(UISelectOne selectOfPreferences) {
		this.selectOfPreferences = selectOfPreferences;
	}

	public boolean isPreferenciasElicitadas() {
		return preferenciasElicitadas;
	}

	public void setPreferenciasElicitadas(boolean preferenciasElicitadas) {
		this.preferenciasElicitadas = preferenciasElicitadas;
	}	
}
