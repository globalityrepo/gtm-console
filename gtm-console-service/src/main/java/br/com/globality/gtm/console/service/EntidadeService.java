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
 * Session Bean para gerencimento da entidade Entidade.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
@Repository
@Transactional
public class EntidadeService {
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public List<Entidade> findByFiltroComPaginacao(String filtro, int pageSize, int currentPage, Usuario usuarioLogado) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Entidade> cq = cb.createQuery(Entidade.class);
			Root<Entidade> rootEntry = cq.from(Entidade.class);
			CriteriaQuery<Entidade> all = cq.select(rootEntry);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(cb.or(
						cb.like(cb.upper(rootEntry.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(rootEntry.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(rootEntry.<String>get("comentario")), "%" + filtro.toUpperCase() + "%")));
			}
			// Verificando permissão de acesso.
			if (!usuarioLogado.getAdmDePara()) {
				
				// Subquery nas aplicações associadas.
				Subquery<EntidadeAplicacao> subquery01 = all.subquery(EntidadeAplicacao.class); 
				Root<EntidadeAplicacao> entidadeAplicacaoRootEntry = subquery01.from(EntidadeAplicacao.class);  
				subquery01.select(entidadeAplicacaoRootEntry);  
				Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAplicacaoRootEntry.join("entidade");
				List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>(); 
				subQueryPredicates01.add(cb.equal(entidadeJoin.get("id"), rootEntry.get("id")));
				
				// Subquery em acessos delegados.
				Subquery<AcessoDelegado> subquery02 = subquery01.subquery(AcessoDelegado.class); 
				Root<AcessoDelegado> acessoDelegadoRootEntry = subquery02.from(AcessoDelegado.class);  
				subquery02.select(acessoDelegadoRootEntry);  
				Join<AcessoDelegado, Recurso> recursoJoin = acessoDelegadoRootEntry.join("recurso");
				Join<AcessoDelegado, Usuario> usuarioJoin = acessoDelegadoRootEntry.join("usuario");
				List<Predicate> subQueryPredicates02 = new ArrayList<Predicate>();
				subQueryPredicates02.add(cb.equal(acessoDelegadoRootEntry.get("idReferencia"), entidadeAplicacaoRootEntry.get("id")));
				subQueryPredicates02.add(cb.equal(recursoJoin.get("codigo"), AppConstants.COD_RECURSO_ENTIDADE));
				subQueryPredicates02.add(cb.equal(usuarioJoin.get("id"), usuarioLogado.getId()));
				subquery02.where(subQueryPredicates02.toArray(new Predicate[]{})); 
				
				subQueryPredicates01.add(cb.or(cb.exists(subquery02), cb.equal(entidadeAplicacaoRootEntry.<Boolean>get("liberarAcesso"), Boolean.TRUE)));
				subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
				predicates.add(cb.exists(subquery01));
			
			}
			cq.where(predicates.toArray(new Predicate[]{}));
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<Entidade> paginationQuery = em.createQuery(all).setFirstResult(((currentPage-1)*pageSize))
					.setMaxResults(pageSize);
			List<Entidade> result = paginationQuery.getResultList();
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
			Root<Entidade> rootEntry = cq.from(Entidade.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			if (StringUtils.isNotBlank(filtro)) {
				predicates.add(cb.or(
						cb.like(cb.upper(rootEntry.<String>get("codigo")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(rootEntry.<String>get("descricao")), "%" + filtro.toUpperCase() + "%"),
						cb.like(cb.upper(rootEntry.<String>get("comentario")), "%" + filtro.toUpperCase() + "%")));
			}
			// Verificando permissão de acesso.
			if (!usuarioLogado.getAdmDePara()) {
				
				// Subquery nas aplicações associadas.
				Subquery<EntidadeAplicacao> subquery01 = counter.subquery(EntidadeAplicacao.class); 
				Root<EntidadeAplicacao> entidadeAplicacaoRootEntry = subquery01.from(EntidadeAplicacao.class);  
				subquery01.select(entidadeAplicacaoRootEntry);  
				Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAplicacaoRootEntry.join("entidade");
				List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>(); 
				subQueryPredicates01.add(cb.equal(entidadeJoin.get("id"), rootEntry.get("id")));
				
				// Subquery em acessos delegados.
				Subquery<AcessoDelegado> subquery02 = subquery01.subquery(AcessoDelegado.class); 
				Root<AcessoDelegado> acessoDelegadoRootEntry = subquery02.from(AcessoDelegado.class);  
				subquery02.select(acessoDelegadoRootEntry);  
				Join<AcessoDelegado, Recurso> recursoJoin = acessoDelegadoRootEntry.join("recurso");
				Join<AcessoDelegado, Usuario> usuarioJoin = acessoDelegadoRootEntry.join("usuario");
				List<Predicate> subQueryPredicates02 = new ArrayList<Predicate>();
				subQueryPredicates02.add(cb.equal(acessoDelegadoRootEntry.get("idReferencia"), entidadeAplicacaoRootEntry.get("id")));
				subQueryPredicates02.add(cb.equal(recursoJoin.get("codigo"), AppConstants.COD_RECURSO_ENTIDADE));
				subQueryPredicates02.add(cb.equal(usuarioJoin.get("id"), usuarioLogado.getId()));
				subquery02.where(subQueryPredicates02.toArray(new Predicate[]{})); 
				
				subQueryPredicates01.add(cb.or(cb.exists(subquery02), cb.equal(entidadeAplicacaoRootEntry.<Boolean>get("liberarAcesso"), Boolean.TRUE)));
				subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
				predicates.add(cb.exists(subquery01));
			
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
	
	public List<EntidadeAplicacao> findEntidadeAplicacaoyIdEntidade(Long idEntidade, Long idEntidadeAplicacaoA, Usuario usuarioLogado) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EntidadeAplicacao> cq = cb.createQuery(EntidadeAplicacao.class);
			Root<EntidadeAplicacao> rootEntry = cq.from(EntidadeAplicacao.class);
			CriteriaQuery<EntidadeAplicacao> all = cq.select(rootEntry);
			Join<EntidadeAplicacao, Entidade> entidadeJoin = rootEntry.join("entidade");
			Join<EntidadeAplicacao, Aplicacao> aplicacaoJoin = rootEntry.join("aplicacao");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeJoin.get("id"), idEntidade));
			if (idEntidadeAplicacaoA!=null) {
				predicates.add(cb.notEqual(rootEntry.get("id"), idEntidadeAplicacaoA));
			}	
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			// Verificando permissão de acesso.
			if (usuarioLogado!=null && !usuarioLogado.getAdmDePara()) {				
				// Subquery em acessos delegados.
				Subquery<AcessoDelegado> subquery01 = all.subquery(AcessoDelegado.class); 
				Root<AcessoDelegado> acessoDelegadoRootEntry = subquery01.from(AcessoDelegado.class);  
				subquery01.select(acessoDelegadoRootEntry);  
				Join<AcessoDelegado, Recurso> recursoJoin = acessoDelegadoRootEntry.join("recurso");
				Join<AcessoDelegado, Usuario> usuarioJoin = acessoDelegadoRootEntry.join("usuario");
				List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>();
				subQueryPredicates01.add(cb.equal(acessoDelegadoRootEntry.get("idReferencia"), rootEntry.get("id")));
				subQueryPredicates01.add(cb.equal(recursoJoin.get("codigo"), AppConstants.COD_RECURSO_ENTIDADE));
				subQueryPredicates01.add(cb.equal(usuarioJoin.get("id"), usuarioLogado.getId()));
				subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
				subQueryPredicates01.add(cb.exists(subquery01));
				predicates.add(cb.or(cb.exists(subquery01), cb.equal(rootEntry.<Boolean>get("liberarAcesso"), Boolean.TRUE)));
			}
			cq.where(predicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.asc(aplicacaoJoin.get("codigo")));
			TypedQuery<EntidadeAplicacao> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<Registro> findRegistroByIdEntidadeAplicacao(Long idEntidadeAplicacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Registro> cq = cb.createQuery(Registro.class);
			Root<Registro> rootEntry = cq.from(Registro.class);
			CriteriaQuery<Registro> all = cq.select(rootEntry);
			Join<Registro, EntidadeAplicacao> entidadeAplicacaoJoin = rootEntry.join("entidadeAplicacao");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeAplicacaoJoin.get("id"), idEntidadeAplicacao));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<Registro> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasAssociacaoDeParaByIdRegistro(Long idRegistro) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<RegistroDePara> rootEntry = cq.from(RegistroDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<RegistroDePara, Registro> registroAJoin = rootEntry.join("registroDe");
			Join<RegistroDePara, Registro> registroBJoin = rootEntry.join("registroPara");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(
					cb.or(cb.equal(registroAJoin.get("id"), idRegistro),
						  cb.equal(registroBJoin.get("id"), idRegistro)
					)
				);
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasAssociacaoDeParaByIdEntidadeAplicacao(Long idEntidadeAplicacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoAJoin = rootEntry.join("entidadeAplicacaoDe");
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoBJoin = rootEntry.join("entidadeAplicacaoPara");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(
					cb.or(cb.equal(entidadeAplicacaoAJoin.get("id"), idEntidadeAplicacao),
						  cb.equal(entidadeAplicacaoBJoin.get("id"), idEntidadeAplicacao)
					)
				);
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasAssociacaoDeParaAByIdEntidade(Long idEntidade) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoJoin = rootEntry.join("entidadeAplicacaoDe");
			Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAplicacaoJoin.join("entidade");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeJoin.get("id"), idEntidade));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasAssociacaoDeParaBByIdEntidade(Long idEntidade) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoJoin = rootEntry.join("entidadeAplicacaoPara");
			Join<EntidadeAplicacao, Entidade> entidadeJoin = entidadeAplicacaoJoin.join("entidade");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(cb.equal(entidadeJoin.get("id"), idEntidade));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasAssociacaoDeParaByIdEntidadeAplicacaoAAndIdEntidadeAplicacaoB(Long idEntidadeAplicacaoA, Long idEntidadeAplicacaoB) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<EntidadeAplicacaoDePara> rootEntry = cq.from(EntidadeAplicacaoDePara.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoDeJoin = rootEntry.join("entidadeAplicacaoDe");
			Join<EntidadeAplicacaoDePara, EntidadeAplicacao> entidadeAplicacaoParaJoin = rootEntry.join("entidadeAplicacaoPara");
			List<Predicate> predicates = new ArrayList<Predicate>(); 
			predicates.add(
				cb.or(cb.and(cb.equal(entidadeAplicacaoDeJoin.get("id"), idEntidadeAplicacaoA), cb.equal(entidadeAplicacaoParaJoin.get("id"), idEntidadeAplicacaoB))
			));
			predicates.add(cb.equal(rootEntry.<Boolean>get("ativo"), Boolean.TRUE));
			cq.where(predicates.toArray(new Predicate[]{})); 
			TypedQuery<Long> query = em.createQuery(counter);
			return (query.getSingleResult()>0) ? Boolean.TRUE : Boolean.FALSE;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
			
}
