package br.com.globality.gtm.console.service;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.globality.gtm.console.model.Grupo;
import br.com.globality.gtm.console.model.Perfil;
import br.com.globality.gtm.console.model.PerfilRecursoPermissao;
import br.com.globality.gtm.console.model.Recurso;
import br.com.globality.gtm.console.model.Usuario;
import br.com.globality.gtm.console.model.UsuarioGrupo;
import br.com.globality.gtm.console.service.exception.DAOException;

/**
 * Session Bean para gerencimento da entidade Usuário.
 * 
 * @author Leonardo Andrade
 *
 */
@Stateless
@LocalBean
@Repository
@Transactional
public class UsuarioService {
		
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	public Usuario efetuarLogin(String login, String senha) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
			Root<Usuario> rootEntry = cq.from(Usuario.class);
			CriteriaQuery<Usuario> all = cq.select(rootEntry);
			Predicate predicate = cb.equal(cb.upper(rootEntry.<String>get("codigo")), login.toUpperCase());
			predicate = cb.and(predicate, cb.equal(rootEntry.get("senha"), senha));
			Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
			predicate = cb.and(predicate, cb.isTrue(isRootEntryAtivo));
			cq.where(predicate);
			TypedQuery<Usuario> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<Recurso> findRecursosPermitidosByIdPerfil(Long idPerfil) throws DAOException {
		
		try {
			
			Session session = em.unwrap(Session.class);
			
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT REC.NU_RECURSO as id, REC.CO_RECURSO as codigo ");
			sql.append(" FROM ISC_TB023_RECURSO REC ");
			sql.append(" WHERE REC.IC_ATIVO = 1 ");
			sql.append(" CONNECT BY PRIOR REC.NU_RECURSO_PAI = REC.NU_RECURSO ");
			sql.append(" START WITH REC.NU_RECURSO IN ( ");
			sql.append("   SELECT REC.NU_RECURSO ");
			sql.append("   FROM ISC_TB025_PERFIL_REC_PERMISSAO PRP ");
			sql.append("   INNER JOIN ISC_TB023_RECURSO REC ");
			sql.append("   ON REC.NU_RECURSO = PRP.NU_RECURSO ");
			sql.append("   INNER JOIN ISC_TB022_PERMISSAO PMS ");
			sql.append("   ON PMS.NU_PERMISSAO = PRP.NU_PERMISSAO ");
			sql.append("   WHERE PRP.IC_ATIVO = 1 ");
			sql.append("   AND PRP.NU_PERFIL = ? ");
			sql.append("   AND PMS.CO_PERMISSAO = 'CON' ");
			sql.append(" ) ");
	
			SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
			sqlQuery.setLong(0, idPerfil);
			sqlQuery.addScalar("id", StandardBasicTypes.LONG);
			sqlQuery.addScalar("codigo", StandardBasicTypes.STRING);
			sqlQuery.setResultTransformer(Transformers.aliasToBean(Recurso.class));
			
			List<Recurso> result = sqlQuery.list();
			return result;
			
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
		
	}
	
	public List<PerfilRecursoPermissao> findPermissoesByIdPerfilAndCodRecurso(Long idPerfil, String codRecurso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PerfilRecursoPermissao> cq = cb.createQuery(PerfilRecursoPermissao.class);
			Root<PerfilRecursoPermissao> rootEntry = cq.from(PerfilRecursoPermissao.class);
			CriteriaQuery<PerfilRecursoPermissao> all = cq.select(rootEntry);
			Join<PerfilRecursoPermissao, Perfil> perfilJoin = rootEntry.join("perfil");
			Join<PerfilRecursoPermissao, Recurso> recursoJoin = rootEntry.join("recurso");
			Predicate predicate = cb.equal(perfilJoin.get("id"), idPerfil);
			predicate = cb.and(cb.equal(recursoJoin.get("codigo"), codRecurso));
			Expression<Boolean> isRootEntryAtivo = rootEntry.get("ativo");
			predicate = cb.and(predicate, cb.isTrue(isRootEntryAtivo));
			cq.where(predicate);
			TypedQuery<PerfilRecursoPermissao> allQuery = em.createQuery(all);
			return allQuery.getResultList();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public Usuario findUsuarioByCodigoAcesso(String codigoAcesso) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
			Root<Usuario> rootEntry = cq.from(Usuario.class);
			CriteriaQuery<Usuario> all = cq.select(rootEntry);
			Expression<String> codigoAcessoExp = rootEntry.get("codigo");
			Predicate predicate = cb.equal(cb.upper(codigoAcessoExp), codigoAcesso.toUpperCase());
			cq.where(predicate);
			TypedQuery<Usuario> allQuery = em.createQuery(all);
			return allQuery.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
	
	public List<UsuarioGrupo> findUsuarioGrupoByIdUsuario(Long idUsuario) throws DAOException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<UsuarioGrupo> cq = cb.createQuery(UsuarioGrupo.class);
			Root<UsuarioGrupo> rootEntry = cq.from(UsuarioGrupo.class);
			CriteriaQuery<UsuarioGrupo> all = cq.select(rootEntry);
			Join<UsuarioGrupo, Usuario> usuarioJoin = rootEntry.join("usuario");
			Join<UsuarioGrupo, Grupo> grupoJoin = rootEntry.join("grupo");
			Predicate predicate = cb.equal(usuarioJoin.get("id"), idUsuario);
			cq.where(predicate);
			cq.orderBy(cb.asc(grupoJoin.get("codigo")));
			TypedQuery<UsuarioGrupo> allQuery = em.createQuery(all);
			List<UsuarioGrupo> result = allQuery.getResultList();
			return result;
		}
		catch (NoResultException e) {
			return null;
		}	
		catch (Exception e) {
			throw new DAOException(e.getMessage(), e);
		}
	}
		
}
