package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by marsqing on 21/03/2017.
 */
@JsonPropertyOrder({"value", "scale"})
public class DotNetTimeRelatedProtobuf {

  private long value;
  private int scale;

  public DotNetTimeRelatedProtobuf() {
  }

  public DotNetTimeRelatedProtobuf(long value, int scale) {
    this.value = value;
    this.scale = scale;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }
}
