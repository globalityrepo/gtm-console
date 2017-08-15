package br.com.globality.gtm.console.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "ISC_TB029_TRANSACAO_APLICACAO")
@NamedQueries({ @NamedQuery(name = "TransacaoAplicacao.findAll", query = "select t from TransacaoAplicacao t") })
@RESTful("transacaoAplicacao")

public class TransacaoAplicacao extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6293970516601210319L;

	@Id
	@Column(name = "NU_TRANSACAO", nullable = false, unique = true)
	private Long id;

	@Column(name = "NU_APLICACAO", nullable = false)
	private Long numeroAplicacao;

	@Column(name = "IC_TIPO_INTERACAO", nullable = false, length = 1)
	private String indicadorAlteracao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroAplicacao() {
		return numeroAplicacao;
	}

	public void setNumeroAplicacao(Long numeroAplicacao) {
		this.numeroAplicacao = numeroAplicacao;
	}

	public String getIndicadorAlteracao() {
		return indicadorAlteracao;
	}

	public void setIndicadorAlteracao(String indicadorAlteracao) {
		this.indicadorAlteracao = indicadorAlteracao;
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
		TransacaoAplicacao other = (TransacaoAplicacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
	