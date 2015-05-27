
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für sortDirection.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="sortDirection">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ASCENDING"/>
 *     &lt;enumeration value="DESCENDING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sortDirection")
@XmlEnum
public enum SortDirection {

    ASCENDING,
    DESCENDING;

    public String value() {
        return name();
    }

    public static SortDirection fromValue(String v) {
        return valueOf(v);
    }

}
