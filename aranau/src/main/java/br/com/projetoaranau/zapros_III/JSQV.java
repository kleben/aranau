package br.com.projetoaranau.zapros_III;

import java.util.ArrayList;

public class JSQV implements java.io.Serializable{
	private ArrayList [] vetorQVs;
	private ArrayList escalaParcial = new ArrayList();
	private ArrayList escalaGeral;
	private Criterion criterioEscala1, criterioEscala2; //par de criterios da escala parcial em uso
	//CRITERIO ESCALA 1 POSSUI TODOS OS SEUS VALORES NA ESCALA (SETADO NO INICIO), CRITERIO 2 SER� INSERIDO
	private int contador;
	private int totalQVs;
	private Object [] anteriorPosterior = new Object [3]; //qd um qv � inserido junto a outro q ja esta na escala e existem outros qv antes/depois
														  //checar se o inserido � tambem melhor ou pior q os outros. Armazena o anterior e posterior
														  //para saber quando parar de comparar.


	public JSQV(int numeroQVs, int numeroEscalas){
		vetorQVs = new ArrayList [numeroEscalas]; //vai abrigar os arrays com as escalas de pares de criterios
		contador = 0;
		totalQVs = numeroQVs;

		/*escalaGeral = new ArrayList();
		String [] escala = {"1qv1", "<", "1qv3", "<","2qv1", "<","2qv3", "<","3qv1","<","5qv1","<","4qv1","<","5qv3","<",
				"4qv3","<","2qv2","<","4qv2","<","3qv3","<","3qv2","<","1qv2","<","5qv2"};
		for(int i = 0; i<escala.length;i++){
			escalaGeral.add(escala[i]);
		}*/
		/*     Combina��o
		 * int aux = 0;
		 * for(int i = 0; i<numeroQVs-1;i++){
		 *    aux = aux + (numeroQVs-i-1);
		}*/
	}

	public ArrayList validarPosicao(QV maior, QV menor, String qualificador){
		ArrayList questoes = null;
		//cond 1 =>parar checagem inserir apos..pois o qv q se movia foi dito melhor que o apos ele
		//cond 2 =>parar checagem inserir antes..pois o qv q se movia foi dito pior que o anterior a ele
		if ( (anteriorPosterior[1].equals(maior.getCode()) && anteriorPosterior[2].equals(menor.getCode()))
				|| (anteriorPosterior[0].equals(maior.getCode()) && anteriorPosterior[1].equals(menor.getCode()))){
			//se o qualificador for diferente (=)
			if(!qualificador.equals(escalaParcial.get(escalaParcial.indexOf(maior.getCode())+1).toString())){
				escalaParcial.set(escalaParcial.indexOf(maior.getCode())+1, qualificador);
			}

			//zerar valores do vetor anteriorPosterior
			for(int i=0; i<anteriorPosterior.length; i++){
				anteriorPosterior[i] = null;
			}

		}else{
			//se n�o acabou a checagem e for p inserir antes (maior)
			if(anteriorPosterior[1].equals(maior.getCode())){

				int pos = escalaParcial.indexOf(menor.getCode());

				int aux = pos-1;

				if(aux>=0){
					while("=".equals(escalaParcial.get(aux).toString())){
						aux = aux-2;
						if(aux<0){
							break;
						}
					}
				}

				if(aux+1==pos){
					escalaParcial.set(pos, maior.getCode());
					escalaParcial.set(pos+2, menor.getCode());
				}else{
					String copia = escalaParcial.get(aux+1).toString();
					pos = escalaParcial.indexOf(maior.getCode());

					escalaParcial.set(aux+1, maior.getCode());
					escalaParcial.set(aux+2, "<"); //setar sinal
					escalaParcial.set(pos-1, "=");
					escalaParcial.set(pos, copia);
				}

				if(escalaParcial.indexOf(maior.getCode())!=0){ //se n�o for o primeiro da escala
					String qvAnterior = escalaParcial.get(escalaParcial.indexOf(maior.getCode())-2).toString();
					if(!qvAnterior.substring(0, qvAnterior.indexOf("q")).equals(maior.getReferenceCriterion().getCode().toString())){
						pos = escalaParcial.indexOf(maior.getCode());

						anteriorPosterior[0] = escalaParcial.get(pos-2);
						anteriorPosterior[2] = escalaParcial.get(pos+2);

						questoes = new ArrayList();
						questoes.add(anteriorPosterior[0]);
						questoes.add(anteriorPosterior[1]);
					}else{
						for(int i=0; i<anteriorPosterior.length; i++){
							anteriorPosterior[i] = null;
						}
					}
				}else{
					for(int i=0; i<anteriorPosterior.length; i++){
						anteriorPosterior[i] = null;
					}
				}

			}else{//se n�o acabou a checagem e for p inserir apos (menor)

				int pos = escalaParcial.indexOf(maior.getCode());

				int aux = pos+1;
				if(aux<escalaParcial.size()){
					while("=".equals(escalaParcial.get(aux).toString())){
						aux = aux+2;
						if(aux>=escalaParcial.size()){
							break;
						}
					}
				}

				if(aux-1==pos){
					escalaParcial.set(pos, menor.getCode());
					escalaParcial.set(pos-2, maior.getCode());
				}else{
					String copia = escalaParcial.get(aux-1).toString();
					pos = escalaParcial.indexOf(menor.getCode());

					escalaParcial.set(aux-1, menor.getCode());
					escalaParcial.set(aux-2, "<"); //setar sinal
					escalaParcial.set(pos+1, "=");
					escalaParcial.set(pos, copia);
				}

				if(escalaParcial.indexOf(menor.getCode())!= escalaParcial.size()-1){ //se n�o for o ultimo da escala
					pos = escalaParcial.indexOf(menor.getCode());
					anteriorPosterior[0] = escalaParcial.get(pos-2);
					anteriorPosterior[2] = escalaParcial.get(pos+2);

					questoes = new ArrayList();
					questoes.add(anteriorPosterior[1]);
					questoes.add(anteriorPosterior[2]);
				}else{
					for(int i=0; i<anteriorPosterior.length; i++){
						anteriorPosterior[i] = null;
					}
				}

			}

		}

		return questoes;
	}

	public ArrayList inserirApos(QV maior, QV menor, String qualificador){
		ArrayList copia = new ArrayList();
		ArrayList questoes = null;

		int posMaior = escalaParcial.indexOf(maior.getCode());

		for (int i = 0; i<posMaior; i++){
			copia.add(escalaParcial.get(i));
		}

		copia.add(maior.getCode());

		//caso hajam qvs equivalentes ao maior
		int aux = posMaior+1;
		if(aux<escalaParcial.size()){
			while("=".equals(escalaParcial.get(aux).toString())){
				copia.add(escalaParcial.get(aux));
				copia.add(escalaParcial.get(aux+1));
				aux = aux+2;
				if(aux>=escalaParcial.size()){
					break;
				}
			}
		}

		copia.add(qualificador);
		copia.add(menor.getCode());
		for(int i=aux; i<escalaParcial.size(); i++){
			copia.add(escalaParcial.get(i));
		}

		escalaParcial = copia;

		if(escalaParcial.indexOf(menor.getCode()) != escalaParcial.size()-1){ // se nao for o ultimo
			anteriorPosterior[0] = escalaParcial.get(escalaParcial.indexOf(menor.getCode())-2).toString();
			anteriorPosterior[1] = menor.getCode();
			anteriorPosterior[2] = escalaParcial.get(escalaParcial.indexOf(menor.getCode())+2).toString();

			questoes = new ArrayList();
			questoes.add(anteriorPosterior[1]);
			questoes.add(anteriorPosterior[2]);
		}else{
			Criterion c = menor.getReferenceCriterion();
			if(c.getQvsPreferenceOrder()!=null){
				for(int i = c.getQvsPreferenceOrder().indexOf(menor.getCode())+1; i<c.getQvsPreferenceOrder().size();i++){
					escalaParcial.add(c.getQvsPreferenceOrder().get(i));
				}
			}
		}

		return questoes;
	}

	public void insereEmUltimo(QV menor, String qualificador){
		escalaParcial.add(qualificador);
		escalaParcial.add(menor.getCode());
		Criterion c = menor.getReferenceCriterion();
		if(c.getQvsPreferenceOrder()!=null){
			for(int i = c.getQvsPreferenceOrder().indexOf(menor.getCode())+1; i<c.getQvsPreferenceOrder().size();i++){
				escalaParcial.add(c.getQvsPreferenceOrder().get(i));
			}
		}
	}

	public void insereEmPrimeiro(QV maior, String qualificador){
		ArrayList copia = new ArrayList();
		copia.add(maior.getCode());
		copia.add(qualificador);
		for(int i = 0; i<escalaParcial.size();i++){
			copia.add(escalaParcial.get(i));
		}
		escalaParcial = copia;
	}

	public ArrayList inserirAntes(QV maior, QV menor, String qualificador){
		ArrayList copia = new ArrayList();
		ArrayList questoes = null;

		int posMenor = escalaParcial.indexOf(menor.getCode());

		//caso hajam qvs equivalentes ao menor
		int aux = posMenor-1;
		if(aux>=0){
			while("=".equals(escalaParcial.get(aux).toString())){
				aux = aux-2;
				if(aux<0){
					break;
				}
			}
		}

		for (int i = 0; i<aux+1; i++){
			copia.add(escalaParcial.get(i));
		}

		copia.add(maior.getCode());
		copia.add(qualificador);

		for(int i=aux+1; i<escalaParcial.size(); i++){
			copia.add(escalaParcial.get(i));
		}

		escalaParcial = copia;



		if(escalaParcial.indexOf(maior.getCode()) != 0){//se nao for o primeiro da lista

			String qvAnterior = escalaParcial.get(escalaParcial.indexOf(maior.getCode())-2).toString();
			if(!qvAnterior.substring(0, qvAnterior.indexOf("q")).equals(maior.getReferenceCriterion().getCode().toString())){
				anteriorPosterior[0] = escalaParcial.get(escalaParcial.indexOf(maior.getCode())-2).toString();
				anteriorPosterior[1] = maior.getCode();
				anteriorPosterior[2] = escalaParcial.get(escalaParcial.indexOf(maior.getCode())+2).toString();

				questoes = new ArrayList();
				questoes.add(anteriorPosterior[0]);
				questoes.add(anteriorPosterior[1]);
			}
		}

		return questoes;
	}

	public void inserirEquivalentes(QV maior, QV menor, String qualificador){
		int pos;
		ArrayList copia = new ArrayList();
		if(escalaParcial.contains(maior.getCode())){
			pos = escalaParcial.indexOf(maior.getCode());
		}else {
			pos = escalaParcial.indexOf(menor.getCode());
		}

		for (int i = 0; i<pos; i++){
			copia.add(escalaParcial.get(i));
		}

		copia.add(maior.getCode());
		copia.add(qualificador);
		copia.add(menor.getCode());
		for(int i=pos+1; i<escalaParcial.size(); i++){
			copia.add(escalaParcial.get(i));
		}
		escalaParcial = copia;
	}

	public ArrayList inserirEscalaParcial(QV maior, QV menor, String qualificador){
		ArrayList questoes = null;

		//se a escala parcial estiver vazia
		if(escalaParcial.size() == 0){
			criterioEscala1 = menor.getReferenceCriterion();
			criterioEscala2 = maior.getReferenceCriterion();
			if(criterioEscala1.getQvsPreferenceOrder()!=null){
				escalaParcial = (ArrayList) criterioEscala1.getQvsPreferenceOrder().clone();
				escalaParcial.add(0,maior.getCode());
				escalaParcial.add(1,qualificador);
			}else if(criterioEscala2.getQvsPreferenceOrder()!=null){
				escalaParcial = (ArrayList) criterioEscala2.getQvsPreferenceOrder().clone();
				escalaParcial.add(qualificador);
				escalaParcial.add(menor.getCode());
				criterioEscala1 = maior.getReferenceCriterion();
				criterioEscala2 = menor.getReferenceCriterion();
			}else{
				escalaParcial.add(maior.getCode());
				escalaParcial.add(qualificador);
				escalaParcial.add(menor.getCode());
			}

		}else if(anteriorPosterior[1]!=null){ //se os 2 j� existiam na escala.. para certificar-se da posicao de um qv
			questoes = validarPosicao(maior, menor, qualificador);
		}else if (maior.getCode().equals(escalaParcial.get(escalaParcial.size()-1).toString())){ //se o QV maior for o ultimo da escala parcial, inserir menor apos esse
			insereEmUltimo(menor, qualificador);
		}else if(menor.getCode().equals(escalaParcial.get(0))){ //se o QV menor for o primeiro da escala parcial, inserir maior antes desse
			insereEmPrimeiro(maior, qualificador);
		}else if(!qualificador.equals("=")){ //se os qvs nao forem equivalentes
			if(escalaParcial.contains(maior.getCode())){ //se o mais preferivel ja esta na escala, insere o menos
														   //preferivel logo apos esse e pergunta qto ao menos e o seguinte
				questoes = inserirApos(maior, menor, qualificador);
			}else if(escalaParcial.contains(menor.getCode())){ //se o menos preferivel ja esta na escala, insere o mais
																 //preferivel antes desse e pergunta qto ao mais e o anterior
				questoes = inserirAntes(maior, menor, qualificador);
			}
		}else{ //caso QVs sejam iguais
			inserirEquivalentes(maior, menor, qualificador);
		}

		Criterion c1 = maior.getReferenceCriterion();
		Criterion c2 = menor.getReferenceCriterion();
		if(anteriorPosterior[1] == null){
			if(c1!=criterioEscala1){ //se c1 nao estiver totalmente inserido na escala
				if(c1.getQvsPreferenceOrder()!=null && c1.getQvsPreferenceOrder().indexOf(maior.getCode())!=c1.getQvsPreferenceOrder().size()-1){
					if(c1.getQvsPreferenceOrder().get(c1.getQvsPreferenceOrder().indexOf(maior.getCode())+1).toString().equals("=")){
						int aux = c1.getQvsPreferenceOrder().indexOf(maior.getCode())+1;
						if(aux<c1.getQvsPreferenceOrder().size()){
							while("=".equals(c1.getQvsPreferenceOrder().get(aux).toString())){
								if(!escalaParcial.contains(c1.getQvsPreferenceOrder().get(aux+1).toString())){
									QV qv = c1.findQV(c1.getQvsPreferenceOrder().get(aux+1).toString());
									inserirEquivalentes(maior, qv, "=");
									aux = aux+2;
									if(aux>=c1.getQvsPreferenceOrder().size()){
										break;
									}
								}
							}
						}
					}
				}
			}
			if(c2!=criterioEscala1){ //se c2 nao estiver totalmente inserido na escala
				if(c2.getQvsPreferenceOrder()!=null && c2.getQvsPreferenceOrder().indexOf(menor.getCode())!=c2.getQvsPreferenceOrder().size()-1){
					if(c2.getQvsPreferenceOrder().get(c2.getQvsPreferenceOrder().indexOf(menor.getCode())+1).toString().equals("=")){
						int aux = c2.getQvsPreferenceOrder().indexOf(menor.getCode())+1;
						if(aux<c2.getQvsPreferenceOrder().size()){
							while("=".equals(c2.getQvsPreferenceOrder().get(aux).toString())){
								if(!escalaParcial.contains(c2.getQvsPreferenceOrder().get(aux+1).toString())){
									QV qv = c2.findQV(c2.getQvsPreferenceOrder().get(aux+1).toString());
									inserirEquivalentes(menor, qv, "=");
									aux = aux+2;
									if(aux>=c2.getQvsPreferenceOrder().size()){
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		if (questoes == null){
			questoes = gerarQuestoes();
		}

		imprimirJSQV();

		return questoes;
	}

	public ArrayList gerarQuestoes(){
		ArrayList questoes = new ArrayList();
		//pegar QV do criterio 1 em posicao intermediaria do meio da escala, pois crit. 1 ja se encontra na escala
		if(criterioEscala1.getQvsPreferenceOrder()!=null && criterioEscala2.getQvsPreferenceOrder()!=null){
			ArrayList ordemC1 = criterioEscala1.getQvsPreferenceOrder();
			ArrayList ordemC2 = criterioEscala2.getQvsPreferenceOrder();

			String qvC2inserir = null; //qv de criterio 2 que nao esta na escala
			String qvC2inserido = null;
			for(int i = 0; i<ordemC2.size();i++){
				if(escalaParcial.contains(ordemC2.get(i))){
					qvC2inserido = ordemC2.get(i).toString();
				}else{
					qvC2inserir = ordemC2.get(i).toString();
					break;
				}
				i++;
			}
			if(qvC2inserir != null){
				int pos = escalaParcial.indexOf(qvC2inserido)+2; //posicao do qv imediatamente apos o ultimo qv de c2
				//se o ultimo qv inserido for o �ltimo da escala parcial
				if(pos > escalaParcial.size()){
					int posInserir = ordemC2.indexOf(qvC2inserir);
					for (int i = posInserir; i<ordemC2.size();i++){
						qvC2inserir = ordemC2.get(i).toString();
						insereEmUltimo(criterioEscala2.findQV(qvC2inserir), "<");
					}
				}else{
					//considera apenas a parte da escala do ultimo qv do crit2 inserido at� o fim da escala - subescala
					int meioSubEscala = (escalaParcial.size()-1-pos)/2;
					if(meioSubEscala%2 != 0){
						meioSubEscala = meioSubEscala-1;
					}
					questoes.add(escalaParcial.get(meioSubEscala+pos));
					questoes.add(qvC2inserir);
				}
			}
		}else{
			if(criterioEscala1 == criterioEscala2){
				String qvInserir = null; //qv do criterio que nao esta na escala
				for(int i = 0; i<criterioEscala1.getQualitativeValues().size();i++){
					String qv = ((QV)criterioEscala1.getQualitativeValues().get(i)).getCode();
					if(!escalaParcial.contains(qv)){
						qvInserir = qv;
					}
				}
				if(qvInserir != null){
					int qvMeioEscala = escalaParcial.size()/2;
					if(qvMeioEscala%2 != 0){
						qvMeioEscala = qvMeioEscala-1;
					}
					questoes.add(escalaParcial.get(qvMeioEscala));
					questoes.add(qvInserir);
				}
			}
		}
		if (questoes.size()==0){
			return null;
		}
		return questoes;
	}

	public int findQvValue(String qv){
		int positionQV = escalaGeral.indexOf(qv);
		int valueQV = 1;
		for(int i = 0; i<positionQV; i++){
			if(escalaGeral.get(i).toString().equals("<"))
				valueQV++;
		}
		return valueQV;
	}

	public boolean escalasParciaisContemCriterio(Criterion c){
		for(int i = 0; i<vetorQVs.length; i++){
			if(vetorQVs[i]!=null){
				ArrayList parcial = vetorQVs[i];
				for(int j = 0; j<parcial.size();j++){
					String obj = parcial.get(j).toString();
					if(obj.substring(0, obj.indexOf('q')).equals(c.getCode().toString())){
						return true;
					}
					j++;
				}
			}else{
				if(escalaParcial.size()!=0){
					return ultimaEscalaParcialContemCriterio(c);
				}
			}

		}
		return false;
	}

	public boolean ultimaEscalaParcialContemCriterio(Criterion c){
		for(int j = 0; j<escalaParcial.size();j++){
			String obj = escalaParcial.get(j).toString();
			if(obj.substring(0, obj.indexOf('q')).equals(c.getCode().toString())){
				return true;
			}
			j++;
		}
		return false;
	}

	public ArrayList finalizaEscalaParcial(){
		int c1 = -1;
		int c2 = -1;
		ArrayList indices = new ArrayList();

/*
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameValidarEscalaParcial().setVisible(true);
            }
        });
*/
		vetorQVs[contador] = escalaParcial;
		contador++;

		for(int i = 0; i<escalaParcial.size(); i++){
			String obj = escalaParcial.get(i).toString();
			if(c1 == -1){
				c1 = Integer.parseInt(obj.substring(0, obj.indexOf('q')));
			}else{
				if(c1 != Integer.parseInt(obj.substring(0, obj.indexOf('q'))) && c2==-1){
					c2 = Integer.parseInt(obj.substring(0, obj.indexOf('q')));
					break;
				}
			}
			i++;
		}

		escalaParcial = new ArrayList();

		indices.add(c1);
		indices.add(c2);

		return indices;
	}

	public ArrayList qvsEmComum(ArrayList lista, ArrayList preferencias){
		ArrayList comum = new ArrayList();
		for(int i = 0; i<preferencias.size();i++){
			if(lista.contains(preferencias.get(i))){
				comum.add(preferencias.get(i));
			}
		}
		return comum;
	}

	public ArrayList modificarEscalas(ArrayList preferencias){
		//remover o primeiro elemento --> "contradicao"
		preferencias.remove(0);
		//remover o ultimo qv q � igual ao primeiro
    	preferencias.remove(preferencias.size()-1);
		for(int i = 0; i<contador;i++){
			ArrayList lista = vetorQVs[i];
			ArrayList qvsEmComum = qvsEmComum(lista, preferencias);
			for (int j = 0; j<qvsEmComum.size()-1;j++){
				//dividir + 1 pois lista contem os operadores <
				int indiceListaqv1 = lista.indexOf(qvsEmComum.get(j))/2;
				int indiceListaqv2 = lista.indexOf(qvsEmComum.get(j+1))/2;
				int indicePrefqv1 = preferencias.indexOf(qvsEmComum.get(j));
				int indicePrefqv2 = preferencias.indexOf(qvsEmComum.get(j+1));
				if(indiceListaqv1<indiceListaqv2){    //se na escala 1 � preferivel a 2
					if(indicePrefqv1>indicePrefqv2){  //se no array das contradi��es 2 � preferivel a 1
						//adicionar 2 na frente de 1
						lista.add(lista.indexOf(qvsEmComum.get(j)), lista.get(lista.indexOf(qvsEmComum.get(j+1))));
						lista.add(indiceListaqv1*2+1,"<");
						//remover 2 da lista
						lista.remove(indiceListaqv2*2);
						lista.remove(indiceListaqv2*2+1);
					}
				}else if(indiceListaqv1>indiceListaqv2){//se na escala 2 � preferivel a 1
					if(indicePrefqv1<indicePrefqv2){    //se no array das contradi��es 1 � preferivel a 2
						//adicionar 2 na frente de 1
						lista.add(lista.indexOf(qvsEmComum.get(j+1)), lista.get(lista.indexOf(qvsEmComum.get(j))));
						lista.add(indiceListaqv2*2+1,"<");
						//remover 1 da lista
						lista.remove(indiceListaqv1*2+2);
						lista.remove(indiceListaqv1*2+1);
					}
				}
			}
			//vetorQVs[i] = lista;
		}
		ArrayList contradicao = montarJSQV();
		if(contradicao!=null){
			for(int i = 1; i<contradicao.size();i++){
				contradicao.set(i, ZaprosMethod.getInstance().findQV(contradicao.get(i).toString()).getChoiceText());
			}
			return contradicao;
		}else{
			return null;				//se n�o houverem mais contradicoes
		}
	}

	public ArrayList montarJSQV(){
//		String qv = "";
		if(contador == 1){ //se houverem 2 crit�rios
			escalaGeral = vetorQVs[0];
		}else{
			boolean continua = existemQVs();
			while(continua){
				ArrayList qvs = findQVDominante();
				if(!qvs.get(0).toString().equals("contradicao")){  //se nao houve contradicao
					if(escalaGeral == null){
						 escalaGeral = new ArrayList();
					}

					for (int i = 0; i<qvs.size();i++){
						escalaGeral.add(qvs.get(i));
						if(i<qvs.size()-1){
							escalaGeral.add("=");
						}
					}
					for(int i = 0; i<contador; i++){
						ArrayList parcial = vetorQVs[i];
						for(int j = 0; j<qvs.size(); j++){
							if(parcial.contains(qvs.get(j))){
								int pos = parcial.indexOf(qvs.get(j));
								if(pos+1<parcial.size()){   //se n�o for o ultimo
									parcial.remove(pos+1);  //remover operador
								}
								parcial.remove(pos);    // remover qv
							}
						}
					}
					continua = existemQVs();
					if(continua){
						escalaGeral.add("<");
					}

				}else{
					return qvs;
				}
			}
		}
		imprimirEscalaGeral();
		return null;
	}

	public boolean existemQVs(){
		for (int i = 0; i<contador; i++){
			ArrayList parcial = vetorQVs[i];
			if(!parcial.isEmpty()){
				return true;
			}
		}
		return false;
	}

	public ArrayList findQVDominante(){
		ArrayList qv = new ArrayList();

		ArrayList antigosPreferiveis = new ArrayList(); //armazena todos que ja foram ou s�o prefer�veis

		//quando o qv prefer�vel � mudado, seta-se i=0 para testar todas as esc parciais novamente
		for(int i = 0; i<contador; i++){
			ArrayList parcial = vetorQVs[i];
			if(!parcial.isEmpty()){
				if(qv.size()==0){
					qv.add(parcial.get(0).toString()); //pega o 1o qv da 1a escala
					antigosPreferiveis.add(qv.get(0));
				}else{
					for(int j = 0; j<qv.size();j++){
						if(parcial.contains(qv.get(j).toString())){
							int pos = parcial.indexOf(qv.get(j).toString());
							if(pos!=0){	//se n�o for o primeiro
								if(!(parcial.get(pos-1).toString().equals("="))){ //se o antecessor for prefer�vel, muda o qv dominante
									qv = new ArrayList();
									qv.add(parcial.get(0).toString());
									if(!antigosPreferiveis.contains(qv.get(0))){
										antigosPreferiveis.add(qv.get(0));
										i = -1;
										break;
									}else{
										return eliminaContradicao(antigosPreferiveis, qv.get(0).toString());
									}
								}else{
									int aux = pos-1;
									int tamanhoArrayQv = qv.size();
									while(aux>0 && "=".equals(parcial.get(aux).toString())){
										if(!qv.contains(parcial.get(aux-1))){
											qv.add(parcial.get(aux-1));
											if(!antigosPreferiveis.contains(parcial.get(aux-1))){
												antigosPreferiveis.add(parcial.get(aux-1));
											}else{
												return eliminaContradicao(antigosPreferiveis, parcial.get(aux-1).toString());
											}
										}
										aux = aux-2;
									}
									if(aux>0){
										qv = new ArrayList();
										qv.add(parcial.get(0).toString());
										if(!antigosPreferiveis.contains(qv.get(0))){
											antigosPreferiveis.add(qv.get(0));
											i = -1;
											break;
										}else{
											return eliminaContradicao(antigosPreferiveis, qv.get(0).toString());
										}
									}else{
										if(tamanhoArrayQv != qv.size()){ //se foram adicionados qvs iguais ao vetor, comecar novamente
											i = -1;
											break;
										}
									}
								}
							}else{//se for o primeiro, adiciona todos os iguais a ele
								int aux = pos+1;
								int tamanhoArrayQv = qv.size();
								while(aux<parcial.size() && "=".equals(parcial.get(aux).toString())){//se for o primeiro e tiverem proximos iguais
									if(!qv.contains(parcial.get(aux+1))){
										qv.add(parcial.get(aux+1));
										if(!antigosPreferiveis.contains(parcial.get(aux+1))){
											antigosPreferiveis.add(parcial.get(aux+1));
										}else{
											return eliminaContradicao(antigosPreferiveis, parcial.get(aux+1).toString());
										}
									}
									aux = aux+2;
								}
								if(tamanhoArrayQv != qv.size()){ //se foram adicionados qvs iguais ao vetor, comecar novamente
									i = -1;
									break;
								}
							}
						}
					}
				}
			}
		}

		return qv;
	}

	public ArrayList eliminaContradicao(ArrayList anteriores, String qvContradicao){
		ArrayList retorno = new ArrayList();
		retorno.add("contradicao");

		int pos = anteriores.indexOf(qvContradicao);

		//adicionar todos os qvs que fazem parte do ciclo
		for(int i = pos; i<anteriores.size();i++){
			retorno.add(anteriores.get(i));
		}
		retorno.add(qvContradicao);
		/*retorno.add(anteriores.get(pos+1));

		//adicionar o ultimo qv q foi menos preferivel que o qv q gerou a contradicao
		retorno.add(anteriores.get(anteriores.size()-1));
		*/

		return retorno;
	}

	public void atualizarUltimaEscalaParcial(ArrayList escala){
		vetorQVs[contador-1] = escala;
	}

	public void imprimirJSQV(){
		System.out.println("");
		System.out.println("----------------- JSQV " + contador+" -----------------");
		for (int i = 0; i< escalaParcial.size();i++){
			if(i == escalaParcial.size()-1){//mudar de linha na impress�o do ultimo
				System.out.println(escalaParcial.get(i).toString());
			}else{
				System.out.print(escalaParcial.get(i).toString());
			}
		}
		System.out.println("------------------------------------------");
	}

	public void imprimirEscalaGeral(){
		System.out.println("");
		System.out.println("----------------- Escala Geral -----------------");
		for (int i = 0; i< escalaGeral.size();i++){
			if(i == escalaGeral.size()-1){//mudar de linha na impress�o do ultimo
				System.out.println(escalaGeral.get(i).toString());
			}else{
				System.out.print(escalaGeral.get(i).toString());
			}
		}
		System.out.println("------------------------------------------------");
	}

	public ArrayList[] getVetorQVs() {
		return vetorQVs;
	}

	public void setVetorQVs(String[] vetorQVs) {
		vetorQVs = vetorQVs;
	}

	public ArrayList getEscalaParcial() {
		return escalaParcial; //retorna a escala parcial em uso
	}

	public void setEscalaParcial(ArrayList escalaParcial) {
		this.escalaParcial = escalaParcial;
	}

	public ArrayList getUltimaEscalaParcial(){
		return vetorQVs[contador-1];
	}

	public int getTotalQVs() {
		return totalQVs;
	}

	public void setTotalQVs(int totalQVs) {
		this.totalQVs = totalQVs;
	}

	public ArrayList getEscalaGeral() {
		return escalaGeral;
	}

	public void setEscalaGeral(ArrayList escalaGeral) {
		this.escalaGeral = escalaGeral;
	}
}
