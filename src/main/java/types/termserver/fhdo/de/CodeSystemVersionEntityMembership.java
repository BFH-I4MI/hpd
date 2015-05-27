
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für codeSystemVersionEntityMembership complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="codeSystemVersionEntityMembership">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" minOccurs="0"/>
 *         &lt;element name="codeSystemVersion" type="{de.fhdo.termserver.types}codeSystemVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{de.fhdo.termserver.types}codeSystemVersionEntityMembershipId" minOccurs="0"/>
 *         &lt;element name="isAxis" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isMainClass" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="orderNr" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemVersionEntityMembership", propOrder = {
    "codeSystemEntity",
    "codeSystemVersion",
    "id",
    "isAxis",
    "isMainClass",
    "orderNr"
})
public class CodeSystemVersionEntityMembership {

    protected CodeSystemEntity codeSystemEntity;
    protected CodeSystemVersion codeSystemVersion;
    protected CodeSystemVersionEntityMembershipId id;
    protected Boolean isAxis;
    protected Boolean isMainClass;
    protected Long orderNr;

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
     * Ruft den Wert der codeSystemVersion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemVersion }
     *     
     */
    public CodeSystemVersion getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * Legt den Wert der codeSystemVersion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemVersion }
     *     
     */
    public void setCodeSystemVersion(CodeSystemVersion value) {
        this.codeSystemVersion = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemVersionEntityMembershipId }
     *     
     */
    public CodeSystemVersionEntityMembershipId getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemVersionEntityMembershipId }
     *     
     */
    public void setId(CodeSystemVersionEntityMembershipId value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der isAxis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAxis() {
        return isAxis;
    }

    /**
     * Legt den Wert der isAxis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAxis(Boolean value) {
        this.isAxis = value;
    }

    /**
     * Ruft den Wert der isMainClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMainClass() {
        return isMainClass;
    }

    /**
     * Legt den Wert der isMainClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMainClass(Boolean value) {
        this.isMainClass = value;
    }

    /**
     * Ruft den Wert der orderNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOrderNr() {
        return orderNr;
    }

    /**
     * Legt den Wert der orderNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOrderNr(Long value) {
        this.orderNr = value;
    }

}
