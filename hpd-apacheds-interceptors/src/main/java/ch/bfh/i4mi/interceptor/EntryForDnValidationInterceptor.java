package ch.bfh.i4mi.interceptor;

import java.util.List;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.interceptor.BaseInterceptor;
import org.apache.directory.server.core.api.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.api.interceptor.context.HasEntryOperationContext;
import org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class checks if the value of an attribute is a valid dn or not.
 * In case of an non-valid dn an exception is thrown.
 *
 * @author Kevin Tippenhauer
 */
public class EntryForDnValidationInterceptor extends BaseInterceptor {

    /** The names of the attributes to check for valid dn. */
    private String[] arrAttributeNamesToCheck = {"memberOf", "member", "owner"};

    /** The OperationContext. */
    private OperationContext operationContext;

    /** logger used by this class. */
    private static final Logger LOG = LoggerFactory
            .getLogger(EntryForDnValidationInterceptor.class);

	/**
     * Initialize the registries, normalizers.
     * @param aDirectoryService The DirectoryService for this interceptor
     * @throws LdapException thrown if an exception happens
     */
    public final void init(final DirectoryService aDirectoryService) throws LdapException {
        LOG.debug("Initialiazing the EntryForDnValidationInterceptor");
        super.init(aDirectoryService);
    }

    /**
     * Sets the array with the attributes to check for valid dn.
     * @param anArrWithAttributeNamesToCheck the attributes to check for valid dn
     */
    public final void setAttributeNamesToCheck(final String[] anArrWithAttributeNamesToCheck) {
        this.arrAttributeNamesToCheck = anArrWithAttributeNamesToCheck;
    }

    @Override
    public final void add(final AddOperationContext addOpContext) throws LdapException {
        this.operationContext = addOpContext;
        final Entry entry = addOpContext.getEntry();
        LOG.debug(">> DnValidationOperation : AddContext for Dn '"
                + this.operationContext.getEntry().getDn() + "'");

        for (final String attributeNameToCheck : this.arrAttributeNamesToCheck) {
            final Attribute attribute = entry.get(attributeNameToCheck);
            if (attribute != null) {
                checkAttributeValue(attribute);
            }
        }
        LOG.debug("<< DnValidationOperation : successful");
        next(addOpContext);
    }

    @Override
    public final void modify(final ModifyOperationContext modifyOperationContext)
            throws LdapException {
        this.operationContext = modifyOperationContext;

        LOG.debug(">> DnValidationOperation : ModifyContext for Dn '"
                + this.operationContext.getEntry().getDn() + "'");

        final List<Modification> items = modifyOperationContext.getModItems();

        for (final String attributeNameToCheck : this.arrAttributeNamesToCheck) {
            for (final Modification modification : items) {
                final ModificationOperation operation = modification.getOperation();
                if (operation == ModificationOperation.ADD_ATTRIBUTE
                        || operation == ModificationOperation.REPLACE_ATTRIBUTE) {
                    final Attribute attribute = modification.getAttribute();
                    if (attribute.getUpId().equalsIgnoreCase(
                            attributeNameToCheck)) {
                        checkAttributeValue(attribute);
                    }
                }
            }
        }
        LOG.debug("<< DnValidationOperation : successful");
        next(modifyOperationContext);
    }

    /**
     * Checks if the value of the given attribute is a existing entry in the LDAP.
     * @param attribute the attribute to check
     * @throws LdapException thrown if an exception happens
     */
    protected final void checkAttributeValue(final Attribute attribute) throws LdapException {
        final Dn dn = new Dn(attribute.getString());
        
//        LOG.debug("test: " + attribute.getString());
        
        // check if the entry already exists
        if (dn.isEmpty() || !hasEntry(new HasEntryOperationContext(
                this.operationContext.getSession(), dn))) {
            throw new LdapException("\n\n\nEntry: " + dn.toString()
                    + " could not be found!\n\n\n");
        }
    }

    /**
     * Checks if the entry from the HasEntryOperationContext exists.
     *
     * @param hasEntryContext the context for the entry validation
     * @return true, if the entry exists
     * @throws LdapException thrown if an exception happens
     */
    public final boolean hasEntry(final HasEntryOperationContext hasEntryContext)
            throws LdapException {
        hasEntryContext.getDn().apply(this.schemaManager);
        return next(hasEntryContext);
    }
}
