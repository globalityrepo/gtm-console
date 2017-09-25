package br.com.globality.gtm.console.model;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.globality.gtm.console.model.compositeId.UsuarioGrupoCompositeId;

/**
 * @author Bryan Duarte
 *
 */
@Entity
@Table(name = "USUAR_GRP")
@NamedQueries({ @NamedQuery(name = "UsuarioGrupo.findAll", query = "select t from UsuarioGrupo t") })
public class UsuarioGrupo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4428329041387386548L;
	
	@EmbeddedId
	private UsuarioGrupoCompositeId id;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_USUAR", nullable=false, insertable=false, updatable=false)
	private Usuario usuario;
	
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="N_GRP", nullable=false, insertable=false, updatable=false)
	private Grupo grupo;

	public UsuarioGrupoCompositeId getId() {
		return id;
	}

	public void setId(UsuarioGrupoCompositeId id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
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
		UsuarioGrupo other = (UsuarioGrupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}