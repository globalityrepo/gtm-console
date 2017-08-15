package br.com.globality.gtm.console.service;

import java.lang.reflect.Field;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.globality.gtm.console.model.AbstractEntity;
import br.com.globality.gtm.console.model.annotation.GenericOrderByField;
import br.com.globality.gtm.console.model.annotation.GenericPredicateField;
import br.com.globality.gtm.console.service.exception.DAOException;

/**
 * Serviço genérico de persistência.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
@Repository
@Transactional
public class GenericPersitenceService {

	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;

	/**
	 * Adiciona uma entidade ao banco.
	 * 
	 * @param t
	 * @return
	 */
	public <T> T add(T t) throws DAOException {
		try {
			em.persist(t);
			return t;
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}

	/**
	 * Atualiza uma entidade
	 * 
	 * @param t
	 * @return
	 */
	public <T> T update(T t) throws DAOException {
		try {
			return em.merge(t);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}

	/**
	 * Remove uma entidade
	 * 
	 * @param t
	 * @param k
	 */
	public <K, T> void delete(Class<T> t, K k) throws DAOException  {
		try {
			AbstractEntity obj = (AbstractEntity)em.find(t, k);
			em.remove(obj);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
	/**
	 * Busca uma entidade pelo identificador
	 * 
	 * @param t
	 * @param k
	 * @return
	 */
	public <K, T> T findById(Class<T> t, K k) throws DAOException {
		try {
			return em.find(t, k);
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}		
	}
	
	/**
	 * Lista todos os registros da entidade informada.
	 * 
	 * @param t
	 * @return
	 */
	public <T> List<T> findAll(Class<T> t) throws DAOException {
		return findAll(t, false);
	}

	/**
	 * Busca todas as entidades do tipo especificado.
	 * 
	 * @param t
	 * @return
	 */
	public <T> List<T> findAll(Class<T> t, String filterColumn, String orderColumn) throws DAOException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(t);
			Root<T> root = query.from(t);
			query.select(root);
			if (!StringUtils.isBlank(filterColumn)) {
				Expression<Boolean> isRootEntryAtiva = root.get(filterColumn);
				query.where(builder.isTrue(isRootEntryAtiva));
			}
			if (!StringUtils.isBlank(orderColumn)) {
				query.orderBy(builder.asc(root.get(orderColumn)));
			}
			TypedQuery<T> q = em.createQuery(query);
			return q.getResultList();
		}
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}	
	}
	
	/**
	 * Monta o filtro das consultas.
	 * 
	 * @param Class<T>, CriteriaBuilder, rootEntry, String
	 * @return Predicate
	 */
	private <T> Predicate montarPredicateByFiltro(Class<T> t, CriteriaBuilder cb, Root<T> rootEntry,  String filtro) {
		Predicate predicate = null;
		for (Field f : t.getDeclaredFields()) {
		   GenericPredicateField genericField = (GenericPredicateField)f.getAnnotation(GenericPredicateField.class);
           if (genericField != null) {
        	   Expression<String> predicateExp = null;
        	   if (StringUtils.isBlank(genericField.value())) {
	        	   predicateExp = rootEntry.get(f.getName());
	           }
        	   else {
        		   Join<T, T> pedicateJoinExp = rootEntry.join(f.getName());
        		   predicateExp = pedicateJoinExp.get(genericField.value());
        	   }
        	   if (predicate == null) 
        		   predicate = cb.like(cb.upper(predicateExp), "%" + filtro.toUpperCase() + "%");
        	   else 
        		   predicate = cb.or(predicate, cb.like(cb.upper(predicateExp), "%" + filtro.toUpperCase() + "%"));
           }
        }
		return predicate;
	}
	
	private <T> List<Order> montarOrderBy(Class<T> t, CriteriaBuilder cb, Root<T> rootEntry) {
		List<Order> orderLst = null;
		for (Field f : t.getDeclaredFields()) {
		   GenericOrderByField genericField = (GenericOrderByField)f.getAnnotation(GenericOrderByField.class);
           if (genericField != null) {
        	   if (orderLst==null)
        		   orderLst = new ArrayList<Order>();
        	   if (genericField.value().equalsIgnoreCase("asc")) {
        		   orderLst.add(cb.asc(rootEntry.get(f.getName())));
        	   }
        	   else {
        		   orderLst.add(cb.desc(rootEntry.get(f.getName())));
        	   }
           }
        }
		return orderLst;
	}
	
	/**
	 * Totaliza query conforme filtro informado.
	 * 
	 * @param Class<T>, String
	 * @return Long
	 */
	public <T> Long countRegistrosByFiltro(Class<T> t, String filtro, boolean applyFilterByAtivo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<T> rootEntry = cq.from(t);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			List<Predicate> predicates = new ArrayList<Predicate>();
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(montarPredicateByFiltro(t, cb, rootEntry, filtro));
			}
			if (applyFilterByAtivo) {
				Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
				predicates.add(cb.isTrue(isRootEntryAtivo));
			}
			if (!predicates.isEmpty())
				cq.where(predicates.toArray(new Predicate[]{}));
			TypedQuery<Long> query = em.createQuery(counter);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public <T> Long countRegistrosByFiltro(Class<T> t, String filtro) throws DAOException {
		return countRegistrosByFiltro(t, filtro, false);
	}
	
	public <T> List<T> findByFiltroComPaginacao(Class<T> t, String filtro, int pageSize, int currentPage, boolean applyFilterByAtivo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(t);
			Root<T> rootEntry = cq.from(t);
			CriteriaQuery<T> all = cq.select(rootEntry);
			List<Predicate> predicates = new ArrayList<Predicate>();
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(montarPredicateByFiltro(t, cb, rootEntry, filtro));
			}
			if (applyFilterByAtivo) {
				Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
				predicates.add(cb.isTrue(isRootEntryAtivo));
			}
			if (!predicates.isEmpty())
				cq.where(predicates.toArray(new Predicate[]{}));
			List<Order> orderLst = montarOrderBy(t, cb, rootEntry);
			if (orderLst!=null) 
				cq.orderBy(orderLst);
			TypedQuery<T> paginationQuery = em.createQuery(all).setFirstResult(((currentPage-1)*pageSize))
					.setMaxResults(pageSize);
			List<T> result = paginationQuery.getResultList();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public <T> List<T> findByFiltroComPaginacao(Class<T> t, String filtro, int pageSize, int currentPage) throws DAOException {
		return findByFiltroComPaginacao(t, filtro, pageSize, currentPage, false);
	}
	
	public <T> List<T> findByFiltro(Class<T> t, String filtro, boolean applyFilterByAtivo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(t);
			Root<T> rootEntry = cq.from(t);
			CriteriaQuery<T> all = cq.select(rootEntry);
			List<Predicate> predicates = new ArrayList<Predicate>();
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(montarPredicateByFiltro(t, cb, rootEntry, filtro));
			}
			if (applyFilterByAtivo) {
				Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
				predicates.add(cb.isTrue(isRootEntryAtivo));
			}
			if (!predicates.isEmpty())
				cq.where(predicates.toArray(new Predicate[]{}));
			List<Order> orderLst = montarOrderBy(t, cb, rootEntry);
			if (orderLst!=null) 
				cq.orderBy(orderLst);
			TypedQuery<T> paginationQuery = em.createQuery(all);
			List<T> result = paginationQuery.getResultList();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public <T> List<T> findByFiltro(Class<T> t, String filtro) throws DAOException {
		return findByFiltro(t, filtro, false);
	}
	
	public <T> List<T> findAll(Class<T> t, boolean applyFilterByAtivo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(t);
			Root<T> rootEntry = cq.from(t);
			CriteriaQuery<T> all = cq.select(rootEntry);
			if (applyFilterByAtivo) {
				Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
				Predicate predicate = cb.isTrue(isRootEntryAtivo);
				cq.where(predicate);
			}
			List<Order> orderLst = montarOrderBy(t, cb, rootEntry);
			if (orderLst!=null) 
				cq.orderBy(orderLst);
			TypedQuery<T> paginationQuery = em.createQuery(all);
			List<T> result = paginationQuery.getResultList();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
}
