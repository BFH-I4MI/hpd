package ch.vivates.ihe.hpd.pid.exceptions;

/**
 * The Class AuthorizationException contains the needed functionality to
 * throw an authorization exception.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class AuthorizationException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -291533674456037321L;
	
	/**
	 * Instantiates a new authorization exception.
	 */
	public AuthorizationException() {
		super("This operation is not permitted.");
	}
	
	/**
	 * Instantiates a new authorization exception with a cause.
	 *
	 * @param cause the cause
	 */
	public AuthorizationException(Throwable cause) {
		super("This operation is not permitted.", cause);
	}
	
	/**
	 * Instantiates a new authorization exception with a message.
	 *
	 * @param message the message for the authorization exception.
	 */
	public AuthorizationException(String message) {
		super("This operation is not permitted:" + message);
	}
	
	/**
	 * Instantiates a new authorization exception with a message and a cause.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AuthorizationException(String message, Throwable cause) {
		super("This operation is not permitted:" + message, cause);
	}

}
