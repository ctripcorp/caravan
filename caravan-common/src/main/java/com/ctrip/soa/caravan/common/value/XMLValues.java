package com.ctrip.soa.caravan.common.value;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@SuppressWarnings("unchecked")
public final class XMLValues {

    private static final Logger _logger = LoggerFactory.getLogger(XMLValues.class);

    private static DatatypeFactory _datatypeFactory;
    private static Class<XMLGregorianCalendar> _xmlGregorianCalendarType;

    static {
        try {
            _datatypeFactory = DatatypeFactory.newInstance();
            _logger.info("Default DatatypeFactory impl class: " + _datatypeFactory.getClass());

            _xmlGregorianCalendarType = (Class<XMLGregorianCalendar>) _datatypeFactory.newXMLGregorianCalendar().getClass();
            _logger.info("Default XMLGregorianCalendar impl class: " + _xmlGregorianCalendarType);
        } catch (Throwable ex) {
            _logger.error("Failed to init XML datatypes", ex);
        }
    }

    public static Class<XMLGregorianCalendar> xmlGregorianCalendarType() {
        NullArgumentChecker.DEFAULT.check(_xmlGregorianCalendarType, "xmlGregorianCalendarType");
        return _xmlGregorianCalendarType;
    }

    public static DatatypeFactory datatypeFactory() {
        NullArgumentChecker.DEFAULT.check(_datatypeFactory, "datatypeFactory");
        return _datatypeFactory;
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        return toXMLGregorianCalendar(DateValues.toGregorianCalendar(date));
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(GregorianCalendar calendar) {
        return datatypeFactory().newXMLGregorianCalendar(calendar);
    }

    public static Date toDate(XMLGregorianCalendar calendar) {
        return DateValues.toDate(toGregorianCalendar(calendar));
    }

    public static GregorianCalendar toGregorianCalendar(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar();
    }

    private XMLValues() {

    }

}
