package br.com.projetoaranau.zapros_III;

import java.util.ArrayList;

import javax.swing.ListModel;

public class FIQ implements java.io.Serializable{

	private ZaprosMethod zapros;

	public FIQ(ZaprosMethod zapros){
		this.zapros = zapros;
	}

	//receives 2 alternatives the value of the comparison or the FIQ (if 1 alternative is the reference situation)
	//returns positive (first is better than the second), negative (second better than first), or null (equivalents)
	public int calculateFIQ(Alternative alternative1, Alternative alternative2){
		int fiqValue = 0;
		ArrayList<ArrayList<Integer>> ranksQV = this.getVectorOfQualityVariations(alternative1, alternative2);
		ArrayList<Integer> ranksQVCriterionAlt1 = ranksQV.get(0);
		ArrayList<Integer> ranksQVCriterionAlt2 = ranksQV.get(1);
		
		for (int i = 0; i<ranksQVCriterionAlt1.size(); i++){
			//If the value of the 1st alternative is worse then the other one, fiq is dimished
			if(ranksQVCriterionAlt1.get(i) == 0){
				fiqValue -= ranksQVCriterionAlt2.get(i);
			}else{
				//If the value of the 1st alternative is better then the other one, fiq is summed
				fiqValue += ranksQVCriterionAlt1.get(i);;
			}
		}
		return fiqValue;
	}
	
	public ArrayList<ArrayList<Integer>> getVectorOfQualityVariations(Alternative alternative1, Alternative alternative2){
		ListModel valuesAlt1 = alternative1.getValues();
		ListModel valuesAlt2 = alternative2.getValues();
		
		//array that stores the position of each alternative QV on the JSQV
		ArrayList<Integer> ranksQVCriterionAlt1 = new ArrayList<Integer>();
		ArrayList<Integer> ranksQVCriterionAlt2 = new ArrayList<Integer>();
		
		//for all the alternatives' criteria, do:
		for (int i = 0; i<zapros.getCriteria().size(); i++){
			String valueAlt1 = valuesAlt1.getElementAt(i).toString();
			String valueAlt2 = valuesAlt2.getElementAt(i).toString();

			//if the criteria values of A and B are not equal
			if(!(valueAlt1.equals(valueAlt2))){
				//same criterion
				String criterionCode = valueAlt1.substring(0, valueAlt1.indexOf("/"));
				Criterion criterion = (Criterion) zapros.findCriterionByCode(criterionCode);
												
				int positionValueCriterionAlt1 = Integer.parseInt(valueAlt1.substring(valueAlt1.indexOf("/")+1, valueAlt1.length()))-1;
				int positionValueCriterionAlt2 = Integer.parseInt(valueAlt2.substring(valueAlt2.indexOf("/")+1, valueAlt2.length()))-1;

				String valueCriterionAlt1 = criterion.getValues().get(positionValueCriterionAlt1).toString();
				String valueCriterionAlt2 = criterion.getValues().get(positionValueCriterionAlt2).toString();

				QV qv = criterion.findQV(valueCriterionAlt1, valueCriterionAlt2);

				if(qv == null){
					qv = criterion.findQV(valueCriterionAlt2, valueCriterionAlt1);
					ranksQVCriterionAlt1.add(zapros.findQVPositionOnScale(qv));
					ranksQVCriterionAlt2.add(0);
				}else{
					ranksQVCriterionAlt1.add(0);
					ranksQVCriterionAlt2.add(zapros.findQVPositionOnScale(qv));
				}
			}
		}
		ArrayList<ArrayList<Integer>> ranksQV = new ArrayList<ArrayList<Integer>>();
		ranksQV.add(ranksQVCriterionAlt1);
		ranksQV.add(ranksQVCriterionAlt2);
		return ranksQV;
	}

}
