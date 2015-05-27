
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für valueSetMetadataValue complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="valueSetMetadataValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityVersion" type="{de.fhdo.termserver.types}codeSystemEntityVersion" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="metadataParameter" type="{de.fhdo.termserver.types}metadataParameter" minOccurs="0"/>
 *         &lt;element name="parameterValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valuesetVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "valueSetMetadataValue", propOrder = {
    "codeSystemEntityVersion",
    "id",
    "metadataParameter",
    "parameterValue",
    "valuesetVersionId"
})
public class ValueSetMetadataValue {

    protected CodeSystemEntityVersion codeSystemEntityVersion;
    protected Long id;
    protected MetadataParameter metadataParameter;
    protected String parameterValue;
    protected Long valuesetVersionId;

    /**
     * Ruft den Wert der codeSystemEntityVersion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public CodeSystemEntityVersion getCodeSystemEntityVersion() {
        return codeSystemEntityVersion;
    }

    /**
     * Legt den Wert der codeSystemEntityVersion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSystemEntityVersion }
     *     
     */
    public void setCodeSystemEntityVersion(CodeSystemEntityVersion value) {
        this.codeSystemEntityVersion = value;
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
     * Ruft den Wert der metadataParameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MetadataParameter }
     *     
     */
    public MetadataParameter getMetadataParameter() {
        return metadataParameter;
    }

    /**
     * Legt den Wert der metadataParameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadataParameter }
     *     
     */
    public void setMetadataParameter(MetadataParameter value) {
        this.metadataParameter = value;
    }

    /**
     * Ruft den Wert der parameterValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * Legt den Wert der parameterValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterValue(String value) {
        this.parameterValue = value;
    }

    /**
     * Ruft den Wert der valuesetVersionId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValuesetVersionId() {
        return valuesetVersionId;
    }

    /**
     * Legt den Wert der valuesetVersionId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValuesetVersionId(Long value) {
        this.valuesetVersionId = value;
    }

}
