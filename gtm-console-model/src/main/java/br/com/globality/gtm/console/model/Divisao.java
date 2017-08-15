package br.com.globality.gtm.console.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.globality.gtm.console.model.annotation.GenericOrderByField;
import br.com.globality.gtm.console.model.annotation.GenericPredicateField;
import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB002_DIVISAO")
@NamedQueries({ @NamedQuery(name = "Divisao.findAll", query = "select t from Divisao t") })
@SequenceGenerator(name = "seq_divisao", sequenceName = "ISC_TB002_DIVISAO_S", initialValue = 1)
@RESTful
public class Divisao extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6712881260821999482L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_divisao")
	@Column(name = "NU_DIVISAO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_DIVISAO", nullable = true, length = 64)
	@GenericOrderByField("asc")
	@GenericPredicateField
	private String codigo;
	
	@Column(name = "DE_DIVISAO", nullable = true, length = 512)
	@GenericPredicateField
	private String descricao;
	
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
		Divisao other = (Divisao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
