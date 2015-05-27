
package de.fhdo.terminologie.ws.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für overallErrorCategory.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="overallErrorCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INFO"/>
 *     &lt;enumeration value="WARN"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "overallErrorCategory")
@XmlEnum
public enum OverallErrorCategory {

    INFO,
    WARN,
    ERROR;

    public String value() {
        return name();
    }

    public static OverallErrorCategory fromValue(String v) {
        return valueOf(v);
    }

}
