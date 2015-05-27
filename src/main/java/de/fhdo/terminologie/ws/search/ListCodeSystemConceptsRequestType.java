
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.CodeSystem;
import types.termserver.fhdo.de.CodeSystemEntity;


/**
 * <p>Java-Klasse für listCodeSystemConceptsRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="listCodeSystemConceptsRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loginToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystem" type="{de.fhdo.termserver.types}codeSystem"/>
 *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" minOccurs="0"/>
 *         &lt;element name="searchParameter" type="{http://search.ws.terminologie.fhdo.de/}searchType" minOccurs="0"/>
 *         &lt;element name="pagingParameter" type="{http://search.ws.terminologie.fhdo.de/}pagingType" minOccurs="0"/>
 *         &lt;element name="lookForward" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sortingParameter" type="{http://search.ws.terminologie.fhdo.de/}sortingType" minOccurs="0"/>
 *         &lt;element name="loadMetadata" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="loadTranslation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listCodeSystemConceptsRequestType", propOrder = {
    "loginToken",
    "codeSystem",
    "codeSystemEntity",
    "searchParameter",
    "pagingParameter",
    "lookForward",
    "sortingParameter",
    "loadMetadata",
    "loadTranslation"
})
public class ListCodeSystemConceptsRequestType {

    protected String loginToken;
    @XmlElement(required = true)
    protected CodeSystem codeSystem;
    protected CodeSystemEntity codeSystemEntity;
    protected SearchType searchParameter;
    protected PagingType pagingParameter;
    protected boolean lookForward;
    protected SortingType sortingParameter;
    protected Boolean loadMetadata;
    protected Boolean loadTranslation;

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
     * Ruft den Wert der codeSystem-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystem }
     *     
     */
    public CodeSystem getCodeSystem() {
        return codeSystem;
    }

    /**
     * Legt den Wert der codeSystem-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystem }
     *     
     */
    public void setCodeSystem(CodeSystem value) {
        this.codeSystem = value;
    }

    /**
     * Ruft den Wert der codeSystemEntity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntity }
     *     
     */
    public CodeSystemEntity getCodeSystemEntity() {
        return codeSystemEntity;
    }

    /**
     * Legt den Wert der codeSystemEntity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntity }
     *     
     */
    public void setCodeSystemEntity(CodeSystemEntity value) {
        this.codeSystemEntity = value;
    }

    /**
     * Ruft den Wert der searchParameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SearchType }
     *     
     */
    public SearchType getSearchParameter() {
        return searchParameter;
    }

    /**
     * Legt den Wert der searchParameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchType }
     *     
     */
    public void setSearchParameter(SearchType value) {
        this.searchParameter = value;
    }

    /**
     * Ruft den Wert der pagingParameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PagingType }
     *     
     */
    public PagingType getPagingParameter() {
        return pagingParameter;
    }

    /**
     * Legt den Wert der pagingParameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PagingType }
     *     
     */
    public void setPagingParameter(PagingType value) {
        this.pagingParameter = value;
    }

    /**
     * Ruft den Wert der lookForward-Eigenschaft ab.
     * 
     */
    public boolean isLookForward() {
        return lookForward;
    }

    /**
     * Legt den Wert der lookForward-Eigenschaft fest.
     * 
     */
    public void setLookForward(boolean value) {
        this.lookForward = value;
    }

    /**
     * Ruft den Wert der sortingParameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SortingType }
     *     
     */
    public SortingType getSortingParameter() {
        return sortingParameter;
    }

    /**
     * Legt den Wert der sortingParameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SortingType }
     *     
     */
    public void setSortingParameter(SortingType value) {
        this.sortingParameter = value;
    }

    /**
     * Ruft den Wert der loadMetadata-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoadMetadata() {
        return loadMetadata;
    }

    /**
     * Legt den Wert der loadMetadata-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoadMetadata(Boolean value) {
        this.loadMetadata = value;
    }

    /**
     * Ruft den Wert der loadTranslation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoadTranslation() {
        return loadTranslation;
    }

    /**
     * Legt den Wert der loadTranslation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoadTranslation(Boolean value) {
        this.loadTranslation = value;
    }

}
