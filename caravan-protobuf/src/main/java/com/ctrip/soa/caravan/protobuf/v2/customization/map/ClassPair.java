package com.ctrip.soa.caravan.protobuf.v2.customization.map;

public class ClassPair {

  Class<?> keyClass;
  Class<?> valueClass;

  public ClassPair(Class<?> keyClass, Class<?> valueClass) {
    this.keyClass = keyClass;
    this.valueClass = valueClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ClassPair pair = (ClassPair) o;

    if (keyClass != null ? !keyClass.equals(pair.keyClass) : pair.keyClass != null) {
      return false;
    }
    return valueClass != null ? valueClass.equals(pair.valueClass) : pair.valueClass == null;
  }

  @Override
  public int hashCode() {
    int result = keyClass != null ? keyClass.hashCode() : 0;
    result = 31 * result + (valueClass != null ? valueClass.hashCode() : 0);
    return result;
  }
}