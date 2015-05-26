package ch.vivates.ihe.hpd.pid.exceptions;


public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = -291533674456037321L;
	
	public AuthorizationException() {
		super("This operation is not permitted.");
	}
	
	public AuthorizationException(Throwable cause) {
		super("This operation is not permitted.", cause);
	}
	
	public AuthorizationException(String message) {
		super("This operation is not permitted:" + message);
	}
	
	public AuthorizationException(String message, Throwable cause) {
		super("This operation is not permitted:" + message, cause);
	}

}
