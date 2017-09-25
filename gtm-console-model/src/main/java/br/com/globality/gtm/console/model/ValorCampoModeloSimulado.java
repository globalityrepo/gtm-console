package br.com.globality.gtm.console.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.globality.gtm.console.model.compositeId.ValorCampoModeloSimuladoCompositeId;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "VLR_CPO_MOD_SMULA")
@NamedQueries({ @NamedQuery(name = "ValorCampoModeloSimulado.findAll", query = "select t from ValorCampoModeloSimulado t") })
public class ValorCampoModeloSimulado extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ValorCampoModeloSimuladoCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_MOD_SMULA", nullable=false, insertable=false, updatable=false)
	private ModeloSimulado modeloSimulado;
	
	@Column(name = "N_CPO_MOD_SMULA", nullable=false, insertable=false, updatable=false)
	private Long ordemCampo;
	
	@Column(name = "N_VLR_CPO_MOD_SMULA", nullable=false, insertable=false, updatable=false)
	private Long ordemValorCampo;
	
	@Column(name = "R_VLR_CPO_MOD_SMULA", nullable = true, length = 512)
	private String descricao;
	
	public ValorCampoModeloSimuladoCompositeId getId() {
		return id;
	}

	public void setId(ValorCampoModeloSimuladoCompositeId id) {
		this.id = id;
	}

	public ModeloSimulado getModeloSimulado() {
		return modeloSimulado;
	}

	public void setModeloSimulado(ModeloSimulado modeloSimulado) {
		this.modeloSimulado = modeloSimulado;
	}

	public Long getOrdemCampo() {
		return ordemCampo;
	}

	public void setOrdemCampo(Long ordemCampo) {
		this.ordemCampo = ordemCampo;
	}

	public Long getOrdemValorCampo() {
		return ordemValorCampo;
	}

	public void setOrdemValorCampo(Long ordemValorCampo) {
		this.ordemValorCampo = ordemValorCampo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		ValorCampoModeloSimulado other = (ValorCampoModeloSimulado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}