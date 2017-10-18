package com.ctrip.soa.caravan.util.serializer.xml;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class XmlFilterInputStream extends FilterInputStream {

    private final int AMPERSAND_VALUE = (int)'&';
    private final int SHARP_VALUE = (int)'#';
    private final int NO_NEED_VALUE = -1;
    private final int[] SPECIAL_CHARS = new int[] { (int)'a', (int)'m', (int)'p', (int)';', (int)'#' };
    
    private boolean justReadAmpersand;
    private int lookAheadIndex = NO_NEED_VALUE;

    public XmlFilterInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        if (lookAheadIndex != NO_NEED_VALUE) {
            if (lookAheadIndex < SPECIAL_CHARS.length)
                return SPECIAL_CHARS[lookAheadIndex++];

            lookAheadIndex = NO_NEED_VALUE;
            justReadAmpersand = false;
        }

        int c = in.read();
        if (c == -1)
            return -1;

        if (justReadAmpersand) {
            if (c == SHARP_VALUE) {
                c = SPECIAL_CHARS[0];
                lookAheadIndex = 1;
            }
        }

        justReadAmpersand = c == AMPERSAND_VALUE;
        return c;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (off < 0 || len < 0 || len > b.length - off)
            throw new IndexOutOfBoundsException();

        if (len == 0)
            return 0;

        int c = read();
        if (c == -1)
            return -1;

        b[off] = (byte)c;
        int i = 1;
        try {
            for (; i < len ; i++) {
                c = read();
                if (c == -1)
                    break;

                b[off + i] = (byte)c;
            }
        } catch (IOException ex) {
        }
        return i;
    }
}
