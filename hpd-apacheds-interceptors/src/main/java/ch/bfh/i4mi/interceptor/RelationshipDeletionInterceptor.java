package ch.bfh.i4mi.interceptor;

import java.util.List;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.interceptor.BaseInterceptor;
import org.apache.directory.server.core.api.interceptor.context.DeleteOperationContext;
import org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor checks if a group contains any member attributes. If there
 * are any member attributes an exception is thrown if someone tries to delete
 * the entry.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule
 *
 */
public class RelationshipDeletionInterceptor extends BaseInterceptor {
	/**
	 * Name of the organization unit with the entries to check.
	 */
	private static final String OU_NAME = "ou=Relationship";

	/**
	 * Name of the attribute to check.
	 */
	private static final String ATTRIBUTE_NAME = "hpdProviderStatus";

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
			.getLogger(RelationshipDeletionInterceptor.class);

	/**
	 * Initialize the registers, normalizers.
	 * 
	 * @param aDirectoryService
	 *            the DirectoryService to initialize the parent.
	 * @throws LdapException exception thrown on error.
	 */
	public final void init(final DirectoryService aDirectoryService)
			throws LdapException {
		LOG.debug("Initialiazing the RelationshipDeletionInterceptor");
		super.init(aDirectoryService);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#delete(org.apache.directory.server.core.api.interceptor.context.DeleteOperationContext)
	 */
	@Override
	public final void delete(final DeleteOperationContext deleteOperationContext)
			throws LdapException {
		this.opContext = deleteOperationContext;
		this.opContextEntry = deleteOperationContext.getEntry();
		LOG.debug(">> RelationshipDeletionInterceptor : DeleteOperationContext '"
				+ this.opContext.getDn());

		if (this.opContextEntry.getDn().getRdn(1).getName().equals(OU_NAME)) {		
			if (this.opContextEntry.get("member").size() != 1
					&& this.opContextEntry
							.get("member")
							.getString()
							.equals(this.opContextEntry.get("owner")
									.getString())) {
				throw new LdapException(
						"To delete a relationship object all members except the owner must be removed first.");
			}

		}
		next(deleteOperationContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#modify(org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext)
	 */
	@Override
	public final void modify(final ModifyOperationContext modifyOperationContext)
			throws LdapException {
		this.opContext = modifyOperationContext;
		List<Modification> items = modifyOperationContext.getModItems();
		LOG.debug(">> RelationshipDeletionInterceptor : DeleteOperationContext '"
				+ this.opContext.getDn());
		for (Modification modification : items) {
			ModificationOperation operation = modification.getOperation();
			if (operation == ModificationOperation.ADD_ATTRIBUTE
					|| operation == ModificationOperation.REPLACE_ATTRIBUTE) {

				Attribute attribute = modification.getAttribute();

				if (attribute.getUpId().equalsIgnoreCase(ATTRIBUTE_NAME)
						&& attribute.getString().equalsIgnoreCase("inactive")
						&& this.opContextEntry.get("member") != null) {
					throw new LdapException(
							"Relationship entry must not contain any members.");
				}
			}
		}
		LOG.debug("<< RelationshipDeletionInterceptor : successful");
		next(modifyOperationContext);
	}
}
