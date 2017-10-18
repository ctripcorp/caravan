package com.ctrip.soa.caravan.protobuf.v2;

/**
 * Created by marsqing on 16/03/2017.
 */
public class Pojo2 {

  public static class Embed {

    private int a;
    private int b;

    public int getA() {
      return a;
    }

    public void setA(int a) {
      this.a = a;
    }

    public int getB() {
      return b;
    }

    public void setB(int b) {
      this.b = b;
    }
  }

  private int i;

  private Embed pe;

  private int j;

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public Embed getPe() {
    return pe;
  }

  public void setPe(Embed pe) {
    this.pe = pe;
  }

  public int getJ() {
    return j;
  }

  public void setJ(int j) {
    this.j = j;
  }
}
