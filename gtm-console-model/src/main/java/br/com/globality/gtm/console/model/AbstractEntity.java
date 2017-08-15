package br.com.globality.gtm.console.model;

import java.io.Serializable;

import javax.persistence.Transient;

public abstract class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6694596414157850637L;
		
	@Transient
	private Long paginacaoQtdeRegConsulta;
	
	public Long getPaginacaoQtdeRegConsulta() {
		return paginacaoQtdeRegConsulta;
	}

	public void setPaginacaoQtdeRegConsulta(Long paginacaoQtdeRegConsulta) {
		this.paginacaoQtdeRegConsulta = paginacaoQtdeRegConsulta;
	} 
	
}
