package com.ctrip.soa.caravan.ribbon.util;

import com.ctrip.soa.caravan.common.value.MapValues;
import org.slf4j.Logger;

import java.util.Map;
/**
 * Created by w.jian on 2016/8/18.
 */
public class LogUtil {

    public static String messageWithTag(String message, Map<String, String> tags) {
        if (MapValues.isNullOrEmpty(tags))
            return message;

        StringBuilder builder = new StringBuilder("[[");
        for(Map.Entry<String, String> entry : tags.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]]");
        return builder.append(message).toString();
    }

    public static void error(Logger logger, String message, Map<String, String> tags) {
        logger.error(messageWithTag(message, tags));
    }

    public static void error(Logger logger, String message, Throwable t, Map<String, String> tags) {
        logger.error(messageWithTag(message, tags), t);
    }

    public static void warn(Logger logger, String message, Map<String, String> tags) {
        logger.warn(messageWithTag(message, tags));
    }

    public static void warn(Logger logger, String message, Throwable t, Map<String, String> tags) {
        logger.warn(messageWithTag(message, tags), t);
    }

    public static void info(Logger logger, String message, Map<String, String> tags) {
        logger.info(messageWithTag(message, tags));
    }

    public static void info(Logger logger, String message, Throwable t, Map<String, String> tags) {
        logger.info(messageWithTag(message, tags), t);
    }

    public static void debug(Logger logger, String message, Map<String, String> tags) {
        logger.debug(messageWithTag(message, tags));
    }

    public static void debug(Logger logger, String message, Throwable t, Map<String, String> tags) {
        logger.debug(messageWithTag(message, tags), t);
    }
}
