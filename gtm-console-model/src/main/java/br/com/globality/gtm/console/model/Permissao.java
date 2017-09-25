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

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "PRMSS")
@NamedQueries({ @NamedQuery(name = "Permissao.findAll", query = "select t from Permissao t") })
public class Permissao extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4904889033209776225L;

	@Id
	@Column(name = "N_PRMSS", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "C_PRMSS", nullable = false, length = 30)
	@NotNull
	private String codigo;
	
	@Column(name = "I_PRMSS", nullable = false, length = 80)
	@NotNull
	private String nome;
	
	@Column(name = "N_ORD", nullable = false)
	private Long ordem;
	
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
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
		Permissao other = (Permissao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}