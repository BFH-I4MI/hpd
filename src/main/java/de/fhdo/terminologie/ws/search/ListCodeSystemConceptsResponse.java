
package de.fhdo.terminologie.ws.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import types.termserver.fhdo.de.CodeSystemEntity;


/**
 * <p>Java-Klasse für ListCodeSystemConceptsResponse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ListCodeSystemConceptsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="returnInfos" type="{http://search.ws.terminologie.fhdo.de/}returnType" minOccurs="0"/>
 *                   &lt;element name="pagingInfos" type="{http://search.ws.terminologie.fhdo.de/}pagingResultType" minOccurs="0"/>
 *                   &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListCodeSystemConceptsResponse", propOrder = {
    "_return"
})
public class ListCodeSystemConceptsResponse {

    @XmlElement(name = "return")
    protected ListCodeSystemConceptsResponse.Return _return;

    /**
     * Ruft den Wert der return-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ListCodeSystemConceptsResponse.Return }
     *     
     */
    public ListCodeSystemConceptsResponse.Return getReturn() {
        return _return;
    }

    /**
     * Legt den Wert der return-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ListCodeSystemConceptsResponse.Return }
     *     
     */
    public void setReturn(ListCodeSystemConceptsResponse.Return value) {
        this._return = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="returnInfos" type="{http://search.ws.terminologie.fhdo.de/}returnType" minOccurs="0"/>
     *         &lt;element name="pagingInfos" type="{http://search.ws.terminologie.fhdo.de/}pagingResultType" minOccurs="0"/>
     *         &lt;element name="codeSystemEntity" type="{de.fhdo.termserver.types}codeSystemEntity" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "returnInfos",
        "pagingInfos",
        "codeSystemEntity"
    })
    public static class Return {

        protected ReturnType returnInfos;
        protected PagingResultType pagingInfos;
        @XmlElement(nillable = true)
        protected List<CodeSystemEntity> codeSystemEntity;

        /**
         * Ruft den Wert der returnInfos-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link ReturnType }
         *     
         */
        public ReturnType getReturnInfos() {
            return returnInfos;
        }

        /**
         * Legt den Wert der returnInfos-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link ReturnType }
         *     
         */
        public void setReturnInfos(ReturnType value) {
            this.returnInfos = value;
        }

        /**
         * Ruft den Wert der pagingInfos-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link PagingResultType }
         *     
         */
        public PagingResultType getPagingInfos() {
            return pagingInfos;
        }

        /**
         * Legt den Wert der pagingInfos-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link PagingResultType }
         *     
         */
        public void setPagingInfos(PagingResultType value) {
            this.pagingInfos = value;
        }

        /**
         * Gets the value of the codeSystemEntity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the codeSystemEntity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCodeSystemEntity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CodeSystemEntity }
         * 
         * 
         */
        public List<CodeSystemEntity> getCodeSystemEntity() {
            if (codeSystemEntity == null) {
                codeSystemEntity = new ArrayList<CodeSystemEntity>();
            }
            return this.codeSystemEntity;
        }

    }

}
