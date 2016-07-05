package br.com.projetoaranau.zapros_III;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import br.com.projetoaranau.util.MergeSortZapros;

public class ZaprosMethod implements java.io.Serializable{

	private ArrayList<Criterion> criteria;
	private ArrayList<Alternative> alternatives;
	private ArrayList<Alternative> possibleAlternatives; //the alternatives that are not real
	private JSQV jsqvScale;
	private Alternative referenceSituation;
	private String problemName;
	//private SimpleGraph<Alternative> alternativesGraph;

    static boolean instance = false;
    static ZaprosMethod zapros;

//  Methods related to an instance of ZaprosMethod
    private ZaprosMethod(){
    	criteria = new ArrayList<Criterion>();
    	alternatives = new ArrayList<Alternative>();
    	possibleAlternatives = new ArrayList<Alternative>();
    }

	public static ZaprosMethod getInstance() {
		if (!instance) {
			zapros = new ZaprosMethod();
			instance = true;
		}
		return zapros;
	}

	public static void setInstance(ZaprosMethod zaprosFromFile) {
		if(zaprosFromFile==null){
			instance = false;
		}else{
			zapros = zaprosFromFile;
			instance = true;
		}
	}


//	Methods related to criteria
	public void addCriterion(String description, List<String> list) {
		int code = criteria.size()+1;
		Criterion criterion = new Criterion(code+"", description, list);
		getCriteria().add(criterion);
		removePreferences();
		if(this.alternatives.size()>0){
			alternatives = new ArrayList<Alternative>();
		}
	}

	public void setCriterion(int position, Criterion criterion){
		Criterion newCriterion= new Criterion(criterion.getCode(), criterion.getDescription(), criterion.getValues());
		getCriteria().set(position, newCriterion);
		removePreferences();
		if(this.alternatives.size()>0){
			alternatives = new ArrayList<Alternative>();
		}
	}

	public void removeCriterio(int criterionIndex){
		getCriteria().remove(criterionIndex);
		removePreferences();
		if(this.alternatives.size()>0){
			alternatives = new ArrayList<Alternative>();
		}
	}

	public Criterion findCriterionByCode(String criterionCode) {
		for (int i = 0; i<criteria.size();i++){
			Criterion criterion = (Criterion)criteria.get(i);
			if(criterion.getCode().equals(criterionCode)){
				return criterion;
			}
		}
		return null;
	}

	public Criterion findCriterionByDescription(String criterionDescription) {
		for (int i = 0; i<criteria.size();i++){
			Criterion criterion = (Criterion)criteria.get(i);
			if(criterion.getDescription().equals(criterionDescription)){
				return criterion;
			}
		}
		return null;
	}


//	Methods related to alternatives
	public void addAlternative(String description, ListModel values){
		int code = alternatives.size()+1;
		Alternative a = new Alternative(code+"",description, values);
		a.setFiq(calculateFIQ(a));
		getAlternatives().add(a);
	}

	public void setAlternative(int position, Alternative alternative){
		Alternative newAlternative= new Alternative(alternative.getCode(), alternative.getDescription(), alternative.getValues());
		newAlternative.setFiq(calculateFIQ(newAlternative));
		getAlternatives().set(position, newAlternative);
	}

	public void removeAlternative(int code){
		getAlternatives().remove(code);
	}

	public Alternative findAlternative(String code) {
		for (int i = 0; i<alternatives.size();i++){
			Alternative alternative = (Alternative)alternatives.get(i);
			if(alternative.getCode().equals(code)){
				return alternative;
			}
		}
		return null;
	}

	public void clearAlternativesRanks(){
		for (int i=0; i<alternatives.size(); i++){
			alternatives.get(i).clearComparisonData();
		}
	}
	
	public void setDominantAlternativesForAllAlternatives(){
		Alternative alternative;
		ArrayList<Alternative> dominantAlternatives;
		for(int i = 0; i<alternatives.size();i++){
			alternative = alternatives.get(i);
			dominantAlternatives = new ArrayList<Alternative>();
			for(int j = 0; j<i; j++){
				if(alternatives.get(j).getRank()==alternative.getRank()-1){
					dominantAlternatives.add(alternatives.get(j));
				}
			}
			//alternative.setDominantAlternatives(dominantAlternatives);
		}
	}
	
//	Methods related to preferences
	public QV findQV(String code){
		Criterion criterion = (Criterion)criteria.get(Integer.parseInt(code.substring(0, 1))-1);
		return criterion.findQV(code);
	}

	public void removePreferences(){
		this.setJsqvScale(null);
		for (int i = 0; i<criteria.size(); i++){
			Criterion criterion = (Criterion) criteria.get(i);
			criterion.setQvsPreferenceOrder(null);
		}
	}

//TODO OK acima
	public ArrayList montarEscalaQVsPara1Criterio(String maior,String menor, boolean igual){
		ArrayList questoes = new ArrayList();
		Criterion c = (Criterion)findCriterionByDescription(maior.substring(0,maior.indexOf(":")));
		QV qvMaior = c.findQV(maior.substring(maior.indexOf(":")+2, maior.indexOf("*FIM*")-1), maior.substring(maior.indexOf("*FIM*")+6, maior.length()-1));
		QV qvMenor = c.findQV(menor.substring(menor.indexOf(":")+2, menor.indexOf("*FIM*")-1), menor.substring(menor.indexOf("*FIM*")+6, menor.length()-1));
		if(igual){
			questoes = jsqvScale.inserirEscalaParcial(qvMaior, qvMenor, "=");  //diz o que deve ser inserido e em qual arrayList
		}else{
			questoes = jsqvScale.inserirEscalaParcial(qvMaior,qvMenor, "<");
		}
		if(questoes!=null){
			qvMaior = null;
			qvMenor = null;
			if(questoes.get(0) != null){
				qvMaior = findQV(questoes.get(0).toString());
				qvMenor = findQV(questoes.get(1).toString());
				questoes.set(0, qvMaior.getCode());
				questoes.set(1, qvMenor.getCode());
			}
		}else{
			c.setQvsPreferenceOrder(jsqvScale.getEscalaParcial());
			jsqvScale.setEscalaParcial(new ArrayList());
			//encontrar outro criterio para elicitar pref.
			Criterion c1;
			QV qv1 = null;
			QV qv2 = null;
			for(int i = 0; i<zapros.getCriteria().size();i++){
		        c1 = (Criterion)zapros.getCriteria().get(i);
		        if(c1.getQualitativeValues().size()>1){
		        	if(c1.getQvsPreferenceOrder()==null){
				        qv1 = (QV)c1.getQualitativeValues().get(0);
				        qv2 = (QV)c1.getQualitativeValues().get(1);
				        break;
		        	}
		        }else{
		        	ArrayList arrayPref = new ArrayList();
		        	arrayPref.add(((QV)c1.getQualitativeValues().get(0)).getCode());
		        	c1.setQvsPreferenceOrder(arrayPref);
		        }
	        }
			if(qv1==null){
				return null;
			}else{
				questoes = new ArrayList();
				questoes.add(0, qv1.getCode());
				questoes.add(1, qv2.getCode());
			}
		}
		return questoes;
	}

	public ArrayList montarEscalaJSQV(String maior, String menor, boolean igual){
		ArrayList questoes = new ArrayList();
			QV qvMaior = ((Criterion)findCriterionByDescription(maior.substring(0,maior.indexOf(":")))).findQV(maior.substring(maior.indexOf(":")+2, maior.indexOf("*FIM*")-1), maior.substring(maior.indexOf("*FIM*")+6, maior.length()-1));
			QV qvMenor = ((Criterion)findCriterionByDescription(menor.substring(0,menor.indexOf(":")))).findQV(menor.substring(menor.indexOf(":")+2, menor.indexOf("*FIM*")-1), menor.substring(menor.indexOf("*FIM*")+6, menor.length()-1));
			if(igual){
				questoes = jsqvScale.inserirEscalaParcial(qvMaior, qvMenor, "=");  //diz o que deve ser inserido e em qual arrayList
			}else{
				questoes = jsqvScale.inserirEscalaParcial(qvMaior,qvMenor, "<");
			}
			if(questoes!=null){
				qvMaior = findQV(questoes.get(0).toString());
				qvMenor = findQV(questoes.get(1).toString());
			}else if(questoes == null){
				//se n�o h� mais qvs para inserir na escala parcial
				ArrayList indicesEscala = jsqvScale.finalizaEscalaParcial();

				int c1 = Integer.parseInt(indicesEscala.get(0).toString());
				int c2 = Integer.parseInt(indicesEscala.get(1).toString());

				if(c1>c2){
					int aux = c1;
					c1 = c2;
					c2 = aux;
				}

				if(c2 == criteria.size()){
					if(c1 == criteria.size()-1){
						ArrayList contradicao = jsqvScale.montarJSQV();
						if(contradicao!=null){
							for(int i = 1; i<contradicao.size();i++){
								contradicao.set(i, findQV(contradicao.get(i).toString()).getChoiceText());
							}
							return contradicao;
						}else{
							return null;				//se n�o houverem mais qvs para escalas parciais
						}
					}else{
						Criterion criterio1 = (Criterion)criteria.get(c1);
						Criterion criterio2 = (Criterion)criteria.get(c1+1);
						qvMaior = (QV)criterio1.findQV(criterio1.getQvsPreferenceOrder().get(0).toString());
						qvMenor = (QV)criterio2.findQV(criterio2.getQvsPreferenceOrder().get(0).toString());
						//c1, c1+1
					}
				}else{
					Criterion criterio1 = (Criterion)criteria.get(c1-1);
					Criterion criterio2 = (Criterion)criteria.get(c2);
					qvMaior = (QV)criterio1.findQV(criterio1.getQvsPreferenceOrder().get(0).toString());
					qvMenor = (QV)criterio2.findQV(criterio2.getQvsPreferenceOrder().get(0).toString());
				}
			}
		if(qvMaior!=null && qvMenor!=null){
			questoes = new ArrayList();
			questoes.add(qvMaior.getChoiceText());
			questoes.add(qvMenor.getChoiceText());
		}
		return questoes;
	}

	public int findQVPositionOnScale(String qv){
		return getJsqvScale().findQvValue(qv);
	}
	
	public int findQVPositionOnScale(QV qv){
		return getJsqvScale().findQvValue(qv.getCode());
	}


//	Methods related to the alternatives comparison
	

	public void gerarAlternativasFicticias(){
		new Combinacao();
		orderAlternatives("fiq", possibleAlternatives);
		/*ArrayList<Alternative> listaAlternativasAConsiderar = new ArrayList<Alternative>();
		ArrayList <String> primeiraAlt = new ArrayList<String>();
		int menorFiq = alternatives.get(0).getFiq();
		int fiq = menorFiq;
		int aux = 0;
		while (menorFiq == fiq){
			primeiraAlt.add(alternatives.get(aux).getValues().toString());
			aux++;
			fiq = alternatives.get(aux).getFiq();
		}
		int ultimaAlt = ((Alternative) alternatives.get(alternatives.size()-1)).getFiq();
		Alternative altFicticia;
		boolean copiar = false;
		for(int i = 0;i<possibleAlternatives.size();i++){
			altFicticia = possibleAlternatives.get(i);
			for(int j = 0; j<primeiraAlt.size();j++){
				if(primeiraAlt.get(j).equals(altFicticia.getValues().toString())){
					copiar = true;
				}
			}
			if(altFicticia.getFiq() > ultimaAlt){
				break;
			}
			if(copiar){
				listaAlternativasAConsiderar.add(possibleAlternatives.get(i));
			}
		}

		possibleAlternatives = listaAlternativasAConsiderar;*/
	}

//	TODO ok abaixo
	
	public int calculateFIQ(Alternative alternative){
		int fiqValue = new FIQ(this).calculateFIQ(this.getReferenceSituation(), alternative);
		if(fiqValue<0){
			fiqValue = fiqValue*(-1);
		}
		alternative.setFiq(fiqValue);
		return alternative.getFiq();
	}

	public String compare(Alternative alternative1, Alternative alternative2){
		ArrayList<ArrayList<Integer>> ranksQV = new FIQ(this).getVectorOfQualityVariations(alternative1, alternative2);
		ArrayList<Integer> ranksQVCriterionAlt1 = ranksQV.get(0);
		ArrayList<Integer> ranksQVCriterionAlt2 = ranksQV.get(1);
		
		//sorts the ranks of QVs in ascending order
		orderAltenativesQVsValues(ranksQVCriterionAlt1, ranksQVCriterionAlt2);
		
		//if the alternatives have the same criterion values, return equivalent
		if(ranksQVCriterionAlt1.toString().equals(ranksQVCriterionAlt2.toString()))
			return "equivalent";
		
		//return Alternative 1 if it has no less preferable values then Alt2 and, at least one is better
		boolean isPreferable = true;
		for(int i=0; i<ranksQVCriterionAlt1.size(); i++){
			if (ranksQVCriterionAlt1.get(i)>ranksQVCriterionAlt2.get(i)){
				isPreferable = false;
				break;
			}
		}
		if(isPreferable)
			return alternative1.getCode();
		
		//return Alternative 1 if it has no less preferable values then Alt2 and, at least one is better
		isPreferable = true;
		for(int i=0; i<ranksQVCriterionAlt2.size(); i++){
			if (ranksQVCriterionAlt2.get(i)>ranksQVCriterionAlt1.get(i)){
				isPreferable = false;
				break;
			}
		}
		if(isPreferable)
			return alternative2.getCode();
		
		return "incomparable";
	}

	public void orderAlternatives(String parameter, ArrayList<Alternative> array){
		new MergeSortZapros().mergeSort(array, 0, array.size(), parameter);
	}

	public void orderAltenativesQVsValues(ArrayList array1, ArrayList array2){
		new MergeSortZapros().mergeSort(array1, 0, array1.size(), null);
		new MergeSortZapros().mergeSort(array2, 0, array2.size(), null);
	}
	
	private void findDominantAlternative(Alternative alternativeToRank, ArrayList<Alternative> alternativesToCompare, boolean testIncomparableCases){
		boolean restart = true;
		while (restart){
			restart = false;
			/*for(Alternative alternativeToCompareTo: alternativesToCompare){
				if(!alternativeToCompareTo.isCompared && !alternativesGraph.areInTheSameBranch(alternativeToRank, alternativeToCompareTo))
				{
					alternativeToCompareTo.isCompared = true;
					String result = compare(alternativeToRank, alternativeToCompareTo);
					if(!result.equals("incomparable")){
						processComparison(result, alternativeToRank, alternativeToCompareTo);
					}else{
						if(alternativesGraph.getIncomingNodesOf(alternativeToCompareTo).size()>0){
							ArrayList<Alternative> subList = new ArrayList<Alternative>(alternativesToCompare.subList(0,alternativesToCompare.indexOf(alternativeToCompareTo)+1));
							//removes the alternatives that were already compared -- the alternatives situated before the current one to compare to
							alternativesToCompare.removeAll(subList);
							addAllDistinct(alternativesToCompare, (ArrayList<Alternative>) alternativesGraph.getIncomingNodesOf(alternativeToCompareTo));
							restart = true;
							break;
						}
					}
				}
			}*/
		}
	}
	
	public void addAllDistinct(ArrayList<Alternative> collection, ArrayList<Alternative> collectionToAdd){
		for(Alternative alternative: collectionToAdd)
			if(!collection.contains(alternative))
				collection.add(alternative);
	}
	
	public void processComparison(String result, Alternative alternativeToRank, Alternative alternativeToCompareTo){
//		if(result.equals(alternativeToRank.getCode())){
//			findDominantAlternative(alternativeToRank, (ArrayList<Alternative>) alternativesGraph.getIncomingNodesOf(alternativeToCompareTo), false);
//			alternativesGraph.addVerifiedEdge(alternativeToRank, alternativeToCompareTo);
			alternativeToCompareTo.addDominant(alternativeToRank);
//		}else if(result.equals(alternativeToCompareTo.getCode())){
//			alternativesGraph.addVerifiedEdge(alternativeToCompareTo, alternativeToRank);
			alternativeToRank.addDominant(alternativeToCompareTo);
//		}else if(result.equals("equivalent")){
//			alternativeToCompareTo.addEquivalent(alternativeToRank);
//			alternativeToRank.addEquivalent(alternativeToCompareTo);
//			alternativeToRank.addDominant((ArrayList<Alternative>)alternativesGraph.getIncomingNodesOf(alternativeToCompareTo));
//		}
	}
	
//	Method that compares the alternatives
	public void alternativesComparison(ArrayList<Alternative> alternativesToCompare){
		ArrayList<Alternative> comparisonLeafNodes;
		//comparison starts with the second alternative of the array for the first one is already on the leafNodes array
		int a=0;
		for(Alternative alternativeToRank: alternativesToCompare){
			a++; System.out.println(a+"\n");
//			comparisonLeafNodes = (ArrayList<Alternative>) alternativesGraph.getLeafNodes();
//			alternativesGraph.addVertex(alternativeToRank);
			tryToIntegrateExistingGraphs(alternativeToRank);
//			findDominantAlternative(alternativeToRank, comparisonLeafNodes, true);
			resetAlternativesComparisonVariable();
		}
//		alternativesGraph.removeAllRedundantEdges();
	}
	
	public void resetAlternativesComparisonVariable(){
//		for(Alternative alternative: alternativesGraph.vertexSet())
//			alternative.isCompared = false;
	}
	
	public void tryToIntegrateExistingGraphs(Alternative alternativeToRank){
//		ArrayList<Alternative> rootNodes = (ArrayList<Alternative>) alternativesGraph.getRootNodes();
//		rootNodes.remove(alternativeToRank);
//		for(Alternative rootAlternative: rootNodes){
//			String result = compare(rootAlternative, alternativeToRank);
//			if(!result.equals("incomparable"))
//				processComparison(result, alternativeToRank, rootAlternative);
//		}
	}
	
	public ArrayList<Alternative> generateResults(boolean compareAllPossibleAlternatives){
//		alternativesGraph = new SimpleGraph<Alternative>(null);
		//Set the array of alternatives to compare: the real alternatives of the problem or all possible ones
		ArrayList<Alternative> comparisonAlternatives;
//		if(compareAllPossibleAlternatives){
//			gerarAlternativasFicticias();
//			comparisonAlternatives = possibleAlternatives;
//			for(Alternative alternative: possibleAlternatives){
//				for(Alternative realAlternative: alternatives){
//					if(alternative.isEqualValues(realAlternative)){
//						int realAlternativePosition = possibleAlternatives.indexOf(alternative);
//						possibleAlternatives.set(realAlternativePosition, realAlternative);
//						break;
//					}
//				}
//			}
//		}else{
//			comparisonAlternatives = alternatives;
//		}
//		
//		//Order the alternatives to compare according to their formal index of quality
//		orderAlternatives("fiq", comparisonAlternatives);
//		//Clear all data from past comparisons
//		this.clearAlternativesRanks();
//		
//		alternativesComparison(comparisonAlternatives);
//		
//		if(compareAllPossibleAlternatives){
//			printEdgeSet();
//			ArrayList<Alternative> alternativesConsidered = (ArrayList<Alternative>)comparisonAlternatives.clone();	
//			//Removes imaginary alternatives of the problem
//			for(Alternative alternative: alternativesConsidered){
//				if(!alternatives.contains(alternative)){
//					alternativesGraph.removeVerifiedEdge(alternative);
//					alternativesGraph.removeVertex(alternative);
//				}
//			}
//		}
//		//establish alternatives ranks and dominant alternatives
//		for(Alternative alternative: alternatives){
//			alternative.setRank(GraphFactory.getValueOfDegree(alternativesGraph, alternative));
//			alternative.printDominantAlternatives((ArrayList<Alternative>) alternativesGraph.getIncomingNodesOf(alternative));
//		}
//			
//		orderAlternatives("rank", alternatives);
//		
//		criateGraphTextFile();
//
		return alternatives;
	}
	
	public void printEdgeSet(){
//		for(DefaultEdge e:alternativesGraph.edgeSet()){
//			System.out.println(alternativesGraph.getEdgeSource(e)+"-"+alternativesGraph.getEdgeTarget(e));
//		}
	}

//	Method related to the graph of alternatives
	public void criateGraphTextFile(){
		File file;
		try {
			String aux;
			file = new File(this.problemName+" - GraphFile.txt");
			FileWriter writer = new FileWriter(file);
			PrintWriter obj = new PrintWriter(writer);
			for(Alternative alternative: alternatives){
//				ArrayList<Alternative> dominantAlternatives = (ArrayList<Alternative>) alternativesGraph.getIncomingNodesOf(alternative);
//				for(Alternative dominantAlternative:dominantAlternatives){
//					aux = dominantAlternative.getDescription()+'-'+alternative.getDescription();
//					obj.println(aux);
//					obj.flush();
//				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Getters and Setters
	public ArrayList<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(ArrayList<Criterion> criteria) {
		this.criteria = criteria;
	}

	public ArrayList<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(ArrayList<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	public JSQV getJsqvScale() {
		return jsqvScale;
	}

	public void setJsqvScale(JSQV jsqvScale) {
		this.jsqvScale = jsqvScale;
	}

	public Alternative getReferenceSituation() {
		if(referenceSituation == null || (this.getCriteria().size() != referenceSituation.getValues().getSize())){
			DefaultListModel values = new DefaultListModel();
			Criterion criterion;
			for (int i = 0; i<criteria.size(); i++){
				criterion = (Criterion)criteria.get(i);
				values.addElement(criterion.getCode()+"/"+1); //it takes always the best value of each criteria
			}
			referenceSituation = new Alternative("","Situa��o de Refer�ncia", values);
		}
		return referenceSituation;
	}

	public void setReferenceSituation(Alternative referenceSituation) {
		this.referenceSituation = referenceSituation;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public ArrayList<Alternative> getPossibleAlternatives() {
		return possibleAlternatives;
	}

	public void setPossibleAlternatives(ArrayList<Alternative> possibleAlternatives) {
		this.possibleAlternatives = possibleAlternatives;
	}

}