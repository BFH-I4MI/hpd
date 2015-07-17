package ch.bfh.i4mi.interceptor;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.entry.ClonedServerEntry;
import org.apache.directory.server.core.api.interceptor.BaseInterceptor;
import org.apache.directory.server.core.api.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.api.interceptor.context.DeleteOperationContext;
import org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor keeps the member attribute of relationships in
 * sync with the memberOf attribute from an HPI or HOI.
 * 
 * ATTENTION: This interceptor is not in sync with the webservice. Changes made to the LDAP
 * through interceptors are not published to the update queue.
 *
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
public class RelationshipCompletionInterceptor extends BaseInterceptor {

	/**
	 * Name of the attribute to check.
	 */
	private static final String ATTRIBUTE_NAME = "memberof";
	
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
			.getLogger(RelationshipCompletionInterceptor.class);

	/**
	 * Initialize the registers, normalizers.
	 * 
	 * @param aDirectoryService
	 *            the DirectoryService to initialize the parent.
	 * @throws LdapException thrown exception on error.
	 */
	public final void init(final DirectoryService aDirectoryService)
			throws LdapException {
		LOG.debug("Initialiazing the RelationshipCompletionInterceptor");
		super.init(aDirectoryService);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#add(org.apache.directory.server.core.api.interceptor.context.AddOperationContext)
	 */
	@Override
	public final void add(final AddOperationContext addOperationContext)
			throws LdapException {
		this.opContext = addOperationContext;
		this.opContextEntry = addOperationContext.getEntry();

		Attribute attribute = this.opContextEntry.get(ATTRIBUTE_NAME);
		if (attribute != null) {
			if (attribute.getUpId().equalsIgnoreCase(ATTRIBUTE_NAME)) {

				LOG.debug(">> AddEntryRelationshipOperation : AddContext for Dn '"
						+ this.opContextEntry.getDn() + "'\n" + attribute.toString());

				for (Value<?> value : attribute) {
					relationshipCompleter(this.opContext.getEntry().getDn(), new Dn(
							value.getString()),
							ModificationOperation.ADD_ATTRIBUTE);
				}
			}
		}
		LOG.debug("<< AddEntryRelationshipOperation : successful");
		next(addOperationContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#modify(org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext)
	 */
	@Override
	public final void modify(final ModifyOperationContext modifyOperationContext)
			throws LdapException {
		this.opContext = modifyOperationContext;
		List<Modification> items = modifyOperationContext.getModItems();

		for (Modification modification : items) {
			ModificationOperation operation = modification.getOperation();
			if (operation == ModificationOperation.ADD_ATTRIBUTE
					|| operation == ModificationOperation.REMOVE_ATTRIBUTE
					|| operation == ModificationOperation.REPLACE_ATTRIBUTE) {

				Attribute attribute = modification.getAttribute();
				
				LOG.debug(">> ModifyEntryRelationshipOperation : ModifyContext for Dn '"
						+ this.opContext.getEntry().getDn()
						+ "'\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
						+ " Attribute '" + attribute.toString() + "'");

				if (attribute.getUpId().equalsIgnoreCase(ATTRIBUTE_NAME)) {
					if (attribute.get() == null) {
						/*
						 * when the last or all memberOf values are deleted
						 * there could be no value in the modification. Because
						 * we need to remove the member attribute of the
						 * relationship(s) we need this value. For this we
						 * exchange the attribute from the modification with the
						 * one from the OperationContext.
						 */

						for (Attribute attr : this.opContext.getEntry()
								.getAttributes()) {
							if (attr.getUpId().equalsIgnoreCase(ATTRIBUTE_NAME)) {
								for (Value<?> value : attribute) {
									relationshipCompleter(this.opContext.getEntry()
											.getDn(),
											new Dn(value.getString()),
											operation);
								}
							}
						}
					} else {
						for (Value<?> value : attribute) {
							relationshipCompleter(this.opContext.getEntry().getDn(),
									new Dn(value.getString()), operation);
						}
					}
				}
			}
		}
		LOG.debug("<< ModifyEntryRelationshipOperation : successful");
		next(modifyOperationContext);
	}

	/* (non-Javadoc)
	 * @see org.apache.directory.server.core.api.interceptor.BaseInterceptor#delete(org.apache.directory.server.core.api.interceptor.context.DeleteOperationContext)
	 */
	@Override
	public final void delete(final DeleteOperationContext deleteOperationContext)
			throws LdapException {
		this.opContext = deleteOperationContext;
		this.opContextEntry = deleteOperationContext.getEntry();

		Attribute attribute = this.opContextEntry.get(ATTRIBUTE_NAME);
		if (attribute != null) {
			

			MessageFormat formatter = new MessageFormat(">> DeleteEntryRelationshipOperation : ModifyContext for Dn");
			formatter.setLocale(Locale.getDefault());
			
			LOG.debug(">> DeleteEntryRelationshipOperation : ModifyContext for Dn '" + this.opContext.getEntry().getDn() + "'");

			for (Value<?> value : attribute) {
				relationshipCompleter(this.opContextEntry.getDn(), new Dn(value.getString()),
						ModificationOperation.REMOVE_ATTRIBUTE);
			}
		}
		LOG.debug("<< DeleteEntryRelationshipOperation : successful");
		next(deleteOperationContext);
	}

	/**
	 * Creates the member attribute in cn=[EntryCn],ou=Relationship where the
	 * memberOf was set.
	 *
	 * @param memberDn
	 *            the dn of the entry where the memberOf value will be set
	 * @param relationshipEntryDn
	 *            the memberOf attribute of the entry.
	 * @param operation
	 *            ModificationOperation for the relationship.
	 * @throws LdapException
	 *             LdapException
	 */
	private void relationshipCompleter(final Dn memberDn,
			final Dn relationshipEntryDn, final ModificationOperation operation)
			throws LdapException {
		Entry relationshipEntry = ((ClonedServerEntry) this.opContext.getSession()
				.lookup(relationshipEntryDn)).getOriginalEntry();

		switch (operation) {
		case ADD_ATTRIBUTE:
			relationshipEntry.add("member", memberDn.toString());
			break;
		case REMOVE_ATTRIBUTE:
			relationshipEntry.remove("member", memberDn.toString());
			break;
		case REPLACE_ATTRIBUTE:
			throw new LdapException(
					"REPLACE operations on multivalued attribute 'memberOf' or "
							+ "'member'" + operation.toString());
		default:
			throw new LdapException("Default in switch(operation) used. This should never happen.");
		}
	}
}
