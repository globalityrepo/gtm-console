package br.com.globality.gtm.console.model.compositeId;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class TransacaoPassoAcaoPendenteCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3923192335407987841L;

	@Column(name = "NU_TRA_PASSO", nullable = false)
	private Long idTransacaoPasso;
    
	@Column(name = "CO_EVT_TIPO", nullable = false, length = 4)
	private String idEventoTipo;
	
	@Column(name = "DT_TRA_ACA_PENDENTE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	public Long getIdTransacaoPasso() {
		return idTransacaoPasso;
	}

	public void setIdTransacaoPasso(Long idTransacaoPasso) {
		this.idTransacaoPasso = idTransacaoPasso;
	}

	public String getIdEventoTipo() {
		return idEventoTipo;
	}

	public void setIdEventoTipo(String idEventoTipo) {
		this.idEventoTipo = idEventoTipo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((idEventoTipo == null) ? 0 : idEventoTipo.hashCode());
		result = prime * result + ((idTransacaoPasso == null) ? 0 : idTransacaoPasso.hashCode());
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
		TransacaoPassoAcaoPendenteCompositeId other = (TransacaoPassoAcaoPendenteCompositeId) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (idEventoTipo == null) {
			if (other.idEventoTipo != null)
				return false;
		} else if (!idEventoTipo.equals(other.idEventoTipo))
			return false;
		if (idTransacaoPasso == null) {
			if (other.idTransacaoPasso != null)
				return false;
		} else if (!idTransacaoPasso.equals(other.idTransacaoPasso))
			return false;
		return true;
	}
	
}
