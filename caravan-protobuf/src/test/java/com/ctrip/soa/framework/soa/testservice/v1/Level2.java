package com.ctrip.soa.framework.soa.testservice.v1;

/**
 * Created by marsqing on 22/03/2017.
 */
public class Level2 {

  private int value;
  private Level3 level3;

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Level3 getLevel3() {
    return level3;
  }

  public void setLevel3(Level3 level3) {
    this.level3 = level3;
  }
}
