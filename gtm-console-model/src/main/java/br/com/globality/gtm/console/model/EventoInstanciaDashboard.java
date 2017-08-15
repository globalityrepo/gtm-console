package br.com.globality.gtm.console.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author Leonardo Andrade
 *
 */
@Entity
@Table(name = "ISC_VW001_DASHBOARD")
@NamedQueries({ @NamedQuery(name = "EventoInstanciaDashboard.findAll", query = "select t from EventoInstanciaDashboard t") })
public class EventoInstanciaDashboard extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5195532904041444691L;

	@Id
	@Column(name = "NU_TRA_EVT_INSTANCIA", nullable = false, unique = true, length = 36)
	private String id;
	
	@Column(name = "DE_EVT_INSTANCIA", nullable = true, length = 512)
	private String descricao;

	@Column(name = "TS_EVT_INSTANCIA", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(name = "DE_TRA_PASSO", nullable = true, length = 512)
	private String descricaoTransacaoPasso;
		
	@Column(name = "CO_EVT_TIPO", nullable = true, length = 4)
	private String codigoEventoTipo;
	
	@Column(name = "CO_EVT_NIVEL", nullable = true, length = 1)
	private String codigoEventoNivel;
	
	@Column(name = "DE_EVT_NIVEL", nullable = true, length = 512)
	private String descricaoEventoNivel;
	
	@Column(name = "CO_GRUPO", nullable = true, length = 64)
	private String codigoGrupo;
	
	@Column(name = "NU_TRA_INSTANCIA", nullable = true, length = 36)
	private String idTransacaoInstancia;
	
	@Column(name = "TS_TRA_INSTANCIA", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTransacaoInstancia;
	
	@Column(name = "NU_TRANSACAO", nullable = true)
	private Long idTransacao;
	
	@Column(name = "CO_TRANSACAO", nullable = true, length = 64)
	private String codigoTransacao;
	
	@Column(name = "IN_HAS_CONTEUDO", nullable = false, length = 1)
	private Boolean possuiConteudo;
	
	@Column(name = "ID_EVT_INSTANCIA_PAI", nullable = true, length = 36)
	private String idEventoInstanciaPai;
	
	@Transient
	private List<TransacaoParametro> parametros;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getDescricaoTransacaoPasso() {
		return descricaoTransacaoPasso;
	}

	public void setDescricaoTransacaoPasso(String descricaoTransacaoPasso) {
		this.descricaoTransacaoPasso = descricaoTransacaoPasso;
	}

	public String getCodigoEventoTipo() {
		return codigoEventoTipo;
	}

	public void setCodigoEventoTipo(String codigoEventoTipo) {
		this.codigoEventoTipo = codigoEventoTipo;
	}

	public String getCodigoEventoNivel() {
		return codigoEventoNivel;
	}

	public void setCodigoEventoNivel(String codigoEventoNivel) {
		this.codigoEventoNivel = codigoEventoNivel;
	}

	public String getDescricaoEventoNivel() {
		return descricaoEventoNivel;
	}

	public void setDescricaoEventoNivel(String descricaoEventoNivel) {
		this.descricaoEventoNivel = descricaoEventoNivel;
	}

	public String getCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(String codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	public String getIdTransacaoInstancia() {
		return idTransacaoInstancia;
	}

	public void setIdTransacaoInstancia(String idTransacaoInstancia) {
		this.idTransacaoInstancia = idTransacaoInstancia;
	}

	public Date getDataTransacaoInstancia() {
		return dataTransacaoInstancia;
	}

	public void setDataTransacaoInstancia(Date dataTransacaoInstancia) {
		this.dataTransacaoInstancia = dataTransacaoInstancia;
	}

	public Long getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(Long idTransacao) {
		this.idTransacao = idTransacao;
	}

	public String getCodigoTransacao() {
		return codigoTransacao;
	}

	public void setCodigoTransacao(String codigoTransacao) {
		this.codigoTransacao = codigoTransacao;
	}

	public Boolean getPossuiConteudo() {
		return possuiConteudo;
	}

	public void setPossuiConteudo(Boolean possuiConteudo) {
		this.possuiConteudo = possuiConteudo;
	}

	public String getIdEventoInstanciaPai() {
		return idEventoInstanciaPai;
	}

	public void setIdEventoInstanciaPai(String idEventoInstanciaPai) {
		this.idEventoInstanciaPai = idEventoInstanciaPai;
	}

	public List<TransacaoParametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<TransacaoParametro> parametros) {
		this.parametros = parametros;
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
		EventoInstanciaDashboard other = (EventoInstanciaDashboard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
