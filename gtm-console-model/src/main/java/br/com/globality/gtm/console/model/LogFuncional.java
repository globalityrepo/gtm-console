package br.com.globality.gtm.console.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "LOG_FUNCL")
@NamedQueries({ @NamedQuery(name = "LogFuncional.findAll", query = "select t from LogFuncional t") })
@SequenceGenerator(name = "seq_log_funcional", sequenceName = "SQ07_LOG_FUNCL", initialValue = 1)
public class LogFuncional extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6840912552273509594L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log_funcional")
	@Column(name = "N_LOG_FUNCL", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "C_ATIVO", nullable = false, length = 1)
	private boolean ativo;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_USUAR", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_REC", nullable = false)
	private Recurso recurso;
	
	@Column(name = "N_REFT", nullable = false)
	private Long idReferencia;
	
	@Column(name = "D_INCL", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Long getIdReferencia() {
		return idReferencia;
	}

	public void setIdReferencia(Long idReferencia) {
		this.idReferencia = idReferencia;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
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
		LogFuncional other = (LogFuncional) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
