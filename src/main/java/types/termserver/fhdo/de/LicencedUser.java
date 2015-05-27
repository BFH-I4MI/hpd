
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für licencedUser complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="licencedUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemVersion" type="{de.fhdo.termserver.types}codeSystemVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{de.fhdo.termserver.types}licencedUserId" minOccurs="0"/>
 *         &lt;element name="licenceType" type="{de.fhdo.termserver.types}licenceType" minOccurs="0"/>
 *         &lt;element name="termUser" type="{de.fhdo.termserver.types}termUser" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licencedUser", propOrder = {
    "codeSystemVersion",
    "id",
    "licenceType",
    "termUser",
    "validFrom",
    "validTo"
})
public class LicencedUser {

    protected CodeSystemVersion codeSystemVersion;
    protected LicencedUserId id;
    protected LicenceType licenceType;
    protected TermUser termUser;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validFrom;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validTo;

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
     *     {@link LicencedUserId }
     *     
     */
    public LicencedUserId getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LicencedUserId }
     *     
     */
    public void setId(LicencedUserId value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der licenceType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LicenceType }
     *     
     */
    public LicenceType getLicenceType() {
        return licenceType;
    }

    /**
     * Legt den Wert der licenceType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LicenceType }
     *     
     */
    public void setLicenceType(LicenceType value) {
        this.licenceType = value;
    }

    /**
     * Ruft den Wert der termUser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TermUser }
     *     
     */
    public TermUser getTermUser() {
        return termUser;
    }

    /**
     * Legt den Wert der termUser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TermUser }
     *     
     */
    public void setTermUser(TermUser value) {
        this.termUser = value;
    }

    /**
     * Ruft den Wert der validFrom-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidFrom() {
        return validFrom;
    }

    /**
     * Legt den Wert der validFrom-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidFrom(XMLGregorianCalendar value) {
        this.validFrom = value;
    }

    /**
     * Ruft den Wert der validTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidTo() {
        return validTo;
    }

    /**
     * Legt den Wert der validTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidTo(XMLGregorianCalendar value) {
        this.validTo = value;
    }

}
