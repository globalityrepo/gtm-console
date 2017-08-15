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
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.globality.gtm.console.model.AcessoDelegado;
import br.com.globality.gtm.console.model.Aplicacao;
import br.com.globality.gtm.console.model.Entidade;
import br.com.globality.gtm.console.model.EntidadeAplicacao;
import br.com.globality.gtm.console.model.EntidadeAplicacaoDePara;
import br.com.globality.gtm.console.model.Recurso;
import br.com.globality.gtm.console.model.Registro;
import br.com.globality.gtm.console.model.RegistroDePara;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.service.exception.DAOException;
import br.com.globality.gtm.console.util.AppConstants;

/**
 * Session Bean para gerencimento da entidade EntidadeAplicacaoDePara.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
@Repository
@Transactional
public class DeParaService {
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public List<EntidadeAplicacaoDePara> findByFiltroComPaginacao(String filtro, int pageSize, int currentPage, Usuario usuarioLogado) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EntidadeAplicacaoDePara> cq = cb.createQuery(EntidadeAplicacaoDePara.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<EntidadeAplicacaoDePara> all = cq.select(rootEntry);
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAppDeJoin = rootEntry.join("entidadeAplicacaoDe");
			Join<EntidadeAplicacao, Aplicacao> appDeJoin = entidadeAppDeJoin.join("aplicacao");
			Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAppDeJoin.join("entidade");
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAppParaJoin = rootEntry.join("entidadeAplicacaoPara");
			Join<EntidadeAplicacao, Aplicacao> appParaJoin = entidadeAppParaJoin.join("aplicacao");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(cb.or(
						cb.like(cb.upper(rootEntry.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("comentario")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appDeJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appDeJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appParaJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appParaJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%")));
			}
			// Verificando permissão de acesso.
			if (!usuarioLogado.getAdmDePara()) {
				
				// Subquery em acessos delegados.
				Subquery<AcessoDelegado> subquery01 = all.subquery(AcessoDelegado.class); 
				Root<AcessoDelegado> acessoDelegadoRootEntry = subquery01.from(AcessoDelegado.class);  
				subquery01.select(acessoDelegadoRootEntry);  
				Join<AcessoDelegado, Recurso> recursoJoin = acessoDelegadoRootEntry.join("recurso");
				Join<AcessoDelegado, Usuario> usuarioJoin = acessoDelegadoRootEntry.join("usuario");
				List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>();
				subQueryPredicates01.add(cb.equal(acessoDelegadoRootEntry.get("idReferencia"), rootEntry.get("id")));
				subQueryPredicates01.add(cb.equal(recursoJoin.get("codigo"), AppConstants.COD_RECURSO_DEPARA));
				subQueryPredicates01.add(cb.equal(usuarioJoin.get("id"), usuarioLogado.getId()));
				subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
				predicates.add(cb.or(cb.exists(subquery01), cb.equal(rootEntry.<Boolean>get("liberarAcesso"), Boolean.TRUE)));
			
			}
			cq.where(predicates.toArray(new Predicate[]{}));
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<EntidadeAplicacaoDePara> paginationQuery = em.createQuery(all).setFirstResult(((currentPage-1)*pageSize))
					.setMaxResults(pageSize);
			List<EntidadeAplicacaoDePara> result = paginationQuery.getResultList();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Long countRegistrosByFiltro(String filtro, Usuario usuarioLogado) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAppDeJoin = rootEntry.join("entidadeAplicacaoDe");
			Join<EntidadeAplicacao, Aplicacao> appDeJoin = entidadeAppDeJoin.join("aplicacao");
			Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAppDeJoin.join("entidade");
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAppParaJoin = rootEntry.join("entidadeAplicacaoPara");
			Join<EntidadeAplicacao, Aplicacao> appParaJoin = entidadeAppParaJoin.join("aplicacao");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(cb.or(
						cb.like(cb.upper(rootEntry.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(entidadeJoin.<String>get("comentario")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appDeJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appDeJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appParaJoin.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(appParaJoin.<String>get("descricao")), "%" + filtro.toUpperCase() + "%")));
			}
			// Verificando permissão de acesso.
			if (!usuarioLogado.getAdmDePara()) {
				
				// Subquery em acessos delegados.
				Subquery<AcessoDelegado> subquery01 = counter.subquery(AcessoDelegado.class); 
				Root<AcessoDelegado> acessoDelegadoRootEntry = subquery01.from(AcessoDelegado.class);  
				subquery01.select(acessoDelegadoRootEntry);  
				Join<AcessoDelegado, Recurso> recursoJoin = acessoDelegadoRootEntry.join("recurso");
				Join<AcessoDelegado, Usuario> usuarioJoin = acessoDelegadoRootEntry.join("usuario");
				List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>();
				subQueryPredicates01.add(cb.equal(acessoDelegadoRootEntry.get("idReferencia"), rootEntry.get("id")));
				subQueryPredicates01.add(cb.equal(recursoJoin.get("codigo"), AppConstants.COD_RECURSO_DEPARA));
				subQueryPredicates01.add(cb.equal(usuarioJoin.get("id"), usuarioLogado.getId()));
				subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
				predicates.add(cb.or(cb.exists(subquery01), cb.equal(rootEntry.<Boolean>get("liberarAcesso"), Boolean.TRUE)));
			
			}
			cq.where(predicates.toArray(new Predicate[]{}));
			TypedQuery<Long> query = em.createQuery(counter);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<RegistroDePara> findRegistroDeParaIdEntidadeAplicacaoDePara(Long idEntidadeAplicacaoDePara) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<RegistroDePara> cq = cb.createQuery(RegistroDePara.class);
			Root<RegistroDePara> rootEntry = cq.from(RegistroDePara.class);
			CriteriaQuery<RegistroDePara> all = cq.select(rootEntry);
			Join<RegistroDePara, Registro> registroDeJoin = rootEntry.join("registroDe");
			Join<RegistroDePara, EntidadeAplicacaoDePara> entidadeAppDeParaJoin = rootEntry.join("entidadeAplicacaoDePara");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeAppDeParaJoin.get("id"), idEntidadeAplicacaoDePara));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.asc(registroDeJoin.get("codigo")));
			TypedQuery<RegistroDePara> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<RegistroDePara> findRegistrosByIdEntidadeAplicacaoDePara(Long idEntidadeAplicacaoDePara) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<RegistroDePara> cq = cb.createQuery(RegistroDePara.class);
			Root<RegistroDePara> rootEntry = cq.from(RegistroDePara.class);
			CriteriaQuery<RegistroDePara> all = cq.select(rootEntry);
			Join<RegistroDePara, EntidadeAplicacaoDePara> entidadeAplicacaoDeParaJoin = rootEntry.join("entidadeAplicacaoDePara");
			Join<RegistroDePara, Registro> registroDeJoin = rootEntry.join("registroDe");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeAplicacaoDeParaJoin.get("id"), idEntidadeAplicacaoDePara));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.asc(registroDeJoin.get("codigo")));
			TypedQuery<RegistroDePara> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Registro findRegistroEntidadeAplicacaoByCodigo(Long idEntidadeAplicacao, String codigo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Registro> cq = cb.createQuery(Registro.class);
			Root<Registro> rootEntry = cq.from(Registro.class);
			CriteriaQuery<Registro> all = cq.select(rootEntry);
			Join<Registro, EntidadeAplicacao> entidadeAplicacaoJoin = rootEntry.join("entidadeAplicacao");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeAplicacaoJoin.get("id"), idEntidadeAplicacao));
			predicates.add(cb.equal(rootEntry.<String>get("codigo"), codigo));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Registro> allQuery = em.createQuery(all);
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
