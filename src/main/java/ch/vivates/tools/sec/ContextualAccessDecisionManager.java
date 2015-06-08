package ch.vivates.tools.sec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.SOAPException;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import ch.bfh.i4mi.validators.AttributeValidator;
import ch.vivates.ihe.hpd.pid.exceptions.AuthorizationException;
import ch.vivates.ihe.hpd.pid.model.cs.AddRequest;
import ch.vivates.ihe.hpd.pid.model.cs.BatchRequest;
import ch.vivates.ihe.hpd.pid.model.cs.DelRequest;
import ch.vivates.ihe.hpd.pid.model.cs.DsmlAttr;
import ch.vivates.ihe.hpd.pid.model.cs.DsmlMessage;
import ch.vivates.ihe.hpd.pid.model.cs.DsmlModification;
import ch.vivates.ihe.hpd.pid.model.cs.ModifyDNRequest;
import ch.vivates.ihe.hpd.pid.model.cs.ModifyRequest;

public class ContextualAccessDecisionManager implements AccessDecisionManager {

	private static final Logger LOG = LoggerFactory.getLogger("DecisionManager");

	private static final Set<ConfigAttribute> SUPPORTED_POLICIES = new HashSet<ConfigAttribute>();

	{
		SUPPORTED_POLICIES.add(new SecurityConfig("SCOPE"));
		SUPPORTED_POLICIES.add(new SecurityConfig("PUBLIC"));
	}

	private String basePath;
	private String relRdn = "ou=Relationship";
	private String orgRdn = "ou=HCRegulatedOrganization";
	private String hpRdn = "ou=HCProfessional";

	private LdapConnectionPool ldapConnectionPool;
	
	private AttributeValidator attributeValidator;

	@Override
	public void decide(Authentication authentication, Object exchange, Collection<ConfigAttribute> enforcingPolicies)
			throws AccessDeniedException, InsufficientAuthenticationException {
		LOG.info("Authorizing request from: "
				+ (authentication != null && !authentication.getName().isEmpty() ? authentication.getName() : "unknown"));
		LOG.info("Authenticated: " + authentication.isAuthenticated());
		for (ConfigAttribute ca : enforcingPolicies) {
			LOG.info("Enforcing following policies: " + ca.getAttribute());
			switch (ca.getAttribute()) {
			case "SCOPE":
				LOG.info("Scope check authorization applies.");
				checkScope(authentication, (BatchRequest) ((Exchange) exchange).getIn().getBody());
				break;
			case "PUBLIC":
				LOG.info("Public authorization applies.");
				break;
			default:
				LOG.error("Unsupported policy: " + ca.getAttribute());
			}
		}

	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return SUPPORTED_POLICIES.contains(attribute);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	// Möglicherweise boolean für admin rechte als parameter mitgeben und neue security
	// policy erstellen. Wäre die bessere Lösung
	
	private void checkScope(Authentication authentication, BatchRequest request) {
		if (authentication.getDetails() == null) {
			throw new AuthorizationException("Authentication is missing community ID.");
		}
		String communityUID = authentication.getDetails().toString();
		LdapConnection connection = null;
		try {
			connection = ldapConnectionPool.getConnection();

			for (DsmlMessage op : request.getBatchRequests()) {

				switch (getOperationName(op)) {
				case "AddRequest":
					AddRequest addRequest = (AddRequest) op;
					String targetAddDn = addRequest.getDn();
					Map<String, List<String>> attributesMap = getAttributesMap(addRequest);

					// ADD: Destination check
					if (attributesMap.get("objectClass").contains("HCRegulatedOrganization")
							&& !targetAddDn.endsWith(orgRdn + "," + basePath)) {
						throw new AuthorizationException(
								"[INVALID FEED REQ] Wrong element location: HCRegulatedOrganization objects must go under: [" + orgRdn
										+ "," + basePath + "]");
					}
					if (attributesMap.get("objectClass").contains("HCProfessional")
							&& !targetAddDn.endsWith(hpRdn + "," + basePath)) {
						throw new AuthorizationException(
								"[INVALID FEED REQ] Wrong element location: HCProfessional objects must go under: [" + hpRdn + ","
										+ basePath + "]");
					}
					if (attributesMap.get("objectClass").contains("groupOfNames")
							&& !targetAddDn.endsWith(relRdn + "," + basePath)) {
						throw new AuthorizationException(
								"[INVALID FEED REQ] Wrong element location: Relationship (groupOfNames) objects must go under: [" + relRdn
										+ "," + basePath + "]");
					}

					// ADD: Type check
					if (attributesMap.get("objectClass").contains("HCRegulatedOrganization")
							&& (!attributesMap.get("objectClass").contains("uidObject") || !attributesMap.get("objectClass")
									.contains("hpdProvider"))) {
						throw new AuthorizationException(
								"[INVALID FEED REQ] Missing auxiliary element class: HCRegulatedOrganization elements require auxiliary object classes: 'hpdProvider' and 'uidObject'");
					}
					if (attributesMap.get("objectClass").contains("HCProfessional")
							&& (!attributesMap.get("objectClass").contains("hpdProvider") || !attributesMap.get("objectClass")
									.contains("naturalPerson"))) {
						throw new AuthorizationException(
								"[INVALID FEED REQ] Missing auxiliary element class: HCRegulatedOrganization elements require auxiliary object classes: 'hpdProvider' and 'naturalPerson'");
					}
					
					// ***************** tuk1 *****************
					if (attributesMap.get("objectClass").contains(
							"businessCategory")) {
						for (DsmlAttr attr : addRequest.getAttr()) {
							// Checks if:
							//  - the attribute is 'businessCategory'
							//  - the value of the attribute is 'community'
							//  - the executing user is not the user 'root'
							if (attr.getName().equalsIgnoreCase(
									"businessCategory")
									&& attr.getValue().get(0)
											.equalsIgnoreCase("community")
									&& !authentication.getName()
											.equalsIgnoreCase("root")) {
								throw new AuthorizationException(
										"[INVALID FEED REQ] Community creation allowed only to 'root'");
							}
						}
					}
					// ***************** /tuk1 *****************

					// ADD-REL: New owner ORG in community
					if (targetAddDn.contains(relRdn)) {
						String ownerDn = attributesMap.get("owner").get(0);
						if (!StringUtils.isBlank(ownerDn) && connection.exists(ownerDn)) {
							verifyCommunityLink(ownerDn, communityUID, connection);
						} // TODO: We could detect an issue here: missing owner is a granted failure
					}
					
					// ***************** tuk1 *****************
					for (DsmlAttr attr : addRequest.getAttr()) {
						for (String value : attr.getValue()) {
							LOG.info("Attrname: " + attr.getName() + " value: " +value);
								if (!attributeValidator.checkTerminology(attr.getName(), value)) {
									throw new AuthorizationException(
											"[INVALID FEED REQ] Your attributes must match to the terminology from the index server.");
								}
						}
					}

					// ***************** /tuk1 *****************
					break;

				case "ModifyRequest":
					ModifyRequest modRequest = (ModifyRequest) op;
					Map<String, DsmlModification> modificationsMap = getModificationsMap(modRequest);

					String targetModDn = modRequest.getDn();

					// MOD-ORG: Is in community
					// MOD-HP: Is in community
					if (targetModDn.contains(orgRdn) || targetModDn.contains(hpRdn)) {
						verifyCommunityLink(targetModDn, communityUID, connection);
					}
					
					if (targetModDn.contains(relRdn)) {
						// MOD-REL: Current owner ORG is in community
						Entry existingRel = connection.lookup(targetModDn);
						if(existingRel != null) {
							verifyCommunityLink(existingRel.get("owner").getString(), communityUID, connection);
						}
						
						// MOD-REL: New owner ORG in community
						DsmlModification ownerModificaiton = modificationsMap.get("owner");
						if(ownerModificaiton != null && "replace".equals(ownerModificaiton.getOperation())) {
							if(!ownerModificaiton.getValue().isEmpty()) {
								verifyCommunityLink(ownerModificaiton.getValue().get(0), communityUID, connection);
							} // TODO: We could detect an issue here: empty owner is a granted failure
						}
					}

					break;

				case "DelRequest":
					DelRequest delRequest = (DelRequest) op;
					String targetDelDn = delRequest.getDn();
					
					// DEL-ORG: Is in community
					if (targetDelDn.contains(orgRdn)) {
						verifyCommunityLink(targetDelDn, communityUID, connection);
					}

					// DEL-HP: Has no relationships
					if (targetDelDn.contains(hpRdn)) {
						SearchRequest searchRequest = new SearchRequestImpl();
						searchRequest.setBase(new Dn(relRdn+","+basePath));
						searchRequest.setFilter("(objectClass=groupOfNames)");
						searchRequest.setScope(SearchScope.ONELEVEL);
						searchRequest.addAttributes("member="+targetDelDn);

						if(!connection.search(searchRequest).available()) {
							throw new AuthorizationException("[INVALID FEED REQ] HP has relationsips. Before removing a HP, remove all his relationships.");
						}
					}
					
					// DEL-REL: Current owner ORG is in community
					if (targetDelDn.contains(relRdn)) {
						Entry existingRel = connection.lookup(targetDelDn);
						verifyCommunityLink(existingRel.get("owner").getString(), communityUID, connection);
					}
					
					break;

				case "ModifyDNRequest":
					ModifyDNRequest modDnRequest = (ModifyDNRequest) op;
					
					// MOD-DN-ORG: No new superior
					// MOD-DN-HP: No new superior
					// MOD-DN-REL: No new superior
					if(!StringUtils.isBlank(modDnRequest.getNewSuperior())) {
						throw new AuthorizationException("[INVALID FEED REQ] Moving elements is forbidden. 'NewSuperior' attribute in ModifyDNRequest muss not be set.");
					}
					
					break;

				default:

				}
			}
		} catch (LdapException e) {
			LOG.error("Unable authorize query for policy: SCOPE", e);
			throw new AccessDeniedException("Unable authorize query for policy: SCOPE", e);
		} catch (SOAPException e) {
			LOG.error("Invalid terminology", e);
			throw new AccessDeniedException(
					"Unable authorize query for policy: TERMINOLOGY", e);
		} finally {
			if(connection != null) {
				try {
					ldapConnectionPool.releaseConnection(connection);
				} catch (LdapException e) {
					LOG.warn("Error releasing LDAP connection", e);
				}
			}
		}
	}

	private Map<String, List<String>> getAttributesMap(AddRequest addRequest) {
		Map<String, List<String>> attributesMap = new HashMap<String, List<String>>(addRequest.getAttr().size());
		for (DsmlAttr attr : addRequest.getAttr()) {
			attributesMap.put(attr.getName(), new CaseInsensitiveArrayList(attr.getValue()));
		}
		return attributesMap;
	}

	private Map<String, DsmlModification> getModificationsMap(ModifyRequest modRequest) {
		Map<String, DsmlModification> modificationsMap = new HashMap<String, DsmlModification>(modRequest.getModification().size());
		for (DsmlModification modif : modRequest.getModification()) {
			modificationsMap.put(modif.getName(), modif);
		}
		return modificationsMap;
	}

	private void verifyCommunityLink(String elementDn, String communityUID, LdapConnection connection) {
		List<String> topOrgDns = getTopParents(elementDn, connection);
		topOrgDns.add(elementDn);
		String uid = null;
		for (String dn : topOrgDns) {
			uid = dn.split(",")[0];
			if(uid.equalsIgnoreCase("uid="+communityUID)) {
				return;
			}
		}
		throw new AuthorizationException("[INVALID FEED REQ] Access denied. The element '"+elementDn+"' is not linked to your community.");
	}
	
	private List<String> getTopParents(String elementDn, LdapConnection connection) {
		List<String> topParents = new ArrayList<String>();
		getTopParents0(elementDn, topParents, connection);
		return topParents;
	}
	
	private boolean getTopParents0(String elementDn, List<String> topParents, LdapConnection connection) {
		SearchRequest searchRequest = new SearchRequestImpl();
		boolean hasParents = false;
		SearchCursor sc = null;
		
		try {
			searchRequest.setBase(new Dn(relRdn+","+basePath));
			searchRequest.setFilter("(&(objectClass=groupOfNames)(member="+elementDn+"))");
			searchRequest.setScope(SearchScope.ONELEVEL);
			
			sc = connection.search(searchRequest);
			String ownerDn;
			Entry entry;
			while(sc.next()) {
				try {
					entry = sc.getEntry();
					if(entry != null) {
						ownerDn = entry.get("owner").getString();
						if(!elementDn.equalsIgnoreCase(ownerDn)) {
							hasParents = true;
							topParents.add(ownerDn);
							getTopParents0(ownerDn, topParents, connection);
						}
					}
				} catch (LdapException e) {
					LOG.debug(e.getMessage(), e);
				}
			}
		} catch (LdapException e) {
			LOG.error("Error building search request.", e);
		} catch (CursorException e) {
			LOG.error("Error searching through LDAP query results.", e);
		} finally {
			if(sc != null) {
				sc.close();
			}
		}
		return hasParents;
	}
	
	private String getOperationName(DsmlMessage msg) {
		String fullClass = msg.getClass().getName();
		return fullClass.substring(fullClass.lastIndexOf(".") + 1);
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setRelRdn(String relRdn) {
		this.relRdn = relRdn;
	}

	public void setOrgRdn(String orgRdn) {
		this.orgRdn = orgRdn;
	}

	public void setHpRdn(String hpRdn) {
		this.hpRdn = hpRdn;
	}

	public void setLdapConnectionPool(LdapConnectionPool ldapConnectionPool) {
		this.ldapConnectionPool = ldapConnectionPool;
	}
	
	public void setAttributeValidator(AttributeValidator attributeValidator) {
		this.attributeValidator = attributeValidator;
	}
	
	private class CaseInsensitiveArrayList extends ArrayList<String> {
		
		private static final long serialVersionUID = 4937525101490935626L;
		
		public CaseInsensitiveArrayList(List<String> list) {
			super(list);
		}
		
	    @Override
	    public boolean contains(Object o) {
	        String paramStr = (String)o;
	        for (String s : this) {
	            if (paramStr.equalsIgnoreCase(s)) return true;
	        }
	        return false;
	    }
	}

}
