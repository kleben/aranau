package br.com.projetoaranau.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.UploadedFile;

@SessionScoped
@ManagedBean(name = "genericBean")
public class GenericBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2460168561104735770L;
	private UploadedFile file;
	
	
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
