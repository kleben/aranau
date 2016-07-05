package br.com.projetoaranau.zapros_III;

import java.util.ArrayList;
import javax.swing.ListModel;

public class Alternative{

	private String code;
	private String description;
	private ListModel values;
	private int fiq;
	private int rank = 0;
	//only used when an alternative has a rank x and domines another with rank x+j, where j>1
	private int rankFuzzy = 0;
	private ArrayList<Alternative> equivalentAlternatives = new ArrayList<Alternative>();
	private ArrayList<Alternative> dominantAlternatives = new ArrayList<Alternative>();
	
	public Alternative(String code, String description, ListModel values){
		this.code = code;
		this.description = description;
		this.values = values;
		printAlternative();
	}

	public void printAlternative(){
		System.out.println("");
		System.out.println("----------------- Alternative -----------------");
		System.out.println("Description: "+ this.getDescription() + " - Fiq: "+this.getFiq());
		System.out.println("Values : " + values.getElementAt(0));
		for (int i = 1; i<values.getSize();i++){
			System.out.println("          "+values.getElementAt(i));
		}
		System.out.println("-----------------------------------------------");
	}
	
	public void printDominantAlternatives(ArrayList<Alternative> dominantAlternatives){
		System.out.print(this.getDescription()+ " ->  ");
		for(Alternative dominant: dominantAlternatives){
			System.out.print(dominant.getDescription()+ " - ");
			this.addDominant(dominant);
		}
		System.out.println("");
	}
	
	public void addEquivalent(Alternative alternative){
		if(!this.getEquivalentAlternatives().contains(alternative)){
			this.equivalentAlternatives.add(alternative);
		}
	}
	
	public void addDominant(Alternative alternative){
		if(!this.getDominantAlternatives().contains(alternative)){
			this.dominantAlternatives.add(alternative);
		}
	}
	
	public void addDominant(ArrayList<Alternative> alternatives){
		for(Alternative alternative: alternatives){
			this.addDominant(alternative);
		}
	}
	
	/*public void addAllDominant(Alternative alternative){
		if(!this.getAllDominantAlternatives().contains(alternative)){
			this.allDominantAlternatives.add(alternative);
		}
	}
	
	public void addAllDominants(ArrayList<Alternative> alternatives){
		for(Alternative alternative: alternatives){
			this.addAllDominant(alternative);
		}
	}*/

	public void clearComparisonData(){
		this.rank = 0;
		this.rankFuzzy = 0;
		this.dominantAlternatives = new ArrayList<Alternative>();
	}
	
	public boolean isEqualValues(Alternative alternativeToCompare){
		for(int i = 0; i<alternativeToCompare.getValues().getSize(); i++){
			if(!this.getValues().getElementAt(i).equals(alternativeToCompare.getValues().getElementAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public String getRankToShow(){
		//TODO
		if(this.getRankFuzzy()>0)
			return this.getRank()+"";//"/"+this.getRankFuzzy();
		else
			return this.getRank()+"";
	}
	
	@Override 
	public boolean equals(Object alternativeToCompare) {
		if(alternativeToCompare instanceof Alternative){
			if(this.code.equals(((Alternative)alternativeToCompare).code) && this.description.equals(((Alternative)alternativeToCompare).description)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return this.getCode();
	}
	
	//Properties to represent a graph node
	public boolean isCompared;
	public boolean isVisitedNode;
	public int degree;

	//Getters and Setters
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ListModel getValues() {
		return values;
	}

	public void setValues(ListModel values) {
		this.values = values;
	}

	public int getFiq() {
		return fiq;
	}

	public void setFiq(int fiq) {
		this.fiq = fiq;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public ArrayList<Alternative> getDominantAlternatives() {
		if(dominantAlternatives == null){
			dominantAlternatives = new ArrayList<Alternative>();
		}
		return dominantAlternatives;
	}

	public void setDominantAlternatives(ArrayList<Alternative> dominant) {
		this.dominantAlternatives = dominant;
	}
	
	/*public ArrayList<Alternative> getAllDominantAlternatives() {
		if(allDominantAlternatives == null){
			allDominantAlternatives = new ArrayList<Alternative>();
		}
		return allDominantAlternatives;
	}

	public void setAllDominantAlternatives(ArrayList<Alternative> dominant) {
		this.allDominantAlternatives = dominant;
	}*/

	public int getRankFuzzy() {
		return this.rankFuzzy;
	}

	public void setRankFuzzy(int rankFuzzy) {
		//only sets the fuzzy rank of an alternative if it isn't defined yet or
		//if the new value for the fuzzy rank is smaller than the current one.
		if(this.rankFuzzy == 0){
			this.rankFuzzy = rankFuzzy;
		}else{
			if(this.rankFuzzy > rankFuzzy){
				this.rankFuzzy = rankFuzzy;
			}
		}
	}

	public ArrayList<Alternative> getEquivalentAlternatives() {
		return equivalentAlternatives;
	}
}
