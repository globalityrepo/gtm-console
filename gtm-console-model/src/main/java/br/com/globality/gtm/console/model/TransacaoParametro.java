package br.com.globality.gtm.console.model;

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

import org.apache.commons.lang.StringUtils;

import br.com.globality.gtm.console.model.annotation.GenericPredicateField;
import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "ISC_TB013_TRA_PARAMETRO ")
@NamedQueries({ @NamedQuery(name = "TransacaoParametro.findAll", query = "select t from TransacaoParametro t") })
@SequenceGenerator(name = "seq_transacao_paramentro", sequenceName = "ISC_TB013_TRA_PARAMETRO_S", initialValue = 1)
@RESTful
public class TransacaoParametro extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5650518333941744092L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao_paramentro")
	@Column(name = "NU_PARAMETRO", nullable = false, unique = true)
	private Long id;

	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRANSACAO", nullable=true)
	@GenericPredicateField("codigo")
	private Transacao transacao;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="CO_EVT_TIPO", nullable=true)
	@GenericPredicateField("id")
	private EventoTipo eventoTipo;
	
	@Column(name = "NO_PARAMETRO", nullable = true, length = 64)
	@GenericPredicateField
	private String nome;
	
	@Column(name = "NO_PAR_NAMESPACE", nullable = true, length = 512)
	@GenericPredicateField
	private String namespace;

	@Column(name = "NO_PAR_PREFIXO", nullable = true, length = 512)
	@GenericPredicateField
	private String prefixo;

	@Column(name = "NO_PAR_CAMINHO", nullable = true, length = 512)
	private String caminho;

	@Column(name = "IC_PAR_MOMENTO", nullable = true, length = 1)
	private String momento;

	@Column(name = "IC_PAR_ATIVO", nullable = true, length = 1)
	@GenericPredicateField
	private String ativo;
		
	@Transient
	private String momentoFormatado;
	
	@Transient
	private String ativoFormatado;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public EventoTipo getEventoTipo() {
		return eventoTipo;
	}

	public void setEventoTipo(EventoTipo eventoTipo) {
		this.eventoTipo = eventoTipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public String getMomento() {
		return momento;
	}

	public void setMomento(String momento) {
		this.momento = momento;
	}
	
	public void setMomentoFormatado(String momentoFormatado) {
		this.momentoFormatado = momentoFormatado;
	}

	public String getMomentoFormatado() {
		if (StringUtils.isNotBlank(momento)) {
			if (momento.equalsIgnoreCase("I")) {
				return "CRIAÇÃO DO CONTEÚDO DO EVENTO";
			}
			else if (momento.equalsIgnoreCase("M")) {
				return "CRIAÇÃO CONSIDERANDO O PARÂMETRO";
			}
			else {
				return "EXCLUSÃO DO CONTEÚDO DO EVENTO";
			}
		}
		return "-";
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getAtivoFormatado() {
		return ativo!=null && ativo.equalsIgnoreCase("Y") ? "SIM" : "NÃO";
	}

	public void setAtivoFormatado(String ativoFormatado) {
		this.ativoFormatado = ativoFormatado;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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
		TransacaoParametro other = (TransacaoParametro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
