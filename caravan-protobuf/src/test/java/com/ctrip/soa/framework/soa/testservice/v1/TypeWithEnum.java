package com.ctrip.soa.framework.soa.testservice.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by marsqing on 22/03/2017.
 */
public class TypeWithEnum {

  @JsonProperty("Type1")
  private EnumType1 Type1;

  private EnumType2 type2;

  private EmbedType1 embed;

  public EnumType1 getType1() {
    return Type1;
  }

  public void setType1(EnumType1 type1) {
    this.Type1 = type1;
  }

  public EnumType2 getType2() {
    return type2;
  }

  public void setType2(EnumType2 type2) {
    this.type2 = type2;
  }

  public EmbedType1 getEmbed() {
    return embed;
  }

  public void setEmbed(EmbedType1 embed) {
    this.embed = embed;
  }
}
