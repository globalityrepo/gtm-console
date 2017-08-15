package br.com.globality.gtm.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;

import br.com.globality.gtm.model.Usuario;

/**
 * Session Bean para gerencimento da entidade Usuário.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
public class UsuarioService {
	
	@Inject
	private Logger log;
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public Usuario efetuarLogin(String login, String senha) throws Exception{
		try {
			TypedQuery<Usuario> query = em.createQuery("FROM Usuario p WHERE UPPER(p.codigo) = ? AND p.senha = ?", Usuario.class);
			query.setParameter(1, login.toUpperCase());
			query.setParameter(2, senha);
			Usuario usuario = query.getSingleResult(); 
			return usuario;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw e;
		}
	}
		
}
