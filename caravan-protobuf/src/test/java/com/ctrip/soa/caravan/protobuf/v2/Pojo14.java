package com.ctrip.soa.caravan.protobuf.v2;

import java.util.List;
import java.util.Map;

/**
 * Created by marsqing on 17/06/2017.
 */
public class Pojo14 {

  public enum SomeEnum {
    Enum1, Enum2, Enum3
  }

  private List<SomeEnum> list;
  private SomeEnum[] array;
  private Map<SomeEnum, String> keyOfMap;
  private Map<String, SomeEnum> valueOfMap;

  public List<SomeEnum> getList() {
    return list;
  }

  public void setList(List<SomeEnum> list) {
    this.list = list;
  }

  public SomeEnum[] getArray() {
    return array;
  }

  public void setArray(SomeEnum[] array) {
    this.array = array;
  }

  public Map<SomeEnum, String> getKeyOfMap() {
    return keyOfMap;
  }

  public void setKeyOfMap(Map<SomeEnum, String> keyOfMap) {
    this.keyOfMap = keyOfMap;
  }

  public Map<String, SomeEnum> getValueOfMap() {
    return valueOfMap;
  }

  public void setValueOfMap(Map<String, SomeEnum> valueOfMap) {
    this.valueOfMap = valueOfMap;
  }
}
