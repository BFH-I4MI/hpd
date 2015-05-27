
package types.termserver.fhdo.de;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für metadataParameter complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="metadataParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystem" type="{de.fhdo.termserver.types}codeSystem" minOccurs="0"/>
 *         &lt;element name="codeSystemMetadataValues" type="{de.fhdo.termserver.types}codeSystemMetadataValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="languageCd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxLength" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="metadataParameterType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paramDatatype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paramName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paramNameDisplay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueSet" type="{de.fhdo.termserver.types}valueSet" minOccurs="0"/>
 *         &lt;element name="valueSetMetadataValues" type="{de.fhdo.termserver.types}valueSetMetadataValue" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataParameter", propOrder = {
    "codeSystem",
    "codeSystemMetadataValues",
    "description",
    "id",
    "languageCd",
    "maxLength",
    "metadataParameterType",
    "paramDatatype",
    "paramName",
    "paramNameDisplay",
    "valueSet",
    "valueSetMetadataValues"
})
public class MetadataParameter {

    protected CodeSystem codeSystem;
    @XmlElement(nillable = true)
    protected List<CodeSystemMetadataValue> codeSystemMetadataValues;
    protected String description;
    protected Long id;
    protected String languageCd;
    protected Integer maxLength;
    protected String metadataParameterType;
    protected String paramDatatype;
    protected String paramName;
    protected String paramNameDisplay;
    protected ValueSet valueSet;
    @XmlElement(nillable = true)
    protected List<ValueSetMetadataValue> valueSetMetadataValues;

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
     * Gets the value of the codeSystemMetadataValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeSystemMetadataValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeSystemMetadataValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeSystemMetadataValue }
     * 
     * 
     */
    public List<CodeSystemMetadataValue> getCodeSystemMetadataValues() {
        if (codeSystemMetadataValues == null) {
            codeSystemMetadataValues = new ArrayList<CodeSystemMetadataValue>();
        }
        return this.codeSystemMetadataValues;
    }

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
     * Ruft den Wert der languageCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageCd() {
        return languageCd;
    }

    /**
     * Legt den Wert der languageCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageCd(String value) {
        this.languageCd = value;
    }

    /**
     * Ruft den Wert der maxLength-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Legt den Wert der maxLength-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxLength(Integer value) {
        this.maxLength = value;
    }

    /**
     * Ruft den Wert der metadataParameterType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadataParameterType() {
        return metadataParameterType;
    }

    /**
     * Legt den Wert der metadataParameterType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadataParameterType(String value) {
        this.metadataParameterType = value;
    }

    /**
     * Ruft den Wert der paramDatatype-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParamDatatype() {
        return paramDatatype;
    }

    /**
     * Legt den Wert der paramDatatype-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParamDatatype(String value) {
        this.paramDatatype = value;
    }

    /**
     * Ruft den Wert der paramName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Legt den Wert der paramName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParamName(String value) {
        this.paramName = value;
    }

    /**
     * Ruft den Wert der paramNameDisplay-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParamNameDisplay() {
        return paramNameDisplay;
    }

    /**
     * Legt den Wert der paramNameDisplay-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParamNameDisplay(String value) {
        this.paramNameDisplay = value;
    }

    /**
     * Ruft den Wert der valueSet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ValueSet }
     *     
     */
    public ValueSet getValueSet() {
        return valueSet;
    }

    /**
     * Legt den Wert der valueSet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSet }
     *     
     */
    public void setValueSet(ValueSet value) {
        this.valueSet = value;
    }

    /**
     * Gets the value of the valueSetMetadataValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSetMetadataValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSetMetadataValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueSetMetadataValue }
     * 
     * 
     */
    public List<ValueSetMetadataValue> getValueSetMetadataValues() {
        if (valueSetMetadataValues == null) {
            valueSetMetadataValues = new ArrayList<ValueSetMetadataValue>();
        }
        return this.valueSetMetadataValues;
    }

}
