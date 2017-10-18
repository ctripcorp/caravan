package com.ctrip.soa.caravan.protobuf.v2;

import com.ctrip.soa.framework.soa.testservice.v1.BasicTypes1;
import java.util.Map;

/**
 * Created by marsqing on 20/04/2017.
 */
public class Pojo8 {

  private Map<Embed, BasicTypes1> map;

  public Map<Embed, BasicTypes1> getMap() {
    return map;
  }

  public void setMap(Map<Embed, BasicTypes1> map) {
    this.map = map;
  }

}
