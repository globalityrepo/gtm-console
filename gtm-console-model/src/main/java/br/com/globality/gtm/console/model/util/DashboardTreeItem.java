package br.com.globality.gtm.console.model.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.globality.gtm.console.model.EventoInstanciaDashboard;

/**
 * @author Leonardo Andrade
 *
 */
public class DashboardTreeItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1924454499457681719L;

	private Long id;
	
	private String descricao;
	
	private Long status;
	
	private String mensagem;
	
	private Date data;
	
	private String dataFormatada;
	
	private boolean hasConteudo;
	
	private EventoInstanciaDashboard item;
	
	private List<DashboardTreeItem> filhos;
	
	private Long idPai;

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

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDataFormatada() {
		return dataFormatada;
	}

	public void setDataFormatada(String dataFormatada) {
		this.dataFormatada = dataFormatada;
	}

	public EventoInstanciaDashboard getItem() {
		return item;
	}

	public void setItem(EventoInstanciaDashboard item) {
		this.item = item;
	}

	public List<DashboardTreeItem> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<DashboardTreeItem> filhos) {
		this.filhos = filhos;
	}

	public Long getIdPai() {
		return idPai;
	}

	public void setIdPai(Long idPai) {
		this.idPai = idPai;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public boolean isHasConteudo() {
		return hasConteudo;
	}

	public void setHasConteudo(boolean hasConteudo) {
		this.hasConteudo = hasConteudo;
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
		DashboardTreeItem other = (DashboardTreeItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
		
}
