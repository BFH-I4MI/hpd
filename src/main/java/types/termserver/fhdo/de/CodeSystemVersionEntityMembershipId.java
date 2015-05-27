
package types.termserver.fhdo.de;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für codeSystemVersionEntityMembershipId complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="codeSystemVersionEntityMembershipId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeSystemEntityId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codeSystemVersionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codeSystemVersionEntityMembershipId", propOrder = {
    "codeSystemEntityId",
    "codeSystemVersionId"
})
public class CodeSystemVersionEntityMembershipId {

    protected Long codeSystemEntityId;
    protected Long codeSystemVersionId;

    /**
     * Ruft den Wert der codeSystemEntityId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemEntityId() {
        return codeSystemEntityId;
    }

    /**
     * Legt den Wert der codeSystemEntityId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemEntityId(Long value) {
        this.codeSystemEntityId = value;
    }

    /**
     * Ruft den Wert der codeSystemVersionId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodeSystemVersionId() {
        return codeSystemVersionId;
    }

    /**
     * Legt den Wert der codeSystemVersionId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodeSystemVersionId(Long value) {
        this.codeSystemVersionId = value;
    }

}
