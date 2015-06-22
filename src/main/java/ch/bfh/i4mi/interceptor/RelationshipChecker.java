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

public class RelationshipChecker {
	/**
	 * Name of the attribute to check.
	 */
	private String ownerAttributeName;

	private String memberOfAttributeName;

	private String catAttrName;

	private String catValueForCommunity;

	private String ouHealthOrgRdn;

	private String ouHealthProRdn;

	private OperationContext opContext;
	private Entry opContextEntry;
	private Attribute attribute;

	private static final Logger LOG = LoggerFactory
			.getLogger(RelationshipInterceptor.class);

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

				int comCounter = 0;
				// HP
				for (Value<?> value : this.attribute) {
					Entry ownerOfRelationship = getOwnerOfRelationship(value);

					if (!isEntryOfOU(ownerOfRelationship, this.ouHealthOrgRdn)) {
						// Throw exception if the owner of the relationship
						// object is not
						// from the OU HcRegulatedOrganization
						throw new LdapException(
								"HPs can only be connected to organizations or a community.");
					} else if (isEntryOfOU(ownerOfRelationship,
							this.ouHealthOrgRdn)
							&& isCommunity(ownerOfRelationship)) {
						// Owner is a community not a HO and not the only value.
						// Count the communities to check if they are mixed up
						// with organizations.

						comCounter++;
					}
				}

				if (comCounter != 0
						|| comCounter != this.opContextEntry.get(
								this.memberOfAttributeName).size()) {
					throw new LdapException(
							"Community and organization relationships can not be mixed for HPs.");
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

	protected void checkModifyAddRequest() throws LdapException {
		LOG.debug("checkModifyAddRequest()");
		if (this.opContextEntry.get(this.memberOfAttributeName) == null
				|| this.opContextEntry.get(this.memberOfAttributeName).size() < 1) {
			LOG.debug("size() < 1");
			// No memberOf attributes
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
					if (!isCommunity(ownerOfNewRelationship)) {
						throw new LdapException(
								"Community relationships for HPs are only allowed when there are no "
										+ "relationships to HOs.");
					}
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
							"Community relationships for HPs are only allowed when there are no "
									+ "relationships to HOs.");
				}
			}
		} else {
			// if (this.opContextEntry.get(this.memberOfAttributeName).size() >
			// 1)
			// Is already a multiple
			LOG.debug("size() > 1");
			Entry ownerOfExistingRelationship = getOwnerOfRelationship(this.opContextEntry
					.get(this.memberOfAttributeName).get());
			
			for (Value<?> value : this.attribute) {
				LOG.debug("Value of owner: " + value.getString());
				Entry ownerOfRelationship = getOwnerOfRelationship(value);

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
					if(isCommunity(ownerOfExistingRelationship)) {
						if (!isCommunity(ownerOfRelationship)) {					
							throw new LdapException(
									"Community relationships for HPs is only allowed when there are no "
											+ "relationships to organizations.");
						}
					} else if(isEntryOfOU(ownerOfExistingRelationship, this.ouHealthOrgRdn)) {
						if (isCommunity(ownerOfRelationship)) {
							throw new LdapException(
									"Organization relationships for HPs is only allowed when there are no "
											+ "relationships to communities.");
						}
					}
				}
			}
		}

	}

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

	private static boolean isEntryOfOU(final Entry entry, final String rdnName) {
		if (entry.getDn().getRdn(1).getName().equals(rdnName)) {
			return true;
		}
		return false;
	}

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
	 * @param value
	 *            Value containing the dn of the relationship.
	 * @return the owner entry of the relationship.
	 * @throws LdapException
	 *             thrown on invalid value.
	 * @throws LdapInvalidDnException
	 *             thrown on invalid value.
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
