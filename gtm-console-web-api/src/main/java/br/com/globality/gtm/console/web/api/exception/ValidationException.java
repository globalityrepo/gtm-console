package br.com.globality.gtm.console.web.api.exception;

/**
 * @author Leonardo Andrade
 *
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = -5060844520321022030L;

	public ValidationException(String message) {
		super(message);
	}
	
}
