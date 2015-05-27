
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für conceptValueSetMembershipId complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="conceptValueSetMembershipId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
@XmlType(name = "conceptValueSetMembershipId", propOrder = {
    "codeSystemEntityVersionId",
    "valuesetVersionId"
})
public class ConceptValueSetMembershipId {

    protected Long codeSystemEntityVersionId;
    protected Long valuesetVersionId;

    /**
     * Ruft den Wert der codeSystemEntityVersionId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemEntityVersionId() {
        return codeSystemEntityVersionId;
    }

    /**
     * Legt den Wert der codeSystemEntityVersionId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemEntityVersionId(Long value) {
        this.codeSystemEntityVersionId = value;
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
