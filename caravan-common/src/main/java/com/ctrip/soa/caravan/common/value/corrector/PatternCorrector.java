package com.ctrip.soa.caravan.common.value.corrector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.google.common.base.Strings;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PatternCorrector implements ValueCorrector<String> {

    private Pattern _pattern;

    public PatternCorrector(Pattern pattern) {
        NullArgumentChecker.DEFAULT.check(pattern, "pattern");
        _pattern = pattern;
    }

    @Override
    public String correct(String value) {
        if (Strings.isNullOrEmpty(value))
            return null;
        Matcher matcher = _pattern.matcher(value);
        if (matcher.matches())
            return value;
        else
            return null;
    }
}