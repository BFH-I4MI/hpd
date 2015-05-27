
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.CodeSystemConcept;


/**
 * <p>Java-Klasse für listGloballySearchedConceptsRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="listGloballySearchedConceptsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystemConcept" type="{de.fhdo.termserver.types}codeSystemConcept" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listGloballySearchedConceptsRequestType", propOrder = {
    "loginToken",
    "codeSystemConcept"
})
public class ListGloballySearchedConceptsRequestType {

    protected String loginToken;
    protected CodeSystemConcept codeSystemConcept;

    /**
     * Ruft den Wert der loginToken-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * Legt den Wert der loginToken-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginToken(String value) {
        this.loginToken = value;
    }

    /**
     * Ruft den Wert der codeSystemConcept-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemConcept }
     *     
     */
    public CodeSystemConcept getCodeSystemConcept() {
        return codeSystemConcept;
    }

    /**
     * Legt den Wert der codeSystemConcept-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemConcept }
     *     
     */
    public void setCodeSystemConcept(CodeSystemConcept value) {
        this.codeSystemConcept = value;
    }

}
