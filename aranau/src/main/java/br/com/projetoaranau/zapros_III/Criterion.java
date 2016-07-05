package br.com.projetoaranau.zapros_III;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;

public class Criterion implements java.io.Serializable{

	private String code;
	private String description;
	private List<String> values;
	private ArrayList<QV> qualitativeValues;
	private ArrayList<String> qvsPreferenceOrder;

	public Criterion(String code, String description, List<String> list){
		this.code = code;
		this.description = description;
		this.values = list;
		printCriterion();
		addQVs();
	}

	public void addQVs(){
		qualitativeValues = new ArrayList<QV>();
		int aux = 0;
		int totalValues = this.getValues().size();
		for(int i=0; i<totalValues-1;i++){
			aux = aux + (totalValues-i-1);
		}
		int positionValue1 = 0;
		int positionValue2 = positionValue1+1;
		for(int i = 0; i<aux; i++){
			if(positionValue2 == totalValues){
				if(positionValue1+1 < totalValues-1){
					positionValue1++;
					positionValue2 = positionValue1+1;
				}
			}
			QV qv = new QV(this, positionValue1, positionValue2, i+1);
			qualitativeValues.add(qv);
			positionValue2++;
		}
	}

	public QV findQV(String code){
		ArrayList<QV> qvs = this.getQualitativeValues();
		for (int i = 0; i<qvs.size(); i++){
			QV qv = qvs.get(i);
			if(qv.getCode().equals(code)){
				return qv;
			}
		}
		return null;
	}

	public QV findQV(String value1, String value2){
		for(int i = 0; i<qualitativeValues.size(); i++){
			QV qv = (QV) qualitativeValues.get(i);
			//if the qv's value 1 is equal to the first parameter (value1)
			if(qv.getValue1Criterion().equals(value1)){
				//if the qv's value 2 is equal to the second parameter
				if(qv.getValue2Criterion().equals(value2)){
					return qv;
				}
			}
		}
		return null;
	}

	public QV isPreferableTo(String codeQV1, String codeQV2){
		QV qv1 = this.findQV(codeQV1);
		QV qv2 = this.findQV(codeQV2);
		if(qvsPreferenceOrder.indexOf(codeQV1)<qvsPreferenceOrder.indexOf(codeQV2)){
			return qv1;
		}else if (qvsPreferenceOrder.indexOf(codeQV2)<qvsPreferenceOrder.indexOf(codeQV1)){
			return qv2;
		}else{
			return null;
		}
	}

	public void printCriterion(){
		System.out.println("");
		System.out.println("----------------- Criterion -----------------");
		System.out.println("Description: "+ this.getDescription());
		System.out.println("Values : " + values.get(0));
		for (int i = 1; i<values.size();i++){
			System.out.println("          "+values.get(i));
		}
		System.out.println("-----------------------------------------------");
	}

	//Getters and Setters
	public ArrayList<QV> getQualitativeValues() {
		return qualitativeValues;
	}

	public void setQualitativeValues(ArrayList<QV> qualitativeValues) {
		this.qualitativeValues = qualitativeValues;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public ArrayList<String> getQvsPreferenceOrder() {
		return qvsPreferenceOrder;
	}

	public void setQvsPreferenceOrder(ArrayList<String> qvsPreferenceOrder) {
		this.qvsPreferenceOrder = qvsPreferenceOrder;
	}

}
