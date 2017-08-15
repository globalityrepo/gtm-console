package br.com.globality.gtm.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "ISC_TB026_USUARIO_TRA_GRUPO")
@NamedQueries({ @NamedQuery(name = "UsuarioTransacaoGrupo.findAll", query = "select t from UsuarioTransacaoGrupo t") })
@SequenceGenerator(name = "seq_usuario_tra_grupo", sequenceName = "ISC_TB026_USU_TRA_GRUPO_S", initialValue = 1)
public class UsuarioTransacaoGrupo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1435674082655084034L;

	@Id
	@Column(name = "NU_USUARIO_TRA_GRUPO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario_tra_grupo")
	private Long id;
	
	@Column(name = "IC_ATIVO", nullable = false)
	@NotNull
	private Boolean ativo;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO", nullable=false)
	@NotNull
	private Usuario usuario;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRA_GRUPO", nullable=false)
	@NotNull
	private TransacaoGrupo transacaoGrupo;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO_INCLUSAO", nullable=false)
	@NotNull
	private UsuarioTransacaoGrupo usuarioInclusao;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dataInclusao;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO_ALTERACAO", nullable = true)
	private UsuarioTransacaoGrupo usuarioAlteracao;
	
	@Column(name = "DT_ALTERACAO", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TransacaoGrupo getTransacaoGrupo() {
		return transacaoGrupo;
	}

	public void setTransacaoGrupo(TransacaoGrupo transacaoGrupo) {
		this.transacaoGrupo = transacaoGrupo;
	}

	public UsuarioTransacaoGrupo getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioTransacaoGrupo usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public UsuarioTransacaoGrupo getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioTransacaoGrupo usuarioAlteracao) {
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
		UsuarioTransacaoGrupo other = (UsuarioTransacaoGrupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}