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

	/**
	 * Name of the attribute to check.
	 */
	private final static String OWNER_ATTR_NAME = "owner";

	private final static String MEMBER_OF_ATTR_NAME = "memberOf";

	private final static String CAT_ATTR_NAME = "businessCategory";

	private final static String CAT_VALUE_FOR_COM = "community";

	private final static String OU_HEALTH_ORG = "ou=HCRegulatedOrganization";

	private final static String OU_HEALTH_PRO = "ou=HCProfessional";

	// private final static String OU_HEALTH_RS = "ou=Relationship";

	/**
	 * The operation context.
	 */
	OperationContext opContext;

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
	 * @param aDirectoryService
	 *            the DirectoryService to initialize the parent.
	 * @throws LdapException
	 */
	public final void init(final DirectoryService aDirectoryService)
			throws LdapException {
		LOG.debug("Initialiazing the RelationshipInterceptor");
		super.init(aDirectoryService);
	}

	@Override
	public final void add(final AddOperationContext addOperationContext)
			throws LdapException {
		this.opContext = addOperationContext;
		this.opContextEntry = addOperationContext.getEntry();

		Attribute attribute = this.opContextEntry.get(MEMBER_OF_ATTR_NAME);
		// Only check if the relationship is valid when a memberOf value is here.
		if (attribute != null) {
			RelationshipChecker rc = new RelationshipChecker(OWNER_ATTR_NAME,
					MEMBER_OF_ATTR_NAME, CAT_ATTR_NAME, CAT_VALUE_FOR_COM,
					OU_HEALTH_ORG, OU_HEALTH_PRO, this.opContext,
					this.opContextEntry, attribute);

			if (!isCommunity(this.opContextEntry)
					&& (isEntryOfOU(this.opContextEntry, OU_HEALTH_ORG) || isEntryOfOU(
							this.opContextEntry, OU_HEALTH_PRO))) {
				rc.checkAddRequest();
			}
		}

		LOG.debug("<< AddEntryRelationshipOperation : successful");
		next(addOperationContext);
	}

	@Override
	public final void modify(final ModifyOperationContext modifyOperationContext)
			throws LdapException {
		this.opContext = modifyOperationContext;
		this.opContextEntry = modifyOperationContext.getEntry();
		List<Modification> items = modifyOperationContext.getModItems();

		for (Modification modification : items) {
			ModificationOperation operation = modification.getOperation();
			Attribute attribute = modification.getAttribute();
			RelationshipChecker rc = new RelationshipChecker(OWNER_ATTR_NAME,
					MEMBER_OF_ATTR_NAME, CAT_ATTR_NAME, CAT_VALUE_FOR_COM,
					OU_HEALTH_ORG, OU_HEALTH_PRO, this.opContext,
					this.opContextEntry, attribute);

			if (operation == ModificationOperation.ADD_ATTRIBUTE) {
				if (attribute.getUpId().equalsIgnoreCase(MEMBER_OF_ATTR_NAME)) {
					rc.checkModifyAddRequest();
				}
			} else if (operation == ModificationOperation.REMOVE_ATTRIBUTE) {
				int minNumberOfValues = 2;
				if(modification.getAttribute() != null) {
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
					rc.checkModifyReplace();
				}
			}
		}
		LOG.debug("<< ModifyEntryRelationshipOperation : successful");
		next(modifyOperationContext);
	}

	private static boolean isCommunity(Entry entry)
			throws LdapInvalidAttributeValueException {
		if (entry.get(CAT_ATTR_NAME) != null
				&& entry.get(CAT_ATTR_NAME).getString()
						.equalsIgnoreCase(CAT_VALUE_FOR_COM)) {
			return true;
		}
		return false;
	}

	private static boolean isEntryOfOU(final Entry entry, final String rdnName) {
		if (entry.getDn().getRdn(1).getName().equals(rdnName)) {
			return true;
		}
		return false;
	}
}
