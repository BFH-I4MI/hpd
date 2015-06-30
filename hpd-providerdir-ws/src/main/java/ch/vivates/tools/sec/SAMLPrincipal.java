package ch.vivates.tools.sec;

import java.util.Collection;

import org.opensaml.saml2.core.Assertion;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SAMLPrincipal extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8627230668614204523L;

	private final Assertion assertion;
	
	public SAMLPrincipal(Assertion assertion) {
		super(null);
		this.assertion = assertion;
	}
	
	public SAMLPrincipal(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.assertion = null;
	}

	@Override
	public Assertion getCredentials() {
		return this.assertion;
	}

	@Override
	public String getPrincipal() {
		if(assertion != null) {
			return this.assertion.getSubject().getNameID().getValue();
		} else {
			return "unknown";
		}
	}
	
}
