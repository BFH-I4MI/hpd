
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für codeSystemEntityVersionAssociation complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="codeSystemEntityVersionAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="associationKind" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="associationType" type="{de.fhdo.termserver.types}associationType" minOccurs="0"/>
 *         &lt;element name="codeSystemEntityVersionByCodeSystemEntityVersionId1" type="{de.fhdo.termserver.types}codeSystemEntityVersion" minOccurs="0"/>
 *         &lt;element name="codeSystemEntityVersionByCodeSystemEntityVersionId2" type="{de.fhdo.termserver.types}codeSystemEntityVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="insertTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="leftId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="statusDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemEntityVersionAssociation", propOrder = {
    "associationKind",
    "associationType",
    "codeSystemEntityVersionByCodeSystemEntityVersionId1",
    "codeSystemEntityVersionByCodeSystemEntityVersionId2",
    "id",
    "insertTimestamp",
    "leftId",
    "status",
    "statusDate"
})
public class CodeSystemEntityVersionAssociation {

    protected Integer associationKind;
    protected AssociationType associationType;
    protected CodeSystemEntityVersion codeSystemEntityVersionByCodeSystemEntityVersionId1;
    protected CodeSystemEntityVersion codeSystemEntityVersionByCodeSystemEntityVersionId2;
    protected Long id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar insertTimestamp;
    protected Long leftId;
    protected Integer status;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusDate;

    /**
     * Ruft den Wert der associationKind-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssociationKind() {
        return associationKind;
    }

    /**
     * Legt den Wert der associationKind-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssociationKind(Integer value) {
        this.associationKind = value;
    }

    /**
     * Ruft den Wert der associationType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AssociationType }
     *     
     */
    public AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * Legt den Wert der associationType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AssociationType }
     *     
     */
    public void setAssociationType(AssociationType value) {
        this.associationType = value;
    }

    /**
     * Ruft den Wert der codeSystemEntityVersionByCodeSystemEntityVersionId1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public CodeSystemEntityVersion getCodeSystemEntityVersionByCodeSystemEntityVersionId1() {
        return codeSystemEntityVersionByCodeSystemEntityVersionId1;
    }

    /**
     * Legt den Wert der codeSystemEntityVersionByCodeSystemEntityVersionId1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public void setCodeSystemEntityVersionByCodeSystemEntityVersionId1(CodeSystemEntityVersion value) {
        this.codeSystemEntityVersionByCodeSystemEntityVersionId1 = value;
    }

    /**
     * Ruft den Wert der codeSystemEntityVersionByCodeSystemEntityVersionId2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public CodeSystemEntityVersion getCodeSystemEntityVersionByCodeSystemEntityVersionId2() {
        return codeSystemEntityVersionByCodeSystemEntityVersionId2;
    }

    /**
     * Legt den Wert der codeSystemEntityVersionByCodeSystemEntityVersionId2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public void setCodeSystemEntityVersionByCodeSystemEntityVersionId2(CodeSystemEntityVersion value) {
        this.codeSystemEntityVersionByCodeSystemEntityVersionId2 = value;
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
     * Ruft den Wert der insertTimestamp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInsertTimestamp() {
        return insertTimestamp;
    }

    /**
     * Legt den Wert der insertTimestamp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInsertTimestamp(XMLGregorianCalendar value) {
        this.insertTimestamp = value;
    }

    /**
     * Ruft den Wert der leftId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLeftId() {
        return leftId;
    }

    /**
     * Legt den Wert der leftId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLeftId(Long value) {
        this.leftId = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStatus(Integer value) {
        this.status = value;
    }

    /**
     * Ruft den Wert der statusDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatusDate() {
        return statusDate;
    }

    /**
     * Legt den Wert der statusDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStatusDate(XMLGregorianCalendar value) {
        this.statusDate = value;
    }

}
