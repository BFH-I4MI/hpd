
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für searchType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="searchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="caseSensitive" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="startsWith" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="traverseConceptsToRoot" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="wholeWords" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchType", propOrder = {
    "caseSensitive",
    "startsWith",
    "traverseConceptsToRoot",
    "wholeWords"
})
public class SearchType {

    protected Boolean caseSensitive;
    protected Boolean startsWith;
    protected Boolean traverseConceptsToRoot;
    protected Boolean wholeWords;

    /**
     * Ruft den Wert der caseSensitive-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Legt den Wert der caseSensitive-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCaseSensitive(Boolean value) {
        this.caseSensitive = value;
    }

    /**
     * Ruft den Wert der startsWith-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStartsWith() {
        return startsWith;
    }

    /**
     * Legt den Wert der startsWith-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStartsWith(Boolean value) {
        this.startsWith = value;
    }

    /**
     * Ruft den Wert der traverseConceptsToRoot-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTraverseConceptsToRoot() {
        return traverseConceptsToRoot;
    }

    /**
     * Legt den Wert der traverseConceptsToRoot-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTraverseConceptsToRoot(Boolean value) {
        this.traverseConceptsToRoot = value;
    }

    /**
     * Ruft den Wert der wholeWords-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWholeWords() {
        return wholeWords;
    }

    /**
     * Legt den Wert der wholeWords-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWholeWords(Boolean value) {
        this.wholeWords = value;
    }

}
