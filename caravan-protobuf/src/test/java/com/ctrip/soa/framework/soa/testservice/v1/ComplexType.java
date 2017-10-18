
package com.ctrip.soa.framework.soa.testservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ComplexType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="intType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="basicTypes1" type="{http://soa.ctrip.com/framework/soa/testservice/v1}BasicTypes1"/>
 *         &lt;element name="basicTypes2" type="{http://soa.ctrip.com/framework/soa/testservice/v1}BasicTypes2"/>
 *         &lt;element name="enumType1" type="{http://soa.ctrip.com/framework/soa/testservice/v1}EnumType1"/>
 *         &lt;element name="enumType2" type="{http://soa.ctrip.com/framework/soa/testservice/v1}EnumType2"/>
 *         &lt;element name="arrayTypes" type="{http://soa.ctrip.com/framework/soa/testservice/v1}ArrayTypes"/>
 *         &lt;element name="listTypes" type="{http://soa.ctrip.com/framework/soa/testservice/v1}ListTypes"/>
 *         &lt;element name="stringType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexType", propOrder = {
    "intType",
    "basicTypes1",
    "basicTypes2",
    "enumType1",
    "enumType2",
    "arrayTypes",
    "listTypes",
    "stringType"
})
public class ComplexType {

    protected int intType;
    @XmlElement(required = true)
    protected BasicTypes1 basicTypes1;
    @XmlElement(required = true)
    protected BasicTypes2 basicTypes2;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EnumType1 enumType1;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected EnumType2 enumType2;
    @XmlElement(required = true)
    protected ArrayTypes arrayTypes;
    @XmlElement(required = true)
    protected ListTypes listTypes;
    @XmlElement(required = true)
    protected String stringType;

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
     * 获取basicTypes1属性的值。
     *
     * @return
     *     possible object is
     *     {@link BasicTypes1 }
     *
     */
    public BasicTypes1 getBasicTypes1() {
        return basicTypes1;
    }

    /**
     * 设置basicTypes1属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BasicTypes1 }
     *
     */
    public void setBasicTypes1(BasicTypes1 value) {
        this.basicTypes1 = value;
    }

    /**
     * 获取basicTypes2属性的值。
     *
     * @return
     *     possible object is
     *     {@link BasicTypes2 }
     *
     */
    public BasicTypes2 getBasicTypes2() {
        return basicTypes2;
    }

    /**
     * 设置basicTypes2属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BasicTypes2 }
     *
     */
    public void setBasicTypes2(BasicTypes2 value) {
        this.basicTypes2 = value;
    }

    /**
     * 获取enumType1属性的值。
     *
     * @return
     *     possible object is
     *     {@link EnumType1 }
     *
     */
    public EnumType1 getEnumType1() {
        return enumType1;
    }

    /**
     * 设置enumType1属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link EnumType1 }
     *
     */
    public void setEnumType1(EnumType1 value) {
        this.enumType1 = value;
    }

    /**
     * 获取enumType2属性的值。
     *
     * @return
     *     possible object is
     *     {@link EnumType2 }
     *
     */
    public EnumType2 getEnumType2() {
        return enumType2;
    }

    /**
     * 设置enumType2属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link EnumType2 }
     *
     */
    public void setEnumType2(EnumType2 value) {
        this.enumType2 = value;
    }

    /**
     * 获取arrayTypes属性的值。
     *
     * @return
     *     possible object is
     *     {@link ArrayTypes }
     *
     */
    public ArrayTypes getArrayTypes() {
        return arrayTypes;
    }

    /**
     * 设置arrayTypes属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ArrayTypes }
     *
     */
    public void setArrayTypes(ArrayTypes value) {
        this.arrayTypes = value;
    }

    /**
     * 获取listTypes属性的值。
     *
     * @return
     *     possible object is
     *     {@link ListTypes }
     *
     */
    public ListTypes getListTypes() {
        return listTypes;
    }

    /**
     * 设置listTypes属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ListTypes }
     *
     */
    public void setListTypes(ListTypes value) {
        this.listTypes = value;
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

}
