package br.com.projetoaranau.view;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import br.com.projetoaranau.zapros_III.ZaprosMethod;

@SessionScoped
@Named("genericBean")
public class GenericBean implements Serializable{
	
	private static final long serialVersionUID = -2460168561104735770L;
	private UploadedFile fileUploaded;
	protected ZaprosMethod zapros;
	
	
	public GenericBean(){
		if (zapros == null)
			zapros = ZaprosMethod.getInstance();
	}
	public void upload() {
        if(fileUploaded != null) {
            FacesMessage message = new FacesMessage("Succesful", fileUploaded.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
	 
	public UploadedFile getFileUploaded() {
		return fileUploaded;
	}
	public void setFileUploaded(UploadedFile fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	public ZaprosMethod getZapros() {
		return zapros;
	}

	public void setZapros(ZaprosMethod zapros) {
		this.zapros = zapros;
	}
	
}
