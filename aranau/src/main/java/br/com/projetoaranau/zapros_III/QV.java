package br.com.projetoaranau.zapros_III;

public class QV implements java.io.Serializable{

	private String code;
	private String value1Criterion;
	private String value2Criterion;
	private Criterion referenceCriterion;

	public QV(Criterion referenceCriterion, int positionValue1, int positionValue2, int count){
		this.referenceCriterion = referenceCriterion;
		code = referenceCriterion.getCode()+"qv"+count;
		value1Criterion = referenceCriterion.getValues().get(positionValue1).toString();
		value2Criterion = referenceCriterion.getValues().get(positionValue2).toString();
		printQV();
	}

	public void printQV(){
		System.out.println("--------------------- QV ---------------------");
		System.out.println("Code: "+ this.getCode());
		System.out.println("Value: " + this.value1Criterion+" --> "+this.value2Criterion);
		System.out.println("----------------------------------------------");
	}
	
	@Override 
	public boolean equals(Object qvToCompare) {
		if(qvToCompare instanceof QV){
			if(this.code.equals(((QV)qvToCompare).code) && this.value1Criterion.equals(((QV)qvToCompare).value1Criterion)
					&& this.value2Criterion.equals(((QV)qvToCompare).value2Criterion) 
					&& this.referenceCriterion.equals(((QV)qvToCompare).referenceCriterion)){
				return true;
			}
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Criterion getReferenceCriterion() {
		return referenceCriterion;
	}

	public void setReferenceCriterion(Criterion referenceCriterion) {
		this.referenceCriterion = referenceCriterion;
	}

	public String getValue1Criterion() {
		return value1Criterion;
	}

	public void setValue1Criterion(String value1Criterion) {
		this.value1Criterion = value1Criterion;
	}

	public String getValue2Criterion() {
		return value2Criterion;
	}

	public void setValue2Criterion(String value2Criterion) {
		this.value2Criterion = value2Criterion;
	}

	public String getChoiceText() {
		String text = "";
		text = new String(getReferenceCriterion().getDescription()+
					":'"+getValue1Criterion() + "'*FIM*'" + getValue2Criterion() + "'");
		return text;
	}
	
	@Override
	public String toString() {
		return this.code;
	}


}

