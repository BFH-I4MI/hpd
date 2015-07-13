package ch.bfh.i4mi.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.interceptor.BaseInterceptor;
import org.apache.directory.server.core.api.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.api.interceptor.context.ModifyOperationContext;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AttributeConditionsInterceptor checks if the special rules for the
 * HPD are fulfilled on an request.
 */
public class AttributeConditionsInterceptor extends BaseInterceptor {

	/** The org.slf4j.Logger for this class. */
	private Logger LOG = LoggerFactory
			.getLogger(AttributeConditionsInterceptor.class);

	/** The Constant OU_HEALTH_ORG contains the RDN for an organization. */
	private static final String OU_HEALTH_ORG = "ou=HCRegulatedOrganization";

	/** The Constant OU_HEALTH_PRO contains the RDN for a professional. */
	private static final String OU_HEALTH_PRO = "ou=HCProfessional";

	/** The Constant SINGLE contains the boolean value for a single attribute. */
	private static final Boolean SINGLE = true;

	/**
	 * The Constant MULTIPLE contains the boolean value for a multiple
	 * attribute.
	 */
	private static final Boolean MULTIPLE = false;

	/**
	 * The Constant OPTIONAL contains the boolean value for a optional
	 * attribute.
	 */
	private static final Boolean OPTIONAL = true;

	/**
	 * The Constant REQUIRED contains the boolean value for a required
	 * attribute.
	 */
	private static final Boolean REQUIRED = false;

	/**
	 * Contains the organizational attributes which are different from the IHE
	 * standard.
	 */
	private List<HPDAttributeSettings> orgAttrToCheck = new ArrayList<HPDAttributeSettings>();

	/**
	 * Contains the health professional attributes which are different from the
	 * IHE standard.
	 */
	private List<HPDAttributeSettings> proAttrToCheck = new ArrayList<HPDAttributeSettings>();

	/** The OperationContext. */
	private OperationContext operationContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.directory.server.core.api.interceptor.BaseInterceptor#init
	 * (org.apache.directory.server.core.api.DirectoryService)
	 */
	public final void init(final DirectoryService aDirectoryService)
			throws LdapException {
		LOG.debug("Initialiazing the AttributeConditionsInterceptor");

		try {
			// name, isSingle, isOptional, minOccurence, maxOccurence
			// RELATIONSHIP ATTRIBUTES ARE CHECKED FROM THE RELATIONSHIP
			// INTERCEPTOR

			// Organizations
			orgAttrToCheck.add(new HPDAttributeSettings("HcIdentifier", SINGLE,
					REQUIRED, 1, 1));
			orgAttrToCheck.add(new HPDAttributeSettings("uid", SINGLE,
					REQUIRED, 1, 1));
			orgAttrToCheck.add(new HPDAttributeSettings("o", MULTIPLE,
					REQUIRED, 1, Integer.MAX_VALUE));
			orgAttrToCheck.add(new HPDAttributeSettings("businessCategory",
					MULTIPLE, REQUIRED, 1, Integer.MAX_VALUE));
			orgAttrToCheck.add(new HPDAttributeSettings(
					"hpdProviderPracticeAddress", MULTIPLE, OPTIONAL, 0, 2));

			// Professionals
			proAttrToCheck.add(new HPDAttributeSettings("HcIdentifier", SINGLE,
					REQUIRED, 1, 1));
			proAttrToCheck.add(new HPDAttributeSettings(
					"hpdProviderPracticeAddress", SINGLE, OPTIONAL, 0, 1));
			proAttrToCheck.add(new HPDAttributeSettings("givenName", MULTIPLE,
					REQUIRED, 1, Integer.MAX_VALUE));
			proAttrToCheck.add(new HPDAttributeSettings("sn", SINGLE, REQUIRED,
					1, 1));
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
		LOG.debug("AttributeConditionsInterceptor - End init()");
		super.init(aDirectoryService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.directory.server.core.api.interceptor.BaseInterceptor#add(
	 * org.apache
	 * .directory.server.core.api.interceptor.context.AddOperationContext)
	 */
	@Override
	public final void add(final AddOperationContext addOperationContext)
			throws LdapException {
		this.operationContext = addOperationContext;
		final Entry entry = operationContext.getEntry();

		LOG.debug(">> OptionalRequiredOperation : AddContext for Dn '"
				+ entry.getDn() + "'");

		if (isEntryOfOU(entry, OU_HEALTH_ORG)) {
			checkAddOp(orgAttrToCheck);
		} else if (isEntryOfOU(entry, OU_HEALTH_PRO)) {
			checkAddOp(proAttrToCheck);
		}

		LOG.debug("<< OptionalRequiredOperation : successful");
		next(addOperationContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.directory.server.core.api.interceptor.BaseInterceptor#modify
	 * (org
	 * .apache.directory.server.core.api.interceptor.context.ModifyOperationContext
	 * )
	 */
	@Override
	public final void modify(final ModifyOperationContext modifyOperationContext)
			throws LdapException {
		final List<Modification> items = modifyOperationContext.getModItems();
		this.operationContext = modifyOperationContext;
		final Entry entry = operationContext.getEntry();

		LOG.debug(">> OptionalRequiredOperation : ModifyContext for Dn '"
				+ entry.getDn() + "'");

		for (Modification modification : items) {
			if (isEntryOfOU(entry, OU_HEALTH_ORG)) {
				checkModOp(modification, orgAttrToCheck);
			} else if (isEntryOfOU(entry, OU_HEALTH_PRO)) {
				checkModOp(modification, proAttrToCheck);
			}
		}
		LOG.debug("<< OptionalRequiredOperation : successful");
		next(modifyOperationContext);
	}

	/**
	 * Checks if an ADD-Operation is valid.
	 *
	 * @param attributesToCheck
	 *            the attributes to check
	 * @throws LdapException
	 *             the ldap exception thrown on unexpected errors.
	 */
	private void checkAddOp(final List<HPDAttributeSettings> attributesToCheck)
			throws LdapException {
		final Entry entry = operationContext.getEntry();
		for (final HPDAttributeSettings attributeToCheck : attributesToCheck) {

			Attribute attribute = entry
					.get(attributeToCheck.getAttributeName());

			// Check if the attribute is required and present otherwise throw
			// exception.
			if (!attributeToCheck.isOptional() && attribute == null) {
				throw new LdapException("Required attribute '"
						+ attributeToCheck.getAttributeName() + "' is null.");
			} else if (attributeToCheck.isSingleValue()) {
				if (attribute != null
						&& entry.get(attributeToCheck.getAttributeName())
								.size() != 1) {
					// If attribute is null it must be optional.
					throw new LdapException("Single attribute '"
							+ attributeToCheck.getAttributeName()
							+ "' occures more than once.");
				}
			} else {
				// It's a multiple value attribute.

				// Check if it fulfills the minimal number requirement.
				if (entry.get(attributeToCheck.getAttributeName()).size() < attributeToCheck
						.getNumberOfMinOccurence()) {

					throw new LdapException(
							"Attribute '"
									+ attributeToCheck.getAttributeName()
									+ "' doesn't fulfill it's minimal number requirement of "
									+ attributeToCheck
											.getNumberOfMinOccurence() + ".");
					// Or the maximal number requirement.
				} else if (entry.get(attributeToCheck.getAttributeName())
						.size() > attributeToCheck.getNumberOfMaxOccurence()) {

					throw new LdapException(
							"Attribute '"
									+ attributeToCheck.getAttributeName()
									+ "' doesn't fulfill it's maximal number requirement of "
									+ attributeToCheck
											.getNumberOfMinOccurence() + ".");

				}
			}
		}
	}

	/**
	 * Check a Modification is valid.
	 *
	 * @param modification
	 *            a Modification from a ModificationOperationContext
	 * @param attributesToCheck
	 *            the attributes to check
	 * @throws LdapException
	 *             the ldap exception thrown on unexpected errors.
	 */
	private void checkModOp(final Modification modification,
			final List<HPDAttributeSettings> attributesToCheck)
			throws LdapException {
		Attribute attribute = modification.getAttribute();
		for (HPDAttributeSettings attributeToCheck : attributesToCheck) {
			if (attribute.getUpId().equalsIgnoreCase(
					attributeToCheck.getAttributeName())) {
				switch (modification.getOperation()) {
				case ADD_ATTRIBUTE:
					checkAddMod(attribute, attributeToCheck);
					break;
				case REPLACE_ATTRIBUTE:
					checkReplaceMod(attribute, attributeToCheck);
					break;
				case REMOVE_ATTRIBUTE:
					checkRemoveMod(attribute, attributeToCheck);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Check if a REMOVE-Modification is valid.
	 *
	 * @param attribute
	 *            the attribute to remove
	 * @param attrProperties
	 *            the special attribute properties for this attribute
	 * @throws LdapException
	 *             the ldap exception thrown on unexpected errors.
	 */
	private void checkRemoveMod(final Attribute attribute,
			final HPDAttributeSettings attrProperties) throws LdapException {
		if (!attrProperties.isOptional()) {
			final Entry entry = this.operationContext.getEntry();
			// Required, if size equals 1 throw exception
			if (entry.get(attribute.getUpId()).size() == attrProperties
					.getNumberOfMinOccurence()) {
				throw new LdapException(
						"ModifyRemove operation on an attribute which has already "
								+ "reached it's minimum number.");
			}
		}
	}

	/**
	 * Check if a REPLACE-Modification is valid.
	 *
	 * @param attribute
	 *            the attribute to replace
	 * @param attrProperties
	 *            the special attribute properties for this attribute
	 * @throws LdapException
	 *             the ldap exception thrown on unexpected errors.
	 */
	private void checkReplaceMod(final Attribute attribute,
			final HPDAttributeSettings attrProperties) throws LdapException {
		final Entry entry = this.operationContext.getEntry();
		if (entry.get(attribute.getUpId()).size() != 1) {
			throw new LdapException(
					"ModifyReplace operation on an attribute which doesn't exist "
							+ "or has multiple attribute values.");
		}
	}

	/**
	 * Check if an ADD-Modification is valid.
	 *
	 * @param attribute
	 *            the attribute to add
	 * @param attrProperties
	 *            the special properties for this attribute
	 * @throws LdapException
	 *             the ldap exception thrown on unexpected errors.
	 */
	private void checkAddMod(final Attribute attribute,
			final HPDAttributeSettings attrProperties) throws LdapException {
		// Optional Single kein Attibute -> OK
		// Optional Multi kein Attribute -> OK
		// Optional Single mit Attribute -> FALSE
		// Optional Multi mit Attribute -> anz. Attribute < max -> OK ansonsten
		// FALSE
		// Required Single kein Attribute -> sollte es nicht geben
		// Required Multi kein Attribute -> sollte es nicht geben
		// Required Single mit Attribute -> Fehler REPLACE verwenden
		// Required Multi mit Attributen -> anz. Attribute < max -> OK ansonsten
		// FALSE
		final Entry entry = this.operationContext.getEntry();
		if (attrProperties.isSingleValue()) {
			if (entry.get(attribute.getUpId()) != null) {
				// There is already one attribute of this type, throw exception
				// because it's single value
				throw new LdapException(
						"ModifyAdd Ooperation on an existing single "
								+ "attribute value.");
			}
		} else {
			// Multiple
			if (entry.get(attribute.getUpId()).size() == attrProperties
					.getNumberOfMaxOccurence()) {
				throw new LdapException(
						"ModifyAdd operation on an attribute which has reached it's "
								+ "maximal occurence.");
			}
		}
	}

	/**
	 * Checks if an entry is part of a OU.
	 *
	 * @param entry
	 *            the entry
	 * @param rdnName
	 *            the rdn name of the OU
	 * @return true, if entry is part of the OU
	 */
	private static boolean isEntryOfOU(final Entry entry, final String rdnName) {
		Boolean isEntryOfOU = false;
		if (entry.getDn().getRdn(1).getName().equals(rdnName)) {
			isEntryOfOU = true;
		}
		return isEntryOfOU;
	}

}
