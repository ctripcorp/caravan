package com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by marsqing on 21/03/2017.
 */
public class DecimalConverter {

  // code from http://stackoverflow.com/questions/9258313/conversion-bigdecimal-java-to-c-like-decimal

  public static byte[] bigDecimalToNetDecimal(BigDecimal paramBigDecimal) throws IllegalArgumentException {
    // .Net Decimal target
    byte[] result = new byte[16];

    // Unscaled absolute value
    BigInteger unscaledInt = paramBigDecimal.abs().unscaledValue();
    // Scale
    int scale = paramBigDecimal.scale();
    int bitLength = unscaledInt.bitLength();

    while (bitLength > 96) {
      unscaledInt = unscaledInt.divide(BigInteger.TEN);
      scale--;
      bitLength = unscaledInt.bitLength();
    }

    // Byte array
    byte[] unscaledBytes = unscaledInt.toByteArray();
    int unscaledFirst = 0;
    if (unscaledBytes[0] == 0) {
      unscaledFirst = 1;
    }

    if (scale > 28 || scale < 0) {
        throw new IllegalArgumentException(String.format("BigDecimal %s with scale %s exceeds .Net Decimal range of [0, 28]", paramBigDecimal, scale));
    }
    result[1] = (byte) scale;

    // Copy unscaled value to bytes 8-15
    for (int pSource = unscaledBytes.length - 1, pTarget = 15; (pSource >= unscaledFirst)
        && (pTarget >= 4); pSource--, pTarget--) {
      result[pTarget] = unscaledBytes[pSource];
    }

    // Signum at byte 0
    if (paramBigDecimal.signum() < 0) {
      result[0] = -128;
    }

    return result;
  }

  public static BigDecimal netDecimalToBigDecimal(int signScale, byte[] paramNetDecimal) {
    byte scale = (byte) ((signScale & 0x01FE) >> 1);
    int signum = (paramNetDecimal[3] & 0x01) == 1 ? -1 : 1;
    byte[] magnitude = new byte[12];
    for (int ptr = 0; ptr < 12; ptr++) {
      magnitude[ptr] = paramNetDecimal[ptr + 4];
    }
    BigInteger unscaledInt = new BigInteger(signum, magnitude);

    return new BigDecimal(unscaledInt, scale);
  }

}
