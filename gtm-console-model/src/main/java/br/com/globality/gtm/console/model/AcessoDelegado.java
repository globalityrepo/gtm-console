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
@Table(name = "PRMSS_REST")
@NamedQueries({ @NamedQuery(name = "AcessoDelegado.findAll", query = "select t from AcessoDelegado t") })
@SequenceGenerator(name = "seq_permissao_restrita", sequenceName = "SQ09_PRMSS_REST", initialValue = 1)
public class AcessoDelegado extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3224408443445484173L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_permissao_restrita")
	@Column(name = "N_PRMSS_REST", nullable = false, unique = true)
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
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_USUAR_INCL", nullable = false)
	private Usuario usuarioInclusao;
	
	@Column(name = "D_INCL", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_USUAR_ALT", nullable = true)
	private Usuario usuarioAlteracao;
	
	@Column(name = "D_ALT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
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

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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
		AcessoDelegado other = (AcessoDelegado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
