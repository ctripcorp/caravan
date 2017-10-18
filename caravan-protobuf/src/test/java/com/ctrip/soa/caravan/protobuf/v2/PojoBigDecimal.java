package com.ctrip.soa.caravan.protobuf.v2;

import java.math.BigDecimal;

/**
 * Created by marsqing on 15/03/2017.
 */
public class PojoBigDecimal {

  public static class Embeded {

    private int pInt;

    public int getpInt() {
      return pInt;
    }

    public void setpInt(int pInt) {
      this.pInt = pInt;
    }
  }

  public enum Enum {
    Enum1,
    Enum2
  }
  // unsupported pb2 type
  //  private char pChar;
//  private short pShort;

  //  private byte pByte;
//  private int pInt;
//  private long pLong;
//  private float pFloat;
//  private double pDouble;
//  private boolean pBoolean;
//  private String pString;
//  private String[] pStringArray;
//  private List<String> pListString;
  private BigDecimal pBigDecimal;
  //  private GregorianCalendar pGregorianCalendar;
//  private Enum pEnum;
//  private Class<?> pClass;
//  private Embeded pEmbeded;

  //  public byte getpByte() {
//    return pByte;
//  }
//
//  public void setpByte(byte pByte) {
//    this.pByte = pByte;
//  }
//
//  public int getpInt() {
//    return pInt;
//  }
//
//  public void setpInt(int pInt) {
//    this.pInt = pInt;
//  }
//
//  public long getpLong() {
//    return pLong;
//  }
//
//  public void setpLong(long pLong) {
//    this.pLong = pLong;
//  }
//
//  public float getpFloat() {
//    return pFloat;
//  }
//
//  public void setpFloat(float pFloat) {
//    this.pFloat = pFloat;
//  }
//
//  public double getpDouble() {
//    return pDouble;
//  }
//
//  public void setpDouble(double pDouble) {
//    this.pDouble = pDouble;
//  }
//
//  public boolean ispBoolean() {
//    return pBoolean;
//  }
//
//  public void setpBoolean(boolean pBoolean) {
//    this.pBoolean = pBoolean;
//  }
//
//  public String getpString() {
//    return pString;
//  }
//
//  public void setpString(String pString) {
//    this.pString = pString;
//  }
//
//  public String[] getpStringArray() {
//    return pStringArray;
//  }
//
//  public void setpStringArray(String[] pStringArray) {
//    this.pStringArray = pStringArray;
//  }
//
//  public List<String> getpListString() {
//    return pListString;
//  }
//
//  public void setpListString(List<String> pListString) {
//    this.pListString = pListString;
//  }

  public BigDecimal getpBigDecimal() {
    return pBigDecimal;
  }

  public void setpBigDecimal(BigDecimal pBigDecimal) {
    this.pBigDecimal = pBigDecimal;
  }

//  public Embeded getpEmbeded() {
//    return pEmbeded;
//  }
//
//  public void setpEmbeded(Embeded pEmbeded) {
//    this.pEmbeded = pEmbeded;
//  }
  }
