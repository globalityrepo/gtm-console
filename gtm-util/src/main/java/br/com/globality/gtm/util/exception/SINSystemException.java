package br.com.globality.gtm.util.exception;

/**
 * Exceção pai de todas as exceções de sistema.
 * 
 * @author <a href="mailto:asouza@redhat.com">Ângelo Galvão</a>
 */
public class SINSystemException extends RuntimeException {

	private static final long serialVersionUID = 7720172632206418386L;

	public SINSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SINSystemException(String message) {
		super(message);
	}

}
