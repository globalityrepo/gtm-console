package br.com.globality.gtm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "ISC_TB003_TRANSACAO")
@NamedQueries({ @NamedQuery(name = "Transacao.findAll", query = "select t from Transacao t") })
@SequenceGenerator(name = "seq_transacao", sequenceName = "ISC_TB003_TRANSACAO_S", initialValue = 1)
public class Transacao implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3180924629857955885L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacao")
	@Column(name = "NU_TRANSACAO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_TRANSACAO", nullable = false, length = 64)
	@NotNull
	private String codigo;
	
	@Column(name = "DE_TRANSACAO", nullable = false, length = 512)
	@NotNull
	private String descricao;
	
	@Column(name = "QT_DIA_EVENTO", nullable = false, length = 512)
	@NotNull
	private Long qtdeDiaEvento;
	
	@Column(name = "QT_DIA_CONTEUDO_EVENTO", nullable = false, length = 512)
	@NotNull
	private Long qtdeDiaConteudoEvento;
	
	@Column(name = "IC_TRANSACAO_RESTRICAO", nullable = false, length = 512)
	@NotNull
	private String possuiRestricao;
	
	@Column(name = "NU_TRA_GRUPO", nullable = false, length = 512)
	@NotNull
	private int grupo;

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

	public Long getQtdeDiaEvento() {
		return qtdeDiaEvento;
	}

	public void setQtdeDiaEvento(Long qtdeDiaEvento) {
		this.qtdeDiaEvento = qtdeDiaEvento;
	}

	public Long getQtdeDiaConteudoEvento() {
		return qtdeDiaConteudoEvento;
	}

	public void setQtdeDiaConteudoEvento(Long qtdeDiaConteudoEvento) {
		this.qtdeDiaConteudoEvento = qtdeDiaConteudoEvento;
	}

	public String getPossuiRestricao() {
		return possuiRestricao;
	}

	public void setPossuiRestricao(String possuiRestricao) {
		this.possuiRestricao = possuiRestricao;
	}

	public int getGrupo() {
		return grupo;
	}

	public void setGrupo(int grupo) {
		this.grupo = grupo;
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
