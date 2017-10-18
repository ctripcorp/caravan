
package com.ctrip.soa.framework.soa.testservice.v1;

import java.math.BigDecimal;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>BasicTypes1 complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BasicTypes1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="byteType" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="shortType" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="intType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="longType" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="floatType" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="doubleType" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="decimalType" type="{http://www.w3.org/2001/XMLSchema}bigdecimal"/>
 *         &lt;element name="booleanType" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="stringType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateTimeType" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="binaryType" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasicTypes1", propOrder = {
    "byteType",
    "shortType",
    "intType",
    "longType",
    "floatType",
    "doubleType",
    "decimalType",
    "booleanType",
    "stringType",
    "dateTimeType",
    "binaryType"
})
public class BasicTypes1 {

    protected byte byteType;
    protected short shortType;
    protected int intType;
    protected long longType;
    protected float floatType;
    protected double doubleType;
    @XmlElement(required = true)
    protected BigDecimal decimalType;
    protected boolean booleanType;
    @XmlElement(required = true)
    protected String stringType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTimeType;
    @XmlElement(required = true)
    protected byte[] binaryType;
    protected Calendar calendar;

    /**
     * 获取byteType属性的值。
     * 
     */
    public byte getByteType() {
        return byteType;
    }

    /**
     * 设置byteType属性的值。
     * 
     */
    public void setByteType(byte value) {
        this.byteType = value;
    }

    /**
     * 获取shortType属性的值。
     *
     */
    public short getShortType() {
        return shortType;
    }

    /**
     * 设置shortType属性的值。
     *
     */
    public void setShortType(short value) {
        this.shortType = value;
    }

    /**
     * 获取intType属性的值。
     *
     */
    public int getIntType() {
        return intType;
    }

    /**
     * 设置intType属性的值。
     *
     */
    public void setIntType(int value) {
        this.intType = value;
    }

    /**
     * 获取longType属性的值。
     *
     */
    public long getLongType() {
        return longType;
    }

    /**
     * 设置longType属性的值。
     *
     */
    public void setLongType(long value) {
        this.longType = value;
    }

    /**
     * 获取floatType属性的值。
     *
     */
    public float getFloatType() {
        return floatType;
    }

    /**
     * 设置floatType属性的值。
     *
     */
    public void setFloatType(float value) {
        this.floatType = value;
    }

    /**
     * 获取doubleType属性的值。
     *
     */
    public double getDoubleType() {
        return doubleType;
    }

    /**
     * 设置doubleType属性的值。
     *
     */
    public void setDoubleType(double value) {
        this.doubleType = value;
    }

    /**
     * 获取decimalType属性的值。
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDecimalType() {
        return decimalType;
    }

    /**
     * 设置decimalType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setDecimalType(BigDecimal value) {
        this.decimalType = value;
    }

    /**
     * 获取booleanType属性的值。
     *
     */
    public boolean isBooleanType() {
        return booleanType;
    }

    /**
     * 设置booleanType属性的值。
     *
     */
    public void setBooleanType(boolean value) {
        this.booleanType = value;
    }

    /**
     * 获取stringType属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStringType() {
        return stringType;
    }

    /**
     * 设置stringType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStringType(String value) {
        this.stringType = value;
    }

    /**
     * 获取dateTimeType属性的值。
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDateTimeType() {
        return dateTimeType;
    }

    /**
     * 设置dateTimeType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDateTimeType(XMLGregorianCalendar value) {
        this.dateTimeType = value;
    }

    /**
     * 获取binaryType属性的值。
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaryType() {
        return binaryType;
    }

    /**
     * 设置binaryType属性的值。
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaryType(byte[] value) {
        this.binaryType = value;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
