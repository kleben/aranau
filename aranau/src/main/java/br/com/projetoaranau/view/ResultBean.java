package br.com.projetoaranau.view;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.swing.ListModel;

import br.com.projetoaranau.zapros_III.Alternative;
import br.com.projetoaranau.zapros_III.Criterion;
import br.com.projetoaranau.zapros_III.ZaprosMethod;

@SessionScoped
@ManagedBean(name = "resultBean")
public class ResultBean extends GenericBean {
	
	private static final long serialVersionUID = -2201205826752914001L;
	private HashMap rankAnterior;
	private String tabela = ""; 
	
	
	public ResultBean() {
		super();
		this.zapros.hashCode();
	}

	public String result() {
		zapros.orderAlternatives("fiq", zapros.getAlternatives());
		
		tabela = gerarTabelaHTML(ZaprosMethod.getInstance().getAlternatives(), "fiq");
		return "/views/result/result.xhtml";
	}
	
	public void gerarResultado(String tipoResultado){
		tabela = gerarTabelaHTML(ZaprosMethod.getInstance().getAlternatives(), tipoResultado);
	}
		
	public String gerarTabelaHTML(ArrayList alternativas, String tipoTabela){
    	String html = "<html>";
        String cor = "#E8E8E8";

        if(tipoTabela.equals("fiq")){
	    	String col1 = "Alternatives";
	    	String col2 = "Representation";
	    	String col3 = "FIQ";

	    	html = html + "<TABLE align=center valign=top width=\"550\" border=\"1\" bordercolorlight = 3300ff bordercolordark = ff00CC bgcolor = \""+cor+"\">"+
	  		  			  "<TR>"+
	  		  			  "<TD align=center valign=middle>  "+col1+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col2+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col3+"  </TD>"+
	  		  			  "</TR>";
	        for(int i = 0; i<alternativas.size(); i++){
	        	Alternative a = (Alternative) alternativas.get(i);
	        	ListModel valoresCriterios = a.getValues();

	        	if(cor.equals("#E8E8E8")){
	        		cor = "#F5F5F5";
	        	}else{
	        		cor = "#E8E8E8";
	        	}

	 	  		html = html +  "<TR>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getDescription()+"  </TD>";
	 	  					   //"<TH bgcolor=\""+cor+"\" rowspan=\"1\">  "+a.getDescricao()+" </TH>";
	        	Criterion c;
	        	String texto = "";
	        	for (int j=0; j<valoresCriterios.getSize(); j++){
	        		String codValor = valoresCriterios.getElementAt(j).toString();
					c = (Criterion)ZaprosMethod.getInstance().getCriteria().get(Integer.parseInt(codValor.substring(0,codValor.indexOf("/")))-1);
					//texto = texto +" "+ c.getValores().getElementAt(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					if(j!=valoresCriterios.getSize()-1){
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString()+"<br>";
					}else{
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					}
	        	}
	        	html = html + "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+texto+"  </TD>"+
	        				  "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getFiq()+"  </TD>" +
			                  "</TR>";
	        }
        }else if(tipoTabela.equals("rank")){
        	String col1 = "Rank";
        	String col2 = "Alternatives";
        	String col3 = "Representation";
	    	String col4 = "FIQ";
	    	rankAnterior = new HashMap();

	    	html = html + "<TABLE align=center valign=top width=\"575\" border=\"1\" bordercolorlight = 3300ff bordercolordark = ff00CC bgcolor = \""+cor+"\">"+
	  		  			  "<TR>"+
	  		  			  "<TD align=center valign=middle>  "+col1+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col2+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col3+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col4+"  </TD>"+
	  		  			  "</TR>";
	        for(int i = 0; i<alternativas.size(); i++){
	        	Alternative a = (Alternative) alternativas.get(i);
	        	ListModel valoresCriterios = a.getValues();

	        	if(cor.equals("#E8E8E8")){
	        		cor = "#F5F5F5";
	        	}else{
	        		cor = "#E8E8E8";
	        	}

	 	  		html = html +  "<TR>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getRankToShow()+"  </TD>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getDescription()+"  </TD>";
	 	  					   //"<TH bgcolor=\""+cor+"\" rowspan=\"1\">  "+a.getDescricao()+" </TH>";
	 	  		rankAnterior.put(a.getCode(), a.getRankToShow());
	        	Criterion c;
	        	String texto = "";
	        	for (int j=0; j<valoresCriterios.getSize(); j++){
	        		String codValor = valoresCriterios.getElementAt(j).toString();
					c = (Criterion)ZaprosMethod.getInstance().getCriteria().get(Integer.parseInt(codValor.substring(0,codValor.indexOf("/")))-1);
					//texto = texto +" "+ c.getValores().getElementAt(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					if(j!=valoresCriterios.getSize()-1){
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString()+"<br>";
					}else{
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					}
	        	}
	        	html = html + "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+texto+"  </TD>"+
				  			  "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getFiq()+"  </TD>" +
				  			  "</TR>";
	        }
        }else if(tipoTabela.equals("novoRank")){
        	String col1 = "Rank";
        	String col2 = "Previous Rank";
        	String col3 = "Alternatives";
        	String col4 = "Representation";
	    	String col5 = "FIQ";

	    	html = html + "<TABLE align=center valign=top width=\"600\" border=\"1\" bordercolorlight = 3300ff bordercolordark = ff00CC bgcolor = \""+cor+"\">"+
	  		  			  "<TR>"+
	  		  			  "<TD align=center valign=middle>  "+col1+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col2+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col3+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col4+"  </TD>"+
	  		  			  "<TD align=center valign=middle>  "+col5+"  </TD>"+
	  		  			  "</TR>";
	        for(int i = 0; i<alternativas.size(); i++){
	        	Alternative a = (Alternative) alternativas.get(i);
	        	ListModel valoresCriterios = a.getValues();

	        	if(cor.equals("#E8E8E8")){
	        		cor = "#F5F5F5";
	        	}else{
	        		cor = "#E8E8E8";
	        	}

	 	  		html = html +  "<TR>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getRank()+"  </TD>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+rankAnterior.get(a.getCode())+"  </TD>"+
	 	  					   "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getDescription()+"  </TD>";
	 	  					   //"<TH bgcolor=\""+cor+"\" rowspan=\"1\">  "+a.getDescricao()+" </TH>";
	        	Criterion c;
	        	String texto = "";
	        	for (int j=0; j<valoresCriterios.getSize(); j++){
	        		String codValor = valoresCriterios.getElementAt(j).toString();
					c = (Criterion)ZaprosMethod.getInstance().getCriteria().get(Integer.parseInt(codValor.substring(0,codValor.indexOf("/")))-1);
					//texto = texto +" "+ c.getValores().getElementAt(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					if(j!=valoresCriterios.getSize()-1){
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString()+"<br>";
					}else{
						texto = texto +" "+ c.getValues().get(Integer.parseInt(codValor.substring(codValor.indexOf("/")+1))-1).toString();
					}
	        	}
	        	html = html + "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+texto+"  </TD>"+
				  			  "<TD align=center valign=middle bgcolor=\""+cor+"\">  "+a.getFiq()+"  </TD>" +
				  			  "</TR>";
	        }
        }
        return html;
    }

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

}
