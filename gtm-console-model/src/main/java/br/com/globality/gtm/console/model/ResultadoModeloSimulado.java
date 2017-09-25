package br.com.globality.gtm.console.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.globality.gtm.console.model.compositeId.ResultadoModeloSimuladoCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "RESUL_MOD_SMULA")
@NamedQueries({ @NamedQuery(name = "ResultadoModeloSimulado.findAll", query = "select t from ResultadoModeloSimulado t") })
public class ResultadoModeloSimulado extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ResultadoModeloSimuladoCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_MOD_SMULA", nullable=false, insertable=false, updatable=false)
	private ModeloSimulado modeloSimulado;
	
	@Column(name = "N_RESUL_MOD_SMULA", nullable=false, insertable=false, updatable=false)
	private Long ordem;
	
	@Column(name = "B_CONTD_XML", columnDefinition = "CLOB", nullable = true)
	@Lob
	private String conteudo;
	
	public ResultadoModeloSimuladoCompositeId getId() {
		return id;
	}

	public void setId(ResultadoModeloSimuladoCompositeId id) {
		this.id = id;
	}

	public ModeloSimulado getModeloSimulado() {
		return modeloSimulado;
	}

	public void setModeloSimulado(ModeloSimulado modeloSimulado) {
		this.modeloSimulado = modeloSimulado;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
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
		ResultadoModeloSimulado other = (ResultadoModeloSimulado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}