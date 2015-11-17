package ch.bfh.i4mi.interceptor;

import java.util.List;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.interceptor.BaseInterceptor;
import org.apache.directory.server.core.api.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor keeps the member attribute of relationships in sync with the
 * memberOf attribute from an HPI or HOI.
 *
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
public class RelationshipInterceptor extends BaseInterceptor {

	/** The name of the attribute 'owner'. */
	private static final String OWNER_ATTR_NAME = "owner";

	/** The name of the attribute 'memberOf'. */
	private static final String MEMBER_OF_ATTR_NAME = "memberOf";

	/** The name of the attribute 'businessCategory'. */
	private static final String CAT_ATTR_NAME = "businessCategory";

	/** The value for a community in 'businessCategory'. */
	private static final String CAT_VALUE_FOR_COM = "community";

	/** RDN for the OU for 'HCRegulatedOrganization'. */
	private static final String OU_HEALTH_ORG = "ou=HCRegulatedOrganization";

	/** RDN for the OU for 'HCProfessional'. */
	private static final String OU_HEALTH_PRO = "ou=HCProfessional";

	/**
	 * The operation context.
	 */
	private OperationContext opContext;

	/**
	 * The entry to which the operation context belongs to.
	 */
	private Entry opContextEntry;

	/** logger used by this class. */
	private static final Logger LOG = LoggerFactory
			.getLogger(RelationshipInterceptor.class);

	/**
	 * Initialize the registers, normalizers.
	 *
	 * @param aDirectoryService the DirectoryService to initialize the parent.
	 * @throws LdapException the ldap exception
	 */
	public final void init(final DirectoryService aDirectoryService)
			throws LdapException {
		LOG.debug("Initialiazing the RelationshipInterceptor");
		super.init(aDirectoryService);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#add(org.apache.directory.server.core.api.interceptor.context.AddOperationContext)
	 */
	@Override
	public final void add(final AddOperationContext addOpContext)
			throws LdapException {
		this.opContext = addOpContext;
		this.opContextEntry = addOpContext.getEntry();


		if(isEntryOfOU(this.opContextEntry, OU_HEALTH_ORG) || isEntryOfOU(
				this.opContextEntry, OU_HEALTH_PRO)) {
			
			final Attribute attribute = this.opContextEntry.get(MEMBER_OF_ATTR_NAME);
			// Only check if the relationship is valid when a memberOf value is here.
			
			if(attribute == null) {
				if(!isCommunity(this.opContextEntry)) {
					throw new LdapException(
							"HOs and HPs must have at least one relationship.");
				}
			} else {
				RelationshipChecker relChecker;
	
				// Has a memberOf value, check if it is:
				//  - A community -> No checks at all, root must be is the sender of the request.
				//  - Not a community and is a HCProfessional -> Relationship checking is necessary.
				//  - Not a community and is a HCOrganization -> Relationship checking is necessary.
				
				relChecker = new RelationshipChecker(OWNER_ATTR_NAME,
						MEMBER_OF_ATTR_NAME, CAT_ATTR_NAME, CAT_VALUE_FOR_COM,
						OU_HEALTH_ORG, OU_HEALTH_PRO, this.opContext,
						this.opContextEntry, attribute);
				
				relChecker.checkAddRequest();
			}
		}

		LOG.debug("<< AddEntryRelationshipOperation : successful");
		next(addOpContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#modify(org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext)
	 */
	@Override
	public final void modify(final ModifyOperationContext modifyOpContext)
			throws LdapException {
		this.opContext = modifyOpContext;
		this.opContextEntry = modifyOpContext.getEntry();
		final List<Modification> items = modifyOpContext.getModItems();

		for (final Modification modification : items) {
			final ModificationOperation operation = modification.getOperation();
			final Attribute attribute = modification.getAttribute();
			
			RelationshipChecker relChecker = new RelationshipChecker(OWNER_ATTR_NAME,
					MEMBER_OF_ATTR_NAME, CAT_ATTR_NAME, CAT_VALUE_FOR_COM,
					OU_HEALTH_ORG, OU_HEALTH_PRO, this.opContext,
					this.opContextEntry, attribute);

			if (operation == ModificationOperation.ADD_ATTRIBUTE) {
				if (attribute.getUpId().equalsIgnoreCase(MEMBER_OF_ATTR_NAME)) {
					relChecker.checkModifyAddRequest();
				}
			} else if (operation == ModificationOperation.REMOVE_ATTRIBUTE) {
				int minNumberOfValues = 2;
				if (modification.getAttribute() != null) {
					minNumberOfValues = modification.getAttribute().size() + 1;
				}
				
				if (attribute.getUpId().equalsIgnoreCase(MEMBER_OF_ATTR_NAME)) {
					if (this.opContextEntry.get(MEMBER_OF_ATTR_NAME).size() < minNumberOfValues
							&& !isCommunity(this.opContextEntry)) {
						throw new LdapException(
								"HOs and HPs must have at least one relationship.");
					}
				}
			} else if (operation == ModificationOperation.REPLACE_ATTRIBUTE) {
				if (attribute.getUpId().equalsIgnoreCase(MEMBER_OF_ATTR_NAME)) {
					relChecker.checkModifyReplace();
				}
			}
		}
		LOG.debug("<< ModifyEntryRelationshipOperation : successful");
		next(modifyOpContext);
	}

	/**
	 * Checks if the entry is a community.
	 *
	 * @param entry the entry
	 * @return true, if is community
	 * @throws LdapInvalidAttributeValueException the LDAP invalid attribute value exception
	 */
	private static boolean isCommunity(final Entry entry)
			throws LdapInvalidAttributeValueException {
		
		if (entry.get(CAT_ATTR_NAME) != null
				&& entry.get(CAT_ATTR_NAME).getString()
						.equalsIgnoreCase(CAT_VALUE_FOR_COM)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is entry of ou.
	 *
	 * @param entry the entry
	 * @param rdnName the rdn name
	 * @return true, if is entry of ou
	 */
	private static boolean isEntryOfOU(final Entry entry, final String rdnName) {
		if (entry.getDn().getRdn(1).getName().equals(rdnName)) {
			return true;
		}
		return false;
	}
}
