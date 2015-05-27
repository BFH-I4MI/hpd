
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.ConceptValueSetMembership;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="returnInfos" type="{http://search.ws.terminologie.fhdo.de/}returnType" minOccurs="0"/>
 *         &lt;element name="conceptValueSetMembership" type="{de.fhdo.termserver.types}conceptValueSetMembership" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "returnInfos",
    "conceptValueSetMembership"
})
@XmlRootElement(name = "returnConceptValueSetMembershipResponseType")
public class ReturnConceptValueSetMembershipResponseType {

    protected ReturnType returnInfos;
    protected ConceptValueSetMembership conceptValueSetMembership;

    /**
     * Ruft den Wert der returnInfos-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReturnType }
     *     
     */
    public ReturnType getReturnInfos() {
        return returnInfos;
    }

    /**
     * Legt den Wert der returnInfos-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnType }
     *     
     */
    public void setReturnInfos(ReturnType value) {
        this.returnInfos = value;
    }

    /**
     * Ruft den Wert der conceptValueSetMembership-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ConceptValueSetMembership }
     *     
     */
    public ConceptValueSetMembership getConceptValueSetMembership() {
        return conceptValueSetMembership;
    }

    /**
     * Legt den Wert der conceptValueSetMembership-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ConceptValueSetMembership }
     *     
     */
    public void setConceptValueSetMembership(ConceptValueSetMembership value) {
        this.conceptValueSetMembership = value;
    }

}
