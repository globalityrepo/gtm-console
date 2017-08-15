package br.com.globality.gtm.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB017_TRA_PASSO")
@NamedQueries({ @NamedQuery(name = "TransacaoPasso.findAll", query = "select t from TransacaoPasso t") })
public class TransacaoPasso implements Serializable {

	private static final long serialVersionUID = 2337198258061138851L;

	@Id
	@Column(name = "NU_TRA_PASSO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_TRA_PASSO", nullable = false, length = 64)
	@NotNull
	private String codigo;
	
	@Column(name = "DE_TRA_PASSO", nullable = true, length = 512)
	private String descricao;
		
	@Column(name = "IC_EVT_INS_CONT", nullable = false, length = 1)
	@NotNull
	private String reenvio;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRANSACAO", nullable=false)
	@NotNull
	private Transacao transacao;

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

	public String getReenvio() {
		return reenvio;
	}

	public void setReenvio(String reenvio) {
		this.reenvio = reenvio;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
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

	
	
	