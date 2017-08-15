package br.com.globality.gtm.console.service;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.globality.gtm.console.model.EventoInstanciaDashboard;
import br.com.globality.gtm.console.model.EventoNivel;
import br.com.globality.gtm.console.model.EventoTipo;
import br.com.globality.gtm.console.model.Grupo;
import br.com.globality.gtm.console.model.Transacao;
import br.com.globality.gtm.console.model.TransacaoGrupo;
import br.com.globality.gtm.console.model.TransacaoInstancia;
import br.com.globality.gtm.console.model.TransacaoParametro;
import br.com.globality.gtm.console.model.TransacaoParametroValor;
import br.com.globality.gtm.console.model.TransacaoPasso;
import br.com.globality.gtm.console.model.TransacaoPassoAcao;
import br.com.globality.gtm.console.model.TransacaoPassoAcaoPendente;
import br.com.globality.gtm.console.model.TransacaoPassoAcaoPendenteEvento;
import br.com.globality.gtm.console.model.TransacaoPassoAcaoTodo;
import br.com.globality.gtm.console.model.TransacaoPassoEventoNivel;
import br.com.globality.gtm.console.model.TransacaoPassoInstancia;
import br.com.globality.gtm.console.service.exception.DAOException;

/**
 * Session Bean para gerencimento da entidade Transacao.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless 
@LocalBean
@Repository
@Transactional
public class TransacaoService {
	
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public List<TransacaoPasso> findTransacaoPassoByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPasso> cq = cb.createQuery(TransacaoPasso.class);
			Root<TransacaoPasso> rootEntry = cq.from(TransacaoPasso.class);
			CriteriaQuery<TransacaoPasso> all = cq.select(rootEntry);
			Join<TransacaoPasso, Transacao> transacaoJoin = rootEntry.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<TransacaoPasso> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<TransacaoParametro> findTransacaoParametroByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoParametro> cq = cb.createQuery(TransacaoParametro.class);
			Root<TransacaoParametro> rootEntry = cq.from(TransacaoParametro.class);
			CriteriaQuery<TransacaoParametro> all = cq.select(rootEntry);
			Join<TransacaoParametro, Transacao> transacaoJoin = rootEntry.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
			cq.orderBy(cb.asc(rootEntry.get("nome")));
			TypedQuery<TransacaoParametro> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<Transacao> findTransacaoByIdTransacaoGrupo(Long idTransacaoGrupo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Transacao> cq = cb.createQuery(Transacao.class);
			Root<Transacao> rootEntry = cq.from(Transacao.class);
			CriteriaQuery<Transacao> all = cq.select(rootEntry);
			Join<Transacao, TransacaoGrupo> transacaoGrupoJoin = rootEntry.join("transacaoGrupo");
			Predicate predicate = cb.equal(transacaoGrupoJoin.get("id"), idTransacaoGrupo);
			cq.where(predicate);
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<Transacao> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<TransacaoInstancia> findTransacaoInstanciaByFiltro(Date dataInicial, 
			Date dataFinal, List<Grupo> grupos, List<Transacao> transacoes, List<EventoNivel> eventosNivel, Long qtdeRegistros) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoInstancia> cq = cb.createQuery(TransacaoInstancia.class);
			Root<TransacaoInstancia> rootEntry = cq.from(TransacaoInstancia.class);
			CriteriaQuery<TransacaoInstancia> all = cq.select(rootEntry);
			List<Predicate> mainQueryPredicates = new ArrayList<Predicate>(); 			
			Subquery<EventoInstanciaDashboard> subquery = all.subquery(EventoInstanciaDashboard.class); 
			Root<EventoInstanciaDashboard> eventoInstanciaRootEntry = subquery.from(EventoInstanciaDashboard.class);  
			subquery.select(eventoInstanciaRootEntry);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>(); 
			subQueryPredicates.add(cb.equal(eventoInstanciaRootEntry.get("idTransacaoInstancia"), rootEntry.get("id")));
			if (dataInicial!=null) {
				subQueryPredicates.add(cb.greaterThanOrEqualTo(rootEntry.<Date>get("dataTransacaoInstancia"), dataInicial));
			}
			if (dataFinal!=null) {
				subQueryPredicates.add(cb.lessThanOrEqualTo(rootEntry.<Date>get("dataTransacaoInstancia"), dataFinal));
			}
			if (transacoes!=null && !transacoes.isEmpty()) {
				Predicate transacaoPredicate = null;
				for (Transacao transacao : transacoes) {
					Subquery<TransacaoInstancia> subquery01 = subquery.subquery(TransacaoInstancia.class); 
					Root<TransacaoInstancia> transacaoInstanciaRootEntry = subquery01.from(TransacaoInstancia.class);  
					subquery01.select(transacaoInstanciaRootEntry);  					
					List<Predicate> subQueryPredicates01 = new ArrayList<Predicate>(); 
					Join<TransacaoInstancia, TransacaoGrupo> transacaoGrupoJoin01 = transacaoInstanciaRootEntry.join("transacaoGrupo");
					Join<TransacaoGrupo, Transacao> transacaoJoin01 = transacaoGrupoJoin01.join("transacao");
					subQueryPredicates01.add(cb.equal(transacaoInstanciaRootEntry.get("id"), eventoInstanciaRootEntry.get("idTransacaoInstancia")));
					subQueryPredicates01.add(cb.equal(transacaoJoin01.get("id"), transacao.getId()));
					if (StringUtils.isNotBlank(transacao.getIdTransacaoInstancia())) {
						subQueryPredicates01.add(cb.equal(transacaoInstanciaRootEntry.get("id"), transacao.getIdTransacaoInstancia()));
					}
					if (transacao.getIdParametro()!=null && transacao.getIdParametro()>0) {
						Subquery<TransacaoParametro> subquery02 = subquery01.subquery(TransacaoParametro.class); 
						Root<TransacaoParametro> transacaoParametroRootEntry = subquery02.from(TransacaoParametro.class);  
						subquery02.select(transacaoParametroRootEntry);  			
						Join<TransacaoParametro, Transacao> transacaoJoin02 = transacaoParametroRootEntry.join("transacao");
						List<Predicate> subQueryPredicates02 = new ArrayList<Predicate>(); 
						subQueryPredicates02.add(cb.equal(transacaoJoin02.get("id"), transacaoJoin01.get("id")));
						subQueryPredicates02.add(cb.equal(transacaoParametroRootEntry.get("id"), transacao.getIdParametro()));
						if (StringUtils.isNotBlank(transacao.getValorParametro())) {
							Subquery<TransacaoParametroValor> subquery03 = subquery02.subquery(TransacaoParametroValor.class); 
							Root<TransacaoParametroValor> transacaoParametroValorRootEntry = subquery03.from(TransacaoParametroValor.class);  
							subquery03.select(transacaoParametroValorRootEntry);  		
							Join<TransacaoParametroValor, TransacaoParametro> transacaoParametroJoin = transacaoParametroValorRootEntry.join("transacaoParametro");
							List<Predicate> subQueryPredicates03 = new ArrayList<Predicate>(); 
							subQueryPredicates03.add(cb.equal(transacaoParametroJoin.get("id"), transacaoParametroRootEntry.get("id")));
							subQueryPredicates03.add(cb.equal(transacaoParametroValorRootEntry.get("valor"), transacao.getValorParametro()));
							subquery03.where(subQueryPredicates03.toArray(new Predicate[]{}));
							subQueryPredicates02.add(cb.exists(subquery03));
						}
						subquery02.where(subQueryPredicates02.toArray(new Predicate[]{}));
						subQueryPredicates01.add(cb.exists(subquery02));
					}
					List<Long> params = new ArrayList<Long>();
					for (Grupo grupo : grupos) {
						params.add(grupo.getId());
					}
					if (!params.isEmpty()) {
						Expression<Long> exp = transacaoGrupoJoin01.get("grupo");
						subQueryPredicates01.add(exp.in(params));
					}
					subquery01.where(subQueryPredicates01.toArray(new Predicate[]{})); 
					if (transacaoPredicate==null) {
						transacaoPredicate = cb.exists(subquery01);
					}
					else {
						transacaoPredicate = cb.or(transacaoPredicate,cb.exists(subquery01));
					}
				}
				subQueryPredicates.add(cb.and(transacaoPredicate));
			}
			List<String> params = new ArrayList<String>();
			for (EventoNivel eventoNivel : eventosNivel) {
				params.add(eventoNivel.getId());
			}
			if (!params.isEmpty()) {
				Expression<String> exp = eventoInstanciaRootEntry.get("codigoEventoNivel");
				subQueryPredicates.add(exp.in(params));
			}
			subquery.where(subQueryPredicates.toArray(new Predicate[]{})); 
			mainQueryPredicates.add(cb.exists(subquery));
			cq.where(mainQueryPredicates.toArray(new Predicate[]{})); 
			cq.orderBy(cb.desc(rootEntry.get("data")));
			TypedQuery<TransacaoInstancia> allQuery = em.createQuery(all).setMaxResults(qtdeRegistros.intValue());
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<EventoInstanciaDashboard> findEventoInstanciaByFiltro(List<TransacaoInstancia> transacoes) throws DAOException {
		try {
			if (transacoes!=null && !transacoes.isEmpty()) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<EventoInstanciaDashboard> cq = cb.createQuery(EventoInstanciaDashboard.class);
				Root<EventoInstanciaDashboard> rootEntry = cq.from(EventoInstanciaDashboard.class);
				CriteriaQuery<EventoInstanciaDashboard> all = cq.select(rootEntry);
				List<Predicate> mainQueryPredicates = new ArrayList<Predicate>(); 	
				List<String> params = new ArrayList<String>();
				for (TransacaoInstancia transacao : transacoes) {
					params.add(transacao.getId());
				}
				Expression<String> exp01 = rootEntry.get("idTransacaoInstancia");
				mainQueryPredicates.add(exp01.in(params));
				cq.where(mainQueryPredicates.toArray(new Predicate[]{})); 
				List<Order> orderLst = new ArrayList<Order>();
				orderLst.add(cb.asc(rootEntry.get("data")));
				cq.orderBy(orderLst);
				TypedQuery<EventoInstanciaDashboard> allQuery = em.createQuery(all);
				return allQuery.getResultList();
			}
			return null;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<TransacaoPassoEventoNivel> findTransacaoPassoEventoNivelByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoEventoNivel> cq = cb.createQuery(TransacaoPassoEventoNivel.class);
			Root<TransacaoPassoEventoNivel> rootEntry = cq.from(TransacaoPassoEventoNivel.class);
			CriteriaQuery<TransacaoPassoEventoNivel> all = cq.select(rootEntry);
			Join<TransacaoPassoEventoNivel, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			cq.where(predicate);
			TypedQuery<TransacaoPassoEventoNivel> allQuery = em.createQuery(all);
			List<TransacaoPassoEventoNivel> result = allQuery.getResultList();
			return result;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<TransacaoPassoAcao> findTransacaoPassoAcaoByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoPassoAcao> cq = cb.createQuery(TransacaoPassoAcao.class);
			Root<TransacaoPassoAcao> rootEntry = cq.from(TransacaoPassoAcao.class);
			CriteriaQuery<TransacaoPassoAcao> all = cq.select(rootEntry);
			Join<TransacaoPassoAcao, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			cq.where(predicate);
			TypedQuery<TransacaoPassoAcao> allQuery = em.createQuery(all);
			List<TransacaoPassoAcao> result = allQuery.getResultList();
			return result;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<TransacaoGrupo> findGrupoByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TransacaoGrupo> cq = cb.createQuery(TransacaoGrupo.class);
			Root<TransacaoGrupo> rootEntry = cq.from(TransacaoGrupo.class);
			CriteriaQuery<TransacaoGrupo> all = cq.select(rootEntry);
			Join<TransacaoGrupo, Transacao> transacaoJoin = rootEntry.join("transacao");
			Join<TransacaoGrupo, Grupo> grupoJoin = rootEntry.join("grupo");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
			cq.orderBy(cb.asc(grupoJoin.get("codigo")));
			TypedQuery<TransacaoGrupo> allQuery = em.createQuery(all);
			List<TransacaoGrupo> result = allQuery.getResultList();
			return result;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<Transacao> findTransacaoByGrupos(List<Grupo> grupos) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Transacao> cq = cb.createQuery(Transacao.class);
			Root<Transacao> rootEntry = cq.from(Transacao.class);
			CriteriaQuery<Transacao> all = cq.select(rootEntry);
			
			Subquery<TransacaoGrupo> subquery = all.subquery(TransacaoGrupo.class); 
			Root<TransacaoGrupo> transacaoGrupoRootEntry = subquery.from(TransacaoGrupo.class);
			Join<TransacaoGrupo, Transacao> transacaoJoin = transacaoGrupoRootEntry.join("transacao");
			Join<TransacaoGrupo, Grupo> grupoJoin = transacaoGrupoRootEntry.join("grupo");
			subquery.select(transacaoGrupoRootEntry);
			List<Predicate> subqueryPredicates = new ArrayList<Predicate>(); 
			subqueryPredicates.add(cb.equal(transacaoJoin.get("id"), rootEntry.get("id")));
			if (grupos!=null && !grupos.isEmpty()) {
				List<Long> params = new ArrayList<Long>();
				for (Grupo grupo : grupos) {
					params.add(grupo.getId());
				}
				if (!params.isEmpty()) {
					Expression<String> exp01 = grupoJoin.get("id");
					subqueryPredicates.add(exp01.in(params));
				}
			}
			subquery.where(subqueryPredicates.toArray(new Predicate[]{})); 
			
			cq.where(cb.exists(subquery)); 
			cq.orderBy(cb.asc(rootEntry.get("codigo")));
			TypedQuery<Transacao> allQuery = em.createQuery(all);
			
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Boolean hasTransacaoInstanciaByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoInstancia> rootEntry = cq.from(TransacaoInstancia.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoInstancia, TransacaoGrupo> transacaoGrupoJoin = rootEntry.join("transacaoGrupo");
			Join<TransacaoGrupo, Transacao> transacaoJoin = transacaoGrupoJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoInstanciaByIdTransacaoGrupo(Long idTransacaoGrupo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoInstancia> rootEntry = cq.from(TransacaoInstancia.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoInstancia, TransacaoGrupo> transacaoGrupoJoin = rootEntry.join("transacaoGrupo");
			Predicate predicate = cb.equal(transacaoGrupoJoin.get("id"), idTransacaoGrupo);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoInstanciaByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoInstancia> rootEntry = cq.from(TransacaoPassoInstancia.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoInstancia, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPasso, Transacao> transacaoJoin = transacaoPassoJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoInstanciaByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoInstancia> rootEntry = cq.from(TransacaoPassoInstancia.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoInstancia, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoParametroValorByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoParametroValor> rootEntry = cq.from(TransacaoParametroValor.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoParametroValor, TransacaoParametro> transacaoParametroJoin = rootEntry.join("transacaoParametro");
			Join<TransacaoParametro, Transacao> transacaoJoin = transacaoParametroJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoParametroValorByIdTransacaoParametro(Long idTransacaoParametro) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoParametroValor> rootEntry = cq.from(TransacaoParametroValor.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoParametroValor, TransacaoParametro> transacaoParametroJoin = rootEntry.join("transacaoParametro");
			Predicate predicate = cb.equal(transacaoParametroJoin.get("id"), idTransacaoParametro);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoEventoNivelByIdTransacaoPasso(Long idTransacaoPasso, String idEventoNivel) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoEventoNivel> rootEntry = cq.from(TransacaoPassoEventoNivel.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoEventoNivel, EventoNivel> eventoNivelJoin = rootEntry.join("eventoNivel");
			Join<TransacaoPassoEventoNivel, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(eventoNivelJoin.get("id"), idEventoNivel);
			predicate = cb.and(predicate, cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso));
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoTodoByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoTodo> rootEntry = cq.from(TransacaoPassoAcaoTodo.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoTodo, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPasso, Transacao> transacaoJoin = transacaoPassoJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoTodoByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		return hasTransacaoPassoAcaoTodoByIdTransacaoPassoAcao(idTransacaoPasso, null);
	}
	
	public Boolean hasTransacaoPassoAcaoTodoByIdTransacaoPassoAcao(Long idTransacaoPasso, String idEventoTipo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoTodo> rootEntry = cq.from(TransacaoPassoAcaoTodo.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoTodo, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			if (StringUtils.isNotBlank(idEventoTipo)) {
				Join<TransacaoPassoAcaoTodo, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
				predicate = cb.and(predicate, cb.equal(eventoTipoJoin.get("id"), idEventoTipo));
			}
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoPendenteByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoPendente> rootEntry = cq.from(TransacaoPassoAcaoPendente.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoPendente, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPasso, Transacao> transacaoJoin = transacaoPassoJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoPendenteByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		return hasTransacaoPassoAcaoPendenteByIdTransacaoPassoAcao(idTransacaoPasso, null);
	}
	
	public Boolean hasTransacaoPassoAcaoPendenteByIdTransacaoPassoAcao(Long idTransacaoPasso, String idEventoTipo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoPendente> rootEntry = cq.from(TransacaoPassoAcaoPendente.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoPendente, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			if (StringUtils.isNotBlank(idEventoTipo)) {
				Join<TransacaoPassoAcaoPendente, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
				predicate = cb.and(predicate, cb.equal(eventoTipoJoin.get("id"), idEventoTipo));
			}
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoPendenteEventoByIdTransacao(Long idTransacao) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoPendenteEvento> rootEntry = cq.from(TransacaoPassoAcaoPendenteEvento.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoPendenteEvento, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Join<TransacaoPasso, Transacao> transacaoJoin = transacaoPassoJoin.join("transacao");
			Predicate predicate = cb.equal(transacaoJoin.get("id"), idTransacao);
			cq.where(predicate);
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
	
	public Boolean hasTransacaoPassoAcaoPendenteEventoByIdTransacaoPasso(Long idTransacaoPasso) throws DAOException {
		return hasTransacaoPassoAcaoPendenteEventoByIdTransacaoPassoAcao(idTransacaoPasso, null);
	}
	
	public Boolean hasTransacaoPassoAcaoPendenteEventoByIdTransacaoPassoAcao(Long idTransacaoPasso, String idEventoTipo) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<TransacaoPassoAcaoPendenteEvento> rootEntry = cq.from(TransacaoPassoAcaoPendenteEvento.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Join<TransacaoPassoAcaoPendenteEvento, TransacaoPasso> transacaoPassoJoin = rootEntry.join("transacaoPasso");
			Predicate predicate = cb.equal(transacaoPassoJoin.get("id"), idTransacaoPasso);
			if (StringUtils.isNotBlank(idEventoTipo)) {
				Join<TransacaoPassoAcaoPendenteEvento, EventoTipo> eventoTipoJoin = rootEntry.join("eventoTipo");
				predicate = cb.and(predicate, cb.equal(eventoTipoJoin.get("id"), idEventoTipo));
			}
			cq.where(predicate);
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
	
	public Boolean hasTransacaoByCodigo(String codigo, Long idRef) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Transacao> rootEntry = cq.from(Transacao.class);
			CriteriaQuery<Long> counter = cq.select(cb.count(rootEntry));
			Predicate predicate = cb.equal(cb.upper(rootEntry.<String>get("codigo")), codigo.trim().toUpperCase());
			predicate = cb.and(predicate, cb.notEqual(rootEntry.get("id"), idRef));
			cq.where(predicate);
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
