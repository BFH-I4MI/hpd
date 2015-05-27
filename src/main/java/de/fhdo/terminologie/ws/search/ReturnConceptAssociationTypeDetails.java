
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ReturnConceptAssociationTypeDetails complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReturnConceptAssociationTypeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{http://search.ws.terminologie.fhdo.de/}returnConceptAssociationTypeDetailsRequestType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnConceptAssociationTypeDetails", propOrder = {
    "parameter"
})
public class ReturnConceptAssociationTypeDetails {

    protected ReturnConceptAssociationTypeDetailsRequestType parameter;

    /**
     * Ruft den Wert der parameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReturnConceptAssociationTypeDetailsRequestType }
     *     
     */
    public ReturnConceptAssociationTypeDetailsRequestType getParameter() {
        return parameter;
    }

    /**
     * Legt den Wert der parameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnConceptAssociationTypeDetailsRequestType }
     *     
     */
    public void setParameter(ReturnConceptAssociationTypeDetailsRequestType value) {
        this.parameter = value;
    }

}
