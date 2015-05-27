
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für sortType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="sortType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALPHABETICALLY"/>
 *     &lt;enumeration value="ORDER_NR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sortType")
@XmlEnum
public enum SortType {

    ALPHABETICALLY,
    ORDER_NR;

    public String value() {
        return name();
    }

    public static SortType fromValue(String v) {
        return valueOf(v);
    }

}
