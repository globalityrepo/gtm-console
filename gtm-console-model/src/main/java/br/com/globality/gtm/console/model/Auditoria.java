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
import javax.validation.constraints.NotNull;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "ISC_TB006_AUDITORIA")
@NamedQueries({ @NamedQuery(name = "Auditoria.findAll", query = "select t from Auditoria t") })
@SequenceGenerator(name = "seq_auditoria", sequenceName = "ISC_TB006_AUDITORIA_S", initialValue = 1)
public class Auditoria extends AbstractEntity {
	
	private static final long serialVersionUID = -4097276363737591925L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_auditoria")
	@Column(name = "NU_AUDITORIA", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "DE_AUDITORIA", nullable = false, length = 512)
	private String descricao;
	
	@Column(name = "TS_AUDITORIA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date data;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_USUARIO", nullable=false)
	@NotNull
	private Usuario usuario;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="NU_TRANSACAO", nullable=false)
	@NotNull
	private Transacao transacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
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
		Auditoria other = (Auditoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
