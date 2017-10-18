
package com.ctrip.soa.framework.soa.testservice.v1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>ArrayTypes complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayTypes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="byteType" type="{http://www.w3.org/2001/XMLSchema}byte" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="shortType" type="{http://www.w3.org/2001/XMLSchema}short" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="intType" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="longType" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="floatType" type="{http://www.w3.org/2001/XMLSchema}float" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="doubleType" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="decimalType" type="{http://www.w3.org/2001/XMLSchema}bigdecimal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="booleanType" type="{http://www.w3.org/2001/XMLSchema}boolean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stringType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dateTimeType" type="{http://www.w3.org/2001/XMLSchema}dateTime" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ubyteType" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="uintType" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ushortType" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ulongType" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayTypes", propOrder = {
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
    "ubyteType",
    "uintType",
    "ushortType",
    "ulongType"
})
public class ArrayTypes {

    @XmlElement(type = Byte.class)
    protected List<Byte> byteType;
    @XmlElement(type = Short.class)
    protected List<Short> shortType;
    @XmlElement(type = Integer.class)
    protected List<Integer> intType;
    @XmlElement(type = Long.class)
    protected List<Long> longType;
    @XmlElement(type = Float.class)
    protected List<Float> floatType;
    @XmlElement(type = Double.class)
    protected List<Double> doubleType;
    protected List<BigDecimal> decimalType;
    @XmlElement(type = Boolean.class)
    protected List<Boolean> booleanType;
    protected List<String> stringType;
    @XmlSchemaType(name = "dateTime")
    protected List<XMLGregorianCalendar> dateTimeType;
    @XmlElement(type = Short.class)
    @XmlSchemaType(name = "unsignedByte")
    protected List<Short> ubyteType;
    @XmlElement(type = Long.class)
    @XmlSchemaType(name = "unsignedInt")
    protected List<Long> uintType;
    @XmlElement(type = Integer.class)
    @XmlSchemaType(name = "unsignedShort")
    protected List<Integer> ushortType;
    @XmlSchemaType(name = "unsignedLong")
    protected List<BigInteger> ulongType;

    /**
     * Gets the value of the byteType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the byteType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getByteType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Byte }
     *
     *
     */
    public List<Byte> getByteType() {
        if (byteType == null) {
            byteType = new ArrayList<Byte>();
        }
        return this.byteType;
    }

    /**
     * Gets the value of the shortType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shortType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShortType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Short }
     *
     *
     */
    public List<Short> getShortType() {
        if (shortType == null) {
            shortType = new ArrayList<Short>();
        }
        return this.shortType;
    }

    /**
     * Gets the value of the intType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the intType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     *
     *
     */
    public List<Integer> getIntType() {
        if (intType == null) {
            intType = new ArrayList<Integer>();
        }
        return this.intType;
    }

    /**
     * Gets the value of the longType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the longType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLongType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     *
     *
     */
    public List<Long> getLongType() {
        if (longType == null) {
            longType = new ArrayList<Long>();
        }
        return this.longType;
    }

    /**
     * Gets the value of the floatType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the floatType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloatType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Float }
     *
     *
     */
    public List<Float> getFloatType() {
        if (floatType == null) {
            floatType = new ArrayList<Float>();
        }
        return this.floatType;
    }

    /**
     * Gets the value of the doubleType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the doubleType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDoubleType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     *
     *
     */
    public List<Double> getDoubleType() {
        if (doubleType == null) {
            doubleType = new ArrayList<Double>();
        }
        return this.doubleType;
    }

    /**
     * Gets the value of the decimalType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the decimalType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDecimalType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigDecimal }
     *
     *
     */
    public List<BigDecimal> getDecimalType() {
        if (decimalType == null) {
            decimalType = new ArrayList<BigDecimal>();
        }
        return this.decimalType;
    }

    /**
     * Gets the value of the booleanType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the booleanType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBooleanType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     *
     *
     */
    public List<Boolean> getBooleanType() {
        if (booleanType == null) {
            booleanType = new ArrayList<Boolean>();
        }
        return this.booleanType;
    }

    /**
     * Gets the value of the stringType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stringType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStringType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getStringType() {
        if (stringType == null) {
            stringType = new ArrayList<String>();
        }
        return this.stringType;
    }

    /**
     * Gets the value of the dateTimeType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateTimeType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateTimeType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLGregorianCalendar }
     *
     *
     */
    public List<XMLGregorianCalendar> getDateTimeType() {
        if (dateTimeType == null) {
            dateTimeType = new ArrayList<XMLGregorianCalendar>();
        }
        return this.dateTimeType;
    }

    /**
     * Gets the value of the ubyteType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ubyteType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUbyteType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Short }
     *
     *
     */
    public List<Short> getUbyteType() {
        if (ubyteType == null) {
            ubyteType = new ArrayList<Short>();
        }
        return this.ubyteType;
    }

    /**
     * Gets the value of the uintType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uintType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUintType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     *
     *
     */
    public List<Long> getUintType() {
        if (uintType == null) {
            uintType = new ArrayList<Long>();
        }
        return this.uintType;
    }

    /**
     * Gets the value of the ushortType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ushortType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUshortType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     *
     *
     */
    public List<Integer> getUshortType() {
        if (ushortType == null) {
            ushortType = new ArrayList<Integer>();
        }
        return this.ushortType;
    }

    /**
     * Gets the value of the ulongType property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ulongType property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUlongType().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     *
     *
     */
    public List<BigInteger> getUlongType() {
        if (ulongType == null) {
            ulongType = new ArrayList<BigInteger>();
        }
        return this.ulongType;
    }

    public void setByteType(List<Byte> value) {
        this.byteType = value;
    }

    public void setShortType(List<Short> value) {
        this.shortType = value;
    }

    public void setIntType(List<Integer> value) {
        this.intType = value;
    }

    public void setLongType(List<Long> value) {
        this.longType = value;
    }

    public void setFloatType(List<Float> value) {
        this.floatType = value;
    }

    public void setDoubleType(List<Double> value) {
        this.doubleType = value;
    }

    public void setDecimalType(List<BigDecimal> value) {
        this.decimalType = value;
    }

    public void setBooleanType(List<Boolean> value) {
        this.booleanType = value;
    }

    public void setStringType(List<String> value) {
        this.stringType = value;
    }

    public void setDateTimeType(List<XMLGregorianCalendar> value) {
        this.dateTimeType = value;
    }

    public void setUbyteType(List<Short> value) {
        this.ubyteType = value;
    }

    public void setUintType(List<Long> value) {
        this.uintType = value;
    }

    public void setUshortType(List<Integer> value) {
        this.ushortType = value;
    }

    public void setUlongType(List<BigInteger> value) {
        this.ulongType = value;
    }

}
