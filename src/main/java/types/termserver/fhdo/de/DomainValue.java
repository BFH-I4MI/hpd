
package types.termserver.fhdo.de;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für domainValue complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="domainValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attribut1classname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribut1value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codeSystems" type="{de.fhdo.termserver.types}codeSystem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="domain" type="{de.fhdo.termserver.types}domain" minOccurs="0"/>
 *         &lt;element name="domainCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainDisplay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainValueId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="domainValuesForDomainValueId1" type="{de.fhdo.termserver.types}domainValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="domainValuesForDomainValueId2" type="{de.fhdo.termserver.types}domainValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="imageFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sysParamsForModifyLevel" type="{de.fhdo.termserver.types}sysParam" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sysParamsForValidityDomain" type="{de.fhdo.termserver.types}sysParam" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "domainValue", propOrder = {
    "attribut1Classname",
    "attribut1Value",
    "codeSystems",
    "domain",
    "domainCode",
    "domainDisplay",
    "domainValueId",
    "domainValuesForDomainValueId1",
    "domainValuesForDomainValueId2",
    "imageFile",
    "orderNo",
    "sysParamsForModifyLevel",
    "sysParamsForValidityDomain"
})
public class DomainValue {

    @XmlElement(name = "attribut1classname")
    protected String attribut1Classname;
    @XmlElement(name = "attribut1value")
    protected String attribut1Value;
    @XmlElement(nillable = true)
    protected List<CodeSystem> codeSystems;
    protected Domain domain;
    protected String domainCode;
    protected String domainDisplay;
    protected Long domainValueId;
    @XmlElement(nillable = true)
    protected List<DomainValue> domainValuesForDomainValueId1;
    @XmlElement(nillable = true)
    protected List<DomainValue> domainValuesForDomainValueId2;
    protected String imageFile;
    protected Integer orderNo;
    @XmlElement(nillable = true)
    protected List<SysParam> sysParamsForModifyLevel;
    @XmlElement(nillable = true)
    protected List<SysParam> sysParamsForValidityDomain;

    /**
     * Ruft den Wert der attribut1Classname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribut1Classname() {
        return attribut1Classname;
    }

    /**
     * Legt den Wert der attribut1Classname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribut1Classname(String value) {
        this.attribut1Classname = value;
    }

    /**
     * Ruft den Wert der attribut1Value-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribut1Value() {
        return attribut1Value;
    }

    /**
     * Legt den Wert der attribut1Value-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribut1Value(String value) {
        this.attribut1Value = value;
    }

    /**
     * Gets the value of the codeSystems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeSystem }
     * 
     * 
     */
    public List<CodeSystem> getCodeSystems() {
        if (codeSystems == null) {
            codeSystems = new ArrayList<CodeSystem>();
        }
        return this.codeSystems;
    }

    /**
     * Ruft den Wert der domain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Domain }
     *     
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Legt den Wert der domain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Domain }
     *     
     */
    public void setDomain(Domain value) {
        this.domain = value;
    }

    /**
     * Ruft den Wert der domainCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainCode() {
        return domainCode;
    }

    /**
     * Legt den Wert der domainCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainCode(String value) {
        this.domainCode = value;
    }

    /**
     * Ruft den Wert der domainDisplay-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainDisplay() {
        return domainDisplay;
    }

    /**
     * Legt den Wert der domainDisplay-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainDisplay(String value) {
        this.domainDisplay = value;
    }

    /**
     * Ruft den Wert der domainValueId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDomainValueId() {
        return domainValueId;
    }

    /**
     * Legt den Wert der domainValueId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDomainValueId(Long value) {
        this.domainValueId = value;
    }

    /**
     * Gets the value of the domainValuesForDomainValueId1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainValuesForDomainValueId1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainValuesForDomainValueId1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainValue }
     * 
     * 
     */
    public List<DomainValue> getDomainValuesForDomainValueId1() {
        if (domainValuesForDomainValueId1 == null) {
            domainValuesForDomainValueId1 = new ArrayList<DomainValue>();
        }
        return this.domainValuesForDomainValueId1;
    }

    /**
     * Gets the value of the domainValuesForDomainValueId2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainValuesForDomainValueId2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainValuesForDomainValueId2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainValue }
     * 
     * 
     */
    public List<DomainValue> getDomainValuesForDomainValueId2() {
        if (domainValuesForDomainValueId2 == null) {
            domainValuesForDomainValueId2 = new ArrayList<DomainValue>();
        }
        return this.domainValuesForDomainValueId2;
    }

    /**
     * Ruft den Wert der imageFile-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageFile() {
        return imageFile;
    }

    /**
     * Legt den Wert der imageFile-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageFile(String value) {
        this.imageFile = value;
    }

    /**
     * Ruft den Wert der orderNo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * Legt den Wert der orderNo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrderNo(Integer value) {
        this.orderNo = value;
    }

    /**
     * Gets the value of the sysParamsForModifyLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sysParamsForModifyLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSysParamsForModifyLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SysParam }
     * 
     * 
     */
    public List<SysParam> getSysParamsForModifyLevel() {
        if (sysParamsForModifyLevel == null) {
            sysParamsForModifyLevel = new ArrayList<SysParam>();
        }
        return this.sysParamsForModifyLevel;
    }

    /**
     * Gets the value of the sysParamsForValidityDomain property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sysParamsForValidityDomain property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSysParamsForValidityDomain().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SysParam }
     * 
     * 
     */
    public List<SysParam> getSysParamsForValidityDomain() {
        if (sysParamsForValidityDomain == null) {
            sysParamsForValidityDomain = new ArrayList<SysParam>();
        }
        return this.sysParamsForValidityDomain;
    }

}
