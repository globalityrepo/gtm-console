package br.com.globality.gtm.console.model.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CampoModeloSimuladoCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "N_MOD_SMULA", nullable = false)
	private Long idModeloSimulado;
    
	@Column(name = "N_CPO_MOD_SMULA", nullable = false)
	private Long ordem;

	public Long getIdModeloSimulado() {
		return idModeloSimulado;
	}

	public void setIdModeloSimulado(Long idModeloSimulado) {
		this.idModeloSimulado = idModeloSimulado;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + ((idModeloSimulado == null) ? 0 : idModeloSimulado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampoModeloSimuladoCompositeId other = (CampoModeloSimuladoCompositeId) obj;
		if (ordem == null) {
			if (other.ordem != null)
				return false;
		} else if (!ordem.equals(other.ordem))
			return false;
		if (idModeloSimulado == null) {
			if (other.idModeloSimulado != null)
				return false;
		} else if (!idModeloSimulado.equals(other.idModeloSimulado))
			return false;
		return true;
	}
	
}
