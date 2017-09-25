package br.com.globality.gtm.console.model;

import java.util.Date;
import java.util.List;

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
import br.com.globality.gtm.console.util.annotation.RESTful;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ENTID_APLIC_ORIGE_PARA")
@NamedQueries({ @NamedQuery(name = "EntidadeAplicacaoDePara.findAll", query = "select t from EntidadeAplicacaoDePara t") })
@SequenceGenerator(name = "seq_entidade_aplicacao_de_para", sequenceName = "SQ04_ENTID_APLIC_ORIGE_PARA", initialValue = 1)
@RESTful
public class EntidadeAplicacaoDePara extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4173784182166624039L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_entidade_aplicacao_de_para")
	@Column(name = "N_ENTID_APLIC_ORIGE_PARA", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "C_ENTID_APLIC_ORIGE_PARA", nullable = true, length = 30)
	@GenericOrderByField("asc")
	@GenericPredicateField
	private String codigo;
	
	@Column(name = "C_ATIVO", nullable = false, length = 1)
	private boolean ativo;
	
	@Column(name = "C_ACSSO_LIBRD", nullable = false, length = 1)
	private Boolean liberarAcesso;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_ENTID_APLIC_ORIGE", nullable = false)
	private EntidadeAplicacao entidadeAplicacaoDe;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "N_ENTID_APLIC_PARA", nullable = false)
	private EntidadeAplicacao entidadeAplicacaoPara;
	
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
	
	@Transient
	private Entidade entidade;
	
	@Transient
	private List<AcessoDelegado> acessosDelegados;
	
	@Transient
	private List<RegistroDePara> registrosDePara;
	
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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getLiberarAcesso() {
		return liberarAcesso;
	}

	public void setLiberarAcesso(Boolean liberarAcesso) {
		this.liberarAcesso = liberarAcesso;
	}

	public EntidadeAplicacao getEntidadeAplicacaoDe() {
		return entidadeAplicacaoDe;
	}

	public void setEntidadeAplicacaoDe(EntidadeAplicacao entidadeAplicacaoDe) {
		this.entidadeAplicacaoDe = entidadeAplicacaoDe;
	}

	public EntidadeAplicacao getEntidadeAplicacaoPara() {
		return entidadeAplicacaoPara;
	}

	public void setEntidadeAplicacaoPara(EntidadeAplicacao entidadeAplicacaoPara) {
		this.entidadeAplicacaoPara = entidadeAplicacaoPara;
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

	public List<AcessoDelegado> getAcessosDelegados() {
		return acessosDelegados;
	}

	public void setAcessosDelegados(List<AcessoDelegado> acessosDelegados) {
		this.acessosDelegados = acessosDelegados;
	}

	public List<RegistroDePara> getRegistrosDePara() {
		return registrosDePara;
	}

	public void setRegistrosDePara(List<RegistroDePara> registrosDePara) {
		this.registrosDePara = registrosDePara;
	}

	public Entidade getEntidade() {
		return entidade;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
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
		EntidadeAplicacaoDePara other = (EntidadeAplicacaoDePara) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
