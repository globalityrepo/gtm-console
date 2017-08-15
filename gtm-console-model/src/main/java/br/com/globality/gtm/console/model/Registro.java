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
import javax.persistence.Transient;

import br.com.globality.gtm.console.model.annotation.GenericOrderByField;
import br.com.globality.gtm.console.model.annotation.GenericPredicateField;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_TB032_REGISTRO")
@NamedQueries({ @NamedQuery(name = "Registro.findAll", query = "select t from Registro t") })
@SequenceGenerator(name = "seq_registro", sequenceName = "ISC_TB032_REGISTRO_S", initialValue = 1)
public class Registro extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5469277526889771103L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_registro")
	@Column(name = "NU_REGISTRO", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "CO_REGISTRO", nullable = true, length = 30)
	@GenericOrderByField("asc")
	@GenericPredicateField
	private String codigo;
	
	@Column(name = "DE_REGISTRO", nullable = false, length = 512)
	@GenericPredicateField
	private String descricao;
	
	@Column(name = "IC_ATIVO", nullable = false, length = 1)
	private boolean ativo;
	
	@Column(name = "IC_IMPORTADO", nullable = false, length = 1)
	private boolean importado;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "NU_ENTIDADE_APLICACAO", nullable = false)
	private EntidadeAplicacao entidadeAplicacao;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "NU_USUARIO_INCLUSAO", nullable = false)
	private Usuario usuarioInclusao;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;
	
	@ManyToOne(optional=true, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "NU_USUARIO_ALTERACAO", nullable = true)
	private Usuario usuarioAlteracao;
	
	@Column(name = "DT_ALTERACAO", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@Transient
	private boolean hasEdicao;
	
	@Transient
	private String codigoOriginal;
	
	@Transient
	private String descricaoOriginal;
	
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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isImportado() {
		return importado;
	}

	public void setImportado(boolean importado) {
		this.importado = importado;
	}

	public EntidadeAplicacao getEntidadeAplicacao() {
		return entidadeAplicacao;
	}

	public void setEntidadeAplicacao(EntidadeAplicacao entidadeAplicacao) {
		this.entidadeAplicacao = entidadeAplicacao;
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

	public boolean getHasEdicao() {
		return hasEdicao;
	}

	public void setHasEdicao(boolean hasEdicao) {
		this.hasEdicao = hasEdicao;
	}

	public String getCodigoOriginal() {
		return codigoOriginal;
	}

	public void setCodigoOriginal(String codigoOriginal) {
		this.codigoOriginal = codigoOriginal;
	}

	public String getDescricaoOriginal() {
		return descricaoOriginal;
	}

	public void setDescricaoOriginal(String descricaoOriginal) {
		this.descricaoOriginal = descricaoOriginal;
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
		Registro other = (Registro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
