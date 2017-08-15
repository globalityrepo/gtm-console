package br.com.globality.gtm.service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Serviço genérico de persistência.
 * 
 * @author jsantos
 *
 */
@Stateless
@LocalBean
public class GenericPersitenceService {

	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;

	/**
	 * Adiciona uma entidade ao banco.
	 * 
	 * @param t
	 * @return
	 */
	public <T> T add(T t) {
		em.persist(t);
		return t;
	}

	/**
	 * Atualiza uma entidade
	 * 
	 * @param t
	 * @return
	 */
	public <T> T update(T t) {
		return em.merge(t);
	}

	/**
	 * Remove uma entidade
	 * 
	 * @param t
	 * @param k
	 */
	public <K, T> void delete(Class<T> t, K k) {
		T obj = em.find(t, k);
		em.remove(obj);
	}

	/**
	 * Busca uma entidade pelo identificador
	 * 
	 * @param t
	 * @param k
	 * @return
	 */
	public <K, T> T findById(Class<T> t, K k) {
		return em.find(t, k);
	}

	/**
	 * Busca todas as entidades do tipo especificado.
	 * 
	 * @param t
	 * @return
	 */
	public <T> List<T> findAll(Class<T> t) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(t);
		Root<T> root = query.from(t);
		query.select(root);
		TypedQuery<T> q = em.createQuery(query);
		return q.getResultList();
	}
}
