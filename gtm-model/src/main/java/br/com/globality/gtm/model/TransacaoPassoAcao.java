package br.com.globality.gtm.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Bryan Duarte
 *
 */
@Entity @IdClass(TransacaoPassoAcaoCompositeId.class)
@Table(name = "ISC_TB004_TRA_PAS_ACAO")
@NamedQueries({ @NamedQuery(name = "TransacaoPassoAcao.findAll", query = "select t from TransacaoPassoAcao t") })
public class TransacaoPassoAcao implements Serializable {

	private static final long serialVersionUID = 2337198258061138851L;

	@Id
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_PASSO", nullable=false)
	private TransacaoPasso transacaoPasso;
	
	@Id
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="CO_EVT_TIPO", nullable=false)
	private EventoTipo eventoTipo;
	
	@Column(name = "QT_INTERVALO", nullable = false)
	@NotNull
	private Long intervalo;
	
	@Column(name = "QT_TENTATIVAS", nullable = false, length = 6)
	@NotNull
	private Long tentativas;
	
	@Column(name = "DE_FILA_DESTINO", nullable = false, length = 512)
	@NotNull
	private String destino;
	
	@Column(name = "DE_DESTINATARIO", nullable = false, length = 512)
	@NotNull
	private String destinatario;
	
	@Column(name = "IC_REENVIO", nullable = false, length = 1)
	@NotNull
	private String reenvio;
	
	@Column(name = "DE_TRA_NU_XPATH", nullable = false, length = 512)
	@NotNull
	private String departamentoTransacao;
	
	@Column(name = "DE_TRA_NU_NAMESPACE", nullable = false, length = 512)
	@NotNull
	private String namespace;
	
	@Column(name = "DE_TRA_NU_PREFIXO", nullable = false, length = 512)
	@NotNull
	private String prefixo;

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

	public Long getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(Long intervalo) {
		this.intervalo = intervalo;
	}

	public Long getTentativas() {
		return tentativas;
	}

	public void setTentativas(Long tentativas) {
		this.tentativas = tentativas;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getReenvio() {
		return reenvio;
	}

	public void setReenvio(String reenvio) {
		this.reenvio = reenvio;
	}

	public String getDepartamentoTransacao() {
		return departamentoTransacao;
	}

	public void setDepartamentoTransacao(String departamentoTransacao) {
		this.departamentoTransacao = departamentoTransacao;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transacaoPasso == null) ? 0 : transacaoPasso.hashCode());
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
		TransacaoPassoAcao other = (TransacaoPassoAcao) obj;
		if (transacaoPasso == null) {
			if (other.transacaoPasso != null)
				return false;
		} else if (!transacaoPasso.equals(other.transacaoPasso))
			return false;
		return true;
	}
	
}

class TransacaoPassoAcaoCompositeId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	TransacaoPasso transacaoPasso;
    EventoTipo eventoTipo;
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventoTipo == null) ? 0 : eventoTipo.hashCode());
		result = prime * result + ((transacaoPasso == null) ? 0 : transacaoPasso.hashCode());
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
		TransacaoPassoAcaoCompositeId other = (TransacaoPassoAcaoCompositeId) obj;
		if (eventoTipo == null) {
			if (other.eventoTipo != null)
				return false;
		} else if (!eventoTipo.equals(other.eventoTipo))
			return false;
		if (transacaoPasso == null) {
			if (other.transacaoPasso != null)
				return false;
		} else if (!transacaoPasso.equals(other.transacaoPasso))
			return false;
		return true;
	}
        
}
