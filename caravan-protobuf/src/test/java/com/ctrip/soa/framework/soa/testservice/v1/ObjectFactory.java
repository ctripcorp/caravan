
package com.ctrip.soa.framework.soa.testservice.v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ctrip.soa.framework.soa.testservice.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ctrip.soa.framework.soa.testservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ComplexType }
     * 
     */
    public ComplexType createComplexType() {
        return new ComplexType();
    }

    /**
     * Create an instance of {@link ArrayTypes }
     * 
     */
    public ArrayTypes createArrayTypes() {
        return new ArrayTypes();
    }

    /**
     * Create an instance of {@link ListTypes }
     * 
     */
    public ListTypes createListTypes() {
        return new ListTypes();
    }

    /**
     * Create an instance of {@link BasicTypes2 }
     * 
     */
    public BasicTypes2 createBasicTypes2() {
        return new BasicTypes2();
    }

    /**
     * Create an instance of {@link BasicTypes1 }
     * 
     */
    public BasicTypes1 createBasicTypes1() {
        return new BasicTypes1();
    }

}
