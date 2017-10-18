package com.ctrip.soa.framework.soa.testservice.v1;

/**
 * Created by marsqing on 22/03/2017.
 */
public class Level1 {

  private int value;

  private Level2 level2;

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Level2 getLevel2() {
    return level2;
  }

  public void setLevel2(Level2 level2) {
    this.level2 = level2;
  }
}
