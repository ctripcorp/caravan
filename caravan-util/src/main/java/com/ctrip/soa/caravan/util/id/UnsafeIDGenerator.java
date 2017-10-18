package com.ctrip.soa.caravan.util.id;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class UnsafeIDGenerator {

    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final String TIME_FORMAT = "yyMMddHHmmssSSS";

    public static final int TIME_BASED_RANDOM_MIN_LENGTH = TIME_FORMAT.length() + 3;

    private UnsafeIDGenerator() {

    }

    public static String random(int length) {
        if (length <= 0)
            return StringValues.EMPTY;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[ThreadLocalRandom.current().nextInt(CHARS.length)]);
        }

        return sb.toString();
    }

    public static String timeBasedRandom(int length) {
        String date = DateValues.toString(new Date(), TIME_FORMAT, UTC_TIME_ZONE);
        if (length < TIME_BASED_RANDOM_MIN_LENGTH)
            length = TIME_BASED_RANDOM_MIN_LENGTH;
        return date + random(length - TIME_FORMAT.length());
    }

}
