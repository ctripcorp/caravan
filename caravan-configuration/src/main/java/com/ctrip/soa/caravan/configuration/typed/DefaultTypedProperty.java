package com.ctrip.soa.caravan.configuration.typed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.PropertyWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedProperty<T> extends PropertyWrapper implements TypedProperty<T> {

    private static Logger _logger = LoggerFactory.getLogger(DefaultTypedProperty.class);

    private ValueParser<T> _valueParser;

    public DefaultTypedProperty(Property property, ValueParser<T> valueParser) {
        super(property);
        NullArgumentChecker.DEFAULT.check(valueParser, "valueParser");
        _valueParser = valueParser;
    }

    @Override
    public T typedValue() {
        String value = value();
        if (StringValues.isNullOrWhitespace(value))
            return null;

        T typedValue = null;
        try {
            typedValue = _valueParser.parse(value.trim());
        } catch (Throwable ex) {
            _logger.error(String.format("%s cannot parse the string value, key=%s, value=%s", getClass().getName(),
                    key(), value));
        }

        return typedValue;
    }

    @Override
    public String toString() {
        return String.format("{ key: %s, value: %s, typedValue: %s }", key(), value(), typedValue());
    }

}
