package ch.bfh.i4mi.interceptor;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.core.api.entry.ClonedServerEntry;
import org.apache.directory.server.core.api.interceptor.context.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class RelationshipChecker checks if a change on the relationships is valid.
 */
public class RelationshipChecker {
	/**
	 * Name of the attribute to check.
	 */
	private String ownerAttributeName;

	/** The member of attribute name. */
	private String memberOfAttributeName;

	/** The cat attr name. */
	private String catAttrName;

	/** The cat value for community. */
	private String catValueForCommunity;

	/** The ou health org rdn. */
	private String ouHealthOrgRdn;

	/** The ou health pro rdn. */
	private String ouHealthProRdn;

	/** The op context. */
	private OperationContext opContext;
	
	/** The op context entry. */
	private Entry opContextEntry;
	
	/** The attribute. */
	private Attribute attribute;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(RelationshipInterceptor.class);

	/**
	 * Instantiates a new relationship checker.
	 *
	 * @param anOwnerAttributeName the an owner attribute name
	 * @param aMemberOfAttributeName the a member of attribute name
	 * @param aCatAttrName the a cat attr name
	 * @param aCatValueForCommunity the a cat value for community
	 * @param anOuHealthOrgRdn the an ou health org rdn
	 * @param ouHeathProRdn the ou heath pro rdn
	 * @param anOperationContext the an operation context
	 * @param anOpContextEntry the an op context entry
	 * @param anAttribute the an attribute
	 * @throws LdapException the ldap exception
	 */
	RelationshipChecker(String anOwnerAttributeName,
			String aMemberOfAttributeName, String aCatAttrName,
			String aCatValueForCommunity, String anOuHealthOrgRdn,
			String ouHeathProRdn, OperationContext anOperationContext,
			Entry anOpContextEntry, Attribute anAttribute) throws LdapException {

			this.ownerAttributeName = anOwnerAttributeName;
			this.memberOfAttributeName = aMemberOfAttributeName;
			this.catAttrName = aCatAttrName;
			this.catValueForCommunity = aCatValueForCommunity;
			this.ouHealthOrgRdn = anOuHealthOrgRdn;
			this.ouHealthProRdn = ouHeathProRdn;
			this.opContext = anOperationContext;
			this.opContextEntry = anOpContextEntry;
			this.attribute = anAttribute;
	}

	/**
	 * Check add request.
	 *
	 * @throws LdapException the ldap exception
	 */
	protected void checkAddRequest() throws LdapException {
		if (this.opContextEntry.get(this.memberOfAttributeName).size() > 1) {
			// Multiple memberOf values

			if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
				// HO and multiple memberOf so it must be a Root-HO
				for (Value<?> value : this.attribute) {
					Entry ownerOfRelationship = getOwnerOfRelationship(value);

					if (!isCommunity(ownerOfRelationship)) {
						throw new LdapException(
								"Root-HO must not contain any non community relationships. "
										+ "Sub-HOs are single value.");
					}
				}

			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
				// HP
				for (Value<?> value : this.attribute) {
					Entry ownerOfRelationship = getOwnerOfRelationship(value);

					if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
						throw new LdapException(
								"HPs can only be connected to organizations or a community.");
					} else if (isEntryOfOU(ownerOfRelationship,
							this.ouHealthOrgRdn)
							&& isCommunity(ownerOfRelationship)) {
						// Owner is a community not a HO and not the only value.
						throw new LdapException(
								"Community relationship for HPs is only allowed when there are no "
										+ "other relationships.");
					}
				}
			} else {
				throw new LdapException(
						"Unexpected entry with multiple memberOf values.");
			}
		} else {
			// Single memberOf value
			Entry ownerOfRelationship = getOwnerOfRelationship(this.attribute
					.get());
			if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
				if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
					// Entry is connected neither to a HO nor a community.
					throw new LdapException(
							"HOs must be connected with a HO or communities.");
				}

			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
				if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
					// Entry is connected neither to a HO nor a community.
					throw new LdapException(
							"HOs must be connected with a HO or communities.");
				}
			} else {
				throw new LdapException(
						"Unexpected entry with a single memberOf values.");
			}
		}
	}

	/**
	 * Check modify add request.
	 *
	 * @throws LdapException the ldap exception
	 */
	protected void checkModifyAddRequest() throws LdapException {
		LOG.debug("checkModifyAddRequest()");
		if (this.opContextEntry.get(this.memberOfAttributeName) == null
				|| this.opContextEntry.get(this.memberOfAttributeName).size() < 1) {
			LOG.debug("size() < 1");
			// Single memberOf value
			Entry ownerOfRelationship = getOwnerOfRelationship(this.attribute
					.get());
			if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
				if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
					// Entry is connected neither to a HO nor a
					// community.
					throw new LdapException(
							"HOs must be connected with a HO or communities.");
				}
			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
				if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
					// Entry is connected neither to a HO nor a
					// community.
					throw new LdapException(
							"HOs must be connected with a HO or communities.");
				}
			} else {
				throw new LdapException(
						"Unexpected entry with a single memberOf values.");
			}
		} else if (this.opContextEntry.get(this.memberOfAttributeName).size() == 1) {
			LOG.debug("size() == 1");
			// 1. Check type of the opContextEntry
			Entry ownerOfExistingRelationship = getOwnerOfRelationship(this.opContextEntry
					.get(this.memberOfAttributeName).get());

			LOG.debug("Owner of current R: "
					+ ownerOfExistingRelationship.toString());
			if (isCommunity(ownerOfExistingRelationship)) {
				LOG.debug("Owner is Community");

				// relationship with a community
				Entry ownerOfNewRelationship = getOwnerOfRelationship(this.attribute
						.get());
				if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
					if (!isCommunity(ownerOfNewRelationship)) {
						throw new LdapException(
								"Root-HO must not contain any non community relationships.");
					}
				} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
					throw new LdapException(
							"Community relationship for HPs is only allowed when there are no "
									+ "other relationships.");
				}
			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
				LOG.debug("isEntryofOU");
				// relationship is a HO
				throw new LdapException(
						"Sub-HO can only have one relationship to another Sub-HO or Root-HO");
			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
				if (isCommunity(getOwnerOfRelationship(this.attribute.get()))) {
					// The old relationship is to an HO, the new is to a
					// community
					throw new LdapException(
							"Community relationship for HPs is only allowed when there are no "
									+ "other relationships.");
				}
			}
		} else {
			// if (this.opContextEntry.get(this.memberOfAttributeName).size() >
			// 1)
			// Is already a multiple
			LOG.debug("size() > 1");
			Entry ownerOfRelationship = getOwnerOfRelationship(this.attribute
					.get());

			if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
				// Is a Root-HO
				LOG.debug("Is a Root-HO");
				if (!isCommunity(ownerOfRelationship)) {
					throw new LdapException(
							"Root-HO must not contain any non community relationships.");
				}

			} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
				// Is a HPProfessional
				LOG.debug("Is a HPProfessional");
				if (isCommunity(ownerOfRelationship)) {
					throw new LdapException(
							"Community relationship for HPs is only allowed when there are no "
									+ "other relationships.");
				}
			}
		}

	}

	/**
	 * Check modify replace.
	 *
	 * @throws LdapException the ldap exception
	 */
	protected void checkModifyReplace() throws LdapException {
		if (this.opContextEntry.get(this.memberOfAttributeName).size() != 1) {
			throw new LdapException(
					"RelationshipInterceptor.ReplaceWrongNoOfAttr");
		}
		// Single memberOf value
		Entry ownerOfRelationship = getOwnerOfRelationship(this.attribute.get());
		if (isEntryOfOU(this.opContextEntry, this.ouHealthOrgRdn)) {
			if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
				// Entry is connected neither to a HO nor a
				// community.
				throw new LdapException(
						"HOs must be connected with a HO or communities.");
			}

		} else if (isEntryOfOU(this.opContextEntry, this.ouHealthProRdn)) {
			if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
				// Entry is connected neither to a HO nor a
				// community.
				throw new LdapException(
						"HOs must be connected with a HO or communities.");
			}
		} else {
			throw new LdapException(
					"Unexpected entry with a single memberOf values.");
		}
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

	/**
	 * Checks if is community.
	 *
	 * @param entry the entry
	 * @return true, if is community
	 * @throws LdapInvalidAttributeValueException the ldap invalid attribute value exception
	 */
	private boolean isCommunity(Entry entry)
			throws LdapInvalidAttributeValueException {
		if (entry.get(this.catAttrName) != null
				&& entry.get(this.catAttrName).getString()
						.equalsIgnoreCase(this.catValueForCommunity)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the owner of a relationship based on the relationship dn.
	 *
	 * @param value            Value containing the dn of the relationship.
	 * @return the owner entry of the relationship.
	 * @throws LdapInvalidDnException             thrown on invalid value.
	 * @throws LdapException             thrown on invalid value.
	 */

	private Entry getOwnerOfRelationship(final Value<?> value)
			throws LdapInvalidDnException, LdapException {
		Entry relationshipEntry = ((ClonedServerEntry) this.opContext
				.getSession().lookup(new Dn(value.getString())))
				.getOriginalEntry();

		Entry ownerOfRelationship = ((ClonedServerEntry) this.opContext
				.getSession().lookup(
						new Dn(relationshipEntry.get(this.ownerAttributeName)
								.getString()))).getOriginalEntry();

		return ownerOfRelationship;

	}

}
