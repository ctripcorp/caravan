package com.ctrip.soa.caravan.util.serializer.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;

public class ObjectFactory {

  public final static QName QNAME = new QName("http://soa.ctrip.com/common/types/v1", "XMLPojo");

  @XmlElementDecl(namespace = "http://soa.ctrip.com/common/types/v1", name = "XMLPojo")
  public JAXBElement<XMLPojo> createCheckHealthRequest(XMLPojo value) {
    return new JAXBElement<XMLPojo>(QNAME, XMLPojo.class, null, value);
  }

}
