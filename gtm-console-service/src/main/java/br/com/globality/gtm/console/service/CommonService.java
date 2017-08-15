package br.com.globality.gtm.console.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.globality.gtm.console.model.AcessoDelegado;
import br.com.globality.gtm.console.model.ConfiguracaoSistema;
import br.com.globality.gtm.console.model.Recurso;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.exception.DAOException;

/**
 * Session Bean para gerencimento da entidades comuns.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
@Repository
@Transactional
public class CommonService {
		
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public ConfiguracaoSistema findConfiguracaoSistema() throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ConfiguracaoSistema> cq = cb.createQuery(ConfiguracaoSistema.class);
			Root<ConfiguracaoSistema> rootEntry = cq.from(ConfiguracaoSistema.class);
			CriteriaQuery<ConfiguracaoSistema> all = cq.select(rootEntry);
			TypedQuery<ConfiguracaoSistema> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<AcessoDelegado> findAcessosDelegadosByCodRecursoAndIdRef(String codigoRecurso, Long idRef) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AcessoDelegado> cq = cb.createQuery(AcessoDelegado.class);
			Root<AcessoDelegado> rootEntry = cq.from(AcessoDelegado.class);
			CriteriaQuery<AcessoDelegado> all = cq.select(rootEntry);
			Join<AcessoDelegado, Recurso> recursoJoin = rootEntry.join("recurso");
			Join<AcessoDelegado, Usuario> usuarioJoin = rootEntry.join("usuario");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(recursoJoin.get("codigo"), codigoRecurso));
			predicates.add(cb.equal(rootEntry.<Long>get("idReferencia"), idRef));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.asc(usuarioJoin.get("codigo")));
			TypedQuery<AcessoDelegado> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Recurso findRecursoByCodigo(String codigoRecurso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Recurso> cq = cb.createQuery(Recurso.class);
			Root<Recurso> rootEntry = cq.from(Recurso.class);
			CriteriaQuery<Recurso> all = cq.select(rootEntry);
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(rootEntry.get("codigo"), codigoRecurso));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Recurso> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
		
}
