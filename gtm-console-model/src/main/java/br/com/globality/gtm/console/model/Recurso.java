package br.com.globality.gtm.console.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ISC_TB023_RECURSO")
@NamedQueries({ @NamedQuery(name = "Recurso.findAll", query = "select t from Recurso t") })
public class Recurso extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1806027463941873769L;

	@Id
	@Column(name = "NU_RECURSO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_RECURSO", nullable = false, length = 30)
	@NotNull
	private String codigo;
	
	@Column(name = "NM_RECURSO", nullable = false, length = 80)
	@NotNull
	private String nome;
		
	@Column(name = "IC_ATIVO", nullable = false)
	@NotNull
	private Boolean ativo;
	
	@Column(name = "DT_CARGA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dataCarga;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_RECURSO_PAI", nullable=true)
	private Recurso recursoPai;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_MODULO", nullable=true)
	private Modulo modulo;
	
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
	
	public Recurso getRecursoPai() {
		return recursoPai;
	}

	public void setRecursoPai(Recurso recursoPai) {
		this.recursoPai = recursoPai;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
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
		Recurso other = (Recurso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}