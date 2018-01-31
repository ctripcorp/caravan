package com.ctrip.soa.caravan.util.serializer.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLPojo", propOrder = {
    "name", "id"
})
public class XMLPojo {

  @XmlElement(name = "name", required = true)
  private String name;

  @XmlElement(name = "id", required = true)
  private long id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    XMLPojo xmlPojo = (XMLPojo) o;

    if (id != xmlPojo.id) {
      return false;
    }
    return name != null ? name.equals(xmlPojo.name) : xmlPojo.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (int) (id ^ (id >>> 32));
    return result;
  }
}
