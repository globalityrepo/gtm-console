package br.com.globality.gtm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "ISC_TB007_TRA_GRUPO")
@NamedQueries({ @NamedQuery(name = "TransacaoPassoInstancia.findAll", query = "select t from TransacaoPassoInstancia t") })
@RESTful(value="aplicacao")
public class TransacaoPassoInstancia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8899438330866534742L;



	@Id
	@Column(name = "NU_TRA_GRUPO", nullable = false, unique = true)
	private int id;
	
	@Column(name = "CO_TRA_GRUPO", nullable = false, length = 64)
	@NotNull
	private String transacaoGrupo;
	
	@Column(name = "DE_TRA_GRUPO", nullable = false, length = 512)
	@NotNull
	private String departamentoTransacaoGrupo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTransacaoGrupo() {
		return transacaoGrupo;
	}

	public void setTransacaoGrupo(String transacaoGrupo) {
		this.transacaoGrupo = transacaoGrupo;
	}

	public String getDepartamentoTransacaoGrupo() {
		return departamentoTransacaoGrupo;
	}

	public void setDepartamentoTransacaoGrupo(String departamentoTransacaoGrupo) {
		this.departamentoTransacaoGrupo = departamentoTransacaoGrupo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		TransacaoPassoInstancia other = (TransacaoPassoInstancia) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
