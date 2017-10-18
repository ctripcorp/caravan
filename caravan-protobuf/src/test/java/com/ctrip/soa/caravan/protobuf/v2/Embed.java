package com.ctrip.soa.caravan.protobuf.v2;

/**
 * Created by marsqing on 20/04/2017.
 */
public class Embed {

  public Embed(int i) {
    this.i = i;
  }

  public Embed() {
  }

  private int i;

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Embed embed = (Embed) o;

    return i == embed.i;
  }

  @Override
  public int hashCode() {
    return i;
  }
}
