package com.ctrip.soa.caravan.common.value;

import java.util.Arrays;
import java.util.Collection;

import com.ctrip.soa.caravan.common.collect.KeyValuePair;
import com.google.common.base.Strings;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class StringValues {

    public static final String EMPTY = "";
    public static final String NULL = "null";

    private StringValues() {

    }

    public static String toString(Object value) {
        if (value == null)
            return null;

        return value.toString();
    }

    public static String toLowerCase(String value) {
        if (value == null)
            return null;

        return value.toLowerCase();
    }

    public static String toUpperCase(String value) {
        if (value == null)
            return null;

        return value.toUpperCase();
    }

    public static boolean isNullOrWhitespace(String value) {
        if (value == null)
            return true;

        return trim(value).isEmpty();
    }

    public static String intern(String value) {
        return value == null ? null : value.intern();
    }

    public static String trim(String s, char... chars) {
        s = trimStart(s, chars);
        s = trimEnd(s, chars);
        return s;
    }

    public static String trimEnd(String s, char... chars) {
        if (s == null || chars == null)
            return s;

        Arrays.sort(chars);

        int index;
        for (index = s.length() - 1; index >= 0; index--) {
            char c = s.charAt(index);
            if (Character.isWhitespace(c))
                continue;

            int i = Arrays.binarySearch(chars, c);
            if (i >= 0)
                continue;

            break;
        }

        if (index == s.length() - 1)
            return s;

        return index < 0 ? StringValues.EMPTY : s.substring(0, index + 1);
    }

    public static String trimStart(String s, char... chars) {
        if (s == null || chars == null)
            return s;

        Arrays.sort(chars);

        int index;
        for (index = 0; index < s.length(); index++) {
            char c = s.charAt(index);
            if (Character.isWhitespace(c))
                continue;

            int i = Arrays.binarySearch(chars, c);
            if (i >= 0)
                continue;

            break;
        }

        if (index == 0)
            return s;

        return index == s.length() ? StringValues.EMPTY : s.substring(index);
    }

    public static String trim(String s, String trimmed) {
        if (s == null || trimmed == null)
            return s;

        trimmed = trim(trimmed);
        if (Strings.isNullOrEmpty(trimmed))
            return trim(s);

        s = trimStartInternal(s, trimmed);

        s = trimEndInternal(s, trimmed);

        return s;
    }

    public static String trimStart(String s, String trimmed) {
        if (s == null || trimmed == null)
            return s;

        trimmed = trim(trimmed);
        if (Strings.isNullOrEmpty(trimmed))
            return trim(s);

        return trimStartInternal(s, trimmed);
    }

    private static String trimStartInternal(String s, String trimmed) {
        s = trim(s);
        if (Strings.isNullOrEmpty(s))
            return s;

        if (trimmed.length() > s.length())
            return s;

        if (!s.startsWith(trimmed))
            return s;

        if (s.length() == trimmed.length())
            return EMPTY;

        s = s.substring(trimmed.length());
        return trimStart(s, trimmed);
    }

    public static String trimEnd(String s, String trimmed) {
        if (s == null || trimmed == null)
            return s;

        trimmed = trim(trimmed);
        if (Strings.isNullOrEmpty(trimmed))
            return trim(s);

        return trimEndInternal(s, trimmed);
    }

    private static String trimEndInternal(String s, String trimmed) {
        s = trim(s);
        if (Strings.isNullOrEmpty(s))
            return s;

        if (trimmed.length() > s.length())
            return s;

        if (!s.endsWith(trimmed))
            return s;

        if (s.length() == trimmed.length())
            return EMPTY;

        s = s.substring(0, s.length() - trimmed.length());
        return trimEnd(s, trimmed);
    }

    public static String concatPathParts(String... pathParts) {
        if (pathParts == null)
            return null;

        String url = null;
        for (String item : pathParts) {
            if (StringValues.isNullOrWhitespace(item))
                continue;
            item = item.trim();

            if (url == null)
                url = item;
            else
                url += (url.endsWith("/") ? StringValues.EMPTY : "/") + Strings.nullToEmpty(trimStart(item, '/'));
        }

        return url;
    }

    public static KeyValuePair<String, String> toKeyValuePair(String s) {
        return toKeyValuePair(s, ":");
    }

    public static KeyValuePair<String, String> toKeyValuePair(String s, String separator) {
        if (isNullOrWhitespace(s) || isNullOrWhitespace(separator))
            return null;

        s = s.trim();
        separator = separator.trim();

        String[] parts = s.split(separator, 2);
        if (parts.length != 2)
            return null;

        if (isNullOrWhitespace(parts[0]) || isNullOrWhitespace(parts[1]))
            return null;

        return new KeyValuePair<String, String>(parts[0].trim(), parts[1].trim());
    }

    public static <T> String join(T[] data, char separator) {
        return join(data, String.valueOf(separator));
    }

    public static <T> String join(T[] data, String separator) {
        if (data == null)
            return null;

        return join(Arrays.asList(data), separator);
    }

    public static String join(Collection<?> data, char separator) {
        return join(data, String.valueOf(separator));
    }

    public static String join(Collection<?> data, String separator) {
        if (CollectionValues.isNullOrEmpty(data))
            return null;

        StringBuilder sb = new StringBuilder();
        for (Object item : data) {
            if (item == null)
                continue;

            sb.append(item);
            if (separator != null)
                sb.append(separator);
        }

        if (sb.length() == 0)
            return null;

        if (separator == null)
            return sb.toString();

        return sb.substring(0, sb.length() - separator.length());
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == s2)
            return true;

        if (s1 == null || s2 == null)
            return false;

        return s1.equals(s2);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == s2)
            return true;

        if (s1 == null || s2 == null)
            return false;

        return s1.equalsIgnoreCase(s2);
    }
}
