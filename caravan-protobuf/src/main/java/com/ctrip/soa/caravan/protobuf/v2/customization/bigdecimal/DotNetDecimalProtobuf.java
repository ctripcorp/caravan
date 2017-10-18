package com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by marsqing on 21/03/2017.
 */
@JsonPropertyOrder({"low", "high", "signScale"})
public class DotNetDecimalProtobuf {

  private long low;
  private int high;
  private int signScale;

  public DotNetDecimalProtobuf() {
  }

  public DotNetDecimalProtobuf(long low, int high, int signScale) {
    this.low = low;
    this.high = high;
    this.signScale = signScale;
  }

  public long getLow() {
    return low;
  }

  public void setLow(long low) {
    this.low = low;
  }

  public int getHigh() {
    return high;
  }

  public void setHigh(int high) {
    this.high = high;
  }

  public int getSignScale() {
    return signScale;
  }

  public void setSignScale(int signScale) {
    this.signScale = signScale;
  }
}
