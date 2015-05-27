
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für sortingType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="sortingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sortBy" type="{http://search.ws.terminologie.fhdo.de/}sortByField" minOccurs="0"/>
 *         &lt;element name="sortDirection" type="{http://search.ws.terminologie.fhdo.de/}sortDirection" minOccurs="0"/>
 *         &lt;element name="sortType" type="{http://search.ws.terminologie.fhdo.de/}sortType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sortingType", propOrder = {
    "sortBy",
    "sortDirection",
    "sortType"
})
public class SortingType {

    @XmlSchemaType(name = "string")
    protected SortByField sortBy;
    @XmlSchemaType(name = "string")
    protected SortDirection sortDirection;
    @XmlSchemaType(name = "string")
    protected SortType sortType;

    /**
     * Ruft den Wert der sortBy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SortByField }
     *     
     */
    public SortByField getSortBy() {
        return sortBy;
    }

    /**
     * Legt den Wert der sortBy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SortByField }
     *     
     */
    public void setSortBy(SortByField value) {
        this.sortBy = value;
    }

    /**
     * Ruft den Wert der sortDirection-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SortDirection }
     *     
     */
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    /**
     * Legt den Wert der sortDirection-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SortDirection }
     *     
     */
    public void setSortDirection(SortDirection value) {
        this.sortDirection = value;
    }

    /**
     * Ruft den Wert der sortType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SortType }
     *     
     */
    public SortType getSortType() {
        return sortType;
    }

    /**
     * Legt den Wert der sortType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SortType }
     *     
     */
    public void setSortType(SortType value) {
        this.sortType = value;
    }

}
