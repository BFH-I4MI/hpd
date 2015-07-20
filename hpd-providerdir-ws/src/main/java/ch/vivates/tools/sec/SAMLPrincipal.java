package ch.vivates.tools.sec;

import java.util.Collection;

import org.opensaml.saml2.core.Assertion;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The Class SAMLPrincipal.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class SAMLPrincipal extends AbstractAuthenticationToken {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8627230668614204523L;

	/** The assertion. */
	private final Assertion assertion;
	
	/**
	 * Instantiates a new SAML principal.
	 *
	 * @param assertion the assertion
	 */
	public SAMLPrincipal(Assertion assertion) {
		super(null);
		this.assertion = assertion;
	}
	
	/**
	 * Instantiates a new SAML principal.
	 *
	 * @param authorities the authorities
	 */
	public SAMLPrincipal(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.assertion = null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Assertion getCredentials() {
		return this.assertion;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public String getPrincipal() {
		if(assertion != null) {
			return this.assertion.getSubject().getNameID().getValue();
		} else {
			return "unknown";
		}
	}
	
}
