
package com.ctrip.soa.framework.soa.testservice.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>EnumType2的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="EnumType2">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EnumType2")
@XmlEnum
public enum EnumType2 {

    OK;

    public String value() {
        return name();
    }

    public static EnumType2 fromValue(String v) {
        return valueOf(v);
    }

}
