package com.ctrip.soa.caravan.common.value.corrector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PatternCorrectorTest {

    @Test
    public void ipTest() {
        PatternCorrector pc = new PatternCorrector(Patterns.IP_ADDRESS);
        assertNotNull(pc.correct("10.10.199.255"));
        assertNull(pc.correct("10.10.199.256"));
        assertNull(pc.correct("10.10.199.*"));
        assertNull(pc.correct("10.10.19"));
        assertNull(pc.correct("1.10.19.test"));
        assertNull(pc.correct(null));
        assertNull(pc.correct(""));
        assertNull(pc.correct("   "));
    }

    @Test
    public void mailTest() {
        PatternCorrector pc = new PatternCorrector(Patterns.MAIL_ADDRESS);
        assertNotNull(pc.correct("chennan@ctrip.com"));
        assertNull(pc.correct("chennan"));
        assertNull(pc.correct("@"));
        assertNull(pc.correct("ctrip.com"));
        assertNull(pc.correct("@ctrip.com"));
        assertNull(pc.correct(null));
        assertNull(pc.correct(""));
        assertNull(pc.correct("   "));
    }
}
