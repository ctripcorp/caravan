
package com.ctrip.soa.framework.soa.testservice.v1;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BasicTypes2 complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BasicTypes2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ubyteType" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="uintType" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="ushortType" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *         &lt;element name="ulongType" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasicTypes2", propOrder = {
    "ubyteType",
    "uintType",
    "ushortType",
    "ulongType"
})
public class BasicTypes2 {

    @XmlSchemaType(name = "unsignedByte")
    protected short ubyteType;
    @XmlSchemaType(name = "unsignedInt")
    protected long uintType;
    @XmlSchemaType(name = "unsignedShort")
    protected int ushortType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger ulongType;

    /**
     * 获取ubyteType属性的值。
     * 
     */
    public short getUbyteType() {
        return ubyteType;
    }

    /**
     * 设置ubyteType属性的值。
     * 
     */
    public void setUbyteType(short value) {
        this.ubyteType = value;
    }

    /**
     * 获取uintType属性的值。
     * 
     */
    public long getUintType() {
        return uintType;
    }

    /**
     * 设置uintType属性的值。
     * 
     */
    public void setUintType(long value) {
        this.uintType = value;
    }

    /**
     * 获取ushortType属性的值。
     * 
     */
    public int getUshortType() {
        return ushortType;
    }

    /**
     * 设置ushortType属性的值。
     * 
     */
    public void setUshortType(int value) {
        this.ushortType = value;
    }

    /**
     * 获取ulongType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUlongType() {
        return ulongType;
    }

    /**
     * 设置ulongType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUlongType(BigInteger value) {
        this.ulongType = value;
    }

}
