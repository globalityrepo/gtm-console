package br.com.globality.gtm.console.model.compositeId;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class TransacaoPassoAcaoPendenteEventoCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3923192335407987841L;

	@Column(name = "N_TRANS_PASSO", nullable = false)
	private Long idTransacaoPasso;
    
	@Column(name = "C_EVNTO_TPO", nullable = false, length = 4)
	private String idEventoTipo;
	
	@Column(name = "D_TRANS_ACAO_PEND", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(name = "N_TRANS_EVNTO_INSTN", nullable = false, length = 1)
	private String idEventoInstancia;
	
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

	public String getIdEventoInstancia() {
		return idEventoInstancia;
	}

	public void setIdEventoInstancia(String idEventoInstancia) {
		this.idEventoInstancia = idEventoInstancia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((idEventoInstancia == null) ? 0 : idEventoInstancia.hashCode());
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
		TransacaoPassoAcaoPendenteEventoCompositeId other = (TransacaoPassoAcaoPendenteEventoCompositeId) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (idEventoInstancia == null) {
			if (other.idEventoInstancia != null)
				return false;
		} else if (!idEventoInstancia.equals(other.idEventoInstancia))
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
