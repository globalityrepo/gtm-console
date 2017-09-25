package br.com.globality.gtm.console.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "PRFIL")
@NamedQueries({ @NamedQuery(name = "Perfil.findAll", query = "select t from Perfil t") })
@RESTful("perfil")
public class Perfil extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4577496497620040446L;

	@Id
	@Column(name = "N_PRFIL", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "I_PRFIL", nullable = false, length = 80)
	@NotNull
	private String nome;
	
	@Column(name = "R_PRFIL", nullable = false, length = 512)
	@NotNull
	private String descricao;
	
	@Column(name = "C_ATIVO", nullable = false)
	@NotNull
	private Boolean ativo;
	
	@Column(name = "D_CARGA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dataCarga;
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataCarga() {
		return dataCarga;
	}

	public void setDataCarga(Date dataCarga) {
		this.dataCarga = dataCarga;
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
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}