package br.com.projetoaranau.util;

import java.util.ArrayList;

import br.com.projetoaranau.zapros_III.Alternative;
import br.com.projetoaranau.zapros_III.ZaprosMethod;

public class MergeSortZapros {

	ZaprosMethod zapros = ZaprosMethod.getInstance();

	public ArrayList mergeSort(ArrayList array, int index1, int arraySize, String parameter){
		if (index1 < arraySize-1){
			int index2 = (int) Math.floor((index1 + arraySize)/2);
			mergeSort(array, index1, index2, parameter);
			mergeSort(array, index2, arraySize, parameter);

			/*if(parameter.equals("alternatives")){
				zapros = ZaprosMethod.getInstance();
				mergeAlternatives(array, index1, index2, arraySize);
			}else{
				
			}*/
			merge(array, index1, index2, arraySize, parameter);
		}
		return array;
	}

	public void merge(ArrayList array, int index1, int index2, int arraySize, String parameter){
		int aux1 = index2 - index1;
		int aux2 = arraySize - index2;

		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();
		for (int i=0; i<aux1;i++){
			list1.add(array.get(index1+i));
		}
		for (int i=0; i<aux2;i++){
			list2.add(array.get(index2+i));
		}
		int i = 0;
		int j = 0;
		if(parameter == null){
			for (int k = index1; k<arraySize;k++){
				if(i>=list1.size() && j<list2.size()){
					array.set(k,list2.get(j));
					j++;
				}else if(i<list1.size() && j>=list2.size()){
					array.set(k,list1.get(i));
					i++;
				}else if(((Integer)list1.get(i))<=((Integer)list2.get(j))){
					array.set(k,list1.get(i));
					i++;
				}else{
					array.set(k,list2.get(j));
					j++;
				}
			}
		}else if(parameter.equals("fiq")){
			for (int k = index1; k<arraySize;k++){
				if(i>=list1.size() && j<list2.size()){
					array.set(k,list2.get(j));
					j++;
				}else if(i<list1.size() && j>=list2.size()){
					array.set(k,list1.get(i));
					i++;
				}else if(((Alternative)list1.get(i)).getFiq()<=((Alternative)list2.get(j)).getFiq()){
					array.set(k,list1.get(i));
					i++;
				}else{
					array.set(k,list2.get(j));
					j++;
				}
			}
		}else if(parameter.equals("rank")){
			for (int k = index1; k<arraySize;k++){
				if(i>=list1.size() && j<list2.size()){
					array.set(k,list2.get(j));
					j++;
				}else if(i<list1.size() && j>=list2.size()){
					array.set(k,list1.get(i));
					i++;
				}else if(((Alternative)list1.get(i)).getRank()<=((Alternative)list2.get(j)).getRank()){
					array.set(k,list1.get(i));
					i++;
				}else{
					array.set(k,list2.get(j));
					j++;
				}
			}
		}else if(parameter.equals("alternatives")){
			for (int k = index1; k<arraySize;k++){
				if(i>=list1.size() && j<list2.size()){
					array.set(k,list2.get(j));
					j++;
				}else if(i<list1.size() && j>=list2.size()){
					array.set(k,list1.get(i));
					i++;
				}else if(zapros.compare((Alternative)list1.get(i),(Alternative)list2.get(j)).equals(((Alternative)list1.get(i)).getCode())){
					//if the ith alternative of list1 is better than the one on the jth position of list2
					array.set(k,list1.get(i));
					i++;
				}else if(zapros.compare((Alternative)list1.get(i),(Alternative)list2.get(j)).equals(((Alternative)list2.get(j)).getCode())){
					//if the jth alternative of list2 is better than the one on the ith position of list1
					array.set(k,list2.get(j));
					j++;
				}
			}
		}
	}

	public void mergeAlternatives(ArrayList array, int index1, int index2, int arraySize){
		int aux1 = index2 - index1;
		int aux2 = arraySize - index2;

		ArrayList list1 = new ArrayList();
		ArrayList list2 = new ArrayList();
		for (int i=0; i<aux1;i++){
			list1.add(array.get(index1+i));
		}
		for (int i=0; i<aux2;i++){
			list2.add(array.get(index2+i));
		}
		int i = 0;
		int j = 0;
		for (int k = index1; k<arraySize;k++){
			if(i>=list1.size() && j<list2.size()){
				array.set(k,list2.get(j));
				j++;
			}else if(i<list1.size() && j>=list2.size()){
				array.set(k,list1.get(i));
				i++;
			}else if(zapros.compare((Alternative)list1.get(i),(Alternative)list2.get(j)).equals(((Alternative)list1.get(i)).getCode())){
				//if the ith alternative of list1 is better than the one on the jth position of list2
				array.set(k,list1.get(i));
				i++;
			}else if(zapros.compare((Alternative)list1.get(i),(Alternative)list2.get(j)).equals(((Alternative)list2.get(j)).getCode())){
				//if the jth alternative of list2 is better than the one on the ith position of list1
				array.set(k,list2.get(j));
				j++;
			}
		}
	}

}
