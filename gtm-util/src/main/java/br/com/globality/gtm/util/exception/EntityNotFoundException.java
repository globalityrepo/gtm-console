package br.com.globality.gtm.util.exception;

/**
 * Exceção lançada caso não exista uma entidade específica
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 *
 */
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -3154450820938644370L;
	
	private Long id;
	
	public EntityNotFoundException(Long id) {
		this.id = id; 
	}
	
	public Long getId() {
		return id;
	}
}
