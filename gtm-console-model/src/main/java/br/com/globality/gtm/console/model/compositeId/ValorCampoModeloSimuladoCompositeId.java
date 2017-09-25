package br.com.globality.gtm.console.model.compositeId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ValorCampoModeloSimuladoCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "N_MOD_SMULA", nullable = false)
	private Long idModeloSimulado;
    
	@Column(name = "N_CPO_MOD_SMULA", nullable = false)
	private Long ordemCampo;
	
	@Column(name = "N_VLR_CPO_MOD_SMULA", nullable = false)
	private Long ordemValorCampo;

	public Long getIdModeloSimulado() {
		return idModeloSimulado;
	}

	public void setIdModeloSimulado(Long idModeloSimulado) {
		this.idModeloSimulado = idModeloSimulado;
	}

	public Long getOrdemCampo() {
		return ordemCampo;
	}

	public void setOrdemCampo(Long ordemCampo) {
		this.ordemCampo = ordemCampo;
	}

	public Long getOrdemValorCampo() {
		return ordemValorCampo;
	}

	public void setOrdemValorCampo(Long ordemValorCampo) {
		this.ordemValorCampo = ordemValorCampo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ordemValorCampo == null) ? 0 : ordemValorCampo.hashCode());
		result = prime * result + ((ordemCampo == null) ? 0 : ordemCampo.hashCode());
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
		ValorCampoModeloSimuladoCompositeId other = (ValorCampoModeloSimuladoCompositeId) obj;
		if (ordemValorCampo == null) {
			if (other.ordemValorCampo != null)
				return false;
		} else if (!ordemValorCampo.equals(other.ordemValorCampo))
			return false;
		if (ordemCampo == null) {
			if (other.ordemCampo != null)
				return false;
		} else if (!ordemCampo.equals(other.ordemCampo))
			return false;
		if (idModeloSimulado == null) {
			if (other.idModeloSimulado != null)
				return false;
		} else if (!idModeloSimulado.equals(other.idModeloSimulado))
			return false;
		return true;
	}
	
}
