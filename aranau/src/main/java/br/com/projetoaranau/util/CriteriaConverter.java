package br.com.projetoaranau.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.projetoaranau.model.Criteria;

@FacesConverter(value = "criteriaConverter")
public class CriteriaConverter implements Converter {

	private static final Map<Criteria, String> CRITERIAS = new HashMap<Criteria, String>();

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String stringValor) {

		if ("".equals(stringValor)) {
			return null;
		}
		// Procura pelo criteria correspondente ao select item selecionado:
		for (Entry<Criteria, String> entry : CRITERIAS.entrySet()) {
			if (entry.getValue().equals(stringValor)) {
				return entry.getKey();
			}
		}

		throw new RuntimeException("Erro inesperado no converter " + getClass().getName()
				+ ": Referência para uma entidade foi perdida.");
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object criteria) {
		// Se o objeto selecionado for o valor vazio retorna
		// a string contendo a descrição.
		if ("".equals(criteria.toString())) {
			return "";
		}

		// Adiciona os demais Fornecedores, que são Criteria ao Map de CRITERIAS, id
		// do mesmo.
		synchronized (CRITERIAS) {
			if (CRITERIAS.containsKey(criteria)) {
				CRITERIAS.remove(criteria);
			}
			CRITERIAS.put((Criteria) criteria, ((Criteria) criteria).getId().toString());
			return CRITERIAS.get(criteria);
		}
	}
}
