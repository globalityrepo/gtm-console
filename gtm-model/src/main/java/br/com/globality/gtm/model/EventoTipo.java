package br.com.globality.gtm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.globality.gtm.util.annotation.RESTful;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB011_EVT_TIPO")
@NamedQueries({ @NamedQuery(name = "EventoTipo.findAll", query = "select t from EventoTipo t") })
@RESTful(value="eventoTipo")
public class EventoTipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	@Id
	@Column(name = "CO_EVT_TIPO", nullable = false, length = 4)
	private String id;
	
	@Column(name = "DE_EVT_TIPO", nullable = false, length = 512)
	@NotNull
	private String descricao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		EventoTipo other = (EventoTipo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
