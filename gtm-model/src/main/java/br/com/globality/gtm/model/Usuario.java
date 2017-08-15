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
@Table(name = "ISC_TB020_USUARIO")
@NamedQueries({ @NamedQuery(name = "Usuario.findAll", query = "select t from Usuario t") })
@SequenceGenerator(name = "seq_usuario", sequenceName = "ISC_TB020_USUARIO_S", initialValue = 1)
public class Usuario implements Serializable {

	private static final long serialVersionUID = -8451060762259968019L;

	@Id
	@Column(name = "NU_USUARIO", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	private Long id;
	
	@Column(name = "CO_USUARIO", nullable = false, length = 30)
	@NotNull
	private String codigo;
	
	@Column(name = "TX_SENHA", nullable = false, length = 80)
	@NotNull
	private String senha;
	
	@Column(name = "NM_USUARIO", nullable = false, length = 80)
	@NotNull
	private String nome;
	
	@Column(name = "TX_EMAIL", nullable = false, length = 80)
	@NotNull
	private String email;
	
	@Column(name = "IC_ATIVO", nullable = false)
	@NotNull
	private Boolean ativo;
	
	@Column(name = "IC_BLOQUEADO", nullable = false)
	@NotNull
	private Boolean bloqueado;
	
	@Column(name = "IC_NATIVO", nullable = false)
	@NotNull
	private Boolean nativo;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_PERFIL", nullable=false)
	@NotNull
	private Perfil perfil;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO_INCLUSAO", nullable=false)
	@NotNull
	private Usuario usuarioInclusao;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date dataInclusao;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO_ALTERACAO", nullable = true)
	private Usuario usuarioAlteracao;
	
	@Column(name = "DT_ALTERACAO", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Boolean getNativo() {
		return nativo;
	}

	public void setNativo(Boolean nativo) {
		this.nativo = nativo;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}