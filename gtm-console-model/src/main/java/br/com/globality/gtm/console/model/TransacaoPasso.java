package br.com.globality.gtm.console.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.globality.gtm.console.model.annotation.GenericPredicateField;
import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB017_TRA_PASSO")
@NamedQueries({ @NamedQuery(name = "TransacaoPasso.findAll", query = "select t from TransacaoPasso t") })
@SequenceGenerator(name = "seq_transacao_passo", sequenceName = "ISC_TB017_TRA_PASSO_S", initialValue = 1)
@RESTful
public class TransacaoPasso extends AbstractEntity {
	
	private static final long serialVersionUID = 2337198258061138851L;

	@Id
	@Column(name = "NU_TRA_PASSO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao_passo")
	private Long id;
	
	@Column(name = "CO_TRA_PASSO", nullable = true, length = 64)
	@GenericPredicateField
	private String codigo;
	
	@Column(name = "DE_TRA_PASSO", nullable = true, length = 512)
	@GenericPredicateField
	private String descricao;
		
	@Column(name = "IC_EVT_INS_CONT", nullable = true, length = 1)
	private String gravarNaBase;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRANSACAO", nullable=true)
	@GenericPredicateField("codigo")
	private Transacao transacao;
	
	@Transient
	private List<EventoNivel> eventoNiveis;
	
	@Transient
	private List<TransacaoPassoAcao> acoes;
	
	@Transient
	private String gravarNaBaseFormatado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getGravarNaBase() {
		return gravarNaBase;
	}

	public void setGravarNaBase(String gravarNaBase) {
		this.gravarNaBase = gravarNaBase;
	}
	
	public String getGravarNaBaseFormatado() {
		return gravarNaBase!=null && gravarNaBase.equalsIgnoreCase("Y") ? "SIM" : "NÃO";
	}

	public void setGravarNaBaseFormatado(String gravarNaBaseFormatado) {
		this.gravarNaBaseFormatado = gravarNaBaseFormatado;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}
	
	public List<EventoNivel> getEventoNiveis() {
		return eventoNiveis;
	}

	public void setEventoNiveis(List<EventoNivel> eventoNiveis) {
		this.eventoNiveis = eventoNiveis;
	}
		
	public List<TransacaoPassoAcao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<TransacaoPassoAcao> acoes) {
		this.acoes = acoes;
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
		TransacaoPasso other = (TransacaoPasso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

	
	
	