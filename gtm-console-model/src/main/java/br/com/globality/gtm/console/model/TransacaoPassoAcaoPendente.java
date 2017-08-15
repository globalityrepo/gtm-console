package br.com.globality.gtm.console.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.globality.gtm.console.model.annotation.GenericPredicateField;
import br.com.globality.gtm.console.model.compositeId.TransacaoPassoAcaoPendenteCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB015_TRA_PAS_ACA_PENDENTE")
public class TransacaoPassoAcaoPendente extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5274438135527579665L;

	@EmbeddedId
	private TransacaoPassoAcaoPendenteCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_PASSO", nullable=false, insertable=false, updatable=false)
	@GenericPredicateField("codigo")
	private TransacaoPasso transacaoPasso;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="CO_EVT_TIPO", nullable=false, insertable=false, updatable=false)
	@GenericPredicateField("id")
	private EventoTipo eventoTipo;
	
	@Column(name = "DT_TRA_ACA_PENDENTE", nullable=false, insertable=false, updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(name = "DT_TRA_ACA_PEN_CLOSED", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFechamento;
	
	@Column(name = "CD_TRA_ACA_PEN_USER", nullable = true, length = 6)
	private String codigoUsuario;

	@Column(name = "IC_TRA_ACA_PEN_STATUS", nullable = true, length = 1)
	private String status;

	public TransacaoPassoAcaoPendenteCompositeId getId() {
		return id;
	}

	public void setId(TransacaoPassoAcaoPendenteCompositeId id) {
		this.id = id;
	}

	public TransacaoPasso getTransacaoPasso() {
		return transacaoPasso;
	}

	public void setTransacaoPasso(TransacaoPasso transacaoPasso) {
		this.transacaoPasso = transacaoPasso;
	}

	public EventoTipo getEventoTipo() {
		return eventoTipo;
	}

	public void setEventoTipo(EventoTipo eventoTipo) {
		this.eventoTipo = eventoTipo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TransacaoPassoAcaoPendente other = (TransacaoPassoAcaoPendente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
