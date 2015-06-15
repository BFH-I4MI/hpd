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
import de.fhdo.terminologie.ws.search.Search_Service;

public class AttributeValidator {
	
	private AttributeValidatorSettings attributeValidatorSettings;
	
	public AttributeValidator(AttributeValidatorSettings anAttributeValidatorSettings) {
		this.setAttributeValidatorSettings(anAttributeValidatorSettings);
	}
	
	public void setAttributeValidatorSettings(AttributeValidatorSettings anAttributeValidatorSettings) {
		this.attributeValidatorSettings = anAttributeValidatorSettings;
	}
	
	/**
	 * Returns the current version as long number for a code system.
	 * @param codeSystemName the name of the code system which current version should be looked up
	 * @return returns -1l if no matching code system was found on the terminology server otherwise
	 * 		   the current version as long value
	 * @throws SOAPException
	 */
	public long getCurrentCodeSystemVersion(String codeSystemName) throws SOAPException {
			// create webservice reference and port
			Search_Service service = new Search_Service();
			Search port = service.getSearchPort();

			// define parameter
			ListCodeSystemsRequestType request = new ListCodeSystemsRequestType();
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setName(codeSystemName);
			request.setCodeSystem(codeSystem);

			// invoke method
			ListCodeSystemsResponse.Return response = port
					.listCodeSystems(request);
			if (response.getReturnInfos().getStatus() == de.fhdo.terminologie.ws.search.Status.OK) {
				if(response.getReturnInfos().getCount() > 0) {
					return response.getCodeSystem().get(0).getCurrentVersionId();
				}
				return -1l;
			} else {
				throw new SOAPException(response.getReturnInfos().toString());
			}
	}

	public CodeSystemConcept currentConceptCodeFilter(String codeSystemName,
			String code) throws SOAPException {
		Search_Service service = new Search_Service();
		Search port = service.getSearchPort();

		// define parameter
		ListCodeSystemConceptsRequestType request = new ListCodeSystemConceptsRequestType();
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
	
	public boolean checkTerminology(String anAttrName, String anAttrValue) throws SOAPException {
		// The value 'community' can only be set by the user 'root'. The value is not in the terminology.
		if(anAttrName.equalsIgnoreCase("businessCategory") && anAttrValue.equalsIgnoreCase("community")) {
			return true;
		}
		
		String indexServerAttrName = attributeValidatorSettings.getTranslation(anAttrName);
		if(indexServerAttrName == null) {
			// If there is no translation, there is no code system for the attribute.
			// If true, no exception occurs if it's a invalid attribute name.
			return true;
		} else if(indexServerAttrName.equalsIgnoreCase("NoCodeSystem")) {
			// There is no code system for the attribute but its a valid attribute name.
			return true;
		} else if (this.currentConceptCodeFilter(
				indexServerAttrName, anAttrValue) == null) {
			// In the code system for anAttrName was no value equals anAttrValue
			return false;
		}
		// A concept was found for the value of anAttrValue in the code system anAttrName
		return true;
	}
}
