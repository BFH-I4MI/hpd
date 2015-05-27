
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für sysParam complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="sysParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainValueByModifyLevel" type="{de.fhdo.termserver.types}domainValue" minOccurs="0"/>
 *         &lt;element name="domainValueByValidityDomain" type="{de.fhdo.termserver.types}domainValue" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="javaDatatype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objectId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sysParam", propOrder = {
    "description",
    "domainValueByModifyLevel",
    "domainValueByValidityDomain",
    "id",
    "javaDatatype",
    "name",
    "objectId",
    "value"
})
public class SysParam {

    protected String description;
    protected DomainValue domainValueByModifyLevel;
    protected DomainValue domainValueByValidityDomain;
    protected Long id;
    protected String javaDatatype;
    protected String name;
    protected Long objectId;
    protected String value;

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der domainValueByModifyLevel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomainValue }
     *     
     */
    public DomainValue getDomainValueByModifyLevel() {
        return domainValueByModifyLevel;
    }

    /**
     * Legt den Wert der domainValueByModifyLevel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainValue }
     *     
     */
    public void setDomainValueByModifyLevel(DomainValue value) {
        this.domainValueByModifyLevel = value;
    }

    /**
     * Ruft den Wert der domainValueByValidityDomain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DomainValue }
     *     
     */
    public DomainValue getDomainValueByValidityDomain() {
        return domainValueByValidityDomain;
    }

    /**
     * Legt den Wert der domainValueByValidityDomain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainValue }
     *     
     */
    public void setDomainValueByValidityDomain(DomainValue value) {
        this.domainValueByValidityDomain = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der javaDatatype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaDatatype() {
        return javaDatatype;
    }

    /**
     * Legt den Wert der javaDatatype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaDatatype(String value) {
        this.javaDatatype = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der objectId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * Legt den Wert der objectId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setObjectId(Long value) {
        this.objectId = value;
    }

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Legt den Wert der value-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
