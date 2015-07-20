package ch.bfh.i4mi.validators;

import javax.xml.soap.SOAPException;

import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.CodeSystemConcept;
import types.termserver.fhdo.de.CodeSystemEntity;
import types.termserver.fhdo.de.CodeSystemEntityVersion;
import types.termserver.fhdo.de.CodeSystemVersion;
import types.termserver.fhdo.de.CodeSystemVersionEntityMembership;
import de.fhdo.terminologie.ws.search.ListCodeSystemConceptsRequestType;
import de.fhdo.terminologie.ws.search.ListCodeSystemConceptsResponse;
import de.fhdo.terminologie.ws.search.ListCodeSystemsRequestType;
import de.fhdo.terminologie.ws.search.ListCodeSystemsResponse;
import de.fhdo.terminologie.ws.search.Search;
import de.fhdo.terminologie.ws.search.SearchType;
import de.fhdo.terminologie.ws.search.Search_Service;

/**
 * The Class AttributeValidator validates if a transmitted code is valid or not.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
public class AttributeValidator {

	/** The attribute validator settings. */
	private AttributeValidatorSettings attributeValidatorSettings;

	/**
	 * Instantiates a new attribute validator.
	 *
	 * @param anAttributeValidatorSettings an attribute validator settings object
	 */
	public AttributeValidator(
			AttributeValidatorSettings anAttributeValidatorSettings) {
		this.setAttributeValidatorSettings(anAttributeValidatorSettings);
	}

	/**
	 * Sets the attribute validator settings.
	 *
	 * @param anAttributeValidatorSettings the new attribute validator settings
	 */
	public void setAttributeValidatorSettings(
			AttributeValidatorSettings anAttributeValidatorSettings) {
		this.attributeValidatorSettings = anAttributeValidatorSettings;
	}

	/**
	 * Returns the current version as long number for a code system.
	 *
	 * @param codeSystemName            the name of the code system which current version should be
	 *            looked up
	 * @return returns -1l if no matching code system was found on the
	 *         terminology server otherwise the current version as long value
	 * @throws SOAPException the SOAP exception
	 */
	public long getCurrentCodeSystemVersion(String codeSystemName)
			throws SOAPException {
		// create webservice reference and port
		Search_Service service = new Search_Service();
		Search port = service.getSearchPort();

		// define parameter
		ListCodeSystemsRequestType request = new ListCodeSystemsRequestType();
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setName(codeSystemName);
		request.setCodeSystem(codeSystem);

		// invoke method
		ListCodeSystemsResponse.Return response = port.listCodeSystems(request);
		if (response.getReturnInfos().getStatus() == de.fhdo.terminologie.ws.search.Status.OK) {
			if (response.getReturnInfos().getCount() > 0) {
				return response.getCodeSystem().get(0).getCurrentVersionId();
			}
			return -1l;
		} else {
			throw new SOAPException(response.getReturnInfos().toString());
		}
	}

	/**
	 * Current concept code filter.
	 *
	 * @param codeSystemName the code system name
	 * @param code the code
	 * @return the current code system concept
	 * @throws SOAPException the SOAP exception
	 */
	public CodeSystemConcept currentConceptCodeFilter(String codeSystemName,
			String code) throws SOAPException {
		Search_Service service = new Search_Service();
		Search port = service.getSearchPort();

		// define parameter
		ListCodeSystemConceptsRequestType request = new ListCodeSystemConceptsRequestType();

		SearchType searchType = new SearchType();
		searchType.setWholeWords(true);
		request.setSearchParameter(searchType);
		request.getSearchParameter().setWholeWords(true);

		CodeSystemVersion csvRequest = new CodeSystemVersion();
		csvRequest.setVersionId(getCurrentCodeSystemVersion(codeSystemName));

		// get code system with version id
		request.setCodeSystem(new types.termserver.fhdo.de.CodeSystem());
		request.getCodeSystem().getCodeSystemVersions().add(csvRequest);
		CodeSystemVersionEntityMembership csvem = new CodeSystemVersionEntityMembership();
		csvem.setIsMainClass(true);

		// retrieve only main classes (root concepts)
		CodeSystemEntity cse = new CodeSystemEntity();
		CodeSystemEntityVersion csev = new CodeSystemEntityVersion();
		CodeSystemConcept csc = new CodeSystemConcept();

		csc.setCode(code);
		csev.getCodeSystemConcepts().add(csc);
		cse.getCodeSystemEntityVersions().add(csev);

		request.setCodeSystemEntity(cse);
		request.getCodeSystemEntity().getCodeSystemVersionEntityMemberships()
				.add(csvem);

		// invoke method
		ListCodeSystemConceptsResponse.Return response = port
				.listCodeSystemConcepts(request);

		// handle response
		if (response.getReturnInfos().getStatus() == de.fhdo.terminologie.ws.search.Status.OK) {
			if (response.getReturnInfos().getCount() > 0) {
				for (CodeSystemEntity responseCse : response
						.getCodeSystemEntity()) {
					for (CodeSystemEntityVersion responseCsev : responseCse
							.getCodeSystemEntityVersions()) {
						
						if (responseCsev.getVersionId().compareTo(
								responseCse.getCurrentVersionId()) == 0) {

							return responseCsev.getCodeSystemConcepts().get(0);
						}
					}
				}
			}
			// When this point is reached there is no current concept for the
			// delivered code.
			return null;
		} else {
			throw new SOAPException(response.getReturnInfos().toString());
		}
	}

	/**
	 * Checks if an attribute value is part of the terminology for an attribute name.
	 *
	 * @param anAttrName an attribute name
	 * @param anAttrValue an attribute value
	 * @return true, if the attribute value is legal
	 * @throws SOAPException the SOAP exception
	 */
	public boolean checkTerminology(String anAttrName, String anAttrValue)
			throws SOAPException {
		boolean isLegalValue = true;

		// The value 'community' can only be set by the user 'root'. The value
		// is not in the terminology.
		if (!(anAttrName.equalsIgnoreCase("businessCategory") && anAttrValue
				.equalsIgnoreCase("community"))) {

			String indexServerAttrName = attributeValidatorSettings
					.getTranslation(anAttrName);
			if (indexServerAttrName == null) {
				// If there is no translation, there is no code system for the
				// attribute.
				// If true, no exception occurs if it's a invalid attribute
				// name.
				isLegalValue = true;
			} else if (indexServerAttrName.equalsIgnoreCase("NoCodeSystem")) {
				// There is no code system for the attribute but its a valid
				// attribute name.
				isLegalValue = true;
			} else {
				CodeSystemConcept resultCsc = this.currentConceptCodeFilter(
						indexServerAttrName, anAttrValue);

				if (resultCsc == null
						|| !resultCsc.getCode().equalsIgnoreCase(anAttrValue)) {
					// In the code system for anAttrName was no value exactly
					// equals anAttrValue
					isLegalValue = false;
				}
			}
		}
		// A concept was found for the value of anAttrValue in the code system
		// anAttrName
		return isLegalValue;
	}
}
