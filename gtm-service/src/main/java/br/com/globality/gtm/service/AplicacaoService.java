package br.com.globality.gtm.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.globality.gtm.model.Aplicacao;

/**
 * Session Bean para gerencimento da entidade projeto.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a> 
 *
 */
@Stateless
@LocalBean
public class AplicacaoService {
	
	// @Inject
	@PersistenceContext(unitName="br.com.globality.gtm")
	private EntityManager em;
	
	/**
	 * Cria um novo projeto:
	 * 	- Adiciona no banco de dados;
	 *  - Inicia o procesos de negócio de identificação do projeto.
	 * @param projeto
	 * @return
	 */	
	public Aplicacao createProjeto(Aplicacao projeto){
		
		// Salva o projeto no banco de dados
		em.persist(projeto);
		
		// Necessário para recarregar os relacionamentos da entidade.
		em.flush();
		em.refresh(projeto);		
			
		return projeto;
		
	}
	
	public void addDadosFinanciamentoProjeto(){
		
	}
		
}
